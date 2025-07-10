package com.kenacbank.authservice.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class GenericResponse {
    private String message;
    private Object data;
    private boolean success;

    public GenericResponse(String message) {
        this.message = message;
    }

    public GenericResponse(String message, Object data) {
        this.message = message;
        this.data = data;
        this.success = true;
    }
}
