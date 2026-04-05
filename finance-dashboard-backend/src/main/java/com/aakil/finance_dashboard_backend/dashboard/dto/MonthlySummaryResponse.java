package com.aakil.finance_dashboard_backend.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySummaryResponse {
    private BigDecimal incomeThisMonth;
    private BigDecimal expenseThisMonth;
    private BigDecimal balanceThisMonth;
    private long transactionCount;
}
