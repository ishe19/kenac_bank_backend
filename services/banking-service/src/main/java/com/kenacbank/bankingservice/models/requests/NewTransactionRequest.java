package com.kenacbank.bankingservice.models.requests;



public record NewTransactionRequest(
    String accountNumber,
    String transactionType,
    Double amount,
    String description,
    String currencyCode
) {

}
