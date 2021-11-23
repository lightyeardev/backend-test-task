package com.golightyear.backend.account.presentation;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class CashMoneyRequest {
    BigDecimal amount;
}
