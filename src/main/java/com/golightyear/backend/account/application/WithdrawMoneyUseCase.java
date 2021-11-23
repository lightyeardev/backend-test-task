package com.golightyear.backend.account.application;

import com.golightyear.backend.account.AccountRepository;
import com.golightyear.backend.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawMoneyUseCase {

    private final AccountRepository accountRepository;

    public boolean withdrawMoney(WithdrawMoneyCommand command) {
        Account account = accountRepository.findAndLock(command.accountId()).orElseThrow();

        if (!account.withdraw(command.amount(), null)) {
            return false;
        }

        accountRepository.updateActivities(account);
        return true;
    }
}
