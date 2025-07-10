package com.kenacbank.authservice.services.impl;

import com.kenacbank.authservice.client_user.ClientUserClient;
import com.kenacbank.authservice.models.entities.KenacUser;
import com.kenacbank.authservice.models.requests.ClientRegisterRequest;
import com.kenacbank.authservice.models.requests.LoginRequest;
import com.kenacbank.authservice.models.requests.PasswordResetRequest;
import com.kenacbank.authservice.models.requests.RegisterRequest;
import com.kenacbank.authservice.models.response.AuthResponse;
import com.kenacbank.authservice.models.response.GenericResponse;
import com.kenacbank.authservice.models.response.LoginResponse;
import com.kenacbank.authservice.repositories.KenacUserRepository;
import com.kenacbank.authservice.services.interfaces.AuthService;
import com.kenacbank.authservice.utils.UserType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private KenacUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final ClientUserClient userClient;

    @Override
    public ResponseEntity<GenericResponse> register(RegisterRequest request) {
        try{
            Optional<KenacUser> existingUser = userRepository.findByEmail(request.email());

            if(existingUser.isPresent()) {
                return ResponseEntity.badRequest().body(new GenericResponse("Email already exists"));
            }

            KenacUser newUser = KenacUser
                    .builder()
                    .name(request.firstName())
                    .surname(request.lastName())
                    .userCode(UUID.randomUUID().toString())
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .userType(UserType.CLIENT)
                    .build();

            KenacUser savedUser = userRepository.save(newUser);

            ClientRegisterRequest clientRegisterRequest = new ClientRegisterRequest(
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getSurname(),
                    savedUser.getEmail()
            );

            //ONCE SAVED I need to call the client service to complete the registration process
            var response = this.userClient.createClient(clientRegisterRequest).orElseThrow(
                    () -> new RuntimeException("Failed to create client in client service")
            );

            LOGGER.info("User registered successfully: {}", savedUser.getEmail());
            return ResponseEntity.ok(new GenericResponse("User registered successfully."));

        } catch (Exception e) {
            LOGGER.error("Error registering user: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new GenericResponse("Failed to register user."));
        }
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public ResponseEntity<GenericResponse> forgotPassword(String email) {
        return null;
    }

    @Override
    public ResponseEntity<GenericResponse> resetPassword(PasswordResetRequest passwordResetRequest) {
        return null;
    }
}
