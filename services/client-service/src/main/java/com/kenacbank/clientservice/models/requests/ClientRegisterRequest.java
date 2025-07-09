package com.kenacbank.clientservice.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientRegisterRequest {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
}
