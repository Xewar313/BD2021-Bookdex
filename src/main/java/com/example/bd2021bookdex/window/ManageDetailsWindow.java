package com.example.bd2021bookdex.window;

import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.BookEntity;
import com.example.bd2021bookdex.database.entities.TagEntity;
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
public class ManageDetailsWindow extends JDialog {
    public ManageDetailsWindow (BookDisplayLabel creator, BookEntity toManage, MainWindow owner, DatabaseSearcher searcher, DatabaseModifier modifier) {
        super(owner, "Change genre and tags");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(new Dimension(screen.width/5, screen.height/3));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(4,4,4,4);
        
        add(new JLabel("Genre (Only first 40 chars will be saved):"), gbc);
        
        gbc.gridy = 1;
        
        JTextArea genre = new JTextArea();
        genre.setText(toManage.getGenre());
        genre.setMinimumSize(new Dimension(screen.width/ 6, 20));
        genre.setLineWrap(true);
        add(genre, gbc);
        genre.setSize(new Dimension(getSize().width / 6 * 5, 20));

        gbc.gridy = 3;
        add(new JLabel("Change tags:"), gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        add(new JLabel("Add:"), gbc);

        gbc.gridx = 1;
        add(new JLabel("Remove:"), gbc);
        
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        TagEntity[] tagEntities = toManage.getTags().toArray(new TagEntity[]{});
        DefaultListModel<TagEntity> addedTags = prepareListModel(tagEntities);
        JList<TagEntity> addedTagsList = prepareList(addedTags);
        JScrollPane scrollAdded = preparePane(addedTagsList);
        add(scrollAdded, gbc);
        
        gbc.gridx = 1;
        List<TagEntity> tagEntitiesToRemove = searcher.getTags();
        tagEntitiesToRemove.removeAll(toManage.getTags());
        DefaultListModel<TagEntity> restTags = prepareListModel(tagEntitiesToRemove.toArray(new TagEntity[]{}));
        JList<TagEntity> restTagsList = prepareList(restTags);
        JScrollPane scrollRest = preparePane(restTagsList);
        add(scrollRest, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        MyButton toAdd = new MyButton("Remove");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> {
            if (addedTagsList.getSelectedValue() != null) {
                TagEntity toMove = addedTagsList.getSelectedValue();
                addedTags.removeElement(toMove);
                restTags.addElement(toMove);

            }
        });

        gbc.gridy = 6;
        gbc.gridx = 1;
        toAdd = new MyButton("Add");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> {
            if (restTagsList.getSelectedValue() != null) {
                TagEntity toMove = restTagsList.getSelectedValue();
                restTags.removeElement(toMove);
                addedTags.addElement(toMove);

            }
        });
        
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        toAdd = new MyButton("Confirm");
        add(toAdd, gbc);
        toAdd.addActionListener(actionEvent -> confirm(creator, toManage, modifier, addedTags, genre.getText()));
        
        setModalityType(ModalityType.APPLICATION_MODAL);
        setLocation(screen.width/2 - getSize().width/2, screen.height/2 - getSize().height/2);
        setBackground(Color.white);
        setVisible(true);
    }
    
    private DefaultListModel<TagEntity> prepareListModel(TagEntity[] tagEntities) {
        Arrays.sort(tagEntities);
        DefaultListModel <TagEntity> tagsList = new DefaultListModel<>();
        tagsList.addAll(List.of(tagEntities));
        return tagsList;
    }

    private JList<TagEntity> prepareList(DefaultListModel<TagEntity> tagsList) {
        JList<TagEntity> tags = new JList<>(tagsList);
        tags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tags.setLayoutOrientation(JList.VERTICAL);
        tags.setVisibleRowCount(3);
        return tags;
    }
    
    private JScrollPane preparePane(JList<TagEntity> tags) {
        JScrollPane scroll = new JScrollPane(tags);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setMinimumSize(new Dimension(150,100));
        scroll.setPreferredSize(new Dimension(150,100));
        scroll.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        return scroll;
    }


    private void confirm(BookDisplayLabel creator, BookEntity toManage, DatabaseModifier modifier, DefaultListModel<TagEntity> added, String genre) {

        toManage.setGenre(genre.substring(0, Math.min(40, genre.length())));
        toManage.setTags(added);
        modifier.updateInDb(toManage);
        creator.update();
        dispose();
    }

}
