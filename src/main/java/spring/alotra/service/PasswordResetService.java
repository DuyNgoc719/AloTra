package spring.alotra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.alotra.entity.ResetPassword;
import spring.alotra.repository.PasswordResetRepository;

@Service
public class PasswordResetService {
    @Autowired
    private PasswordResetRepository passwordResetRepository;

    public void saveToken(ResetPassword token) {
        passwordResetRepository.save(token);
    }

    public ResetPassword findByToken(String token) {
        return passwordResetRepository.findByToken(token);
    }

    public void deleteToken(ResetPassword token) {
        passwordResetRepository.delete(token);
    }
}
