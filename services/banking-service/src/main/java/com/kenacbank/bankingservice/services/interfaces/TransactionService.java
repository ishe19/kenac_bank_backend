package com.kenacbank.bankingservice.services.interfaces;

import com.kenacbank.bankingservice.models.reponses.GenericResponse;
import com.kenacbank.bankingservice.models.requests.NewTransactionRequest;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<GenericResponse> createTransaction(NewTransactionRequest request);

    ResponseEntity<GenericResponse> getAllTransactions();
}
