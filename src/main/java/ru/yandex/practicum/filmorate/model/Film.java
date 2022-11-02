package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class Film {

    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public static final DateTimeFormatter FILM_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String getReleaseDate() {
        return releaseDate.format(FILM_DATE_TIME_FORMATTER);
    }

}
