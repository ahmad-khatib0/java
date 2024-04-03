import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
// NOTE: that importing java.awt.* doesn’t automatically import the event package. Imports
// are not recursive. Packages don’t really contain other packages, even if the hierarchi‐
// cal naming scheme would imply that they do.)

public class Example2 {
  public static void main(String[] args) {
    JFrame frame = new JFrame("HelloJava2");
    frame.add(new HelloComponent2("Hello, Java!"));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300, 300);
    frame.setVisible(true);
  }
}

class HelloComponent2 extends JComponent implements MouseMotionListener {

  String theMessage;
  int messageX = 125, messageY = 95;

  public HelloComponent2(String msg) {
    theMessage = msg;
    addMouseMotionListener(this);
  }

  public void paintComponent(Graphics g) {
    g.drawString(theMessage, messageX, messageY);
  }

  public void mouseDragged(MouseEvent e) {
    // Save the mouse coordinates and paint the message.
    messageX = e.getX();
    messageY = e.getY();
    repaint();
  }

  public void mouseMoved(MouseEvent e) {
  }
}
