package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UserStorageDB implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public List<User> getUsers() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User add(User user) {
        String sql = "insert into USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        userCheck(user.getId());
        String sql = "update USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User getUserById(int id) {
        String sql = "select * from USERS where USER_ID =?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        User user = new User();
        if (rs.next()) {
            user.setId(id);
            user.setEmail(rs.getString("EMAIL"));
            user.setLogin(rs.getString("LOGIN"));
            user.setName(rs.getString("NAME"));
            user.setBirthday(Objects.requireNonNull(rs.getDate("BIRTHDAY")).toLocalDate());
        } else {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    private void userCheck(int userId) {
        String checkUser = "select * from USERS where USER_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(checkUser, userId);
        if (!rs.next()) {
            throw new NotFoundException("User not found");
        }
    }

}
