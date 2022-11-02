package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/films")
public class FilmController {

    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film add(@RequestBody Film film) {
        log.info("Получена запрос на добавление фильма");
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Такой фильм уже есть");
        }
        FilmValidator.validate(film);
        if (film.getId() == null) {
            film.setId(id);
            id++;
        }
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получена запрос на обновление фильма");
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Такого фильма нет");
        }
        films.put(film.getId(), film);
        log.info("Фильм обновлен");
        return film;
    }

}
