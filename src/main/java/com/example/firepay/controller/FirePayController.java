package com.example.firepay.controller;

import com.example.firepay.controller.dto.CustomerInvoiceResponse;
import com.example.firepay.service.lean.FirePayService;
import com.example.firepay.service.lean.FirePayWebhookHandlerService;
import com.example.firepay.service.lean.webhook.EventMeta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FirePayController {

    private final FirePayService firePayService;
    private final FirePayWebhookHandlerService firePayWebhookHandlerService;

    @GetMapping("/invoice")
    public CustomerInvoiceResponse invoiceDetails(@RequestParam String invoiceId) {
        return getInvoice(invoiceId);
    }

    @GetMapping("/invoices/{invoiceId}")
    public CustomerInvoiceResponse getInvoice(@PathVariable String invoiceId) {
        log.info("Fetch invoice: {}", invoiceId);
        return firePayService.getInvoice(UUID.fromString(invoiceId));
    }

    @PostMapping("/webhook")
    public void captureWebhook(@RequestBody EventMeta webhook) {
        log.info("webhook: {}", webhook);
        firePayWebhookHandlerService.handleWebhook(webhook);
    }

    @DeleteMapping("/start-again")
    public void startAgain() {
        log.info("delete everything and start again");
        firePayService.startAgain();
    }
}
