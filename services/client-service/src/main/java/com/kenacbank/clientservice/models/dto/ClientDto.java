package com.kenacbank.clientservice.models.dto;

import java.time.LocalDateTime;

public record ClientDto(
        Long userId,
        String clientCode,
        String firstName,
        String lastName,
        String email,
        LocalDateTime createdAt
) {
}
