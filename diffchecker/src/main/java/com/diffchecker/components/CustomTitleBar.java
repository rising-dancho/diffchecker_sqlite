package com.diffchecker.components;

import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class CustomTitleBar extends JPanel {

  private final JFrame frame;
  private final JLabel titleLabel;
  private final JButton closeButton;
  private final JButton minimizeButton;
  private final JButton maximizeButton;
  private final JPanel controlPanel;

  private final Color FONT_COLOR = new Color(0xd6d6d6);

  private Dimension previousSize;

  // SYNTAX STYLES
  private static final Map<String, String> SYNTAX_STYLES = Map.ofEntries(
      Map.entry("None", SyntaxConstants.SYNTAX_STYLE_NONE),
      Map.entry("Java", SyntaxConstants.SYNTAX_STYLE_JAVA),
      Map.entry("Python", SyntaxConstants.SYNTAX_STYLE_PYTHON),
      Map.entry("JavaScript", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT),
      Map.entry("C", SyntaxConstants.SYNTAX_STYLE_C),
      Map.entry("C++", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS),
      Map.entry("C#", SyntaxConstants.SYNTAX_STYLE_CSHARP),
      Map.entry("HTML", SyntaxConstants.SYNTAX_STYLE_HTML),
      Map.entry("XML", SyntaxConstants.SYNTAX_STYLE_XML),
      Map.entry("SQL", SyntaxConstants.SYNTAX_STYLE_SQL),
      Map.entry("JSON", SyntaxConstants.SYNTAX_STYLE_JSON),
      Map.entry("YAML", SyntaxConstants.SYNTAX_STYLE_YAML),
      Map.entry("PHP", SyntaxConstants.SYNTAX_STYLE_PHP),
      Map.entry("Ruby", SyntaxConstants.SYNTAX_STYLE_RUBY),
      Map.entry("Kotlin", SyntaxConstants.SYNTAX_STYLE_KOTLIN));

  // BUTTON COLOR AND HOVER COLOR
  private static final Color BTN_COLOR_DARKER = new Color(0x00744d);
  private static final Color BTN_COLOR_BLACK = new Color(0x242526);

  // package‑level config
  private static String PACKAGE_NAME;

  public CustomTitleBar(JFrame frame,
      SplitTextTabPanel splitPanel,
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

    RoundedButton menuButton = new RoundedButton("☰");
    menuButton.setBackgroundColor(BTN_COLOR_BLACK);
    menuButton.setHoverBackgroundColor(BTN_COLOR_DARKER);
    menuButton.setBorderColor(BTN_COLOR_BLACK);
    menuButton.setHoverBorderColor(BTN_COLOR_DARKER);
    menuButton.setBorderThickness(2);
    menuButton.setCornerRadius(10);
    menuButton.setMargin(new Insets(0, 0, 0, 0));
    menuButton.setFont(menuButton.getFont().deriveFont(14f));

    // Example popup menu
    JPopupMenu popup = new JPopupMenu();
    JMenu appearance = new JMenu("Appearance");
    appearance.add(new JMenuItem("Dark theme"));
    appearance.add(new JMenuItem("Light theme"));

    popup.add(appearance);
    // SYNTAX HIGHLIGHTING MENU
    popup.add(createSyntaxMenu(splitPanel));

    menuButton.addActionListener(e -> popup.show(menuButton, 0, menuButton.getHeight()));

    controlPanel.add(minimizeButton);
    controlPanel.add(maximizeButton);
    controlPanel.add(closeButton);

    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    rightPanel.setOpaque(false);
    rightPanel.add(controlPanel); // then add minimize/maximize/close

    JPanel centerPanel = new JPanel();
    centerPanel.setOpaque(false);
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

    centerPanel.add(titleLabel);
    centerPanel.add(Box.createRigidArea(new Dimension(10, 0))); // optional gap
    centerPanel.add(menuButton); // now appears after the title

    add(centerPanel, BorderLayout.CENTER);
    add(rightPanel, BorderLayout.EAST);

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

    JButton templateButton = new JButton(new ImageIcon(
        getClass().getResource("/" + PACKAGE_NAME + "/images/" + defIcon)));
    int size = 32; // or 32 depending on your icons
    templateButton.setPreferredSize(new Dimension(size, size));
    templateButton.setBorderPainted(false);
    templateButton.setFocusPainted(false);
    templateButton.setContentAreaFilled(false);
    templateButton.setToolTipText(defIcon.split("_")[0]); // quick tooltip

    templateButton.addActionListener(action);
    templateButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        templateButton.setIcon(new ImageIcon(getClass().getResource(
            "/" + PACKAGE_NAME + "/images/" + hoverIcon)));
      }

      @Override
      public void mouseExited(MouseEvent e) {
        templateButton.setIcon(new ImageIcon(getClass().getResource(
            "/" + PACKAGE_NAME + "/images/" + defIcon)));
      }
    });
    return templateButton;
  }

  // ───────────────────────────── SYNTAX MENU ────────────────────────────────
  // Dynamically create menu items from SYNTAX_STYLES map
  private JMenu createSyntaxMenu(SplitTextTabPanel splitPanel) {
    JMenu syntaxHighlighting = new JMenu("Syntax Highlighting");

    // 1. Always add "None" first
    String noneStyle = SYNTAX_STYLES.get("None");
    JMenuItem noneItem = new JMenuItem("None");
    noneItem.addActionListener(e -> splitPanel.setSyntaxStyleBoth(noneStyle));
    syntaxHighlighting.add(noneItem);

    // 2. Add the rest (skip "None")
    for (Map.Entry<String, String> entry : SYNTAX_STYLES.entrySet()) {
      if ("None".equals(entry.getKey()))
        continue; // skip
      String displayName = entry.getKey();
      String styleConstant = entry.getValue();

      JMenuItem item = new JMenuItem(displayName);
      item.addActionListener(e -> {
        // Example: apply to both editors
        splitPanel.setSyntaxStyleBoth(styleConstant);

        // OR, if you want left/right separately:
        // splitPanel.setSyntaxStyleLeft(styleConstant);
        // splitPanel.setSyntaxStyleRight(styleConstant);
      });
      syntaxHighlighting.add(item);
    }

    return syntaxHighlighting;
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