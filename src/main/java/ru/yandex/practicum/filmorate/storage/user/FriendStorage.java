package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@Getter
@Setter
@Component
public class FriendStorage {

    public Map<Integer, LinkedHashSet<User>> friendsMap = new HashMap<>();
}
