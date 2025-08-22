package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class JMenu_bar extends JFrame {
  public static void main(String[] args) {
    new JMenu_bar();
  }

  public JMenu_bar() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    // setLayout(new BorderLayout());
    setLayout(null);

    // TOP LEVEL CONTAINERS
    // INTERMEDIATE CONTAINERS
    // ATOMIC COMPONENTS

    // Menu bar
    JMenuBar jmenubar = new JMenuBar();
    jmenubar.setVisible(true);

    // Buttons inside the bar
    JMenu jmenu = new JMenu("File");

    // Items
    JMenuItem item1 = new JMenuItem("New");
    JMenuItem item2 = new JMenuItem("Save");
    JMenuItem item3 = new JMenuItem("Load");
    JMenuItem item4 = new JMenuItem("Exit");

    jmenubar.add(jmenu);
    jmenu.add(item1);
    jmenu.add(item2);
    jmenu.add(item3);
    jmenu.add(item4);

    item4.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }

    });

    setJMenuBar(jmenubar);
    setVisible(true);
  }
}
