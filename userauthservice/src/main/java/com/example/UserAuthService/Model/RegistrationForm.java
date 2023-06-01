package com.example.UserAuthService.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegistrationForm {
    private String name;
    private String email;
    private String password;
}
