package com.golightyear.backend.application.lock;

import com.golightyear.backend.domain.account.AccountId;
import com.golightyear.backend.domain.common.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountLockService {

    private static final String ACCOUNT_RESOURCE_TYPE = "account";

    private final LockService lockService;

    public <T> T executeTransactionWithAccountLockAndReturn(AccountId accountId, LockedProcedure<T> procedure) {
        return lockService.executeWithLock(new Resource(ACCOUNT_RESOURCE_TYPE, accountId.value().toString()), procedure);
    }
}
