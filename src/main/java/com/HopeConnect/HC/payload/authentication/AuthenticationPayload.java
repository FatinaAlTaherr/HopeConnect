package com.HopeConnect.HC.payload.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationPayload {

    private String email;
    private String password;
}
