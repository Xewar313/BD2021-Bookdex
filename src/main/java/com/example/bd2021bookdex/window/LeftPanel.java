package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.apiconnection.ApiSearcher;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@org.springframework.stereotype.Component
public class LeftPanel extends JPanel {


    ApiSearcher Asearcher;
    DatabaseSearcher DBsearcher;
    RecentChangesList changes;
    SelectedScrollPane list;
    JComboBox<UserEntity> users = new JComboBox<>();
    JScrollPane changesScroll;
    
    @Autowired
    public LeftPanel(RecentChangesList src, SelectedScrollPane list, ApiSearcher ASE, DatabaseSearcher SE) {
        DBsearcher = SE;
        Asearcher = ASE;
        this.list = list;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension thisSize = new Dimension(screen.width/8,screen.height/2);
        this.setMinimumSize(thisSize);
        this.setPreferredSize(thisSize);
        this.setMaximumSize(thisSize);
        changes = src;
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setLabel("Recent changes:");
        add(Box.createRigidArea(new Dimension(0,5)));
        changesScroll = new JScrollPane(src);
        changesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        changesScroll.setMaximumSize(new Dimension(screen.width / 4,screen.height / 3));
        changesScroll.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        changesScroll.getViewport().setBackground(Color.white);
        add(changesScroll);
        add(Box.createRigidArea(new Dimension(0,30)));
        setLabel("Choose user:");
        add(Box.createRigidArea((new Dimension(0, 20))));
        for (var u : SE.getUserList())
            users.addItem(u);
        add(users);
        users.setSelectedItem(users.getItemAt(0));
        users.setBackground(Color.white);
        users.addActionListener(actionEvent -> setUser());
        users.setPreferredSize(new Dimension(thisSize.width/6 * 5, 30));
        users.setMaximumSize(new Dimension(thisSize.width/6 * 5, 30));
        setUser();
    }
    
    public void updateUsers() {
        var prevUser = users.getSelectedItem();
        try {
            users.removeAllItems();
        } catch (Exception ignored){}
        for (var u : DBsearcher.getUserList())
            users.addItem(u);
        users.setSelectedItem(prevUser);
    }
    
    private void setLabel(String name) {
        JLabel temp = new JLabel(name);
        temp.setFont(temp.getFont().deriveFont(13.5f));
        add(temp);
        temp.setAlignmentX(CENTER_ALIGNMENT);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }
    public void setUser() {
        if (users.getSelectedItem() == null)
            return;
        changes.setUser((UserEntity) Objects.requireNonNull(users.getSelectedItem()));
        changes.revalidate();
        changes.repaint();
        list.clear();
        DBsearcher.setUser((UserEntity) Objects.requireNonNull(users.getSelectedItem()));
        Asearcher.setUser((UserEntity) Objects.requireNonNull(users.getSelectedItem()));
    }
}
