package com.diffchecker.java_fundamentals.swing_and_awt.pos_system;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class MainScreen extends JFrame {
  // Widgets
  JToggleButton customerBtn;
  JToggleButton employeeBtn;
  JToggleButton productBtn;
  JToggleButton salesBtn;
  JToggleButton invoiceBtn;

  // JPanel Loader    
  JPanelLoader loader = new JPanelLoader();

  public MainScreen() throws HeadlessException {
    createLayout();
  }

  public void createLayout() {
    // Set layout manager for the frame
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    // ─── Navigation Panel ─────────────────────────────────────────────
    JPanel p1 = new JPanel(new GridLayout(5, 1));
    TitledBorder titledBorder = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.GRAY, 1), "Navigation",
        TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
    p1.setBorder(titledBorder);

    gbc.gridx = 0; // column 0
    gbc.gridy = 0; // row 0
    gbc.gridheight = 1;
    gbc.weightx = 0.2; // 20% width
    gbc.weighty = 1.0; // take full heights
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(10, 10, 10, 10);
    add(p1, gbc);

    // ─── Dashboard Panel ─────────────────────────────────────────────
    JPanel p2 = new JPanel(new GridLayout(5, 1));
    TitledBorder titledBorder2 = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.GRAY, 1), "Dashboard",
        TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
    p2.setBorder(titledBorder2);

    gbc.gridx = 1; // column 1
    gbc.gridy = 0; // same row
    gbc.weightx = 0.8; // 80% width
    gbc.weighty = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    add(p2, gbc);

    customerBtn = new JToggleButton("Customers");
    employeeBtn = new JToggleButton("Employee");
    productBtn = new JToggleButton("Product");
    salesBtn = new JToggleButton("Sales");
    invoiceBtn = new JToggleButton("Invoice");

    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(customerBtn);
    buttonGroup.add(employeeBtn);
    buttonGroup.add(productBtn);
    buttonGroup.add(salesBtn);
    buttonGroup.add(invoiceBtn);

    p1.add(customerBtn);
    p1.add(employeeBtn);
    p1.add(productBtn);
    p1.add(salesBtn);
    p1.add(invoiceBtn);

    // Customer Button
    customerBtn.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        CustomersPanel c = new CustomersPanel();
        loader.jPanelLoader(p2, c);
      }

    });

  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainScreen mainScreen = new MainScreen();
      mainScreen.setTitle("POS SYSTEM");
      mainScreen.setSize(1200, 800);
      mainScreen.setLocationRelativeTo(null); // Center window
      mainScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainScreen.setVisible(true);
    });
  }
}
