package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.User.Role;

import lombok.Data;

@Data
public class RegisterPayload {
    private String email;
    private String password;
    private String userName;
    private String phoneNumber;
    private Role role;
}