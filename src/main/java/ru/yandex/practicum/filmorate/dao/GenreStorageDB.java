package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreStorageDB implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

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
