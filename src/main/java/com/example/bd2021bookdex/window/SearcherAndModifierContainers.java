package com.example.bd2021bookdex.window;

import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;

@org.springframework.stereotype.Component
public class SearcherAndModifierContainers extends JPanel {
    BookSearcherPanel bookSearcher;
    CollectionSearcherPanel collectionSearcher;
    CreatorPanel creator;
    SelectedList target;
    
    @Autowired
    public SearcherAndModifierContainers(SelectedList target, BookSearcherPanel book, CollectionSearcherPanel collection, CreatorPanel creat) {
        bookSearcher = book;
        collectionSearcher = collection;
        creator = creat;
        this.target = target;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension thisSize = new Dimension(screen.width/6,screen.height/2);
        this.setMinimumSize(thisSize);
        this.setPreferredSize(thisSize);
        this.setMaximumSize(thisSize);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Dimension buttonSize = new Dimension(thisSize.width / 3, thisSize.width / 6);
        gbc.gridx = 0;
        gbc.gridy = 0;
        MyButton first = new MyButton("Books");
        prepButton(first, buttonSize);
        add(first,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        MyButton second = new MyButton("Collections");
        prepButton(second, buttonSize);
        add(second,gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        MyButton third = new MyButton("Create");
        prepButton(third, buttonSize);
        add(third,gbc);
        
    }
    
    private void prepButton(MyButton toPrepare, Dimension buttonSize) {

        toPrepare.setFont(toPrepare.getFont().deriveFont(10.55f));
        toPrepare.setPreferredSize(buttonSize); 
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }
}
