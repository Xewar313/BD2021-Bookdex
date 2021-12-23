package com.example.bd2021bookdex.database;

import com.example.bd2021bookdex.database.entities.AuthorEntity;
import com.example.bd2021bookdex.database.entities.BookEntity;
import com.example.bd2021bookdex.database.entities.ChangesEntity;
import com.example.bd2021bookdex.database.entities.UserEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEnum;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

@Component
public class DatabaseSearcher {

    private final SessionFactory factory;
    private final Vector<String> authors, categories, titles, genres;
    private UserEntity user;
    private BookStatusEnum status;
    @Autowired
    public DatabaseSearcher(SessionFactory injected) {
        authors = new Vector<>();
        categories = new Vector<>();
        titles = new Vector<>();
        genres = new Vector<>();
        factory = injected;
    }
    public void setUser(UserEntity s) {
        user = s;
    }
    public void addAuthor(String name) {
        authors.addAll(List.of(name.split("\\s+")));
        int x;
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
    public List<BookStatusEntity> findFoundBooks() {
        Transaction tx = null;
        List<BookStatusEntity> results = null;
        if (user == null)
            return null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BookStatusEntity> query = cb.createQuery(BookStatusEntity.class);
            Root<BookStatusEntity> root = query.from(BookStatusEntity.class);
            Join<BookStatusEntity, BookEntity> bookJoin = root.join("book");
            List<Predicate> predicates = new LinkedList<>();
                
            predicates.add(cb.equal(root.get("id").get("ownerId"), user.getId()));
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            if (!authors.isEmpty()) {
                Join<BookEntity, AuthorEntity> authorsJoin = bookJoin.join("authors");
                List<Predicate> tempList = new LinkedList<>();
                for (String author: authors)
                    tempList.add(cb.like(authorsJoin.get("name"), '%' + author + '%'));
                predicates.add(cb.or(tempList.toArray(new Predicate[]{})));
            }
           
            for (String title: titles)
                predicates.add(cb.like(bookJoin.get("title"), '%' + title + '%'));
            if (!categories.isEmpty()) {
                List<Predicate> tempList = new LinkedList<>();
                for (String category: categories)
                    tempList.add(cb.like(bookJoin.get("category"), '%' + category + '%'));
                predicates.add(cb.or(cb.and(tempList.toArray(new Predicate[]{})), cb.isNull(bookJoin.get("category"))));
            }
            if (!genres.isEmpty()) {
                List<Predicate> tempList = new LinkedList<>();
                for (String genre: genres)
                    tempList.add(cb.like(bookJoin.get("genre"), '%' + genre + '%'));
                predicates.add(cb.or(cb.and(tempList.toArray(new Predicate[]{})), cb.isNull(bookJoin.get("genre"))));
            }
            query.where(cb.and(predicates.toArray(new Predicate[] {})));
            Query<BookStatusEntity> q = session.createQuery(query);
            results = q.getResultList();
            for (var r : results) {
                System.out.println(r.getBook().getId());
            }
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        reset();
        return results;
    }
    
    public List<BookEntity> findBooks() {
        Transaction tx = null;
        List<BookEntity> results = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BookEntity> query = cb.createQuery(BookEntity.class);
            Root<BookEntity> root = query.from(BookEntity.class);
            List<Predicate> predicates = new LinkedList<>();
            
            if (!authors.isEmpty()) {
                Join<BookEntity, AuthorEntity> authorsJoin = root.join("authors");
                List<Predicate> tempList = new LinkedList<>();
                for (String author: authors)
                    tempList.add(cb.like(authorsJoin.get("name"), '%' + author + '%'));
                predicates.add(cb.or(tempList.toArray(new Predicate[]{})));
            }

            for (String title: titles)
                predicates.add(cb.like(root.get("title"), '%' + title + '%'));
            if (!categories.isEmpty()) {
                List<Predicate> tempList = new LinkedList<>();
                for (String category: categories)
                    tempList.add(cb.like(root.get("category"), '%' + category + '%'));
                predicates.add(cb.or(cb.and(tempList.toArray(new Predicate[]{})), cb.isNull(root.get("category"))));
            }
            if (!genres.isEmpty()) {
                List<Predicate> tempList = new LinkedList<>();
                for (String genre: genres)
                    tempList.add(cb.like(root.get("genre"), '%' + genre + '%'));
                predicates.add(cb.or(cb.and(tempList.toArray(new Predicate[]{})), cb.isNull(root.get("genre"))));
            }
            query.where(cb.and(predicates.toArray(new Predicate[] {})));
            Query<BookEntity> q = session.createQuery(query);
            results = q.getResultList();
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        reset();
        return results;
    }
    public List<UserEntity> getUserList() {
        Transaction tx = null;
        List<UserEntity> results = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<UserEntity> query = cb.createQuery(UserEntity.class);
            Root<UserEntity> root = query.from(UserEntity.class);
            query.select(root);
            
            Query<UserEntity> q = session.createQuery(query);
            results = q.getResultList();
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return results;
    }
    
    public List<BookStatusEntity> getStatuses(List<BookEntity> bookList) {
        Transaction tx = null;
        List<BookStatusEntity> results = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BookStatusEntity> query = cb.createQuery(BookStatusEntity.class);
            Root<BookStatusEntity> root = query.from(BookStatusEntity.class);
            List<Integer> ids  = new LinkedList<>();
            for (var book : bookList)
                ids.add(book.getId());
            query.where(cb.and(cb.equal(root.get("id").get("ownerId"), user.getId()), root.get("id").get("bookId").in(ids)));

            Query<BookStatusEntity> q = session.createQuery(query);
            results = q.getResultList();
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return results;
    }
}
