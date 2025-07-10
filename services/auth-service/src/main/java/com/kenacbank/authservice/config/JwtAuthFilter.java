package com.kenacbank.authservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenacbank.authservice.config.services.JwtService;
import com.kenacbank.authservice.repositories.UserTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final
    JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper mapper;
    private final UserTokenRepository userTokenRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userCode;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            userCode = jwtService.extractUsername(jwt);


            if (userCode != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userCode);
                boolean isTokenValid = checkTokenValidity(jwt);
                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("message", "Expired token");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getWriter(), errorDetails);
                LOGGER.error("Expired token: {}", e.getMessage());
            } else {
                LOGGER.error("Auth Filter Error: {}", e.getMessage());
                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("message", "Access Denied");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getWriter(), errorDetails);
            }
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkTokenValidity(String jwt) {
        boolean isTokenValid = false;
        isTokenValid = userTokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
        return isTokenValid;
    }

}