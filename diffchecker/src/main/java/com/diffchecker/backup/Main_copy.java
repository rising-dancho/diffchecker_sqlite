package com.diffchecker.backup;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.Dimension;

import javax.swing.JFrame; // THE WINDOW ITSELF
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

// JFrame is the actual WINDOW ITSELF
public class Main_copy extends JFrame {
    static ImageIcon logo = new ImageIcon(Main_copy.class.getResource("/diffchecker/images/logo.png"));
    // Create menu bar
    static JMenuBar menuBar = new JMenuBar();
    // Create "File" menu
    static JMenu fileMenu = new JMenu("File");
    // Create menu items
    static JMenuItem newItem = new JMenuItem("New");
    static JMenuItem openItem = new JMenuItem("Open");
    static JMenuItem exitItem = new JMenuItem("Exit");
    // Create a container (panel) and the contents inside it
    JPanel container = new JPanel();
    JLabel label1 = new JLabel();

    public static void main(String[] args) {
        new Main_copy();
    }

    // CONSTRUCTOR
    public Main_copy() {

        /// ============ WINDOW ============
        // Set up frame or the window
        this.setTitle("Diffchecker");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(logo.getImage());
        this.getContentPane().setBackground(new Color(0x1F1F1F));
        this.setSize(1080, 720);

        // COMPLICATED WAY TO CENTER THE WINDOW
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        int xPos = (dim.width / 2) - (this.getWidth() / 2);
        int yPos = (dim.height / 2) - (this.getHeight() / 2);
        this.setLocation(xPos, yPos);

        // EASY WAY TO CENTER THE WINDOW
        // this.setLocationRelativeTo(null);

        /// ============ FILE MENU ============

        // Add action to "Exit"
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add menu items to the File menu
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator(); // adds a separator line
        fileMenu.add(exitItem);

        // Add File menu to the menu bar
        menuBar.add(fileMenu);

        // Set the menu bar to the frame
        this.setJMenuBar(menuBar);
        // Show the frame after setting up everything
        this.setVisible(true);

        /// ============ PANEL ============
        label1.setText("Welcome to my homepage!");
        label1.setForeground(new Color(0xEEEEEE));
        container.add(label1);
        container.setBackground(new Color(0x1F1F1F));
        this.add(container);

    }
}
