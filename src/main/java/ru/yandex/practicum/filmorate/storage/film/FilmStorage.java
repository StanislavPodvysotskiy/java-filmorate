package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@Component
public interface FilmStorage {

    Map<Integer, Film> getFilms();

    void add(Film film);

    void update(Film film);

}
