package com.blog.api.controller;

import com.blog.api.commons.CustomBadRequestException;
import com.blog.api.commons.DataError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceManager {

  @ExceptionHandler({CustomBadRequestException.class})
  public ResponseEntity<DataError> handlerBadRequest(CustomBadRequestException exception) {

    DataError error = new DataError(exception.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<DataError> handlerException(Exception exception) {

    DataError error = new DataError();

    error.setCause(exception.getCause());

    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(error);
  }
}