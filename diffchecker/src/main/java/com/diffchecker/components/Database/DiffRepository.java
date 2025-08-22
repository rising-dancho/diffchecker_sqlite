package com.diffchecker.components.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiffRepository {
  private final DB db;

  public DiffRepository(DB db) {
    this.db = db;
  }

  public List<DiffData> getAllDiffs() {
    List<DiffData> list = new ArrayList<>();
    String sql = "SELECT id, title, left_text, right_text FROM diff_tabs";

    try (Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String left = rs.getString("left_text");
        String right = rs.getString("right_text");

        list.add(new DiffData(id, title, left, right)); // ✅ preserve id
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public boolean updateDiff(DiffData data) {
    String sql = "UPDATE diff_tabs SET title = ?, left_text = ?, right_text = ? WHERE id = ?";
    try (Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, data.title);
      stmt.setString(2, data.leftText);
      stmt.setString(3, data.rightText);
      stmt.setInt(4, data.id);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean deleteDiff(int id) {
    String sql = "DELETE FROM diff_tabs WHERE id = ?";
    try (Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean saveDiff(DiffData data) {
    String sql = "INSERT INTO diff_tabs (title, left_text, right_text) VALUES (?, ?, ?)";
    try (Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, data.title);
      stmt.setString(2, data.leftText);
      stmt.setString(3, data.rightText);
      int affected = stmt.executeUpdate();

      if (affected > 0) {
        try (ResultSet keys = stmt.getGeneratedKeys()) {
          if (keys.next()) {
            data.id = keys.getInt(1); // ✅ store new ID in object
          }
        }
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

}
