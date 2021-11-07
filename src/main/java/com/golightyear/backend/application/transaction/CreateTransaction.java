package com.golightyear.backend.application.transaction;

import com.golightyear.backend.application.lock.AccountLockService;
import com.golightyear.backend.domain.account.Account;
import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.common.InvalidTransactionException;
import com.golightyear.backend.domain.common.NoSuchModelException;
import com.golightyear.backend.domain.transaction.Transaction;
import com.golightyear.backend.domain.transaction.TransactionCreateRequest;
import com.golightyear.backend.domain.transaction.TransactionType;
import com.golightyear.backend.infrastructure.persistence.account.AccountRepository;
import com.golightyear.backend.infrastructure.persistence.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateTransaction {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountLockService lockService;
    private final List<TransactionProcessor> transactionProcessors;

    public Optional<Transaction> execute(UUID accountIdValue, TransactionCreateRequest request) {
        var accountId = new AccountId(accountIdValue);
        var account = accountRepository.find(accountId)
                .orElseThrow(() -> new NoSuchModelException("Invalid account " + accountId));
        validate(account, request);

        // should be enhanced to check for idempotence
        return lockService.executeTransactionWithAccountLockAndReturn(accountId, () -> {
            List<Transaction> transactions = transactionProcessors.stream()
                    .map(t -> t.execute(request))
                    .collect(Collectors.toList());
            transactionRepository.saveAll(transactions);
            return debitTransactionResponse(transactions);
        });
    }

    private void validate(Account account, TransactionCreateRequest request) {
        if (account.isInactive()) {
            throw new InvalidTransactionException("Cannot perform transaction as account is inactive");
        }
        if (request.sourceBalanceId().equals(request.targetBalanceId())) {
            throw new InvalidTransactionException("Cannot send money to self");
        }
    }

    private Optional<Transaction> debitTransactionResponse(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.type() == TransactionType.DEBIT)
                .findFirst();
    }
}
