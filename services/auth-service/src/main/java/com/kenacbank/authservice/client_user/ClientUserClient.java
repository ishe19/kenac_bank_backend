package com.kenacbank.authservice.client_user;

import com.kenacbank.authservice.models.requests.ClientRegisterRequest;
import com.kenacbank.authservice.models.response.GenericResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "client-service", url = "${application.config.client-url}")
public interface ClientUserClient {
    @PostMapping("/register")
    Optional<GenericResponse> createClient(@RequestBody ClientRegisterRequest request);

    @GetMapping("/is-blacklisted/{userId}")
    Optional<GenericResponse> isClientBlacklisted(@PathVariable Long userId);
}
