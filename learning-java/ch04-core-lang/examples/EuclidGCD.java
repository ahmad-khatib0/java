package ch04.examples;

/**
 * A basic implementation of Euclid's greatest common denominator
 * algorithm.
 *
 * @see https://en.wikipedia.org/wiki/Algorithm
 */
public class EuclidGCD {
  public static void main(String args[]) {
    int a = 2701;
    int b = 222;
    while (b != 0) {
      if (a > b) {
        a = a - b;
      } else {
        b = b - a;
      }
    }
    System.out.println("GCD is " + a);
  }
}
