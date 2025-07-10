package com.kenacbank.authservice.services.interfaces;

import com.kenacbank.authservice.models.requests.LoginRequest;
import com.kenacbank.authservice.models.requests.PasswordResetRequest;
import com.kenacbank.authservice.models.requests.RegisterRequest;
import com.kenacbank.authservice.models.response.AuthResponse;
import com.kenacbank.authservice.models.response.GenericResponse;
import com.kenacbank.authservice.models.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<GenericResponse> register(RegisterRequest request);

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest);

    ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<GenericResponse> forgotPassword(String email);

    ResponseEntity<GenericResponse> resetPassword(PasswordResetRequest passwordResetRequest);

    ResponseEntity<GenericResponse> getLoggedInUser();
}
