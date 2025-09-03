package org.jetblue.jetblue.Repositories;


import org.jetblue.jetblue.Models.DAO.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
