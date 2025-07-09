package com.kenacbank.clientservice.controller;

import com.kenacbank.clientservice.models.requests.ClientRegisterRequest;
import com.kenacbank.clientservice.models.requests.ClientUpdateRequest;
import com.kenacbank.clientservice.models.responses.GenericResponse;
import com.kenacbank.clientservice.services.interfaces.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private ClientService clientService;


    @PostMapping("/register")
    public ResponseEntity<GenericResponse> registerClient(@RequestBody ClientRegisterRequest request) {
        return clientService.registerClient(request);
    }

    @PutMapping("/update/{clientCode}")
    public ResponseEntity<GenericResponse> updateClient(@PathVariable String clientCode, @RequestBody ClientUpdateRequest request) {
        return clientService.updateClient(clientCode, request);
    }


    @GetMapping("/all")
    public ResponseEntity<GenericResponse> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/exists/{userId}")
    public ResponseEntity<GenericResponse> checkIfClientExists(@PathVariable Long userId) {
        return clientService.checkIfClientExists(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GenericResponse> getClientByUserId(@PathVariable Long userId) {
        return clientService.getClientByUserId(userId);
    }


    @PutMapping("/blacklist/{clientCode}")
    public ResponseEntity<GenericResponse> blacklistClient(@PathVariable String clientCode) {
        return clientService.blacklistClient(clientCode);
    }

}
