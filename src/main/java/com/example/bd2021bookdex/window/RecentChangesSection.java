package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;

@org.springframework.stereotype.Component
public class RecentChangesSection extends JPanel {

    RecentChangesList changes;
    @Autowired
    public RecentChangesSection(RecentChangesList src) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension thisSize = new Dimension(screen.width/8,screen.height/2);
        this.setMinimumSize(thisSize);
        this.setPreferredSize(thisSize);
        this.setMaximumSize(thisSize);
        changes = src;
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        add(new JLabel("Recent changes"));
        add(Box.createRigidArea(new Dimension(0,5)));
        JScrollPane changesScroll = new JScrollPane(src);
        changesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        changesScroll.setMaximumSize(new Dimension(screen.width / 4,screen.height / 3));
        changesScroll.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        add(changesScroll);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }
    public void setUser(UserEntity usr) {
        changes.setUser(usr);
    }
}
