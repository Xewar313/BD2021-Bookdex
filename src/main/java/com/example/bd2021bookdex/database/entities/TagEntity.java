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
    @ManyToMany
    @JoinTable(
            name = "BOOK_TAGGING",
            joinColumns = { @JoinColumn(name = "tag_id")},
            inverseJoinColumns = { @JoinColumn(name = "book_id")}
    )
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
}
