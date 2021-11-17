package com.golightyear.backend.config;

import com.golightyear.backend.account.application.AccountService;
import com.golightyear.backend.account.domain.AccountRepository;
import com.golightyear.backend.account.domain.BalanceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServiceConfig {

    @Bean
    public AccountService accountService(AccountRepository accountRepository, BalanceRepository balanceRepository) {
        return new AccountService(accountRepository, balanceRepository);
    }
}
