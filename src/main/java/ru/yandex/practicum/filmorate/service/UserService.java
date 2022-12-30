package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public List<User> getAll() {
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
        return friendStorage.getAllFriends(id);
    }

    public User addFriend(int userId, int friendId) {
        return friendStorage.addFriend(userId, friendId);
    }

    public List<User> removeFriend(int userId, int friendId) {
        return friendStorage.removeFriend(userId, friendId);
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        return friendStorage.getCommonFriends(userId, friendId);
    }

}
