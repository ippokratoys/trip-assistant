package com.mantzavelas.tripassistantapi.dtos;

public class AuthTokenDto {

    private String authToken;

    public AuthTokenDto(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() { return authToken; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }
}
