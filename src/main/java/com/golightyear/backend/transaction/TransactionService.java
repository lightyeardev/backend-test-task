package com.golightyear.backend.transaction;

import com.golightyear.backend.account.AccountService;
import com.golightyear.backend.account.domain.AccountBalance;
import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.transaction.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public Transaction create(Transaction transaction) {

        AccountId fromAccount = transaction.fromAccount();
        AccountId toAccount = transaction.toAccount();
        assertAccountActive(fromAccount);
        assertAccountActive(toAccount);

        AccountBalance balance = new AccountBalance(transaction.amount().value());
        try{
            accountService.removeFromBalance(fromAccount, balance);
            accountService.addToBalance(toAccount, balance);
            transactionRepository.add(transaction);
        } catch (DataIntegrityViolationException exception){
            throw new IllegalStateException("Transaction cannot be created, please check account ıds and balances");
        }

        return transaction;
    }

    private void assertAccountActive(AccountId accountId) {
        if (!accountService.isAccountActive(accountId)) {
            throw new IllegalStateException("Account is not active or does not exist");
        }
    }

}
