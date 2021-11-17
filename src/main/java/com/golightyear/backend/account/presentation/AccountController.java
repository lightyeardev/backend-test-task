package com.golightyear.backend.account.presentation;

import com.golightyear.backend.account.application.AccountService;
import com.golightyear.backend.account.domain.Account;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("account")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> add(@RequestBody AccountCreateRequest request) {
        return ok(accountService.create(request.name()));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        return ok(accountService.findAll().stream()
            .map(AccountResponse::from)
            .collect(toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> find(@PathVariable("id") String id) {
        return accountService
            .find(id)
            .map(AccountResponse::from)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponse> balance(@PathVariable("id") String id) {
        return accountService.balance(id)
            .map(BalanceResponse::from)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }
}
