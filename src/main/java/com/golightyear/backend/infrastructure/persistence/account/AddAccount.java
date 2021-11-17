package com.golightyear.backend.infrastructure.persistence.account;

import com.golightyear.backend.account.domain.Account;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@AllArgsConstructor
public class AddAccount {
    private final ApplicationJdbcOperations jdbcOperations;

    private static final String SQL = """
        INSERT INTO account (id, version, name, state, create_time, last_modified)
        VALUES (:id, :version, :name, :state, :createTime, :lastModified)
        ON CONFLICT (id) DO UPDATE SET
            name = :name,
            version = :version,
            state = :state,
            last_modified = :lastModified
        """;

    public void execute(Account account) {
        jdbcOperations.execute(SQL, new MapSqlParameterSource()
            .addValue("id", account.id().value())
            .addValue("version", account.version())
            .addValue("name", account.name().value())
            .addValue("state", account.state())
            .addValue("createTime", account.createTime())
            .addValue("lastModified", account.lastModified())
        );
    }
}
