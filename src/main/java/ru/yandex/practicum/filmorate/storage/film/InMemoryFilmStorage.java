package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private int id = 1;

    private final Map<Integer, Film> films = new HashMap<>();

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public void add(Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Такой фильм уже есть");
        }
        FilmValidator.validate(film);
        if (film.getId() == null) {
            film.setId(id++);
        }
        films.put(film.getId(), film);
    }

    public void update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Такого фильма нет");
        }
        films.put(film.getId(), film);
    }

}
