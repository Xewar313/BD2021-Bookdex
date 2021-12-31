package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.entities.BookCollectionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

@Component
@Scope("prototype")
public class CollectionDisplayLabel extends JPanel {
    private static final int GBC_I = 3;
    @Autowired
    private SelectedScrollPane toAdd;
    private final BookCollectionEntity collection;

    public CollectionDisplayLabel(BookCollectionEntity src, int x, int y) {
        this.setMinimumSize(new Dimension(x,y));
        this.setPreferredSize(new Dimension(x,y));
        this.setMaximumSize(new Dimension(x,y));
        collection = src;
        setBackground(Color.white);
        setLayout(new GridBagLayout());
        setBorder(new LineBorder(Color.black, 1));

        JLabel nameLabel = new JLabel();
        add(nameLabel, createGbc(0,0));
        nameLabel.setText(src.getName());

        JLabel booksLabel = new JLabel();
        add(booksLabel, createGbc(0,1));
        StringBuilder books = new StringBuilder();
        for (var book : collection.getBooks()) {
            if (booksLabel.getFontMetrics(booksLabel.getFont()).stringWidth(books.toString()) +
                    booksLabel.getFontMetrics(booksLabel.getFont()).stringWidth(book.getTitle()) > x) {
                books.append(".....");
                break;
            }
            books.append(book.getTitle()).append(", ");
        }
        if (collection.getBooks().size() > 0) {
            books.deleteCharAt(books.length() - 1);
            books.deleteCharAt(books.length() - 1);
        }
        booksLabel.setText(books.toString());

        JTextArea desc = new JTextArea();
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(desc);
        scroll.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setMinimumSize(new Dimension(x / 5*3, y*2/5));
        scroll.setPreferredSize(new Dimension(x / 5*3, y*2/5));
        scroll.setBorder(null);
        desc.setMaximumSize(new Dimension(x/4*3,y/2));
        desc.setLineWrap(true);
        desc.setText(collection.getDesc());
        add(scroll, createGbc(0,2));

        JLabel bookCount = new JLabel();
        add(bookCount, createGbc(0,4));
        bookCount.setText("Number of books: " + collection.getBooks().size());
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                toAdd.setBooks(new LinkedList<>(collection.getBooks()));
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
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
