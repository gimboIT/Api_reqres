package models;

import lombok.Data;

@Data
public class RegisterUserBody {
    private String email;
    private String password;
}
