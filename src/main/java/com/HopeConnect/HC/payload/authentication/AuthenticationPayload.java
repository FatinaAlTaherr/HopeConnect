package com.HopeConnect.HC.payload.authentication;

public class AuthenticationPayload {

    private String email;
    private String password;

    // No-argument constructor
    public AuthenticationPayload() {
    }

    // Constructor with parameters
    public AuthenticationPayload(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Builder class for AuthenticationPayload
    public static class Builder {
        private String email;
        private String password;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public AuthenticationPayload build() {
            return new AuthenticationPayload(email, password);
        }
    }

    // Builder method to get an instance of the Builder
    public static Builder builder() {
        return new Builder();
    }
}
