package com.golightyear.backend.infrastructure.persistence.transaction;

import com.golightyear.backend.application.transaction.TransactionFactory;
import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.transaction.Currency;
import com.golightyear.backend.domain.transaction.Money;
import com.golightyear.backend.domain.transaction.Transaction;
import com.golightyear.backend.domain.transaction.TransactionId;
import com.golightyear.backend.domain.transaction.TransactionType;
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
public class JdbcTransactionRepository implements TransactionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TransactionFactory transactionFactory;

    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String ACCOUNT_ID = "account_id";
    private static final String AMOUNT = "amount";
    private static final String CURRENCY = "currency";
    private static final String CREATE_TIME = "create_time";

    @Override
    public void save(Transaction transaction) {
        jdbcTemplate.update(SAVE, createSource(transaction));
    }

    @Override
    public void saveAll(List<Transaction> transactions) {
        var batchParameters = transactions.stream()
                .map(this::createSource)
                .toArray(SqlParameterSource[]::new);
        jdbcTemplate.batchUpdate(SAVE, batchParameters);
    }

    @Override
    public Optional<Transaction> findById(TransactionId transactionId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                            FIND_BY_ID,
                            new MapSqlParameterSource(Map.of(ID, transactionId.value())),
                            this::mapRow
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Transaction> findByAccountId(AccountId accountId) {
        return jdbcTemplate.query(
                FIND_BY_ACCOUNT_ID,
                new MapSqlParameterSource(Map.of(ACCOUNT_ID, accountId.value())),
                this::mapRow
        );
    }

    private SqlParameterSource createSource(Transaction transaction) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(ID, transaction.id().value());
        source.addValue(TYPE, transaction.type().name());
        source.addValue(ACCOUNT_ID, transaction.accountId().value());
        source.addValue(AMOUNT, transaction.money().value());
        source.addValue(CURRENCY, transaction.money().currency().name());
        source.addValue(CREATE_TIME, Timestamp.from(transaction.createTime()));
        return source;
    }

    private Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        return transactionFactory.existing(
                new TransactionId(UUID.fromString(rs.getString(ID))),
                TransactionType.valueOf(rs.getString(TYPE)),
                new AccountId(UUID.fromString(rs.getString(ACCOUNT_ID))),
                new Money(rs.getBigDecimal(AMOUNT), Currency.valueOf(rs.getString(CURRENCY))),
                rs.getTimestamp(CREATE_TIME).toInstant()
        );
    }

    private static final String SAVE =
            // language="PostgreSQL"
            """
                    INSERT INTO transaction(
                            id,
                            type,
                            account_id,
                            amount,
                            currency,
                            create_time
                        ) VALUES (
                            :id,
                            :type,
                            :account_id,
                            :amount,
                            :currency,
                            :create_time
                        )
                    """;

    private static final String FIND_BY_ID =
            // language="PostgreSQL"
            """
                        SELECT
                        id,
                        type,
                        account_id,
                        amount,
                        currency,
                        create_time FROM transaction WHERE
                        id = :id
                    """;

    private static final String FIND_BY_ACCOUNT_ID =
            // language="PostgreSQL"
            """
                        SELECT
                        id,
                        type,
                        account_id,
                        amount,
                        currency,
                        create_time FROM transaction WHERE
                        account_id = :account_id
                    """;
}
