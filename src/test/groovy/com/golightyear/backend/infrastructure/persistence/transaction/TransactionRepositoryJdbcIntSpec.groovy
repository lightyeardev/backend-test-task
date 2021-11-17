package com.golightyear.backend.infrastructure.persistence.transaction

import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.account.domain.transaction.TransactionRepository
import com.golightyear.backend.testdata.TransactionTestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations

class TransactionRepositoryJdbcIntSpec extends AbstractIntegrationSpec {
    @Autowired
    TransactionRepository repository

    @Autowired
    NamedParameterJdbcOperations jdbcOperations

    def "should add transaction"() {
        given:
            def transaction = new TransactionTestData().build()

        when:
            repository.add(transaction)

        then:
            jdbcOperations.queryForObject("SELECT count(*) FROM account_transaction", new MapSqlParameterSource(), Integer.class) == 1
    }
}
