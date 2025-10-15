package com.wcr.wcrbackend.DTO;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class UtilityShifting {
	private String id, utility_shifting_id,user_id,user_name,designation, work_id_fk, identification, location_name, reference_number, utility_description,
	utility_type_fk, utility_category_fk, owner_name, execution_agency_fk, contract_id_fk, start_date, scope, completed,
	shifting_status_fk, shifting_completion_date, remarks, latitude, longitude, impacted_contract_id_fk, requirement_stage_fk, planned_completion_date,
	contract_id,contract_name,work_name,project_id_fk,project_name,department_fk,Status_fk,work_short_name,contract_short_name,user_type_fk,
	category_fk,user_role_code,hod_user_id_fk,unit_fk,attachment,progress_date,progress_of_work,executive_user_id_fk,name,utility_shifting_file_type,created_by_user_id_fk,modified_by,modified_date,
	total,inprogress,pending,utility_data_id, uploaded_file, status, uploaded_by_user_id_fk, uploaded_on,Work_code,utilities,balance,remaining,mail_body_header;
	
	private String []  progress_dates, progress_of_works,attachment_file_types,attachmentNames,attachmentFileNames;
	
	private List<MultipartFile> utilityShiftingFiles;
	private List<UtilityShifting> utilityList,processList,report1List,report2List;;
	private MultipartFile utilityFile;
	
	private List<UtilityShifting> utilityShiftingFilesList;
	private List<UtilityShifting> utilityShiftingProgressDetailsList;
	
	private String custodian,executed_by,impacted_element,affected_structures,target_date,contract_id_code,reporting_to_id_srfk,
	latest_progress_date,latest_progress_update,chainage;

	public String getCustodian() {
		return custodian;
	}

	public void setCustodian(String custodian) {
		this.custodian = custodian;
	}

	public String getExecuted_by() {
		return executed_by;
	}

	public void setExecuted_by(String executed_by) {
		this.executed_by = executed_by;
	}

	public String getImpacted_element() {
		return impacted_element;
	}

	public void setImpacted_element(String impacted_element) {
		this.impacted_element = impacted_element;
	}

	public String getAffected_structures() {
		return affected_structures;
	}

	public void setAffected_structures(String affected_structures) {
		this.affected_structures = affected_structures;
	}

	public String getTarget_date() {
		return target_date;
	}

	public void setTarget_date(String target_date) {
		this.target_date = target_date;
	}

	public String getUtilities() {
		return utilities;
	}

	public void setUtilities(String utilities) {
		this.utilities = utilities;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getRemaining() {
		return remaining;
	}

	public void setRemaining(String remaining) {
		this.remaining = remaining;
	}

	public List<UtilityShifting> getReport1List() {
		return report1List;
	}

	public void setReport1List(List<UtilityShifting> report1List) {
		this.report1List = report1List;
	}

	public List<UtilityShifting> getReport2List() {
		return report2List;
	}

	public void setReport2List(List<UtilityShifting> report2List) {
		this.report2List = report2List;
	}

	public String getWork_code() {
		return Work_code;
	}

	public void setWork_code(String work_code) {
		Work_code = work_code;
	}

	public String getUtility_data_id() {
		return utility_data_id;
	}

	public void setUtility_data_id(String utility_data_id) {
		this.utility_data_id = utility_data_id;
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

	public List<UtilityShifting> getUtilityList() {
		return utilityList;
	}

	public void setUtilityList(List<UtilityShifting> utilityList) {
		this.utilityList = utilityList;
	}

	public List<UtilityShifting> getProcessList() {
		return processList;
	}

	public void setProcessList(List<UtilityShifting> processList) {
		this.processList = processList;
	}

	public MultipartFile getUtilityFile() {
		return utilityFile;
	}

	public void setUtilityFile(MultipartFile utilityFile) {
		this.utilityFile = utilityFile;
	}

	public String getExecutive_user_id_fk() {
		return executive_user_id_fk;
	}

	public void setExecutive_user_id_fk(String executive_user_id_fk) {
		this.executive_user_id_fk = executive_user_id_fk;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUtility_shifting_id() {
		return utility_shifting_id;
	}

	public void setUtility_shifting_id(String utility_shifting_id) {
		this.utility_shifting_id = utility_shifting_id;
	}

	public String getWork_id_fk() {
		return work_id_fk;
	}

	public void setWork_id_fk(String work_id_fk) {
		this.work_id_fk = work_id_fk;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getReference_number() {
		return reference_number;
	}

	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}

	public String getUtility_description() {
		return utility_description;
	}

	public void setUtility_description(String utility_description) {
		this.utility_description = utility_description;
	}

	public String getUtility_type_fk() {
		return utility_type_fk;
	}

	public void setUtility_type_fk(String utility_type_fk) {
		this.utility_type_fk = utility_type_fk;
	}

	public String getUtility_category_fk() {
		return utility_category_fk;
	}

	public void setUtility_category_fk(String utility_category_fk) {
		this.utility_category_fk = utility_category_fk;
	}

	public String getOwner_name() {
		return owner_name;
	}

	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}

	public String getExecution_agency_fk() {
		return execution_agency_fk;
	}

	public void setExecution_agency_fk(String execution_agency_fk) {
		this.execution_agency_fk = execution_agency_fk;
	}

	public String getContract_id_fk() {
		return contract_id_fk;
	}

	public void setContract_id_fk(String contract_id_fk) {
		this.contract_id_fk = contract_id_fk;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
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

	public String getShifting_status_fk() {
		return shifting_status_fk;
	}

	public void setShifting_status_fk(String shifting_status_fk) {
		this.shifting_status_fk = shifting_status_fk;
	}

	public String getShifting_completion_date() {
		return shifting_completion_date;
	}

	public void setShifting_completion_date(String shifting_completion_date) {
		this.shifting_completion_date = shifting_completion_date;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getImpacted_contract_id_fk() {
		return impacted_contract_id_fk;
	}

	public void setImpacted_contract_id_fk(String impacted_contract_id_fk) {
		this.impacted_contract_id_fk = impacted_contract_id_fk;
	}

	public String getRequirement_stage_fk() {
		return requirement_stage_fk;
	}

	public void setRequirement_stage_fk(String requirement_stage_fk) {
		this.requirement_stage_fk = requirement_stage_fk;
	}

	public String getPlanned_completion_date() {
		return planned_completion_date;
	}

	public void setPlanned_completion_date(String planned_completion_date) {
		this.planned_completion_date = planned_completion_date;
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

	public String getWork_name() {
		return work_name;
	}

	public void setWork_name(String work_name) {
		this.work_name = work_name;
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

	public String getDepartment_fk() {
		return department_fk;
	}

	public void setDepartment_fk(String department_fk) {
		this.department_fk = department_fk;
	}

	public String getCategory_fk() {
		return category_fk;
	}

	public void setCategory_fk(String category_fk) {
		this.category_fk = category_fk;
	}

	public String getStatus_fk() {
		return Status_fk;
	}

	public void setStatus_fk(String status_fk) {
		Status_fk = status_fk;
	}

	public String getUser_role_code() {
		return user_role_code;
	}

	public void setUser_role_code(String user_role_code) {
		this.user_role_code = user_role_code;
	}

	public String getWork_short_name() {
		return work_short_name;
	}

	public void setWork_short_name(String work_short_name) {
		this.work_short_name = work_short_name;
	}

	public String getHod_user_id_fk() {
		return hod_user_id_fk;
	}

	public void setHod_user_id_fk(String hod_user_id_fk) {
		this.hod_user_id_fk = hod_user_id_fk;
	}

	public String getContract_short_name() {
		return contract_short_name;
	}

	public void setContract_short_name(String contract_short_name) {
		this.contract_short_name = contract_short_name;
	}

	public String getUnit_fk() {
		return unit_fk;
	}

	public void setUnit_fk(String unit_fk) {
		this.unit_fk = unit_fk;
	}

	public List<MultipartFile> getUtilityShiftingFiles() {
		return utilityShiftingFiles;
	}

	public void setUtilityShiftingFiles(List<MultipartFile> utilityShiftingFiles) {
		this.utilityShiftingFiles = utilityShiftingFiles;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String [] getProgress_dates() {
		return progress_dates;
	}

	public void setProgress_dates(String [] progress_dates) {
		this.progress_dates = progress_dates;
	}

	public String [] getProgress_of_works() {
		return progress_of_works;
	}

	public void setProgress_of_works(String [] progress_of_works) {
		this.progress_of_works = progress_of_works;
	}

	public List<UtilityShifting> getUtilityShiftingFilesList() {
		return utilityShiftingFilesList;
	}

	public void setUtilityShiftingFilesList(List<UtilityShifting> utilityShiftingFilesList) {
		this.utilityShiftingFilesList = utilityShiftingFilesList;
	}

	public List<UtilityShifting> getUtilityShiftingProgressDetailsList() {
		return utilityShiftingProgressDetailsList;
	}

	public void setUtilityShiftingProgressDetailsList(List<UtilityShifting> utilityShiftingProgressDetailsList) {
		this.utilityShiftingProgressDetailsList = utilityShiftingProgressDetailsList;
	}

	public String getProgress_date() {
		return progress_date;
	}

	public void setProgress_date(String progress_date) {
		this.progress_date = progress_date;
	}

	public String getProgress_of_work() {
		return progress_of_work;
	}

	public void setProgress_of_work(String progress_of_work) {
		this.progress_of_work = progress_of_work;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUtility_shifting_file_type() {
		return utility_shifting_file_type;
	}

	public void setUtility_shifting_file_type(String utility_shifting_file_type) {
		this.utility_shifting_file_type = utility_shifting_file_type;
	}

	public String [] getAttachment_file_types() {
		return attachment_file_types;
	}

	public void setAttachment_file_types(String [] attachment_file_types) {
		this.attachment_file_types = attachment_file_types;
	}

	public String [] getAttachmentNames() {
		return attachmentNames;
	}

	public void setAttachmentNames(String [] attachmentNames) {
		this.attachmentNames = attachmentNames;
	}

	public String getModified_by() {
		return modified_by;
	}

	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}

	public String getModified_date() {
		return modified_date;
	}

	public void setModified_date(String modified_date) {
		this.modified_date = modified_date;
	}

	public String getCreated_by_user_id_fk() {
		return created_by_user_id_fk;
	}

	public void setCreated_by_user_id_fk(String created_by_user_id_fk) {
		this.created_by_user_id_fk = created_by_user_id_fk;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getInprogress() {
		return inprogress;
	}

	public void setInprogress(String inprogress) {
		this.inprogress = inprogress;
	}

	public String getPending() {
		return pending;
	}

	public void setPending(String pending) {
		this.pending = pending;
	}
	
	public boolean checkNullOrEmpty() throws IllegalAccessException {
		boolean flag = true;
		try {
			for (Field f : getClass().getDeclaredFields())
		        if (!StringUtils.isEmpty(f.get(this)))
		        	flag = false;
		} catch (Exception e) {
			
		}
	    
	    return flag;            
	}

	public String getMail_body_header() {
		return mail_body_header;
	}

	public void setMail_body_header(String mail_body_header) {
		this.mail_body_header = mail_body_header;
	}

	public String getContract_id_code() {
		return contract_id_code;
	}

	public void setContract_id_code(String contract_id_code) {
		this.contract_id_code = contract_id_code;
	}

	public String getReporting_to_id_srfk() {
		return reporting_to_id_srfk;
	}

	public void setReporting_to_id_srfk(String reporting_to_id_srfk) {
		this.reporting_to_id_srfk = reporting_to_id_srfk;
	}

	public String getLatest_progress_date() {
		return latest_progress_date;
	}

	public void setLatest_progress_date(String latest_progress_date) {
		this.latest_progress_date = latest_progress_date;
	}

	public String getLatest_progress_update() {
		return latest_progress_update;
	}

	public void setLatest_progress_update(String latest_progress_update) {
		this.latest_progress_update = latest_progress_update;
	}

	public String getChainage() {
		return chainage;
	}

	public void setChainage(String chainage) {
		this.chainage = chainage;
	}

	public String [] getAttachmentFileNames() {
		return attachmentFileNames;
	}

	public void setAttachmentFileNames(String [] attachmentFileNames) {
		this.attachmentFileNames = attachmentFileNames;
	}
}

