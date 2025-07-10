package com.kenacbank.bankingservice.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OpenAccountRequest {
    private Long clientId;
    private String accountType;
    private String currency;
    private Double initialDeposit;

}
