package com.diffchecker.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.IntConsumer;

public class ClosableTabTitleComponent extends JPanel {
    private final Color ACTIVE_COLOR = new Color(0xF9FAFA); // Active tab color
    private final Color INACTIVE_COLOR = new Color(0x888690); // Inactive tab color
    private final Color FONT_COLOR = new Color(0xd6d6d6);

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

    /**
     * @param tabbedPane         The JTabbedPane this title belongs to
     * @param title              The text to display
     * @param onTabEmptyFallback Runnable to add a new tab if all tabs are closed
     * @param onCloseTabAtIndex  IntConsumer to handle closing the tab at a specific
     *                           index
     */
    public ClosableTabTitleComponent(JTabbedPane tabbedPane, String title, Runnable onTabEmptyFallback,
            IntConsumer onCloseTabAtIndex) {

        super(new BorderLayout(10, 0)); // add horizontal gap between label and button
        this.tabbedPane = tabbedPane;
        setOpaque(false);

        titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.BOTTOM);

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
        // TAB TITLE FONT WEIGHT AND SIZE
        Font base = new Font("SansSerif", Font.BOLD, titleLabel.getFont().getSize());
        titleLabel.setFont(base.deriveFont(14f));

        SwingUtilities.invokeLater(this::updateColor);
        tabbedPane.addChangeListener(e -> updateColor());

        // CLOSE BUTTON ACTION
        JButton closeButton = new JButton("✕") {
            private boolean hover = false;

            {
                setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 4));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setOpaque(false);
                setForeground(FONT_COLOR);

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });

                setToolTipText("<html><strong>Close Tab</strong> <br> ( Ctrl + W )</html>");
            }

            @Override
            protected void paintComponent(Graphics g) {
                if (hover) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(80, 80, 80, 180)); // hover color
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6); // rounded background
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        closeButton.setFont(closeButton.getFont().deriveFont(14f));

        closeButton.addActionListener(e -> {
            int index = tabbedPane.indexOfTabComponent(this);
            if (index != -1) {
                onCloseTabAtIndex.accept(index); // close the correct tab
            }
        });

        add(titleLabel, BorderLayout.CENTER);
        add(closeButton, BorderLayout.EAST);
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // optional: horizontal padding
    }

}
