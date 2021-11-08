package com.golightyear.backend.transaction;

import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.transaction.domain.Transaction;
import com.golightyear.backend.transaction.domain.TransactionAmount;
import com.golightyear.backend.transaction.domain.TransactionRequest;
import com.golightyear.backend.transaction.domain.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody TransactionRequest request) {
        final var transaction = Transaction.builder()
                .fromAccount(new AccountId(request.fromAccount()))
                .toAccount(new AccountId(request.toAccount()))
                .amount(new TransactionAmount(request.amount()))
                .build();

        transactionService.create(transaction);
        return ok(TransactionResponse.from(transaction));
    }

}
