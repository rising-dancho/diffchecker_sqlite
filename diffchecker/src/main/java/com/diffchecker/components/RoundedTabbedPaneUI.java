package com.diffchecker.components;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class RoundedTabbedPaneUI extends BasicTabbedPaneUI {

  private final Color selectedColor = new Color(0x363839);
  private final Color unselectedColor = new Color(0x242526);
  private final int arc = 6; // Increase for more roundness

  private final Color hoverColor = new Color(0x00744d);
  private int hoveredTabIndex = -1;

  private TabHoverListener hoverListener;

  public void setHoverListener(TabHoverListener listener) {
    this.hoverListener = listener;
  }

  public interface TabHoverListener {
    void onTabHoverChanged(int hoveredIndex);
  }

  // This controls space around the tabs:
  @Override
  protected void installDefaults() {
    super.installDefaults();
    tabInsets = new Insets(6, 6, 6, 6); // top, left, bottom, right (more padding)
    tabAreaInsets = new Insets(5, 0, 5, 0); // space around tab area
    contentBorderInsets = new Insets(0, 0, 0, 0);
  }

  @Override
  protected Insets getTabInsets(int tabPlacement, int tabIndex) {
    return new Insets(6, 12, 6, 12); // Adjust horizontal space (left/right)
  }

  @Override
  public void installUI(JComponent c) {
    super.installUI(c); // Initializes tabPane
    tabPane.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        int tab = getTabAtLocation(e.getX(), e.getY());
        if (tab != hoveredTabIndex) {
          hoveredTabIndex = tab;
          if (hoverListener != null) {
            hoverListener.onTabHoverChanged(hoveredTabIndex);
          }
          tabPane.repaint();
        }
      }

      @Override
      public void mouseDragged(MouseEvent e) {
        if (hoveredTabIndex != -1) {
          hoveredTabIndex = -1;
          tabPane.repaint();
        }
      }
    });
  }

  private int getTabAtLocation(int x, int y) {
    for (int i = 0; i < tabPane.getTabCount(); i++) {
      Rectangle rect = getTabBounds(tabPane, i);
      if (rect.contains(x, y)) {
        return i;
      }
    }
    return -1;
  }

  @Override
  protected void paintTabBackground(Graphics g, int tabPlacement,
      int tabIndex, int x, int y, int w, int h, boolean isSelected) {

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Color bg;
    int bgY = y;
    int bgH = h;

    if (isSelected) {
      bg = selectedColor;
      bgY = y + 5;
      bgH = h - 10;
    } else if (tabIndex == hoveredTabIndex) {
      bg = hoverColor;
      bgY = y + 3;
      bgH = h - 4;
    } else {
      bg = unselectedColor;
      bgY = y + 5;
      bgH = h - 10;
    }

    g2.setColor(bg);
    g2.fillRoundRect(x + 2, bgY, w - 4, bgH, arc, arc);
    g2.dispose();
  }

  @Override
  protected void paintContentBorder(Graphics g, int tabPlacement,
      int selectedIndex) {
    // Optional: don't paint a border
  }

  @Override
  protected void paintFocusIndicator(Graphics g, int tabPlacement,
      Rectangle[] rects, int tabIndex,
      Rectangle iconRect, Rectangle textRect, boolean isSelected) {
    // Do not paint focus indicator
  }

  @Override
  protected void paintTabBorder(Graphics g, int tabPlacement,
      int tabIndex, int x, int y, int w, int h, boolean isSelected) {
    if (!isSelected)
      return;

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setColor(new Color(0x5A5A5A)); // border color
    int arc = 6;
    g2.setStroke(new BasicStroke(1.5f));
    g2.drawRoundRect(x + 2, y + 5, w - 4, h - 10, arc, arc);

    g2.dispose();
  }

}
