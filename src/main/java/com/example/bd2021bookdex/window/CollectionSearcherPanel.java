package com.example.bd2021bookdex.window;

import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;

@org.springframework.stereotype.Component
public class CollectionSearcherPanel extends JPanel {
    SelectedList target;

    @Autowired
    public CollectionSearcherPanel(SelectedList targ) {
        target = targ;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100,200);
    }
}
