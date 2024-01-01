package basic;

public class App {
  public String getGreeting() {
    return "Hello World!";
  }

  public static void main(String[] args) {
    new BasicProducer();
    new BasicConsumer();

    System.out.println(new App().getGreeting());
  }
}
