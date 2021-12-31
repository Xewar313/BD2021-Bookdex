package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.entities.ChangesEntity;
import com.example.bd2021bookdex.database.entities.UserEntity;
import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

@Component
public class RecentChangesList extends JPanel implements Scrollable {
    Dimension thisSize;
    private static final int howManyRows = 5;
    public RecentChangesList() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        thisSize = new Dimension(screen.width/8,screen.height/3);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return thisSize;
    }
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.VERTICAL) {
            return thisSize.height/howManyRows;
        } else {
            return thisSize.width;
        }
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.VERTICAL) {
            return thisSize.height;
        } else {
            return thisSize.width;
        }
    }

    public void setUser(UserEntity user) {
        removeAll();
        Vector<ChangesEntity> toSort = new Vector<>(user.getChanges()); 
        Collections.sort(toSort);
        for (var change : toSort) {
            add(new RecentChangesLabel(change,thisSize.width,thisSize.height / howManyRows));
        }
    }
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
