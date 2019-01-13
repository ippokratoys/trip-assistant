package com.mantzavelas.tripassistant.restservices.resources;

public class UserResource {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    public UserResource() {
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public static class Builder {

        private UserResource resource;

        public Builder() {
            this.resource = new UserResource();
        }

        public Builder withFirstName(String firstName) {
            resource.setFirstName(firstName);
            return this;
        }

        public Builder withLastName(String lastName) {
            resource.setLastName(lastName);
            return this;
        }

        public Builder withUsername(String username) {
            resource.setUsername(username);
            return this;
        }

        public Builder withPassword(String password) {
            resource.setPassword(password);
            return this;
        }

        public UserResource build() {
            return resource;
        }
    }
}
