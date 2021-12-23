package com.example.bd2021bookdex.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "BOOK")
public class BookEntity implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "thumbnail_link")
    private String thumbnailLink;
    @Column(name = "page_count")
    private int pageCount;
    @Column(name = "google_id")
    private String googleId;
    @Column(name = "category")
    private String category;
    @Column(name = "genre")
    private String genre;
    @Column(name = "book_desc")
    private String description;
    
    @ManyToMany
    @JoinTable(
          name = "AUTHORSHIP",
          joinColumns = { @JoinColumn(name = "book_id")},
          inverseJoinColumns = { @JoinColumn(name = "author_id")}
    )
    private Set<AuthorEntity> authors;
    
    @ManyToMany(mappedBy = "books")
    private Set<TagEntity> tags;
    public BookEntity() {}
    public BookEntity(String title,
                      String thumbnailLink,
                      int pageCount,
                      String googleId,
                      String category,
                      String genre,
                      String description,
                      Set<AuthorEntity> authors) {
        this.title = title;
        this.thumbnailLink = thumbnailLink;
        this.pageCount = pageCount;
        this.googleId = googleId;
        this.category = category;
        this.genre =genre;
        this.description = description;
         this.authors = new HashSet<>(authors);
    }

    public int getId() {
        return id;
    }
}
