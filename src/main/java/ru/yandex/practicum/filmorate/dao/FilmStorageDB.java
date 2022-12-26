package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Qualifier
@Component
@Primary
public class FilmStorageDB implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmStorageDB(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getFilms() {
        String sql = "SELECT * FROM FILMS AS F LEFT OUTER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRE AS FG ON F.FILM_ID = FG.FILM " +
                "LEFT OUTER JOIN GENRE AS G ON FG.GENRE = G.GENRE_ID";
        return jdbcTemplate.query(sql, new FilmsMapper());
    }

    @Override
    public Film getFilmById(int id) {
        filmCheck(id);
        String sql = "SELECT * FROM FILMS AS F LEFT OUTER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRE AS FG ON F.FILM_ID = FG.FILM " +
                "LEFT OUTER JOIN GENRE AS G ON FG.GENRE = G.GENRE_ID WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new FilmMapper());
    }

    @Override
    public Film add(Film film) {
        if (film.getId() != null) {
            filmCheck(film.getId());
        }
        String sql = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId());
        int id = jdbcTemplate.query("SELECT * FROM FILMS WHERE NAME = ?", new Object[]{film.getName()},
                new AddFilmMapper()).get(0).getId();
        updateFilmGenre(film, id);
        String filmRequest = "SELECT * FROM FILMS AS F LEFT OUTER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRE AS FG ON F.FILM_ID = FG.FILM " +
                "LEFT OUTER JOIN GENRE AS G ON FG.GENRE = G.GENRE_ID WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(filmRequest, new Object[]{id}, new FilmMapper());
    }

    @Override
    public Film update(Film film) {
        filmCheck(film.getId());
        String sql = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "RATE = ?, MPA_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRate(), film.getMpa().getId(), film.getId());
        updateFilmGenre(film, film.getId());
        String filmRequest = "SELECT * FROM FILMS AS F LEFT OUTER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRE AS FG ON F.FILM_ID = FG.FILM " +
                "LEFT OUTER JOIN GENRE AS G ON FG.GENRE = G.GENRE_ID WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(filmRequest, new Object[]{film.getId()}, new FilmMapper());
    }

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

    private void updateFilmGenre (Film film, int id) {
        if (film.getGenres().isEmpty()) {
            String removeFilmGenre = "DELETE FROM FILM_GENRE WHERE FILM = ?";
            jdbcTemplate.update(removeFilmGenre, film.getId());
        } else {
            if (film.getId() != null) {
                String removeFilmGenre = "DELETE FROM FILM_GENRE WHERE FILM = ?";
                jdbcTemplate.update(removeFilmGenre, film.getId());
            }
            String addFilmGenre = "INSERT INTO FILM_GENRE (FILM, GENRE) VALUES (?, ?)";
            for (int i = 0; i < film.getGenres().size(); i++) {
                jdbcTemplate.update(addFilmGenre, id, film.getGenres().get(i).getId());
            }
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, new MpaMapper());
    }

    @Override
    public Mpa getMpaById(int id) {
        String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (!rs.next()) {
            throw new NotFoundException("MPA not found");
        }
        return jdbcTemplate.query(sql, new Object[]{id}, new MpaMapper()).get(0);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (!rs.next()) {
            throw new NotFoundException("GENRE not found");
        }
        return jdbcTemplate.query(sql, new Object[]{id}, new GenreMapper()).get(0);
    }

}
