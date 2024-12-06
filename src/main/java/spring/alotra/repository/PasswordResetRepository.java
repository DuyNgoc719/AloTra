package spring.alotra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.alotra.entity.ResetPassword;

@Repository
public interface PasswordResetRepository extends JpaRepository<ResetPassword, Long> {
    ResetPassword findByToken(String token);
}
