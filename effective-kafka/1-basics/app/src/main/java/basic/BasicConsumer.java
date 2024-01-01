package basic;

import static java.lang.System.*;

import java.time.*;
import java.util.*;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.*;

public class BasicConsumer {

  public static void main(String[] args) throws InterruptedException {
    final var topic = "getting-started";

    final Map<String, Object> config = Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9002",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
        ConsumerConfig.GROUP_ID_CONFIG, "basic-consumer-sample",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
        // The auto offset reset configuration stipulates what happens when the consumer
        // subscribes to the topic for the first time. In this case, we would
        // like the consumer’s offset to be reset to the low-water mark for every
        // affected partition, meaning that the consumer will get any backlogged records
        // that existed prior to the creation of the group in Kafka. The default setting
        // is latest, meaning the consumer will not read any prior records.
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false
    // the auto-commit setting is disabled, meaning that the application will commit
    // offsets at its discretion. The default setting is to enable auto-commit with
    // a minimum interval of five seconds.
    );

    // use of a try-with-resources block to ensure the client
    // is closed after it leaves scope
    try (var consumer = new KafkaConsumer<String, String>(config)) {
      consumer.subscribe(Set.of(topic));

      while (true) {
        final var records = consumer.poll(Duration.ofMillis(100));
        for (var record : records) {
          out.format("Got record with value %s%n", record.value());
        }
        consumer.commitAsync();
      }

    }
  }

}
