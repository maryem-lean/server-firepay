package com.example.firepay.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {

    Optional<InvoiceEntity> findByPaymentIntentId(UUID intentId);

    List<InvoiceEntity> findByCustomerId(UUID customerId);
}
