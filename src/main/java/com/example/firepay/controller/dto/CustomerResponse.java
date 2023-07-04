package com.example.firepay.controller.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Builder
@Value
@Jacksonized
public class CustomerResponse {

    UUID id;
    UUID customerId;
    UUID endUserId;
    String name;
    String address;
    String companyName;
    String taxId;
}
