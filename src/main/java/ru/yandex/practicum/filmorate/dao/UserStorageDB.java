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
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", user.getId());
        if (!rs.next()) {
            throw new NotFoundException("User not found");
        }
        String sql = "update USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        String returnUser = "select * from USERS where EMAIL = ?";
        return jdbcTemplate.query(returnUser, new Object[]{user.getEmail()}, new UserMapper()).get(0);
    }

    @Override
    public User getUserById(int id) {
        String sql = "select * from USERS where USER_ID =?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (!rs.next()) {
            throw new NotFoundException("User not found");
        }
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserMapper());
    }

    @Override
    public List<User> getAllFriends(int id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)";
        return jdbcTemplate.query(sql, new Object[]{id}, new UserMapper());
    }

    @Override
    public User addFriend(int userId, int friendId) {
        String checkUser = "select * from USERS where USER_ID =?";
        SqlRowSet rs1 = jdbcTemplate.queryForRowSet(checkUser, userId);
        if (!rs1.next()) {
            throw new NotFoundException("User not found");
        }
        String checkFriend = "select * from USERS where USER_ID =?";
        SqlRowSet rs2 = jdbcTemplate.queryForRowSet(checkFriend, friendId);
        if (!rs2.next()) {
            throw new NotFoundException("User not found");
        }
        String sql = "INSERT INTO FRIENDS (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
        return jdbcTemplate.query(checkUser, new Object[]{userId}, new UserMapper()).get(0);
    }

    @Override
    public List<User> removeFriend(int userId, int friendId) {
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
        String userFriends = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)";
        return jdbcTemplate.query(userFriends, new Object[]{userId}, new UserMapper());
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID IN (SELECT DISTINCT F1.FRIEND_ID " +
                "FROM (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?) AS F1 " +
                "INNER JOIN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?) AS F2 ON F1.FRIEND_ID = F2.FRIEND_ID)";
        return jdbcTemplate.query(sql, new Object[]{userId, friendId}, new UserMapper());
    }

}
