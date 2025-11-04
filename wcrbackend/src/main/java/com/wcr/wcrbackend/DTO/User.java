package com.wcr.wcrbackend.DTO;

import java.util.List;

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
public class User {
	private String user_id,user_name,password,email_id,department_fk,contract_id_code,designation,reporting_to_id_srfk,hod_user_id_fk,dy_hod_user_id_fk,user_role_name_fk,mobile_number,personal_contact_number,landline,extension,pmis_key_fk,remarks,user_image,keyAvailability;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	private String is_password_encrypted,is_test_env_enabled,current_url,decrypted_password;
	
	private String reporting_to_name,project_id_fk,reporting_to_designation,department,department_name,user_access_type,responsible_people_id_fk,user_access_table,user_id_fk,user_type_fk,loginCount,Structure,
	last_login,number_of_logins,user_role_name,user_access_type_fk,access_value,contract_id,contract_name,module_name,work_id,work_id_fk,work_name,
	access_value_id,access_value_name,user_role_code,last7DaysLogins,last30DaysLogins,login_event_date,login_event_type,login_event_type_fk,single_login_session_id,OTP,
	from_date,to_date,Created_by_user_id_fk,contract_short_name,contract_id_fk,contract_permission_checkbox,execution_permission_checkbox,risk_permission_checkbox
	,la_permission_checkbox,department_id_fk,us_permission_checkbox,rr_permission_checkbox,executive_user_id_fk,structure_id_fk,land_work,us_work,rr_work,id, module_fk, executive_id_fk, soft_delete_status;
	long user_leave_id;
	
	private List<User> departmentList,DesignationsList,UserLoginList;
	
	private List<User> userPermissions,reportingToPersonsList;
	
	private List<User>  contractExecutivesList, structureExecutivesList, executivesList, riskExecutivesList, landExecutivesList, utilityExecutivesList, rrExecutivesList;
	
	private MultipartFile fileName;
	
	private MultipartFile userImageFile;
	
	private String[] user_access_types,user_access_values,permissions_check,contract_ids,structures;
	
	private String[] modules,responsible_persons;
	

	private String system_ipa,public_ipa;
	
	private String user_login_details_id;
}
