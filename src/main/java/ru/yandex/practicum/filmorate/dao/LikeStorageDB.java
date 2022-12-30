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
    private final FilmMapper filmMapper;

    @Override
    public void addLike(int filmId, int userId) {
        filmCheck(filmId);
        userCheck(userId);
        String sql = "insert into FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        filmCheck(filmId);
        userCheck(userId);
        String sql = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public List<Film> getPopular(int count) {
        String sql = "SELECT * FROM FILMS AS F LEFT OUTER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRE AS FG ON F.FILM_ID = FG.FILM " +
                "LEFT OUTER JOIN GENRE AS G ON FG.GENRE = G.GENRE_ID ORDER BY RATE DESC LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{count}, filmMapper);
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

    private void updateRate(long filmId) {
        String sqlQuery = "update FILMS f set rate = (select count(l.user_id) from FILM_LIKES l " +
                "where l.film_id = f.film_id)  where film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

}
