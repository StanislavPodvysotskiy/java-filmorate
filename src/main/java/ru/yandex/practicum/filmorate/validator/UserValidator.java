package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Slf4j
public class UserValidator {

    public static void validate(User user) {
        Pattern emailPattern = Pattern.compile(
                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
                        "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
        );
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            log.info("Поле e-mail пустое");
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!emailPattern.matcher(user.getEmail()).matches()) {
            log.info("Неверное поле e-mail");
            throw new ValidationException("Неверный формат e-mail адреса");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Поле login пустое или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Полю name было присвоено значение поля login");
            user.setName(user.getLogin());
        }
        if (LocalDate.parse(user.getBirthday(), User.USER_DATE_TIME_FORMATTER).isAfter(LocalDate.now())) {
            log.info("Неверное поле birthday");
            throw new ValidationException("Дата рождения не может быть позже текущей даты");
        }
    }
}
