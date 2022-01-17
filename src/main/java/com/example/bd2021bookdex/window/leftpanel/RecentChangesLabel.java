package com.example.bd2021bookdex.window.leftpanel;

import com.example.bd2021bookdex.database.entities.ChangesEntity;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RecentChangesLabel extends JPanel{
    private static final int GBC_I = 3;

    public RecentChangesLabel(ChangesEntity src, int x, int y) {
        this.setMinimumSize(new Dimension(x,y));
        this.setPreferredSize(new Dimension(x,y));
        this.setMaximumSize(new Dimension(x,y));
        setBackground(Color.white);
        setBorder(new LineBorder(Color.black));
        setLayout(new GridBagLayout());
        JLabel titleLabel = new JLabel();
        add(titleLabel,createGbc(0));
        titleLabel.setText(src.getBook().getTitle());
        JLabel desc = new JLabel();
        add(desc,createGbc(1));
        switch (src.getStatus()) {
            case FOUND:
                desc.setText("You came across this book");
                break;
            case ADDED:
                desc.setText("You got interesed in this book");
                break;
            case IN_PROGRESS:
                desc.setText("You started reading this book");
                break;
            case READ:
                desc.setText("You finished this book");
                break;
                
        }
        JLabel date = new JLabel();
        add(date,createGbc(2));
        date.setText(src.getDate().toString());
    }
    private GridBagConstraints createGbc(int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.insets = new Insets(GBC_I, GBC_I, GBC_I, GBC_I);
        gbc.insets.left = GBC_I;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        return gbc;
    }
}
