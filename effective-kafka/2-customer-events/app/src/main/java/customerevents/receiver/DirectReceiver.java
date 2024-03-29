package customerevents.receiver;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.obsidiandynamics.worker.WorkerOptions;
import com.obsidiandynamics.worker.WorkerThread;

public final class DirectReceiver extends AbstractReceiver {
  private final WorkerThread pollingThread;

  private final Consumer<String, CustomerPayloadOrError> consumer;

  private final Duration pollTimeout;

  public DirectReceiver(Map<String, Object> consumerConfig,
      String topic,
      Duration pollTimeout) {
    this.pollTimeout = pollTimeout;

    final var mergedConfig = new HashMap<String, Object>();
    mergedConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    mergedConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomerPayloadDeserializer.class.getName());
    mergedConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    mergedConfig.putAll(consumerConfig);
    consumer = new KafkaConsumer<>(mergedConfig);
    consumer.subscribe(Set.of(topic));

    pollingThread = WorkerThread.builder()
        .withOptions(new WorkerOptions().daemon().withName(DirectReceiver.class, "poller"))
        .onCycle(this::onPollCycle)
        .build();
  }

  @Override
  public void start() {
    pollingThread.start();
  }

  // fetch records from Kafka, construct a corresponding ReceiveEvent, and
  // dispatch // the event to all registered listeners. Once all records in the
  // batch have been dispatched, the commitAsync() method of the consumer is
  // invoked, which will have the effect of asynchronously committing the
  // offsets for all records fetched in the last call to poll(). Being
  // asynchronous, the client will dispatch the request in a background thread,
  // not waiting for the commit response from the brokers; the responses will
  // arrive at an indeterminate time in the future, after commitAsync() returns
  private void onPollCycle(WorkerThread t) throws InterruptedException {
    final ConsumerRecords<String, CustomerPayloadOrError> records;

    try {
      records = consumer.poll(pollTimeout);
    } catch (InterruptException e) {
      throw new InterruptedException("Interrupted during poll");
    }

    if (!records.isEmpty()) {
      for (var record : records) {
        final var payloadOrError = record.value();
        final var event = new ReceiveEvent(
            payloadOrError.getPayload(),
            payloadOrError.getError(),
            record,
            payloadOrError.getEncodedValue());
        fire(event);
      }
      consumer.commitAsync();
    }
  }

  @Override
  public void close() {
    pollingThread.terminate().joinSilently();
    consumer.close();
  }
}
