package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    public Map<Integer, User> getUsers() {
        return users;
    }

    public void add(User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким ID уже есть");
        }
        UserValidator.validate(user);
        if (user.getId() == null) {
            user.setId(id++);
        }
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
    }

    public void update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким ID не найден");
        }
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.info("Пользователь обновлен");
    }
}
