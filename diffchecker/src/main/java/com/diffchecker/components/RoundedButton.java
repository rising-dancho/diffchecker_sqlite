package com.diffchecker.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private Color backgroundColor = new Color(0x00C281);
    private Color hoverBackgroundColor = new Color(0x009966);
    private Color textColor = new Color(0xeeeeee); // FONT COLOR
    private Color borderColor = new Color(0x00C281);
    private Color hoverBorderColor = new Color(0x007a4f); // New: hover border
    private int cornerRadius = 20;
    private int borderThickness = 2;
    private boolean hovered = false;

    public RoundedButton(String text) {
        super(text);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(textColor);
        setMargin(new Insets(5, 10, 5, 10));
        setCursor(Cursor.getDefaultCursor());
        
        // CENTER THE TEXT
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);

        // Hover detection
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                setCursor(Cursor.getDefaultCursor());
                repaint();
            }
        });
    }

    // ─── Setters ──────────────────────────────────────────────
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    public void setHoverBackgroundColor(Color color) {
        this.hoverBackgroundColor = color;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setHoverBorderColor(Color color) {
        this.hoverBorderColor = color;
        repaint();
    }

    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
        repaint();
    }

    public void setTextColor(Color color) {
        this.textColor = color;
        setForeground(color);
        repaint();
    }

    // ─── Paint ────────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(hovered ? hoverBackgroundColor : backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Border
        g2.setColor(hovered ? hoverBorderColor : borderColor);
        g2.setStroke(new BasicStroke(borderThickness));
        g2.drawRoundRect(borderThickness / 2, borderThickness / 2,
                getWidth() - borderThickness, getHeight() - borderThickness,
                cornerRadius, cornerRadius);

        g2.dispose();
    }
}
