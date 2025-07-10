package com.kenacbank.bankingservice.repositories;

import com.kenacbank.bankingservice.models.entities.BankAccount;
import com.kenacbank.bankingservice.models.entities.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

    Optional<BankTransaction> findByBankAccount(BankAccount bankAccount);
}
