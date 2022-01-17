package com.example.bd2021bookdex.database.entities.bookstatusentity;

import com.example.bd2021bookdex.database.entities.BookCollectionEntity;
import com.example.bd2021bookdex.database.entities.BookEntity;
import com.example.bd2021bookdex.database.entities.UserEntity;

import javax.persistence.*;
import javax.swing.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "BOOK_STATUS")
public class BookStatusEntity implements Serializable {
    @AttributeOverrides({
            @AttributeOverride(name="bookId", column = @Column(name="book_id")),
            @AttributeOverride(name="ownerId", column = @Column(name="owner_id"))
    })
    @EmbeddedId
    private BookStatusKey id;

    @MapsId("bookId")
    @ManyToOne
    private BookEntity book;
    @MapsId("ownerId")
    @ManyToOne
    private UserEntity owner;
    
    @ManyToMany
    @JoinTable(
            name = "COLLECTION_OWNERSHIP",
            joinColumns = {
                    @JoinColumn(name = "book_id"),
                    @JoinColumn(name = "owner_id")
            },
            inverseJoinColumns = { @JoinColumn(name = "collection_id")}
    )
    private Set<BookCollectionEntity> collections;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "book_state")
    private BookStatusEnum status;
    
    @Column(name = "is_book_owned")
    private boolean isOwned;
    
    @Column(name = "date_changed")
    private java.sql.Date lastStatusChange;

    public BookStatusEntity(){}
    public BookStatusEntity(BookStatusEnum status, boolean isOwned, java.sql.Date lastChange, BookEntity book, UserEntity user) {
        this.status = status;
        this.isOwned = isOwned;
        this.lastStatusChange = lastChange;   
        id = new BookStatusKey(book.getId(), user.getId());
        this.book = book;
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookStatusEntity that = (BookStatusEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public BookEntity getBook() {
        return  book;
    }

    public boolean isOwned() {
        return isOwned;
    }
    public BookStatusEnum getStatus() {
        return status;
    }

    public UserEntity getUser() {
        return owner;
    }

    public Date getDate() {
        return lastStatusChange;
    }

    public BookStatusKey getId() {
        return id;
    }

    public Set<BookCollectionEntity> getCollection() {
        return collections;
    }

    public void setCollections(DefaultListModel<BookCollectionEntity> added) {
        collections = new HashSet<>();
        for (var coll: added.toArray()) {
            collections.add((BookCollectionEntity) coll);
        }
    }
    
    public String getTitle() {
        if (status != BookStatusEnum.FOUND) {
            return book.getTitle();
        }
        else {
            return book.getTitle().replaceAll("\\S", "?");
        }
    }

    public void increaseStatus() {
        switch (status) {
            case FOUND:
                status = BookStatusEnum.ADDED;
                break;
            case ADDED:
                status = BookStatusEnum.IN_PROGRESS;
                break;
            case IN_PROGRESS:
                status = BookStatusEnum.READ;
                break;
            case READ:
        }
        lastStatusChange = java.sql.Date.valueOf(LocalDate.now());
    }

    public void changeOwned() {
        isOwned = !isOwned;
        if (status == BookStatusEnum.FOUND) 
            status = BookStatusEnum.ADDED;
    }

    public void decreaseStatus() {
        if (status != BookStatusEnum.ADDED)
            return;
        status = BookStatusEnum.FOUND;
        lastStatusChange = java.sql.Date.valueOf(LocalDate.now());
    }
}
