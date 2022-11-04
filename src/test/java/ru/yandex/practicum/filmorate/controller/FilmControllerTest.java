package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    public void shouldAddNewFilm() {
        Film film = new Film(1,
                "name",
                "123456789-123456789-123456789-123456789-123456789-123456789-123456789-" +
                        "123456789-123456789-123456789-123456789-123456789-123456789-123456789-" +
                        "123456789-123456789-123456789-123456789-123456789-123456789-",
                LocalDate.of(1895, 12, 28),
                0);
        filmController.add(film);
        Collection<Film> films = filmController.getAll();
        assertEquals(1, films.size());
    }

    @Test
    public void shouldBeExceptionWhenSameIdAdd() {
        Film film1 = new Film(1, "name1", "description1",
                LocalDate.of(1985, 12, 28), 1);
        Film film2 = new Film(1, "name2", "description2",
                LocalDate.of(1995, 12, 28), 2);
        filmController.add(film1);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            filmController.add(film2);
        });
        String message = exception.getMessage();
        assertEquals("Такой фильм уже есть", message);
    }

    @Test
    public void shouldBeExceptionWhenNameIsBlank() {
        Film film = new Film(1, " ", "description",
                LocalDate.of(19855, 12, 28), 1);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            filmController.add(film);
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
                1);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            filmController.add(film);
        });
        String message = exception.getMessage();
        assertEquals("Описание больше 200 символов", message);
    }

    @Test
    public void shouldBeExceptionWhenYearBefore1895() {
        Film film = new Film(1, "name", "description",
                LocalDate.of(1894, 12, 28), 1);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            filmController.add(film);
        });
        String message = exception.getMessage();
        assertEquals("Дата релиза — раньше 28 декабря 1895 года", message);
    }

    @Test
    public void shouldBeExceptionWhenDurationNegative() {
        Film film = new Film(1, "name", "description",
                LocalDate.of(1985, 12, 28), -1);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            filmController.add(film);
        });
        String message = exception.getMessage();
        assertEquals("Продолжительность фильма должна быть положительной", message);
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = new Film(1, "name", "description",
                LocalDate.of(1985, 12, 28), 1);
        filmController.add(film);
        Film updateFilm = new Film(1, "updateName", "updateDescription",
                LocalDate.of(1995, 12, 28), 2);
        filmController.update(updateFilm);
        List<Film> films = new ArrayList<>(filmController.getAll());
        assertEquals("updateName", films.get(0).getName());
    }

    @Test
    public void shouldBeExceptionWhenIdNotFound() {
        Film film = new Film(1, "name", "description",
                LocalDate.of(1985, 12, 28), 1);
        filmController.add(film);
        Film updateFilm = new Film(2, "updateName", "updateDescription",
                LocalDate.of(1995, 12, 28), 2);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            filmController.update(updateFilm);
        });
        String message = exception.getMessage();
        assertEquals("Такого фильма нет", message);
    }

}
