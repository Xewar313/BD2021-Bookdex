package com.example.bd2021bookdex.database.entities;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TAG")
public class TagEntity implements Comparable<TagEntity> {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "tag_name")
    private String name;
    @ManyToMany (mappedBy = "tags")
    private Set<BookEntity> books;
    
    public TagEntity(String name) {
        this.name = name;
    }
    
    public TagEntity() {
        
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEntity tagEntity = (TagEntity) o;
        return id == tagEntity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(TagEntity tagEntity) {
        return name.compareTo(tagEntity.name);
    }
}
