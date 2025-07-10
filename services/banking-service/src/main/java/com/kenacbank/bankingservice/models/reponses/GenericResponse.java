package com.kenacbank.bankingservice.models.reponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenericResponse {
    private String message;
    private boolean success;
    private Object data;

    public GenericResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public GenericResponse(String message, Object data) {
        this.message = message;
        this.success = true;
        this.data = data;
    }

    public GenericResponse(String message) {
        this.message = message;
    }
}
