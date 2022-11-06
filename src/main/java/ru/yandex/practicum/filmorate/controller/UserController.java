package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User add(@RequestBody User user) {
        log.info("Получена запрос на добавление пользователя");
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким ID уже есть");
        }
        UserValidator.validate(user);
        if (user.getId() == null) {
            user.setId(id);
            id++;
        }
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получена запрос на обновление пользователя");
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким ID не найден");
        }
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.info("Пользователь обновлен");
        return user;
    }

}
