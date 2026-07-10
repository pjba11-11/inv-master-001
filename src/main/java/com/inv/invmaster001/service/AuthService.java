package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.request.RegisterCompanyRequest;
import com.inv.invmaster001.dto.request.RegisterUserRequest;
import com.inv.invmaster001.dto.response.RegisterCommonResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.repository.CompanyRepository;
import com.inv.invmaster001.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    //Register User
    public RegisterCommonResponse registerUser(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(String.valueOf(request.getRole()))
                .build();

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //IMPORTANT: keeps both sides in sync
        company.addUser(user);

        userRepository.save(user);

        return new RegisterCommonResponse("User registered successfully", null);
    }

    //Register Company
    public RegisterCommonResponse registerCompany(RegisterCompanyRequest request) {

        if (companyRepository.existsByCompanyName(request.getCompanyName())) {
            throw new RuntimeException("Company already exists");
        }

        Company company = Company.builder()
                .companyName(request.getCompanyName())
                .gstNumber(request.getGstNumber())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .ifsc(request.getIfsc())
                .upiId(request.getUpiId())
                .logo(request.getLogo())
                .build();

        Company saved = companyRepository.save(company);

        return new RegisterCommonResponse("Company registered successfully", saved.getId());
    }
}