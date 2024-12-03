package spring.alotra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import spring.alotra.entity.UserRole;
import spring.alotra.entity.User;
import spring.alotra.repository.UserRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        Optional<User> userEntity = userRepository.findByUsername(user.getUsername());
        if (userEntity.isPresent()) {
            return ;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.USER);
        userRepository.save(user);
    }

    public Map<String,Object> login(String username, String password) {

        Optional<User> userEntity = userRepository.findByUsername(username);
        Map<String,Object> respone = new HashMap<>();
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (!userEntity.isPresent()) {
            respone.put("message", "Username or password is incorrect");
            return respone;
        }
        String accessToken =generateToken(userEntity.get(),auth,3600);
        respone.put("access_token", accessToken);
        respone.put("expires_in", 3600);
        respone.put("role", userEntity.get().getUserRole().toString());
        return respone;
    }

    private String generateToken(User usersEntity, Authentication authentication, long expiryDuration){
        Instant now =  Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("Ornate")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiryDuration))
                .subject(authentication.getName())
                .claim("role",authentication.getAuthorities().toString())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

}
