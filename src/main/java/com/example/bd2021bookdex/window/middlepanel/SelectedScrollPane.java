package com.example.bd2021bookdex.window.middlepanel;

import com.example.bd2021bookdex.database.entities.BookCollectionEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import com.example.bd2021bookdex.window.ui.ScrollBarUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class SelectedScrollPane extends JScrollPane {
    
    SelectedList list;
    @Autowired
    SelectedScrollPane(SelectedList list) {
        super(list);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.list = list;
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setMaximumSize(new Dimension(screen.width / 2,screen.height / 2));
        this.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        this.getViewport().setBackground(Color.white);
    }

    public void addBooks(List<BookStatusEntity> e) {
        list.addBooks(e);
        validate();
        repaint();
    }

    public void clear() {
        list.removeAll();
        validate();
        repaint();
    }

    public void addCollections(List<BookCollectionEntity> e) {
        list.addCollections(e);
        validate();
        repaint();
    }

    public void removeLabel(BookDisplayLabel label) {
        list.remove(label);
        list.validate();
        validate();
        repaint();
    }
}
