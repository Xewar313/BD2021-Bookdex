package com.example.bd2021bookdex.database;

import com.example.bd2021bookdex.BookSearcher;
import com.example.bd2021bookdex.database.entities.*;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusKey;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

@Component
public class DatabaseSearcher extends BookSearcher {

    private final List<TagEntity> tags = new LinkedList<>();

    private final Vector<String> collectionName = new Vector<>();
    
    protected final SessionFactory factory;
    @Autowired
    public DatabaseSearcher(SessionFactory injected) {
        super();
        factory = injected;
    }
    
    public void addCollName(String name) {
        if (Objects.equals(name, ""))
            return;
        collectionName.addAll(List.of(name.split("\\s+")));
    }
    public void addTag(TagEntity tag) {
        tags.add(tag);
    }

    @Override
    public void reset() {
        super.reset();
        tags.clear();
        collectionName.clear();
    }

    private <T> void addTagsPredicates(CriteriaBuilder cb, From<T,BookEntity> from, List<Predicate> toAdd) {
        Join<BookEntity, TagEntity> tagJoin = from.join("tags");
        List<Predicate> tempList = new LinkedList<>();
        for (TagEntity t: tags)
            tempList.add(cb.equal(tagJoin.get("id"), t.getId()));
        toAdd.add(cb.or(tempList.toArray(new Predicate[]{})));
    }

    private <T> void addAuthorPredicates(CriteriaBuilder cb, From<T,BookEntity> from, List<Predicate> toAdd) {
        Join<BookEntity, AuthorEntity> authorsJoin = from.join("authors");
        List<Predicate> tempList = new LinkedList<>();
        for (String author: authors)
            tempList.add(cb.like(cb.lower(authorsJoin.get("name")), '%' + author.toLowerCase() + '%'));
        toAdd.add(cb.or(tempList.toArray(new Predicate[]{})));
    }

    private <T> void addCategoryAndGenrePredicates(CriteriaBuilder cb, From<T,BookEntity> from, List<Predicate> toAdd) {
        if (!categories.isEmpty()) {
            List<Predicate> tempList = new LinkedList<>();
            for (String category: categories)
                tempList.add(cb.like(cb.lower(from.get("category")), '%' + category.toLowerCase() + '%'));
            toAdd.add(cb.and(tempList.toArray(new Predicate[]{})));
        }
        if (!genres.isEmpty()) {
            List<Predicate> tempList = new LinkedList<>();
            for (String genre: genres)
                tempList.add(cb.like(cb.lower(from.get("genre")), '%' + genre.toLowerCase() + '%'));
            toAdd.add(cb.and(tempList.toArray(new Predicate[]{})));
        }
    }
    
    public List<BookStatusEntity> findFoundBooks() {
        Transaction tx = null;
        List<BookStatusEntity> results = null;
        if (user == null)
            return null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class).distinct(true);
            Root<BookStatusEntity> root = query.from(BookStatusEntity.class);
            Join<BookStatusEntity, BookEntity> bookJoin = root.join("book");
            List<Predicate> predicates = new LinkedList<>();
                
            predicates.add(cb.equal(root.get("id").get("ownerId"), user.getId()));
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (!tags.isEmpty()) {
                addTagsPredicates(cb, bookJoin, predicates);
            }
            if (!authors.isEmpty()) {
               addAuthorPredicates(cb, bookJoin, predicates);
            }
           
            for (String title: titles)
                predicates.add(cb.like(cb.lower(bookJoin.get("title")), '%' + title.toLowerCase() + '%'));

            addCategoryAndGenrePredicates(cb, bookJoin, predicates);
            query.where(cb.and(predicates.toArray(new Predicate[] {})));
            query.multiselect(root.get("id"), cb.count(root)).groupBy(root.get("id"));
            Query<Object[]> resultsId = session.createQuery(query);
            var res = resultsId.list();
            List<BookStatusKey> ids = new LinkedList<>();
            for (var x : res) {
                if (tags.size() == 0 || (Long) x[1] == tags.size())
                    ids.add((BookStatusKey) x[0]);
            }
            CriteriaQuery<BookStatusEntity> queryObj = cb.createQuery(BookStatusEntity.class);
            root = queryObj.from(BookStatusEntity.class);
            queryObj.where(root.get("id").in(ids));
            Query<BookStatusEntity> q = session.createQuery(queryObj);
            results = q.getResultList();
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return results;
    }
    
    public List<BookEntity> findBooks() {
        if (authors.isEmpty() && tags.isEmpty() && titles.isEmpty() && genres.isEmpty())
            return new LinkedList<>();
        Transaction tx = null;
        List<BookEntity> results = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class).distinct(true);
            Root<BookEntity> root = query.from(BookEntity.class);
            List<Predicate> predicates = new LinkedList<>();
            
            if (!authors.isEmpty()) {
                addAuthorPredicates(cb, root, predicates);
            }
            if (!tags.isEmpty()) {
                addTagsPredicates(cb, root, predicates);
            }
            for (String title: titles)
                predicates.add(cb.like(root.get("title"), '%' + title + '%'));

            addCategoryAndGenrePredicates(cb, root, predicates);
            query.where(cb.and(predicates.toArray(new Predicate[] {})));
            query.multiselect(root.get("id"), cb.count(root)).groupBy(root.get("id"));
            Query<Object[]> resultsId = session.createQuery(query);
            var res = resultsId.list();
            List<Integer> ids = new LinkedList<>();
            for (var x : res) {
                if (tags.size() == 0 || (Long) x[1] == tags.size())
                    ids.add((Integer) x[0]);
            }
            CriteriaQuery<BookEntity> queryObj = cb.createQuery(BookEntity.class);
            root = queryObj.from(BookEntity.class);
            queryObj.where(root.get("id").in(ids));
            Query<BookEntity> q = session.createQuery(queryObj);
            results = q.getResultList();
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
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
    
    public List<TagEntity> getTags() {
        Transaction tx = null;
        List<TagEntity> results = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<TagEntity> query = cb.createQuery(TagEntity.class);
            Root<TagEntity> root = query.from(TagEntity.class);
            query.select(root);

            Query<TagEntity> q = session.createQuery(query);
            results = q.getResultList();
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return results;
    }
    
    public AuthorEntity getAuthor(String name) {
        Transaction tx = null;
        AuthorEntity result = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<AuthorEntity> query = cb.createQuery(AuthorEntity.class);
            Root<AuthorEntity> root = query.from(AuthorEntity.class);
            query.where(cb.equal(root.get("name"), name));

            Query<AuthorEntity> q = session.createQuery(query);
            if (!q.getResultList().isEmpty())
                result = q.getResultList().get(0);
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return result;
    }
    public BookEntity getByGoogleId(String id) {
        Transaction tx = null;
        BookEntity result = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BookEntity> query = cb.createQuery(BookEntity.class);
            Root<BookEntity> root = query.from(BookEntity.class);
            query.where(cb.equal(root.get("googleId"), id));

            Query<BookEntity> q = session.createQuery(query);
            if (!q.getResultList().isEmpty())
                result = q.getResultList().get(0);
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return result;
    }

    public BookStatusEntity getUpdatedStatus(BookStatusEntity src) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.refresh(src);
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return src;
    }
    
    public List<BookCollectionEntity> getStatusCollection(BookStatusEntity src) {
        Transaction tx = null;
        List<BookCollectionEntity> result = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.refresh(src);
            Hibernate.initialize(src.getCollection());
            result = new LinkedList<>(src.getCollection());
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return result;
    }
    
    public List<BookCollectionEntity> getCollections(boolean initialize) {
        Transaction tx = null;
        List<BookCollectionEntity> result = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BookCollectionEntity> query = cb.createQuery(BookCollectionEntity.class);
            Root<BookCollectionEntity> root = query.from(BookCollectionEntity.class);
            Predicate toSearch = cb.equal(root.get("owner"), user);
            if (!collectionName.isEmpty()) {
                List<Predicate> tempList = new LinkedList<>();
                for (String name: collectionName)
                    tempList.add(cb.like(root.get("name"), "%" + name + "%"));
                toSearch = cb.and(cb.and(tempList.toArray(new Predicate[]{})), toSearch);
            }
            query.where(toSearch);
            result = session.createQuery(query).list();
            if (initialize)
                for (var res : result) {
                    Hibernate.initialize(res.getBooks());
                }
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return result;
    }
}
