package basic;

import static java.lang.System.*;
import java.util.*;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.*;

public class BasicProducer {
  public static void main(String[] args) throws InterruptedException {

    final var topic = "getting-started";
    final Map<String, Object> config = Map.of(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
        ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    // With the enable.idempotence option set to true, the broker will maintain an
    // internal sequence number for each producer and partition pair, ensuring that
    // records are not processed in duplicate or out-of-order. So it’s good practice
    // to enable idempotence.

    try (var producer = new KafkaProducer<String, String>(config)) {
      while (true) {

        // (myKey) all records will appear on the same partition
        final var key = "myKey";
        final var value = new Date().toString();
        out.format("Publishing record with value %s%n", value);

        final Callback callback = (metadata, exception) -> {
          out.format("Published with metadata: %s, error: %s%n", metadata, exception);
        };

        // publish the record, handling the metadata in the callback
        //
        // There are two overloaded variations of send() method
        // 1. Future<RecordMetadata> send(ProducerRecord<K, V> record) : asynchronously
        // sends the record, returning a Future containing the record metadata.
        // 2. Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback
        // callback): asyn- chronously sends the record, invoking the given Callback
        // implementation when either the record has been successfully persisted on the
        // broker or an error has occurred. Our example uses this variant.
        producer.send(new ProducerRecord<>(topic, key, value), callback);

        // wait a second before publishing another
        Thread.sleep(1000);
      }

    }
  }
}
