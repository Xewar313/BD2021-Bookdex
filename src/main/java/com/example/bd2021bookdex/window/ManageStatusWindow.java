package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.BookEntity;
import com.example.bd2021bookdex.database.entities.ChangesEntity;
import com.example.bd2021bookdex.database.entities.TagEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

@Component
@Scope("prototype")
public class ManageStatusWindow extends JDialog {
    JLabel status;
    public ManageStatusWindow (BookDisplayLabel creator, BookStatusEntity toManage, MainWindow owner, DatabaseSearcher searcher, DatabaseModifier modifier) {
        super(owner, "Change status or remove");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(new Dimension(screen.width/5, screen.height/3));
        this.setLayout(new GridBagLayout());
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
        
        gbc.gridy = 3;
        temp = new JLabel("Do you own that book:");
        temp.setFont(temp.getFont().deriveFont(16f));
        add(temp, gbc);
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

        gbc.gridy = 6;
        temp = new JLabel("Remove book:");
        temp.setFont(temp.getFont().deriveFont(16f));
        add(temp, gbc);

        gbc.gridy = 7;
        toAdd = new MyButton("Remove");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> {
            modifier.deleteFromDb(toManage);
            owner.removeLabel(creator);
            dispose();
        });

        setModalityType(ModalityType.APPLICATION_MODAL);
        setLocation(screen.width/2 - getSize().width/2, screen.height/2 - getSize().height/2);
        setBackground(Color.white);
        setVisible(true);
    }

    private void confirm(BookDisplayLabel creator, BookEntity toManage, DatabaseModifier modifier, DefaultListModel<TagEntity> added, String genre) {

        toManage.setGenre(genre.substring(0, Math.min(40, genre.length())));
        toManage.setTags(added);
        modifier.updateInDb(toManage);
        creator.update();
        dispose();
    }

}
