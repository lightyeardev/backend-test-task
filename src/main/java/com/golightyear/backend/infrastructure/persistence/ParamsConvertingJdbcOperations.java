package com.golightyear.backend.infrastructure.persistence;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ParamsConvertingJdbcOperations implements ApplicationJdbcOperations {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public int execute(String sql, SqlParameterSource paramSource) {
        return jdbcOperations.update(sql, convert(paramSource));
    }


    @Override
    public <T> List<T> findAll(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) {
        return jdbcOperations.query(sql, convert(paramSource), rowMapper);
    }

    @Override
    public <T> Optional<T> findOneOrNone(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) {
        return  findAll(sql, paramSource, rowMapper).stream().findFirst();
    }

    private SqlParameterSource convert(SqlParameterSource source) {
        return new TypeConvertingParameterSource(source);
    }
}
