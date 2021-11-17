package com.golightyear.backend.infrastructure.persistence.balance;

import com.golightyear.backend.account.domain.Balance;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@AllArgsConstructor
public class AddBalance {
    private final ApplicationJdbcOperations jdbcOperations;

    private static final String SQL = """
        INSERT INTO balance (id, account_id, version, amount, create_time, last_modified)
        VALUES (:id, :accountId, :version, :amount, :createTime, :lastModified)
        ON CONFLICT (id) DO UPDATE SET
            version = :version,
            amount = :amount,
            last_modified = :lastModified
        """;

    public void execute(Balance balance) {
        jdbcOperations.execute(SQL, new MapSqlParameterSource()
            .addValue("id", balance.id().value())
            .addValue("accountId", balance.accountId().value())
            .addValue("version", balance.version())
            .addValue("amount", balance.amount().value())
            .addValue("createTime", balance.createTime())
            .addValue("lastModified", balance.lastModified())
        );
    }
}
