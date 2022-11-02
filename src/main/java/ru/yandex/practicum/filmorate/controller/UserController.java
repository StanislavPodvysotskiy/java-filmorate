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
        if(users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким e-mail адресом уже есть");
        }
        return validateId(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return validateId(user);
    }

    private User validateId(@RequestBody User user) {
        if (user.getId() == -1) {
            while (users.containsKey(id)) {
                id++;
            }
            user.setId(id++);
        }
        UserValidator.validate(user);
        users.put(user.getId(), user);
        return user;
    }
}
