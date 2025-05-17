package com.HopeConnect.HC.controllers;

import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.models.User.Role;
import com.HopeConnect.HC.services.ExternalAPI.GeocodingService;
import com.HopeConnect.HC.services.UserServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final GeocodingService geocodingService;


    @GetMapping("/{email}/role")
    public ResponseEntity<Role> getUserRole(@PathVariable String email) {
        try {
            User user = userService.getUser(email);
            Role role = user.getRole();
            return ResponseEntity.ok(role);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{email}/location")
    public ResponseEntity<Map<String, Float>> getUserGeolocation(@PathVariable String email) {
        try {
            User user = userService.getUser(email);
            String address = user.getLocation();
            if (address == null || address.isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            Map<String, Float> coordinates = geocodingService.getGeocodingData(address);
            return ResponseEntity.ok(coordinates);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", -1f)); // Optional: better error handling
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
