package spring.alotra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import spring.alotra.entity.UserEntity;
import spring.alotra.repository.UserRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ServiceImpl {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(UserEntity user) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(user.getUsername());
        if (userEntity.isPresent()) {
            return "Username is already in use";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "User registered successfully";
    }

    public Map<String,Object> login(String username, String password) {

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        Map<String,Object> respone = new HashMap<>();
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (!userEntity.isPresent()) {
            respone.put("message", "Username or password is incorrect");
            return respone;
        }
        String accessToken =generateToken(userEntity.get(),auth,3600);
        respone.put("access_token", accessToken);
        respone.put("expires_in", 3600);
        return respone;
    }

    private String generateToken(UserEntity userEntity, Authentication authentication,long expiryDuration){
        Instant now =  Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("Ornate")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiryDuration))
                .subject(authentication.getName())
                .claim("role",authentication.getAuthorities().toString())
                .claim("firstName",userEntity.getFirstName())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
