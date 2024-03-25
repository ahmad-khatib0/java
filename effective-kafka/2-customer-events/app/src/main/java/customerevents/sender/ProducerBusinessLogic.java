package customerevents.sender;

import java.util.*;

import customerevents.event.*;
import customerevents.sender.EventSender.*;

public final class ProducerBusinessLogic {
  private final EventSender sender;

  public ProducerBusinessLogic(EventSender sender) {
    this.sender = sender;
  }

  public void generateRandomEvents() throws SendException, InterruptedException {
    final var create = new CreateCustomer(UUID.randomUUID(), "Bob", "Brown");
    blockingSend(create);

    if (Math.random() > 0.5) {
      final var update = new UpdateCustomer(create.getId(), "Charlie", "Brown");
      blockingSend(update);
    }

    if (Math.random() > 0.5) {
      final var suspend = new SuspendCustomer(create.getId());
      blockingSend(suspend);

      if (Math.random() > 0.5) {
        final var reinstate = new ReinstateCustomer(create.getId());
        blockingSend(reinstate);
      }
    }
  }

  private void blockingSend(CustomerPayload payload) throws SendException, InterruptedException {
    System.out.format("Publishing %s%n", payload);
    sender.blockingSend(payload);
  }
}
