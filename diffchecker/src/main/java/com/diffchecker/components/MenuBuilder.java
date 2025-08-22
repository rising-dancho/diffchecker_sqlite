package com.diffchecker.components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBuilder {
  public static JMenuBar buildMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem exitItem = new JMenuItem("Exit");

    exitItem.addActionListener(e -> System.exit(0));
    fileMenu.addSeparator();
    fileMenu.add(exitItem);

    // REMOVE BORDER ARTIFACTS
    fileMenu.setBorder(BorderFactory.createEmptyBorder());
    menuBar.setBorder(BorderFactory.createEmptyBorder()); // THIS LINE IS CRUCIAL

    // Foreground
    fileMenu.setForeground(new Color(157, 157, 157));
    // Background
    menuBar.setBackground(new Color(0x242526));

    menuBar.add(fileMenu);
    return menuBar;
  }
}
