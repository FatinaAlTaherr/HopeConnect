package com.HopeConnect.HC.payload.authentication;


import com.HopeConnect.HC.models.User.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPayload {


    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private Role role;

}
