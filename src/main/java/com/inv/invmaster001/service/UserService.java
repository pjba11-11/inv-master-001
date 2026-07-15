package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.request.CreateUserAdminRequest;
import com.inv.invmaster001.dto.response.UserResponse;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByCompany(Long companyId) {
        return userRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
                .stream()
                .map(u -> UserResponse.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .role(u.getRole())
                        .createdAt(u.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public UserResponse createUser(CreateUserAdminRequest request, User adminUser) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // adminUser comes from the security context (loaded in the auth
        // filter's own short-lived session) and is detached by the time it
        // reaches this transaction, so its lazy `company` association can't
        // be initialized here. Re-fetch a managed instance in this session.
        User managedAdmin = userRepository.findById(adminUser.getId())
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .company(managedAdmin.getCompany())
                .build();

        User saved = userRepository.save(user);

        return UserResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
