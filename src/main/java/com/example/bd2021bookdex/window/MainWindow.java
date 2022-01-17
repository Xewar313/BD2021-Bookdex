package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.window.leftpanel.LeftContainer;
import com.example.bd2021bookdex.window.middlepanel.BookDisplayLabel;
import com.example.bd2021bookdex.window.middlepanel.SelectedScrollPane;
import com.example.bd2021bookdex.window.rightpanel.SearcherAndModifierContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainWindow extends JFrame {
    
    private final SelectedScrollPane selected;
    @Autowired
    public MainWindow(LeftContainer list, SelectedScrollPane selected, SearcherAndModifierContainer search) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setName("Bookdex");
        this.setTitle("Bookdex");
        this.setLocation(screen.width/4,screen.height/4);
        this.getContentPane().setBackground(Color.white);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.LINE_AXIS));
        this.getContentPane().add(Box.createRigidArea(new Dimension(5,screen.height/2 + 10)));
        this.getContentPane().add(list);
        this.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        this.selected = selected;
        this.getContentPane().add(selected);
        this.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        this.getContentPane().add(search);
        this.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        this.pack();
        this.setVisible(true);
        this.repaint();
    }

    public void removeLabel(BookDisplayLabel label) {
        selected.removeLabel(label);
    }
}
