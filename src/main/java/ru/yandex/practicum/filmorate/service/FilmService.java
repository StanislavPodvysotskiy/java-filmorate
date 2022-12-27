package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public Collection<Film> getAll() {
        return filmStorage.getFilms();
    }

    public Film add(Film film) {
        FilmValidator.validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        FilmValidator.validate(film);
        return filmStorage.update(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(int filmId, int userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        likeStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return likeStorage.getPopular(count);
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Mpa getMpaById(int id) {
        return mpaStorage.getMpaById(id);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

}