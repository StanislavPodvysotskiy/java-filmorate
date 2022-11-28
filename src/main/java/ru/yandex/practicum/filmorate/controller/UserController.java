package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получен запрос всех пользователей");
        return userService.getAll().values();
    }

    @PostMapping
    public User add(@RequestBody User user) {
        log.info("Получена запрос на добавление пользователя");
        userService.add(user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получена запрос на обновление пользователя");
        userService.update(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userService.addFried(id, friendId);
        return user;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Collection<User> removeFried(@PathVariable int id, @PathVariable int friendId) {
        return userService.removeFried(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getAllFriends(@PathVariable int id) {
        log.info("Заброшены все друзья пользоватенля с ID {}", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

}
