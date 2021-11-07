package com.golightyear.backend.infrastructure.persistence.account;

import com.golightyear.backend.domain.account.Account;
import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.account.AccountName;
import com.golightyear.backend.domain.account.AccountState;
import com.golightyear.backend.domain.balance.Balance;
import com.golightyear.backend.infrastructure.persistence.balance.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcAccountRepository implements AccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BalanceRepository balanceRepository;

    private static final String ID = "id";
    private static final String VERSION = "version";
    private static final String NAME = "name";
    private static final String STATE = "state";
    private static final String CREATE_TIME = "create_time";
    private static final String LAST_MODIFIED = "last_modified";

    @Override
    public void save(Account account) {
        jdbcTemplate.update(SAVE, createSource(account));
    }

    @Override
    public Optional<Account> find(AccountId id) {
        try {
            return jdbcTemplate
                    .query(FIND_BY_ID, new MapSqlParameterSource(Map.of(ID, id.value())), this::mapRow).stream()
                    .findFirst()
                    .map(this::populateBalances);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Account populateBalances(Account account) {
        List<Balance> balances = balanceRepository.findByAccountId(account.id());
        hydrateField("balances", account, balances);
        return account;
    }

    public static void hydrateField(String fieldName, Object object, Object value) {
        Field field = ReflectionUtils.findField(object.getClass(), fieldName);
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, object, value);
    }

    private SqlParameterSource createSource(Account account) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(ID, account.id().value());
        source.addValue(VERSION, account.version());
        source.addValue(NAME, account.name().value());
        source.addValue(STATE, account.state().name());
        source.addValue(CREATE_TIME, Timestamp.from(account.createTime()));
        source.addValue(LAST_MODIFIED, Timestamp.from(account.lastModified()));
        return source;
    }

    private Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Account.builder()
                .id(new AccountId(UUID.fromString(rs.getString(ID))))
                .version(rs.getInt(VERSION))
                .name(new AccountName(rs.getString(NAME)))
                .state(AccountState.valueOf(rs.getString(STATE)))
                .createTime(rs.getTimestamp(CREATE_TIME).toInstant())
                .lastModified(rs.getTimestamp(LAST_MODIFIED).toInstant())
                .build();
    }

    private static final String SAVE =
            // language="PostgreSQL"
            """
                    INSERT INTO account(
                            id,
                            version,
                            name,
                            state,
                            create_time,
                            last_modified
                        ) VALUES (
                            :id,
                            :version,
                            :name,
                            :state,
                            :create_time,
                            :last_modified
                        ) ON CONFLICT (id) DO UPDATE SET
                        version = :version + 1, last_modified = :last_modified
                    """;

    private static final String FIND_BY_ID =
            // language="PostgreSQL"
            """
                        SELECT
                        id,
                        version,
                        name,
                        state,
                        create_time,
                        last_modified FROM account WHERE
                        id = :id
                    """;
}
