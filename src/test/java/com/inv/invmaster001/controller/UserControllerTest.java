package com.inv.invmaster001.controller;

import com.inv.invmaster001.dto.response.UserResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.security.CustomUserDetails;
import com.inv.invmaster001.security.JwtFilter;
import com.inv.invmaster001.security.SecurityConfig;
import com.inv.invmaster001.service.CustomUserDetailsService;
import com.inv.invmaster001.service.JwtService;
import com.inv.invmaster001.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtFilter.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // JwtFilter dependencies — must exist as beans, never mock the filter itself
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private CustomUserDetails adminPrincipal() {
        User admin = User.builder()
                .id(1L)
                .name("Admin")
                .email("admin@acme.in")
                .password("x")
                .role("ADMIN")
                .company(Company.builder().id(1L).build())
                .build();
        return new CustomUserDetails(admin);
    }

    @Test
    void unauthenticatedRequestGets401() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void employeeRoleGets403() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanListUsers() throws Exception {
        when(userService.getUsersByCompany(1L)).thenReturn(List.of(
                UserResponse.builder().id(2L).name("Jane").email("jane@acme.in")
                        .role("EMPLOYEE").build()));

        mockMvc.perform(get("/admin/users").with(user(adminPrincipal())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("jane@acme.in"))
                .andExpect(jsonPath("$[0].role").value("EMPLOYEE"));
    }

    @Test
    void adminCanCreateUser() throws Exception {
        when(userService.createUser(any(), any())).thenReturn(
                UserResponse.builder().id(3L).name("New").email("n@acme.in")
                        .role("EMPLOYEE").build());

        mockMvc.perform(post("/admin/users")
                        .with(user(adminPrincipal()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New\",\"email\":\"n@acme.in\","
                                + "\"password\":\"pw\",\"role\":\"EMPLOYEE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("n@acme.in"));
    }
}
