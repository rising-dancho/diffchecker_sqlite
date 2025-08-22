package com.diffchecker.java_fundamentals.swing_and_awt;

import javax.swing.*;
import java.awt.*;

import com.diffchecker.components.SplitTextTabPanel;
import com.diffchecker.components.Database.DB;
import com.diffchecker.components.Database.DiffData;
import com.diffchecker.components.Database.DiffRepository;

import java.util.List;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Diff Checker");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // center

        // ---- Create tabbed pane ----
        JTabbedPane tabs = new JTabbedPane();

        // ---- Restore last session (load all diffs from DB) ----
        DB db = new DB();
        DiffRepository repo = new DiffRepository(db);

        List<DiffData> diffs = repo.getAllDiffs();
        for (DiffData data : diffs) {
            SplitTextTabPanel panel = new SplitTextTabPanel();
            panel.loadFromDatabase(data);             // populate the text areas
            tabs.addTab(data.title, panel);           // add as a new tab
        }

        // ---- Add to frame ----
        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
