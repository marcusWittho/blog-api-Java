package com.blog.api.controller;

import com.blog.api.dto.UserDto;
import com.blog.api.model.User;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog-api")
public class UserController {

  private UserService userService;

  private UserRepository userRepository;

  private final PasswordEncoder encoder;

  public UserController(UserService userService, UserRepository userRepository,
                        PasswordEncoder encoder) {

    this.userService = userService;
    this.userRepository = userRepository;
    this.encoder = encoder;
  }

  @PostMapping
  public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {

    userDto.setPassword(encoder.encode(userDto.getPassword()));

    return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDto));
  }

  @GetMapping("/validate")
  public ResponseEntity<Boolean> isValid(@RequestBody UserDto userDto) {

    Optional<User> user = userRepository.findByEmail(userDto.getEmail());

    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    boolean valid = encoder.matches(userDto.getPassword(), user.get().getPassword());

    HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;

    return ResponseEntity.status(status).body(valid);
  }

  @GetMapping
  public ResponseEntity<List<User>> listAll() {

    return ResponseEntity.status(HttpStatus.OK).body(userService.listUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> userById(@PathVariable Integer id) {

    return ResponseEntity.status(HttpStatus.OK).body(userService.userById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {

    return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, userDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> removeUser(@PathVariable Integer id) {

    return ResponseEntity.status(HttpStatus.OK).body(userService.removeUser(id));
  }
}
