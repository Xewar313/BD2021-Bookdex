package com.example.bd2021bookdex.window;

import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

@org.springframework.stereotype.Component
public class BookSearcherPanel extends JPanel {
    SelectedList target;
    
    @Autowired
    public BookSearcherPanel(SelectedList targ) {
        target = targ;
    }
}
