package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@Getter
@Setter
@Component
public class LikeStorage {

    public Map<Film, LinkedHashSet<User>> likesMap = new HashMap<>();
}
