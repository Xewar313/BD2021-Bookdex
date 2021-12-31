package com.example.bd2021bookdex.database.entities;

import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEnum;

import javax.persistence.*;

@Entity
@Table(name = "RECENT_CHANGES")
public class ChangesEntity implements Comparable<ChangesEntity>{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "change_type")
    private BookStatusEnum changeType;
    
    @Column(name = "change_date")
    private java.sql.Date lastStatusChange;

    @ManyToOne
    @JoinColumn(name = "change_user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "change_book_id")
    private BookEntity book;
    
    public ChangesEntity(){}
    public ChangesEntity(BookStatusEntity change) {
        book = change.getBook();
        user = change.getUser();
        lastStatusChange = change.getDate();
        changeType = change.getStatus();
    }

    @Override
    public int compareTo(ChangesEntity o) {
        if (o.lastStatusChange.compareTo(lastStatusChange) == 0)
            return o.book.getTitle().compareTo(book.getTitle());
        return o.lastStatusChange.compareTo(lastStatusChange);
    }

    public BookEntity getBook() {
        return book;
    }

    public BookStatusEnum getStatus() {
        return changeType;
    }

    public java.sql.Date getDate() {
        return lastStatusChange;
    }
}
