package com.example.bd2021bookdex.database;

import com.example.bd2021bookdex.database.entities.BookEntity;
import com.example.bd2021bookdex.database.entities.ChangesEntity;
import com.example.bd2021bookdex.database.entities.TagEntity;
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

@Component
public class DatabaseModifier {
    private final SessionFactory factory;
    @Autowired
    public DatabaseModifier(SessionFactory injected) {
        factory = injected;
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
    public boolean updateInDb(Object d) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.update(d);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
        return true;
    }
    public boolean deleteFromDb(Object obj) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {

            tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean deleteFromDb(UserEntity user) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            for (var i : user.getFoundBooks())
                session.delete(i);
            for (var i : user.getChanges())
                session.delete(i);
            for (var i : user.getCollections())
                session.delete(i);
            session.delete(user);
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
        addToDb(toAdd);
        return toAdd;
    }
}
