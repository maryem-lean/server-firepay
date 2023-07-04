package com.example.firepay.model.lean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@Jacksonized
public class PaymentIntentResponse {
    UUID paymentIntentId;
    BigDecimal amount;
    String currency;
    UUID customerId;
    String description;
    List<Object> payments; //todo
    PaymentDestination paymentDestination;
    String createdAt;
}
