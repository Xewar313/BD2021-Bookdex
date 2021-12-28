package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.BookCollectionEntity;
import com.example.bd2021bookdex.database.entities.UserEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainWindow extends JFrame {
    
    public SelectedList s;
    @Autowired
    public MainWindow(RecentChangesSection list, SelectedList selected, SearcherAndModifierContainers search, DatabaseSearcher SE, DatabaseModifier ME) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setName("Bookdex");
        this.setTitle("Bookdex");
        this.setLocation(screen.width/4,screen.height/4);
        this.getContentPane().setBackground(Color.white);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.LINE_AXIS));
        this.getContentPane().add(Box.createRigidArea(new Dimension(5,screen.height/2 + 10)));
        list.setUser(SE.getUserList().get(0));
        this.getContentPane().add(list);
        this.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        UserEntity tiger = SE.getUserList().get(0);
        SE.setUser(tiger);
        s = selected;
         b(SE.getCollections());
        JScrollPane selectedListScroll = new JScrollPane(selected);
        selectedListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        selectedListScroll.setMaximumSize(new Dimension(screen.width / 2,screen.height / 2));
        selectedListScroll.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        this.getContentPane().add(selectedListScroll);
        this.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        this.getContentPane().add(search);
        this.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        this.pack();
        this.setVisible(true);
        this.repaint();
    }
    public void a(java.util.List<BookStatusEntity> e) {
        s.removeAll();
        s.addBooks(e);
    }
    public void b(java.util.List<BookCollectionEntity> e) {
        s.removeAll();
        s.addCollections(e);
    }
}
