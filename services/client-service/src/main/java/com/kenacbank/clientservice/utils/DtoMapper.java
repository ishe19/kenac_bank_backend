package com.kenacbank.clientservice.utils;

import com.kenacbank.clientservice.models.dto.ClientDto;
import com.kenacbank.clientservice.models.entities.Client;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {


    public ClientDto mapToClientDto(Client client) {
        return new ClientDto(
                client.getUserId(),
                client.getClientCode(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getCreatedAt()
        );
    }
}
