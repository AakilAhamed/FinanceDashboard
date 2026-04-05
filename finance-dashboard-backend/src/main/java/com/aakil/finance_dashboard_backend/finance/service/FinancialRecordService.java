package com.aakil.finance_dashboard_backend.finance.service;

import com.aakil.finance_dashboard_backend.finance.dto.CreateFinancialRecordRequest;
import com.aakil.finance_dashboard_backend.finance.dto.FinancialRecordResponse;
import com.aakil.finance_dashboard_backend.finance.dto.UpdateFinancialRecordRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FinancialRecordService {
    FinancialRecordResponse createRecord(CreateFinancialRecordRequest request);

    FinancialRecordResponse getRecordById(Long id);

    Page<FinancialRecordResponse> getAllRecords(int page, int size);

    FinancialRecordResponse updateRecord(Long id, UpdateFinancialRecordRequest request);

    void deleteRecord(Long id);
}