package com.aakil.finance_dashboard_backend.finance.service;

import com.aakil.finance_dashboard_backend.finance.dto.CreateFinancialRecordRequest;
import com.aakil.finance_dashboard_backend.finance.dto.FinancialRecordResponse;
import com.aakil.finance_dashboard_backend.finance.dto.UpdateFinancialRecordRequest;
import com.aakil.finance_dashboard_backend.finance.entity.FinancialRecord;
import com.aakil.finance_dashboard_backend.finance.repository.FinancialRecordRepository;
import com.aakil.finance_dashboard_backend.user.entity.User;
import com.aakil.finance_dashboard_backend.user.entity.UserStatus;
import com.aakil.finance_dashboard_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialRecordServiceImpl implements FinancialRecordService{

    private final UserRepository userRepository;
    private final FinancialRecordRepository financialRecordRepository;

    @Override
    public FinancialRecordResponse createRecord(CreateFinancialRecordRequest request) {
        User user = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new RuntimeException("User not Found"));
        if(user.getStatus() == UserStatus.INACTIVE){
            throw new RuntimeException("User must be ACTIVE to create Records");
        }
        FinancialRecord record = toEntity(request);

        FinancialRecord savedRecord = financialRecordRepository.save(record);

        return toResponse(savedRecord);
    }

    @Override
    public FinancialRecordResponse getRecordById(Long id) {
        FinancialRecord record = financialRecordRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Record Not Found"));

        return toResponse(record);
    }

    @Override
    public Page<FinancialRecordResponse> getAllRecords(int page, int size) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page must be 0 or greater");
        }
        if (size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Size must be greater than 0");
        }

        return financialRecordRepository.findByDeletedFalse(PageRequest.of(page, size))
                .map(this::toResponse);
    }

    @Override
    public FinancialRecordResponse updateRecord(Long id, UpdateFinancialRecordRequest request) {
        FinancialRecord record = financialRecordRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Record Not Found"));
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDescription(request.getDescription());
        record.setTransactionDate(request.getTransactionDate());
        FinancialRecord updatedRecord = financialRecordRepository.save(record);
        return toResponse(updatedRecord);
    }

    @Override
    public void deleteRecord(Long id) {
        FinancialRecord record = financialRecordRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Record not Found"));
        record.setDeleted(true);
        financialRecordRepository.save(record);

    }

    private FinancialRecord toEntity(CreateFinancialRecordRequest request){
        FinancialRecord record = new FinancialRecord();

        record.setAmount(request.getAmount());
        record.setCategory(request.getCategory());
        record.setDescription(request.getDescription());
        record.setType(request.getType());
        record.setTransactionDate(request.getTransactionDate());
        User createdBy = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        record.setCreatedBy(createdBy);

        return record;
    }

    private FinancialRecordResponse toResponse(FinancialRecord record){
        FinancialRecordResponse response = new FinancialRecordResponse();

        response.setId(record.getId());
        response.setAmount(record.getAmount());
        response.setType(record.getType());
        response.setCategory(record.getCategory());
        response.setDescription(record.getDescription());
        response.setTransactionDate(record.getTransactionDate());
        User createdBy = record.getCreatedBy();
        response.setCreatedById(createdBy.getId());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        response.setCreatedByName(createdBy.getName());


        return response;
    }
}
