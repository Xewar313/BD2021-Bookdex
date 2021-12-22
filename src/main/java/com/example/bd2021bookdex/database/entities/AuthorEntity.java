package com.example.bd2021bookdex.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "AUTHOR")
public class AuthorEntity implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "auth_name")
    private String name;
    
    @ManyToMany(mappedBy = "authors")
    private Set<BookEntity> books;
    
    public AuthorEntity() {

    }
    
    public AuthorEntity(String name) {
        this.name = name;
    }
    
    public Set<BookEntity> getBooks() {
        return books;
    }

    public int getId() {
        return id;
    }
}
