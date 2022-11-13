package com.blog.api.service;

import com.blog.api.commons.CustomBadRequestException;
import com.blog.api.commons.CustomNotFoundException;
import com.blog.api.dto.UserDto;
import com.blog.api.model.User;
import com.blog.api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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

    UserRepository repository = Mockito.mock(UserRepository.class);

    when(repository.existsByEmail(userDto.getEmail())).thenThrow(new CustomBadRequestException("Existe um usuário cadastrado com este email."));

    CustomBadRequestException thrown = assertThrows(
      CustomBadRequestException.class,
      () -> repository.existsByEmail(userDto.getEmail())
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
  @DisplayName("06 - Verifica se o método listUsers retorna uma lista de User.")
  void listUsers() {

    UserService listMock = Mockito.mock(UserService.class);
    when(listMock.listUsers()).thenReturn(new ArrayList<>());

    List<User> users = listMock.listUsers();

    assertEquals(0, users.size());
  }

  @Test
  @DisplayName("07 - Verifica se é lançada a exceção correta caso nenhum usuário seja encontrado.")
  void exceptionListIsEmpty() {

    CustomNotFoundException thrown = assertThrows(
      CustomNotFoundException.class,
      () -> service.listUsers(),
      "Nenhum usuário foi encontrado."
    );

    assertTrue(thrown.getMessage().contains("Nenhum usuário foi encontrado."));
  }
}
