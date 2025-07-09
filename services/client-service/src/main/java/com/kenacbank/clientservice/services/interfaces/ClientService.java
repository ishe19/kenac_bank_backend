package com.kenacbank.clientservice.services.interfaces;

import com.kenacbank.clientservice.models.requests.ClientRegisterRequest;
import com.kenacbank.clientservice.models.requests.ClientUpdateRequest;
import com.kenacbank.clientservice.models.responses.GenericResponse;
import org.springframework.http.ResponseEntity;

public interface ClientService {
    ResponseEntity<GenericResponse> registerClient(ClientRegisterRequest request);

    ResponseEntity<GenericResponse> getAllClients();

    ResponseEntity<GenericResponse> updateClient(String clientCode, ClientUpdateRequest request);

    ResponseEntity<GenericResponse> checkIfClientExists(Long userId);

    ResponseEntity<GenericResponse> getClientByUserId(Long userId);

    ResponseEntity<GenericResponse> blacklistClient(String clientCode);
}
