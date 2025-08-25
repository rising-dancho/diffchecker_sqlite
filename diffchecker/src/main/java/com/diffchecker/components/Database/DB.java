package com.diffchecker.components.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

  private static final String URL;

  static {
    // Get %APPDATA% (Roaming) → e.g. C:\Users\YourUser\AppData\Roaming
    String appData = System.getenv("APPDATA");
    if (appData == null) {
      // Fallback if not found
      appData = System.getProperty("user.home");
    }

    // Create a subfolder for your app
    File dbDir = new File(appData, "DiffcheckerAdfinem");
    if (!dbDir.exists()) {
      dbDir.mkdirs();
    }

    // Final DB file path
    File dbFile = new File(dbDir, "diffchecker.db");
    URL = "jdbc:sqlite:" + dbFile.getAbsolutePath();
  }

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
