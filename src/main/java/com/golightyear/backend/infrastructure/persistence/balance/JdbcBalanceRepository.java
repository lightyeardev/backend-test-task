package com.golightyear.backend.infrastructure.persistence.balance;

import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.balance.Balance;
import com.golightyear.backend.domain.balance.BalanceId;
import com.golightyear.backend.domain.balance.BalanceState;
import com.golightyear.backend.domain.transaction.Currency;
import com.golightyear.backend.domain.transaction.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcBalanceRepository implements BalanceRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String ID = "id";
    private static final String VERSION = "version";
    private static final String ACCOUNT_ID = "account_id";
    private static final String STATE = "state";
    private static final String AMOUNT = "amount";
    private static final String CURRENCY = "currency";
    private static final String CREATE_TIME = "create_time";
    private static final String LAST_MODIFIED = "last_modified";

    @Override
    public void save(Balance balance) {
        jdbcTemplate.update(SAVE, createSource(balance));
    }

    @Override
    public void save(List<Balance> balances) {
        var sources = balances.stream()
                .map(this::createSource)
                .toArray(SqlParameterSource[]::new);
        jdbcTemplate.batchUpdate(SAVE, sources);
    }

    @Override
    public Optional<Balance> find(BalanceId balanceId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                            FIND_BY_ID,
                            new MapSqlParameterSource(Map.of(ID, balanceId.value())),
                            this::mapRow
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Balance> findByAccountId(AccountId accountId) {
        return jdbcTemplate.query(
                FIND_BY_ACCOUNT_ID,
                new MapSqlParameterSource(Map.of(ACCOUNT_ID, accountId.value())),
                this::mapRow
        );
    }

    private MapSqlParameterSource createSource(Balance balance) {
        var source = new MapSqlParameterSource();
        source.addValue(ID, balance.id().value());
        source.addValue(VERSION, balance.version());
        source.addValue(ACCOUNT_ID, balance.accountId().value());
        source.addValue(STATE, balance.state().name());
        source.addValue(AMOUNT, balance.money().value());
        source.addValue(CURRENCY, balance.money().currency().name());
        source.addValue(CREATE_TIME, Timestamp.from(balance.createTime()));
        source.addValue(LAST_MODIFIED, Timestamp.from(balance.lastModified()));
        return source;
    }

    private Balance mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Balance.builder()
                .id(new BalanceId(UUID.fromString(rs.getString(ID))))
                .version(rs.getInt(VERSION))
                .accountId(new AccountId(UUID.fromString(rs.getString(ACCOUNT_ID))))
                .state(BalanceState.valueOf(rs.getString(STATE)))
                .money(new Money(rs.getBigDecimal(AMOUNT), Currency.valueOf(rs.getString(CURRENCY))))
                .createTime(rs.getTimestamp(CREATE_TIME).toInstant())
                .lastModified(rs.getTimestamp(LAST_MODIFIED).toInstant())
                .build();
    }

    private static final String SAVE =
            // language="PostgreSQL"
            """
                    INSERT INTO balance(
                            id,
                            version,
                            account_id,
                            state,
                            amount,
                            currency,
                            create_time,
                            last_modified
                        ) VALUES (
                            :id,
                            :version,
                            :account_id,
                            :state,
                            :amount,
                            :currency,
                            :create_time,
                            :last_modified
                        ) ON CONFLICT (id) DO UPDATE SET
                        version = :version + 1, last_modified = :last_modified, amount = :amount
                    """;

    private static final String FIND_BY_ID =
            // language="PostgreSQL"
            """
                        SELECT
                        id,
                        version,
                        account_id,
                        state,
                        amount,
                        currency,
                        create_time,
                        last_modified FROM balance WHERE
                        id = :id
                    """;

    private static final String FIND_BY_ACCOUNT_ID =
            // language="PostgreSQL"
            """
                        SELECT
                        id,
                        version,
                        account_id,
                        state,
                        amount,
                        currency,
                        create_time,
                        last_modified FROM balance WHERE
                        account_id = :account_id
                    """;
}
