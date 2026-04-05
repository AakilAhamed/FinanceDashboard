package com.aakil.finance_dashboard_backend.finance.service;

import com.aakil.finance_dashboard_backend.finance.dto.FinancialRecordResponse;
import com.aakil.finance_dashboard_backend.finance.entity.FinancialRecord;
import com.aakil.finance_dashboard_backend.finance.entity.RecordType;
import com.aakil.finance_dashboard_backend.finance.repository.FinancialRecordRepository;
import com.aakil.finance_dashboard_backend.user.entity.User;
import com.aakil.finance_dashboard_backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class FinancialRecordServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FinancialRecordRepository financialRecordRepository;

    @InjectMocks
    private FinancialRecordServiceImpl financialRecordService;

    @Test
    void getAllRecords_shouldReturnPagedResponse() {
        FinancialRecord record = createRecord(1L, RecordType.INCOME, new BigDecimal("2500.00"));
        Page<FinancialRecord> page = new PageImpl<>(List.of(record), PageRequest.of(0, 10), 1);

        when(financialRecordRepository.findByDeletedFalse(PageRequest.of(0, 10))).thenReturn(page);

        Page<FinancialRecordResponse> result = financialRecordService.getAllRecords(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().getFirst().getId());
        assertEquals("Alice", result.getContent().getFirst().getCreatedByName());
        verify(financialRecordRepository).findByDeletedFalse(PageRequest.of(0, 10));
    }

    @Test
    void getAllRecords_shouldThrowWhenSizeInvalid() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> financialRecordService.getAllRecords(0, 0)
        );

        assertEquals(BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void getRecordById_shouldReturnMappedResponse() {
        FinancialRecord record = createRecord(5L, RecordType.EXPENSE, new BigDecimal("99.99"));
        when(financialRecordRepository.findByIdAndDeletedFalse(5L)).thenReturn(Optional.of(record));

        FinancialRecordResponse response = financialRecordService.getRecordById(5L);

        assertEquals(5L, response.getId());
        assertEquals(RecordType.EXPENSE, response.getType());
        assertEquals("Alice", response.getCreatedByName());
    }

    private FinancialRecord createRecord(Long id, RecordType type, BigDecimal amount) {
        User user = new User();
        user.setId(100L);
        user.setName("Alice");

        FinancialRecord record = new FinancialRecord();
        record.setId(id);
        record.setType(type);
        record.setAmount(amount);
        record.setCategory("General");
        record.setDescription("Test record");
        record.setTransactionDate(LocalDate.of(2026, 4, 5));
        record.setCreatedAt(LocalDateTime.of(2026, 4, 5, 10, 0));
        record.setUpdatedAt(LocalDateTime.of(2026, 4, 5, 10, 0));
        record.setCreatedBy(user);
        return record;
    }
}
