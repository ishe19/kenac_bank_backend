package com.kenacbank.bankingservice.models.entities;


import com.kenacbank.bankingservice.utils.BankAccountStatus;
import com.kenacbank.bankingservice.utils.BankAccountType;
import com.kenacbank.bankingservice.utils.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/**
 * Represents a bank account entity with details such as account number, client ID,
 * account type, balance, and currency. This entity is mapped to the "bank_accounts"
 * table in the database.
 *
 * <p>Includes metadata such as creation and update timestamps, and the ID of the
 * approver. Utilizes JPA annotations for ORM mapping</p>
 */
@Entity
@Table(name = "bank_accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String accountNumber;
    @Column(nullable = false)
    private Long clientId;
    @Enumerated(EnumType.STRING)
    private BankAccountType accountType;
    @Column(nullable = false)
    private Double balance;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private BankAccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
    private Long approvedBy;

}
