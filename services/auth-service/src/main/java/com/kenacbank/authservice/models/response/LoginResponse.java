package com.kenacbank.authservice.models.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginResponse extends GenericResponse {
    private String accessToken;
    private String refreshToken;
    private String message;

    public LoginResponse(String message) {
        this.message = message;
    }

    public LoginResponse(String message, String accessToken, String refreshToken) {
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public LoginResponse(String message, boolean success, String accessToken, String refreshToken) {
        this.message = message;
        super.setSuccess(success);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public LoginResponse(String message, Object data) {
        super(message, data);
    }
}
