package com.aakil.finance_dashboard_backend.dashboard.service;

import com.aakil.finance_dashboard_backend.dashboard.dto.DashboardSummaryResponse;
import com.aakil.finance_dashboard_backend.dashboard.dto.MonthlySummaryResponse;
import com.aakil.finance_dashboard_backend.dashboard.dto.UserSummaryResponse;
import com.aakil.finance_dashboard_backend.finance.entity.RecordType;
import com.aakil.finance_dashboard_backend.finance.repository.FinancialRecordRepository;
import com.aakil.finance_dashboard_backend.user.entity.User;
import com.aakil.finance_dashboard_backend.user.entity.UserStatus;
import com.aakil.finance_dashboard_backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private FinancialRecordRepository financialRecordRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void getSummary_shouldReturnComputedValues() {
        when(financialRecordRepository.sumAmountByType(RecordType.INCOME)).thenReturn(new BigDecimal("1500.00"));
        when(financialRecordRepository.sumAmountByType(RecordType.EXPENSE)).thenReturn(new BigDecimal("500.00"));
        when(financialRecordRepository.countByDeletedFalse()).thenReturn(6L);
        when(userRepository.countByStatus(UserStatus.ACTIVE)).thenReturn(3L);

        DashboardSummaryResponse response = dashboardService.getSummary();

        assertEquals(new BigDecimal("1500.00"), response.getTotalIncome());
        assertEquals(new BigDecimal("500.00"), response.getTotalExpense());
        assertEquals(new BigDecimal("1000.00"), response.getNetBalance());
        assertEquals(6L, response.getTotalTransactions());
        assertEquals(3L, response.getActiveUsers());
    }

    @Test
    void getMonthlySummary_shouldUseExpectedMonthRange() {
        LocalDate from = LocalDate.of(2026, 4, 1);
        LocalDate to = LocalDate.of(2026, 4, 30);

        when(financialRecordRepository.sumAmountByTypeAndTransactionDateBetween(RecordType.INCOME, from, to))
                .thenReturn(new BigDecimal("900.00"));
        when(financialRecordRepository.sumAmountByTypeAndTransactionDateBetween(RecordType.EXPENSE, from, to))
                .thenReturn(new BigDecimal("300.00"));
        when(financialRecordRepository.countByDeletedFalseAndTransactionDateBetween(from, to)).thenReturn(4L);

        MonthlySummaryResponse response = dashboardService.getMonthlySummary(2026, 4);

        assertEquals(new BigDecimal("900.00"), response.getIncomeThisMonth());
        assertEquals(new BigDecimal("300.00"), response.getExpenseThisMonth());
        assertEquals(new BigDecimal("600.00"), response.getBalanceThisMonth());
        assertEquals(4L, response.getTransactionCount());

        verify(financialRecordRepository).countByDeletedFalseAndTransactionDateBetween(from, to);
    }

    @Test
    void getUserSummary_shouldReturnAggregatedUserData() {
        User user = new User();
        user.setId(9L);
        user.setName("Aakil");

        when(userRepository.findById(9L)).thenReturn(Optional.of(user));
        when(financialRecordRepository.sumAmountByUserAndType(9L, RecordType.INCOME))
                .thenReturn(new BigDecimal("1200.00"));
        when(financialRecordRepository.sumAmountByUserAndType(9L, RecordType.EXPENSE))
                .thenReturn(new BigDecimal("450.00"));
        when(financialRecordRepository.countByCreatedByIdAndDeletedFalse(9L)).thenReturn(5L);
        when(financialRecordRepository.findLastActivityByUserId(9L)).thenReturn(LocalDate.of(2026, 4, 5));

        UserSummaryResponse response = dashboardService.getUserSummary(9L);

        assertEquals(9L, response.getUserId());
        assertEquals("Aakil", response.getUserName());
        assertEquals(new BigDecimal("1200.00"), response.getTotalIncome());
        assertEquals(new BigDecimal("450.00"), response.getTotalExpense());
        assertEquals(new BigDecimal("750.00"), response.getNetBalance());
        assertEquals(5L, response.getTotalRecords());
        assertEquals(LocalDate.of(2026, 4, 5), response.getLastActivity());
    }
}
