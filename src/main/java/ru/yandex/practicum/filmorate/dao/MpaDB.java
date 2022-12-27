package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDB implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

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

}
