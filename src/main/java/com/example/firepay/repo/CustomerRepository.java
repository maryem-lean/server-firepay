package com.example.firepay.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    Optional<CustomerEntity> findByCustomerId(UUID customerId);
    Optional<CustomerEntity> findByEndUserId(UUID endUserId);
}
