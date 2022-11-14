package com.blog.api.service;

import com.blog.api.commons.CustomBadRequestException;
import com.blog.api.commons.CustomNotFoundException;
import com.blog.api.commons.CustomUnexpectedException;
import com.blog.api.dto.UserDto;
import com.blog.api.model.User;
import com.blog.api.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

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
        throw new CustomBadRequestException("Informe o nome de usuário.");
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
      logger.error("Erro inesperado ao tentar inserir um novo usuário.");
      throw new CustomUnexpectedException("Erro inesperado ao tentar inserir um novo usuário.");
    }
  }

  public List<User> listUsers() {

    try {
      logger.info("Pedido para listar os usuários cadastrados.");

      List<User> allUsers = this.userRepository.findAll();

      if (allUsers.isEmpty()) {
        throw new CustomNotFoundException("Nenhum usuário foi encontrado.");
      }

      return allUsers;
    } catch (CustomNotFoundException err) {
      logger.info("Info message: " + err.getMessage());
      throw err;
    } catch (Exception err) {
      logger.error("Erro inesperado ao listar usuários cadastrados.");
      throw new CustomUnexpectedException("Erro inesperado ao listar usuários cadastrados.");
    }
  }

  public User userById(Integer id) {

    try {
      logger.info("Buscando usuário pelo id.");

      Optional<User> user = userRepository.findById(id);

      if (user.isEmpty()) {
        throw new CustomNotFoundException("Usuário não encontrado.");
      }

      return user.get();
    } catch (CustomNotFoundException err) {
      logger.error("Error message: " + err.getMessage());
      throw err;
    } catch (Exception err) {
      logger.error("Erro inesperado ao tentar buscar determinado usuário.");
      throw new CustomUnexpectedException("Erro inesperado ao tentar buscar determinado usuário.");
    }
  }

  @Transactional
  public User updateUser(Integer id, UserDto userDto) {

    try {
      logger.info("Buscando usuário que será atualizado.");

      Optional<User> toBeUpdated = userRepository.findById(id);

      if (toBeUpdated.isEmpty()) {
        throw new CustomNotFoundException("Usuário não encontrado.");
      }

      if (userDto.getUsername().isEmpty()) {
        throw new CustomBadRequestException("Informe o nome de usuário.");
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

      toBeUpdated.get().setUsername(userDto.getUsername());
      toBeUpdated.get().setEmail(userDto.getEmail());
      toBeUpdated.get().setPassword(userDto.getPassword());

      userRepository.save(toBeUpdated.get());

      return toBeUpdated.get();
    } catch (CustomNotFoundException err) {
      logger.error("Error message: " + err.getMessage());
      throw err;
    } catch (CustomBadRequestException err) {
      logger.error("Error message: " + err.getMessage());
      throw err;
    } catch (CustomUnexpectedException err) {
      logger.error("Erro inesperado ao tentar atualizar determinado usuário.");
      throw new CustomUnexpectedException(
          "Erro inesperado ao tentar atualizar determinado usuário.");
    }
  }

  public String removeUser(Integer id) {

    try {
      logger.info("Buscando usuário que será removido.");

      Optional<User> toBeRemoved = userRepository.findById(id);

      if (toBeRemoved.isEmpty()) {
        throw new CustomNotFoundException("Usuário não encontrado.");
      }

      userRepository.deleteById(id);

      return "Usuário " + toBeRemoved.get().getUsername() + " removido.";
    } catch (CustomNotFoundException err) {
      logger.error("Error message: " + err.getMessage());
      throw err;
    } catch (CustomUnexpectedException err) {
      logger.error("Erro inesperado ao tentar remover determinado usuário.");
      throw new CustomUnexpectedException(
          "Erro inesperado ao tentar remover determinado usuário.");
    }
  }
}
