package com.diffchecker.components;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class SimpleDocumentListener implements DocumentListener {
  private Runnable callback;

  public SimpleDocumentListener(Runnable callback) {
    this.callback = callback;
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    callback.run();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    callback.run();
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    callback.run();
  }
}
