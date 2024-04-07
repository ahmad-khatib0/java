package ch04.examples;

public class IfDemo {
  public static void main(String args[]) {
    int i = 0;
    if (i == 0)
      System.out.println("i is still zero");
    else
      System.out.println("i is most definitely not zero");

    int j = 0;
    if (j < 0) {
      System.out.println("j is less than 0! Resetting.");
      j = 0;
    } else {
      System.out.println("j is positive or 0. Continuing.");
    }
  }
}
