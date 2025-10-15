package com.wcr.wcrbackend.DTO;

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
public class FormHistory {
	private String form_history_id,work_id_fk,project_id_fk,contract_id_fk,module_name_fk,form_name,module_name,work_name,work,contract,form_action_type,form_details,created_by_user_id_fk,user,created_date,sub_work;
}