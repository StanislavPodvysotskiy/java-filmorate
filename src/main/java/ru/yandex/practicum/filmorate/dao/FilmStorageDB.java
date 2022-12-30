package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FilmStorageDB implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmsMapper filmsMapper;

    @Override
    public Collection<Film> getFilms() {
        String sql = "SELECT * FROM FILMS AS F LEFT OUTER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRE AS FG ON F.FILM_ID = FG.FILM " +
                "LEFT OUTER JOIN GENRE AS G ON FG.GENRE = G.GENRE_ID";
        return jdbcTemplate.query(sql, filmsMapper);
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT * FROM FILMS AS F LEFT OUTER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRE AS FG ON F.FILM_ID = FG.FILM " +
                "LEFT OUTER JOIN GENRE AS G ON FG.GENRE = G.GENRE_ID WHERE FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        Film film = new Film();
        if (rs.next()) {
            film.setId(rs.getInt("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(Objects.requireNonNull(rs.getDate("release_date")).toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setRate(rs.getInt("rate"));
            film.setMpaId(rs.getInt("mpa_id"));
            film.setMpaName(rs.getString("mpa_name"));
            if (rs.getString("genre_name") != null) {
                film.setGenre(rs.getInt("genre_id"), rs.getString("genre_name"));
                while (rs.next()) {
                    film.setGenre(rs.getInt("genre_id"), rs.getString("genre_name"));
                }
            }
        } else {
            throw new NotFoundException("Film not found");
        }
        return film;
    }

    @Override
    public Film add(Film film) {
        String sql = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, 0);
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        updateFilmGenre(film, id);
        return getFilmById(id);
    }

    @Override
    public Film update(Film film) {
        filmCheck(film.getId());
        String sql = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "MPA_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        removeFilmGenres(film.getId());
        updateFilmGenre(film, film.getId());
        return getFilmById(film.getId());
    }

    private void filmCheck(int filmId) {
        String checkFilm = "select * from FILMS where FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(checkFilm, filmId);
        if (!rs.next()) {
            throw new NotFoundException("Film not found");
        }
    }

    private void removeFilmGenres(int id) {
        String removeFilmGenre = "DELETE FROM FILM_GENRE WHERE FILM = ?";
        jdbcTemplate.update(removeFilmGenre, id);
    }

    private void updateFilmGenre(Film film, int id) {
        if (film.getGenres() != null) {
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
