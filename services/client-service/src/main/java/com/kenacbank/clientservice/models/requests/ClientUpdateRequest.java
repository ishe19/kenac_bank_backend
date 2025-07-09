package com.kenacbank.clientservice.models.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ClientUpdateRequest {
    private String address;
    private String phoneNumber;
}
