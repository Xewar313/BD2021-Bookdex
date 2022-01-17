package com.example.bd2021bookdex.window.ui;

import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {

    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;

    public MyButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
        setBackground(Color.gray.darker());
        setForeground(Color.white);
        setFocusable(false);
        setHoverBackgroundColor(Color.LIGHT_GRAY.darker());
        setPressedBackgroundColor(Color.LIGHT_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(pressedBackgroundColor);
        } else if (getModel().isRollover()) {
            g.setColor(hoverBackgroundColor);
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    @Override
    public void setContentAreaFilled(boolean b) {
    }

    public void setHoverBackgroundColor(Color hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }
}
