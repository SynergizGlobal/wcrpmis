package com.wcr.wcrbackend.reference.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Safety {
	
	private String structure_type,status,utility_alignment,general_status,la_land_status,contract_status,impact,utility_category,short_description,root_cause,land_type,approval_status,document_type,priority,status_of,risk_priority,project_priority,sub_category,category,p6_wbs_category,module_name,soft_delete_status_fk;
	private String Table_name,column_name,execution_agency,utility_type,utility_status,requirement_stage,tName,count,value_new,value_old,utility_shifting_file_type;
	private List<Safety> dList;
	private List<Safety> dList1;
	private List<Safety> tablesList;
	private List<Safety> countList;
	
}