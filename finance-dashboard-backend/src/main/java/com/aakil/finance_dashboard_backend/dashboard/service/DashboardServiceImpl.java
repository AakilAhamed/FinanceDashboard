package com.aakil.finance_dashboard_backend.dashboard.service;

import com.aakil.finance_dashboard_backend.dashboard.dto.*;
import com.aakil.finance_dashboard_backend.finance.dto.FinancialRecordResponse;
import com.aakil.finance_dashboard_backend.finance.entity.FinancialRecord;
import com.aakil.finance_dashboard_backend.finance.entity.RecordType;
import com.aakil.finance_dashboard_backend.finance.repository.FinancialRecordRepository;
import com.aakil.finance_dashboard_backend.user.entity.User;
import com.aakil.finance_dashboard_backend.user.entity.UserStatus;
import com.aakil.finance_dashboard_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final FinancialRecordRepository financialRecordRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardSummaryResponse getSummary() {
        BigDecimal totalIncome = financialRecordRepository.sumAmountByType(RecordType.INCOME);
        BigDecimal totalExpense = financialRecordRepository.sumAmountByType(RecordType.EXPENSE);

        DashboardSummaryResponse response = new DashboardSummaryResponse();
        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setNetBalance(totalIncome.subtract(totalExpense));
        response.setTotalTransactions(financialRecordRepository.countByDeletedFalse());
        response.setActiveUsers(userRepository.countByStatus(UserStatus.ACTIVE));

        return response;
    }

    @Override
    public MonthlySummaryResponse getMonthlySummary(int year, int month) {
        if (month < 1 || month > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Month must be between 1 and 12");
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate from = yearMonth.atDay(1);
        LocalDate to = yearMonth.atEndOfMonth();

        BigDecimal income = financialRecordRepository
            .sumAmountByTypeAndTransactionDateBetween(RecordType.INCOME, from, to);
        BigDecimal expense = financialRecordRepository
            .sumAmountByTypeAndTransactionDateBetween(RecordType.EXPENSE, from, to);
        long transactionCount = financialRecordRepository
            .countByDeletedFalseAndTransactionDateBetween(from, to);

        MonthlySummaryResponse response = new MonthlySummaryResponse();
        response.setIncomeThisMonth(income);
        response.setExpenseThisMonth(expense);
        response.setBalanceThisMonth(income.subtract(expense));
        response.setTransactionCount(transactionCount);

        return response;
    }

    @Override
    public List<TrendPointResponse> getTrend(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date range");
        }

        return financialRecordRepository.findTrendData(from, to)
                .stream()
                .map(this::toTrendPointResponse)
                .toList();
    }

    @Override
    public List<CategoryBreakdownResponse> getCategoryBreakdown(RecordType type) {
        return financialRecordRepository.findCategoryBreakdown(type)
                .stream()
                .map(this::toCategoryBreakdownResponse)
                .toList();
    }

    @Override
    public List<FinancialRecordResponse> getRecentRecords(int limit) {
        return financialRecordRepository
                .findByDeletedFalseOrderByCreatedAtDesc(pageRequest(limit))
                .stream()
                .map(this::toFinancialRecordResponse)
                .toList();
    }

    @Override
    public List<FinancialRecordResponse> getTopExpenses(int limit) {
        return financialRecordRepository
                .findByDeletedFalseAndTypeOrderByAmountDesc(RecordType.EXPENSE, pageRequest(limit))
                .stream()
                .map(this::toFinancialRecordResponse)
                .toList();
    }

    @Override
    public List<FinancialRecordResponse> getTopIncome(int limit) {
        return financialRecordRepository
                .findByDeletedFalseAndTypeOrderByAmountDesc(RecordType.INCOME, pageRequest(limit))
                .stream()
                .map(this::toFinancialRecordResponse)
                .toList();
    }

    @Override
    public UserSummaryResponse getUserSummary(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        BigDecimal totalIncome = financialRecordRepository.sumAmountByUserAndType(userId, RecordType.INCOME);
        BigDecimal totalExpense = financialRecordRepository.sumAmountByUserAndType(userId, RecordType.EXPENSE);

        UserSummaryResponse response = new UserSummaryResponse();
        response.setUserId(user.getId());
        response.setUserName(user.getName());
        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setNetBalance(totalIncome.subtract(totalExpense));
        response.setTotalRecords(financialRecordRepository.countByCreatedByIdAndDeletedFalse(userId));
        response.setLastActivity(financialRecordRepository.findLastActivityByUserId(userId));

        return response;
    }

    @Override
    public RecordCountByTypeResponse getRecordCountByType() {
        Object[] counts = financialRecordRepository.findRecordCountByType();

        RecordCountByTypeResponse response = new RecordCountByTypeResponse();
        response.setIncomeCount(getLongAt(counts, 0));
        response.setExpenseCount(getLongAt(counts, 1));

        return response;
    }

    @Override
    public List<RecordCountByUserResponse> getRecordCountByUser() {
        return financialRecordRepository.findRecordCountByUser()
                .stream()
                .map(this::toRecordCountByUserResponse)
                .toList();
    }

    @Override
    public DashboardStatusResponse getStatus() {
        DashboardStatusResponse response = new DashboardStatusResponse();
        response.setTotalUsers(userRepository.count());
        response.setActiveUsers(userRepository.countByStatus(UserStatus.ACTIVE));
        response.setInactiveUsers(userRepository.countByStatus(UserStatus.INACTIVE));
        response.setTotalRecords(financialRecordRepository.countByDeletedFalse());
        response.setDeletedRecords(financialRecordRepository.countByDeletedTrue());

        return response;
    }

    private PageRequest pageRequest(int limit) {
        if (limit <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be greater than 0");
        }
        return PageRequest.of(0, limit);
    }

    private TrendPointResponse toTrendPointResponse(Object[] row) {
        LocalDate date = (LocalDate) row[0];
        BigDecimal income = toBigDecimal(row[1]);
        BigDecimal expense = toBigDecimal(row[2]);

        TrendPointResponse response = new TrendPointResponse();
        response.setDate(date);
        response.setIncome(income);
        response.setExpense(expense);
        response.setBalance(income.subtract(expense));

        return response;
    }

    private CategoryBreakdownResponse toCategoryBreakdownResponse(Object[] row) {
        CategoryBreakdownResponse response = new CategoryBreakdownResponse();
        response.setCategory((String) row[0]);
        response.setAmount(toBigDecimal(row[1]));

        return response;
    }

    private RecordCountByUserResponse toRecordCountByUserResponse(Object[] row) {
        RecordCountByUserResponse response = new RecordCountByUserResponse();
        response.setUserId(getLongAt(row, 0));
        response.setUserName(getStringAt(row, 1));
        response.setRecordCount(getLongAt(row, 2));

        return response;
    }

    private BigDecimal getBigDecimalAt(Object[] row, int index) {
        return toBigDecimal(getAt(row, index));
    }

    private long getLongAt(Object[] row, int index) {
        return toLong(getAt(row, index));
    }

    private String getStringAt(Object[] row, int index) {
        Object value = getAt(row, index);
        return value == null ? null : value.toString();
    }

    private LocalDate getLocalDateAt(Object[] row, int index) {
        Object value = getAt(row, index);
        return value instanceof LocalDate localDate ? localDate : null;
    }

    private Object getAt(Object[] row, int index) {
        if (row == null || index < 0 || index >= row.length) {
            return null;
        }
        return row[index];
    }

    private FinancialRecordResponse toFinancialRecordResponse(FinancialRecord record) {
        FinancialRecordResponse response = new FinancialRecordResponse();

        response.setId(record.getId());
        response.setAmount(record.getAmount());
        response.setType(record.getType());
        response.setCategory(record.getCategory());
        response.setDescription(record.getDescription());
        response.setTransactionDate(record.getTransactionDate());
        response.setCreatedById(record.getCreatedBy().getId());
        response.setCreatedByName(record.getCreatedBy().getName());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());

        return response;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return BigDecimal.ZERO;
    }

    private long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return 0L;
    }
}
