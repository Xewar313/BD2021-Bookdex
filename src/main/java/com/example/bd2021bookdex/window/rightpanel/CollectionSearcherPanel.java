package com.example.bd2021bookdex.window.rightpanel;

import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.BookCollectionEntity;
import com.example.bd2021bookdex.window.ui.MyButton;
import com.example.bd2021bookdex.window.middlepanel.SelectedScrollPane;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;

@org.springframework.stereotype.Component
public class CollectionSearcherPanel extends JPanel {
    SelectedScrollPane target;
    DatabaseSearcher searcher;
    DatabaseModifier modifier;
    JTextArea nameToSearch = new JTextArea();
    JTextArea nameToCreate = new JTextArea();
    JTextArea descToCreate = new JTextArea();
    @Autowired
    public CollectionSearcherPanel(SelectedScrollPane targ, DatabaseSearcher searcher, DatabaseModifier modifier) {
        this.modifier = modifier;
        this.searcher = searcher;
        target = targ;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0,8)));
        
        setLabel("Search collection:", true);
        
        add(Box.createRigidArea(new Dimension(0,8)));
        
        setLabel("Name:", false);
        
        add(Box.createRigidArea(new Dimension(0,4)));
        
        add(nameToSearch);
        
        add(Box.createRigidArea(new Dimension(0,25)));
        
        MyButton searchButton = new MyButton("Search");
        searchButton.addActionListener(actionEvent -> searchCollections());
        prepareButton(searchButton);
        
        add(Box.createRigidArea(new Dimension(0,30)));
        
        setLabel("Create collection:", true);
        
        add(Box.createRigidArea(new Dimension(0,8)));
        
        setLabel("Name:", false);
        
        add(Box.createRigidArea(new Dimension(0,4)));
        
        add(nameToCreate);
        
        add(Box.createRigidArea(new Dimension(0,4)));
        
        setLabel("Description:", false);
        
        add(Box.createRigidArea(new Dimension(0,4)));
        
        add(descToCreate);
        
        add(Box.createRigidArea(new Dimension(0,25)));
        
        descToCreate.setLineWrap(true);
        descToCreate.setWrapStyleWord(true);
        MyButton createButton = new MyButton("Create");
        createButton.addActionListener(actionEvent -> createCollections());
        prepareButton(createButton);
    }

    private void prepareButton(MyButton button) {
        add(button);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(50,30));
    }
    public void setSizes(int x) {
        Dimension textSize = new Dimension(x * 5 / 6,  16);
        nameToSearch.setMinimumSize(textSize);
        nameToSearch.setPreferredSize(textSize);
        nameToSearch.setMaximumSize(textSize);

        nameToCreate.setMinimumSize(textSize);
        nameToCreate.setPreferredSize(textSize);
        nameToCreate.setMaximumSize(textSize);

        Dimension writeSize = new Dimension(x * 5 / 6,  109);
        descToCreate.setMinimumSize(writeSize);
        descToCreate.setPreferredSize(writeSize);
        descToCreate.setMaximumSize(writeSize);
        
    }
    private void searchCollections() {
        if (searcher.getUser() == null)
            return;
        searcher.reset();
        searcher.addCollName(nameToSearch.getText());
        target.addCollections(searcher.getCollections(true));
    }

    private void createCollections() {
        if (searcher.getUser() == null)
            return;
        BookCollectionEntity toAdd = new BookCollectionEntity(nameToCreate.getText(), descToCreate.getText(), searcher.getUser());
        modifier.addToDb(toAdd);
    }
    private void setLabel(String name, boolean shouldBeBigger) {
        JLabel temp = new JLabel(name);
        if (shouldBeBigger)
            temp.setFont(temp.getFont().deriveFont(13.5f));
        add(temp);
        temp.setAlignmentX(CENTER_ALIGNMENT);
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0,getWidth(),getHeight());
    }
}
