package com.aakil.finance_dashboard_backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private boolean success;
    private String message;
    private Map<String, String> errors;
    private LocalDateTime timestamp;
}
