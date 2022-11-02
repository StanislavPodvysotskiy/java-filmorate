package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {

    public static void validate(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.info("Поле name пустое");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.info("Поле description болше 200 символов");
            throw new ValidationException("Описание больше 200 символов");
        }
        if (LocalDate.parse(film.getReleaseDate(), Film.FILM_DATE_TIME_FORMATTER)
                .isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Неверное значение поля releaseDate");
            throw new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.info("Неверное значение поля duration");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
