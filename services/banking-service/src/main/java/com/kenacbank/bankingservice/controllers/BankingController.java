package com.kenacbank.bankingservice.controllers;

import com.kenacbank.bankingservice.models.reponses.GenericResponse;
import com.kenacbank.bankingservice.models.requests.OpenAccountRequest;
import com.kenacbank.bankingservice.services.interfaces.BankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banking")
@RequiredArgsConstructor
public class BankingController {

    private final BankingService bankingService;


    @PostMapping("/open-account")
    public ResponseEntity<GenericResponse> openClientAccount(@RequestBody OpenAccountRequest request) {
        return bankingService.openClientAccunt(request);
    }
}
