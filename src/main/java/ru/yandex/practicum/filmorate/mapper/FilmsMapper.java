package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmsMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int i) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        film.setRate(resultSet.getInt("rate"));
        film.setMpaId(resultSet.getInt("mpa_id"));
        film.setMpaName(resultSet.getString("mpa_name"));
        if (resultSet.getString("genre_name") != null) {
            film.setGenre(resultSet.getInt("genre_id"), resultSet.getString("genre_name"));
        }
        return film;
    }
}
