package com.HopeConnect.HC.DTO;

import lombok.Data;

@Data
public class AuthenticationPayload {
    private String email;
    private String password;
}