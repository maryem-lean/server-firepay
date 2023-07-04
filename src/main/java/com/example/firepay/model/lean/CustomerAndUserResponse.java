package com.example.firepay.model.lean;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
public class CustomerAndUserResponse {
    UUID customerId;
    UUID endUserId;
}
