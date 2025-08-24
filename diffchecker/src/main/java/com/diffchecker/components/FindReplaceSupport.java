package com.diffchecker.components;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;
import org.fife.rsta.ui.search.ReplaceDialog;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;

import javax.swing.*;

public class FindReplaceSupport {
    private final ReplaceDialog replaceDialog;
    private final SearchContext searchContext;

    public FindReplaceSupport(JFrame parentFrame, RSyntaxTextArea textArea) {
        this.searchContext = new SearchContext();
        this.searchContext.setSearchWrap(true);

        SearchListener listener = new SearchListener() {
            @Override
            public void searchEvent(SearchEvent e) {
                SearchResult result = switch (e.getType()) {
                    case FIND -> SearchEngine.find(textArea, e.getSearchContext());
                    case REPLACE -> SearchEngine.replace(textArea, e.getSearchContext());
                    case REPLACE_ALL -> SearchEngine.replaceAll(textArea, e.getSearchContext());
                    case MARK_ALL -> SearchEngine.markAll(textArea, e.getSearchContext());
                };
                if (result != null && !result.wasFound()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                }
            }

            @Override
            public String getSelectedText() {
                return textArea.getSelectedText();
            }
        };

        this.replaceDialog = new ReplaceDialog(parentFrame, listener);
        this.replaceDialog.setSearchContext(searchContext);

        // Register shortcuts
        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();

        im.put(KeyStroke.getKeyStroke("control F"), "Replace");
        im.put(KeyStroke.getKeyStroke("control H"), "Replace");

        am.put("Replace", getReplaceAction());
    }

    public void showReplaceDialog() {
        replaceDialog.setVisible(true);
    }

    /** ✅ Extracted Action so both keyboard and button can use the same behavior */
    public Action getReplaceAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                showReplaceDialog();
            }
        };
    }
}
