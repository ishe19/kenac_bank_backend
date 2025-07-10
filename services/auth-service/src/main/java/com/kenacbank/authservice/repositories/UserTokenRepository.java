package com.kenacbank.authservice.repositories;

import com.kenacbank.authservice.models.entities.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findByToken(String token);

    List<UserToken> findByUserIdAndExpiredFalseAndRevokedFalse(Long id);
}
