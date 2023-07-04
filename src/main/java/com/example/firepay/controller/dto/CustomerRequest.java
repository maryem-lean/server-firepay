package com.example.firepay.controller.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@Jacksonized
public class CustomerRequest {

    String name;
    String address;
    String companyName;
    String taxId;
}
