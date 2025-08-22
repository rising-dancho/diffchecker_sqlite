package com.diffchecker.components.Database;

public class DiffData {
    public int id;
    public String title;
    public String leftText;
    public String rightText;

    public DiffData(int id, String title, String leftText, String rightText) {
        this.id = id;
        this.title = title;
        this.leftText = leftText;
        this.rightText = rightText;
    }

    // For new diffs without an id yet
    public DiffData(String title, String leftText, String rightText) {
        this(-1, title, leftText, rightText);
    }
}
