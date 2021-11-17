package com.golightyear.backend.infrastructure.persistence.transaction;

import com.golightyear.backend.account.domain.transaction.Transaction;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@AllArgsConstructor
public class AddTransaction {
    private final ApplicationJdbcOperations jdbcOperations;

    private static final String SQL = """
        INSERT INTO account_transaction (id, type, account_id, amount, create_time)
        VALUES (:id, :type, :accountId, :amount, :createTime)
        """;

    public void execute(Transaction transaction) {
        jdbcOperations.execute(SQL, new MapSqlParameterSource()
            .addValue("id", transaction.id())
            .addValue("type", transaction.type())
            .addValue("accountId", transaction.accountId())
            .addValue("amount", transaction.amount())
            .addValue("createTime", transaction.createTime())
        );
    }
}
