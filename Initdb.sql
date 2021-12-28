CREATE SEQUENCE hibernate_sequence START 1;
CREATE TABLE BOOK (
    id SERIAL NOT NULL,
    title VARCHAR(150) NOT NULL,
    thumbnail_link VARCHAR(200) default NULL,
    page_count INT default NULL,
    google_id VARCHAR(30) default NULL,
    category VARCHAR(40) default NULL,
    genre VARCHAR(40) default NULL,
    book_desc VARCHAR(4000) default NULL,
    PRIMARY KEY (id),
    UNIQUE (google_id)
);
CREATE TABLE AUTHOR (
    id SERIAL NOT NULL,
    auth_name VARCHAR(40) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE AUTHORSHIP (
    book_id INT REFERENCES BOOK (id) NOT NULL,
    author_id INT REFERENCES AUTHOR (id) NOT NULL,
    PRIMARY KEY (author_id, book_id)
);


CREATE TABLE BOOKDEX_USER (
    id SERIAL NOT NULL,
    username VARCHAR(40) UNIQUE NOT NULL,
    user_password VARCHAR(40) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE BOOK_STATUS (
    book_id INT NOT NULL,
    owner_id INT NOT NULL,
    book_state VARCHAR(20) NOT NULL,
    is_book_owned BOOLEAN default false NOT NULL,
    date_changed DATE NOT NULL,
    PRIMARY KEY (book_id, owner_id),
    CONSTRAINT book_id_fk FOREIGN KEY (book_id) REFERENCES BOOK (id),
    CONSTRAINT owner_id_fk FOREIGN KEY (owner_id) REFERENCES BOOKDEX_USER (id)
);

CREATE TABLE RECENT_CHANGES (
    id SERIAL NOT NULL,
    change_user_id INT NOT NULL,
    change_type VARCHAR(20) NOT NULL,
    change_book_id INT NOT NULL,
    change_date DATE NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT book_id_fk FOREIGN KEY (change_book_id) REFERENCES BOOK (id),
    CONSTRAINT user_id_fk FOREIGN KEY (change_user_id) REFERENCES BOOKDEX_USER (id)
);

CREATE TABLE BOOK_COLLECTION (
    id SERIAL NOT NULL,
    collection_name VARCHAR(50) NOT NULL,
    user_desc VARCHAR(300) default NULL,
    owner_id INT NOT NULL,--not needed per say (can be gotten from Many to Many relation), but makes it easier to use (no need for join)
    PRIMARY KEY (id),
    CONSTRAINT owner_id_fk FOREIGN KEY (owner_id) REFERENCES BOOKDEX_USER (id)
);

CREATE TABLE COLLECTION_OWNERSHIP (
    collection_id SERIAL NOT NULL,
    book_id INT NOT NULL,
    owner_id INT NOT NULL, 
    PRIMARY KEY (collection_id, book_id, owner_id),
    FOREIGN KEY (book_id, owner_id) REFERENCES BOOK_STATUS (book_id, owner_id),
    FOREIGN KEY (collection_id) REFERENCES BOOK_COLLECTION (id)
);

CREATE TABLE TAG (
    id SERIAL NOT NULL,
    tag_name VARCHAR(20) NOT NULL,
    PRIMARY KEY(id),
    UNIQUE(tag_name)
);

CREATE TABLE BOOK_TAGGING (
    tag_id SERIAL NOT NULL,
    book_id INT NOT NULL,
    PRIMARY KEY (tag_id, book_id),
    CONSTRAINT book_id_fk FOREIGN KEY (book_id) REFERENCES BOOK (id),
    CONSTRAINT tag_id_fk FOREIGN KEY (tag_id) REFERENCES TAG (id)
);
CREATE FUNCTION book_status_checker() 
   RETURNS TRIGGER 
  LANGUAGE PLPGSQL
AS $$
BEGIN
   IF (NEW.is_book_owned AND NEW.book_state = 'FOUND') THEN
    RAISE EXCEPTION 'Book cannot be owned and have state lower than added at the same time';
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER check_book_status 
BEFORE INSERT OR UPDATE ON BOOK_STATUS
FOR EACH ROW
EXECUTE PROCEDURE book_status_checker();

CREATE FUNCTION collection_ownership_checker() 
   RETURNS TRIGGER 
  LANGUAGE PLPGSQL
AS $$
BEGIN
   IF (NEW.owner_id != (SELECT owner_id FROM BOOK_COLLECTION WHERE id = NEW.collection_id)) THEN
    RAISE EXCEPTION 'Book of different owner cannot be added';
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER check_added_collection_entry 
BEFORE INSERT OR UPDATE ON COLLECTION_OWNERSHIP
FOR EACH ROW
EXECUTE PROCEDURE collection_ownership_checker();
