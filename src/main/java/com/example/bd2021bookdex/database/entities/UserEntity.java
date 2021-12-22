package com.example.bd2021bookdex.database.entities;

import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "BOOKDEX_USER")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "user_password")
    private String password;
    @OneToMany(mappedBy = "owner")
    private Set<BookStatusEntity> foundBooks;
    @OneToMany(mappedBy = "owner")
    private Set<BookCollectionEntity> collections;
    public UserEntity() {
        
    }
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public int getId() {
        return id;
    }

    public Set<BookStatusEntity> getFoundBooks() {
        return foundBooks;
    }
}
