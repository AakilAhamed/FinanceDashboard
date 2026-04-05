package com.aakil.finance_dashboard_backend.user.dto;

import com.aakil.finance_dashboard_backend.user.entity.Role;
import com.aakil.finance_dashboard_backend.user.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank( message = "Name is required")
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 to 20 Characters long")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "User Status is required")
    private UserStatus status;
}
