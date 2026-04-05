package com.aakil.finance_dashboard_backend.dashboard.controller;

import com.aakil.finance_dashboard_backend.dashboard.dto.*;
import com.aakil.finance_dashboard_backend.dashboard.service.DashboardService;
import com.aakil.finance_dashboard_backend.finance.dto.FinancialRecordResponse;
import com.aakil.finance_dashboard_backend.finance.entity.RecordType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get Dashboard Summary")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard summary fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @Operation(summary = "Get Monthly Summary")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monthly summary fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid month/year values"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(dashboardService.getMonthlySummary(year, month));
    }

    @Operation(summary = "Get Trend Data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trend data fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/trend")
    public ResponseEntity<List<TrendPointResponse>> getTrend(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(dashboardService.getTrend(from, to));
    }

    @Operation(summary = "Get Category Breakdown")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category breakdown fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/category-breakdown")
    public ResponseEntity<List<CategoryBreakdownResponse>> getCategoryBreakdown(
            @RequestParam RecordType type) {
        return ResponseEntity.ok(dashboardService.getCategoryBreakdown(type));
    }

    @Operation(summary = "Get Recent Records")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recent records fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid limit value"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/recent-records")
    public ResponseEntity<List<FinancialRecordResponse>> getRecentRecords(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(dashboardService.getRecentRecords(limit));
    }

    @Operation(summary = "Get Top Expense Records")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Top expense records fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid limit value"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/top-expenses")
    public ResponseEntity<List<FinancialRecordResponse>> getTopExpenses(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(dashboardService.getTopExpenses(limit));
    }

    @Operation(summary = "Get Top Income Records")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Top income records fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid limit value"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/top-income")
    public ResponseEntity<List<FinancialRecordResponse>> getTopIncome(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(dashboardService.getTopIncome(limit));
    }

    @Operation(summary = "Get User Summary")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User summary fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user-summary/{userId}")
    public ResponseEntity<UserSummaryResponse> getUserSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(dashboardService.getUserSummary(userId));
    }

    @Operation(summary = "Get Record Count by Type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Record count by type fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/record-count-by-type")
    public ResponseEntity<RecordCountByTypeResponse> getRecordCountByType() {
        return ResponseEntity.ok(dashboardService.getRecordCountByType());
    }

    @Operation(summary = "Get Record Count by User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Record count by user fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/record-count-by-user")
    public ResponseEntity<List<RecordCountByUserResponse>> getRecordCountByUser() {
        return ResponseEntity.ok(dashboardService.getRecordCountByUser());
    }

    @Operation(summary = "Get System Dashboard Status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "System status fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/status")
    public ResponseEntity<DashboardStatusResponse> getStatus() {
        return ResponseEntity.ok(dashboardService.getStatus());
    }
}
