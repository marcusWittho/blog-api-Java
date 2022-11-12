package com.blog.api.commons;

public class CustomNotFoundException extends RuntimeException {

  public CustomNotFoundException(String message) {

    super(message);
  }
}
