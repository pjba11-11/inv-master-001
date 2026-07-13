package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.request.CreateUserAdminRequest;
import com.inv.invmaster001.dto.response.UserResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    private User admin;

    @BeforeEach
    void setUp() {
        Company company = Company.builder().id(7L).companyName("Acme").build();
        admin = User.builder().id(1L).name("Admin").email("admin@acme.in")
                .role("ADMIN").company(company).build();
    }

    private CreateUserAdminRequest request() {
        CreateUserAdminRequest request = new CreateUserAdminRequest();
        request.setName("Jane");
        request.setEmail("jane@acme.in");
        request.setPassword("raw-password");
        request.setRole("EMPLOYEE");
        return request;
    }

    @Test
    void createUser_rejectsDuplicateEmail() {
        when(userRepository.existsByEmail("jane@acme.in")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request(), admin))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already in use");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_inheritsAdminsCompany() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("ENC");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.createUser(request(), admin);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getCompany().getId()).isEqualTo(7L);
        assertThat(admin.getCompany().getUsers()).contains(captor.getValue());
    }

    @Test
    void createUser_storesEncodedPasswordNeverRaw() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode("raw-password")).thenReturn("ENC");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.createUser(request(), admin);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("ENC");
    }

    @Test
    void createUser_mapsResponseFields() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("ENC");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponse response = userService.createUser(request(), admin);

        assertThat(response.getName()).isEqualTo("Jane");
        assertThat(response.getEmail()).isEqualTo("jane@acme.in");
        assertThat(response.getRole()).isEqualTo("EMPLOYEE");
    }

    @Test
    void getUsersByCompany_mapsEntities() {
        LocalDateTime joined = LocalDateTime.of(2026, 7, 1, 12, 0);
        when(userRepository.findByCompanyIdAndDeletedAtIsNull(7L)).thenReturn(List.of(
                User.builder().id(2L).name("Ravi").email("ravi@acme.in")
                        .role("MANAGER").createdAt(joined).build()));

        List<UserResponse> users = userService.getUsersByCompany(7L);

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getId()).isEqualTo(2L);
        assertThat(users.get(0).getName()).isEqualTo("Ravi");
        assertThat(users.get(0).getRole()).isEqualTo("MANAGER");
        assertThat(users.get(0).getCreatedAt()).isEqualTo(joined);
    }
}
