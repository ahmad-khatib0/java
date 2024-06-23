package se.magnus.microservices.composite.product.services.tracing;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class ObservationUtil {

  private final ObservationRegistry registry;

  public ObservationUtil(ObservationRegistry registry) {
    this.registry = registry;
  }

  public <T> T observe(
      String observationName,
      String contextualName,
      String highCardinalityKey,
      String highCardinalityValue,
      // If the code doesn’t return any value, it can be specified as a Runnable
      // function; otherwise, it needs to be specified as a Supplier function.
      Supplier<T> supplier) {
    // createNotStarted to create a span
    return Observation.createNotStarted(observationName, registry)
        .contextualName(contextualName)
        .highCardinalityKeyValue(highCardinalityKey, highCardinalityValue)
        .observe(supplier);
  }

}
