package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        log.info("Получен запрос всех пользователей");
        return userService.getAll();
    }

    @PostMapping
    public User add(@RequestBody User user) {
        log.info("Получена запрос на добавление пользователя");
        return userService.add(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получена запрос на обновление пользователя");
        userService.update(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        log.info("Запрошен пользователь с id {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь с id {} добавляет в друзья пользователя с id {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Collection<User> removeFried(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь с id {} удаляет из друзей пользователя с id {}", id, friendId);
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable int id) {
        log.info("Заброшены все друзья пользоватенля с ID {}", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Запрошены общие друзья пользователей с id {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

}
