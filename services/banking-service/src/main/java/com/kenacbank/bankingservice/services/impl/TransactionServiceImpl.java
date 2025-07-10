package com.kenacbank.bankingservice.services.impl;

import com.kenacbank.bankingservice.models.dto.TransactionDto;
import com.kenacbank.bankingservice.models.entities.BankAccount;
import com.kenacbank.bankingservice.models.entities.BankTransaction;
import com.kenacbank.bankingservice.models.reponses.GenericResponse;
import com.kenacbank.bankingservice.models.requests.NewTransactionRequest;
import com.kenacbank.bankingservice.repositories.BankAccountRepository;
import com.kenacbank.bankingservice.repositories.BankTransactionRepository;
import com.kenacbank.bankingservice.services.interfaces.TransactionService;
import com.kenacbank.bankingservice.utils.BankAccountStatus;
import com.kenacbank.bankingservice.utils.DtoMapper;
import com.kenacbank.bankingservice.utils.TransactionStatus;
import com.kenacbank.bankingservice.utils.TransactionType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final BankTransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final DtoMapper dtoMapper;

    /**
     * Creates a new bank transaction based on the provided request details.
     * Validates the request parameters, retrieves the associated bank account,
     * and saves the transaction to the repository.
     *
     * @param request the request containing account number, amount, description, and transaction type
     * @return ResponseEntity containing a GenericResponse with a success message or error details
     */
    @Override
    public ResponseEntity<GenericResponse> createTransaction(NewTransactionRequest request) {
        try {
            // Validate request
            if (request.accountNumber() == null || request.amount() <= 0 || request.transactionType() == null) {
                return ResponseEntity.badRequest().body(new GenericResponse("Invalid request parameters"));
            }

            BankAccount account = accountRepository.findByAccountNumber(request.accountNumber())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            TransactionType transactionType = TransactionType.valueOf(request.transactionType().toUpperCase());
            // Check if the account is active

            if (account.getStatus() != BankAccountStatus.ACTIVE) {
                return ResponseEntity.badRequest().body(new GenericResponse("Account is not active"));
            }

            if (transactionType == TransactionType.WITHDRAWAL && account.getBalance() < request.amount()) {
                return ResponseEntity.badRequest().body(new GenericResponse("Insufficient balance for withdrawal"));
            }

            var transaction = BankTransaction
                    .builder()
                    .amount(request.amount())
                    .description(request.description())
                    .transactionCode(UUID.randomUUID().toString())
                    .transactionType(transactionType)
                    .createdAt(LocalDateTime.now())
                    .bankAccount(account)
                    .amount(transactionType == TransactionType.WITHDRAWAL ? -request.amount() : request.amount())
                    .status(transactionType == TransactionType.WITHDRAWAL ? TransactionStatus.PENDING : TransactionStatus.COMPLETED)
                    .build();

            BankTransaction saved = transactionRepository.save(transaction);

            LOGGER.info("Transaction created successfully: {}", saved.getId());
            return ResponseEntity.ok(new GenericResponse("Transaction created successfully"));
        } catch (Exception e) {
            LOGGER.error("Error creating transaction: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new GenericResponse("Failed to create transaction"));
        }
    }


    /**
     * Retrieves all transactions from the repository.
     * If no transactions are found, returns a message indicating so.
     *
     * @return ResponseEntity containing a GenericResponse with the list of transactions or an error message
     */
    @Override
    public ResponseEntity<GenericResponse> getAllTransactions() {
        try {
            List<TransactionDto> transactions = transactionRepository.findAll()
                    .stream().map(dtoMapper::mapToTransactionDto).toList();
            if (transactions.isEmpty()) {
                return ResponseEntity.ok(new GenericResponse("No transactions found"));
            }
            LOGGER.info("Retrieved {} transactions", transactions.size());
            return ResponseEntity.ok(new GenericResponse("Transactions retrieved successfully", transactions));
        } catch (Exception e) {
            LOGGER.error("Error retrieving transactions: {}", e.getMessage());
            return ResponseEntity.status(500).body(new GenericResponse("Failed to retrieve transactions"));
        }
    }
}
