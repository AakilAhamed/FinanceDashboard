package com.aakil.finance_dashboard_backend.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatusResponse {
    private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    private long totalRecords;
    private long deletedRecords;
}
