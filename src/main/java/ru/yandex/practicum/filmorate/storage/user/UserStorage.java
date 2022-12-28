package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Component
public interface UserStorage {

    Collection<User> getUsers();

    User getUserById(int id);

    User add(User user);

    User update(User user);

}
