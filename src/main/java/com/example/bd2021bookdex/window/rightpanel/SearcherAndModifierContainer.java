package com.example.bd2021bookdex.window.rightpanel;

import com.example.bd2021bookdex.window.ui.MyButton;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;

@org.springframework.stereotype.Component
public class SearcherAndModifierContainer extends JPanel {
    BookSearcherPanel bookSearcher;
    CollectionPanel collectionSearcher;
    CreatorPanel creator;
    
    @Autowired
    public SearcherAndModifierContainer(BookSearcherPanel book, CollectionPanel collection, CreatorPanel creat) {
        bookSearcher = book;
        collectionSearcher = collection;
        creator = creat;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension thisSize = new Dimension(screen.width/6,screen.height/2);
        this.setMinimumSize(thisSize);
        this.setPreferredSize(thisSize);
        this.setMaximumSize(thisSize);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Dimension buttonSize = new Dimension(thisSize.width / 3, thisSize.width / 6);
        
        addButtons(buttonSize);
        addPanels(new Dimension(thisSize.width, thisSize.height - buttonSize.height));
        
    }
    
    private void addButtons(Dimension buttonSize) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        MyButton first = new MyButton("Books");
        first.addActionListener(actionEvent -> {
            bookSearcher.update();
            bookSearcher.setVisible(true);
            collectionSearcher.setVisible(false);
            creator.setVisible(false);
        });
        prepButton(first, buttonSize);
        add(first,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        MyButton second = new MyButton("Collections");
        second.addActionListener(actionEvent -> {
            bookSearcher.setVisible(false);
            collectionSearcher.setVisible(true);
            creator.setVisible(false);
        });
        prepButton(second, buttonSize);
        add(second,gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        MyButton third = new MyButton("Create");
        third.addActionListener(actionEvent -> {
            bookSearcher.setVisible(false);
            collectionSearcher.setVisible(false);
            creator.setVisible(true);
        });
        prepButton(third, buttonSize);
        add(third,gbc);
    }
    
    private void addPanels(Dimension panelSize) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        bookSearcher.setVisible(true);
        collectionSearcher.setVisible(false);
        creator.setVisible(false);

        bookSearcher.setPreferredSize(panelSize);
        collectionSearcher.setPreferredSize(panelSize);
        creator.setPreferredSize(panelSize);

        bookSearcher.setSizes(panelSize.width);
        collectionSearcher.setSizes(panelSize.width);
        creator.setSizes(panelSize.width);

        add(bookSearcher, gbc);
        add(collectionSearcher, gbc);
        add(creator, gbc);
    }
    
    private void prepButton(MyButton toPrepare, Dimension buttonSize) {

        toPrepare.setFont(toPrepare.getFont().deriveFont(10.55f));
        toPrepare.setPreferredSize(buttonSize); 
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }
}
