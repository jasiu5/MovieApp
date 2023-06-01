package com.example.UserAuthService.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Data
@Getter
@Setter
public class LoginForm {
    private String email;
    private String password;
}

