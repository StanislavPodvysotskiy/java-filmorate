package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public Map<Integer, User> getAll() {
        return userStorage.getUsers();
    }

    public void add(User user) {
        userStorage.add(user);
    }

    public void update(User user) {
        userStorage.update(user);
    }

    public User getUserById(int id) {
        if (userStorage.getUsers().get(id) == null) {
            throw new NotFoundException("User not found");
        }
        return userStorage.getUsers().get(id);
    }

    public Set<User> getAllFriends(int id) {
        if (userStorage.getUsers().get(id) == null) {
            throw new NotFoundException("User not found");
        }
        return friendStorage.getFriendsMap().get(id);
    }

    public User addFried(int userId, int friendId) {
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        if (user == null || friend == null || userId == friendId) {
            throw new NotFoundException("User not found");
        }
        if (friendStorage.getFriendsMap().containsKey(userId)) {
            friendStorage.getFriendsMap().get(userId).add(friend);
        } else {
            LinkedHashSet<User> setUser = new LinkedHashSet<>();
            setUser.add(friend);
            friendStorage.getFriendsMap().put(userId, setUser);
        }
        if (friendStorage.getFriendsMap().containsKey(friendId)) {
            friendStorage.getFriendsMap().get(friendId).add(user);
        } else {
            LinkedHashSet<User> setFriend = new LinkedHashSet<>();
            setFriend.add(user);
            friendStorage.getFriendsMap().put(friendId, setFriend);
        }
        return user;
    }

    public Set<User> removeFried(int userId, int friendId) {
        if (friendStorage.getFriendsMap().get(userId) == null) {
            throw new NotFoundException("User not found");
        }
        friendStorage.getFriendsMap().get(userId).remove(userStorage.getUsers().get(friendId));
        friendStorage.getFriendsMap().get(friendId).remove(userStorage.getUsers().get(userId));
        return friendStorage.getFriendsMap().get(userId);
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        if (userStorage.getUsers().get(userId) == null ||
                userStorage.getUsers().get(friendId) == null || userId == friendId) {
            throw new NotFoundException("User not found");
        }
        if (friendStorage.getFriendsMap().get(userId) == null || friendStorage.getFriendsMap().get(friendId) == null) {
            return new ArrayList<>();
        }
        Set<User> userSet = friendStorage.friendsMap.get(userId);
        Set<User> friendSet = friendStorage.friendsMap.get(friendId);
        return friendSet.stream().filter(userSet::contains).collect(Collectors.toList());
    }

}
