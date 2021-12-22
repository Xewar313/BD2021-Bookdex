package com.example.bd2021bookdex.database.entities;

import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEnum;

import javax.persistence.*;

@Entity
@Table(name = "RECENT_CHANGES")
public class ChangesEntity {
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
    public ChangesEntity(BookStatusEnum change, java.sql.Date changeDate, BookEntity object, UserEntity changer) {
        book = object;
        user = changer;
        lastStatusChange = changeDate;
        changeType = change;
    }
}
