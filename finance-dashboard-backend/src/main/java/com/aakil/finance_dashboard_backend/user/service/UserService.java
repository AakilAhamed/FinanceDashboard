package com.aakil.finance_dashboard_backend.user.service;

import com.aakil.finance_dashboard_backend.user.dto.CreateUserRequest;
import com.aakil.finance_dashboard_backend.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    void deactivateUser(Long id);

    void activateUser(Long id);
}
