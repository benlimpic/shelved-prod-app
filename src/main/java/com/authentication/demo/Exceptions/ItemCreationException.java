package com.authentication.demo.Exceptions;

public class ItemCreationException extends RuntimeException {

  public ItemCreationException() {
    super();
  }

  public ItemCreationException(String message) {
    super(message);
  }

  public ItemCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}
