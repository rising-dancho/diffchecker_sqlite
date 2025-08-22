package com.diffchecker.java_fundamentals.swing_and_awt.pos_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

  private static final String URL = "jdbc:mysql://localhost:3306/pos";
  private static final String USER = "root";
  private static final String PASSWORD = "";

  /**
   * Gets a new database connection.
   *
   * @return a Connection to the database
   * @throws SQLException if a database access error occurs
   */
  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }
}
