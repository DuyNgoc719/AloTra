package spring.alotra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.alotra.entity.ResetPassword;
import spring.alotra.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void sendResetToken(String email) {
        User user = userService.findUserByEmail(email);

        if (user == null) {
            return;
        }
        String token = UUID.randomUUID().toString();

        ResetPassword resetToken = new ResetPassword();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetService.saveToken(resetToken);

        sendNewPassword(email, token);
    }

    private void sendNewPassword(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setText("Để đặt lại mật khẩu, vui lòng nhấn vào liên kết sau: " +
                "http://localhost:8080/reset-password?token=" + token);
        mailSender.send(message);
    }

    public boolean resetPassword(String token, String newPassword) {
        ResetPassword resetToken = passwordResetService.findByToken(token);

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        User user = resetToken.getUser();
        if (user == null) {
            return false;
        }

        newPassword = passwordEncoder.encode(newPassword);

        user.setPassword(newPassword);
        userService.saveUser(user);
        passwordResetService.deleteToken(resetToken);
        return true;
    }

}
