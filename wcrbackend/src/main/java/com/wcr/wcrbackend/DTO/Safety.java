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
public class Safety {
	private String safety_id,contract_id_fk,title,description,date,location,latitude,longitude,reported_by,responsible_person,department_fk,
	category_fk,impact_fk,root_cause_fk,status_fk,closure_date,lti_hours,equipment_impact,people_impact,work_impact,committee_formed_fk,committee_required_fk,
	investigation_completed,corrective_measure_short_term,corrective_measure_long_term,compensation,payment_date,remarks,short_description,work_short_name,
	category,impact,root_cause,status,contract_id,contract_name,work_id_fk,work_name,project_id_fk,project_name,status_remark_fk,
	department,department_name,attachment,contract_short_name,hod_user_id_fk,dy_hod_user_id_fk,committee_member_name,designation,hod_name,reporting_to_id_srfk,user_name,user_id,
	contractor_name,safety_id_fk,compensation_units,id, unit, value,compensation_unit,hod_designation,dyhod_designation,reported_by_email_id,responsible_person_email_id,existing_status_fk,
	existing_responsible_person,existing_escalated_to,contract_hod_user_id,contract_dyhod_user_id,created_by_user_id_fk,responsible_person_user_id,contract_hod_email_id,
	contract_dyhod_email_id,created_by_email_id,user_role_code,modified_by,modified_date,reported_by_user_id,committe_members,existing_committe_members,nominated_authority,nominated_authority_email_id,
	approve_corrective_measure,safety_incident,work_code,safety_seq_id,searchStr;

	private MultipartFile safetyFile;
	
	private List<MultipartFile> safetyFiles;
	private String[] safetyFileNames,committee_member_names;
	
	private List<Safety> safetyFilesList;
	private List<Safety> safetyCommitteeMembersList;

	private String mail_body_header;
}
