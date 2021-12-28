package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.entities.ChangesEntity;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RecentChangesLabel extends JPanel{
    private static final int GBC_I = 3;
    private ChangesEntity change;
    private JLabel titleLabel = new JLabel();
    private JLabel desc = new JLabel();
    private JLabel date = new JLabel();

    public RecentChangesLabel(ChangesEntity src, int x, int y) {
        this.setMinimumSize(new Dimension(x,y));
        this.setPreferredSize(new Dimension(x,y));
        this.setMaximumSize(new Dimension(x,y));
        change = src;
        setBackground(Color.white);
        setBorder(new LineBorder(Color.black));
        setLayout(new GridBagLayout());
        add(titleLabel,createGbc(0,0));
        titleLabel.setText(change.getBook().getTitle());
        add(desc,createGbc(0,1));
        switch (change.getStatus()) {
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
        add(date,createGbc(0,2));
        date.setText(change.getDate().toString());
    }
    public ChangesEntity getChange() {
        return change;
    }
    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(GBC_I, GBC_I, GBC_I, GBC_I);
        gbc.insets.left = x != 0 ? 3 * GBC_I : GBC_I;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        return gbc;
    }
}
