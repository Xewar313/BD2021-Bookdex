package com.example.bd2021bookdex.database;

import com.example.bd2021bookdex.BookSearcher;
import com.example.bd2021bookdex.database.entities.*;
import com.example.bd2021bookdex.database.entities.bookstatusentity.BookStatusEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

@Component
public class DatabaseSearcher extends BookSearcher {

    private final List<TagEntity> tags = new LinkedList<>();

    private Vector<String> collectionName = new Vector<>();
    
    protected final SessionFactory factory;
    @Autowired
    public DatabaseSearcher(SessionFactory injected) {
        super();
        factory = injected;
    }
    
    public void addCollName(String name) {
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

    public List<BookStatusEntity> findFoundBooks() {
        Transaction tx = null;
        List<BookStatusEntity> results = null;
        if (user == null)
            return null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BookStatusEntity> query = cb.createQuery(BookStatusEntity.class).distinct(true);
            Root<BookStatusEntity> root = query.from(BookStatusEntity.class);
            Join<BookStatusEntity, BookEntity> bookJoin = root.join("book");
            List<Predicate> predicates = new LinkedList<>();
                
            predicates.add(cb.equal(root.get("id").get("ownerId"), user.getId()));
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (!tags.isEmpty()) {
                Join<BookEntity, TagEntity> tagJoin = bookJoin.join("tags");
                List<Predicate> tempList = new LinkedList<>();
                for (TagEntity t: tags)
                    tempList.add(cb.like(tagJoin.get("id"), "" + t.getId()));
                predicates.add(cb.and(tempList.toArray(new Predicate[]{})));
            }
            if (!authors.isEmpty()) {
                Join<BookEntity, AuthorEntity> authorsJoin = bookJoin.join("authors");
                List<Predicate> tempList = new LinkedList<>();
                for (String author: authors)
                    tempList.add(cb.like(authorsJoin.get("name"), '%' + author + '%'));
                predicates.add(cb.and(tempList.toArray(new Predicate[]{})));
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
        return results;
    }
    
    public List<BookEntity> findBooks() {
        Transaction tx = null;
        List<BookEntity> results = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BookEntity> query = cb.createQuery(BookEntity.class).distinct(true);
            Root<BookEntity> root = query.from(BookEntity.class);
            List<Predicate> predicates = new LinkedList<>();
            
            if (!authors.isEmpty()) {
                Join<BookEntity, AuthorEntity> authorsJoin = root.join("authors");
                List<Predicate> tempList = new LinkedList<>();
                for (String author: authors)
                    tempList.add(cb.like(authorsJoin.get("name"), '%' + author + '%'));
                predicates.add(cb.or(tempList.toArray(new Predicate[]{})));
            }
            if (!tags.isEmpty()) {
                Join<BookEntity, TagEntity> tagJoin = root.join("tags");
                List<Predicate> tempList = new LinkedList<>();
                for (TagEntity t: tags)
                    tempList.add(cb.equal(tagJoin.get("id"), t.getId()));
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
   
    
    public List<BookCollectionEntity> getCollections() {
        Transaction tx = null;
        List<BookCollectionEntity> results = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            UserEntity temp = session.get(UserEntity.class, user.getId());
            Hibernate.initialize(temp.getCollections());
            results = new LinkedList<>(temp.getCollections());
            tx.commit();
            
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return results;
    }
}
