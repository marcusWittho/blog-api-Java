package com.blog.api.controller;

import com.blog.api.dto.UserDto;
import com.blog.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog-api")
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/add")
  public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {

    return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDto));
  }
}
