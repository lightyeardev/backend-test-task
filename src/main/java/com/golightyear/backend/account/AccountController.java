package com.golightyear.backend.account;

import com.golightyear.backend.account.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("account")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Account> add(@RequestBody AccountCreateRequest request) {
        final var account = Account.builder()
                .name(new AccountName(request.name()))
                .build();

        accountRepository.add(account);
        return ok(account);
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

}
