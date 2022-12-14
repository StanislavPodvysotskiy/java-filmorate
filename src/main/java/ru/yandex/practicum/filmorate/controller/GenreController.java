package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(value = "/genres")
@RequiredArgsConstructor
public class GenreController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Genre> getAll() {
        log.info("Запрошены все жанры");
        return filmService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable int id) {
        log.info("Запрошен жанр с id {}", id);
        return filmService.getGenreById(id);
    }

}