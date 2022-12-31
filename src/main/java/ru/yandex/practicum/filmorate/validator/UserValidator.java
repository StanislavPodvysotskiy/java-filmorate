package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Slf4j
public class UserValidator {

    public static void validate(User user) {
        validateEmail(user);
        validateEmailFormat(user);
        validateLogin(user);
        validateName(user);
        validateBirthday(user);
    }

    private static void validateEmail(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.info("Поле e-mail пустое");
            throw new ValidationException("Электронная почта не может быть пустой");
        }
    }

    public static void validateEmailFormat(User user) {
        Pattern emailPattern = Pattern.compile(
                "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
                        "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\" +
                        "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]" +
                        "(?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                        "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\" +
                        "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        if (!emailPattern.matcher(user.getEmail()).matches()) {
            log.info("Неверное поле e-mail");
            throw new ValidationException("Неверный формат e-mail адреса");
        }
    }

    private static void validateLogin(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Поле login пустое или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        }
    }

    private static void validateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Полю name было присвоено значение поля login");
            user.setName(user.getLogin());
        }
    }

    private static void validateBirthday(User user) {
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Неверное поле birthday");
            throw new ValidationException("Дата рождения не может быть позже текущей даты");
        }
    }

}
