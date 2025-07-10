package com.kenacbank.bankingservice.utils;

import com.kenacbank.bankingservice.models.dto.TransactionDto;
import com.kenacbank.bankingservice.models.entities.BankTransaction;
import org.springframework.stereotype.Service;

@Service
public class DtoMapper {


    public TransactionDto mapToTransactionDto(BankTransaction transaction){
        return new TransactionDto(
                transaction.getBankAccount().getAccountNumber(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getCurrency(),
                transaction.getTransactionCode(),
                transaction.getCreatedAt()
        );
    }
}
