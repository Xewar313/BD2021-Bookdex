package com.example.bd2021bookdex.database;

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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Component
public class DatabaseModifier {
    private final SessionFactory factory;
    @Autowired
    public DatabaseModifier(SessionFactory injected) {
        factory = injected;
        removeRecentChanges();
        removeStatuses();
    }

    public void removeRecentChanges() {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ChangesEntity> query = cb.createQuery(ChangesEntity.class);
            Root<ChangesEntity> root = query.from(ChangesEntity.class);
            query.select(root);

            Query<ChangesEntity> q = session.createQuery(query);
            var results = q.getResultList();
            
            for (var res : results) {
                if (res.getDate().toLocalDate().compareTo(ZonedDateTime.now().minusWeeks(1).toLocalDate()) < 0) {
                    session.delete(res);
                }
            }
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void removeStatuses() {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BookStatusEntity> query = cb.createQuery(BookStatusEntity.class);
            Root<BookStatusEntity> root = query.from(BookStatusEntity.class);
            query.select(root);

            Query<BookStatusEntity> q = session.createQuery(query);
            var results = q.getResultList();

            for (var res : results) {
                if (res.getDate().toLocalDate().compareTo(ZonedDateTime.now().minusWeeks(3).toLocalDate()) < 0 &&
                        res.getStatus() == BookStatusEnum.FOUND)
                    session.delete(res);
                else if (res.getStatus() == BookStatusEnum.ADDED 
                        && res.getDate().toLocalDate().compareTo(ZonedDateTime.now().minusWeeks(1).toLocalDate()) < 0) {
                    res.decreaseStatus();
                    session.update(res);
                }
                
            }
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public boolean addToDb(Object d) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.save(d);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
        return true;
    }
    
    public void updateInDb(Object d) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.update(d);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
        }
    }
    
    public void deleteFromDb(Object obj) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
    
    public boolean deleteFromDb(UserEntity user) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            UserEntity toRemove = session.get(UserEntity.class, user.getId());
            for (var i : toRemove.getFoundBooks())
                session.delete(i);
            for (var i : toRemove.getChanges())
                session.delete(i);
            for (var i : toRemove.getCollections())
                session.delete(i);
            session.delete(toRemove);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public BookStatusEntity tryAddingConnection(BookEntity book, UserEntity user) {
        BookStatusEntity toAdd = new BookStatusEntity(BookStatusEnum.FOUND,
                false, 
                java.sql.Date.valueOf(LocalDate.now()),
                book,
                user);
        if (addToDb(toAdd)) {
            addToDb(new ChangesEntity(toAdd));
            return toAdd;
        }
        return null;
    }
}
