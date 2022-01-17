package com.example.bd2021bookdex.window.rightpanel;

import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.database.entities.TagEntity;
import com.example.bd2021bookdex.database.entities.UserEntity;
import com.example.bd2021bookdex.window.leftpanel.LeftContainer;
import com.example.bd2021bookdex.window.ui.MyButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

@Component
public class CreatorPanel extends JPanel {
    DatabaseModifier modifer;
    JTextArea tagName = new JTextArea();
    JTextArea username = new JTextArea();
    JPasswordField userPassword = new JPasswordField();
    LeftContainer toUpdate;
    @Autowired
    public CreatorPanel(DatabaseModifier modifier, LeftContainer toUpdate) {
        this.toUpdate = toUpdate;
        this.modifer = modifier;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0,8)));
        
        setLabel("Create tag:",true);
        
        add(Box.createRigidArea(new Dimension(0,8)));
        
        setLabel("Name:", false);
        
        add(Box.createRigidArea(new Dimension(0,4)));
        
        add(tagName);
        
        add(Box.createRigidArea(new Dimension(0,25)));
        
        MyButton createButton = new MyButton("Create");
        createButton.addActionListener(actionEvent -> createTag());
        prepareButton(createButton);
        
        add(Box.createRigidArea(new Dimension(0,30)));
        
        setLabel("Create user", true);
        
        add(Box.createRigidArea(new Dimension(0,8)));
        
        setLabel("Username:", false);
        
        add(Box.createRigidArea(new Dimension(0,4)));
        
        add(username);
        
        add(Box.createRigidArea(new Dimension(0,4)));
        
        setLabel("Password:", false);
        
        add(Box.createRigidArea(new Dimension(0,4)));
        
        add(userPassword);
        
        add(Box.createRigidArea(new Dimension(0,25)));
        
        createButton = new MyButton("Create");
        createButton.addActionListener(actionEvent -> createUser());
        prepareButton(createButton);
    }

    private void createUser() {
        modifer.addToDb(new UserEntity(username.getText(), new String(userPassword.getPassword())));
        toUpdate.updateUsers();
    }

    public void setSizes(int x) {
        Dimension textSize = new Dimension(x * 5 / 6,  16);
        tagName.setMinimumSize(textSize);
        tagName.setPreferredSize(textSize);
        tagName.setMaximumSize(textSize);

        username.setMinimumSize(textSize);
        username.setPreferredSize(textSize);
        username.setMaximumSize(textSize);

        userPassword.setMinimumSize(textSize);
        userPassword.setPreferredSize(textSize);
        userPassword.setMaximumSize(textSize);
    }
    private void setLabel(String name, boolean shouldBeBigger) {
        JLabel temp = new JLabel(name);
        if (shouldBeBigger)
            temp.setFont(temp.getFont().deriveFont(13.5f));
        add(temp);
        temp.setAlignmentX(CENTER_ALIGNMENT);
    }
    private void prepareButton(MyButton button) {
        add(button);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(50,30));
    }
    private void createTag(){
        CompletableFuture.runAsync(() -> modifer.addToDb(new TagEntity(tagName.getText().substring(0, Math.min(20, tagName.getText().length()))))
        );
    }
    @Override
    protected void paintComponent(Graphics g) {
        
        g.setColor(Color.LIGHT_GRAY);

        g.fillRect(0,0,getWidth(),getHeight());
    }
}
