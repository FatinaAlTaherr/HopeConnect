package com.HopeConnect.HC.services.UserServices;
import com.HopeConnect.HC.models.OrphanManagement.Orphanage;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.models.User.Role;
import com.HopeConnect.HC.repositories.OrphanManagementRepositories.OrphanageRepository;
import com.HopeConnect.HC.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final  OrphanageRepository orphanageRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .toList();
    }


    public Orphanage getOrphanageById(Long orphanageId) {
        return orphanageRepository.findById(orphanageId)
                .orElseThrow(() -> new RuntimeException("Orphanage not found"));
    }
}