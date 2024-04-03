import java.awt.Graphics;

import javax.swing.*;

class HelloComponent1 extends JComponent {

  @Override
  public void paintComponent(Graphics g) {
    g.drawString("hello world", 125, 94);
  }
}

public class Example1 {
  public static void main(String[] args) {
    System.out.println("hello world");
    JFrame frame = new JFrame("hello java");
    frame.setSize(300, 150);

    // JLabel label = new JLabel("hello java", JLabel.CENTER);
    // frame.add(label);

    frame.add(new HelloComponent1());
    frame.setVisible(true);
  }
}
