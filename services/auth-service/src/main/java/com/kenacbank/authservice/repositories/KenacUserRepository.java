package com.kenacbank.authservice.repositories;

import com.kenacbank.authservice.models.entities.KenacUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KenacUserRepository extends JpaRepository<KenacUser, Long> {
    Optional<KenacUser> findByUserCode(String userCode);

    Optional<KenacUser> findByEmail(String email);
}
