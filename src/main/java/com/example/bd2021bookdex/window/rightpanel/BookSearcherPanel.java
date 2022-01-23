package com.example.bd2021bookdex.window.rightpanel;

import com.example.bd2021bookdex.apiconnection.ApiSearcher;
import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.BookEntity;
import com.example.bd2021bookdex.database.entities.TagEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEnum;
import com.example.bd2021bookdex.window.ui.MyButton;
import com.example.bd2021bookdex.window.ui.ScrollBarUI;
import com.example.bd2021bookdex.window.middlepanel.SelectedScrollPane;
import com.example.bd2021bookdex.window.leftpanel.LeftContainer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Component
public class BookSearcherPanel extends JPanel {
    SelectedScrollPane target;
    DatabaseSearcher DBSE;
    ApiSearcher ASE;
    DatabaseModifier DBMO;
    JTextArea title = new JTextArea();
    JTextArea authors = new JTextArea();
    JTextArea categories = new JTextArea();
    JTextArea genres = new JTextArea();
    JComboBox<String> statuses = new JComboBox<>();
    DefaultListModel<TagEntity> tagsList;
    JList<TagEntity> tags;
    JScrollPane scroll;
    JComboBox<String> searchAllBox = new JComboBox<>();
    LeftContainer toUpdate;
    
    @Autowired
    public BookSearcherPanel(SelectedScrollPane targ, DatabaseSearcher database, ApiSearcher api, DatabaseModifier modifier, LeftContainer toUpdate) {
        this.toUpdate = toUpdate;
        target = targ;
        DBSE = database;
        DBMO = modifier;
        ASE = api;
        
        setBasicInput();
        
        setTagsInput();

        MyButton searchButton = new MyButton("Search");
        searchButton.addActionListener(actionEvent -> searchBooks());
        add(searchButton);
        searchButton.setAlignmentX(CENTER_ALIGNMENT);
        searchButton.setPreferredSize(new Dimension(50,30));
        
        statuses.addItem("Any");
        statuses.addItem("Come across");
        statuses.addItem("Added");
        statuses.addItem("Being read");
        statuses.addItem("Finished");
        statuses.setBackground(Color.white);
        tags.setBackground(Color.white);
       
        searchAllBox.setBackground(Color.white);
        searchAllBox.addItem("No");
        searchAllBox.addItem("Yes (Set status and genre may be ignored)");
    }

    private void setBasicInput() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0,8)));
        setLabel("Search books:", true);
        add(Box.createRigidArea(new Dimension(0,8)));
        setLabel("Title:", false);
        add(Box.createRigidArea(new Dimension(0,4)));
        add(title);
        add(Box.createRigidArea(new Dimension(0,4)));
        setLabel("Authors:", false);
        add(Box.createRigidArea(new Dimension(0,4)));
        add(authors);
        add(Box.createRigidArea(new Dimension(0,4)));
        setLabel("Categories:", false);
        add(Box.createRigidArea(new Dimension(0,4)));
        add(categories);
        add(Box.createRigidArea(new Dimension(0,4)));
        setLabel("Genres:", false);
        add(Box.createRigidArea(new Dimension(0,4)));
        add(genres);
        add(Box.createRigidArea(new Dimension(0,4)));
        setLabel("Status:", false);
        add(Box.createRigidArea(new Dimension(0,4)));
        add(statuses);
        add(Box.createRigidArea(new Dimension(0,4)));
        setLabel("Tags (use ctrl for multiple selection):", false);
        add(Box.createRigidArea(new Dimension(0,4)));
    }
    
    private void setTagsInput() {
        TagEntity[] tagsEntities = DBSE.getTags().toArray(new TagEntity[]{});

        Arrays.sort(tagsEntities);
        tagsList = new DefaultListModel<>();
        tagsList.addAll(List.of(tagsEntities));
        tags = new JList<>(tagsList);
        tags.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tags.setLayoutOrientation(JList.VERTICAL);
        tags.setVisibleRowCount(3);
        scroll = new JScrollPane(tags);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getVerticalScrollBar().setUI(new ScrollBarUI(Color.white));
        add(scroll);
        
        add(Box.createRigidArea(new Dimension(0,4)));
        setLabel("Search new books?", false);
        add(Box.createRigidArea(new Dimension(0,4)));
        add(searchAllBox);
        add(Box.createRigidArea(new Dimension(0,50)));
    }
    
    public void update() {
        tagsList.clear();
        TagEntity[] tagsEntities = DBSE.getTags().toArray(new TagEntity[]{});
        Arrays.sort(tagsEntities);
        tagsList.addAll(List.of(tagsEntities));
    }
    private void setLabel(String name, boolean shouldBeBigger) {
        JLabel temp = new JLabel(name);
        if (shouldBeBigger)
            temp.setFont(temp.getFont().deriveFont(13.5f));
        add(temp);
        temp.setAlignmentX(CENTER_ALIGNMENT);
    }
    public void setSizes(int x) {
        Dimension textSize = new Dimension(x * 5 / 6,  16);
        Dimension boxSize = new Dimension(x * 5 / 6,  25);
        title.setMinimumSize(textSize);
        authors.setMinimumSize(textSize);
        categories.setMinimumSize(textSize);
        genres.setMinimumSize(textSize);
        statuses.setMinimumSize(boxSize);
        scroll.setMinimumSize(new Dimension(x * 5 / 6, 50));
        searchAllBox.setMinimumSize(boxSize);
        
        title.setPreferredSize(textSize);
        authors.setPreferredSize(textSize);
        categories.setPreferredSize(textSize);
        genres.setPreferredSize(textSize);
        statuses.setPreferredSize(boxSize);
        scroll.setPreferredSize(new Dimension(x * 5 / 6, 50));
        searchAllBox.setPreferredSize(boxSize);
        
        title.setMaximumSize(textSize);
        authors.setMaximumSize(textSize);
        categories.setMaximumSize(textSize);
        genres.setMaximumSize(textSize);
        statuses.setMaximumSize(boxSize);
        scroll.setMaximumSize(new Dimension(x * 5 / 6, 50));
        searchAllBox.setMaximumSize(boxSize);
    }
    private void searchBooks() {
        if (DBSE.getUser() == null)
            return;
        DBSE.reset();
        DBSE.addAuthor(authors.getText());
        DBSE.addTitles(title.getText());
        DBSE.addGenres(genres.getText());
        DBSE.addCategories(categories.getText());
        switch (statuses.getSelectedIndex()) {
            case 1:
                DBSE.setStatus(BookStatusEnum.FOUND);
                break;
            case 2:
                DBSE.setStatus(BookStatusEnum.ADDED);
                break;
            case 3:
                DBSE.setStatus(BookStatusEnum.IN_PROGRESS);
                break;
            case 4:
                DBSE.setStatus(BookStatusEnum.READ);
                break;
            case 0:
                break;
        }
        List<TagEntity> tagsEntities = tags.getSelectedValuesList();
        for (var tag : tagsEntities) {
            DBSE.addTag(tag);
        }
        if (searchAllBox.getSelectedIndex() == 0) {
            List<BookStatusEntity> toSet = DBSE.findFoundBooks();
            target.addBooks(toSet);
        }
        else {
            searchNewBooks();
        }
    }
    private void searchNewBooks() {
        ASE.reset();
        ASE.addAuthor(authors.getText());
        ASE.addTitles(title.getText());
        ASE.addGenres(genres.getText());
        ASE.addCategories(categories.getText());

        List<BookEntity> books = DBSE.findBooks();
        List<BookStatusEntity> toSet = new LinkedList<>();
        for (var book : books) {
            var temp = DBMO.tryAddingConnection(book,DBSE.getUser());
            if (temp != null)
                toSet.add(temp);
        }
        int howMany;
        if (toSet.size() > 10) {
            howMany = 5;
        }
        else {
            howMany = 15 - toSet.size();
        }
        try {
            books = ASE.getBooks(howMany);
        } catch (Exception e) {
            target.addBooks(toSet);
            e.printStackTrace();
            return;
        }

        for (var book : books) {
            var temp = DBMO.tryAddingConnection(book, DBSE.getUser());
            if (temp != null)
                toSet.add(temp);
        }
        target.addBooks(toSet);
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0,getWidth(),getHeight());
    }
}
