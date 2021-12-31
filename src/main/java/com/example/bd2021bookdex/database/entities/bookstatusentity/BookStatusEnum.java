package com.example.bd2021bookdex.database.entities.bookstatusentity;

public enum BookStatusEnum {
    FOUND, ADDED, IN_PROGRESS, READ;

    @Override
    public String toString() {
        switch (this) {
            case READ:
                return "Finished reading";
            case FOUND:
                return "Found while searching";
            case ADDED:
                return "You found this book interesting";
            case IN_PROGRESS:
                return "You are currently reading this book";
        }
        return null;
    }
}

