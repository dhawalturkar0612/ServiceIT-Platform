package com.backend.exceptions;

public class AccountStatusException extends RuntimeException {

    public AccountStatusException(String message) {
        super(message);
    }
}