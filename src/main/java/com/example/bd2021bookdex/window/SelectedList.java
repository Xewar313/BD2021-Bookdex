package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.entities.BookCollectionEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@org.springframework.stereotype.Component
public class SelectedList extends JPanel implements Scrollable {
    Dimension thisSize;
    private static final int howManyRows = 3;
    public SelectedList() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        thisSize = new Dimension(screen.width/4,screen.height/2);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    @Override
    public void paintComponent(Graphics g) {

        //super.paintComponents(g);
    }
    public void addBooks(java.util.List<BookStatusEntity> books) {

        this.removeAll();
        for (var book : books) {
            add(new BookDisplayLabel(book,thisSize.width,thisSize.height / howManyRows) );
        }
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
            add(new CollectionDisplayLabel(coll,thisSize.width,thisSize.height / howManyRows) );
        }
    }
}
