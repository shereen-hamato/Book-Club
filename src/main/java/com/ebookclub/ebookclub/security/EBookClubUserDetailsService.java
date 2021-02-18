package com.ebookclub.ebookclub.security;

import com.ebookclub.ebookclub.model.User;
import com.ebookclub.ebookclub.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;


@Component
public class EBookClubUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with name %s does not exist", s)));

        return withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(new ArrayList<>())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }


    public Optional<User> getLoginUser(String jwtToken) throws UsernameNotFoundException {

        if (jwtProvider.isValidToken(jwtToken)) {String userName = jwtProvider.getUsername(jwtToken);
        User user = userRepository.findByUsername(userName).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with name %s does not exist", userName)));

        return Optional.of(user);
        }
        return Optional.empty();
    }

}