package com.golightyear.backend.presentation;

import com.golightyear.backend.domain.account.Account;
import com.golightyear.backend.domain.account.AccountCreateRequest;
import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.account.AccountName;
import com.golightyear.backend.domain.account.AccountResponse;
import com.golightyear.backend.infrastructure.persistence.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    @PostMapping
    public ResponseEntity<Account> save(@RequestBody AccountCreateRequest request) {
        final var account = Account.builder()
                .name(new AccountName(request.name()))
                .build();

        accountRepository.save(account);
        return ok(account);
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
