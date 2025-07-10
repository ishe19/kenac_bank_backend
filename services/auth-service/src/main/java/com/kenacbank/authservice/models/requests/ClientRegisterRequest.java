package com.kenacbank.authservice.models.requests;


public record ClientRegisterRequest(
        Long userId,
        String firstName,
        String lastName,
        String email
) {
}
