package com.icode.twitterclonebackend.exception;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String s) {
        super(s);
    }
}
