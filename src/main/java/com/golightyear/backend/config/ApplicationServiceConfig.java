package com.golightyear.backend.config;

import com.golightyear.backend.account.application.AccountService;
import com.golightyear.backend.account.domain.account.AccountRepository;
import com.golightyear.backend.account.domain.balance.BalanceRepository;
import com.golightyear.backend.account.domain.transaction.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServiceConfig {

    @Bean
    public AccountService accountService(AccountRepository accountRepository,
                                         BalanceRepository balanceRepository,
                                         TransactionRepository transactionRepository) {
        return new AccountService(accountRepository, balanceRepository, transactionRepository);
    }
}
