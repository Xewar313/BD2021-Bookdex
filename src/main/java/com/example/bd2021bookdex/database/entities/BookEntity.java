package com.example.bd2021bookdex.database.entities;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
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
    //Despite the fact that FetchType.EAGER should be avoided, I decided to use it here, 
    //because every time we get BookEntity, we want to know about its authors
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
          name = "AUTHORSHIP",
          joinColumns = { @JoinColumn(name = "book_id")},
          inverseJoinColumns = { @JoinColumn(name = "author_id")}
    )
    private Set<AuthorEntity> authors;
    
    //as above
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "BOOK_TAGGING",
            joinColumns = { @JoinColumn(name = "book_id")},
            inverseJoinColumns = { @JoinColumn(name = "tag_id")}
    )
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
         this.tags = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getTitle() {
        return title;
    }

    public Set<AuthorEntity> getAuthors() {
        int x = authors.size();
        return new HashSet<>(authors);
    }

    public String getLink() {
        return thumbnailLink;
    }

    public String getDesc() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getGenre() {
        return genre;
    }

    public int getCount() {
        return pageCount;
    }

    public Set<TagEntity> getTags() {
        return tags;
    }

}
