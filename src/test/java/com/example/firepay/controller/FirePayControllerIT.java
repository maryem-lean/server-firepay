package com.example.firepay.controller;

import com.example.firepay.repo.CustomerEntity;
import com.example.firepay.repo.CustomerRepository;
import com.example.firepay.repo.InvoiceEntity;
import com.example.firepay.repo.InvoiceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FirePayControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();
        invoiceRepository.deleteAll();
    }

    @Test
    void testEntityWebhookConsumer() throws Exception {

        customerRepository.save(CustomerEntity.builder()
            .customerId(UUID.fromString("60781b5f-cc13-47e0-8fd5-1c014adee664"))
            .endUserId(UUID.fromString("3838e75a-5b58-431e-9614-10a0ee536746"))
            .name("potato")
            .companyName("company")
            .address("address")
            .taxId("taxid")
            .connected(false)
            .build());

        var entityCreatedWebhook = """
                {
                  "payload": {
                    "id": "ef80550a-c666-40f4-bc55-eea1e92984a2",
                    "customer_id": "60781b5f-cc13-47e0-8fd5-1c014adee664",
                    "app_user_id": "53e7f49d-7222-4c9b-9364-f85cce1cf1bc",
                    "permissions": ["transactions", "balance", "identity", "accounts"],
                    "bank_details": {
                      "name": "Lean 1st Corporate Mockbank",
                      "identifier": "LEAN_SME_MB1_UAE",
                      "bank_type": "SME",
                      "logo": "https://cdn.leantech.me/img/banks/white-lean.png",
                      "main_color": "#1beb76",
                      "background_color": "#ffffff"
                    },
                    "end_user_id": "3838e75a-5b58-431e-9614-10a0ee536746"
                  },
                  "type": "entity.created",
                  "message": "An entity object has been created.",
                  "timestamp": "2023-05-31T16:30:10.034909533Z",
                  "event_id": "b7bd92c9-fc28-496c-8438-5c1d66147dfe"
                }
                """;

        mockMvc.perform(post("/webhook")
            .contentType("application/json")
            .content(entityCreatedWebhook))
            .andExpect(status().isOk());

        Assertions.assertThat(customerRepository.findAll()).hasSize(1);

        customerRepository.findByEndUserId(UUID.fromString("3838e75a-5b58-431e-9614-10a0ee536746"))
            .ifPresent(customer -> {
                assert customer.isConnected();
            });
    }

    @Test
    void testPaymentCreatedWebhookConsumer() throws Exception {
        customerRepository.save(CustomerEntity.builder()
                .customerId(UUID.fromString("60781b5f-cc13-47e0-8fd5-1c014adee664"))
                .name("potato")
                .companyName("company")
                .address("address")
                .taxId("taxid")
                .connected(false)
                .build());

        invoiceRepository.save(InvoiceEntity.builder()
                .customerId(UUID.fromString("60781b5f-cc13-47e0-8fd5-1c014adee664"))
                .endUserId(UUID.fromString("3838e75a-5b58-431e-9614-10a0ee536746"))
                .paymentIntentId(UUID.fromString("18630490-2359-4d2f-8529-8dd95ffbe106"))
                .paymentStatus("STATUS")
                .build());

        var paymentCreatedWebhook = """
                {
                  "payload": {
                    "id": "0f40c115-0e54-4d34-a704-7bd50aadaaaa",
                    "customer_id": "60781b5f-cc13-47e0-8fd5-1c014adee664",
                    "intent_id": "18630490-2359-4d2f-8529-8dd95ffbe106",
                    "status": "AWAITING_AUTHORIZATION",
                    "amount": 10,
                    "currency": "AED",
                    "bank_transaction_reference": "45748549",
                    "end_user_id": "3838e75a-5b58-431e-9614-10a0ee536746"
                  },
                  "type": "payment.created",
                  "message": "A payment object has been created.",
                  "timestamp": "2023-05-31T16:30:22.183401173Z",
                  "event_id": "054ff0be-32c4-4c3d-b3da-ec223a4ddbd5"
                }
                """;

        mockMvc.perform(post("/webhook")
                .contentType("application/json")
                .content(paymentCreatedWebhook))
                .andExpect(status().isOk());

        Assertions.assertThat(invoiceRepository.findAll()).hasSize(1);

        invoiceRepository.findByPaymentIntentId(UUID.fromString("18630490-2359-4d2f-8529-8dd95ffbe106"))
                .ifPresent(invoice -> {
                    assert invoice.getPaymentStatus().equals("AWAITING_AUTHORIZATION");
                });
    }
}
