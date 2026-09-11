package com.bacheloros.bacheloros_backend.controller;

import com.bacheloros.bacheloros_backend.dto.FinanceOverviewResponse;
import com.bacheloros.bacheloros_backend.entity.PeriodType;
import com.bacheloros.bacheloros_backend.service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {
    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/overview")
    public ResponseEntity<FinanceOverviewResponse> getOverview() {
        return ResponseEntity.ok(financeService.getOverview());
    }
}
