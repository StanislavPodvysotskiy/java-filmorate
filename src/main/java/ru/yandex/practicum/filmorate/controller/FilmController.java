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

    private final List<Film> films = new ArrayList<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films;
    }

    @PostMapping
    public Film add(@RequestBody Film film) {
        if (films.contains(film)) {
            throw new ValidationException("Такой фильм уже есть");
        }
        FilmValidator.validate(film);
        films.add(film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (films.contains(film)) {
            FilmValidator.validate(film);
            films.remove(film);
        }
        films.add(film);
        return film;
    }

}
