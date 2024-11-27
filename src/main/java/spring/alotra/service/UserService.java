package spring.alotra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.alotra.entity.User;
import spring.alotra.repository.UserRepository;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usersEntity = userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("not fond username"));
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usersEntity.getUserRole().name());
        return new org.springframework.security.core.userdetails.User(
                usersEntity.getUsername(),
                usersEntity.getPassword(),
                Collections.singleton(authority)
        );
    }
}
