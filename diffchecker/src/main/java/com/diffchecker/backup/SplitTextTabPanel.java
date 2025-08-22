// package com.diffchecker.components;

// import javax.swing.*;
// import javax.swing.text.BadLocationException;
// import javax.swing.text.DefaultHighlighter;
// import javax.swing.text.Highlighter;

// import com.diffchecker.components.Database.DB;
// import com.diffchecker.components.Database.DiffData;
// import com.diffchecker.components.Database.DiffRepository;
// import com.github.difflib.DiffUtils;
// import com.github.difflib.patch.AbstractDelta;
// import com.github.difflib.patch.Patch;

// import java.awt.*;
// import java.awt.event.FocusAdapter;
// import java.awt.event.FocusEvent;
// import java.awt.event.MouseAdapter;
// import java.io.IOException;
// import java.io.InputStream;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;

// public class SplitTextTabPanel extends JPanel {
//     // DEFAULT DECLARATIONS
//     private static final String PACKAGE_NAME = "diffchecker";
//     private final JTextArea jt1 = new JTextArea();
//     private final JTextArea jt2 = new JTextArea();

//     private final JLabel leftSummaryLabel = new JLabel();
//     private final JLabel rightSummaryLabel = new JLabel();

//     // ADD SUMMARY ARROWS
//     private static int added = 0, removed = 0;

//     // WORD HIGHLIGHT
//     private static final Color LINE_REMOVED = new Color(0x40191D);
//     private static final Color LINE_ADDED = new Color(0x12342B);
//     private static final Color WORD_REMOVED = new Color(0x8B1E1D);
//     private static final Color WORD_ADDED = new Color(0x137B5A);

//     private static final Color DELETE_WORD_COLOR = new Color(0x40191D); // darker red
//     private static final Color ADD_WORD_COLOR = new Color(0x12342B); // darker green

//     // FONT COLORS
//     private static final Color EDITOR_BACKGROUND = new Color(0x17181C); // Dark gray
//     private static final Color EDITOR_FONT_COLOR = new Color(0xD4D4D4); // Light text
//     private static final Color EDITOR_BORDER_COLOR = new Color(0x242526); // Light text
//     private static final Color ACTIVE_BORDER_COLOR = new Color(0x00744d);

//     // BACKGROUND COLOR
//     // private static final Color BACKGROUND_LIGHT = new Color(0xF9FAFA);
//     private final Color BACKGROUND_DARK = new Color(0x17181C);
//     private final Color BACKGROUND_LABEL_DARK = new Color(0x17181C);

//     // SUMMARY FONT COLOR
//     // private static final Color REMOVAL_LABEL_COLOR_DARK = new Color(0xB83A3A); // darker red
//     // private static final Color ADDED_LABEL_COLOR_DARK = new Color(0x1C7758); // darker red

//     // BUTTON COLOR AND HOVER COLOR
//     private static final Color BTN_COLOR = new Color(0x00af74);
//     private static final Color BTN_COLOR_DARKER = new Color(0x00744d);
//     private static final Color BTN_COLOR_BLACK = new Color(0x242526);

//     // SCROLL BARS
//     private final JScrollPane scroll1;
//     private final JScrollPane scroll2;

//     // SUMMARY LABELS
//     private final JPanel leftLabelPanel;
//     private final JPanel rightLabelPanel;

//     // CHECKING IF GREEN BORDER IS ACTIVE OR NOT
//     private boolean jt1IsActive = false;
//     private boolean jt2IsActive = false;

//     public SplitTextTabPanel() {
//         setLayout(new BorderLayout());
//         scroll1 = new JScrollPane(jt1);
//         scroll2 = new JScrollPane(jt2);

//         // CUSTOM SCROLLBARS
//         scroll1.getVerticalScrollBar().setUI(new CustomScrollBarUI());
//         scroll2.getVerticalScrollBar().setUI(new CustomScrollBarUI());
//         scroll1.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
//         scroll2.getHorizontalScrollBar().setUI(new CustomScrollBarUI());

//         scroll1.getHorizontalScrollBar().setOpaque(true);
//         scroll1.getHorizontalScrollBar().setBackground(EDITOR_BACKGROUND); // Match your dark theme

//         scroll2.getHorizontalScrollBar().setOpaque(true);
//         scroll2.getHorizontalScrollBar().setBackground(EDITOR_BACKGROUND);
//         scroll1.getVerticalScrollBar().setOpaque(true);
//         scroll1.getVerticalScrollBar().setBackground(EDITOR_BACKGROUND);

//         scroll2.getVerticalScrollBar().setOpaque(true);
//         scroll2.getVerticalScrollBar().setBackground(EDITOR_BACKGROUND);

//         scroll1.setOpaque(false);
//         scroll1.getViewport().setOpaque(false);

//         scroll2.setOpaque(false);
//         scroll2.getViewport().setOpaque(false);

//         // FONT FAMILY OF THE TEXTAREAS
//         try {
//             InputStream is = getClass().getResourceAsStream(
//                     "/" + PACKAGE_NAME + "/fonts/FiraCode-Regular.ttf");
//             Font firaCode = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 16f);

//             jt1.setFont(firaCode);
//             jt2.setFont(firaCode);
//         } catch (FontFormatException | IOException e) {
//             e.printStackTrace();
//             // Fallback if font fails to load
//             jt1.setFont(new Font("Monospaced", Font.PLAIN, 14));
//             jt2.setFont(new Font("Monospaced", Font.PLAIN, 14));
//         }

//         // REMOVE DEFAULT BORDERS
//         jt1.setBorder(BorderFactory.createEmptyBorder());
//         jt2.setBorder(BorderFactory.createEmptyBorder());

//         // jt1.setBorder(BorderFactory.createLineBorder(Color.BLUE));

//         // Synchronize vertical scrolling
//         JScrollBar vBar1 = scroll1.getVerticalScrollBar();
//         JScrollBar vBar2 = scroll2.getVerticalScrollBar();

//         vBar1.addAdjustmentListener(e -> {
//             if (vBar2.getValue() != vBar1.getValue()) {
//                 vBar2.setValue(vBar1.getValue());
//             }
//         });

//         vBar2.addAdjustmentListener(e -> {
//             if (vBar1.getValue() != vBar2.getValue()) {
//                 vBar1.setValue(vBar2.getValue());
//             }
//         });

//         scroll1.setRowHeaderView(new LineNumberingTextArea(jt1));
//         scroll2.setRowHeaderView(new LineNumberingTextArea(jt2));

//         scroll1.setBorder(null);
//         scroll2.setBorder(null);

//         scroll1.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
//         scroll2.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));

//         // Create label panel for each text area
//         leftLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         leftLabelPanel.add(leftSummaryLabel);
//         leftLabelPanel.setBackground(BACKGROUND_LABEL_DARK);

//         rightLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         rightLabelPanel.add(rightSummaryLabel);
//         rightLabelPanel.setBackground(BACKGROUND_LABEL_DARK);

//         JPanel p1 = new JPanel(new BorderLayout());
//         p1.add(leftLabelPanel, BorderLayout.NORTH);
//         p1.add(scroll1, BorderLayout.CENTER);

//         JPanel p2 = new JPanel(new BorderLayout());
//         p2.add(rightLabelPanel, BorderLayout.NORTH);
//         p2.add(scroll2, BorderLayout.CENTER);

//         // SIDE BY SIDE TEXT AREAS
//         JPanel sideBySidePanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 10px gap between areas
//         sideBySidePanel.setBackground(BACKGROUND_DARK); // Match theme
//         sideBySidePanel.add(p1);
//         sideBySidePanel.add(p2);

//         // add(splitPane, BorderLayout.CENTER);
//         JPanel contentPanel = new JPanel(new BorderLayout());
//         contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // top, left, bottom, right
//         contentPanel.setBackground(BACKGROUND_DARK); // match your theme
//         contentPanel.add(sideBySidePanel, BorderLayout.CENTER);

//         // TEST BORDER
//         // contentPanel.setBorder(BorderFactory.createLineBorder(REMOVAL_LABEL_COLOR_DARK));

//         add(contentPanel, BorderLayout.CENTER);

//         // CUSTOM BUTTON
//         RoundedButton diffcheckBtn = new RoundedButton("Find Difference");
//         diffcheckBtn.setBackgroundColor(BTN_COLOR); // <- normal color
//         diffcheckBtn.setHoverBackgroundColor(BTN_COLOR_DARKER); // <- hover color
//         diffcheckBtn.setBorderColor(BTN_COLOR);// <- normal color
//         diffcheckBtn.setHoverBorderColor(BTN_COLOR_DARKER); // <- hover color
//         diffcheckBtn.setBorderThickness(2);
//         diffcheckBtn.setCornerRadius(10);
//         diffcheckBtn.addActionListener(e -> highlightDiffs());

//         RoundedButton previousBtn = new RoundedButton("◀️");
//         previousBtn.setBackgroundColor(BTN_COLOR_BLACK); // <- normal color
//         previousBtn.setHoverBackgroundColor(BTN_COLOR_DARKER); // <- hover color
//         previousBtn.setBorderColor(BTN_COLOR_BLACK);// <- normal color
//         previousBtn.setHoverBorderColor(BTN_COLOR_DARKER); // <- hover color
//         previousBtn.setBorderThickness(2);
//         previousBtn.setCornerRadius(10);
//         previousBtn.setMargin(new Insets(5, 10, 5, 0));

//         RoundedButton nextBtn = new RoundedButton("▶️");
//         nextBtn.setBackgroundColor(BTN_COLOR_BLACK); // <- normal color
//         nextBtn.setHoverBackgroundColor(BTN_COLOR_DARKER); // <- hover color
//         nextBtn.setBorderColor(BTN_COLOR_BLACK);// <- normal color
//         nextBtn.setHoverBorderColor(BTN_COLOR_DARKER); // <- hover color
//         nextBtn.setBorderThickness(2);
//         nextBtn.setCornerRadius(10);
//         nextBtn.setMargin(new Insets(5, 10, 5, 0));

//         JPanel bottomPanel = new JPanel(new BorderLayout()); // CENTER = button centered
//         bottomPanel.setBackground(BACKGROUND_DARK);
//         bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
//         // bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));

//         // LEFT: Clear Button
//         RoundedButton clearBtn = new RoundedButton("Clear");
//         clearBtn.setBackgroundColor(BTN_COLOR_BLACK);
//         clearBtn.setHoverBackgroundColor(BTN_COLOR_DARKER);
//         clearBtn.setBorderColor(BTN_COLOR_BLACK);
//         clearBtn.setHoverBorderColor(BTN_COLOR_DARKER);
//         clearBtn.setBorderThickness(2);
//         clearBtn.setCornerRadius(10);

//         JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         leftButtonPanel.setBackground(BACKGROUND_DARK);
//         leftButtonPanel.add(clearBtn);
//         bottomPanel.add(leftButtonPanel, BorderLayout.WEST);

//         // CENTER: diffcheckBtn, previousBtn, nextBtn
//         JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//         centerButtonPanel.setBackground(BACKGROUND_DARK);
//         centerButtonPanel.add(diffcheckBtn);
//         centerButtonPanel.add(previousBtn);
//         centerButtonPanel.add(nextBtn);
//         bottomPanel.add(centerButtonPanel, BorderLayout.CENTER);

//         // RIGHT: Save Button
//         RoundedButton saveBtn = new RoundedButton("Save");
//         saveBtn.setBackgroundColor(BTN_COLOR_BLACK);
//         saveBtn.setHoverBackgroundColor(BTN_COLOR_DARKER);
//         saveBtn.setBorderColor(BTN_COLOR_BLACK);
//         saveBtn.setHoverBorderColor(BTN_COLOR_DARKER);
//         saveBtn.setBorderThickness(2);
//         saveBtn.setCornerRadius(10);
//         saveBtn.addActionListener(e -> saveToDatabase());

//         JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//         rightButtonPanel.setBackground(BACKGROUND_DARK);
//         rightButtonPanel.add(saveBtn);
//         bottomPanel.add(rightButtonPanel, BorderLayout.EAST);

//         add(bottomPanel, BorderLayout.SOUTH);

//         // Add labels above each scroll pane
//         p1.add(leftLabelPanel, BorderLayout.NORTH);
//         p2.add(rightLabelPanel, BorderLayout.NORTH);

//         // INITIALLY HIDE SUMMARY LABELS
//         leftLabelPanel.setVisible(false);
//         rightLabelPanel.setVisible(false);

//         jt1.setBackground(EDITOR_BACKGROUND);
//         jt1.setForeground(EDITOR_FONT_COLOR);
//         jt1.setCaretColor(EDITOR_FONT_COLOR); // make the cursor visible

//         jt2.setBackground(EDITOR_BACKGROUND);
//         jt2.setForeground(EDITOR_FONT_COLOR);
//         jt2.setCaretColor(EDITOR_FONT_COLOR);

//         // REMOVING THE WHITE SQUARES AT THE INTERSCTION OF THE SCROLLBARS
//         // Unique corners for scroll1
//         JPanel scroll1CornerLeft = new JPanel();
//         scroll1CornerLeft.setBackground(EDITOR_BACKGROUND);
//         JPanel scroll1CornerRight = new JPanel();
//         scroll1CornerRight.setBackground(EDITOR_BACKGROUND);

//         // Unique corners for scroll2
//         JPanel scroll2CornerLeft = new JPanel();
//         scroll2CornerLeft.setBackground(EDITOR_BACKGROUND);
//         JPanel scroll2CornerRight = new JPanel();
//         scroll2CornerRight.setBackground(EDITOR_BACKGROUND);

//         // Set corners for scroll1
//         scroll1.setCorner(JScrollPane.LOWER_LEFT_CORNER, scroll1CornerLeft);
//         scroll1.setCorner(JScrollPane.LOWER_RIGHT_CORNER, scroll1CornerRight);

//         // Set corners for scroll2
//         scroll2.setCorner(JScrollPane.LOWER_LEFT_CORNER, scroll2CornerLeft);
//         scroll2.setCorner(JScrollPane.LOWER_RIGHT_CORNER, scroll2CornerRight);

//         // TEST BORDER
//         // scroll1.setBorder(BorderFactory.createLineBorder(REMOVAL_LABEL_COLOR_DARK));

//         // ------------- end

//         // Scroll panes (optional, matches textarea bg)
//         scroll1.getViewport().setBackground(EDITOR_BACKGROUND);
//         scroll2.getViewport().setBackground(EDITOR_BACKGROUND);

//         // Background of the left/right label panels
//         leftLabelPanel.setBackground(EDITOR_BACKGROUND);
//         rightLabelPanel.setBackground(EDITOR_BACKGROUND);

//         // ADD BORDER UPON ACTIVATING TEXAREAS
//         jt1.addFocusListener(new FocusAdapter() {
//             @Override
//             public void focusGained(FocusEvent e) {
//                 jt1IsActive = true;
//                 jt2IsActive = false;
//                 scroll1.setBorder(BorderFactory.createLineBorder(ACTIVE_BORDER_COLOR));
//                 scroll2.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
//             }

//             @Override
//             public void focusLost(FocusEvent e) {
//                 jt1IsActive = false;
//                 scroll1.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
//             }
//         });

//         jt2.addFocusListener(new FocusAdapter() {
//             @Override
//             public void focusGained(FocusEvent e) {
//                 jt2IsActive = true;
//                 jt1IsActive = false;
//                 scroll2.setBorder(BorderFactory.createLineBorder(ACTIVE_BORDER_COLOR));
//                 scroll1.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
//             }

//             @Override
//             public void focusLost(FocusEvent e) {
//                 jt2IsActive = false;
//                 scroll2.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
//             }
//         });

//         // Background click handler
//         addMouseListener(new MouseAdapter() {

//             public void mousePressed(java.awt.event.MouseEvent e) {
//                 Component clicked = SwingUtilities.getDeepestComponentAt(SplitTextTabPanel.this, e.getX(), e.getY());
//                 if (!(SwingUtilities.isDescendingFrom(clicked, jt1) || SwingUtilities.isDescendingFrom(clicked, jt2))) {
//                     requestFocusInWindow(); // steal focus from textareas
//                     repaint(); // helps caret disappear properly
//                 }
//             }
//         });

//         // TEST BORDER
//         // setBorder(BorderFactory.createLineBorder(REMOVAL_LABEL_COLOR_DARK));
//     }

//     private void highlightDiffs() {
//         // Reset counters
//         removed = 0;
//         added = 0;

//         // Clear previous highlights
//         jt1.getHighlighter().removeAllHighlights();
//         jt2.getHighlighter().removeAllHighlights();

//         // Get text content
//         String text1 = jt1.getText();
//         String text2 = jt2.getText();

//         // Split into lines
//         List<String> lines1 = Arrays.asList(text1.split("\n"));
//         List<String> lines2 = Arrays.asList(text2.split("\n"));

//         // Use diff utils
//         Patch<String> patch = DiffUtils.diff(lines1, lines2);

//         Highlighter.HighlightPainter removePainter = new DefaultHighlighter.DefaultHighlightPainter(DELETE_WORD_COLOR);
//         Highlighter.HighlightPainter addPainter = new DefaultHighlighter.DefaultHighlightPainter(ADD_WORD_COLOR);

//         for (AbstractDelta<String> delta : patch.getDeltas()) {
//             int origPos = delta.getSource().getPosition();
//             int revPos = delta.getTarget().getPosition();

//             switch (delta.getType()) {
//                 case DELETE:
//                     removed += delta.getSource().size();
//                     highlightFullLines(jt1, lines1, origPos, delta.getSource().size(), removePainter);
//                     break;
//                 case INSERT:
//                     added += delta.getTarget().size();
//                     highlightFullLines(jt2, lines2, revPos, delta.getTarget().size(), addPainter);
//                     break;
//                 case CHANGE:
//                     removed += delta.getSource().size();
//                     added += delta.getTarget().size();
//                     highlightFullLines(jt1, lines1, origPos, delta.getSource().size(), removePainter);
//                     highlightFullLines(jt2, lines2, revPos, delta.getTarget().size(), addPainter);

//                     // Word-level highlights for changed lines
//                     for (int i = 0; i < Math.min(delta.getSource().size(), delta.getTarget().size()); i++) {
//                         highlightWordDiffs(jt1, lines1.get(origPos + i), getLineStartOffset(jt1, origPos + i),
//                                 removePainter);
//                         highlightWordDiffs(jt2, lines2.get(revPos + i), getLineStartOffset(jt2, revPos + i),
//                                 addPainter);
//                     }
//                     break;
//             }
//         }
//     }

//     private void highlightFullLines(JTextArea jt12, List<String> lines, int startLine, int count,
//             Highlighter.HighlightPainter painter) {
//         for (int i = 0; i < count; i++) {
//             int startOffset = getLineStartOffset(jt12, startLine + i);
//             int endOffset = startOffset + lines.get(startLine + i).length();
//             try {
//                 jt12.getHighlighter().addHighlight(startOffset, endOffset, painter);
//             } catch (BadLocationException e) {
//                 e.printStackTrace();
//             }
//         }
//     }

//     private void highlightWordDiffs(JTextArea jt12, String line, int lineStartOffset,
//             Highlighter.HighlightPainter painter) {
//         // Split by space for word diff
//         String[] words = line.split(" ");
//         for (String word : words) {
//             if (word.trim().isEmpty())
//                 continue;
//             // Apply highlight only if word length > 0
//             try {
//                 int start = lineStartOffset + line.indexOf(word);
//                 int end = start + word.length();
//                 jt12.getHighlighter().addHighlight(start, end, painter);
//             } catch (BadLocationException e) {
//                 e.printStackTrace();
//             }
//         }
//     }

//     private int getLineStartOffset(JTextArea jt12, int line) {
//         try {
//             return jt12.getDocument().getDefaultRootElement().getElement(line).getStartOffset();
//         } catch (Exception e) {
//             return 0;
//         }
//     }

//     // DATABASE
//     public void loadFromDatabase(DiffData data) {
//         jt1.setText(data.leftText);
//         jt2.setText(data.rightText);
//     }

//     private void saveToDatabase() {
//         String title = JOptionPane.showInputDialog(this, "Enter a title for this diff:");
//         if (title == null || title.trim().isEmpty()) {
//             JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         String leftText = jt1.getText();
//         String rightText = jt2.getText();

//         DiffData data = new DiffData(title, leftText, rightText);

//         DB db = new DB();
//         DiffRepository repo = new DiffRepository(db);

//         boolean success = repo.saveDiff(data);
//         if (success) {
//             JOptionPane.showMessageDialog(this, "Diff saved successfully!");
//         } else {
//             JOptionPane.showMessageDialog(this, "Failed to save diff.", "Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }

// }