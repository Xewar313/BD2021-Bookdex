package com.example.bd2021bookdex;

import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.window.MainWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.*;

@SpringBootApplication
public class Bd2021BookdexApplication {
    
    @Autowired
    DatabaseModifier ME;
    public static void main(String[] args) {
        var ctx = new SpringApplicationBuilder(Bd2021BookdexApplication.class).headless(false).run(args);
    }
}
