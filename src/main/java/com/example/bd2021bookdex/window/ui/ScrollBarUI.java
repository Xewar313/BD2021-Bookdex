package com.example.bd2021bookdex.window.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ScrollBarUI extends BasicScrollBarUI {
    private final Dimension d = new Dimension();
    private final Color baseColor;
    public ScrollBarUI(Color base) {
        baseColor = base;
        minimumThumbSize = new Dimension(100,100);
    }
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return d;
            }
        };
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return d;
            }
        };
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(baseColor);
        g2.fillRect(r.x, r.y, r.width, r.height);
        g2.setColor(new Color(238,238,238));
        g2.fillRoundRect(r.x + r.width / 4, r.y, r.width / 2, r.height, 10, 10);
        g2.setPaint(baseColor);
        g2.drawRoundRect(r.x + r.width / 4, r.y, r.width / 2, r.height, 10, 10);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();
        Color color;
        JScrollBar sb = (JScrollBar) c;
        if (!sb.isEnabled()) {
            return;
        } else if (isDragging) {
            color = Color.GRAY.darker();
        } else if (isThumbRollover()) {
            color = Color.LIGHT_GRAY;
        } else {
            color = Color.GRAY;
        }
        g2.setColor(baseColor);
        g2.fillRect(r.x,r.y, r.width, r.height);
        g2.setPaint(color);
        g2.fillRoundRect(r.x + r.width / 4, r.y, r.width / 2, r.height, 10, 10);
        g2.setPaint(new Color(238,238,238));
        g2.drawRoundRect(r.x + r.width / 4, r.y, r.width / 2, r.height, 10, 10);
        g2.dispose();
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);
        scrollbar.repaint();
    }
}
