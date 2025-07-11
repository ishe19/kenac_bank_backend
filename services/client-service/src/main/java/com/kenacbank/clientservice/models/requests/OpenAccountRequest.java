package com.kenacbank.clientservice.models.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OpenAccountRequest {
    private Long clientId;
    private String accountType;
    private String currency;
    private Double initialDeposit;
}
