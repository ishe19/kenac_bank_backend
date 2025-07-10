package com.kenacbank.bankingservice.services.impl;

import com.kenacbank.bankingservice.models.reponses.GenericResponse;
import com.kenacbank.bankingservice.models.requests.OpenAccountRequest;
import com.kenacbank.bankingservice.services.interfaces.BankingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankingServiceImpl implements BankingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankingServiceImpl.class);
    @Override
    public ResponseEntity<GenericResponse> openClientAccunt(OpenAccountRequest request) {
        try{
            //stopping here for now

            LOGGER.info("Opening account for client ID: {}", request.getClientId());

            return new ResponseEntity<>(new GenericResponse(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new GenericResponse("Failed to open account: " + e.getMessage()));
        }
    }
}
