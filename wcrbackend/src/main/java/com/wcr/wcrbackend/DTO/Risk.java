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
public class Risk {
	
	private String risk_id_pk,id, project_name,work_name,project_id_fk, work_id_fk, risk_id, sub_area_fk, date_of_identification,area,risk_revision_id, risk_id_pk_fk, date,
	priority, probability,item_no,priority_fk,work_short_name, impact, owner, responsible_person,assessment_date, risk_action_id,mitigation_plan, action_taken, attachment,
	sub_area, risk_area_fk,classification,owner_user_id,responsible_user_id,atr_date,work_id,risk_rating,status,mitigation_plan_old,sub_work,area_item_no,sub_area_item_no,
	risk_revision_id_fk,user_type,user_role_code,user_id,user_designation,created_by_user_id_fk,user_name,designation,alerts_user_id;

	private String risk_upload_id,remarks,uploaded_by_user_id_fk,uploaded_on,uploaded_by,reporting_to_user_id,message_id,hod_user_id_fk,total,priority_risks,low_risks;
	private boolean readonlyForm = true;
	private MultipartFile riskFile;
	private MultipartFile riskAssessmentFile;
	
	private List<Risk> risks; 
	private List<Risk> riskActions; 
	private List<String> atr_dates,action_takens,atr_dates_old,action_takens_old;
}
