package com.wcr.wcrbackend.DTO;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
public class UtilityShifting {
	private String id, utility_shifting_id,user_id,user_name,designation, work_id_fk, identification, location_name, reference_number, utility_description,
	utility_type_fk, utility_category_fk, owner_name, execution_agency_fk, contract_id_fk, start_date, scope, completed,
	shifting_status_fk, shifting_completion_date, remarks, latitude, longitude, impacted_contract_id_fk, requirement_stage_fk, planned_completion_date,
	contract_id,contract_name,work_name,project_id_fk,project_name,department_fk,Status_fk,work_short_name,contract_short_name,user_type_fk,
	category_fk,user_role_code,hod_user_id_fk,unit_fk,attachment,progress_date,progress_of_work,executive_user_id_fk,name,utility_shifting_file_type,created_by_user_id_fk,modified_by,modified_date,
	total,inprogress,pending,utility_data_id, uploaded_file, status, uploaded_by_user_id_fk, uploaded_on,Work_code,utilities,balance,remaining,mail_body_header;
	
	private String []  progress_dates, progress_of_works,attachment_file_types,attachmentNames,attachmentFileNames;
	
	private List<MultipartFile> utilityShiftingFiles;
	private List<UtilityShifting> utilityList,processList,report1List,report2List;;
	private MultipartFile utilityFile;
	
	private List<UtilityShifting> utilityShiftingFilesList;
	private List<UtilityShifting> utilityShiftingProgressDetailsList;
	
	private String custodian,executed_by,impacted_element,affected_structures,target_date,contract_id_code,reporting_to_id_srfk,
	latest_progress_date,latest_progress_update,chainage;
	
	public boolean checkNullOrEmpty() throws IllegalAccessException {
		boolean flag = true;
		try {
			for (Field f : getClass().getDeclaredFields())
		        if (!StringUtils.isEmpty(f.get(this)))
		        	flag = false;
		} catch (Exception e) {
			
		}
	    
	    return flag;            
	}

}

