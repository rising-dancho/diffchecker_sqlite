package com.diffchecker.components;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class CustomLineNumbers extends RTextScrollPane {

  private static final Color BACKGROUND_COLOR = new Color(0x17181C); // Dark gray
  private static final Color NUMBER_COLOR = new Color(0x5f5e65); // Light line number
  private static final Font LINE_NUMBER_FONT = new Font("Consolas", Font.BOLD, 16);
  private static final String PACKAGE_NAME = "diffchecker";

  private final RSyntaxTextArea textArea;

  public CustomLineNumbers(RSyntaxTextArea textArea) {
    super(textArea);
    this.textArea = textArea;

    // Enable code folding
    this.textArea.setCodeFoldingEnabled(true);

    // Customize gutter
    Gutter gutter = this.getGutter();
    gutter.setBackground(BACKGROUND_COLOR);
    gutter.setLineNumberColor(NUMBER_COLOR);
    gutter.setLineNumberFont(LINE_NUMBER_FONT);

    try {
      InputStream is = getClass().getResourceAsStream(
          "/" + PACKAGE_NAME + "/fonts/FiraCode-Regular.ttf");
      Font firaCode;
      firaCode = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 16f);
      this.textArea.setFont(firaCode);
    } catch (FontFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public RSyntaxTextArea getTextArea() {
    return textArea;
  }
}
