package com.example.UserAuthService.Controller;

import com.example.UserAuthService.Model.LoginForm;
import com.example.UserAuthService.Model.RegistrationForm;
import com.example.UserAuthService.Model.User;
import com.example.UserAuthService.Model.UserTokenForm;
import com.example.UserAuthService.Repository.UserRepository;
import com.example.UserAuthService.Security.JWT.TokenProvider;
import com.example.UserAuthService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test(){
        return "this is a test page";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestBody LoginForm logUser){

        return userService.loginUser(logUser);
    }

    @PostMapping("/registration")
    public String createUser(
            @RequestBody RegistrationForm regUser){

        return userService.registerUser(regUser);

    }

    @PostMapping("/usertoken")
    public Map<String, String> getUser(
            @RequestBody UserTokenForm tokenForm){
        Map<String, String> dict = new HashMap<String, String>();

        String token = tokenForm.getToken();
        String email = tokenProvider.getUsername(token);
        User user = userService.findUserByEmail(email);
        long id = user.getId();

        dict.put("id", Long.toString(id));
        dict.put("email", email);

        return dict;
    }



}

