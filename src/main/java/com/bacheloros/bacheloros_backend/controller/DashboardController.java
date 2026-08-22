package com.bacheloros.bacheloros_backend.controller;

import com.bacheloros.bacheloros_backend.dto.DashboardResponse;
import com.bacheloros.bacheloros_backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(
            @RequestParam Integer month, @RequestParam Integer year) {
        return ResponseEntity.ok(dashboardService.getDashboard(month, year));
    }
}