package com.kenacbank.authservice.models.requests;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
