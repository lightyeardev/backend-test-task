package com.golightyear.backend.infrastructure.persistence.account;

import com.golightyear.backend.account.domain.account.Account;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.List;

@AllArgsConstructor
public class FindAllAccount {
    private final ApplicationJdbcOperations jdbcOperations;
    private final AccountRowMapper mapper;

    private static final String SQL = "select * from account";

    public List<Account> execute() {
        return jdbcOperations.findAll(SQL, new MapSqlParameterSource(), mapper);
    }
}
