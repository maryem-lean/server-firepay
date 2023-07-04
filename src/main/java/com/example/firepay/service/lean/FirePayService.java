package com.example.firepay.service.lean;

import com.example.firepay.controller.dto.CustomerInvoiceResponse;
import com.example.firepay.controller.dto.CustomerRequest;
import com.example.firepay.controller.dto.CustomerResponse;
import com.example.firepay.controller.dto.InvoiceRequest;
import com.example.firepay.model.lean.CustomerAndUserResponse;
import com.example.firepay.repo.CustomerEntity;
import com.example.firepay.repo.CustomerRepository;
import com.example.firepay.repo.InvoiceEntity;
import com.example.firepay.repo.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FirePayService {

    private final LeanClient leanClient;
    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;

    public CustomerAndUserResponse createCustomerAndUser(CustomerRequest customerRequest) {
        var customer = leanClient.createCustomer(customerRequest.getCompanyName());
        var user = leanClient.createEndUser(customer.getCustomerId(), customerRequest.getName());
        customerRepository.save(mapToCustomerEntity(customerRequest, user.getCustomerId(), user.getId()));
        return CustomerAndUserResponse.builder()
                .customerId(user.getCustomerId())
                .endUserId(user.getId())
                .build();
    }

    public UUID createInvoice(InvoiceRequest invoiceRequest) {
        var customerEntity = customerRepository.findByCustomerId(invoiceRequest.getCustomerId()).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Customer not found"));
        var paymentIntent = leanClient.createPaymentIntent(invoiceRequest.getAmount(), invoiceRequest.getCurrency(), customerEntity.getCustomerId());
        var invoiceEntity = InvoiceEntity.builder()
                .customerId(customerEntity.getCustomerId())
                .endUserId(customerEntity.getEndUserId())
                .paymentIntentId(paymentIntent.getPaymentIntentId())
                .paymentStatus("INVOICE_GENERATED")
                .currency(invoiceRequest.getCurrency())
                .amount(invoiceRequest.getAmount())
                .issueDate(LocalDate.now())
                .dueDate(invoiceRequest.getDueDate())
                .updatedAt(Instant.now())
                .number(invoiceRequest.getNumber())
                .build();
        invoiceEntity = invoiceRepository.save(invoiceEntity);
        return invoiceEntity.getId();
    }

    public CustomerInvoiceResponse getInvoice(UUID invoiceId) {
        var invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Invoice not found"));
        var customer = customerRepository.findByCustomerId(invoice.getCustomerId()).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Customer not found"));
        return mapToCustomerInvoiceResponse(invoice, customer);
    }

    private CustomerInvoiceResponse mapToCustomerInvoiceResponse(InvoiceEntity invoice, CustomerEntity customer) {
        return CustomerInvoiceResponse.builder()
                .id(invoice.getId())
                .customer(mapToCustomerResponse(customer))
                .paymentIntentId(invoice.getPaymentIntentId())
                .paymentStatus(invoice.getPaymentStatus())
                .currency(invoice.getCurrency())
                .amount(invoice.getAmount())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .number(invoice.getNumber())
                .connected(customer.isConnected())
                .build();
    }

    public List<InvoiceEntity> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public List<InvoiceEntity> getCustomerInvoices(UUID customerId) {
        var customerEntity = getCustomer(customerId);
        return invoiceRepository.findByCustomerId(customerEntity.getCustomerId());
    }

    private CustomerEntity mapToCustomerEntity(CustomerRequest customerRequest, UUID customerId, UUID endUserId) {
        return CustomerEntity.builder()
                .customerId(customerId)
                .endUserId(endUserId)
                .name(customerRequest.getName())
                .address(customerRequest.getAddress())
                .companyName(customerRequest.getCompanyName())
                .taxId(customerRequest.getTaxId())
                .build();
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToCustomerResponse)
                .toList();
    }

    public CustomerEntity getCustomer(UUID id) {
        return customerRepository.findByCustomerId(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    private CustomerResponse mapToCustomerResponse(CustomerEntity customerEntity) {
        return CustomerResponse.builder()
                .id(customerEntity.getId())
                .customerId(customerEntity.getCustomerId())
                .endUserId(customerEntity.getEndUserId())
                .name(customerEntity.getName())
                .address(customerEntity.getAddress())
                .companyName(customerEntity.getCompanyName())
                .taxId(customerEntity.getTaxId())
                .build();
    }

    public void startAgain() {
        customerRepository.deleteAll();
        invoiceRepository.deleteAll();
    }
}
