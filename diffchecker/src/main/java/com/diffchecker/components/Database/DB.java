package com.diffchecker.components.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

  private static final String URL = "jdbc:sqlite:diffchecker.db";

  public DB() {
    // Auto-create schema if database is new
    try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
      String createTable = """
          CREATE TABLE IF NOT EXISTS diff_tabs (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              title TEXT NOT NULL,
              left_text TEXT,
              right_text TEXT
          );
          """;
      stmt.execute(createTable);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets a new database connection.
   *
   * @return a Connection to the SQLite database
   * @throws SQLException if a database access error occurs
   */
  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL);
  }

  public static void close(AutoCloseable... resources) {
    for (AutoCloseable res : resources) {
      if (res != null) {
        try {
          res.close();
        } catch (Exception ignored) {
        }
      }
    }
  }
}
