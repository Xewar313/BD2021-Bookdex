package com.example.bd2021bookdex;

import com.example.bd2021bookdex.database.entities.UserEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEnum;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Vector;


public class BookSearcher {
    protected final Vector<String> authors, categories, titles, genres;
    protected UserEntity user;
    protected BookStatusEnum status;
    public BookSearcher() {
        authors = new Vector<>();
        categories = new Vector<>();
        titles = new Vector<>();
        genres = new Vector<>();
    }
    public void setUser(UserEntity s) {
        user = s;
    }
    public void addAuthor(String name) {
        List<String > x = List.of(name.split("\\s+"));
        authors.addAll(x);
    }
    public void addCategories(String name) {
        categories.addAll(List.of(name.split("\\s+")));
    }
    public void addTitles(String name) {
        titles.addAll(List.of(name.split("\\s+")));
    }
    public void addGenres(String name) {
        genres.addAll(List.of(name.split("\\s+")));
    }
    public void setStatus(BookStatusEnum state) {
        status = state;
    }
    public void reset() {
        status = null;
        authors.clear();
        categories.clear();
        titles.clear();
        genres.clear();
    }
}
