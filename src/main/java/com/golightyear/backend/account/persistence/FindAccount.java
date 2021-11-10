package com.golightyear.backend.account.persistence;

import com.golightyear.backend.account.domain.Account;
import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;
import com.golightyear.backend.infrastructure.persistence.account.AccountRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Optional;

@AllArgsConstructor
public class FindAccount {
    private final ApplicationJdbcOperations jdbcOperations;
    private final AccountRowMapper mapper;

    private final String SQL = "SELECT * FROM account WHERE id = :id";

    public Optional<Account> execute(AccountId accountId) {
        return jdbcOperations.findOneOrNone(
            SQL,
            new MapSqlParameterSource("id", accountId.value()),
            mapper
        );
    }
}
