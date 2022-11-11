package com.blog.api.commons;

public class DataError {

  private String error;

  private Throwable cause;

  public DataError() {}

  public DataError(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public Throwable getCause() {
    return cause;
  }

  public void setCause(Throwable cause) {
    this.cause = cause;
  }
}
