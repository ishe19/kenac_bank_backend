package com.kenacbank.bankingservice.models.entities;


import com.kenacbank.bankingservice.utils.Currency;
import com.kenacbank.bankingservice.utils.TransactionStatus;
import com.kenacbank.bankingservice.utils.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/**
 * Represents a bank transaction entity.
 *
 * <p>This entity is mapped to the "bank_transactions" table in the database and
 * includes details such as transaction code, reference, type, currency, associated
 * bank account, status, timestamps, and amount.</p>
 *
 * <p>Annotations are used to define the entity's properties, including ID generation,
 * column constraints, and relationships with other entities.</p>
 */
@Entity
@Table(name = "bank_transactions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String transactionCode;
    @Column(nullable = false, unique = true)
    private String reference;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @JoinColumn(name = "bank_account_id", nullable = false, referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private BankAccount bankAccount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double amount;
    private String description;

}
