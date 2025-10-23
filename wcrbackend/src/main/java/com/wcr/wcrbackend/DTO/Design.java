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
public class Design {
	private String design_id, contract_id_fk, department_id_fk,drawing_type_code,department_name, work_name,hod, dy_hod,designation,hod_designation,dy_hod_designation, prepared_by_id_fk, consultant_contract_id_fk,
	proof_consultant_contract_id_fk,contract_name, structure_type_fk, component, drawing_type_fk, contractor_drawing_no, mrvc_drawing_no,project_id_fk,
	division_drawing_no, hq_drawing_no, drawing_title, planned_start, planned_finish, revision,clearance_to_consultant, consultant_submission,work_id_fk,department_fk,work_short_name,
	mrvc_reviewed, divisional_approval, hq_approval, gfc_released, as_built_status, as_built_date, remarks,submited_to_proof_consultant_fk,approval_by_proof_consultant_fk,
	 revision_status_fk,revision_date,revision_remarks,divisional_submission_fk,hq_submission_fk,attachment,is_there_issue,issue_description,issue_priority_id,
	 issue_category_id,created_by_user_id_fk,contract_short_name,submitted_to_division,submitted_to_hq,query_raised_by_division,query_replied_to_division,query_raised_by_hq,
	 query_replied_to_hq,crs_sanction_fk,submitted_for_crs_sanction,query_raised_for_crs_sanction,query_replied_for_crs_sanction,crs_sanction_approved,design_seq_id,searchStr,
	 project_id,work_id,work_code,consult_contarct,proof_consult_contarct,project_name,contract_id,required_date,revision_status,status,uploaded_by_user_id_fk,design_data_id, railway_id,uploaded_file, user_id,uploaded_on,user_role_code,user_name,modified_by,modified_date;
	
	private String id, threepvc,drawing_no,structure_name,correspondence_letter_no,upload_file,design_file_id,design_id_fk,latest,submission_purpose,structure_id_fk,design_file_type,submssionpurpose,design_file_types,name,structure, stage_fk,design_status_submit, approving_railway,approval_authority_fk,submitted_by, submitted_to, submitted_date, submssion_purpose,design_file_type_fk,current,
	
	element,activity,target_date,actual_date,scope,task_code;
	
	private String []ids,taskcodes, design_id_fks, design_file_ids,stage_fks,designDocumentFileNames,designDocumentNames, submitted_bys, submitted_tos, submitted_dates, submssion_purposes,design_file_type_fks,currents;
	
	private String[] revisions,revision_dates,UploadFileNames,drawing_nos,correspondence_letter_nos,upload_files,latests, consultant_submissions,design_file_typess, mrvc_revieweds, divisional_approvals, hq_approvals, revision_status_fks, remarkss;
	
	private String[]  p6activityids,structures,components,elements,activities,scopes,target_dates,actual_dates,designremarks;
	
	private MultipartFile designFile;
	private MultipartFile[] designDocumentFiles,uploadFiles;
	
	private List<Design> designRevisions;
	private List<Design> designStatusList;
	private List<MultipartFile> designFiles;
	private List<Design> designFilesList;
	private String[] designFileNames;
}
