package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.entities.AuthorEntity;
import com.example.bd2021bookdex.database.entities.BookCollectionEntity;
import com.example.bd2021bookdex.database.entities.TagEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEnum;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CollectionDisplayLabel extends JPanel {
    private static final int GBC_I = 3;

    private BookCollectionEntity collection;
    private JLabel nameLabel = new JLabel();
    private JLabel booksLabel = new JLabel();
    private JTextArea desc = new JTextArea();
    private JLabel bookCount = new JLabel();
    public CollectionDisplayLabel(BookCollectionEntity src, int x, int y) {
        this.setMinimumSize(new Dimension(x,y));
        this.setPreferredSize(new Dimension(x,y));
        this.setMaximumSize(new Dimension(x,y));
        collection = src;
        setBackground(Color.white);
        setLayout(new GridBagLayout());
        setBorder(new LineBorder(Color.black, 1));
        
        add(nameLabel, createGbc(0,0));
        nameLabel.setText(src.getName());
        
        add(booksLabel, createGbc(0,1));
        StringBuilder books = new StringBuilder();
        for (var book : collection.getBooks()) {
            if (booksLabel.getFontMetrics(booksLabel.getFont()).stringWidth(books.toString()) +
                    booksLabel.getFontMetrics(booksLabel.getFont()).stringWidth(book.getBook().getTitle()) > x) {
                books.append("...");
                break;
            }
            books.append(book.getBook().getTitle()).append(", ");
        }
        booksLabel.setText(books.toString());
        
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
        
        add(bookCount, createGbc(0,4));
        bookCount.setText(String.valueOf(collection.getBooks().size()));
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
