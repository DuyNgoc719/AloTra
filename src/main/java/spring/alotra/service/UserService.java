package spring.alotra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.alotra.entity.UsersEntity;
import spring.alotra.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity usersEntity = userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("not fond username"));
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usersEntity.getRole().name());
        return new User(
                usersEntity.getUsername(),
                usersEntity.getPassword(),
                Collections.singleton(authority)
        );
    }
}
