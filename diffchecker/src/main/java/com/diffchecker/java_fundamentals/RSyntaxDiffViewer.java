package com.diffchecker.java_fundamentals;

import java.awt.*;
import java.awt.event.AdjustmentListener;
import java.util.List;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

public class RSyntaxDiffViewer extends JPanel {

  private RSyntaxTextArea leftArea;
  private RSyntaxTextArea rightArea;

  private static final Color LINE_REMOVED = new Color(0x40191D);
  private static final Color LINE_ADDED = new Color(0x12342B);
  private static final Color WORD_REMOVED = new Color(0x8B1E1D);
  private static final Color WORD_ADDED = new Color(0x137B5A);

  public RSyntaxDiffViewer(String leftText, String rightText, String syntaxStyle) {
    setLayout(new GridLayout(1, 2));

    leftArea = createArea(leftText, syntaxStyle);
    rightArea = createArea(rightText, syntaxStyle);

    RTextScrollPane spLeft = new RTextScrollPane(leftArea);
    RTextScrollPane spRight = new RTextScrollPane(rightArea);

    syncScroll(spLeft, spRight);

    add(spLeft);
    add(spRight);

    highlightDiffs(leftText, rightText);
  }

  private RSyntaxTextArea createArea(String text, String syntaxStyle) {
    RSyntaxTextArea area = new RSyntaxTextArea(30, 60);
    area.setText(text);
    area.setSyntaxEditingStyle(syntaxStyle); // e.g., SyntaxConstants.SYNTAX_STYLE_JAVA
    area.setCodeFoldingEnabled(true);
    area.setEditable(false);
    area.setAntiAliasingEnabled(true);
    return area;
  }

  private void syncScroll(RTextScrollPane sp1, RTextScrollPane sp2) {
    AdjustmentListener syncScroll = e -> {
      Adjustable source = e.getAdjustable();
      JScrollBar targetBar = (source == sp1.getVerticalScrollBar())
          ? sp2.getVerticalScrollBar()
          : sp1.getVerticalScrollBar();
      targetBar.setValue(source.getValue());
    };
    sp1.getVerticalScrollBar().addAdjustmentListener(syncScroll);
    sp2.getVerticalScrollBar().addAdjustmentListener(syncScroll);
  }

  private void highlightDiffs(String leftText, String rightText) {
    List<String> leftLines = Arrays.asList(leftText.split("\n"));
    List<String> rightLines = Arrays.asList(rightText.split("\n"));

    Patch<String> patch = DiffUtils.diff(leftLines, rightLines);

    for (AbstractDelta<String> delta : patch.getDeltas()) {
      int origPos = delta.getSource().getPosition();
      int revPos = delta.getTarget().getPosition();

      switch (delta.getType()) {
        case DELETE:
          highlightFullLines(leftArea, origPos, delta.getSource().size(), LINE_REMOVED);
          break;
        case INSERT:
          highlightFullLines(rightArea, revPos, delta.getTarget().size(), LINE_ADDED);
          break;
        case CHANGE:
          highlightFullLines(leftArea, origPos, delta.getSource().size(), LINE_REMOVED);
          highlightFullLines(rightArea, revPos, delta.getTarget().size(), LINE_ADDED);

          for (int i = 0; i < Math.min(delta.getSource().size(), delta.getTarget().size()); i++) {
            highlightWordDiffs(leftArea, origPos + i, delta.getSource().getLines().get(i),
                delta.getTarget().getLines().get(i), WORD_REMOVED, true);
            highlightWordDiffs(rightArea, revPos + i, delta.getSource().getLines().get(i),
                delta.getTarget().getLines().get(i), WORD_ADDED, false);
          }
          break;
      }
    }
  }

  private void highlightFullLines(RSyntaxTextArea area, int startLine, int count, Color color) {
    for (int i = 0; i < count; i++) {
      try {
        int startOffset = area.getLineStartOffset(startLine + i);
        int endOffset = area.getLineEndOffset(startLine + i);
        area.addLineHighlight(startLine + i, color);
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
    }
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
        }
        pos += token.length();
      }
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      String text1 = " switch (delta.getType()) {\r\n" + //
          "        case DELETE:\r\n" + //
          "          highlightFullLines(leftArea, origPos, delta.getSource().size(), LINE_REMOVED);\r\n" + //
          "          break;\r\n" + //
          "        case INSERT:\r\n" + //
          "          highlightFullLines(rightArea, revPos, delta.getTarget().size(), LINE_ADDED);\r\n" + //
          "          break;\r\n" + //
          "        case CHANGE:\r\n" + //
          "          highlightFullLines(leftArea, origPos, delta.getSource().size(), LINE_REMOVED);\r\n" + //
          "          highlightFullLines(rightArea, revPos, itlog.getTarget().size(), LINE_ADDED);\r\n" + //
          "\r\n" + //
          "          for (int i = 0; i < Math.min(delta.getSource().size(), delta.getTarget().size()); i++) {\r\n" + //
          "            highlightWordDiffs(leftArea, origPos + i, delta.getSource().getLines().get(i),\r\n" + //
          "                delta.getTarget().getLines().get(i), WORD_REMOVED, true);\r\n" + //
          "            highlightWordDiffs(rightArea, revPos + i, delta.getSource().getLines().get(i),\r\n" + //
          "                delta.getTarget().getLines().get(i), WORD_ADDED, false);\r\n" + //
          "          }\r\n" + //
          "          break;\r\n" + //
          "      }\r\n" + //
          "    }";
      String text2 = " switch (delta.getType()) {\r\n" + //
          "        case DELETE:\r\n" + //
          "          highlightFullLines(leftArea, origPos, delta.getSource().size(), LINE_REMOVED);\r\n" + //
          "          break;\r\n" + //
          "        case INSERT:\r\n" + //
          "          highlightFullLines(rightArea, lel, delta.getTarget().size(), LINE_ADDED);\r\n" + //
          "          break;\r\n" + //
          "        case CHANGE:\r\n" + //
          "          highlightFullLines(rightArea, revPos, delta.getTarget().size(), LINE_ADDED);\r\n" + //
          "\r\n" + //
          "          for (int i = 0; i < Math.min(delta.getSource().size(), delta.getTarget().size()); i++) {\r\n" + //
          "            highlightWordDiffs(leftArea, origPos + i, delta.getSource().getLines().get(i),\r\n" + //
          "                delta.getTarget().getLines().get(i), WORD_REMOVED, true);\r\n" + //
          "            highlightWordDiffs(rightArea, revPos + i, delta.getSource().getLines().get(i),\r\n" + //
          "                delta.getTarget().getLines().get(i), WORD_ADDED, false);\r\n" + //
          "          }\r\n" + //
          "          break;\r\n" + //
          "      }\r\n" + //
          "    }";

      RSyntaxDiffViewer viewer = new RSyntaxDiffViewer(
          text1, text2, SyntaxConstants.SYNTAX_STYLE_JAVA);

      // Load theme from resources
      try {
        Theme theme = Theme.load(
            RSyntaxDiffViewer.class.getResourceAsStream("/diffchecker/themes/mytheme.xml"));
        theme.apply(viewer.leftArea);
        theme.apply(viewer.rightArea);
      } catch (Exception e) {
        e.printStackTrace();
      }

      JFrame frame = new JFrame("RSyntax Diff Viewer");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1000, 600);
      frame.add(viewer);
      frame.setVisible(true);
    });
  }

}
