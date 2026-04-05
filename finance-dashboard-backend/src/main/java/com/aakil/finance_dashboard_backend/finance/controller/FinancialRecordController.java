package com.aakil.finance_dashboard_backend.finance.controller;

import com.aakil.finance_dashboard_backend.finance.dto.CreateFinancialRecordRequest;
import com.aakil.finance_dashboard_backend.finance.dto.FinancialRecordResponse;
import com.aakil.finance_dashboard_backend.finance.dto.UpdateFinancialRecordRequest;
import com.aakil.finance_dashboard_backend.finance.service.FinancialRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/records")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;

    @Operation(summary = "Create New Financial Record")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Financial Record Created Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User Not Found")
    })
    @PostMapping
    public ResponseEntity<FinancialRecordResponse> createRecord(
            @Valid @RequestBody CreateFinancialRecordRequest request) {

        FinancialRecordResponse response = financialRecordService.createRecord(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Fetch Financial Record by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Financial Record Fetched Successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Financial Record Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FinancialRecordResponse> getRecordById(
            @PathVariable Long id) {

        return ResponseEntity.ok(financialRecordService.getRecordById(id));
    }

    @Operation(summary = "Fetch All Financial Record")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Financial records fetched successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
        public ResponseEntity<Page<FinancialRecordResponse>> getAllRecords(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                return ResponseEntity.ok(financialRecordService.getAllRecords(page, size));
    }

    @Operation(summary = "Update Existing Financial Record")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Financial Record Updated Successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Financial Record Not Found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FinancialRecordResponse> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFinancialRecordRequest request) {

        FinancialRecordResponse response =
                financialRecordService.updateRecord(id, request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete Existing Financial Record")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Financial Record Deleted Successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Financial Record Not Found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long id) {

        financialRecordService.deleteRecord(id);

        return ResponseEntity.noContent().build();
    }
}