package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.request.RegisterCompanyRequest;
import com.inv.invmaster001.dto.request.RegisterUserRequest;
import com.inv.invmaster001.dto.response.RegisterCommonResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.entity.UserRole;
import com.inv.invmaster001.repository.CompanyRepository;
import com.inv.invmaster001.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private CompanyRepository companyRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private AuthService authService;

    private RegisterUserRequest userRequest() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setCompanyId(7L);
        request.setName("Jane");
        request.setEmail("jane@acme.in");
        request.setPassword("raw-password");
        request.setRole(UserRole.MANAGER);
        return request;
    }

    @Test
    void registerUser_rejectsDuplicateEmail() {
        when(userRepository.existsByEmail("jane@acme.in")).thenReturn(true);

        assertThatThrownBy(() -> authService.registerUser(userRequest()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_companyNotFound() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(companyRepository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.registerUser(userRequest()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Company not found");
    }

    @Test
    void registerUser_encodesPasswordAndJoinsCompany() {
        Company company = Company.builder().id(7L).companyName("Acme").build();
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(companyRepository.findById(7L)).thenReturn(Optional.of(company));
        when(passwordEncoder.encode("raw-password")).thenReturn("ENC");

        RegisterCommonResponse response = authService.registerUser(userRequest());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("ENC");
        assertThat(captor.getValue().getRole()).isEqualTo("MANAGER");
        assertThat(company.getUsers()).contains(captor.getValue());
        assertThat(response.getMessage()).isEqualTo("User registered successfully");
    }

    @Test
    void registerCompany_rejectsDuplicateName() {
        when(companyRepository.existsByCompanyName("Acme")).thenReturn(true);

        assertThatThrownBy(() -> authService.registerCompany(
                RegisterCompanyRequest.builder().companyName("Acme").build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Company already exists");

        verify(companyRepository, never()).save(any());
    }

    @Test
    void registerCompany_returnsSavedCompanyId() {
        when(companyRepository.existsByCompanyName("Acme")).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> {
            Company c = inv.getArgument(0);
            c.setId(31L);
            return c;
        });

        RegisterCommonResponse response = authService.registerCompany(
                RegisterCompanyRequest.builder()
                        .companyName("Acme")
                        .gstNumber("29ABCDE1234F1Z5")
                        .email("billing@acme.in")
                        .build());

        assertThat(response.getCompanyId()).isEqualTo(31L);
        assertThat(response.getMessage()).isEqualTo("Company registered successfully");
    }
}
