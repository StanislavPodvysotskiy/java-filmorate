package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public interface FilmStorage {

    Collection<Film> getFilms();

    Film getFilmById(int id);

    Film add(Film film);

    Film update(Film film);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getPopular(int count);

    List<Mpa> getAllMpa();

    Mpa getMpaById(int id);

    List<Genre> getAllGenres();

    Genre getGenreById(int id);

}
