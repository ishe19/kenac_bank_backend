package com.kenacbank.clientservice.models.entities;

import com.kenacbank.clientservice.utils.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/**
 * Represents a client entity in the system.
 *
 * <p>This entity is mapped to the "clients" table in the database and includes
 * various attributes such as client code, user ID, name, email, status, and
 * timestamps for creation and updates. It utilizes Lombok annotations for
 * boilerplate code reduction and JPA annotations for ORM mapping.</p>
 *
 * <ul>
 *   <li><b>id</b>: Unique identifier for the client.</li>
 *   <li><b>clientCode</b>: Unique code assigned to the client, cannot be null.</li>
 *   <li><b>userId</b>: Identifier for the associated user.</li>
 *   <li><b>firstName</b>: First name of the client.</li>
 *   <li><b>lastName</b>: Last name of the client.</li>
 *   <li><b>email</b>: Unique email address of the client, cannot be null.</li>
 *   <li><b>status</b>: Current status of the client, represented by the CustomerStatus enum.</li>
 *   <li><b>approvedBy</b>: Identifier of the user who approved the client.</li>
 *   <li><b>createdAt</b>: Timestamp of when the client was created.</li>
 *   <li><b>updatedAt</b>: Timestamp of the last update to the client.</li>
 * </ul>
 */
@Entity
@Table(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String clientCode;
    @Column(nullable = false, unique = true)
    private Long userId;
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;
    private Long approvedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
