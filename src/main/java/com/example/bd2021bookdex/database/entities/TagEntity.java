package com.example.bd2021bookdex.database.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "TAG")
public class TagEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "tag_name")
    private String name;
    @ManyToMany (mappedBy = "tags")
    private Set<BookEntity> books;
    
    public TagEntity(String name, Set<BookEntity> books) {
        this.name = name;
        this.books = books;
    }

    public void clearBooks() {
        books.clear();
    }
    public TagEntity() {
        
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
