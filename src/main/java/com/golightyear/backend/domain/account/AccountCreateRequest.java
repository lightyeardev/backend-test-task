package com.golightyear.backend.domain.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountCreateRequest {

    @NonNull
    String name;

}
