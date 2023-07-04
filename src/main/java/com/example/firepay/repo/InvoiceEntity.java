package com.example.firepay.repo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@Entity
@Table(name = "invoice")
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    UUID customerId; //TODO might include customer
    UUID endUserId; // FIXME I don't think I need this
    UUID paymentIntentId;
    String paymentStatus;
    String currency;
    BigDecimal amount;
    LocalDate issueDate;
    LocalDate dueDate;
    Instant updatedAt;
    String number;
}
