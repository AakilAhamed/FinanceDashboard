package com.aakil.finance_dashboard_backend.common.response;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}