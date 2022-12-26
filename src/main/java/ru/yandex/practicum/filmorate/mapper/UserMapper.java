package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("USER_ID"));
        user.setName(resultSet.getString("NAME"));
        user.setEmail(resultSet.getString("EMAIL"));
        user.setLogin(resultSet.getString("LOGIN"));
        user.setBirthday(resultSet.getDate("BIRTHDAY").toLocalDate());
        return user;
    }
}
