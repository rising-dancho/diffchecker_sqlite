package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class JScroll_pane extends JFrame {
  public static void main(String[] args) {
    new JScroll_pane();
  }

  public JScroll_pane() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Content panel with BoxLayout (vertical stacking)
    JPanel contentPanel = new JPanel();
    contentPanel.setBackground(Color.WHITE);
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    // Sample long text
    String longText = "<html><body style='width:1000px;'>"
        + "<br><br>"
        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
        + "Nunc leo arcu, posuere at velit non, eleifend bibendum leo. "
        + "Vivamus a urna elit. Maecenas ornare felis elit, sed dapibus elit dapibus sit amet. "
        + "Pellentesque pretium leo eu mollis condimentum. Vivamus eu orci vehicula, suscipit ante eu, "
        + "posuere erat. Cras iaculis consectetur libero, sit amet mollis sem lobortis ullamcorper. "
        + "Donec gravida vitae justo ut cursus. Morbi ante mauris, placerat eget dui in, varius tincidunt arcu. "
        + "Ut maximus dui et tristique elementum. Vestibulum elit dolor, varius vel quam non, iaculis ultrices sapien. "
        + "Vestibulum ultricies accumsan sapien. Phasellus scelerisque sapien et tincidunt consectetur."
        + "<br><br>"
        + "</body></html>";

    // Add multiple long labels
    for (int i = 0; i < 20; i++) {
      JLabel label = new JLabel(longText);
      label.setOpaque(true);
      label.setBackground(Color.WHITE);
      label.setAlignmentX(LEFT_ALIGNMENT);
      // label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500)); // allow
      // stretching
      contentPanel.add(label);
    }

    // Scroll pane
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    add(scrollPane, BorderLayout.CENTER);
    setVisible(true);
  }
}
