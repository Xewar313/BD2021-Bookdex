package com.example.bd2021bookdex.window.middlepanel;

import com.example.bd2021bookdex.database.entities.BookCollectionEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@org.springframework.stereotype.Component
public class SelectedList extends JPanel implements Scrollable {
    Dimension thisSize;
    private static final int howManyRows = 3;
    @Autowired
    private ApplicationContext appContext;
    
    public SelectedList() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        thisSize = new Dimension(screen.width/4,screen.height/2);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    
    @Override
    public void paintComponent(Graphics g) {
    }
    public void addBooks(java.util.List<BookStatusEntity> books) {

        this.removeAll();
        for (var book : books) {
            add(appContext.getBean(BookDisplayLabel.class,book,thisSize.width,thisSize.height / howManyRows ));
        }
        this.revalidate();
        this.repaint();
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

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public void addCollections(List<BookCollectionEntity> e) {
        this.removeAll();
        for (var coll : e) {
            add(appContext.getBean(CollectionDisplayLabel.class,coll,thisSize.width,thisSize.height / howManyRows));
        }
    }
}
