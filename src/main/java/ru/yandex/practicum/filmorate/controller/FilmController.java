package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.constraints.Positive;
import java.util.*;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Запрошены все фильмы");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable int id) {
        log.info("Запрошен фильм с id {}", id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film add(@RequestBody Film film) {
        log.info("Получена запрос на добавление фильма");
        log.info(film.getDescription());
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получена запрос на обновление фильма");
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Фильму с id {} добавлен лайк от пользователя с id {}",id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("У фильма с id {} забирает лайк пользователь с id {}",id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam (value = "count", defaultValue = "10")@Positive Integer count) {
        log.info("Запрошено {} самых популярных фильма(ов)", count);
        return filmService.getPopular(count);
    }

}
