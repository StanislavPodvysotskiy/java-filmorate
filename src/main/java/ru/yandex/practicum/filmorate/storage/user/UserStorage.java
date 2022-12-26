package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public interface UserStorage {

    Collection<User> getUsers();

    User getUserById(int id);

    User add(User user);

    User update(User user);

    List<User> getCommonFriends(int userId, int friendId);

    List<User> getAllFriends(int id);

    User addFriend(int userId, int friendId);

    List<User> removeFriend(int userId, int friendId);
}
