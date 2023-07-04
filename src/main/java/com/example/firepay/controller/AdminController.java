package com.example.firepay.controller;

import com.example.firepay.controller.dto.CustomerRequest;
import com.example.firepay.controller.dto.CustomerResponse;
import com.example.firepay.controller.dto.InvoiceRequest;
import com.example.firepay.repo.CustomerEntity;
import com.example.firepay.repo.InvoiceEntity;
import com.example.firepay.service.lean.FirePayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {

    private final FirePayService firePayService;

    @PostMapping("/customers")
    String createCustomer(@RequestBody CustomerRequest customer) {
        log.info("Create customer: {}", customer);
        firePayService.createCustomerAndUser(customer);
        return "customer created";
    }
    
    @GetMapping("/customers")
    List<CustomerResponse> fetchCustomers() {
        log.info("Fetch customers");
        return firePayService.getAllCustomers();
    }

    @GetMapping("/customers/{customerId}")
    CustomerEntity fetchCustomer(@PathVariable String customerId) {
        log.info("Fetch customer by id {}", customerId);
        return firePayService.getCustomer(UUID.fromString(customerId));
    }

    @GetMapping("/customers/{customerId}/invoices")
    List<InvoiceEntity> fetchCustomerInvoices(@PathVariable String customerId) {
        log.info("Fetch customer invoices for customer id {}", customerId);
        return firePayService.getCustomerInvoices(UUID.fromString(customerId));
    }

    @PostMapping("/invoices")
    String createInvoice(@RequestBody InvoiceRequest invoice) {
        log.info("Create invoice: {}", invoice);
        return firePayService.createInvoice(invoice).toString();
    }

    @GetMapping("/invoices")
    List<InvoiceEntity> getAllInvoices() {
        log.info("Fetch invoices");
        return firePayService.getAllInvoices();
    }

}
