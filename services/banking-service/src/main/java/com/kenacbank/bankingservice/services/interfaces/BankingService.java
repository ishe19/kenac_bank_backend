package com.kenacbank.bankingservice.services.interfaces;

import com.kenacbank.bankingservice.models.reponses.GenericResponse;
import com.kenacbank.bankingservice.models.requests.OpenAccountRequest;
import org.springframework.http.ResponseEntity;

public interface BankingService {
    ResponseEntity<GenericResponse> openClientAccount(OpenAccountRequest request);

    ResponseEntity<GenericResponse> approveAccount(String accountNumber);

    ResponseEntity<GenericResponse> getClientAccountBalances(Long clientId);
}
