package com.blog.api.commons;

public class CustomBadRequestException extends RuntimeException {

  public CustomBadRequestException(String message) {

    super(message);
  }
}
