package com.kenacbank.clientservice.services.implementations;

import com.kenacbank.clientservice.models.dto.ClientDto;
import com.kenacbank.clientservice.models.entities.Client;
import com.kenacbank.clientservice.models.requests.ClientRegisterRequest;
import com.kenacbank.clientservice.models.requests.ClientUpdateRequest;
import com.kenacbank.clientservice.models.responses.GenericResponse;
import com.kenacbank.clientservice.repositories.ClientRepository;
import com.kenacbank.clientservice.services.interfaces.ClientService;
import com.kenacbank.clientservice.utils.CustomerStatus;
import com.kenacbank.clientservice.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the ClientService interface providing client management operations.
 *
 * <p>This service handles client registration, retrieval, and updates. It interacts
 * with the ClientRepository to perform database operations and uses DtoMapper for
 * converting entities to DTOs. Logging is utilized for tracking operations and errors.</p>
 *
 * <ul>
 *   <li><b>registerClient</b>: Registers a new client, ensuring all required fields are present
 *       and the email is unique. Returns a success or failure response.</li>
 *   <li><b>getAllClients</b>: Retrieves all clients, maps them to DTOs, and returns them in a
 *       response. Handles cases where no clients are found.</li>
 *   <li><b>updateClient</b>: Updates an existing client's details based on the provided client
 *       code and update request. Returns a success or failure response.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final DtoMapper dtoMapper;


    /**
     * Registers a new client based on the provided registration request.
     *
     * <p>This method validates the request to ensure all required fields are present.
     * If the email already exists in the system, it returns a bad request response.
     * Otherwise, it creates a new client entity, saves it to the repository, and
     * returns a success response. In case of any exceptions, it logs the error and
     * returns a failure response.</p>
     *
     * @param request the client registration request containing user details
     * @return a ResponseEntity containing a GenericResponse indicating success or failure
     */
    @Override
    public ResponseEntity<GenericResponse> registerClient(ClientRegisterRequest request) {
        try {

            if (request.getUserId() == null || request.getFirstName() == null || request.getLastName() == null || request.getEmail() == null) {
                return ResponseEntity.badRequest().body(new GenericResponse("All fields are required", false));
            }

            Optional<Client> optionalClient = clientRepository.findByEmail(request.getEmail());

            if (optionalClient.isPresent()) {
                LOGGER.warn("Client with email {} already exists", request.getEmail());
                return ResponseEntity.badRequest().body(new GenericResponse("Client with this email already exists", false));
            }

            Client client = Client
                    .builder()
                    .userId(request.getUserId())
                    .clientCode(UUID.randomUUID().toString())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .status(CustomerStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();

            clientRepository.save(client);

            LOGGER.info("Client registered successfully: {}", client);

            return ResponseEntity.ok(new GenericResponse("Client registered successfully", true));

        } catch (Exception e) {
            LOGGER.error("Error registering client: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new GenericResponse("Failed to register client", false));
        }
    }

    /**
     * Retrieves all clients from the repository.
     *
     * <p>This method fetches all client entities, maps them to DTOs, and returns them
     * in a success response. If no clients are found, it returns a response indicating
     * that no clients were found. In case of any exceptions, it logs the error and
     * returns a failure response.</p>
     *
     * @return a ResponseEntity containing a GenericResponse with the list of clients
     *         or an error message if the retrieval fails
     */
    @Override
    public ResponseEntity<GenericResponse> getAllClients() {
        try {

            List<ClientDto> clients = clientRepository.findAll().stream().map(dtoMapper::mapToClientDto).toList();

            if (clients.isEmpty()) {
                LOGGER.info("No clients found");
                return ResponseEntity.ok(new GenericResponse("No clients found", true, clients));
            }

            LOGGER.info("Retrieved {} clients", clients.size());

            return ResponseEntity.ok(new GenericResponse("Clients retrieved successfully", true, clients));

        } catch (Exception e) {
            LOGGER.error("Error retrieving clients: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new GenericResponse("Failed to retrieve clients", false));
        }
    }

    /**
     * Updates an existing client based on the provided client code and update request.
     *
     * <p>This method checks if the client exists in the repository. If not, it returns
     * a bad request response. If the client exists, it updates the client's address
     * and phone number if provided, sets the updated timestamp, and saves the changes.
     * In case of any exceptions, it logs the error and returns a failure response.</p>
     *
     * @param clientCode the unique code of the client to be updated
     * @param request    the client update request containing new details
     * @return a ResponseEntity containing a GenericResponse indicating success or failure
     */
    @Override
    public ResponseEntity<GenericResponse> updateClient(String clientCode, ClientUpdateRequest request) {
        try{
            Optional<Client> optionalClient = clientRepository.findByClientCode(clientCode);

            if (optionalClient.isEmpty()) {
                LOGGER.warn("Client with code {} not found", clientCode);
                return ResponseEntity.badRequest().body(new GenericResponse("Client not found", false));
            }

            Client client = optionalClient.get();

            if (request.getAddress() != null) {
                client.setAddress(request.getAddress());
            }
            if (request.getPhoneNumber() != null) {
                client.setPhoneNumber(request.getPhoneNumber());
            }

            client.setUpdatedAt(LocalDateTime.now());

            clientRepository.save(client);

            LOGGER.info("Client updated successfully: {}", client);

            return ResponseEntity.ok(new GenericResponse("Client updated successfully", true));

        } catch (Exception e) {
            LOGGER.error("Error updating client: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new GenericResponse("Failed to update client", false));
        }
    }

    /**
     * Checks if a client exists in the repository based on the provided user ID.
     *
     * <p>This method queries the ClientRepository to determine if a client with the
     * specified user ID exists. It returns a ResponseEntity containing a GenericResponse
     * indicating whether the client exists or not.</p>
     *
     * @param userId the ID of the user to check for existence
     * @return a ResponseEntity containing a GenericResponse indicating the existence of the client
     */
    @Override
    public ResponseEntity<GenericResponse> checkIfClientExists(Long userId) {
        return clientRepository.findById(userId)
                .map(client -> ResponseEntity.ok(new GenericResponse("Client exists", true)))
                .orElseGet(() -> ResponseEntity.ok(new GenericResponse("Client does not exist", false)));
    }

    @Override
    public ResponseEntity<GenericResponse> getClientByUserId(Long userId) {
        return clientRepository.findById(userId)
                .map(client -> ResponseEntity.ok(new GenericResponse("Client found", true, dtoMapper.mapToClientDto(client))))
                .orElseGet(() -> ResponseEntity.ok(new GenericResponse("Client not found", false)));
    }

    @Override
    public ResponseEntity<GenericResponse> blacklistClient(String clientCode) {
        try{
            Optional<Client> optionalClient = clientRepository.findByClientCode(clientCode);

            if (optionalClient.isEmpty()) {
                LOGGER.warn("Client with code: {} not found", clientCode);
                return ResponseEntity.badRequest().body(new GenericResponse("Client not found", false));
            }

            Client client = optionalClient.get();
            client.setStatus(CustomerStatus.BLACKLISTED);
            client.setUpdatedAt(LocalDateTime.now());

            clientRepository.save(client);

            LOGGER.info("Client blacklisted successfully: {}", client);

            return ResponseEntity.ok(new GenericResponse("Client blacklisted successfully", true));

        } catch (Exception e) {
            LOGGER.error("Error blacklisting client: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new GenericResponse("Failed to blacklist client", false));
        }
    }
}
