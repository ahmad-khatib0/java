package customerevents.receiver;

@FunctionalInterface
public interface EventListener {
  void onEvent(ReceiveEvent event);
}
