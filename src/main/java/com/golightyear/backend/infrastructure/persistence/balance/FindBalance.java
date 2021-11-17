package com.golightyear.backend.infrastructure.persistence.balance;

import com.golightyear.backend.account.domain.account.AccountId;
import com.golightyear.backend.account.domain.balance.Balance;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Optional;

@AllArgsConstructor
public class FindBalance {
    private final ApplicationJdbcOperations jdbcOperations;
    private final BalanceRowMapper mapper;

    private static final String SQL = "SELECT * from balance where account_id = :accountId";

    public Optional<Balance> execute(AccountId accountId) {
        return jdbcOperations.findOneOrNone(
            SQL,
            new MapSqlParameterSource("accountId", accountId.value()),
            mapper
        );
    }
}
