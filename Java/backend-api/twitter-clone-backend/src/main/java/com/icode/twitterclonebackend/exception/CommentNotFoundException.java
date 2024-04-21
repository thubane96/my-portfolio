package com.icode.twitterclonebackend.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String s) {
        super(s);
    }
}
