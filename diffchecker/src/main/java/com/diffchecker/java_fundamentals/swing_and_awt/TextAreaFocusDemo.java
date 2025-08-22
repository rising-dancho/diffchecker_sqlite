package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TextAreaFocusDemo {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("TextArea Focus Demo");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(new MyPanel());
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    });
  }
}

class MyPanel extends JPanel {
  private boolean jt1IsActive = false;
  private boolean jt2IsActive = false;

  private final Color ACTIVE_BORDER_COLOR = Color.GREEN;
  private final Color EDITOR_BORDER_COLOR = Color.GRAY;

  public MyPanel() {
    // Make this panel focusable
    setFocusable(true);

    JTextArea jt1 = new JTextArea(5, 20);
    JTextArea jt2 = new JTextArea(5, 20);

    JScrollPane scroll1 = new JScrollPane(jt1);
    JScrollPane scroll2 = new JScrollPane(jt2);

    scroll1.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
    scroll2.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));

    setLayout(new GridLayout(3, 1, 10, 10));
    add(scroll1);
    add(scroll2);

    // Update borders based on focus
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("permanentFocusOwner", evt -> {
      Component focused = (Component) evt.getNewValue();

      if (SwingUtilities.isDescendingFrom(focused, jt1)) {
        jt1IsActive = true;
        jt2IsActive = false;
        scroll1.setBorder(BorderFactory.createLineBorder(ACTIVE_BORDER_COLOR, 2));
        scroll2.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR, 1));
      } else if (SwingUtilities.isDescendingFrom(focused, jt2)) {
        jt1IsActive = false;
        jt2IsActive = true;
        scroll1.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR, 1));
        scroll2.setBorder(BorderFactory.createLineBorder(ACTIVE_BORDER_COLOR, 2));
      } else {
        jt1IsActive = false;
        jt2IsActive = false;
        scroll1.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR, 1));
        scroll2.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR, 1));
      }
    });

    // Click on background panel to remove focus from textareas
    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        Component clickedComponent = SwingUtilities.getDeepestComponentAt(MyPanel.this, e.getX(), e.getY());

        if (!(SwingUtilities.isDescendingFrom(clickedComponent, jt1)
            || SwingUtilities.isDescendingFrom(clickedComponent, jt2))) {
          requestFocusInWindow(); // steal focus to hide caret
        }
      }
    });
  }
}
