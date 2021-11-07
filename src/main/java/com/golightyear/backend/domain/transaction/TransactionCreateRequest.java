package com.golightyear.backend.domain.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionCreateRequest {

    @NonNull
    UUID sourceBalanceId;

    @NonNull
    UUID targetBalanceId;

    @NonNull
    BigDecimal amount;

    @NonNull
    String currency;

}
