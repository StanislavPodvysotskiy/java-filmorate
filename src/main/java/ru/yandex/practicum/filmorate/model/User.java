package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Data
public class User {

    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public static final DateTimeFormatter USER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String getBirthday() {
        return birthday.format(USER_DATE_TIME_FORMATTER);
    }
}
