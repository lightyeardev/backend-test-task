package com.golightyear.backend.infrastructure.persistence;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Optional;

public interface ApplicationJdbcOperations {
    int execute(String sql, SqlParameterSource paramSource);

    <T> List<T> findAll(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper);

    <T> Optional<T> findOneOrNone(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper);

}
