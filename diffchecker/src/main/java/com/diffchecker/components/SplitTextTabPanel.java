package com.diffchecker.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import com.diffchecker.components.Database.DB;
import com.diffchecker.components.Database.DiffData;
import com.diffchecker.components.Database.DiffRepository;
import com.diffchecker.components.Helper.EditorUtils;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// RSyntaxTextArea dependencies
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

// RSyntaxTextArea search and replace dependencies
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class SplitTextTabPanel extends JPanel {
    // WORD HIGHLIGHT
    private static final Color LINE_REMOVED_DARK = new Color(0x40191D);
    private static final Color LINE_ADDED_DARK = new Color(0x12342B);
    private static final Color WORD_REMOVED_DARK = new Color(0x8B1E1D);
    private static final Color WORD_ADDED_DARK = new Color(0x137B5A);

    // private static final Color LINE_REMOVED_DARK = new Color(0x40191D);
    // private static final Color LINE_ADDED_DARK = new Color(0x12342B);
    // private static final Color WORD_REMOVED_DARK = new Color(0x8B1E1D);
    // private static final Color WORD_ADDED_DARK = new Color(0x137B5A);

    // EDITOR COLORS
    private static final Color EDITOR_BACKGROUND = new Color(0x17181C); // Dark gray
    private static final Color EDITOR_BORDER_COLOR = new Color(0x242526); // Light text
    private static final Color ACTIVE_BORDER_COLOR = new Color(0x00744d);

    // BACKGROUND COLOR
    private final Color BACKGROUND_DARK = new Color(0xffffff);

    // BUTTON COLOR AND HOVER COLOR
    private static final Color BTN_COLOR = new Color(0x00af74);
    private static final Color BTN_COLOR_DARKER = new Color(0x00744d);
    private static final Color BTN_COLOR_BLACK = new Color(0x242526);

    // Keep the XML's size
    private static final int sizeFromXML = 14; // match your <baseFont size="14"/>

    // DEFAULT DECLARATIONS
    private RSyntaxTextArea jt1;
    private RSyntaxTextArea jt2;

    private final RTextScrollPane scroll1;
    private final RTextScrollPane scroll2;

    private FindReplaceSupport findReplace1;
    private FindReplaceSupport findReplace2;

    private RoundedButton highlightToggleBtn;
    private RoundedButton wordWrapToggleBtn;

    // CHECKING IF GREEN BORDER IS ACTIVE OR NOT
    private boolean jt1IsActive = false;
    private boolean jt2IsActive = false;

    // THEME MANAGEMENT
    private boolean darkThemeEnabled = true;

    // FOR NAVIGATING DIFFS
    private final List<DiffGroup> diffGroups = new ArrayList<>();
    private int currentGroupIndex = -1;
    private DiffData currentDiff; // to track the saved diff record

    private static class DiffGroup {
        EditorUtils.HighlightInfo left;
        EditorUtils.HighlightInfo right;
    }

    // TOGGLE WORD HIGHLIGHT
    private boolean wordHighlightEnabled = false;

    // TOGGLE WORD WRAP
    private boolean wordWrapEnabled = false;

    private RSyntaxTextArea lastFocusedEditor;

    private final FocusAdapter trackFocus = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (e.getComponent() instanceof RSyntaxTextArea) {
                lastFocusedEditor = (RSyntaxTextArea) e.getComponent();
            }
        }
    };

    // TRACKING UNSAVED CHANGES
    private boolean isDirty = false;

    // DOCUMENT LISTENERS TO TRACK CHANGES
    private void markDirty() {
        if (!isDirty) {
            isDirty = true;
            // Append * to tab title
            Container parent = getParent();
            while (parent != null && !(parent instanceof JTabbedPane)) {
                parent = parent.getParent();
            }
            if (parent instanceof JTabbedPane) {
                JTabbedPane tabbedPane = (JTabbedPane) parent;
                int index = tabbedPane.indexOfComponent(this);
                if (index != -1) {
                    String title = tabbedPane.getTitleAt(index);
                    if (!title.endsWith("*")) {
                        tabbedPane.setTitleAt(index, title + "*");
                    }
                }
            }
        }
    }

    private void markSaved() {
        isDirty = false;
    }

    public boolean hasUnsavedChanges() {
        return isDirty;
    }

    private final DocumentListener dirtyListener1 = new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            markDirty();
        }

        public void removeUpdate(DocumentEvent e) {
            markDirty();
        }

        public void changedUpdate(DocumentEvent e) {
            markDirty();
        }
    };

    private final DocumentListener dirtyListener2 = new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            markDirty();
        }

        public void removeUpdate(DocumentEvent e) {
            markDirty();
        }

        public void changedUpdate(DocumentEvent e) {
            markDirty();
        }
    };

    @Override
    public void addNotify() {
        super.addNotify();
        if (findReplace1 == null || findReplace2 == null) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                findReplace1 = new FindReplaceSupport(frame, jt1);
                findReplace2 = new FindReplaceSupport(frame, jt2);
            }
        }
    }

    public SplitTextTabPanel() {
        setLayout(new BorderLayout());
        setKeyboardShortcuts();

        // DECLARE TEXT AREAS
        jt1 = EditorUtils.createRSyntaxArea();
        jt2 = EditorUtils.createRSyntaxArea();

        jt1.setCodeFoldingEnabled(true);
        jt2.setCodeFoldingEnabled(true);

        // TRACK EDITOR CHANGES
        jt1.getDocument().addDocumentListener(dirtyListener1);
        jt2.getDocument().addDocumentListener(dirtyListener2);

        // TRACK FOCUS
        jt1.addFocusListener(trackFocus);
        jt2.addFocusListener(trackFocus);

        // DISABLE CURRENT LINE HIGHLIGHTING SINCE IT CLASHES WITH DIFF HIGHLIGHT
        jt1.setHighlightCurrentLine(false);
        jt2.setHighlightCurrentLine(false);

        // Disable mark occurrences (highlights and tooltips for matching tokens)
        jt1.setMarkOccurrences(false);
        jt2.setMarkOccurrences(false);

        // Disable matched bracket popup tooltips
        jt1.setBracketMatchingEnabled(false);
        jt2.setBracketMatchingEnabled(false);

        scroll1 = new RTextScrollPane(jt1);
        scroll2 = new RTextScrollPane(jt2);

        // CUSTOM SCROLLBARS
        scroll1.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scroll2.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scroll1.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        scroll2.getHorizontalScrollBar().setUI(new CustomScrollBarUI());

        scroll1.getHorizontalScrollBar().setOpaque(true);
        scroll1.getHorizontalScrollBar().setBackground(BACKGROUND_DARK);

        scroll2.getHorizontalScrollBar().setOpaque(true);
        scroll2.getHorizontalScrollBar().setBackground(BACKGROUND_DARK);
        scroll1.getVerticalScrollBar().setOpaque(true);
        scroll1.getVerticalScrollBar().setBackground(BACKGROUND_DARK);

        scroll2.getVerticalScrollBar().setOpaque(true);
        scroll2.getVerticalScrollBar().setBackground(BACKGROUND_DARK);

        scroll1.setOpaque(false);
        scroll1.getViewport().setOpaque(false);

        scroll2.setOpaque(false);
        scroll2.getViewport().setOpaque(false);

        // REMOVE DEFAULT BORDERS
        jt1.setBorder(BorderFactory.createEmptyBorder());
        jt2.setBorder(BorderFactory.createEmptyBorder());

        // SYNCHRONIZED VERTICAL SCROLLING
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

        scroll1.setBorder(null);
        scroll2.setBorder(null);

        scroll1.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));
        scroll2.setBorder(BorderFactory.createLineBorder(EDITOR_BORDER_COLOR));

        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(scroll1, BorderLayout.CENTER);

        JPanel p2 = new JPanel(new BorderLayout());
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
        // Hook the 🔍 button
        findBtn.addActionListener(e -> {
            RSyntaxTextArea target = lastFocusedEditor;
            if (target == jt1 && findReplace1 != null) {
                findReplace1.getReplaceAction().actionPerformed(
                        new java.awt.event.ActionEvent(jt1, ActionEvent.ACTION_PERFORMED, "Replace"));
            } else if (target == jt2 && findReplace2 != null) {
                findReplace2.getReplaceAction().actionPerformed(
                        new java.awt.event.ActionEvent(jt2, ActionEvent.ACTION_PERFORMED, "Replace"));
            } else {
                EditorUtils.showToast(findBtn,
                        "<html>Click inside one of the <strong>text editors</strong> first, <br> then press the &nbsp\" &nbsp 🔍 &nbsp \"&nbsp button to use <strong>Find/Replace</strong></html>");
            }
        });

        highlightToggleBtn = new RoundedButton();
        highlightToggleBtn.setText(null);
        // Load local SVG (supports recoloring and scaling)
        FlatSVGIcon highlightIcon = new FlatSVGIcon("diffchecker/images/icons/highlight.svg", 20, 20);
        // turn the icon monochrome white
        highlightIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.WHITE));
        highlightToggleBtn.setIcon(highlightIcon);
        highlightToggleBtn.setBackgroundColor(BTN_COLOR_BLACK); // <- normal color
        highlightToggleBtn.setHoverBackgroundColor(BTN_COLOR_DARKER); // <- hover color
        highlightToggleBtn.setBorderColor(BTN_COLOR_BLACK);// <- normal color
        highlightToggleBtn.setHoverBorderColor(BTN_COLOR_DARKER); // <- hover color
        highlightToggleBtn.setBorderThickness(2);
        highlightToggleBtn.setCornerRadius(10);
        highlightToggleBtn.setMargin(new Insets(5, 5, 5, 5));
        highlightToggleBtn.addActionListener(e -> {
            wordHighlightEnabled = !wordHighlightEnabled; // toggle state
            highlightToggleBtn.setSelectedState(wordHighlightEnabled);

            try {
                // clear old highlights
                jt1.getHighlighter().removeAllHighlights();
                jt2.getHighlighter().removeAllHighlights();
                EditorUtils.highlightPositions.clear();
                highlightDiffs();
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });

        wordWrapToggleBtn = new RoundedButton();
        highlightToggleBtn.setText(null);
        // Load local SVG (supports recoloring and scaling)
        FlatSVGIcon wordWrapIcon = new FlatSVGIcon("diffchecker/images/icons/wrap_text.svg", 20, 20);
        // turn the icon monochrome white
        wordWrapIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.WHITE));
        wordWrapToggleBtn.setIcon(wordWrapIcon);
        wordWrapToggleBtn.setBackgroundColor(BTN_COLOR_BLACK); // <- normal color
        wordWrapToggleBtn.setHoverBackgroundColor(BTN_COLOR_DARKER); // <- hover color
        wordWrapToggleBtn.setBorderColor(BTN_COLOR_BLACK);// <- normal color
        wordWrapToggleBtn.setHoverBorderColor(BTN_COLOR_DARKER); // <- hover color
        wordWrapToggleBtn.setBorderThickness(2);
        wordWrapToggleBtn.setCornerRadius(10);
        wordWrapToggleBtn.setMargin(new Insets(5, 5, 5, 5));
        wordWrapToggleBtn.addActionListener(e -> {
            wordWrapToggle();
        });
        RoundedButton previousBtn = new RoundedButton("◀️");
        previousBtn.setBackgroundColor(BTN_COLOR_BLACK); // <- normal color
        previousBtn.setHoverBackgroundColor(BTN_COLOR_DARKER); // <- hover color
        previousBtn.setBorderColor(BTN_COLOR_BLACK);// <- normal color
        previousBtn.setHoverBorderColor(BTN_COLOR_DARKER); // <- hover color
        previousBtn.setBorderThickness(2);
        previousBtn.setCornerRadius(10);
        previousBtn.setMargin(new Insets(5, 10, 5, 0));
        previousBtn.addActionListener(e -> {
            previousDiff();
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
            nextDiff();
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
        });

        RoundedButton deleteBtn = new RoundedButton("✖");
        deleteBtn.setBackgroundColor(BTN_COLOR_BLACK);
        deleteBtn.setHoverBackgroundColor(BTN_COLOR_DARKER);
        deleteBtn.setBorderColor(BTN_COLOR_BLACK);
        deleteBtn.setHoverBorderColor(BTN_COLOR_DARKER);
        deleteBtn.setBorderThickness(2);
        deleteBtn.setCornerRadius(10);
        deleteBtn.addActionListener(e -> {
            deleteDiff();
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

        // TOOLTIPS
        diffcheckBtn.setToolTipText("<html><strong>Find Difference</strong> <br> ( Alt + Shift + Enter )</html>");
        previousBtn.setToolTipText("<html><strong>Previous Diff</strong> <br> ( Alt + Left Arrow )</html>");
        nextBtn.setToolTipText("<html><strong>Next Diff</strong> <br> ( Alt + Right Arrow )</html>");
        clearBtn.setToolTipText("<html><strong>Clear</strong> <br> ( Ctrl + R )</html>");
        deleteBtn.setToolTipText("<html><strong>Delete</strong> <br> ( Ctrl + Shift + X )</html>");
        findBtn.setToolTipText("<html><strong>Find/Replace</strong> <br> ( Ctrl + F )</html>");
        saveBtn.setToolTipText("<html><strong>Save</strong> <br> ( Ctrl + S )</html>");
        highlightToggleBtn.setToolTipText("<html><strong>Toggle Word Highlight</strong> <br> ( Alt + E )</html>");
        wordWrapToggleBtn.setToolTipText("<html><strong>Toggle Word Wrap</strong> <br> ( Alt + Q )</html>");

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
        centerButtonPanel.add(wordWrapToggleBtn);
        centerButtonPanel.add(highlightToggleBtn);
        centerButtonPanel.add(diffcheckBtn);
        centerButtonPanel.add(previousBtn);
        centerButtonPanel.add(nextBtn);
        bottomPanel.add(centerButtonPanel, BorderLayout.CENTER);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.setBackground(BACKGROUND_DARK);
        rightButtonPanel.add(findBtn);
        rightButtonPanel.add(saveBtn);
        bottomPanel.add(rightButtonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // REMOVE WHITE SQUARES AT THE CORNERS OF THE SCROLLBARS
        removingWhiteSquaresAtCornersOfScrollBars();

        // APPLY ACTIVATED BORDER STYLE
        activatedEditorBorderStyle();

        // PRE APPLY THEME TO RUN DEFAULT
        applyTheme(darkThemeEnabled);
    }

    // KEYBOARD SHORTCUTS
    public void setKeyboardShortcuts() {
        // CTRL + S HOTKEY FOR SAVING
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("control S"), "saveDiff");

        getActionMap().put("saveDiff", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToDatabase();
            }
        });

        // CTRL + Q HOTKEY FOR TOGGLING WORD WRAP
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("alt Q"), "toggleWordWrap");

        getActionMap().put("toggleWordWrap", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wordWrapToggle();
            }
        });

        // CTRL + E HOTKEY FOR TOGGLING WORD HIGHLIGHT
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("alt E"), "toggleHighlightWord");

        getActionMap().put("toggleHighlightWord", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightWordToggle();
            }
        });

        // CTRL + SHIFT + ENTER hotkey for diff checking
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                        "highlightDiffs");

        getActionMap().put("highlightDiffs", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    highlightDiffs();
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // CTRL + SHIFT + X hotkey for Deleting from database
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                        "deleteDiff");

        getActionMap().put("deleteDiff", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteDiff();
            }
        });

        // ALT + LEFT = Previous diff
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK), "previousDiff");

        getActionMap().put("previousDiff", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousDiff();
            }
        });

        // ALT + RIGHT = Next diff
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK), "nextDiff");

        getActionMap().put("nextDiff", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextDiff();
            }
        });

        // CTRL + R HOTKEY FOR CLEARING
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("control R"), "clearTextAreas");

        getActionMap().put("clearTextAreas", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jt1.setText("");
                jt2.setText("");
            }
        });

        // CTRL + T HOTKEY FOR TOGGLING THEME
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("control G"), "toggleTheme");

        getActionMap().put("toggleTheme", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                darkThemeEnabled = !darkThemeEnabled;
                applyTheme(darkThemeEnabled);
            }
        });
    }

    public void removingWhiteSquaresAtCornersOfScrollBars() {
        // REMOVING THE WHITE SQUARES AT THE INTERSCTION OF THE SCROLLBARS
        // Unique corners for scroll1
        JPanel scroll1CornerLeft = new JPanel();
        scroll1CornerLeft.setBackground(BACKGROUND_DARK);
        JPanel scroll1CornerRight = new JPanel();
        scroll1CornerRight.setBackground(BACKGROUND_DARK);

        // Unique corners for scroll2
        JPanel scroll2CornerLeft = new JPanel();
        scroll2CornerLeft.setBackground(BACKGROUND_DARK);
        JPanel scroll2CornerRight = new JPanel();
        scroll2CornerRight.setBackground(BACKGROUND_DARK);

        // Set corners for scroll1
        scroll1.setCorner(JScrollPane.LOWER_LEFT_CORNER, scroll1CornerLeft);
        scroll1.setCorner(JScrollPane.LOWER_RIGHT_CORNER, scroll1CornerRight);

        // Set corners for scroll2
        scroll2.setCorner(JScrollPane.LOWER_LEFT_CORNER, scroll2CornerLeft);
        scroll2.setCorner(JScrollPane.LOWER_RIGHT_CORNER, scroll2CornerRight);
    }

    public void activatedEditorBorderStyle() {
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

        // STEAL FOCUS FROM TEXTAREAS WHEN CLICKING OUTSIDE
        addMouseListener(new MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent e) {
                Component clicked = SwingUtilities.getDeepestComponentAt(SplitTextTabPanel.this, e.getX(), e.getY());
                if (!(SwingUtilities.isDescendingFrom(clicked, jt1) || SwingUtilities.isDescendingFrom(clicked, jt2))) {
                    requestFocusInWindow(); // steal focus from textareas
                    repaint(); // helps caret disappear properly
                }
            }
        });
    }

    private void applyTheme(boolean dark) {
        // Color bg, fg, border, caret;

        // if (dark) {
        // // DARK THEME
        // bg = EDITOR_BACKGROUND;
        // fg = EDITOR_FONT_COLOR;
        // border = EDITOR_BORDER_COLOR;
        // caret = EDITOR_CARRET_COLOR;
        // } else {
        // // LIGHT THEME
        // bg = Color.WHITE;
        // fg = Color.BLACK;
        // border = EDITOR_BORDER_COLOR;
        // caret = EDITOR_CARRET_COLOR;
        // }

        // jt2.setBackground(bg);
        // jt2.setForeground(fg);
        // jt2.setCaretColor(caret);

        // // Apply to both editors
        // jt1.setBackground(bg);
        // jt1.setForeground(fg);
        // jt1.setCaretColor(caret);

        // jt2.setBackground(bg);
        // jt2.setForeground(fg);
        // jt2.setCaretColor(caret);

        // revalidate();
        // repaint();

        String themePath = dark ? "/diffchecker/themes/dark.xml"
                : "/diffchecker/themes/light.xml";

        try (InputStream in = getClass().getResourceAsStream(themePath)) {
            if (in != null) {
                Theme theme = Theme.load(in);
                theme.apply(jt1);
                theme.apply(jt2);

                // If you also want scroll pane borders/background consistent:
                Color bg = jt1.getBackground();
                scroll1.getViewport().setBackground(bg);
                scroll2.getViewport().setBackground(bg);

                // Load embedded Fira Code
                InputStream fontStream = getClass().getResourceAsStream("/diffchecker/fonts/FiraCode-Regular.ttf");
                Font firaCode = Font.createFont(Font.TRUETYPE_FONT, fontStream);

                firaCode = firaCode.deriveFont(Font.PLAIN, sizeFromXML);

                jt1.setFont(firaCode);
                jt2.setFont(firaCode);
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    // APPLY SYNTAX HIGHLIGHTING : CONNECTED TO CUSTOM TITLE BAR
    public void setSyntaxStyleBoth(String style) {
        if (jt1 != null)
            jt1.setSyntaxEditingStyle(style);
        if (jt2 != null)
            jt2.setSyntaxEditingStyle(style);
    }

    private void highlightWordToggle() {
        wordHighlightEnabled = !wordHighlightEnabled; // toggle state
        highlightToggleBtn.setSelectedState(wordHighlightEnabled);

        try {
            // clear old highlights
            jt1.getHighlighter().removeAllHighlights();
            jt2.getHighlighter().removeAllHighlights();
            EditorUtils.highlightPositions.clear();
            highlightDiffs();
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void wordWrapToggle() {
        wordWrapEnabled = !wordWrapEnabled; // toggle state
        wordWrapToggleBtn.setSelectedState(wordWrapEnabled);

        jt1.setLineWrap(wordWrapEnabled);
        jt1.setWrapStyleWord(wordWrapEnabled);

        jt2.setLineWrap(wordWrapEnabled);
        jt2.setWrapStyleWord(wordWrapEnabled);

        // Force UI refresh
        jt1.revalidate();
        jt1.repaint();
        jt2.revalidate();
        jt2.repaint();
    }

    private void focusDiffGroup(DiffGroup group) {
        if (group == null)
            return;

        // // Steal focus from text areas temporarily
        requestFocusInWindow();

        if (group.left != null) {
            EditorUtils.scrollToOffset(jt1, group.left.startOffset);
        }

        if (group.right != null) {
            EditorUtils.scrollToOffset(jt2, group.right.startOffset);
        }

    }

    private void previousDiff() {
        if (diffGroups.isEmpty())
            return;
        currentGroupIndex--;
        if (currentGroupIndex < 0)
            currentGroupIndex = diffGroups.size() - 1;
        focusDiffGroup(diffGroups.get(currentGroupIndex));
    }

    private void nextDiff() {
        if (diffGroups.isEmpty())
            return;
        currentGroupIndex++;
        if (currentGroupIndex >= diffGroups.size())
            currentGroupIndex = 0;
        focusDiffGroup(diffGroups.get(currentGroupIndex));
    }

    private void deleteDiff() {
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
                    EditorUtils.highlightFullLines(jt1, origPos, delta.getSource().size(), LINE_REMOVED_DARK);
                    int startOffsetLeft = jt1.getLineStartOffset(origPos);
                    group.left = new EditorUtils.HighlightInfo(jt1, startOffsetLeft, startOffsetLeft);
                    break;

                case INSERT:
                    EditorUtils.highlightFullLines(jt2, revPos, delta.getTarget().size(), LINE_ADDED_DARK);
                    int startOffsetRight = jt2.getLineStartOffset(revPos);
                    group.right = new EditorUtils.HighlightInfo(jt2, startOffsetRight, startOffsetRight);
                    break;

                case CHANGE:
                    EditorUtils.highlightFullLines(jt1, origPos, delta.getSource().size(), LINE_REMOVED_DARK);
                    EditorUtils.highlightFullLines(jt2, revPos, delta.getTarget().size(), LINE_ADDED_DARK);

                    int lOff = jt1.getLineStartOffset(origPos);
                    int rOff = jt2.getLineStartOffset(revPos);
                    group.left = new EditorUtils.HighlightInfo(jt1, lOff, lOff);
                    group.right = new EditorUtils.HighlightInfo(jt2, rOff, rOff);

                    // Word-level highlighting (only if toggle ON)
                    if (wordHighlightEnabled) {
                        for (int i = 0; i < Math.min(delta.getSource().size(), delta.getTarget().size()); i++) {
                            EditorUtils.highlightWordDiffs(
                                    jt1, origPos + i,
                                    delta.getSource().getLines().get(i),
                                    delta.getTarget().getLines().get(i),
                                    WORD_REMOVED_DARK, true);
                            EditorUtils.highlightWordDiffs(
                                    jt2, revPos + i,
                                    delta.getSource().getLines().get(i),
                                    delta.getTarget().getLines().get(i),
                                    WORD_ADDED_DARK, false);
                        }
                    }
                    break;
                default:
                    break;
            }

            diffGroups.add(group);
        }
    }

    // LOAD/STORE FROM DATABASE
    public void loadFromDatabase(DiffData data) {
        currentDiff = data;

        // temporarily detach listeners
        jt1.getDocument().removeDocumentListener(dirtyListener1);
        jt2.getDocument().removeDocumentListener(dirtyListener2);

        jt1.setText(data.leftText);
        jt2.setText(data.rightText);

        // reattach listeners
        jt1.getDocument().addDocumentListener(dirtyListener1);
        jt2.getDocument().addDocumentListener(dirtyListener2);

        markSaved(); // reset dirty flag
    }

    public void saveToDatabase() {
        String title = JOptionPane.showInputDialog(
                this,
                "What's the title of this diff?",
                currentDiff != null ? currentDiff.title : "");

        // User pressed Cancel or closed the dialog → do nothing
        if (title == null) {
            return;
        }

        // User pressed OK but left input blank → show error and stop
        if (title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Title cannot be empty.",
                    "Invalid Title",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Capitalize the title
        title = EditorUtils.capitalizeTitle(title);

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

        currentDiff.leftText = jt1.getText();
        currentDiff.rightText = jt2.getText();

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
            markSaved();
        }
    }

    // GHOST CODES FOR REVIEW PURPOSES
    public void ghostCodes() {
    }

}