package com.golightyear.backend.infrastructure.persistence.lock;

import com.golightyear.backend.domain.common.Resource;
import com.golightyear.backend.domain.lock.DatabaseLock;
import com.golightyear.backend.domain.lock.Lock;
import com.golightyear.backend.domain.lock.LockException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
@RequiredArgsConstructor
public class JdbcLockRepository implements LockRepository {

    private static final String NAMESPACE_PARAM = "namespace";
    private static final String LOCK_ID_PARAM = "lockId";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Lock lock(Resource resource) throws LockException {
        try {
            // make timeout configurable
            namedParameterJdbcTemplate.execute("SET LOCAL lock_timeout = 1000;", PreparedStatement::execute);
            namedParameterJdbcTemplate.execute(LOCK_SQL, getParamsForLock(resource), PreparedStatement::execute);
            return new DatabaseLock(resource.key(), resource.name()) {
            };
        } catch (RuntimeException e) {
            throw new LockException(String.format("Failed to acquire lock %s:%s", resource.name(), resource.key()), e);
        }
    }

    private MapSqlParameterSource getParamsForLock(Resource resource) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(NAMESPACE_PARAM, resource.getNameHashCode());
        params.addValue(LOCK_ID_PARAM, resource.getKeyHashCode());
        return params;
    }

    private static final String LOCK_SQL = "SELECT pg_advisory_xact_lock(:" + NAMESPACE_PARAM + ", :" + LOCK_ID_PARAM + ")";
}
