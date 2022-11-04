package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @Test
    public void shouldAddNewUser() {
        User user = new User(1, "mail@yandex.ru", "login", "name",
                LocalDate.of(1995, 12, 28));
        userController.add(user);
        Collection<User> users = userController.getAll();
        assertEquals(1, users.size());
    }

    @Test
    public void shouldBeExceptionWhenSameIdAdd() {
        User user1 = new User(1, "mail@yandex.ru", "login1", "name1",
                LocalDate.of(1995, 12, 28));
        User user2 = new User(1, "mail@mail.ru", "login2", "name2",
                LocalDate.of(1985, 12, 28));
        userController.add(user1);
        Exception exception =  assertThrows(RuntimeException.class, () -> {
            userController.add(user2);
        });
        assertEquals("Пользователь с таким ID уже есть", exception.getMessage());
    }

    @Test
    public void shouldBeExceptionWhenEmailIsBlank() {
        User user = new User(1, " ", "login", "name",
                LocalDate.of(1995, 12, 28));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userController.add(user);
        });
        assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    public void shouldBeExceptionWhenEmailPatterNotMatch() {
        User user = new User(1, "mail.yandex.ru@", "login", "name",
                LocalDate.of(1995, 12, 28));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userController.add(user);
        });
        assertEquals("Неверный формат e-mail адреса", exception.getMessage());
    }

    @Test
    public void shouldBeExceptionWhenLoginHaveSpaces() {
        User user = new User(1, "mail@yandex.ru", "log in", "name",
                LocalDate.of(1995, 12, 28));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userController.add(user);
        });
        assertEquals("Логин не может быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    public void shouldReplaceNameByLoginWhenItsEmpty() {
        User user = new User(1, "mail@yandex.ru", "login", "",
                LocalDate.of(1995, 12, 28));
        userController.add(user);
        List<User> users = new ArrayList<>(userController.getAll());
        assertEquals("login", users.get(0).getName());
    }

    @Test
    public void shouldBeExceptionWhenBirthdayAfterLocalDateNow() {
        User user = new User(1, "mail@yandex.ru", "login", "name",
                LocalDate.of(2995, 12, 28));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userController.add(user);
        });
        assertEquals("Дата рождения не может быть позже текущей даты", exception.getMessage());
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User(1, "mail@yandex.ru", "login", "name",
                LocalDate.of(1995, 12, 28));
        userController.add(user);
        User updateUser = new User(1, "mail@mail.ru", "updateLogin", "updateName",
                LocalDate.of(1985, 12, 28));
        userController.update(updateUser);
        List<User> users = new ArrayList<>(userController.getAll());
        assertEquals("mail@mail.ru", users.get(0).getEmail());
    }

    @Test
    public void shouldBeExceptionWhenIdNotFound() {
        User user = new User(1, "mail@yandex.ru", "login", "name",
                LocalDate.of(1995, 12, 28));
        userController.add(user);
        User updateUser = new User(2, "mail@mail.ru", "updateLogin", "updateName",
                LocalDate.of(1985, 12, 28));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userController.update(updateUser);
        });
        assertEquals("Пользователь с таким ID не найден", exception.getMessage());
    }

}
