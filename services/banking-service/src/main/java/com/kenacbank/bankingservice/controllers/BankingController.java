package com.kenacbank.bankingservice.controllers;

import com.kenacbank.bankingservice.models.reponses.GenericResponse;
import com.kenacbank.bankingservice.models.requests.NewTransactionRequest;
import com.kenacbank.bankingservice.models.requests.OpenAccountRequest;
import com.kenacbank.bankingservice.services.interfaces.BankingService;
import com.kenacbank.bankingservice.services.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banking")
@RequiredArgsConstructor
public class BankingController {

    private final BankingService bankingService;
    private final TransactionService transactionService;


    @PostMapping("/open-account")
    public ResponseEntity<GenericResponse> openClientAccount(@RequestBody OpenAccountRequest request) {
        return bankingService.openClientAccount(request);
    }

    @PutMapping("/approve-account/{accountNumber}")
    public ResponseEntity<GenericResponse> approveAccount(@PathVariable String accountNumber) {
        return bankingService.approveAccount(accountNumber);
    }

    @GetMapping("/accounts/{clientId}/balances")
    public ResponseEntity<GenericResponse> getClientAccountBalances(@PathVariable Long clientId) {
        return bankingService.getClientAccountBalances(clientId);
    }

    @PostMapping("/transactions")
    public ResponseEntity<GenericResponse> createTransaction(@RequestBody NewTransactionRequest request) {
        return transactionService.createTransaction(request);
    }


    @GetMapping("/transactions")
    public ResponseEntity<GenericResponse> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
}
