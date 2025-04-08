package com.authentication.demo.Exceptions;

public class CollectionCreationException extends RuntimeException {

    public CollectionCreationException() {
        super();
    }

    public CollectionCreationException(String message) {
        super(message);
    }

    public CollectionCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}