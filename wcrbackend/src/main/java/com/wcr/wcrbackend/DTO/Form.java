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
public class Form {
	private String form_id,module_name_fk,form_name,parent_form_id_sr_fk,web_form_url,mobile_form_url,priority,soft_delete_status_fk,
	folder_name,form_access_id,form_id_fk,access_type,access_value,access_value_id,access_value_name,display_in_mobile,
	user_role_access,user_type_access,user_access,url_type;
	
	private String[] access_types,access_values;
	
	private List<Form> accessPermissions;
}
