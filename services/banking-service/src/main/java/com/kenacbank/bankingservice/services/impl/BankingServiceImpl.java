package com.kenacbank.bankingservice.services.impl;

import com.kenacbank.bankingservice.models.entities.BankAccount;
import com.kenacbank.bankingservice.models.reponses.GenericResponse;
import com.kenacbank.bankingservice.models.requests.OpenAccountRequest;
import com.kenacbank.bankingservice.repositories.BankAccountRepository;
import com.kenacbank.bankingservice.services.interfaces.BankingService;
import com.kenacbank.bankingservice.utils.BankAccountStatus;
import com.kenacbank.bankingservice.utils.BankAccountType;
import com.kenacbank.bankingservice.utils.Currency;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankingServiceImpl implements BankingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankingServiceImpl.class);

    private final BankAccountRepository bankAccountRepository;

    /**
     * Opens a new bank account for a client based on the provided request details.
     * Validates the request parameters and generates a unique account number.
     * Saves the new account to the repository and returns a response indicating
     * the success or failure of the operation.
     *
     * @param request the request containing client ID, account type, currency, and initial deposit
     * @return ResponseEntity containing a GenericResponse with a success message and HTTP status
     */
    @Override
    public ResponseEntity<GenericResponse> openClientAccount(OpenAccountRequest request) {
        try{
            // Validating my request parameters
            if (request.getClientId() == null || request.getAccountType() == null || request.getCurrency() == null) {
                return ResponseEntity.badRequest().body(new GenericResponse("Invalid request parameters"));
            }

            LOGGER.info("Opening account for client ID: {}", request.getClientId());
            BankAccount newAccount = BankAccount
                    .builder()
                    .accountNumber(generateAccountNumber())
                    .accountType(BankAccountType.valueOf(request.getAccountType()))
                    .currency(Currency.valueOf(request.getCurrency()))
                    .balance(request.getInitialDeposit())
                    .status(BankAccountStatus.PENDING)
                    .clientId(request.getClientId())
                    .createdAt(LocalDateTime.now())
                    .build();

            bankAccountRepository.save(newAccount);

            LOGGER.info("Account opened successfully for client ID: {}", request.getClientId());
            return new ResponseEntity<>(new GenericResponse("Account opened successfully"), HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new GenericResponse("Failed to open account: " + e.getMessage()));
        }
    }


    /**
     * Approves a bank account by updating its status to ACTIVE based on the provided account number.
     * Searches for the account in the repository and updates its status if found.
     * Returns a response indicating the success or failure of the operation.
     *
     * @param accountNumber the unique identifier of the bank account to be approved
     * @return ResponseEntity containing a GenericResponse with a success message and HTTP status,
     *         or an error message if the account is not found or an exception occurs
     */
    @Override
    public ResponseEntity<GenericResponse> approveAccount(String accountNumber) {
        try{
            // Find the account by account number
            BankAccount account = bankAccountRepository.findAll().stream()
                    .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                    .findFirst()
                    .orElse(null);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new GenericResponse("Account not found"));
            }

            // Update the account status to APPROVED
            account.setStatus(BankAccountStatus.ACTIVE);
            bankAccountRepository.save(account);

            LOGGER.info("Account {} approved successfully", accountNumber);
            return ResponseEntity.ok(new GenericResponse("Account approved successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new GenericResponse("Failed to approve account: " + e.getMessage()));
        }
    }

    /**
     * Retrieves the account balances for a specific client based on their client ID.
     * Fetches all accounts associated with the client and constructs a response
     * containing the account numbers and their respective balances.
     *
     * @param clientId the unique identifier of the client whose account balances are to be fetched
     * @return ResponseEntity containing a GenericResponse with the account balances or an error message
     */
    @Override
    public ResponseEntity<GenericResponse> getClientAccountBalances(Long clientId) {
        try{
            if (clientId == null) {
                return ResponseEntity.badRequest().body(new GenericResponse("Invalid client ID"));
            }

            LOGGER.info("Fetching account balances for client ID: {}", clientId);
            List<BankAccount> accounts = bankAccountRepository.findByClientId(clientId);

            if (accounts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new GenericResponse("No accounts found for the client"));
            }

            StringBuilder balances = new StringBuilder();
            for (BankAccount account : accounts) {
                balances.append(String.format("Account Number: %s, Balance: %.2f %s%n",
                        account.getAccountNumber(), account.getBalance(), account.getCurrency()));
            }

            LOGGER.info("Fetched account balances for client ID: {}", clientId);
            return ResponseEntity.ok(new GenericResponse(balances.toString()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new GenericResponse("Failed to fetch account balances: " + e.getMessage()));
        }
    }

    /**
     * Generates a unique 12-digit account number for a new bank account.
     * The account number is created by incrementing the highest existing
     * account ID in the repository and formatting it with a "KENAC_" prefix.
     *
     * @return a formatted string representing the new account number
     */
    private String generateAccountNumber() {
        //generating a 12-digit account number, in incremental order, first get last db entry from db
        // and increment it by 1
        long lastAccountId = bankAccountRepository.findAll().stream()
                .mapToLong(BankAccount::getId)
                .max()
                .orElse(0L);

        //12-digit account number
        String accountNumber = String.format("KENAC_%012d", lastAccountId + 1);
        LOGGER.info("Generated account number: {}", accountNumber);
        return accountNumber;
    }
}
