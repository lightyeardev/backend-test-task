package com.golightyear.backend.account.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class AccountCreateRequest {

    String name;

}
