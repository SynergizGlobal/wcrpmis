package com.wcr.wcrbackend.reference.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User  {
	private String user_id,user_name,password,email_id,department_fk,designation,reporting_to_id_srfk,user_role_name_fk,mobile_number,landline,extension,pmis_key_fk,remarks,user_image,keyAvailability;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	
	private String reporting_to_name,department,department_name,user_access_type,user_access_table,user_id_fk,
	last_login,number_of_logins,user_role_name,user_access_type_fk,access_value,contract_id,contract_name,module_name,work_id,work_name,
	access_value_id,access_value_name,user_role_code;
	
	private List<User> userPermissions;
	
	private MultipartFile fileName;
	
	private String[] user_access_types,user_access_values;

}
