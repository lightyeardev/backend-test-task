package com.golightyear.backend.transaction.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class TransactionRequest {

    String fromAccount;
    String toAccount;
    BigDecimal amount;

}
