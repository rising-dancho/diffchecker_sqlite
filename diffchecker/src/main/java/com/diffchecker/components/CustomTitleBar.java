package com.diffchecker.components;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class CustomTitleBar extends JPanel {

  private final JFrame frame;
  private final JLabel titleLabel;
  private final JButton closeButton;
  private final JButton minimizeButton;
  private final JButton maximizeButton;
  private final JPanel controlPanel;

  private final Color FONT_COLOR = new Color(0xd6d6d6);

  private Dimension previousSize;

  // package‑level config
  private static String PACKAGE_NAME;

  public CustomTitleBar(JFrame frame,
      String title,
      String packageName,
      String iconPath,
      Color background,
      int height) {

    // ── store static config so createButton() can access it
    PACKAGE_NAME = packageName;

    this.frame = frame;
    this.previousSize = frame.getSize();

    setLayout(new BorderLayout());
    setBackground(background);

    // fixed height; allow width to stretch but avoid Integer.MAX_VALUE weirdness
    Dimension fixedSize = new Dimension(new Dimension(Integer.MAX_VALUE, 33));
    setPreferredSize(fixedSize);
    setMaximumSize(fixedSize);
    setMinimumSize(new Dimension(0, height));

    // debug: red border already added from Main, but we can keep this too
    // setBorder(BorderFactory.createLineBorder(Color.RED));

    // ── Title label (optional icon) --------------------------------------------
    titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    titleLabel.setForeground(FONT_COLOR);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    titleLabel.setVerticalAlignment(SwingConstants.CENTER);

    if (iconPath != null) {
      ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
      titleLabel.setIcon(icon);
      titleLabel.setIconTextGap(10);
    }

    // ── Control buttons ---------------------------------------------------------
    controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
    controlPanel.setOpaque(false);
    controlPanel.setOpaque(false);

    minimizeButton = createButton("minimize_def.png", "minimize_hover.png",
        e -> frame.setState(JFrame.ICONIFIED));

    maximizeButton = createButton("maximize_def.png", "maximize_hover.png",
        e -> toggleMaximize());

    closeButton = createButton("close_def.png", "close_hover.png",
        e -> System.exit(0));

    controlPanel.add(minimizeButton);
    controlPanel.add(maximizeButton);
    controlPanel.add(closeButton);

    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.setOpaque(false);
    centerPanel.add(titleLabel, BorderLayout.WEST);

    add(centerPanel, BorderLayout.CENTER);
    add(controlPanel, BorderLayout.EAST);

    // ── Enable dragging ---------------------------------------------------------
    new TitlebarMover(
        frame,
        this,
        this::toggleMaximize,
        () -> {
          updateMaximizeIcon();
          previousSize = frame.getSize(); // <-- capture restored size again
        });
  }

  // ------------------------------------------------------------------ utilities
  private JButton createButton(String defIcon,
      String hoverIcon,
      ActionListener action) {

    JButton b = new JButton(new ImageIcon(
        getClass().getResource("/" + PACKAGE_NAME + "/images/" + defIcon)));
    int size = 32; // or 32 depending on your icons
    b.setPreferredSize(new Dimension(size, size));
    b.setBorderPainted(false);
    b.setFocusPainted(false);
    b.setContentAreaFilled(false);
    b.setToolTipText(defIcon.split("_")[0]); // quick tooltip

    b.addActionListener(action);
    b.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        b.setIcon(new ImageIcon(getClass().getResource(
            "/" + PACKAGE_NAME + "/images/" + hoverIcon)));
      }

      @Override
      public void mouseExited(MouseEvent e) {
        b.setIcon(new ImageIcon(getClass().getResource(
            "/" + PACKAGE_NAME + "/images/" + defIcon)));
      }
    });
    return b;
  }

  // ---------------------------------------------------------------- maximize
  // logic
  private void toggleMaximize() {
    if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
      frame.setExtendedState(JFrame.NORMAL);
      frame.setSize(previousSize);
    } else {
      previousSize = frame.getSize();
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    updateMaximizeIcon();
  }

  private void updateMaximizeIcon() {
    boolean maximized = (frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;

    String def = maximized ? "collapse_def.png" : "maximize_def.png";
    String hover = maximized ? "collapse_hover.png" : "maximize_hover.png";
    maximizeButton.setIcon(new ImageIcon(
        getClass().getResource("/" + PACKAGE_NAME + "/images/" + def)));

    // refresh hover behaviour
    for (MouseListener ml : maximizeButton.getMouseListeners()) {
      if (ml instanceof MouseAdapter)
        maximizeButton.removeMouseListener(ml);
    }
    maximizeButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        maximizeButton.setIcon(new ImageIcon(getClass().getResource(
            "/" + PACKAGE_NAME + "/images/" + hover)));
      }

      @Override
      public void mouseExited(MouseEvent e) {
        maximizeButton.setIcon(new ImageIcon(getClass().getResource(
            "/" + PACKAGE_NAME + "/images/" + def)));
      }
    });
  }
}