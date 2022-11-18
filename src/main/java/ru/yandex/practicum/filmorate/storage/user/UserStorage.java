package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

@Component
public interface UserStorage {

    Map<Integer, User> getUsers();

    void add(User user);

    void update(User user);
}
