package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeStorageDB implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int filmId, int userId) {
        filmCheck(filmId);
        userCheck(userId);
        String sql1 = "insert into FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        String sql2 = "UPDATE FILMS SET RATE = RATE + 1 WHERE FILM_ID = ?";
        jdbcTemplate.update(sql1, filmId, userId);
        jdbcTemplate.update(sql2, filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        filmCheck(filmId);
        userCheck(userId);
        String sql1 = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        String sql2 = "UPDATE FILMS SET RATE = RATE - 1 WHERE FILM_ID = ?";
        jdbcTemplate.update(sql1, filmId, userId);
        jdbcTemplate.update(sql2, filmId);
    }

    @Override
    public List<Film> getPopular(int count) {
        String sql = "SELECT * FROM FILMS AS F LEFT OUTER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRE AS FG ON F.FILM_ID = FG.FILM " +
                "LEFT OUTER JOIN GENRE AS G ON FG.GENRE = G.GENRE_ID ORDER BY RATE DESC LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{count}, new FilmMapper());
    }

    private void filmCheck(int filmId) {
        String checkFilm = "select * from FILMS where FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(checkFilm, filmId);
        if (!rs.next()) {
            throw new NotFoundException("Film not found");
        }
    }

    private void userCheck(int userId) {
        String checkUser = "select * from USERS where USER_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(checkUser, userId);
        if (!rs.next()) {
            throw new NotFoundException("User not found");
        }
    }

}
