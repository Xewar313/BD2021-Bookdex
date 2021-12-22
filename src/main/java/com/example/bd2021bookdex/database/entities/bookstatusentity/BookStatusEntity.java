package com.example.bd2021bookdex.database.entities.bookstatusentity;

import com.example.bd2021bookdex.database.entities.BookEntity;
import com.example.bd2021bookdex.database.entities.UserEntity;

import javax.persistence.*;
import java.io.Serializable;

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
    public BookEntity getBook() {
        return  book;
    }
}
