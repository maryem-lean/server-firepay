package com.example.firepay.service.lean;

import com.example.firepay.model.lean.Customer;
import com.example.firepay.model.lean.EndUser;
import com.example.firepay.model.lean.PaymentIntentRequest;
import com.example.firepay.model.lean.PaymentIntentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LeanClient {

    static final String CUSTOMERS_URL = "/customers/v1";
    static final String END_USER_URL = CUSTOMERS_URL + "/end-users";
    static final String PAYMENT_INTENT_URL = "/payments/v1/intents";

    @Value("${app.token}")
    private String appToken;

    @Value("${lean.base.host}")
    private String leanHost;

    private final RestTemplate restTemplate;

    public Customer createCustomer(String companyName) {
        var request = Customer.builder().appUserId(companyName).build();
        return restTemplate.postForObject(leanHost + CUSTOMERS_URL, getHttpEntity(request), Customer.class);
    }

    public EndUser createEndUser(UUID customerId, String name) {
        var request = EndUser.builder().customerId(customerId).reference(name).build();
        return restTemplate.postForObject(leanHost + END_USER_URL, getHttpEntity(request), EndUser.class);
    }

    public PaymentIntentResponse createPaymentIntent(BigDecimal amount, String currency, UUID customerId) {
        var request = PaymentIntentRequest.builder()
                .amount(amount)
                .currency(currency)
                .customerId(customerId)
                .build();
        return restTemplate.postForObject(leanHost + PAYMENT_INTENT_URL, getHttpEntity(request), PaymentIntentResponse.class);
    }

    private HttpEntity<?> getHttpEntity(Object request) {
        return new HttpEntity<>(request, getDefaultHeaders());
    }

    private HttpHeaders getDefaultHeaders() {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("lean-app-token", appToken);
        return headers;
    }

}
