package com.kenacbank.authservice.services.impl;

import com.kenacbank.authservice.client_user.ClientUserClient;
import com.kenacbank.authservice.config.services.JwtService;
import com.kenacbank.authservice.models.entities.KenacUser;
import com.kenacbank.authservice.models.entities.UserToken;
import com.kenacbank.authservice.models.requests.ClientRegisterRequest;
import com.kenacbank.authservice.models.requests.LoginRequest;
import com.kenacbank.authservice.models.requests.PasswordResetRequest;
import com.kenacbank.authservice.models.requests.RegisterRequest;
import com.kenacbank.authservice.models.response.AuthResponse;
import com.kenacbank.authservice.models.response.GenericResponse;
import com.kenacbank.authservice.models.response.LoginResponse;
import com.kenacbank.authservice.repositories.KenacUserRepository;
import com.kenacbank.authservice.repositories.UserTokenRepository;
import com.kenacbank.authservice.services.interfaces.AuthService;
import com.kenacbank.authservice.utils.TokenType;
import com.kenacbank.authservice.utils.UserType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final KenacUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientUserClient userClient;
    private final UserTokenRepository userTokenRepository;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<GenericResponse> register(RegisterRequest request) {
        try {
            Optional<KenacUser> existingUser = userRepository.findByEmail(request.email());

            if (existingUser.isPresent()) {
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
        try {
            Optional<KenacUser> userOptional = userRepository.findByEmail(loginRequest.email());

            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(new LoginResponse("Invalid email or password."));
            }

            KenacUser user = userOptional.get();

            if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
                return ResponseEntity.badRequest().body(new LoginResponse("Invalid email or password."));
            }

            if(user.getUserType() == UserType.CLIENT) {
                GenericResponse response = userClient.isClientBlacklisted(user.getId())
                        .orElseThrow(() -> new RuntimeException("Failed to check client blacklist status"));

                if(response.isSuccess()){
                    return ResponseEntity.badRequest().body(new LoginResponse("User is blacklisted and cannot login."));
                }
            }

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("userType", user.getUserType().name());
            String jwtToken = jwtService.generateToken(user, extraClaims);
            String refreshToken = jwtService.generateRefreshToken(user);
            revokeUserTokens(user);
            saveUserToken(jwtToken, user);

            LOGGER.info("User logged in successfully: {}", user.getEmail());
            return ResponseEntity.ok(new LoginResponse("Login successful", true, jwtToken, refreshToken));
        } catch (Exception e) {
            LOGGER.error("Error logging in user: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new LoginResponse("Failed to login user."));
        }
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

    @Override
    public ResponseEntity<GenericResponse> getLoggedInUser() {
        try{
            String userCode = getContext().getAuthentication().getName();

            Optional<KenacUser> loggedInUser = userRepository.findByUserCode(userCode);
            return loggedInUser.map(kenacUser -> ResponseEntity.ok(new GenericResponse("Logged in user retrieved successfully.", kenacUser)))
                    .orElseGet(() -> ResponseEntity.status(401).body(new GenericResponse("No user is currently logged in.")));

        } catch (Exception e) {
            LOGGER.error("Error retrieving logged in user: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new GenericResponse("Failed to retrieve logged in user."));
        }
    }


    private void revokeUserTokens(KenacUser user) {
        var validUserTokens = userTokenRepository.findByUserIdAndExpiredFalseAndRevokedFalse(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        userTokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(String jwtToken, KenacUser user) {
        Optional<UserToken> existingToken = userTokenRepository.findByToken(jwtToken);

        if (existingToken.isPresent()) {
            // Update existing token, assuming token is still associated with the same user
            existingToken.get().setExpired(false);
            existingToken.get().setRevoked(false);
            userTokenRepository.save(existingToken.get());
        } else {
            // Token doesn't exist, create a new one
            var userToken = UserToken
                    .builder()
                    .token(jwtToken)
                    .user(user)
                    .tokenType(TokenType.BEARER)
                    .expired(false)
                    .revoked(false)
                    .build();
            userTokenRepository.save(userToken);
        }
    }
}
