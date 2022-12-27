package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    @Test
    public void shouldBeExceptionWhenNameIsBlank() {
        Film film = new Film(1, " ", "description",
                LocalDate.of(19855, 12, 28), 1, 0);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            FilmValidator.validate(film);;
        });
        String message = exception.getMessage();
        assertEquals("Название не может быть пустым", message);
    }

    @Test
    public void shouldBeExceptionWhenDescriptionLengthMoreThan200() {
        Film film = new Film(1,
                "name",
                "123456789-123456789-123456789-123456789-123456789-123456789-123456789-" +
                        "123456789-123456789-123456789-123456789-123456789-123456789-123456789-" +
                        "123456789-123456789-123456789-123456789-123456789-123456789-1",
                LocalDate.of(1985, 12, 28),
                1, 0);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            FilmValidator.validate(film);;
        });
        String message = exception.getMessage();
        assertEquals("Описание больше 200 символов", message);
    }

    @Test
    public void shouldBeExceptionWhenYearBefore1895() {
        Film film = new Film(1, "name", "description",
                LocalDate.of(1894, 12, 28), 1, 0);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            FilmValidator.validate(film);;
        });
        String message = exception.getMessage();
        assertEquals("Дата релиза — раньше 28 декабря 1895 года", message);
    }

    @Test
    public void shouldBeExceptionWhenDurationNegative() {
        Film film = new Film(1, "name", "description",
                LocalDate.of(1985, 12, 28), -1, 0);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            FilmValidator.validate(film);;
        });
        String message = exception.getMessage();
        assertEquals("Продолжительность фильма должна быть положительной", message);
    }

}
