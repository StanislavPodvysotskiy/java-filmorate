package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Qualifier
@Component
@Primary
public class UserStorageDB implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserStorageDB(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getUsers() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User add(User user) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", user.getId());
        if (rs.next()) {
            throw new NotFoundException("User already exist");
        }
        String sql = "insert into USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        String returnUser = "select * from USERS where EMAIL = ?";
        return jdbcTemplate.query(returnUser, new Object[]{user.getEmail()}, new UserMapper()).get(0);
    }

    @Override
    public User update(User user) {
        userCheck(user.getId());
        String sql = "update USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        String returnUser = "select * from USERS where EMAIL = ?";
        return jdbcTemplate.query(returnUser, new Object[]{user.getEmail()}, new UserMapper()).get(0);
    }

    @Override
    public User getUserById(int id) {
        userCheck(id);
        String sql = "select * from USERS where USER_ID =?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserMapper());
    }

    private void userCheck(int userId) {
        String checkUser = "select * from USERS where USER_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(checkUser, userId);
        if (!rs.next()) {
            throw new NotFoundException("User not found");
        }
    }

}
