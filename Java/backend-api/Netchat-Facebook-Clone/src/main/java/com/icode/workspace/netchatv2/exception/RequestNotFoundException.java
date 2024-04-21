package com.icode.workspace.netchatv2.exception;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException(String request_not_found) {
        super(request_not_found);
    }
}
