package dev.ewd.mediashelf_spring.mediashelf_spring.repository;

import dev.ewd.mediashelf_spring.mediashelf_spring.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    void deleteByExpiryDateBefore(LocalDateTime now);
}
