package com.golightyear.backend.account.application;

import com.golightyear.backend.account.AccountRepository;
import com.golightyear.backend.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SendMoneyUseCase {

    private final AccountRepository accountRepository;

    public boolean sendMoney(SendMoneyCommand command) {
        Account sourceAccount = accountRepository.findAndLock(command.sourceAccountId()).orElseThrow();
        Account targetAccount = accountRepository.findAndLock(command.targetAccountId()).orElseThrow();

        if (!sourceAccount.withdraw(command.amount(), targetAccount.id())) {
            return false;
        }

        if (!targetAccount.deposit(command.amount(), sourceAccount.id())) {
            return false;
        }

        accountRepository.updateActivities(sourceAccount);
        accountRepository.updateActivities(targetAccount);

        return true;
    }
}
