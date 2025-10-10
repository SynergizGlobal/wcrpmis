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
public class Alerts {
	private String alert_id, alert_level, alert_type_fk, alert_type_id, alert_type, contract_id, created_date,
			alert_status, alert_value, count, hod, work_short_name, contract_short_name, hod_email, dy_hod_email,
			contract_id_fk, contractor_id_fk, contractor_id, contractor_name, remarks, email_id, user_id, work_id,
			work_id_fk, contract_name, work_name, user_name, designation, user_role_name, alerts_user_id, read_time,
			condition_value, bg_condition_value, insure_condition_value, cp_condition_value, cv_condition_value;

	private String redirect_url, department_name, responsible_person, escalated_to, hod_user_id_fk, dy_hod_user_id_fk,
			created_by_user_id_fk, user_id_fk, alert_level_fk, sub_work, reporting_to_user_id, reporting_to_email_id,
			alert_type_image, details, validity;

	private String status, uploaded_by_user_id_fk, corrective_measure, user_role_name_fk, user_type_fk, owner,
			owner_user_id, responsible_person_user_id, assessment_date, module_name, incharge_user_id_fk,
			amendment_not_required_in_contract, user_role_code, rowspan;
}