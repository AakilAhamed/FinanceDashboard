package com.aakil.finance_dashboard_backend.dashboard.service;

import com.aakil.finance_dashboard_backend.dashboard.dto.*;
import com.aakil.finance_dashboard_backend.finance.dto.FinancialRecordResponse;
import com.aakil.finance_dashboard_backend.finance.entity.RecordType;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {
    DashboardSummaryResponse getSummary();

    MonthlySummaryResponse getMonthlySummary(int year, int month);

    List<TrendPointResponse> getTrend(LocalDate from, LocalDate to);

    List<CategoryBreakdownResponse> getCategoryBreakdown(RecordType type);

    List<FinancialRecordResponse> getRecentRecords(int limit);

    List<FinancialRecordResponse> getTopExpenses(int limit);

    List<FinancialRecordResponse> getTopIncome(int limit);

    UserSummaryResponse getUserSummary(Long userId);

    RecordCountByTypeResponse getRecordCountByType();

    List<RecordCountByUserResponse> getRecordCountByUser();

    DashboardStatusResponse getStatus();
}
