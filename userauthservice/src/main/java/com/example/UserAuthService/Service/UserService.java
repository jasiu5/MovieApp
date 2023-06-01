package com.example.UserAuthService.Service;

import com.example.UserAuthService.Model.LoginForm;
import com.example.UserAuthService.Model.RegistrationForm;
import com.example.UserAuthService.Model.User;
import com.example.UserAuthService.Repository.UserRepository;
import com.example.UserAuthService.Security.JWT.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public void saveUser(String name, String email, String pswd){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(pswd));
        user.setRole("USER");
        userRepository.save(user);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findUserByName(String name){
        return userRepository.findByName(name);
    }

    public String loginUser(LoginForm logUser){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                logUser.getEmail(), logUser.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        return token;
    }

    public String registerUser(RegistrationForm regUser){

        if(findUserByEmail(regUser.getEmail()) != null){
            return null;
        }

        saveUser(regUser.getName(), regUser.getEmail(), regUser.getPassword());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                regUser.getEmail(), regUser.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        return token;
    }
}

