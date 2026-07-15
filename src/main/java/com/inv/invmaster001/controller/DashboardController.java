package com.inv.invmaster001.controller;

import com.inv.invmaster001.dto.response.dashboard.DashboardAIResponse;
import com.inv.invmaster001.dto.response.dashboard.DashboardResponse;
import com.inv.invmaster001.security.CustomUserDetails;
import com.inv.invmaster001.service.dashboard.DashboardAIService;
import com.inv.invmaster001.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private  final DashboardAIService dashboardAIService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<DashboardResponse> getDashboard(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        DashboardResponse response =
                dashboardService.getDashboard(
                        currentUser.getUser()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ai")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<DashboardAIResponse> getDashboardAI(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        DashboardResponse dashboard =
                dashboardService.getDashboard(currentUser.getUser());

        DashboardAIResponse response =
                dashboardAIService.generateInsights(
                        dashboard,
                        currentUser.getUser().getCompany());

        return ResponseEntity.ok(response);
    }
}