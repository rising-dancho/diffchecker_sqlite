package com.diffchecker.components;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import com.diffchecker.components.Database.DB;
import com.diffchecker.components.Database.DiffData;
import com.diffchecker.components.Database.DiffRepository;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// RSyntaxTextArea dependencies
import org.fife.ui.rsyntaxtextarea.*;
// RSyntaxTextArea search and replace dependencies
import org.fife.ui.rtextarea.*;

public class SplitTextTabPanel extends JPanel {
    // DEFAULT DECLARATIONS
    private static final String PACKAGE_NAME = "diffchecker";
    private RSyntaxTextArea jt1;
    private RSyntaxTextArea jt2;

    // WORD HIGHLIGHT
    private static final Color LINE_REMOVED = new Color(0x40191D);
    private static final Color LINE_ADDED = new Color(0x12342B);
    private static final Color WORD_REMOVED = new Color(0x8B1E1D);
    private static final Color WORD_ADDED = new Color(0x137B5A);

    // FONT COLORS
    private static final Color EDITOR_BACKGROUND = new Color(0x17181C); // Dark gray
    private static final Color EDITOR_FONT_COLOR = new Color(0xD4D4D4); // Light text
    private static final Color EDITOR_BORDER_COLOR = new Color(0x242526); // Light text
    private static final Color ACTIVE_BORDER_COLOR = new Color(0x00744d);

    // BACKGROUND COLOR
    // private static final Color BACKGROUND_LIGHT = new Color(0xF9FAFA);
    private final Color BACKGROUND_DARK = new Color(0x17181C);
    private final Color BACKGROUND_LABEL_DARK = new Color(0x17181C);

    // BUTTON COLOR AND HOVER COLOR
    private static final Color BTN_COLOR = new Color(0x00af74);
    private static final Color BTN_COLOR_DARKER = new Color(0x00744d);
    private static final Color BTN_COLOR_BLACK = new Color(0x242526);

    // SCROLL BARS
    private final JScrollPane scroll1;
    private final JScrollPane scroll2;

    // SUMMARY LABELS
    private final JPanel leftLabelPanel;
    private final JPanel rightLabelPanel;

    private final java.util.List<HighlightInfo> highlightPositions = new ArrayList<>();

    private static class HighlightInfo {
        int startOffset;
        int endOffset;
        RSyntaxTextArea area;

        HighlightInfo(RSyntaxTextArea area, int start, int end) {
            this.area = area;
            this.startOffset = start;
            this.endOffset = end;
        }
    }

    private static class DiffGroup {
        HighlightInfo left;
        HighlightInfo right;
    }

    private final List<DiffGroup> diffGroups = new ArrayList<>();
    private int currentGroupIndex = -1;
    private DiffData currentDiff; // keep reference

    // CHECKING IF GREEN BORDER IS ACTIVE OR NOT
    private boolean jt1IsActive = false;
    private boolean jt2IsActive = false;

    public SplitTextTabPanel() {
        setLayout(new BorderLayout());

        jt1 = createRSyntaxArea();
        jt2 = createRSyntaxArea();
        scroll1 = new RTextScrollPane(jt1);
        scroll2 = new RTextScrollPane(jt2);

        // Apply your theme from XML:
        try (InputStream in = getClass().getResourceAsStream("/diffchecker/themes/mytheme.xml")) {
            Theme theme = Theme.load(in);
            theme.apply(jt1);
            theme.apply(jt2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // CUSTOM SCROLLBARS
        scroll1.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scroll2.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scroll1.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        scroll2.getHorizontalScrollBar().setUI(new CustomScrollBarUI());

        scroll1.getHorizontalScrollBar().setOpaque(true);
        scroll1.getHorizontalScrollBar().setBackground(EDITOR_BACKGROUND); // Match your dark theme

        scroll2.getHorizontalScrollBar().setOpaque(true);
        scroll2.getHorizontalScrollBar().setBackground(EDITOR_BACKGROUND);
        scroll1.getVerticalScrollBar().setOpaque(true);
        scroll1.getVerticalScrollBar().setBackground(EDITOR_BACKGROUND);

        scroll2.getVerticalScrollBar().setOpaque(true);
        scroll2.getVerticalScrollBar().setBackground(EDITOR_BACKGROUND);

        scroll1.setOpaque(false);
        scroll1.getViewport().setOpaque(false);

        scroll2.setOpaque(false);
        scroll2.getViewport().setOpaque(false);

        // FONT FAMILY OF THE TEXTAREAS
        try {
            InputStream is = getClass().getResourceAsStream(
                    "/" + PACKAGE_NAME + "/fonts/FiraCode-Regular.ttf");
            Font firaCode = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 16f);

            jt1.setFont(firaCode);
            jt2.setFont(firaCode);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            // Fallback if font fails to load
            jt1.setFont(new Font("Monospaced", Font.PLAIN, 14));
            jt2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        }

        // REMOVE DEFAULT BORDERS
        jt1.setBorder(BorderFactory.createEmptyBorder());
        jt2.setBorder(BorderFactory.createEmptyBorder());

        // jt1.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        // Synchronize vertical scrolling
        JScrollBar vBar1 = scroll1.getVerticalScrollBar();
        JScrollBar vBar2 = scroll2.getVerticalScrollBar();

        vBar1.addAdjustmentListener(e -> {
            if (vBar2.getValue() != vBar1.getValue()) {
                vBar2.setValue(vBar1.getValue());
            }
        });

        vBar2.addAdjustmentListener(e -> {
            if (vBar1.getValue() != vBar2.getValue()) {
                vBar1.setValue(vBar2.getValue());
            }
        });

        scroll1.setRowHeaderView(new LineNumberingTextArea(jt1));
        scroll2.setRowHeaderView(new LineNumberingTextArea(jt2));

        scroll1.setBorder(null);
        scroll2.setBorder(null);

        scroll1.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
        scroll2.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));

        // Create label panel for each text area
        leftLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftLabelPanel.setBackground(BACKGROUND_LABEL_DARK);

        rightLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rightLabelPanel.setBackground(BACKGROUND_LABEL_DARK);

        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(leftLabelPanel, BorderLayout.NORTH);
        p1.add(scroll1, BorderLayout.CENTER);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(rightLabelPanel, BorderLayout.NORTH);
        p2.add(scroll2, BorderLayout.CENTER);

        // SIDE BY SIDE TEXT AREAS
        JPanel sideBySidePanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 10px gap between areas
        sideBySidePanel.setBackground(BACKGROUND_DARK); // Match theme
        sideBySidePanel.add(p1);
        sideBySidePanel.add(p2);

        // add(splitPane, BorderLayout.CENTER);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // top, left, bottom, right
        contentPanel.setBackground(BACKGROUND_DARK); // match your theme
        contentPanel.add(sideBySidePanel, BorderLayout.CENTER);

        // TEST BORDER
        // contentPanel.setBorder(BorderFactory.createLineBorder(REMOVAL_LABEL_COLOR_DARK));

        add(contentPanel, BorderLayout.CENTER);

        // CUSTOM BUTTON
        RoundedButton diffcheckBtn = new RoundedButton("Find Difference");
        diffcheckBtn.setBackgroundColor(BTN_COLOR); // <- normal color
        diffcheckBtn.setHoverBackgroundColor(BTN_COLOR_DARKER); // <- hover color
        diffcheckBtn.setBorderColor(BTN_COLOR);// <- normal color
        diffcheckBtn.setHoverBorderColor(BTN_COLOR_DARKER); // <- hover color
        diffcheckBtn.setBorderThickness(2);
        diffcheckBtn.setCornerRadius(10);
        diffcheckBtn.addActionListener(e -> {
            try {
                highlightDiffs();
            } catch (BadLocationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        RoundedButton findBtn = new RoundedButton("🔍︎");
        findBtn.setBackgroundColor(BTN_COLOR_BLACK); // <- normal color
        findBtn.setHoverBackgroundColor(BTN_COLOR_DARKER); // <- hover color
        findBtn.setBorderColor(BTN_COLOR_BLACK);// <- normal color
        findBtn.setHoverBorderColor(BTN_COLOR_DARKER); // <- hover color
        findBtn.setBorderThickness(2);
        findBtn.setCornerRadius(10);
        findBtn.setMargin(new Insets(5, 10, 5, 10));
        // findBtn.addActionListener(e -> {
        // // Use jt1 if it's currently active, otherwise jt2
        // RSyntaxTextArea target = jt1IsActive ? jt1 : jt2;

        // String term = JOptionPane.showInputDialog(this, "Find:");
        // if (term == null || term.isBlank())
        // return;
        // String replace = JOptionPane.showInputDialog(this, "Replace with:");
        // if (replace == null)
        // return; // allow empty string as replacement

        // SearchContext context = new SearchContext();
        // context.setSearchFor(term);
        // context.setReplaceWith(replace);
        // context.setRegularExpression(false);

        // SearchEngine.replace(target, context);
        // if (term == null || term.isBlank())
        // return;

        // SearchResult result = SearchEngine.find(target, context);
        // if (!result.wasFound()) {
        // JOptionPane.showMessageDialog(this, "No results found for: " + term,
        // "Search Result", JOptionPane.INFORMATION_MESSAGE);
        // }
        // });

        RoundedButton previousBtn = new RoundedButton("◀️");
        previousBtn.setBackgroundColor(BTN_COLOR_BLACK); // <- normal color
        previousBtn.setHoverBackgroundColor(BTN_COLOR_DARKER); // <- hover color
        previousBtn.setBorderColor(BTN_COLOR_BLACK);// <- normal color
        previousBtn.setHoverBorderColor(BTN_COLOR_DARKER); // <- hover color
        previousBtn.setBorderThickness(2);
        previousBtn.setCornerRadius(10);
        previousBtn.setMargin(new Insets(5, 10, 5, 0));
        previousBtn.addActionListener(e -> {
            if (diffGroups.isEmpty())
                return;
            currentGroupIndex--;
            if (currentGroupIndex < 0)
                currentGroupIndex = diffGroups.size() - 1;
            focusDiffGroup(diffGroups.get(currentGroupIndex));
        });

        RoundedButton nextBtn = new RoundedButton("▶️");
        nextBtn.setBackgroundColor(BTN_COLOR_BLACK); // <- normal color
        nextBtn.setHoverBackgroundColor(BTN_COLOR_DARKER); // <- hover color
        nextBtn.setBorderColor(BTN_COLOR_BLACK);// <- normal color
        nextBtn.setHoverBorderColor(BTN_COLOR_DARKER); // <- hover color
        nextBtn.setBorderThickness(2);
        nextBtn.setCornerRadius(10);
        nextBtn.setMargin(new Insets(5, 10, 5, 0));
        nextBtn.addActionListener(e -> {
            if (diffGroups.isEmpty())
                return;
            currentGroupIndex++;
            if (currentGroupIndex >= diffGroups.size())
                currentGroupIndex = 0;
            focusDiffGroup(diffGroups.get(currentGroupIndex));
        });

        // LEFT: Clear Button
        RoundedButton clearBtn = new RoundedButton("Clear");
        clearBtn.setBackgroundColor(BTN_COLOR_BLACK);
        clearBtn.setHoverBackgroundColor(BTN_COLOR_DARKER);
        clearBtn.setBorderColor(BTN_COLOR_BLACK);
        clearBtn.setHoverBorderColor(BTN_COLOR_DARKER);
        clearBtn.setBorderThickness(2);
        clearBtn.setCornerRadius(10);
        clearBtn.addActionListener(e -> {
            jt1.setText("");
            jt2.setText("");
            leftLabelPanel.setVisible(false);
            rightLabelPanel.setVisible(false);
        });

        RoundedButton deleteBtn = new RoundedButton("✖");
        deleteBtn.setBackgroundColor(BTN_COLOR_BLACK);
        deleteBtn.setHoverBackgroundColor(BTN_COLOR_DARKER);
        deleteBtn.setBorderColor(BTN_COLOR_BLACK);
        deleteBtn.setHoverBorderColor(BTN_COLOR_DARKER);
        deleteBtn.setBorderThickness(2);
        deleteBtn.setCornerRadius(10);
        deleteBtn.addActionListener(e -> {
            if (currentDiff == null || currentDiff.id == -1) {
                JOptionPane.showMessageDialog(this, "No saved record to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete \"" + currentDiff.title + "\"?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                DB db = new DB();
                DiffRepository repo = new DiffRepository(db);
                boolean success = repo.deleteDiff(currentDiff.id);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Deleted successfully!");

                    // Remove this tab from the JTabbedPane
                    Container parent = getParent();
                    while (parent != null && !(parent instanceof JTabbedPane)) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof JTabbedPane) {
                        JTabbedPane tabbedPane = (JTabbedPane) parent;
                        int index = tabbedPane.indexOfComponent(this);
                        if (index != -1) {
                            tabbedPane.remove(index);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Delete failed.");
                }
            }
        });

        // RIGHT: Save Button
        RoundedButton saveBtn = new RoundedButton("Save");
        saveBtn.setBackgroundColor(BTN_COLOR_BLACK);
        saveBtn.setHoverBackgroundColor(BTN_COLOR_DARKER);
        saveBtn.setBorderColor(BTN_COLOR_BLACK);
        saveBtn.setHoverBorderColor(BTN_COLOR_DARKER);
        saveBtn.setBorderThickness(2);
        saveBtn.setCornerRadius(10);
        saveBtn.addActionListener(e -> saveToDatabase());

        JPanel bottomPanel = new JPanel(new BorderLayout()); // CENTER = button centered
        bottomPanel.setBackground(BACKGROUND_DARK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        // bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtonPanel.setBackground(BACKGROUND_DARK);
        leftButtonPanel.add(clearBtn);
        leftButtonPanel.add(deleteBtn);
        bottomPanel.add(leftButtonPanel, BorderLayout.WEST);

        // CENTER: diffcheckBtn, previousBtn, nextBtn
        JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerButtonPanel.setBackground(BACKGROUND_DARK);
        centerButtonPanel.add(findBtn);
        centerButtonPanel.add(diffcheckBtn);
        centerButtonPanel.add(previousBtn);
        centerButtonPanel.add(nextBtn);
        bottomPanel.add(centerButtonPanel, BorderLayout.CENTER);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.setBackground(BACKGROUND_DARK);
        rightButtonPanel.add(saveBtn);
        bottomPanel.add(rightButtonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Add labels above each scroll pane
        p1.add(leftLabelPanel, BorderLayout.NORTH);
        p2.add(rightLabelPanel, BorderLayout.NORTH);

        // INITIALLY HIDE SUMMARY LABELS
        leftLabelPanel.setVisible(false);
        rightLabelPanel.setVisible(false);

        jt1.setBackground(EDITOR_BACKGROUND);
        jt1.setForeground(EDITOR_FONT_COLOR);
        jt1.setCaretColor(EDITOR_FONT_COLOR); // make the cursor visible

        jt2.setBackground(EDITOR_BACKGROUND);
        jt2.setForeground(EDITOR_FONT_COLOR);
        jt2.setCaretColor(EDITOR_FONT_COLOR);

        // REMOVING THE WHITE SQUARES AT THE INTERSCTION OF THE SCROLLBARS
        // Unique corners for scroll1
        JPanel scroll1CornerLeft = new JPanel();
        scroll1CornerLeft.setBackground(EDITOR_BACKGROUND);
        JPanel scroll1CornerRight = new JPanel();
        scroll1CornerRight.setBackground(EDITOR_BACKGROUND);

        // Unique corners for scroll2
        JPanel scroll2CornerLeft = new JPanel();
        scroll2CornerLeft.setBackground(EDITOR_BACKGROUND);
        JPanel scroll2CornerRight = new JPanel();
        scroll2CornerRight.setBackground(EDITOR_BACKGROUND);

        // Set corners for scroll1
        scroll1.setCorner(JScrollPane.LOWER_LEFT_CORNER, scroll1CornerLeft);
        scroll1.setCorner(JScrollPane.LOWER_RIGHT_CORNER, scroll1CornerRight);

        // Set corners for scroll2
        scroll2.setCorner(JScrollPane.LOWER_LEFT_CORNER, scroll2CornerLeft);
        scroll2.setCorner(JScrollPane.LOWER_RIGHT_CORNER, scroll2CornerRight);

        // TEST BORDER
        // scroll1.setBorder(BorderFactory.createLineBorder(REMOVAL_LABEL_COLOR_DARK));

        // ------------- end

        // Scroll panes (optional, matches textarea bg)
        scroll1.getViewport().setBackground(EDITOR_BACKGROUND);
        scroll2.getViewport().setBackground(EDITOR_BACKGROUND);

        // Background of the left/right label panels
        leftLabelPanel.setBackground(EDITOR_BACKGROUND);
        rightLabelPanel.setBackground(EDITOR_BACKGROUND);

        // ADD BORDER UPON ACTIVATING TEXAREAS
        jt1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jt1IsActive = true;
                jt2IsActive = false;
                scroll1.setBorder(BorderFactory.createLineBorder(ACTIVE_BORDER_COLOR));
                scroll2.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                jt1IsActive = false;
                scroll1.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
            }
        });

        jt2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jt2IsActive = true;
                jt1IsActive = false;
                scroll2.setBorder(BorderFactory.createLineBorder(ACTIVE_BORDER_COLOR));
                scroll1.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                jt2IsActive = false;
                scroll2.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
            }
        });

        // Background click handler
        addMouseListener(new MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent e) {
                Component clicked = SwingUtilities.getDeepestComponentAt(SplitTextTabPanel.this, e.getX(), e.getY());
                if (!(SwingUtilities.isDescendingFrom(clicked, jt1) || SwingUtilities.isDescendingFrom(clicked, jt2))) {
                    requestFocusInWindow(); // steal focus from textareas
                    repaint(); // helps caret disappear properly
                }
            }
        });

        // TEST BORDER
        // setBorder(BorderFactory.createLineBorder(REMOVAL_LABEL_COLOR_DARK));
    }

    private RSyntaxTextArea createRSyntaxArea() {
        RSyntaxTextArea area = new RSyntaxTextArea();
        area.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        area.setAntiAliasingEnabled(true);
        area.setEditable(true); // Allow editing if you still want to diff edited text
        area.setBackground(EDITOR_BACKGROUND);
        area.setForeground(EDITOR_FONT_COLOR);
        area.setCaretColor(EDITOR_FONT_COLOR);
        area.setBorder(BorderFactory.createEmptyBorder());
        area.setCodeFoldingEnabled(true);
        return area;
    }

    private void highlightDiffs() throws BadLocationException {
        diffGroups.clear();
        currentGroupIndex = -1;

        String leftText = jt1.getText();
        String rightText = jt2.getText();

        List<String> leftLines = Arrays.asList(leftText.split("\n"));
        List<String> rightLines = Arrays.asList(rightText.split("\n"));

        Patch<String> patch = DiffUtils.diff(leftLines, rightLines);

        for (AbstractDelta<String> delta : patch.getDeltas()) {
            int origPos = delta.getSource().getPosition();
            int revPos = delta.getTarget().getPosition();

            // we'll keep the first line of each side as the "jump" point
            DiffGroup group = new DiffGroup();

            switch (delta.getType()) {
                case DELETE:
                    highlightFullLines(jt1, origPos, delta.getSource().size(), LINE_REMOVED);
                    int startOffsetLeft = jt1.getLineStartOffset(origPos);
                    group.left = new HighlightInfo(jt1, startOffsetLeft, startOffsetLeft);
                    break;

                case INSERT:
                    highlightFullLines(jt2, revPos, delta.getTarget().size(), LINE_ADDED);
                    int startOffsetRight = jt2.getLineStartOffset(revPos);
                    group.right = new HighlightInfo(jt2, startOffsetRight, startOffsetRight);
                    break;

                case CHANGE:
                    highlightFullLines(jt1, origPos, delta.getSource().size(), LINE_REMOVED);
                    highlightFullLines(jt2, revPos, delta.getTarget().size(), LINE_ADDED);

                    int lOff = jt1.getLineStartOffset(origPos);
                    int rOff = jt2.getLineStartOffset(revPos);
                    group.left = new HighlightInfo(jt1, lOff, lOff);
                    group.right = new HighlightInfo(jt2, rOff, rOff);

                    // still highlight words as usual
                    for (int i = 0; i < Math.min(delta.getSource().size(), delta.getTarget().size()); i++) {
                        highlightWordDiffs(jt1, origPos + i, delta.getSource().getLines().get(i),
                                delta.getTarget().getLines().get(i), WORD_REMOVED, true);
                        highlightWordDiffs(jt2, revPos + i, delta.getSource().getLines().get(i),
                                delta.getTarget().getLines().get(i), WORD_ADDED, false);
                    }
                    break;
            }

            diffGroups.add(group);
        }
    }

    private void focusDiffGroup(DiffGroup group) {
        // prefer left side if it exists, otherwise right
        HighlightInfo info = group.left != null ? group.left : group.right;
        info.area.setCaretPosition(info.startOffset);
        info.area.requestFocusInWindow();
    }

    private void highlightWordDiffs(RSyntaxTextArea area, int lineIndex, String oldLine, String newLine, Color color,
            boolean isLeft) {
        List<String> tokens1 = Arrays.asList(oldLine.split("\\b"));
        List<String> tokens2 = Arrays.asList(newLine.split("\\b"));

        Patch<String> wordPatch = DiffUtils.diff(tokens1, tokens2);

        try {
            int pos = area.getLineStartOffset(lineIndex);
            List<String> tokens = isLeft ? tokens1 : tokens2;

            for (String token : tokens) {
                boolean changed = wordPatch.getDeltas().stream()
                        .anyMatch(delta -> (isLeft ? delta.getSource().getLines() : delta.getTarget().getLines())
                                .contains(token));

                if (changed && !token.isBlank()) {
                    int tokenStart = pos;
                    int tokenEnd = pos + token.length();
                    area.getHighlighter().addHighlight(tokenStart, tokenEnd,
                            new DefaultHighlighter.DefaultHighlightPainter(color));
                    highlightPositions.add(new HighlightInfo(area, tokenStart, tokenEnd));
                }
                pos += token.length();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void highlightFullLines(RSyntaxTextArea area, int startLine, int count, Color color) {
        for (int i = 0; i < count; i++) {
            try {
                area.addLineHighlight(startLine + i, color);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    // DATABASE
    public void loadFromDatabase(DiffData data) {
        currentDiff = data;
        jt1.setText(data.leftText);
        jt2.setText(data.rightText);
    }

    private void saveToDatabase() {
        String title = JOptionPane.showInputDialog(this, "Enter a title:",
                currentDiff != null ? currentDiff.title : "");
        if (title == null || title.trim().isEmpty())
            return;

        String leftText = jt1.getText();
        String rightText = jt2.getText();

        DB db = new DB();
        DiffRepository repo = new DiffRepository(db);

        boolean success;
        if (currentDiff != null && currentDiff.id != -1) {
            // Update existing record
            currentDiff.title = title;
            currentDiff.leftText = leftText;
            currentDiff.rightText = rightText;
            success = repo.updateDiff(currentDiff);
        } else {
            // Insert new record
            DiffData newData = new DiffData(title, leftText, rightText);
            success = repo.saveDiff(newData);
            currentDiff = newData; // track this record now
        }

        JOptionPane.showMessageDialog(this, success ? "Saved successfully!" : "Save failed.");

        if (success) {
            // 🔹 Update the tab title in the JTabbedPane
            Container parent = getParent();
            while (parent != null && !(parent instanceof JTabbedPane)) {
                parent = parent.getParent();
            }
            if (parent instanceof JTabbedPane) {
                JTabbedPane tabbedPane = (JTabbedPane) parent;
                int index = tabbedPane.indexOfComponent(this);
                if (index != -1) {
                    // If you use ClosableTabTitleComponent, update its label too
                    Component tabComponent = tabbedPane.getTabComponentAt(index);
                    if (tabComponent instanceof ClosableTabTitleComponent) {
                        ((ClosableTabTitleComponent) tabComponent).setTitle(title);
                    } else {
                        tabbedPane.setTitleAt(index, title);
                    }
                }
            }
        }
    }

}