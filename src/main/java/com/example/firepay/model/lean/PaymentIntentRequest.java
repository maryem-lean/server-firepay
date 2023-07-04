package com.example.firepay.model.lean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentIntentRequest(
        BigDecimal amount,
        String currency,
        UUID paymentDestinationId, //this param is optional; if not sent, the default payment destination will be used
        UUID customerId,
        String description //optional

) {}
