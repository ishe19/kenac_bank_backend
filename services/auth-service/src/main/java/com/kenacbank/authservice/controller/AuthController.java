package com.kenacbank.authservice.controller;


import com.kenacbank.authservice.models.requests.LoginRequest;
import com.kenacbank.authservice.models.requests.PasswordResetRequest;
import com.kenacbank.authservice.models.requests.RegisterRequest;
import com.kenacbank.authservice.models.response.AuthResponse;
import com.kenacbank.authservice.models.response.GenericResponse;
import com.kenacbank.authservice.models.response.LoginResponse;
import com.kenacbank.authservice.services.interfaces.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.refreshToken(request, response);
    }


    @GetMapping("/forgot-password")
    public ResponseEntity<GenericResponse> forgotPassword(@RequestParam String email){
        return authService.forgotPassword(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<GenericResponse> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest){
        return authService.resetPassword(passwordResetRequest);
    }


}
