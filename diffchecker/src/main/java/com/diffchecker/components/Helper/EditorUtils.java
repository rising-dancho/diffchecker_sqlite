package com.diffchecker.components.Helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;
import javax.swing.Timer;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;

public class EditorUtils {

  private static final Color EDITOR_BACKGROUND = new Color(0x17181C);
  private static final Color EDITOR_FONT_COLOR = new Color(0xD4D4D4);
  public static final java.util.List<HighlightInfo> highlightPositions = new ArrayList<>();

  public static class HighlightInfo {
    public int startOffset;
    public int endOffset;
    public RSyntaxTextArea area;

    public HighlightInfo(RSyntaxTextArea area, int start, int end) {
      this.area = area;
      this.startOffset = start;
      this.endOffset = end;
    }
  }

  public static RSyntaxTextArea createRSyntaxArea() {
    RSyntaxTextArea localArea = new RSyntaxTextArea();
    localArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
    localArea.setAntiAliasingEnabled(true);
    localArea.setEditable(true); // Allow editing if you still want to diff edited text
    localArea.setBackground(EDITOR_BACKGROUND);
    localArea.setForeground(EDITOR_FONT_COLOR);
    localArea.setCaretColor(EDITOR_FONT_COLOR);
    localArea.setBorder(BorderFactory.createEmptyBorder());
    // localArea.setCodeFoldingEnabled(true);

    // COMMENT AND UNCOMMENT HOTKEY (Ctrl+/ on Win/Linux, Cmd+/ on macOS)
    int menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    InputMap im = localArea.getInputMap();
    ActionMap am = localArea.getActionMap();

    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, menuMask), "toggleComment");
    am.put("toggleComment", new ToggleCommentWrapper(localArea));

    // LINE WRAPPING FOR LONG LINES
    // localArea.setLineWrap(true);
    // localArea.setWrapStyleWord(true); // optional, wraps at word boundaries
    return localArea;
  }

  static class ToggleCommentWrapper extends AbstractAction {
    private final RSyntaxTextArea area;

    public ToggleCommentWrapper(RSyntaxTextArea area) {
      this.area = area;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      Action toggleComment = area.getActionMap().get(RSyntaxTextAreaEditorKit.rstaToggleCommentAction);
      if (toggleComment != null) {
        toggleComment.actionPerformed(e);
      }
    }
  }

  public static String capitalizeTitle(String input) {
    String[] words = input.trim().toLowerCase().split("\\s+");
    StringBuilder result = new StringBuilder();
    for (String word : words) {
      if (word.isEmpty())
        continue;
      result.append(Character.toUpperCase(word.charAt(0)))
          .append(word.substring(1))
          .append(" ");
    }
    return result.toString().trim();
  }

  public static void highlightFullLines(RSyntaxTextArea area, int startLine, int count, Color color) {
    for (int i = 0; i < count; i++) {
      try {
        area.addLineHighlight(startLine + i, color);
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
    }
  }

  // store highlight positions to clear later
  public static void highlightWordDiffs(RSyntaxTextArea area, int lineIndex, String oldLine, String newLine,
      Color color,
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

  // simple toast popup
  public static void showToast(JButton button, String message) {
    // Create a lightweight popup
    JWindow toast = new JWindow();
    toast.setBackground(new Color(0, 0, 0, 0));

    // Style the label
    JLabel label = new JLabel(message);
    label.setOpaque(true);
    label.setBackground(new Color(50, 50, 50, 220)); // semi-transparent dark bg
    label.setForeground(Color.WHITE);
    label.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    label.setFont(label.getFont().deriveFont(Font.PLAIN, 14f));

    toast.add(label);
    toast.pack();

    // Get button location on screen
    Point btnLoc = button.getLocationOnScreen();

    // Position toast directly above the button, centered horizontally
    int x = btnLoc.x + (button.getWidth() - toast.getWidth()) / 2;
    int y = btnLoc.y - toast.getHeight() - 8; // 8px gap above button

    toast.setLocation(x, y);

    // Show and auto-hide
    toast.setVisible(true);

    new Timer(3000, (ActionEvent e) -> toast.dispose()).start(); // disappear after 3s
  }

  // toast popup centered on screen
  public static void showCenteredToast(String message) {
    JWindow toast = new JWindow();
    toast.setBackground(new Color(0, 0, 0, 0));

    JLabel label = new JLabel(message);
    label.setOpaque(true);
    label.setBackground(new Color(50, 50, 50, 220));
    label.setForeground(Color.WHITE);
    label.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    label.setFont(label.getFont().deriveFont(Font.PLAIN, 14f));

    toast.add(label);
    toast.pack();

    // center on screen
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - toast.getWidth()) / 2;
    int y = (screenSize.height - toast.getHeight()) / 2;
    toast.setLocation(x, y);

    toast.setVisible(true);

    new Timer(3000, (ActionEvent e) -> toast.dispose()).start(); // disappear after 3s
  }

  // helper method
  public static void scrollToOffset(JTextComponent area, int offset) {
    try {
      // hide caret so theme doesn't trigger
      Caret caret = area.getCaret();
      boolean wasVisible = caret.isVisible();
      caret.setVisible(false);

      area.setCaretPosition(offset); // moves view
      Rectangle2D viewRect2D = area.modelToView2D(offset);
      if (viewRect2D != null) {
        Rectangle viewRect = viewRect2D.getBounds(); // convert to Rectangle for scrollRectToVisible
        area.scrollRectToVisible(viewRect);
      }

      caret.setVisible(wasVisible); // optional
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

}
