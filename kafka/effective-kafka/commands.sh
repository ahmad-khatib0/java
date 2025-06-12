#!/bin/bash

#──────────────────────────────────────────────────────────────────────
# Zookeeper Commands
#──────────────────────────────────────────────────────────────────────

# start zookeeper (-daemon to start in background) When launched as a daemon, the standard
# output of the ZooKeeper process is piped to $KAFKA_HOME/logs/zookeeper.out
$KAFKA_HOME/bin/zookeeper-server-start.sh -daemon $KAFKA_HOME/config/zookeeper.properties

# To stop a daemon ZooKeeper process
$KAFKA_HOME/bin/zookeeper-server-stop.sh

#──────────────────────────────────────────────────────────────────────
# Kafka Server Commands
#──────────────────────────────────────────────────────────────────────

# start kafka (Kafka standard output logs are written to $KAFKA_HOME/logs/kafkaServer.out )
$KAFKA_HOME/bin/kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties

# To stop a daemon Kafka process, run
$KAFKA_HOME/bin/kafka-server-stop.sh

# run Kafdrop
java -jar kafdrop-VERSION.jar --kafka.brokerConnect=localhost:9092

#──────────────────────────────────────────────────────────────────────
# Topic Management
#──────────────────────────────────────────────────────────────────────

# Creating a topic
$KAFKA_HOME/bin/kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --partitions 3 --replication-factor 1 --topic getting-started

# The kafka-topics.sh tool can be used to list topics:
$KAFKA_HOME/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list --exclude-internal

# get more detailed information about a specific topic:
$KAFKA_HOME/bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic getting-started

# delete a topic
$KAFKA_HOME/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic getting-started --delete

#──────────────────────────────────────────────────────────────────────
# Producing and Consuming Messages
#──────────────────────────────────────────────────────────────────────

# Publishing records (Records are separated by newlines. The key and the value parts are
# delimited by colons, as indicated by the key.separator property )
$KAFKA_HOME/bin/kafka-console-producer.sh --broker-list localhost:9092 \
  --topic getting-started --property "parse.key=true" --property "key.separator=:"

# Consuming records
$KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
  --topic getting-started --group cli-consumer --from-beginning \
  --property "print.key=true" --property "key.separator=:"

#──────────────────────────────────────────────────────────────────────
# Record Management
#──────────────────────────────────────────────────────────────────────

# truncating the first record from getting-started:2, leaving records at offset 1 and newer intact:
cat <<EOF >/tmp/offsets.json
{
  "partitions": [
    {"topic": "getting-started", "partition": 2, "offset": 1}
  ],
  "version": 1
}
EOF
$KAFKA_HOME/bin/kafka-delete-records.sh --bootstrap-server localhost:9092 --offset-json-file /tmp/offsets.json

# (In an analogous manner, we can truncate the entire partition by specifying
# the current high-water mark in the offset attribute.)

#──────────────────────────────────────────────────────────────────────
# Consumer Group Management
#──────────────────────────────────────────────────────────────────────

# The kafka-consumer-groups.sh tool can be used to query Kafka for a list of consumer groups:
$KAFKA_HOME/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list

# display detailed state information about each consumer group
$KAFKA_HOME/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group cli-consumer --describe --all-topics

# describe all groups:
$KAFKA_HOME/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --all-groups --all-topics
# or with the --state that drills into the present state of the consumer group.
$KAFKA_HOME/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --all-groups --state

#──────────────────────────────────────────────────────────────────────
# Offset Management
#──────────────────────────────────────────────────────────────────────

# Rewinds the offsets for the consumer group cli-consumer to the low-water mark,
# using the --to-earliest flag — resulting in the forced reprocessing of all records when the consumer
# group reconnects. Alternatively, the --to-latest flag can be used to fast-forward the offsets to the
# high-water mark extremity, skipping all backlogged records. Resetting offsets is an offline operation;
# the operation will not proceed in the presence of a connected consumer:
$KAFKA_HOME/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
  --topic getting-started --group cli-consumer --reset-offsets --to-earliest --execute

# By default, passing the --reset-offsets flag will result in a dry run, whereby the tool will list the
# partitions that will be subject to a reset, the existing offsets, as well as the candidate offsets that will
# be assigned upon completion. This is equivalent of running the tool with the --dry-run flag, and is
# designed to protect the user from accidentally corrupting the consumer group's state. To enact the
# change, run the command with the --execute flag, as shown in the command above.

# In addition to resetting offsets for the entire topic, the reset operation can be performed selectively
# on a subset of the topic's partitions. This can be accomplished by passing in a list of partition numbers
# following the topic name, in the form <topic-name>:<first-partition>,<second-partition>,...,<N-th-partition>
# An example of this syntax is featured below. Also, rather than resetting the offset to a partition
# extremity, this example uses the --to-offset parameter to specify a numeric offset.
$KAFKA_HOME/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
  --topic getting-started:0,1 --group cli-consumer --reset-offsets --to-offset 2 --execute

# The next example uses Kafka's record time-stamping to locate an offset based on the given date-time
# value, quoted in ISO 8601 form. Specifically, the offsets will be reset to the earliest point in time that
# occurs at the specified timestamp or after it. This feature is convenient when one needs to wind the
# offsets back to a known point in time. When using the --to-datetime parameter, ensure that the
# offset is passed using the correct timezone; if unspecified, the timezone defaults to the Coordinated
# Universal Time (UTC), also known as Zulu time. In the example below, the timezone had to be
# adjusted to Australian Eastern Daylight Time (AEDT), eleven hours east of Zulu:
$KAFKA_HOME/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
  --topic getting-started:2 --group cli-consumer --reset-offsets --to-datetime 2020-01-27T14:35:54.528+11:00 --execute

# The final option offered by this tool is to shift the offsets by a fixed quantity n, using the
# --shift-by parameter. The magnitude of the shift may be a positive number — for a forward movement, or
# a negative number — to rewind the offsets. The extent of the shift is bounded by the partition
# extremities; the result of 'current offset' + n will be capped by the low-water and high-water marks.

# Another method of resetting the offsets is to delete the offsets altogether, shown in the example
# below. This is, in effect, a lazy form of reset — the assignment of new offsets does not occur until
# a consumer connects to the cluster. When this happens, the auto.offset.reset client property will
# stipulate which extremity the offset should be reset to — either the earliest offset or the latest.
$KAFKA_HOME/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
  --topic getting-started --group cli-consumer --delete-offsets

# Deleting a consumer group:
$KAFKA_HOME/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group cli-consumer --delete
