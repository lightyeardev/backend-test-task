package com.golightyear.backend.account.presentation;

import com.golightyear.backend.account.AccountRepository;
import com.golightyear.backend.account.application.*;
import com.golightyear.backend.account.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository    accountRepository;
    private final DepositMoneyUseCase  depositMoneyUseCase;
    private final SendMoneyUseCase     sendMoneyUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;

    @PostMapping
    public ResponseEntity<AccountResponse> add(@RequestBody AccountCreateRequest request) {
        final var account = Account.builder()
                .name(new AccountName(request.name()))
                .build();

        accountRepository.add(account);
        return ok(AccountResponse.from(account));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        final var responses = accountRepository.findAll().stream()
                .map(AccountResponse::from)
                .collect(toList());

        return ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> find(@PathVariable("id") String id) {
        return accountRepository
                .find(new AccountId(id))
                .map(AccountResponse::from)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity deposit(@PathVariable("id") String id, @RequestBody CashMoneyRequest request) {

        DepositMoneyCommand command = DepositMoneyCommand.builder()
                .accountId(new AccountId(id))
                .amount(new Money(request.amount()))
                .build();

        boolean success = depositMoneyUseCase.depositMoney(command);
        return success ? accepted().build() : badRequest().build();
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity withdraw(@PathVariable("id") String id, @RequestBody CashMoneyRequest request) {

        WithdrawMoneyCommand command = WithdrawMoneyCommand.builder()
                .accountId(new AccountId(id))
                .amount(new Money(request.amount()))
                .build();

        boolean success = withdrawMoneyUseCase.withdrawMoney(command);
        return success ? accepted().build() : badRequest().build();
    }

    @PostMapping("/{id}/send-money")
    public ResponseEntity send(@PathVariable("id") String id, @RequestBody SendMoneyRequest request) {

        SendMoneyCommand command = SendMoneyCommand.builder()
                .sourceAccountId(new AccountId(id))
                .targetAccountId(new AccountId(request.targetAccount()))
                .amount(new Money(request.amount()))
                .build();

        boolean success = sendMoneyUseCase.sendMoney(command);
        return success ? accepted().build() : badRequest().build();
    }

}
