package com.golightyear.backend.account;

import com.golightyear.backend.account.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> add(@RequestBody AccountCreateRequest request) {
        Account account = accountService.save(request.name());
        return ok(AccountResponse.from(account));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        final var responses = accountService.findAll().stream()
                .map(AccountResponse::from)
                .collect(toList());

        return ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> find(@PathVariable("id") String id) {
        return accountService
                .find(id)
                .map(AccountResponse::from)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

}
