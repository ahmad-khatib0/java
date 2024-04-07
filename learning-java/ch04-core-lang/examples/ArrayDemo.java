import java.awt.Component;
import java.util.Arrays;

import javax.swing.JLabel;

public class ArrayDemo {
  public static void main(String[] args) {

    int[] arrayOfInts; // preferred
    int[] arrayOfInts2; // spacing is optional
    int arrayOfInts3[]; // C-style, allowed
    //

    // Array Creation and Initialization
    int number = 10;
    arrayOfInts = new int[42];
    someStrings = new String[number + 2];

    // combine the steps of declaring and allocating the array:
    double[] someNumbers = new double[20];
    Component[] widgets = new Component[12];
    int[] grades = new int[30]; // filled with zeros
    String names[] = new String[42]; // filled with null
    char[] alphabet = new char[26]; // char[26] { '\000', '\000' ... , '\000' }
    //

    JLabel yesLabel = new JLabel("Yes");
    JLabel noLabel = new JLabel("No");

    // Java supports the C-style curly braces {} construct for creating an array
    // and initializing its elements:
    int[] primes = { 2, 3, 5, 7, 7 + 4 };
    String[] verbs = { "run", "jump", "hide" };
    JLabel[] choices = { yesLabel, noLabel };
    Object[] anything = { "run", yesLabel, new Date() };
    String[] musketeers = { "one", "two", "three" }; // String[3] { "one", "two", "three" }

    //
    // Using Arrays
    String[] tmpVar = new String[2 * names.length];
    System.arraycopy(names, 0, tmpVar, 0, names.length);
    names = tmpVar; // or using copyof | copyOfRange:

    byte[] bar = new byte[] { 1, 2, 3, 4, 5 };
    byte[] barCopy = Arrays.copyOf(bar, bar.length);
    byte[] expanded = Arrays.copyOf(bar, bar.length + 2); // the +2 filled with zeros
    byte[] firstThree = Arrays.copyOfRange(bar, 0, 3); // { 1, 2, 3 }
    byte[] lastThree = Arrays.copyOfRange(bar, 2, bar.length); // { 3, 4, 5 }
    byte[] plusTwo = Arrays.copyOfRange(bar, 2, bar.length + 2); // { 3, 4, 5, 0, 0 }

    // Multidimensional Arrays
    ChessPiece[][] chessBoard;
    chessBoard = new ChessPiece[8][8];
    // chessBoard[0][0] = new ChessPiece.Rook;
    // chessBoard[1][0] = new ChessPiece.Pawn;
    // chessBoard[0][1] = new ChessPiece.Knight;
    //

  }
}

class ChessPiece {
}
