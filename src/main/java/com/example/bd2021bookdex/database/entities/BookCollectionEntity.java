package com.example.bd2021bookdex.database.entities;

import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "BOOK_COLLECTION")
public class BookCollectionEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "collection_name")
    private String name;
    @Column(name = "user_desc")
    private String desc;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
    //it is unnecessary - we can get it from BookStatusEntity, but it makes it easier to get collections
    @ManyToMany
    @JoinTable(
            name = "COLLECTION_OWNERSHIP",
            joinColumns = { @JoinColumn(name = "collection_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "book_id"),
                    @JoinColumn(name = "owner_id")
            }
            )
    private Set<BookStatusEntity> books;
    
    public BookCollectionEntity(){}
    public BookCollectionEntity(String name, String desc, UserEntity owner, Set<BookStatusEntity> books){
        this.name = name;
        this.desc = desc;
        this.owner =owner;
        this.books = books;
    }
    
}
