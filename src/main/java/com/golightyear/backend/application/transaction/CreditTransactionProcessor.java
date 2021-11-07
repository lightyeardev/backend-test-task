package com.golightyear.backend.application.transaction;

import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.balance.Balance;
import com.golightyear.backend.domain.balance.BalanceId;
import com.golightyear.backend.domain.common.InvalidTransactionException;
import com.golightyear.backend.domain.common.NoSuchModelException;
import com.golightyear.backend.domain.transaction.Currency;
import com.golightyear.backend.domain.transaction.Money;
import com.golightyear.backend.domain.transaction.Transaction;
import com.golightyear.backend.domain.transaction.TransactionCreateRequest;
import com.golightyear.backend.domain.transaction.TransactionId;
import com.golightyear.backend.domain.transaction.TransactionType;
import com.golightyear.backend.infrastructure.persistence.balance.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreditTransactionProcessor implements TransactionProcessor {

    private final TransactionFactory transactionFactory;
    private final BalanceRepository balanceRepository;

    @Override
    public Transaction execute(TransactionCreateRequest request) {
        var targetBalanceId = new BalanceId(request.targetBalanceId());
        var targetBalance = balanceRepository.find(targetBalanceId)
                .orElseThrow(() -> new NoSuchModelException(String.format("Balance %s does not exist", targetBalanceId)));

        validate(targetBalance);

        var money = new Money(request.amount(), Currency.valueOf(request.currency()));
        var transaction = createTransaction(targetBalance.accountId(), money);

        targetBalance.deposit(transaction.money());
        balanceRepository.save(targetBalance);
        // publish event
        return transaction;
    }

    private void validate(Balance targetBalance) {
        if (targetBalance.isInactive()) {
            throw new InvalidTransactionException("Target balance is inactive");
        }
    }

    private Transaction createTransaction(AccountId accountId, Money money) {
        return transactionFactory.create(
                TransactionId.random(),
                TransactionType.CREDIT,
                accountId,
                money,
                Instant.now()
        );
    }
}
