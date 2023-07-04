package com.example.firepay.controller.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Value
@Jacksonized
public class InvoiceRequest {
    UUID customerId; // the one in our DB
    LocalDate dueDate;
    String number;
    BigDecimal amount;
    String currency;
}
