package com.kenacbank.authservice.models.requests;

public record LoginRequest(
        String email,
        String password
) {
}
