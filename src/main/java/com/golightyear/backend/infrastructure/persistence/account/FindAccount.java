package com.golightyear.backend.infrastructure.persistence.account;

import com.golightyear.backend.account.domain.account.Account;
import com.golightyear.backend.account.domain.account.AccountId;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;
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
