package com.kenacbank.clientservice.repositories;

import com.kenacbank.clientservice.models.entities.Client;
import com.kenacbank.clientservice.utils.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByClientCode(String clientCode);

    Optional<Client> findByStatus(CustomerStatus customerStatus);
}
