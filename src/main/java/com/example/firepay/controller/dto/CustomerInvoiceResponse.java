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
public class CustomerInvoiceResponse {

    UUID id;
    CustomerResponse customer;
    UUID paymentIntentId;
    String paymentStatus;
    String currency;
    BigDecimal amount;
    LocalDate issueDate;
    LocalDate dueDate;
    String number;
    boolean connected;
}
