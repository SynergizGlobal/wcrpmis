package com.wcr.wcrbackend.DTO;

import lombok.Data;

@Data
public class DashboardFilterDTO {
    private String module_name_fk;
    private String dashboard_type_fk;
    private String soft_delete_status_fk;
}

