package com.example.bd2021bookdex.database.entities;

import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "BOOK_COLLECTION")
public class BookCollectionEntity implements Serializable {
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
    public BookCollectionEntity(String name, String desc, UserEntity owner){
        this.name = name;
        this.desc = desc;
        this.owner = owner;
        this.books = new HashSet<>();
    }

    public Set<BookStatusEntity> getBooks() {
        return books;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCollectionEntity that = (BookCollectionEntity) o;
        return id == that.id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
