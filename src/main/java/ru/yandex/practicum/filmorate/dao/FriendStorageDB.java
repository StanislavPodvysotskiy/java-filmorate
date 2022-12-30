package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendStorageDB implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public List<User> getAllFriends(int id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)";
        return jdbcTemplate.query(sql, new Object[]{id}, userMapper);
    }

    @Override
    public User addFriend(int userId, int friendId) {
        userCheck(userId);
        userCheck(friendId);
        String sql = "INSERT INTO FRIENDS (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
        String getUser = "select * from USERS where USER_ID = ?";
        return jdbcTemplate.queryForObject(getUser, new Object[]{userId}, userMapper);
    }

    @Override
    public List<User> removeFriend(int userId, int friendId) {
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
        String userFriends = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)";
        return jdbcTemplate.query(userFriends, new Object[]{userId}, userMapper);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID IN (SELECT DISTINCT F1.FRIEND_ID " +
                "FROM (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?) AS F1 " +
                "INNER JOIN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?) AS F2 ON F1.FRIEND_ID = F2.FRIEND_ID)";
        return jdbcTemplate.query(sql, new Object[]{userId, friendId}, userMapper);
    }

    private void userCheck(int userId) {
        String checkUser = "select * from USERS where USER_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(checkUser, userId);
        if (!rs.next()) {
            throw new NotFoundException("User not found");
        }
    }

}
