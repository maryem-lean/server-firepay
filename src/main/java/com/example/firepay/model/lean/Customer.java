package com.example.firepay.model.lean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@Jacksonized
public class Customer {

    String appUserId;
    UUID customerId;
}
