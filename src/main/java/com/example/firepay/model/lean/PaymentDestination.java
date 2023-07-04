package com.example.firepay.model.lean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@Jacksonized
public class PaymentDestination {
        String id;
        String bankIdentifier;
        String name;
        String iban;
        String displayName;
        String accountNumber;
        String swiftCode;
        String status;
        String address;
        String country;
        String city;
        @JsonProperty("default")
        boolean defaultDestination;
        String ifsc;
        String sortCode;
        String routingNumber;
        String transitCode;
        String branchAddress;
        String currencyIsoCode;
        String postalCode;
        String ownerType;
}
