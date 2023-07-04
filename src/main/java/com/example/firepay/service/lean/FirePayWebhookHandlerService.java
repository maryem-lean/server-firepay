package com.example.firepay.service.lean;

import com.example.firepay.repo.CustomerRepository;
import com.example.firepay.repo.InvoiceRepository;
import com.example.firepay.service.lean.webhook.EventMeta;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FirePayWebhookHandlerService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    public void handleWebhook(EventMeta webhook) {
        if (webhook.getType().equals("entity.created")) {
            processEntityCreatedEvent(webhook);
        } else if (webhook.getType().equals("payment.created") || webhook.getType().equals("payment.updated")) {
            processPaymentCreatedEvent(webhook);
        }
    }

    private void processEntityCreatedEvent(EventMeta webhook) {
        JsonNode data = webhook.getPayload();
        UUID customerId = UUID.fromString(data.get("customer_id").asText());
        customerRepository.findByCustomerId(customerId).ifPresent(customer -> {
            customer.setConnected(true);
            customerRepository.save(customer);
        });
    }

    private void processEntityCreatedEventFixLater(EventMeta webhook) {
        JsonNode data = webhook.getPayload();
        UUID endUserId = UUID.fromString(data.get("end_user_id").asText());
        customerRepository.findByEndUserId(endUserId).ifPresent(customer -> {
            customer.setConnected(true);
            customerRepository.save(customer);
        });
    }

    private void processPaymentCreatedEvent(EventMeta webhook) {
        JsonNode data = webhook.getPayload();
        UUID intentId = UUID.fromString(data.get("intent_id").asText());
        invoiceRepository.findByPaymentIntentId(intentId).ifPresent(invoice -> {
            invoice.setPaymentStatus(data.get("status").asText());
            invoiceRepository.save(invoice);
        });
    }
}
