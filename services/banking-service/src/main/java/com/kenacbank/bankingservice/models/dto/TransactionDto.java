package com.kenacbank.bankingservice.models.dto;

import com.kenacbank.bankingservice.utils.Currency;
import com.kenacbank.bankingservice.utils.TransactionType;

import java.time.LocalDateTime;

public record TransactionDto(
        String accountNumber,
        TransactionType transactionType,
        Double amount,
        String description,
        Currency currencyCode,
        String transactionCode,
        LocalDateTime transactionDateTime

) {
}
