package spring.alotra.config;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    public final JwtDecoder jwtDecoder;
    public JwtUtil(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public Jwt decode(String token) {
        return jwtDecoder.decode(token);
    }
}
