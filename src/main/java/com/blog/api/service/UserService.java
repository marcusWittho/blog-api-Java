package com.blog.api.service;

import com.blog.api.commons.CustomBadRequestException;
import com.blog.api.dto.UserDto;
import com.blog.api.model.User;
import com.blog.api.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

  private UserRepository userRepository;

  private final Logger logger = Logger.getLogger(UserService.class);

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public String addUser(UserDto userDto) {

    try {
      logger.info("Inserção do novo usuário: " + userDto.getUsername());

      if (userDto.getUsername().isEmpty()) {
        throw new CustomBadRequestException("Informe o nome de usuário");
      }

      if (userDto.getEmail().isEmpty()) {
        throw new CustomBadRequestException("Informe o email.");
      }

      if (this.userRepository.existsByEmail(userDto.getEmail())) {
        throw new CustomBadRequestException("Existe um usuário cadastrado com este email.");
      }

      if (userDto.getPassword().isEmpty()) {
        throw new CustomBadRequestException("Informe o password.");
      }

      User newUser = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());

      this.userRepository.save(newUser);

      return "Usuário " + userDto.getUsername() + " cadastrado com sucesso.";
    } catch (CustomBadRequestException err) {
      logger.error("Error message: " + err.getMessage());
      throw err;
    } catch (Exception err) {
      logger.error("Error: " + err.getCause());
      throw err;
    }
  }
}
