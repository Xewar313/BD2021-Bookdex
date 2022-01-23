package com.example.bd2021bookdex.window.middlepanel.popup;

import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.BookEntity;
import com.example.bd2021bookdex.database.entities.ChangesEntity;
import com.example.bd2021bookdex.database.entities.TagEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEnum;
import com.example.bd2021bookdex.window.MainWindow;
import com.example.bd2021bookdex.window.ui.MyButton;
import com.example.bd2021bookdex.window.middlepanel.BookDisplayLabel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@Scope("prototype")
public class ManageStatusWindow extends JDialog {
    JLabel status;
    public ManageStatusWindow (BookDisplayLabel creator, BookStatusEntity toManage, MainWindow owner, DatabaseModifier modifier) {
        super(owner, "Change status or remove");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(new Dimension(screen.width/5, screen.height/3));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        addStatusModifier(creator, toManage, modifier);
        
        addOwnedModifier(creator, toManage, modifier);
        
        addRemoveButton(creator, toManage, owner, modifier);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setLocation(screen.width/2 - getSize().width/2, screen.height/2 - getSize().height/2);
        setBackground(Color.white);
        setVisible(true);
    }

    private void addOwnedModifier(BookDisplayLabel creator, BookStatusEntity toManage, DatabaseModifier modifier) {
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(30,2,2,2);
        gbc.gridy = 3;
        JLabel temp = new JLabel("Do you own that book:");
        temp.setFont(temp.getFont().deriveFont(16f));
        add(temp, gbc);
        gbc.insets = new Insets(2,2,2,2);
        gbc.gridy = 4;
        JLabel owned;
        if (toManage.isOwned()) {
            owned = new JLabel("Yes");
        }
        else {
            owned = new JLabel("No");
        }
        add(owned , gbc);
        gbc.gridy = 5;
        MyButton toAdd = new MyButton("Change owned");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> {

            toManage.changeOwned();
            modifier.updateInDb(toManage);
            creator.update();
            if (toManage.isOwned()) {
                owned.setText("Yes");
            }
            else {
                owned .setText("No");
            }
        });
    }
    
    private void addStatusModifier(BookDisplayLabel creator, BookStatusEntity toManage, DatabaseModifier modifier) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(4,4,4,4);
        JLabel temp = new JLabel("Current status:");
        temp.setFont(temp.getFont().deriveFont(16f));
        add(temp, gbc);
        gbc.gridy = 1;

        status = new JLabel(toManage.getStatus().toString());
        add(status, gbc);
        if (toManage.getStatus() != BookStatusEnum.READ) {
            gbc.gridy = 2;
            MyButton toAdd = new MyButton("Increase status");
            add(toAdd, gbc);
            toAdd.addActionListener(actionEvent -> {
                toManage.increaseStatus();
                modifier.updateInDb(toManage);
                modifier.addToDb(new ChangesEntity(toManage));
                creator.update();
                status.setText(toManage.getStatus().toString());
                if (toManage.getStatus() == BookStatusEnum.READ)
                    toAdd.setVisible(false);
            });
        }
    }
    
    private void addRemoveButton(BookDisplayLabel creator, BookStatusEntity toManage, MainWindow owner, DatabaseModifier modifier) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30,2,2,2);
        gbc.gridy = 6;
        JLabel temp = new JLabel("Remove book:");
        temp.setFont(temp.getFont().deriveFont(16f));
        add(temp, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(2,2,2,2);
        MyButton toAdd = new MyButton("Remove");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> {
            modifier.deleteFromDb(toManage);
            owner.removeLabel(creator);
            dispose();
        });
    }
}
