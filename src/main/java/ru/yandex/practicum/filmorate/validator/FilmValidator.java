package ru.yandex.practicum.filmorate.validator;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {

    public static void validate(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Слишком длинное описание");
        }
        if (LocalDate.parse(film.getReleaseDate(), Film.FILM_DATE_TIME_FORMATTER)
                .isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
