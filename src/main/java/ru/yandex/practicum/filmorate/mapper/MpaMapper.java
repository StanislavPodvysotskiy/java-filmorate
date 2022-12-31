package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaMapper implements RowMapper<Mpa> {

    @Override
    public Mpa mapRow(ResultSet resultSet, int i) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("MPA_ID"));
        mpa.setName(resultSet.getString("MPA_NAME"));
        return mpa;
    }

}
