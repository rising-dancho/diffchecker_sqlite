package com.diffchecker.components;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClosableTabContextMenu extends MouseAdapter {
    private final JTabbedPane tabbedPane;

    public ClosableTabContextMenu(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    private void maybeShowPopup(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e) && e.isPopupTrigger()) {
            int index = tabbedPane.indexAtLocation(e.getX(), e.getY());
            if (index != -1 && index != 0) {
                JPopupMenu popup = new JPopupMenu();
                JMenuItem delete = new JMenuItem("Delete Tab");

                delete.addActionListener(ev -> tabbedPane.remove(index));

                popup.add(delete);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    @Override public void mousePressed(MouseEvent e)  { maybeShowPopup(e); }
    @Override public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
}
