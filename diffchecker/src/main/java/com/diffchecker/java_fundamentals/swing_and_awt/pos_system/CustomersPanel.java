package com.diffchecker.java_fundamentals.swing_and_awt.pos_system;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CustomersPanel extends JPanel {
  // Widgets
  JLabel search_Lbl;
  JLabel name_Lbl;
  JLabel mobile_number_Lbl;

  JTextField name_Field;
  JTextField search_Field;
  JTextField mobile_number_Field;
  JTextField search_table_Field;

  JButton save_Btn;
  JButton search_Btn;
  JButton update_Btn;
  JButton delete_Btn;
  JButton search_table_Btn;

  // Table
  JTable table;
  DefaultTableModel defaultTableModel;

  // Database
  DB database;

  public CustomersPanel() {

    database = new DB();

    // Widgets Initialization
    search_Lbl = new JLabel("Search by Mobile Number: ");
    name_Lbl = new JLabel("Name: ");
    mobile_number_Lbl = new JLabel("Mobile Number: ");

    name_Field = new JTextField();
    search_Field = new JTextField();
    mobile_number_Field = new JTextField();
    search_table_Field = new JTextField();

    save_Btn = new JButton("Save");
    search_Btn = new JButton("Search");
    update_Btn = new JButton("Update");
    delete_Btn = new JButton("Delete");
    search_table_Btn = new JButton("Search Table");

    // Left Panel: Grid
    JPanel leftJPanel = new JPanel();
    leftJPanel.setPreferredSize(new Dimension(200, 300));
    leftJPanel.setLayout(new GridLayout(8, 2));

    leftJPanel.add(search_Lbl);
    leftJPanel.add(search_Field);
    leftJPanel.add(name_Lbl);
    leftJPanel.add(name_Field);
    leftJPanel.add(mobile_number_Lbl);
    leftJPanel.add(mobile_number_Field);

    leftJPanel.add(save_Btn);
    leftJPanel.add(update_Btn);
    leftJPanel.add(search_Btn);
    leftJPanel.add(delete_Btn);
    leftJPanel.add(search_table_Field);
    leftJPanel.add(search_table_Btn);

    // Handling click events
    save_Btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        insertCustomerIntoDB();
      }

    });

    update_Btn.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        updateCustomerIntoDB();
      }
    });

    delete_Btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deleteCustomerFromDB();
      }

    });

    search_Btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        searchCustomerByMobileNumber();
      }
    });

    search_table_Btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        searchTableByName();
      }
    });

    // Right Panel: Table
    JPanel rightJPanel = new JPanel();
    rightJPanel.setPreferredSize(new Dimension(600, 500));
    rightJPanel.setLayout(new FlowLayout());

    // Initialize the Table
    table = new JTable();
    table.setPreferredSize(new Dimension(650, 500));

    // Loading the table
    loadTable();

    // make the table scrollable
    JScrollPane sp = new JScrollPane(table);
    rightJPanel.add(sp);

    add(leftJPanel, BorderLayout.WEST);
    add(rightJPanel, BorderLayout.EAST);

  }

  public void insertCustomerIntoDB() {
    String customer_name = name_Field.getText();
    String mobile_number = mobile_number_Field.getText();

    try {
      Statement query = database.getConnection().createStatement();
      query.executeUpdate(
          "INSERT INTO customers (customer_name, mobile_number) VALUES ('" + customer_name + "', '" + mobile_number
              + "')");
      JOptionPane.showMessageDialog(null, "Customer Saved!");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    loadTable();
  }

  public void updateCustomerIntoDB() {
    String customer_name = name_Field.getText();
    String mobile_number = mobile_number_Field.getText();

    try {
      Statement query = database.getConnection().createStatement();
      query.executeUpdate(
          "UPDATE customers SET customer_name = '" + customer_name + "', mobile_number = '" + mobile_number
              + "' WHERE mobile_number = '" + mobile_number + "'");
      JOptionPane.showMessageDialog(null, "Customer Updated!");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    loadTable();
  }

  public void deleteCustomerFromDB() {
    String mobile_number = mobile_number_Field.getText();
    try {
      Statement query = database.getConnection().createStatement();
      query.executeUpdate("DELETE FROM customers WHERE mobile_number = '" + mobile_number + "'");
      JOptionPane.showMessageDialog(null, "Customer Deleted!");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void searchCustomerByMobileNumber() {
    String search = search_Field.getText();
    try {
      Statement query = database.getConnection().createStatement();
      ResultSet rs = query.executeQuery("SELECT * FROM customers WHERE mobile_number = '" + search + "'");

      if (rs.next()) {
        name_Field.setText(rs.getString("customer_name"));
        mobile_number_Field.setText(rs.getString("mobile_number"));
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    loadTable();
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void searchTableByName() {
    String searched_name = search_table_Field.getText();
    try {
      defaultTableModel.setRowCount(0);
      Statement query = database.getConnection().createStatement();
      ResultSet rs = query.executeQuery("SELECT * FROM customers WHERE customer_name LIKE '%" + searched_name + "%'");
      while (rs.next()) {
        Vector v = new Vector<>();
        v.add(rs.getString(1));
        v.add(rs.getString(2));

        defaultTableModel.addRow(v);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void loadTable() {
    defaultTableModel = new DefaultTableModel();

    // Table Columns
    table = new JTable(defaultTableModel);
    defaultTableModel.addColumn("Customer Name");
    defaultTableModel.addColumn("Mobile Number");

    try {
      // Getting all data from database
      Statement query = database.getConnection().createStatement();
      ResultSet rs = query.executeQuery("SELECT * FROM customers");

      // Inserting all received records
      while (rs.next()) {
        Vector v = new Vector<>();
        v.add(rs.getString(1));
        v.add(rs.getString(2));

        defaultTableModel.addRow(v);

      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

}
