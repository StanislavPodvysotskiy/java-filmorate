package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class FilmValidator {

    public static void validate(Film film) {
        validateName(film);
        validateDescription(film);
        validateReleaseDate(film);
        validateDuration(film);
        genreDuplicateValidation(film);
    }

    private static void validateName(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Поле name пустое");
            throw new ValidationException("Название не может быть пустым");
        }
    }

    private static void validateDescription(Film film) {
        if (film.getDescription().length() > 200) {
            log.info("Поле description болше 200 символов");
            throw new ValidationException("Описание больше 200 символов");
        }
    }

    private static void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Неверное значение поля releaseDate");
            throw new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
        }
    }

    private static void validateDuration(Film film) {
        if (film.getDuration() < 0) {
            log.info("Неверное значение поля duration");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    private static void genreDuplicateValidation(Film film) {
        if (film.getGenres() != null) {
            Set<Genre> duplicates = new HashSet<>(film.getGenres());
            List<Genre> genres = new ArrayList<>(duplicates);
            film.setGenres(genres);
        }
    }
}
