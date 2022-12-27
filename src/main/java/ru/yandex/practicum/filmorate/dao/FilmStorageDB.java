package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Qualifier
@Component
@Primary
@RequiredArgsConstructor
public class FilmStorageDB implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

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

    private void filmCheck(int filmId) {
        String checkFilm = "select * from FILMS where FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(checkFilm, filmId);
        if (!rs.next()) {
            throw new NotFoundException("Film not found");
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
            jdbcTemplate.batchUpdate(addFilmGenre, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Genre genre = film.getGenres().get(i);
                    ps.setInt(1, id);
                    ps.setInt(2, genre.getId());
                }

                @Override
                public int getBatchSize() {
                    return film.getGenres().size();
                }
            });
        }
    }

}
