package com.kenacbank.clientservice.services.interfaces;

import com.kenacbank.clientservice.models.requests.OpenAccountRequest;
import com.kenacbank.clientservice.models.responses.GenericResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "banking-service", url = "${application.config.banking-url}")
public interface BankClient {

    @PostMapping("/banking/open-account")
    Optional<GenericResponse> openClientAccount(@RequestBody OpenAccountRequest request);
}
