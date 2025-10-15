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
public class Issue {
	private String issue_id,contract_id_fk,activity,title,short_description,description,date,location,latitude,longitude,reported_by,responsible_person,department_fk,user_name,
	priority_fk,category_fk,status_fk,corrective_measure,resolved_date,escalated_to,remarks,priority,category,status,contract_id,contract_name,work_id_fk,work_name,work_short_name,
	project_id_fk,project_name,activity_name,strip_chart_component_fk,department,department_name,attachments,railway_id,railway_name,zonal_railway_fk,contract_short_name,
	other_organization,escalation_date,contractor_id_fk,contractor_id,contractor_name,hod_user_id_fk,designation,hod_name,pending_since,hod,
	reported_by_user_id,responsible_person_user_id,escalated_to_user_id,reported_by_designation,responsible_person_designation,escalated_to_designation,
	reported_by_email_id,responsible_person_email_id,escalated_to_email_id,assigned_date,contract_hod_email_id,contract_dyhod_email_id,contract_type_fk,
	dy_hod_user_id_fk,user_type,user_role_code,user_id,file_name,message_id,remarks_old,remarks_new,hod_designation,comment,existingAssignedPerson, 
	dyHod_designation,pending_Since,other_org_resposible_person_name,other_org_resposible_person_designation,created_by,alerts_user_id,modified_by,modified_date,structure,component;
	
	private String contract_hod_user_id,contract_dyhod_user_id,created_by_user_id_fk,created_date,created_by_email_id,
	existing_status_fk,mail_body_header,existing_responsible_person,existing_escalated_to,issue_file_type_fk,issue_file_type,
	issue_file_id,assigned_person_user_id_fk,total_issues,closed_issues,open_issues,la_id,cmd_email,dp_email,dt_email,contractor_email,sse_email,pe_email,aen_mail,
	cmd_name,dp_name,dt_name,dyhod_name,pe_name,aen_name,Sse_name,curdate,issues_related_to,action,ass_email,ass_name,assr_email,assr_name,rs_email,rs_name,pm_email,pm_name,
	ae_email,ae_name,spe_email,spe_name,actionremarks,title1
	;

	private boolean readonlyForm = true;
	private MultipartFile issueFile;
	
	//private List<MultipartFile> issueFiles;
	private MultipartFile[] issueFiles;
	private String[] issueFileNames,attachemnts,issue_file_types,issue_file_ids;
	private List<Issue> issueFilesList;
}
