package com.aakil.finance_dashboard_backend.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordCountByUserResponse {
    private Long userId;
    private String userName;
    private long recordCount;
}
