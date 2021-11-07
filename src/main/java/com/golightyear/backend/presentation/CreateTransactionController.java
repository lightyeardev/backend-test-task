package com.golightyear.backend.presentation;

import com.golightyear.backend.application.transaction.CreateTransaction;
import com.golightyear.backend.domain.common.InvalidTransactionException;
import com.golightyear.backend.domain.common.NoSuchModelException;
import com.golightyear.backend.domain.transaction.TransactionCreateRequest;
import com.golightyear.backend.domain.transaction.TransactionCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/v1/accounts/{accountId}/transactions")
@RequiredArgsConstructor
public class CreateTransactionController {

    private final CreateTransaction createTransaction;

    @PostMapping
    public ResponseEntity<TransactionCreateResponse> add(
            @PathVariable UUID accountId,
            @RequestBody TransactionCreateRequest request
    ) {
        try {
            return createTransaction.execute(accountId, request)
                    .map(TransactionCreateResponse::from)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new InvalidTransactionException("Error while transacting"));
        } catch (NoSuchModelException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (InvalidTransactionException exception) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
        }
    }
}
