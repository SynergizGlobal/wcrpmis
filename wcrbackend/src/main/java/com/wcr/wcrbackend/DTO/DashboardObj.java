package com.wcr.wcrbackend.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class DashboardObj {
	private String dashboard_id, dashboard_name, work_id_fk, contract_id_fk, module_name_fk, parent_dashboard_id_sr_fk, dashboard_url, mobile_view, dashboard_type_fk, priority, icon_path, published_by_user_id_fk,
	published_on, modified_by_user_id_fk,project_id_fk, modified_on, soft_delete_status_fk,folder,work_short_name,contract_short_name,access_type,access_value,access_value_id,access_value_name,user_role_access,user_type_access,user_access;
	
	private String[] access_types,access_values;
	
	List<DashboardObj> accessPermissions;
	
}
