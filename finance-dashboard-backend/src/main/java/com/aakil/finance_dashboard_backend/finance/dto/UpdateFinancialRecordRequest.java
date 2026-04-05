package com.aakil.finance_dashboard_backend.finance.dto;

import com.aakil.finance_dashboard_backend.finance.entity.RecordType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFinancialRecordRequest {
    @Positive
    private BigDecimal amount;

    @NotNull
    private RecordType type;

    @NotBlank
    private String category;

    @NotBlank
    private String description;

    @PastOrPresent
    private LocalDate transactionDate;

    @NotNull
    private Long createdByUserId;
}
