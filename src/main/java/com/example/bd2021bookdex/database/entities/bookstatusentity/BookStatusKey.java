package com.example.bd2021bookdex.database.entities.bookstatusentity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookStatusKey implements Serializable {
    
    private int bookId;
    private int ownerId;
    BookStatusKey(int bid, int oid) {
        bookId = bid;
        ownerId = oid;
    }

    public BookStatusKey() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookStatusKey that = (BookStatusKey) o;
        return bookId == that.bookId && ownerId == that.ownerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, ownerId);
    }
}
