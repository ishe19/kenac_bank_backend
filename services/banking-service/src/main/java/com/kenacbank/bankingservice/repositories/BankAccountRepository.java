package com.kenacbank.bankingservice.repositories;

import com.kenacbank.bankingservice.models.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findByClientId(Long id);

    Optional<BankAccount> findByAccountNumber(String accountNumber);
}
