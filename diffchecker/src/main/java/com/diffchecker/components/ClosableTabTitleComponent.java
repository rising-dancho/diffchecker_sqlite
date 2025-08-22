package com.diffchecker.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClosableTabTitleComponent extends JPanel {
    private final Color ACTIVE_COLOR = new Color(0xF9FAFA); // Active tab color
    private final Color INACTIVE_COLOR = new Color(0x888690); // Inactive tab color

    private final JLabel titleLabel;
    private final Color HOVER_COLOR = new Color(0xd6d6d6); // your desired hover text color
    private boolean isHovered = false;
    private final JTabbedPane tabbedPane;

    public void setHovered(boolean hovered) {
        if (hovered != isHovered) {
            isHovered = hovered;
            updateColor();
        }
    }

    // FOR UPDATING THE TAB TITLE DYNAMICALLY
    public void setTitle(String title) {
        titleLabel.setText(title);
        revalidate();
        repaint();
    }

    public String getTitle() {
        return titleLabel.getText();
    }

    private void updateColor() {
        int index = tabbedPane.indexOfTabComponent(this);
        if (index == tabbedPane.getSelectedIndex()) {
            titleLabel.setForeground(ACTIVE_COLOR);
        } else if (isHovered) {
            titleLabel.setForeground(HOVER_COLOR);
        } else {
            titleLabel.setForeground(INACTIVE_COLOR);
        }
    }

    public ClosableTabTitleComponent(JTabbedPane tabbedPane, String title, Runnable onTabEmptyFallback) {

        super(new BorderLayout(10, 0)); // add horizontal gap between label and button
        this.tabbedPane = tabbedPane;
        setOpaque(false);

        titleLabel = new JLabel(title);

        // CHANGE FONT COLOR OF TAB TITLE
        // Add a listener to repaint text color when tab selection changes
        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.indexOfTabComponent(this);
            if (index != -1) {
                if (index == tabbedPane.getSelectedIndex()) {
                    titleLabel.setForeground(ACTIVE_COLOR);
                } else {
                    titleLabel.setForeground(INACTIVE_COLOR);
                }
            }
        });
        // Initial color setup (delayed to later via invokeLater to ensure index is
        // valid)
        SwingUtilities.invokeLater(() -> {
            int index = tabbedPane.indexOfTabComponent(this);
            if (index == tabbedPane.getSelectedIndex()) {
                titleLabel.setForeground(ACTIVE_COLOR);
            } else {
                titleLabel.setForeground(INACTIVE_COLOR);
            }
        });
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        ImageIcon iconDefault = new ImageIcon(getClass().getResource("/diffchecker/images/close_def.png"));
        ImageIcon iconHover = new ImageIcon(getClass().getResource("/diffchecker/images/close_hover.png"));

        ImageIcon defaultIcon = new ImageIcon(iconDefault.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH));
        ImageIcon hoverIcon = new ImageIcon(iconHover.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH));

        JButton closeButton = new JButton(defaultIcon);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setMargin(new Insets(0, 5, 0, 5));

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setIcon(hoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setIcon(defaultIcon);
            }
        });

        closeButton.addActionListener(e -> {
            int index = tabbedPane.indexOfTabComponent(this);
            if (index != -1) {
                tabbedPane.remove(index);
                if (tabbedPane.getTabCount() == 1 && onTabEmptyFallback != null) {
                    onTabEmptyFallback.run();
                }
            }
        });

        add(titleLabel, BorderLayout.WEST);
        add(closeButton, BorderLayout.EAST);
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // optional: horizontal padding
    }

}
