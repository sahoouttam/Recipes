package com.example.Recipes.Business;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Setter
@Getter
public class User {
    
    private static final String emailRegex = "(?i)[\\w!#$%&'*+/=?`{|}~^-]+" + "(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-z0-9-]+\\.)+[a-z]{2,6}";

    @Id
    @NonNull
    @NotBlank
    @Email(regexp = emailRegex)
    private String email;

    @NonNull
    @NotBlank
    @Size(min = 8)
    private String password;
}
