package com.golightyear.backend.account.application;

import com.golightyear.backend.account.domain.account.*;
import com.golightyear.backend.account.domain.balance.Balance;
import com.golightyear.backend.account.domain.balance.BalanceRepository;
import com.golightyear.backend.account.domain.transaction.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Account create(String name) {
        var account = Account.builder()
            .name(new AccountName(name))
            .build();

        var balance = Balance.builder()
            .accountId(account.id())
            .build();

        accountRepository.add(account);
        balanceRepository.add(balance);

        return account;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> find(String accountId) {
        return accountRepository.find(new AccountId(accountId));
    }

    public Optional<Balance> balance(String accountId) {
        return balanceRepository.find(new AccountId(accountId));
    }

    @Transactional
    public TransactionResult transfer(String source, String target, BigDecimal amount) {
        var sourceAccountId = new AccountId(source);
        var sourceBalance = balanceRepository.find(sourceAccountId);
        if (sourceBalance.isEmpty()) {
            throw new AccountNotFoundException(sourceAccountId);
        }

        var targetAccountId = new AccountId(target);
        var targetBalance = balanceRepository.find(targetAccountId);
        if (targetBalance.isEmpty()) {
            throw new AccountNotFoundException(targetAccountId);
        }

        var debitResult = debit(sourceBalance.get(), amount);
        credit(targetBalance.get(), amount);

        return debitResult;
    }

    private void credit(Balance balance, BigDecimal amount) {
        transactionRepository.add(Transaction.builder()
            .type(TransactionType.CREDIT)
            .accountId(balance.accountId())
            .amount(new TransactionAmount(amount))
            .build());

        balanceRepository.add(balance.credit(amount));
    }

    private TransactionResult debit(Balance balance, BigDecimal amount) {
        var transaction = Transaction.builder()
                .type(TransactionType.DEBIT)
                .accountId(balance.accountId())
                .amount(new TransactionAmount(amount))
                .build();
        transactionRepository.add(transaction);

        var updated = balance.debit(amount);
        balanceRepository.add(updated);

        return TransactionResult.builder()
            .accountId(balance.accountId())
            .balanceAmount(updated.amount())
            .createTime(transaction.createTime())
            .build();
    }
}
