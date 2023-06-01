package com.example.UserAuthService.Service;

import com.example.UserAuthService.Model.User;
import com.example.UserAuthService.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetService implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("User not found!");
        }

        Collection<GrantedAuthority> aut = Collections.singleton(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), aut);
    }
}

