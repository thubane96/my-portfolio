package com.icode.twitterclonebackend.exception;

public class BookmarkNotFoundException extends RuntimeException {
    public BookmarkNotFoundException(String bookmark_was_not_found) {
        super(bookmark_was_not_found);
    }
}
