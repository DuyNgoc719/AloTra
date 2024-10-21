package spring.alotra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.alotra.entity.UserEntity;
import spring.alotra.repository.UserRepository;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("not fond username"));
        return new User(userEntity.getUsername(),userEntity.getPassword(),
                Arrays.stream(userEntity.getRole().split("\\|"))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }
}
