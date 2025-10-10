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
public class OverviewDashboard {
	private String dashboard_id,dashboard_name,dashboard_icon,dashboard_url,source_table_name,source_field_name,source_field_value,show_left_menu;
	private List<OverviewDashboard> formsSubMenu;
	
	private String filter_id, left_menu_id_fk, filters_table, filter_label_name, filter_column_name,ipaddress,
	default_filter_column,default_filter_value, selected_value, priority, 
	filter_column_id,filters_reference_table,filter_option_id,filter_option_value,work_id,parent_id,params,
	query_for_work_search,query_for_filter_options,source_table_alias_name,filters_table_alias_name,order_by,is_first_option_selected,union_all,dashboard_type,dashboard_type_fk,
	 user_type_fk,user_role_name_fk,user_id,level,project_id,la_sub_category_fk,la_sub_category,accessibility;
	
	private List<OverviewDashboard> filter;

	private int work_exists_or_not;
}
