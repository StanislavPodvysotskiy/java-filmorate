package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAll() {
        return userStorage.getUsers();
    }

    public User add(User user) {
        UserValidator.validate(user);
        return userStorage.add(user);
    }

    public void update(User user) {
        UserValidator.validate(user);
        userStorage.update(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public List<User> getAllFriends(int id) {
        return userStorage.getAllFriends(id);
    }

    public User addFriend(int userId, int friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    public List<User> removeFriend(int userId, int friendId) {
        return userStorage.removeFriend(userId, friendId);
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

}
