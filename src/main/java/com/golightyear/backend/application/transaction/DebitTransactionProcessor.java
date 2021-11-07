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
public class DebitTransactionProcessor implements TransactionProcessor {

    private final TransactionFactory transactionFactory;
    private final BalanceRepository balanceRepository;

    @Override
    public Transaction execute(TransactionCreateRequest request) {
        var sourceBalanceId = new BalanceId(request.sourceBalanceId());
        var sourceBalance = balanceRepository.find(sourceBalanceId)
                .orElseThrow(() -> new NoSuchModelException(String.format("Balance %s does not exist", sourceBalanceId)));
        var money = new Money(request.amount(), Currency.valueOf(request.currency()));

        validate(sourceBalance, money);

        var transaction = createTransaction(sourceBalance.accountId(), money);

        sourceBalance.withdraw(transaction.money());
        balanceRepository.save(sourceBalance);
        // publish event
        return transaction;
    }

    private void validate(Balance sourceBalance, Money money) {
        if (sourceBalance.isInactive()) {
            throw new InvalidTransactionException("Source balance is inactive");
        }
        if (sourceBalance.money().lessThan(money)) {
            throw new InvalidTransactionException("Funds are not sufficient");
        }
    }

    private Transaction createTransaction(AccountId accountId, Money money) {
        return transactionFactory.create(
                TransactionId.random(),
                TransactionType.DEBIT,
                accountId,
                money,
                Instant.now()
        );
    }
}
