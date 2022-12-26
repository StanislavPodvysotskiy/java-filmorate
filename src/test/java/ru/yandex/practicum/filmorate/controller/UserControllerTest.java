package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    @Test
    public void shouldBeExceptionWhenEmailIsBlank() {
        User user = new User(1, " ", "login", "name",
                LocalDate.of(1995, 12, 28));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            UserValidator.validate(user);
        });
        assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    public void shouldBeExceptionWhenEmailPatterNotMatch() {
        User user = new User(1, "mail.yandex.ru@", "login", "name",
                LocalDate.of(1995, 12, 28));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            UserValidator.validate(user);
        });
        assertEquals("Неверный формат e-mail адреса", exception.getMessage());
    }

    @Test
    public void shouldBeExceptionWhenLoginHaveSpaces() {
        User user = new User(1, "mail@yandex.ru", "log in", "name",
                LocalDate.of(1995, 12, 28));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            UserValidator.validate(user);
        });
        assertEquals("Логин не может быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    public void shouldReplaceNameByLoginWhenItsEmpty() {
        User user = new User(1, "mail@yandex.ru", "login", "",
                LocalDate.of(1995, 12, 28));
        UserValidator.validate(user);
        assertEquals("login", user.getName());
    }

    @Test
    public void shouldBeExceptionWhenBirthdayAfterLocalDateNow() {
        User user = new User(1, "mail@yandex.ru", "login", "name",
                LocalDate.of(2995, 12, 28));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            UserValidator.validate(user);
        });
        assertEquals("Дата рождения не может быть позже текущей даты", exception.getMessage());
    }

}
