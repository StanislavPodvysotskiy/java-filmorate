package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    List<User> getAllFriends(int id);

    User addFriend(int userId, int friendId);

    List<User> removeFriend(int userId, int friendId);

    List<User> getCommonFriends(int userId, int friendId);

}
