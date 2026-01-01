package com.wcr.wcrbackend.DTO;

import org.springframework.web.multipart.MultipartFile;

public class P6Data {
	
	private String p6_activity_id, p6_task_code,p6_wbs_code_fk, p6_activity_name, status_fk, baseline_start, baseline_finish, 
	start, finish, p6_float,p6_wbs_code, contract_id_fk, fob_id_fk, p6_wbs_name, p6_wbs_parent_code,contract_short_name, p6_wbs_category_fk,
	p6_activity_data_id, data_date, soft_delete_status_fk, p6_file_path, uploaded_by_user_id_fk,work_id_fk,work_short_name,work_name, uploaded_date,upload_type,contract_id,
	contract_name,fob_id,fob_name,created_by_user_id_fk,user_name,designation,isRevised,structure_type_fk,structure,
	 task_code, structure_id_fk , from_structure_id, to_structure_id, section, line, component, component_id , original_duration,
	unit,  component_details, project_id_fk,project_name,remarks, created_date, modified_date, modified_by_user_id_fk,user_role_code,user_type_fk,user_id,scope, completed, weightage,
	task_id;
	
	 
	
	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getOriginal_duration() {
		return original_duration;
	}

	public void setOriginal_duration(String original_duration) {
		this.original_duration = original_duration;
	}

	public String getUser_type_fk() {
		return user_type_fk;
	}

	public void setUser_type_fk(String user_type_fk) {
		this.user_type_fk = user_type_fk;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_role_code() {
		return user_role_code;
	}

	public void setUser_role_code(String user_role_code) {
		this.user_role_code = user_role_code;
	}

	public String getStructure_type_fk() {
		return structure_type_fk;
	}

	public void setStructure_type_fk(String structure_type_fk) {
		this.structure_type_fk = structure_type_fk;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public String getTask_code() {
		return task_code;
	}

	public void setTask_code(String task_code) {
		this.task_code = task_code;
	}

	public String getStructure_id_fk() {
		return structure_id_fk;
	}

	public void setStructure_id_fk(String structure_id_fk) {
		this.structure_id_fk = structure_id_fk;
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

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
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

	private MultipartFile p6dataFile;
	

	public String getIsRevised() {
		return isRevised;
	}

	public void setIsRevised(String isRevised) {
		this.isRevised = isRevised;
	}

	public String getWork_short_name() {
		return work_short_name;
	}

	public void setWork_short_name(String work_short_name) {
		this.work_short_name = work_short_name;
	}

	public String getWork_name() {
		return work_name;
	}

	public void setWork_name(String work_name) {
		this.work_name = work_name;
	}

	public String getWork_id_fk() {
		return work_id_fk;
	}

	public void setWork_id_fk(String work_id_fk) {
		this.work_id_fk = work_id_fk;
	}

	public String getContract_short_name() {
		return contract_short_name;
	}

	public void setContract_short_name(String contract_short_name) {
		this.contract_short_name = contract_short_name;
	}

	public String getCreated_by_user_id_fk() {
		return created_by_user_id_fk;
	}

	public void setCreated_by_user_id_fk(String created_by_user_id_fk) {
		this.created_by_user_id_fk = created_by_user_id_fk;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getFob_id() {
		return fob_id;
	}

	public void setFob_id(String fob_id) {
		this.fob_id = fob_id;
	}

	public String getFob_name() {
		return fob_name;
	}

	public void setFob_name(String fob_name) {
		this.fob_name = fob_name;
	}

	public MultipartFile getP6dataFile() {
		return p6dataFile;
	}

	public void setP6dataFile(MultipartFile p6dataFile) {
		this.p6dataFile = p6dataFile;
	}

	public String getP6_activity_id() {
		return p6_activity_id;
	}

	public void setP6_activity_id(String p6_activity_id) {
		this.p6_activity_id = p6_activity_id;
	}

	public String getP6_task_code() {
		return p6_task_code;
	}

	public void setP6_task_code(String p6_task_code) {
		this.p6_task_code = p6_task_code;
	}

	public String getP6_wbs_code_fk() {
		return p6_wbs_code_fk;
	}

	public void setP6_wbs_code_fk(String p6_wbs_code_fk) {
		this.p6_wbs_code_fk = p6_wbs_code_fk;
	}

	public String getP6_activity_name() {
		return p6_activity_name;
	}

	public void setP6_activity_name(String p6_activity_name) {
		this.p6_activity_name = p6_activity_name;
	}

	public String getStatus_fk() {
		return status_fk;
	}

	public void setStatus_fk(String status_fk) {
		this.status_fk = status_fk;
	}

	public String getBaseline_start() {
		return baseline_start;
	}

	public void setBaseline_start(String baseline_start) {
		this.baseline_start = baseline_start;
	}

	public String getBaseline_finish() {
		return baseline_finish;
	}

	public void setBaseline_finish(String baseline_finish) {
		this.baseline_finish = baseline_finish;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public String getP6_float() {
		return p6_float;
	}

	public void setP6_float(String p6_float) {
		this.p6_float = p6_float;
	}

	public String getP6_wbs_code() {
		return p6_wbs_code;
	}

	public void setP6_wbs_code(String p6_wbs_code) {
		this.p6_wbs_code = p6_wbs_code;
	}

	public String getContract_id_fk() {
		return contract_id_fk;
	}

	public void setContract_id_fk(String contract_id_fk) {
		this.contract_id_fk = contract_id_fk;
	}

	public String getFob_id_fk() {
		return fob_id_fk;
	}

	public void setFob_id_fk(String fob_id_fk) {
		this.fob_id_fk = fob_id_fk;
	}

	public String getP6_wbs_name() {
		return p6_wbs_name;
	}

	public void setP6_wbs_name(String p6_wbs_name) {
		this.p6_wbs_name = p6_wbs_name;
	}

	public String getP6_wbs_parent_code() {
		return p6_wbs_parent_code;
	}

	public void setP6_wbs_parent_code(String p6_wbs_parent_code) {
		this.p6_wbs_parent_code = p6_wbs_parent_code;
	}

	public String getP6_wbs_category_fk() {
		return p6_wbs_category_fk;
	}

	public void setP6_wbs_category_fk(String p6_wbs_category_fk) {
		this.p6_wbs_category_fk = p6_wbs_category_fk;
	}

	public String getP6_activity_data_id() {
		return p6_activity_data_id;
	}

	public void setP6_activity_data_id(String p6_activity_data_id) {
		this.p6_activity_data_id = p6_activity_data_id;
	}

	public String getData_date() {
		return data_date;
	}

	public void setData_date(String data_date) {
		this.data_date = data_date;
	}

	public String getSoft_delete_status_fk() {
		return soft_delete_status_fk;
	}

	public void setSoft_delete_status_fk(String soft_delete_status_fk) {
		this.soft_delete_status_fk = soft_delete_status_fk;
	}

	public String getP6_file_path() {
		return p6_file_path;
	}

	public void setP6_file_path(String p6_file_path) {
		this.p6_file_path = p6_file_path;
	}

	public String getUploaded_by_user_id_fk() {
		return uploaded_by_user_id_fk;
	}

	public void setUploaded_by_user_id_fk(String uploaded_by_user_id_fk) {
		this.uploaded_by_user_id_fk = uploaded_by_user_id_fk;
	}

	public String getUploaded_date() {
		return uploaded_date;
	}

	public void setUploaded_date(String uploaded_date) {
		this.uploaded_date = uploaded_date;
	}

	public String getUpload_type() {
		return upload_type;
	}

	public void setUpload_type(String upload_type) {
		this.upload_type = upload_type;
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

	public String getProject_id_fk() {
		return project_id_fk;
	}

	public void setProject_id_fk(String project_id_fk) {
		this.project_id_fk = project_id_fk;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	
	

}
