package com.mantzavelas.tripassistant.restservices.dtos;

public class UserTokenDto {

    private String authToken;

    public UserTokenDto() {
    }

    public String getAuthToken() { return authToken; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }

}
