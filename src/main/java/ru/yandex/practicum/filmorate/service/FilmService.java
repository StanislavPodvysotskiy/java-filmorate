package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    @Qualifier("filmDaoImpl")
    private final FilmDao filmDao;
    @Qualifier("userDaoImpl")
    private final UserDao userDao;

    public Film addFilm(Film film) {
        Film createdFilm = filmDao.createFilm(film);
        Long autoGeneratedId = createdFilm.getId();
        log.debug("Фильм сохранен в таблице films, присвоен id={}", autoGeneratedId);
        film.setId(autoGeneratedId);
        saveGenresForFilm(film);
        log.debug("Жанры для фильма с id={} сохранены в таблице film_genres", autoGeneratedId);
        saveDirectorsForFilm(film);
        log.debug("Режиссеры для фильма с id={} сохранены в таблице film_directors", autoGeneratedId);
        return filmDao.getFilmById(autoGeneratedId);
    }

    public Film getFilmById(Long id) {
        Film filmFromDatabase = filmDao.getFilmById(id);
        log.debug("Из таблицы films считан фильм с id={}: {}", id, filmFromDatabase);
        return filmFromDatabase;
    }

    public List<Film> getAllFilms() {
        List<Film> allFilms = filmDao.getAllFilms();
        log.debug("Из таблицы films считаны все фильмы: {}", allFilms);
        return filmDao.getAllFilms();
    }

    public Film updateFilm(Film film) {
        Long filmId = film.getId();
        filmDao.updateFilm(film);
        log.debug("Фильм с id={} обновлен в таблице films", filmId);
        filmDao.deleteGenresForFilm(filmId);
        saveGenresForFilm(film);
        log.debug("Жанры для фильма с id={} обновлены в таблице film_genres", filmId);
        filmDao.deleteDirectorsForFilm(filmId);
        saveDirectorsForFilm(film);
        log.debug("Режиссеры для фильма с id={} обновлены в таблице film_directors", filmId);
        return filmDao.getFilmById(filmId);
    }

    private void saveGenresForFilm(Film film) {
        if (film.getGenres() != null) {
            List<Integer> genreIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .collect(Collectors.toList());
            filmDao.createGenresForFilm(film.getId(), genreIds);
        }
    }

    private void saveDirectorsForFilm(Film film) {
        if (film.getDirectors() != null) {
            List<Long> directorIds = film.getDirectors().stream()
                    .map(Director::getId)
                    .collect(Collectors.toList());
            filmDao.createDirectorsForFilm(film.getId(), directorIds);
        }
    }

    public void addLikeToFilm(Long filmId, Long userId) {
        userDao.getUserById(userId);
        filmDao.saveLike(filmId, userId);
        log.debug("Лайк пользователя с id={} добавлен для фильма с id={}, добавлена запись в таблицу likes", userId, filmId);
    }

    public void removeLikeFromFilm(Long filmId, Long userId) {
        userDao.getUserById(userId);
        filmDao.deleteLike(filmId, userId);
        log.debug("Лайк пользователя с id={} удален для фильма с id={},  удалена запись в таблице  likes", userId, filmId);
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> topFilms = filmDao.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getUserIdsWhoLiked().size() - film1.getUserIdsWhoLiked().size())
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Топ фильмов с ограничением в {} шт. получен", count);
        return topFilms;
    }

    public List<Film> getFilmsByDirector(Long directorId, String sortBy) {
        List<Film> filmsForDirector = filmDao.getFilmsByDirector(directorId);
        if (filmsForDirector.isEmpty()) {
            throw new NotFoundException("В базе данных отсутствуют фильмы данного режиссера");
        } else if (sortBy.equals("year")) {
            log.debug("Получен список фильмов режиссера с id={} c сортировкой по году", directorId);
            return filmsForDirector.stream()
                    .sorted(Comparator.comparing(Film::getReleaseDate))
                    .collect(Collectors.toList());
        } else if (sortBy.equals("likes")) {
            log.debug("Получен список фильмов режиссера с id={} с сортировкой по лайкам", directorId);
            return filmsForDirector.stream()
                    .sorted(Comparator.comparingInt(film -> film.getUserIdsWhoLiked().size()))
                    .collect(Collectors.toList());
        } else {
            log.debug("Получен список фильмов режиссера с id={}", directorId);
            return filmsForDirector;
        }
    }

    public void delete(long id) {
        filmDao.delete(id);
    }

}