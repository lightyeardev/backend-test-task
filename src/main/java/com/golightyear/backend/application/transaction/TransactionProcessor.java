package com.golightyear.backend.application.transaction;

import com.golightyear.backend.domain.transaction.Transaction;
import com.golightyear.backend.domain.transaction.TransactionCreateRequest;

public interface TransactionProcessor {

    Transaction execute(TransactionCreateRequest request);
}
