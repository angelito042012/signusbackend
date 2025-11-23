package com.example.signusbackend.auth.dto;

public class ClienteRegisterDTO {
    private String email;
    private String password;

    //private String oauthProvider;
    //private String oauthId; 


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    
}
