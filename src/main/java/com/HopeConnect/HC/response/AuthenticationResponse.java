package com.HopeConnect.HC.response;

public class AuthenticationResponse {

    private String token;

    public AuthenticationResponse() {
    }

    private AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Builder class for AuthenticationResponse
    public static class Builder {
        private String token;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationResponse build() {
            return new AuthenticationResponse(token);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
