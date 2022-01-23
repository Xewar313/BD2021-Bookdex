package com.example.bd2021bookdex.window.leftpanel;

import com.example.bd2021bookdex.apiconnection.ApiSearcher;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.UserEntity;
import com.example.bd2021bookdex.window.ui.MyButton;
import com.example.bd2021bookdex.window.ui.ScrollBarUI;
import com.example.bd2021bookdex.window.middlepanel.SelectedScrollPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

@org.springframework.stereotype.Component
public class LeftContainer extends JPanel {


    ApiSearcher Asearcher;
    DatabaseSearcher DBsearcher;
    RecentChangesList changes;
    SelectedScrollPane list;
    JComboBox<UserEntity> users = new JComboBox<>();
    JScrollPane changesScroll;
    
    @Autowired
    public LeftContainer(RecentChangesList src, SelectedScrollPane list, ApiSearcher ASE, DatabaseSearcher SE) {
        DBsearcher = SE;
        Asearcher = ASE;
        this.list = list;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension thisSize = new Dimension(screen.width/8,screen.height/2);
        this.setMinimumSize(thisSize);
        this.setPreferredSize(thisSize);
        this.setMaximumSize(thisSize);
        changes = src;
        
        addRecentChangesSection(screen);
        
        addUserLogin(thisSize.width, SE);

    }

    private void addRecentChangesSection(Dimension screen) {
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setLabel("Recent changes:");
        add(Box.createRigidArea(new Dimension(0,5)));
        changesScroll = new JScrollPane(changes);
        changesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        changesScroll.setMaximumSize(new Dimension(screen.width / 4,screen.height / 3));
        changesScroll.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        changesScroll.getViewport().setBackground(Color.white);
        add(changesScroll);
    }
    
    private void addUserLogin(int x, DatabaseSearcher SE) {
        add(Box.createRigidArea(new Dimension(0,15)));
        JLabel loginLabel = setLabel("Login:");
        add(Box.createRigidArea((new Dimension(0, 10))));
        JTextArea login = new JTextArea();
        add(login);
        login.setBorder(new LineBorder(Color.black));
        Dimension dim = new Dimension(x / 6 * 5,18);
        login.setMinimumSize(dim);
        login.setPreferredSize(dim);
        login.setMaximumSize(dim);

        add(Box.createRigidArea((new Dimension(0, 10))));
        JLabel passwordLabel = setLabel("Password:");
        add(Box.createRigidArea((new Dimension(0, 10))));
        
        JPasswordField password = new JPasswordField();
        password.setBorder(new LineBorder(Color.black));
        add(password);
        password.setMinimumSize(dim);
        password.setPreferredSize(dim);
        password.setMaximumSize(dim);

        add(Box.createRigidArea((new Dimension(0, 10))));
        MyButton loginButton = new MyButton("Login");
        loginButton.addActionListener(in -> login(loginButton, password, login, loginLabel, passwordLabel));
        loginButton.setAlignmentX(CENTER_ALIGNMENT);
        add(loginButton);
        
    }
    
    private void login(MyButton src, JPasswordField passwordField, JTextArea loginField, JLabel loginLabel, JLabel passwordLabel) {
        UserEntity user = DBsearcher.getUserByName(loginField.getText());
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        char[] password = passwordField.getPassword();
        if (user == null || !encoder.matches(new String(password), user.getPassword())) {
            loginField.setBackground(new Color(255, 80, 80));
            passwordField.setBackground(new Color(255, 80, 80));
            return;
        }

        loginLabel.setText("You are logged as ");
        passwordLabel.setText(loginField.getText());
        
        loginField.setBackground(Color.white);
        loginField.setText("");
        loginField.setVisible(false);
        passwordField.setBackground(Color.white);
        passwordField.setText("");
        passwordField.setVisible(false);
        
        src.setText("Logout");
        src.removeActionListener(src.getActionListeners()[0]);
        src.addActionListener(in -> logout(src, passwordField, loginField, loginLabel, passwordLabel));
        
        Arrays.fill(password, (char) 0);

        changes.setUser(user);
        changes.revalidate();
        changes.repaint();
        Asearcher.setUser(user);
        DBsearcher.setUser(user);
    }

    private void logout(MyButton src, JPasswordField passwordField, JTextArea loginField, JLabel loginLabel, JLabel passwordLabel) {

        loginLabel.setText("Login:");
        passwordLabel.setText("Password:");

        loginField.setVisible(true);
        passwordField.setVisible(true);

        src.setText("Login");
        src.removeActionListener(src.getActionListeners()[0]);
        src.addActionListener(in -> login(src, passwordField, loginField, loginLabel, passwordLabel));
        
        list.clear();
        changes.clear();
        DBsearcher.setUser(null);
    }
    
    private void setVerticalBar() {
        Object comp = users.getUI().getAccessibleChild(users, 0);

        if (comp instanceof JPopupMenu) {
            JPopupMenu popup = (JPopupMenu) comp;
            JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
            scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        }
    }
    
    private JLabel setLabel(String name) {
        JLabel temp = new JLabel(name);
        temp.setFont(temp.getFont().deriveFont(13.5f));
        add(temp);
        temp.setAlignmentX(CENTER_ALIGNMENT);
        return temp;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }
}
