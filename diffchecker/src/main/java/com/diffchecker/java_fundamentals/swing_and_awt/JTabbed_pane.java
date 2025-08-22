package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.*;
import javax.swing.*;

import com.diffchecker.components.ClosableTabContextMenu;
import com.diffchecker.components.ClosableTabTitleComponent;

public class JTabbed_pane extends JFrame {
  private int untitledCounter = 1;

  public static void main(String[] args) {
    new JTabbed_pane();
  }

  public JTabbed_pane() {
    setTitle("Tabs");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setFocusable(false);
    tabbedPane.addMouseListener(new ClosableTabContextMenu(tabbedPane));

    JButton addButton = new JButton("+");
    addButton.setBorder(null);
    addButton.setFocusPainted(false);
    addButton.setContentAreaFilled(false);
    addButton.setPreferredSize(new Dimension(30, 30));
    addButton.addActionListener(e -> addNewTab(tabbedPane));

    tabbedPane.addTab("", null);
    tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, addButton);

    addNewTab(tabbedPane); // First real tab

    add(tabbedPane);
    setVisible(true);
  }

  private void addNewTab(JTabbedPane tabbedPane) {
    JTextArea textArea = new JTextArea();
    int plusTabIndex = tabbedPane.getTabCount() - 1;

    String title = "Untitled-" + untitledCounter++;

    tabbedPane.insertTab(title, null, new JScrollPane(textArea), null, plusTabIndex);
    tabbedPane.setTabComponentAt(
        plusTabIndex,
        new ClosableTabTitleComponent(tabbedPane, title, () -> addNewTab(tabbedPane)));
    tabbedPane.setSelectedIndex(plusTabIndex);
  }
}
