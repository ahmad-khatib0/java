package effectivekafka.sasl;

import static java.lang.System.*;

import java.time.*;
import java.util.*;

import org.apache.kafka.clients.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.config.*;
import org.apache.kafka.common.security.scram.*;
import org.apache.kafka.common.serialization.*;

public final class SaslSslConsumerSample {
  public static void main(String[] args) {
    final var topic = "getting-started";

    final var loginModuleClass = ScramLoginModule.class.getName();
    final var saslJaasConfig = loginModuleClass
        + " required\n"
        + "username=\"alice\"\n"
        + "password=\"alice-secret\";";

    final var config = new HashMap<String, Object>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
    config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
    config.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "https");
    config.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "client.truststore.jks");
    config.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "secret");
    config.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-512");
    config.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "basic-consumer-sample");
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

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
