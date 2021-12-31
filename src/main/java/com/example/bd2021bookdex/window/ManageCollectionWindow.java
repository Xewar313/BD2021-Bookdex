package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.BookCollectionEntity;
import com.example.bd2021bookdex.database.entities.BookEntity;
import com.example.bd2021bookdex.database.entities.TagEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Component
@Scope("prototype")
public class ManageCollectionWindow extends JDialog {
    public ManageCollectionWindow(BookStatusEntity toManage, MainWindow owner, DatabaseSearcher searcher, DatabaseModifier modifier) {
        super(owner, "Change collections");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(new Dimension(screen.width/5, screen.height/3));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(4,4,4,4);

        add(new JLabel("Change collections:"), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Add:"), gbc);

        gbc.gridx = 1;
        add(new JLabel("Remove:"), gbc);


        gbc.gridy = 2;
        gbc.gridx = 0;
        BookCollectionEntity[] collections = searcher.getStatusCollection(toManage).toArray(new BookCollectionEntity[]{});
        DefaultListModel<BookCollectionEntity> addedCollections = prepareListModel(collections);
        JList<BookCollectionEntity> addedCollectionsList = prepareList(addedCollections);
        JScrollPane scrollAdded = preparePane(addedCollectionsList);
        add(scrollAdded, gbc);

        gbc.gridx = 1;
        searcher.reset();
        java.util.List<BookCollectionEntity> tagEntitiesToRemove = searcher.getCollections();
        tagEntitiesToRemove.removeAll(List.of(collections));
        DefaultListModel<BookCollectionEntity> restCollections = prepareListModel(tagEntitiesToRemove.toArray(new BookCollectionEntity[]{}));
        JList<BookCollectionEntity> restCollectionsList = prepareList(restCollections);
        JScrollPane scrollRest = preparePane(restCollectionsList);
        add(scrollRest, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        MyButton toAdd = new MyButton("Remove");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> {
            if (addedCollectionsList.getSelectedValue() != null) {
                BookCollectionEntity toMove = addedCollectionsList.getSelectedValue();
                addedCollections.removeElement(toMove);
                restCollections.addElement(toMove);

            }
        });
        
        gbc.gridx = 1;
        toAdd = new MyButton("Add");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> {
            if (restCollectionsList.getSelectedValue() != null) {
                BookCollectionEntity toMove = restCollectionsList.getSelectedValue();
                restCollections.removeElement(toMove);
                addedCollections.addElement(toMove);

            }
        });

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        toAdd = new MyButton("Confirm");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> confirm(toManage, modifier, addedCollections));

        setModalityType(ModalityType.APPLICATION_MODAL);
        setLocation(screen.width/2 - getSize().width/2, screen.height/2 - getSize().height/2);
        setBackground(Color.white);
        setVisible(true);
    }

    private DefaultListModel<BookCollectionEntity> prepareListModel(BookCollectionEntity[] tagEntities) {
        DefaultListModel <BookCollectionEntity> tagsList = new DefaultListModel<>();
        tagsList.addAll(List.of(tagEntities));
        return tagsList;
    }

    private JList<BookCollectionEntity> prepareList(DefaultListModel<BookCollectionEntity> tagsList) {
        JList<BookCollectionEntity> tags = new JList<>(tagsList);
        tags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tags.setLayoutOrientation(JList.VERTICAL);
        tags.setVisibleRowCount(3);
        return tags;
    }

    private JScrollPane preparePane(JList<BookCollectionEntity> tags) {
        JScrollPane scroll = new JScrollPane(tags);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setMinimumSize(new Dimension(150,100));
        scroll.setPreferredSize(new Dimension(150,100));
        scroll.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        return scroll;
    }


    private void confirm(BookStatusEntity toManage, DatabaseModifier modifier, DefaultListModel<BookCollectionEntity> added) {
        
        toManage.setCollections(added);
        modifier.updateInDb(toManage);
        dispose();
    }

}
