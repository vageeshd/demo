package com.url.shortener.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String userName;
    private String password;
    private Set<String> roles;
    private String email;
}
