package com.golightyear.backend.infrastructure.persistence.lock;

import com.golightyear.backend.domain.lock.Lock;
import com.golightyear.backend.domain.common.Resource;

public interface LockRepository {

    Lock lock(Resource resource);

}
