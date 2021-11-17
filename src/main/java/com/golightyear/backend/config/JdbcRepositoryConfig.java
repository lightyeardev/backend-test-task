package com.golightyear.backend.config;

import com.golightyear.backend.account.domain.account.AccountRepository;
import com.golightyear.backend.account.domain.balance.BalanceRepository;
import com.golightyear.backend.account.domain.transaction.TransactionRepository;
import com.golightyear.backend.infrastructure.persistence.account.AccountRepositoryJdbc;
import com.golightyear.backend.infrastructure.persistence.ApplicationJdbcOperations;
import com.golightyear.backend.infrastructure.persistence.ParamsConvertingJdbcOperations;
import com.golightyear.backend.infrastructure.persistence.balance.BalanceRepositoryJdbc;
import com.golightyear.backend.infrastructure.persistence.transaction.TransactionRepositoryJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@Configuration
public class JdbcRepositoryConfig {

    @Bean
    public ApplicationJdbcOperations applicationJdbcOperations(NamedParameterJdbcOperations jdbcOperations) {
        return new ParamsConvertingJdbcOperations(jdbcOperations);
    }

    @Bean
    public AccountRepository accountRepository(ApplicationJdbcOperations jdbcOperations) {
        return new AccountRepositoryJdbc(jdbcOperations);
    }

    @Bean
    public BalanceRepository balanceRepository(ApplicationJdbcOperations jdbcOperations) {
        return new BalanceRepositoryJdbc(jdbcOperations);
    }

    @Bean
    public TransactionRepository transactionRepository(ApplicationJdbcOperations jdbcOperations) {
        return new TransactionRepositoryJdbc(jdbcOperations);
    }
}
