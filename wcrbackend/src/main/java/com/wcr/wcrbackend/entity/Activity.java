package com.wcr.wcrbackend.entity;

import org.springframework.web.multipart.MultipartFile;

public class Activity {
	private String activity_id,contract_id_fk,structure_type_fk,section,line,structure,component,component_id,order,activity_name,planned_start,
	planned_finish,actual_start,actual_finish,unit,scope,completed,weightage,component_details,remarks,
	contract_id,contract_name,contract_short_name,struture_type,work_id,work_name,work_short_name,structure_type,
	created_date,created_by_user_id_fk,modified_date,modified_by_user_id_fk,activity_id_fk,
	progress_id,progress_date,completed_scope,attachment_url,work_id_fk,fob_id,user_id,user_name,dyhod_user_id_fk,department_fk,updated_by_user_id_fk,
	approved_on,rejected_on,approval_status_fk,department_name,user_role_code,updated_on,updated_by,cumulative_completed,actual_for_the_day,
	total_scope,remaining_scope,approved_or_rejected_by,message_id,updated_scope,designation,from_structure_id,to_structure_id,order_x,order_y;
	
	private String activities_data_id,uploaded_file,status,uploaded_by_user_id_fk,uploaded_on,p6_task_code,project_id_fk,user_type_fk,component_per_prior,structure_per_prior,
	component_per_post,structure_per_post;
	
	
	private boolean message_flag;
	private String message;
	
	private MultipartFile uploadFile;


	public String getApproved_on() {
		return approved_on;
	}

	public void setApproved_on(String approved_on) {
		this.approved_on = approved_on;
	}

	public String getRejected_on() {
		return rejected_on;
	}

	public void setRejected_on(String rejected_on) {
		this.rejected_on = rejected_on;
	}

	public String getApproval_status_fk() {
		return approval_status_fk;
	}

	public void setApproval_status_fk(String approval_status_fk) {
		this.approval_status_fk = approval_status_fk;
	}

	public String getFob_id() {
		return fob_id;
	}

	public void setFob_id(String fob_id) {
		this.fob_id = fob_id;
	}

	public String getActivities_data_id() {
		return activities_data_id;
	}

	public void setActivities_data_id(String activities_data_id) {
		this.activities_data_id = activities_data_id;
	}

	public String getUploaded_file() {
		return uploaded_file;
	}

	public void setUploaded_file(String uploaded_file) {
		this.uploaded_file = uploaded_file;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUploaded_by_user_id_fk() {
		return uploaded_by_user_id_fk;
	}

	public void setUploaded_by_user_id_fk(String uploaded_by_user_id_fk) {
		this.uploaded_by_user_id_fk = uploaded_by_user_id_fk;
	}

	public String getUploaded_on() {
		return uploaded_on;
	}

	public void setUploaded_on(String uploaded_on) {
		this.uploaded_on = uploaded_on;
	}

	public String getProgress_id() {
		return progress_id;
	}

	public void setProgress_id(String progress_id) {
		this.progress_id = progress_id;
	}

	public String getProgress_date() {
		return progress_date;
	}

	public void setProgress_date(String progress_date) {
		this.progress_date = progress_date;
	}

	public String getCompleted_scope() {
		return completed_scope;
	}

	public void setCompleted_scope(String completed_scope) {
		this.completed_scope = completed_scope;
	}

	public String getAttachment_url() {
		return attachment_url;
	}

	public void setAttachment_url(String attachment_url) {
		this.attachment_url = attachment_url;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public String getContract_id_fk() {
		return contract_id_fk;
	}

	public void setContract_id_fk(String contract_id_fk) {
		this.contract_id_fk = contract_id_fk;
	}

	public String getStructure_type_fk() {
		return structure_type_fk;
	}

	public void setStructure_type_fk(String structure_type_fk) {
		this.structure_type_fk = structure_type_fk;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getComponent_id() {
		return component_id;
	}

	public void setComponent_id(String component_id) {
		this.component_id = component_id;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public String getPlanned_start() {
		return planned_start;
	}

	public void setPlanned_start(String planned_start) {
		this.planned_start = planned_start;
	}

	public String getPlanned_finish() {
		return planned_finish;
	}

	public void setPlanned_finish(String planned_finish) {
		this.planned_finish = planned_finish;
	}

	public String getActual_start() {
		return actual_start;
	}

	public void setActual_start(String actual_start) {
		this.actual_start = actual_start;
	}

	public String getActual_finish() {
		return actual_finish;
	}

	public void setActual_finish(String actual_finish) {
		this.actual_finish = actual_finish;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getWeightage() {
		return weightage;
	}

	public void setWeightage(String weightage) {
		this.weightage = weightage;
	}

	public String getComponent_details() {
		return component_details;
	}

	public void setComponent_details(String component_details) {
		this.component_details = component_details;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getContract_id() {
		return contract_id;
	}

	public void setContract_id(String contract_id) {
		this.contract_id = contract_id;
	}

	public String getContract_name() {
		return contract_name;
	}

	public void setContract_name(String contract_name) {
		this.contract_name = contract_name;
	}

	public String getContract_short_name() {
		return contract_short_name;
	}

	public void setContract_short_name(String contract_short_name) {
		this.contract_short_name = contract_short_name;
	}

	public String getStruture_type() {
		return struture_type;
	}

	public void setStruture_type(String struture_type) {
		this.struture_type = struture_type;
	}

	public MultipartFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getWork_id() {
		return work_id;
	}

	public void setWork_id(String work_id) {
		this.work_id = work_id;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public String getWork_name() {
		return work_name;
	}

	public void setWork_name(String work_name) {
		this.work_name = work_name;
	}

	public String getWork_short_name() {
		return work_short_name;
	}

	public void setWork_short_name(String work_short_name) {
		this.work_short_name = work_short_name;
	}

	public String getStructure_type() {
		return structure_type;
	}

	public void setStructure_type(String structure_type) {
		this.structure_type = structure_type;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getCreated_by_user_id_fk() {
		return created_by_user_id_fk;
	}

	public void setCreated_by_user_id_fk(String created_by_user_id_fk) {
		this.created_by_user_id_fk = created_by_user_id_fk;
	}

	public String getModified_date() {
		return modified_date;
	}

	public void setModified_date(String modified_date) {
		this.modified_date = modified_date;
	}

	public String getModified_by_user_id_fk() {
		return modified_by_user_id_fk;
	}

	public void setModified_by_user_id_fk(String modified_by_user_id_fk) {
		this.modified_by_user_id_fk = modified_by_user_id_fk;
	}

	public String getActivity_id_fk() {
		return activity_id_fk;
	}

	public void setActivity_id_fk(String activity_id_fk) {
		this.activity_id_fk = activity_id_fk;
	}

	public String getWork_id_fk() {
		return work_id_fk;
	}

	public void setWork_id_fk(String work_id_fk) {
		this.work_id_fk = work_id_fk;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getDyhod_user_id_fk() {
		return dyhod_user_id_fk;
	}

	public void setDyhod_user_id_fk(String dyhod_user_id_fk) {
		this.dyhod_user_id_fk = dyhod_user_id_fk;
	}

	public String getDepartment_fk() {
		return department_fk;
	}

	public void setDepartment_fk(String department_fk) {
		this.department_fk = department_fk;
	}

	public String getUpdated_by_user_id_fk() {
		return updated_by_user_id_fk;
	}

	public void setUpdated_by_user_id_fk(String updated_by_user_id_fk) {
		this.updated_by_user_id_fk = updated_by_user_id_fk;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getUser_role_code() {
		return user_role_code;
	}

	public void setUser_role_code(String user_role_code) {
		this.user_role_code = user_role_code;
	}

	public String getUpdated_on() {
		return updated_on;
	}

	public void setUpdated_on(String updated_on) {
		this.updated_on = updated_on;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public String getCumulative_completed() {
		return cumulative_completed;
	}

	public void setCumulative_completed(String cumulative_completed) {
		this.cumulative_completed = cumulative_completed;
	}

	public String getActual_for_the_day() {
		return actual_for_the_day;
	}

	public void setActual_for_the_day(String actual_for_the_day) {
		this.actual_for_the_day = actual_for_the_day;
	}

	public String getTotal_scope() {
		return total_scope;
	}

	public void setTotal_scope(String total_scope) {
		this.total_scope = total_scope;
	}

	public String getRemaining_scope() {
		return remaining_scope;
	}

	public void setRemaining_scope(String remaining_scope) {
		this.remaining_scope = remaining_scope;
	}

	public boolean isMessage_flag() {
		return message_flag;
	}

	public void setMessage_flag(boolean message_flag) {
		this.message_flag = message_flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getApproved_or_rejected_by() {
		return approved_or_rejected_by;
	}

	public void setApproved_or_rejected_by(String approved_or_rejected_by) {
		this.approved_or_rejected_by = approved_or_rejected_by;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public String getUpdated_scope() {
		return updated_scope;
	}

	public void setUpdated_scope(String updated_scope) {
		this.updated_scope = updated_scope;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getFrom_structure_id() {
		return from_structure_id;
	}

	public void setFrom_structure_id(String from_structure_id) {
		this.from_structure_id = from_structure_id;
	}

	public String getTo_structure_id() {
		return to_structure_id;
	}

	public void setTo_structure_id(String to_structure_id) {
		this.to_structure_id = to_structure_id;
	}

	public String getOrder_x() {
		return order_x;
	}

	public void setOrder_x(String order_x) {
		this.order_x = order_x;
	}

	public String getOrder_y() {
		return order_y;
	}

	public void setOrder_y(String order_y) {
		this.order_y = order_y;
	}

	public String getP6_task_code() {
		return p6_task_code;
	}

	public void setP6_task_code(String p6_task_code) {
		this.p6_task_code = p6_task_code;
	}

	public String getProject_id_fk() {
		return project_id_fk;
	}

	public void setProject_id_fk(String project_id_fk) {
		this.project_id_fk = project_id_fk;
	}

	public String getUser_type_fk() {
		return user_type_fk;
	}

	public void setUser_type_fk(String user_type_fk) {
		this.user_type_fk = user_type_fk;
	}

	public String getComponent_per_prior() {
		return component_per_prior;
	}

	public void setComponent_per_prior(String component_per_prior) {
		this.component_per_prior = component_per_prior;
	}

	public String getStructure_per_prior() {
		return structure_per_prior;
	}

	public void setStructure_per_prior(String structure_per_prior) {
		this.structure_per_prior = structure_per_prior;
	}

	public String getComponent_per_post() {
		return component_per_post;
	}

	public void setComponent_per_post(String component_per_post) {
		this.component_per_post = component_per_post;
	}

	public String getStructure_per_post() {
		return structure_per_post;
	}

	public void setStructure_per_post(String structure_per_post) {
		this.structure_per_post = structure_per_post;
	}
	
}
