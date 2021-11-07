package com.golightyear.backend.application.lock;

import com.golightyear.backend.domain.common.Resource;
import com.golightyear.backend.domain.lock.Lock;
import com.golightyear.backend.domain.lock.LockException;
import com.golightyear.backend.infrastructure.persistence.lock.LockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class LockService {

    private final TransactionTemplate transactionTemplate;
    private final LockRepository lockRepository;

    public <T> T executeWithLock(Resource resource, LockedProcedure<T> procedure) {
        return transactionTemplate.execute(status -> {
            try {
                Lock lock = lockRepository.lock(resource);
                if (lock == null) {
                    throw new LockException(String.format("Failed acquiring lock for %s", resource));
                }
                return procedure.execute();
            } catch (RuntimeException exception) {
                throw exception;
            } catch (Exception exception) {
                throw new IllegalStateException(exception);
            }
        });
    }
}
