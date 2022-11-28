package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    FilmStorage filmStorage;

    UserStorage userStorage;
    LikeStorage likeStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public Collection<Film> getAll() {
        return filmStorage.getFilms().values();
    }

    public void add(Film film) {
        filmStorage.add(film);
    }

    public void update(Film film) {
        filmStorage.update(film);
    }

    public Film getFilmById(int id) {
        if (filmStorage.getFilms().get(id) == null) {
            throw new NotFoundException("Film not found");
        }
        return filmStorage.getFilms().get(id);
    }

    public void addLike(int filmId, int userId) {
        Film film = makeFilm(filmId);
        User user = makeUser(userId);
        if (!likeStorage.getLikesMap().containsKey(film)) {
            LinkedHashSet<User> set = new LinkedHashSet<>();
            likeStorage.getLikesMap().put(film, set);
        }
        likeStorage.getLikesMap().get(film).add(user);
        film.setRate(film.getRate() + 1);
    }

    public void removeLike(int filmId, int userId) {
        Film film = makeFilm(filmId);
        User user = makeUser(userId);
        if (!likeStorage.getLikesMap().containsKey(film)) {
            throw new NotFoundException("Film not found");
        }
        likeStorage.getLikesMap().get(film).remove(user);
        film.setRate(film.getRate() - 1);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getRate).reversed()).limit(count).collect(Collectors.toList());
    }

    private Film makeFilm(int filmId) {
        Film film = filmStorage.getFilms().get(filmId);
        if (film == null) {
            throw new NotFoundException("Film not found");
        }
        return film;
    }

    private User makeUser(int userId) {
        User user = userStorage.getUsers().get(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

}
