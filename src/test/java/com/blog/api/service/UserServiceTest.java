package com.blog.api.service;

import com.blog.api.commons.CustomBadRequestException;
import com.blog.api.commons.CustomNotFoundException;
import com.blog.api.commons.CustomUnexpectedException;
import com.blog.api.dto.UserDto;
import com.blog.api.model.User;
import com.blog.api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService service;

  @Test
  @DisplayName("01 - Verifica se obteve sucesso na inserção de um novo usuário.")
  void insertUser() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("newUserPassword");

    String response = service.addUser(userDto);

    assertEquals("Usuário newUser cadastrado com sucesso.", response);
  }

  @Test
  @DisplayName("02 - Verifica se a exceção é lançada corretamente em caso de username não informado.")
  void exceptionUsernameIsEmpty() {
    UserDto userDto = new UserDto();

    userDto.setUsername("");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("newUserPassword");

    CustomBadRequestException thrown = assertThrows(
      CustomBadRequestException.class,
      () -> service.addUser(userDto),
      "Informe o nome de usuário."
    );

    assertTrue(thrown.getMessage().contains("Informe o nome de usuário."));
  }

  @Test
  @DisplayName("03 - Verifica se a exceção é lançada corretamente em caso de email não informado.")
  void exceptionEmailIsEmpty() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("");
    userDto.setPassword("newUserPassword");

    CustomBadRequestException thrown = assertThrows(
      CustomBadRequestException.class,
      () -> service.addUser(userDto),
      "Informe o email."
    );

    assertTrue(thrown.getMessage().contains("Informe o email."));
  }

  @Test
  @DisplayName("04 - Verifica se a exceção é lançada corretamente caso o email já exista no DB.")
  public void exceptionEmailExists() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("newUserPassword");

    when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

    CustomBadRequestException thrown = assertThrows(
      CustomBadRequestException.class,
      () -> service.addUser(userDto),
      "Existe um usuário cadastrado com este email."
    );

    assertTrue(thrown.getMessage().contains("Existe um usuário cadastrado com este email."));
  }

  @Test
  @DisplayName("05 - Verifica se a exceção é lançada corretamente em caso de password não informado.")
  void exceptionPasswordIsEmpty() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("");

    CustomBadRequestException thrown = assertThrows(
      CustomBadRequestException.class,
      () -> service.addUser(userDto),
      "Informe o password."
    );

    assertTrue(thrown.getMessage().contains("Informe o password."));
  }

  @Test
  @DisplayName("06 - Verifica lançamento de exceção inesperada.")
  void addUserUnexpectedException() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("newUserPassword");

    User newUser = new User("newUser", "newUserEmail", "newUserPassword");

    when(userRepository.save(newUser)).thenThrow(new CustomUnexpectedException("Erro inesperado ao tentar inserir um novo usuário."));

    CustomUnexpectedException thrown = assertThrows(
      CustomUnexpectedException.class,
      () -> service.addUser(userDto),
      "Erro inesperado ao tentar inserir um novo usuário."
    );

    assertTrue(thrown.getMessage().contains("Erro inesperado ao tentar inserir um novo usuário."));
  }

  @Test
  @DisplayName("07 - Verifica se o método listUsers retorna uma lista de User.")
  void listUsers() {
    User newUser = new User("newUser", "newUserEmail", "newUserPassword");

    List<User> usersList = new ArrayList<>();
    usersList.add(newUser);

    when(userRepository.findAll()).thenReturn(usersList);

    assertEquals(1, service.listUsers().size());
  }

  @Test
  @DisplayName("08 - Verifica se é lançada a exceção correta caso nenhum usuário seja encontrado.")
  void exceptionListIsEmpty() {
    CustomNotFoundException thrown = assertThrows(
      CustomNotFoundException.class,
      () -> service.listUsers(),
      "Nenhum usuário foi encontrado."
    );

    assertTrue(thrown.getMessage().contains("Nenhum usuário foi encontrado."));
  }

  @Test
  @DisplayName("09 - Verifica lançamento de exceção inesperada.")
  void ListUserUnexpectedException() {
    when(userRepository.findAll()).thenThrow(new CustomUnexpectedException("Erro inesperado ao listar usuários cadastrados."));

    CustomUnexpectedException thrown = assertThrows(
      CustomUnexpectedException.class,
      () -> service.listUsers(),
      "Erro inesperado ao listar usuários cadastrados."
    );

    assertTrue(thrown.getMessage().contains("Erro inesperado ao listar usuários cadastrados."));
  }

  @Test
  @DisplayName("10 - Verifica a busca por determinado usuário pelo id.")
  void findUserById() {
    User newUser = new User("newUser", "newUserEmail", "newUserPassword");

    when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));

    assertEquals("newUserEmail", service.userById(newUser.getId()).getEmail());
  }

  @Test
  @DisplayName("11 - Verifica busca por usuário inexistente.")
  void userByIdNotFound() {
    CustomNotFoundException thrown = assertThrows(
      CustomNotFoundException.class,
      () -> service.userById(0),
      "Usuário não encontrado."
    );

    assertTrue(thrown.getMessage().contains("Usuário não encontrado."));
  }

  @Test
  @DisplayName("12 - Verifica lançamento de exceção inesperada.")
  void findUserByIdUnexpectedException() {
    User newUser = new User("newUser", "newUserEmail", "newUserPassword");

    when(userRepository.findById(newUser.getId())).thenThrow(new CustomUnexpectedException("Erro inesperado ao tentar buscar determinado usuário."));

    CustomUnexpectedException thrown = assertThrows(
      CustomUnexpectedException.class,
      () -> service.userById(newUser.getId()),
      "Erro inesperado ao tentar buscar determinado usuário."
    );

    assertTrue(thrown.getMessage().contains("Erro inesperado ao tentar buscar determinado usuário."));
  }

  @Test
  @DisplayName("13 - Verifica atualização de determinado usuário.")
  void updateUser() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("newUserPassword");

    User newUser = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());

    when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));

    User tobeUpdated = service.updateUser(newUser.getId(), userDto);

    assertTrue(tobeUpdated.getEmail().contains(userDto.getEmail()));
  }

  @Test
  @DisplayName("14 - Verifica se a exceção é lançada corretamente em caso usuário não exista.")
  void exceptionUserNotFoundToUpdate() {
    UserDto userDto = new UserDto();

    userDto.setUsername("");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("newUserPassword");

    CustomNotFoundException thrown = assertThrows(
      CustomNotFoundException.class,
      () -> service.updateUser(1, userDto),
      "Usuário não encontrado."
    );

    assertTrue(thrown.getMessage().contains("Usuário não encontrado."));
  }

  @Test
  @DisplayName("15 - Verifica se a exceção é lançada corretamente em caso de username não informado.")
  void exceptionUsernameIsEmptyToUpdate() {
    UserDto userDto = new UserDto();

    userDto.setUsername("");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("newUserPassword");

    User newUser = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());

    when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));

    CustomBadRequestException thrown = assertThrows(
      CustomBadRequestException.class,
      () -> service.updateUser(newUser.getId(), userDto),
      "Informe o nome de usuário."
    );

    assertTrue(thrown.getMessage().contains("Informe o nome de usuário."));
  }

  @Test
  @DisplayName("16 - Verifica se a exceção é lançada corretamente em caso de email não informado.")
  void exceptionEmailIsEmptyToUpdate() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("");
    userDto.setPassword("newUserPassword");

    User newUser = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());

    when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));

    CustomBadRequestException thrown = assertThrows(
      CustomBadRequestException.class,
      () -> service.updateUser(newUser.getId(), userDto),
      "Informe o email."
    );

    assertTrue(thrown.getMessage().contains("Informe o email."));
  }

  @Test
  @DisplayName("17 - Verifica se a exceção é lançada corretamente caso o email já exista no DB.")
  public void exceptionEmailExistsToUpdate() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("newUserPassword");

    User newUser = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());

    when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));
    when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

    CustomBadRequestException thrown = assertThrows(
      CustomBadRequestException.class,
      () -> service.updateUser(newUser.getId(), userDto),
      "Existe um usuário cadastrado com este email."
    );

    assertTrue(thrown.getMessage().contains("Existe um usuário cadastrado com este email."));
  }

  @Test
  @DisplayName("18 - Verifica se a exceção é lançada corretamente em caso de password não informado.")
  void exceptionPasswordIsEmptyToUpdate() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("");

    User newUser = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());

    when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));

    CustomBadRequestException thrown = assertThrows(
      CustomBadRequestException.class,
      () -> service.updateUser(newUser.getId(), userDto),
      "Informe o password."
    );

    assertTrue(thrown.getMessage().contains("Informe o password."));
  }

  @Test
  @DisplayName("19 - Verifica lançamento de exceção inesperada.")
  void updateUnexpectedException() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("newUserPassword");

    User newUser = new User("newUser", "newUserEmail", "newUserPassword");

    when(userRepository.findById(newUser.getId())).thenThrow(new CustomUnexpectedException("Erro inesperado ao tentar atualizar determinado usuário."));

    CustomUnexpectedException thrown = assertThrows(
      CustomUnexpectedException.class,
      () -> service.updateUser(newUser.getId(), userDto),
      "Erro inesperado ao tentar atualizar determinado usuário."
    );

    assertTrue(thrown.getMessage().contains("Erro inesperado ao tentar atualizar determinado usuário."));
  }

  @Test
  @DisplayName("20 - Verifica se a remoção de determinado usuário acontece com sucesso.")
  void removeUser() {
    UserDto userDto = new UserDto();

    userDto.setUsername("newUser");
    userDto.setEmail("newUserEmail");
    userDto.setPassword("newUserPassword");

    User newUser = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());

    when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));

    String removeResponse = service.removeUser(newUser.getId());

    assertEquals("Usuário newUser removido.", removeResponse);
  }

  @Test
  @DisplayName("21 - Verifica tentativa de remoção de um usuário inexistente.")
  void removeUserNotFound() {
    User newUser = new User("newUser", "newUserEmail", "newUserPassword");

    when(userRepository.findById(newUser.getId())).thenReturn(Optional.empty());

    CustomNotFoundException thrown = assertThrows(
      CustomNotFoundException.class,
      () -> service.removeUser(newUser.getId()),
      "Usuário não encontrado."
    );

    assertTrue(thrown.getMessage().contains("Usuário não encontrado."));
  }

  @Test
  @DisplayName("22 - Verifica lançamento de exceção inesperada.")
  void removeUnexpectedException() {
    User newUser = new User("newUser", "newUserEmail", "newUserPassword");

    when(userRepository.findById(newUser.getId())).thenThrow(new CustomUnexpectedException("Erro inesperado ao tentar remover determinado usuário."));

    CustomUnexpectedException thrown = assertThrows(
      CustomUnexpectedException.class,
      () -> service.removeUser(newUser.getId()),
      "Erro inesperado ao tentar remover determinado usuário."
    );

    assertTrue(thrown.getMessage().contains("Erro inesperado ao tentar remover determinado usuário."));
  }
}
