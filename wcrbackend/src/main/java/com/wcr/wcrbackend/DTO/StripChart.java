package com.wcr.wcrbackend.DTO;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripChart {
	private String strip_chart_id,activity_id_fk,activity_id,strip_chart_component_id_fk,strip_chart_activity_id_fk,planned_start,planned_finish,actual_start,actual_finish,unit_fk,scope,completed,component_details,remarks,
	strip_chart_activity_id,strip_chart_activity_name, strip_chart_component,strip_chart_structure, strip_chart_component_id,strip_chart_component_id_name,contract_id_fk,
	strip_chart_line_id_fk,strip_chart_component_fk,strip_chart_section_id_fk,strip_chart_component_name,order,activity_name,latitude,longitude,activity_description,milestone_name,
	strip_chart_line, strip_chart_section_id,component,strip_chart_section_name,line, strip_chart_type,project_id,work_id_fk,weight,strip_chart_structure_id_fk,id,total_scope,
	progress,progress_date,component_id_color,remaining,issue_description,issue_priority_id,issue_category_id,is_there_issue,attachment_url,created_by_user_id_fk,
	project_id_fk,project_name,work_id,work_name,contract_id,contract_name,unit,to_structure_id,from_structure_id,structure,section,strip_chart_structure_id,structure_type,status_name,work_short_name,contract_short_name,data_date,milestone_fk,reported_by
	,user_role_code,p6_task_code,weightage,component_id,status,baseline_start,baseline_finish,finish,start,searchStr,
	user_type_fk,user_id,user_name,designation,structure_type_fk,pending,modified_date,target_date_of_completion,structure_type_completed,structure_completed,component_completed,validation_pending;

	private MultipartFile stripChartFile,uploadFile;
	
	private String [] actualScopes,projects,strip_chart_ids,ids,completedScopes,totalScopes,actual_starts,actual_finishs,activity_ids,p6_task_codes;
	
	private String reported_by_email_id,zonal_railway_fk,other_organization,department_fk,department_name,Division,WorkScope,Balance;
	private String works,during_month,cumulative,wip;
	
	private String cScope,	cCompleted,	cBalance,	cWIP,	wScope,	wCompleted,	wBalance,	wWIP, from_date, to_date;
	
	private List<MultipartFile> issueFiles;
	private List<StripChart> report1List;
	private MultipartFile[] structureFileNames;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
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

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public String getTo_structure_id() {
		return to_structure_id;
	}

	public void setTo_structure_id(String to_structure_id) {
		this.to_structure_id = to_structure_id;
	}

	public String getFrom_structure_id() {
		return from_structure_id;
	}

	public void setFrom_structure_id(String from_structure_id) {
		this.from_structure_id = from_structure_id;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public List<StripChart> getReport1List() {
		return report1List;
	}

	public void setReport1List(List<StripChart> report1List) {
		this.report1List = report1List;
	}

	public String getWeightage() {
		return weightage;
	}

	public void setWeightage(String weightage) {
		this.weightage = weightage;
	}

	public String getP6_task_code() {
		return p6_task_code;
	}

	public void setP6_task_code(String p6_task_code) {
		this.p6_task_code = p6_task_code;
	}

	public String getActivity_id_fk() {
		return activity_id_fk;
	}

	public void setActivity_id_fk(String activity_id_fk) {
		this.activity_id_fk = activity_id_fk;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public String[] getActivity_ids() {
		return activity_ids;
	}

	public void setActivity_ids(String[] activity_ids) {
		this.activity_ids = activity_ids;
	}

	public String getMilestone_fk() {
		return milestone_fk;
	}

	public void setMilestone_fk(String milestone_fk) {
		this.milestone_fk = milestone_fk;
	}
	public String[] getActual_starts() {
		return actual_starts;
	}

	public void setActual_starts(String[] actual_starts) {
		this.actual_starts = actual_starts;
	}

	public String[] getActual_finishs() {
		return actual_finishs;
	}

	public void setActual_finishs(String[] actual_finishs) {
		this.actual_finishs = actual_finishs;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTotal_scope() {
		return total_scope;
	}

	public void setTotal_scope(String total_scope) {
		this.total_scope = total_scope;
	}

	public String getMilestone_name() {
		return milestone_name;
	}

	public void setMilestone_name(String milestone_name) {
		this.milestone_name = milestone_name;
	}

	public String getActivity_description() {
		return activity_description;
	}

	public void setActivity_description(String activity_description) {
		this.activity_description = activity_description;
	}

	public String[] getTotalScopes() {
		return totalScopes;
	}

	public void setTotalScopes(String[] totalScopes) {
		this.totalScopes = totalScopes;
	}

	public String[] getCompletedScopes() {
		return completedScopes;
	}

	public void setCompletedScopes(String[] completedScopes) {
		this.completedScopes = completedScopes;
	}

	public String[] getStrip_chart_ids() {
		return strip_chart_ids;
	}

	public void setStrip_chart_ids(String[] strip_chart_ids) {
		this.strip_chart_ids = strip_chart_ids;
	}

	public String[] getActualScopes() {
		return actualScopes;
	}

	public void setActualScopes(String[] actualScopes) {
		this.actualScopes = actualScopes;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getWork_id() {
		return work_id;
	}

	public void setWork_id(String work_id) {
		this.work_id = work_id;
	}

	public String getWork_name() {
		return work_name;
	}

	public void setWork_name(String work_name) {
		this.work_name = work_name;
	}

	public String getIssue_description() {
		return issue_description;
	}

	public void setIssue_description(String issue_description) {
		this.issue_description = issue_description;
	}

	public String getIssue_priority_id() {
		return issue_priority_id;
	}

	public void setIssue_priority_id(String issue_priority_id) {
		this.issue_priority_id = issue_priority_id;
	}

	public String getIssue_category_id() {
		return issue_category_id;
	}

	public void setIssue_category_id(String issue_category_id) {
		this.issue_category_id = issue_category_id;
	}

	public String getIs_there_issue() {
		return is_there_issue;
	}

	public void setIs_there_issue(String is_there_issue) {
		this.is_there_issue = is_there_issue;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getProgress_date() {
		return progress_date;
	}

	public void setProgress_date(String progress_date) {
		this.progress_date = progress_date;
	}

	public String getStrip_chart_id() {
		return strip_chart_id;
	}

	public void setStrip_chart_id(String strip_chart_id) {
		this.strip_chart_id = strip_chart_id;
	}

	public String getStrip_chart_component_id_fk() {
		return strip_chart_component_id_fk;
	}

	public void setStrip_chart_component_id_fk(String strip_chart_component_id_fk) {
		this.strip_chart_component_id_fk = strip_chart_component_id_fk;
	}

	public String getStrip_chart_activity_id_fk() {
		return strip_chart_activity_id_fk;
	}

	public void setStrip_chart_activity_id_fk(String strip_chart_activity_id_fk) {
		this.strip_chart_activity_id_fk = strip_chart_activity_id_fk;
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

	public String getUnit_fk() {
		return unit_fk;
	}

	public void setUnit_fk(String unit_fk) {
		this.unit_fk = unit_fk;
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

	public String getStrip_chart_activity_id() {
		return strip_chart_activity_id;
	}

	public void setStrip_chart_activity_id(String strip_chart_activity_id) {
		this.strip_chart_activity_id = strip_chart_activity_id;
	}

	public String getStrip_chart_activity_name() {
		return strip_chart_activity_name;
	}

	public void setStrip_chart_activity_name(String strip_chart_activity_name) {
		this.strip_chart_activity_name = strip_chart_activity_name;
	}

	public String getStrip_chart_component() {
		return strip_chart_component;
	}

	public void setStrip_chart_component(String strip_chart_component) {
		this.strip_chart_component = strip_chart_component;
	}

	public String getStrip_chart_structure() {
		return strip_chart_structure;
	}

	public void setStrip_chart_structure(String strip_chart_structure) {
		this.strip_chart_structure = strip_chart_structure;
	}

	public String getStrip_chart_component_id() {
		return strip_chart_component_id;
	}

	public void setStrip_chart_component_id(String strip_chart_component_id) {
		this.strip_chart_component_id = strip_chart_component_id;
	}

	public String getContract_id_fk() {
		return contract_id_fk;
	}

	public void setContract_id_fk(String contract_id_fk) {
		this.contract_id_fk = contract_id_fk;
	}

	public String getStrip_chart_line_id_fk() {
		return strip_chart_line_id_fk;
	}

	public void setStrip_chart_line_id_fk(String strip_chart_line_id_fk) {
		this.strip_chart_line_id_fk = strip_chart_line_id_fk;
	}

	public String getStrip_chart_component_fk() {
		return strip_chart_component_fk;
	}

	public void setStrip_chart_component_fk(String strip_chart_component_fk) {
		this.strip_chart_component_fk = strip_chart_component_fk;
	}

	public String getStrip_chart_section_id_fk() {
		return strip_chart_section_id_fk;
	}

	public void setStrip_chart_section_id_fk(String strip_chart_section_id_fk) {
		this.strip_chart_section_id_fk = strip_chart_section_id_fk;
	}

	public String getStrip_chart_component_name() {
		return strip_chart_component_name;
	}

	public void setStrip_chart_component_name(String strip_chart_component_name) {
		this.strip_chart_component_name = strip_chart_component_name;
	}

	
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getStrip_chart_line() {
		return strip_chart_line;
	}

	public void setStrip_chart_line(String strip_chart_line) {
		this.strip_chart_line = strip_chart_line;
	}

	public String getStrip_chart_section_id() {
		return strip_chart_section_id;
	}

	public void setStrip_chart_section_id(String strip_chart_section_id) {
		this.strip_chart_section_id = strip_chart_section_id;
	}

	public String getStrip_chart_section_name() {
		return strip_chart_section_name;
	}

	public void setStrip_chart_section_name(String strip_chart_section_name) {
		this.strip_chart_section_name = strip_chart_section_name;
	}

	public String getStrip_chart_type() {
		return strip_chart_type;
	}
	
	public void setStrip_chart_type(String strip_chart_type) {
		this.strip_chart_type = strip_chart_type;
	}

	public String getWork_id_fk() {
		return work_id_fk;
	}

	public void setWork_id_fk(String work_id_fk) {
		this.work_id_fk = work_id_fk;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getStrip_chart_structure_id_fk() {
		return strip_chart_structure_id_fk;
	}

	public void setStrip_chart_structure_id_fk(String strip_chart_structure_id_fk) {
		this.strip_chart_structure_id_fk = strip_chart_structure_id_fk;
	}

	public MultipartFile getStripChartFile() {
		return stripChartFile;
	}

	public void setStripChartFile(MultipartFile stripChartFile) {
		this.stripChartFile = stripChartFile;
	}

	public String getStrip_chart_component_id_name() {
		return strip_chart_component_id_name;
	}

	public void setStrip_chart_component_id_name(String strip_chart_component_id_name) {
		this.strip_chart_component_id_name = strip_chart_component_id_name;
	}

	public String getPlanned_start() {
		return planned_start;
	}

	public void setPlanned_start(String planned_start) {
		this.planned_start = planned_start;
	}

	public String getComponent_id_color() {
		return component_id_color;
	}

	public void setComponent_id_color(String component_id_color) {
		this.component_id_color = component_id_color;
	}

	public String getRemaining() {
		return remaining;
	}

	public void setRemaining(String remaining) {
		this.remaining = remaining;
	}

	public String getAttachment_url() {
		return attachment_url;
	}

	public void setAttachment_url(String attachment_url) {
		this.attachment_url = attachment_url;
	}

	public String getCreated_by_user_id_fk() {
		return created_by_user_id_fk;
	}

	public void setCreated_by_user_id_fk(String created_by_user_id_fk) {
		this.created_by_user_id_fk = created_by_user_id_fk;
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

	public String getStrip_chart_structure_id() {
		return strip_chart_structure_id;
	}

	public void setStrip_chart_structure_id(String strip_chart_structure_id) {
		this.strip_chart_structure_id = strip_chart_structure_id;
	}

	public String getStructure_type() {
		return structure_type;
	}

	public void setStructure_type(String structure_type) {
		this.structure_type = structure_type;
	}

	public String getStatus_name() {
		return status_name;
	}

	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}

	public MultipartFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getWork_short_name() {
		return work_short_name;
	}

	public void setWork_short_name(String work_short_name) {
		this.work_short_name = work_short_name;
	}

	public String getContract_short_name() {
		return contract_short_name;
	}

	public void setContract_short_name(String contract_short_name) {
		this.contract_short_name = contract_short_name;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getData_date() {
		return data_date;
	}

	public void setData_date(String data_date) {
		this.data_date = data_date;
	}

	public String getReported_by() {
		return reported_by;
	}

	public void setReported_by(String reported_by) {
		this.reported_by = reported_by;
	}

	public String getReported_by_email_id() {
		return reported_by_email_id;
	}

	public void setReported_by_email_id(String reported_by_email_id) {
		this.reported_by_email_id = reported_by_email_id;
	}

	public List<MultipartFile> getIssueFiles() {
		return issueFiles;
	}

	public void setIssueFiles(List<MultipartFile> issueFiles) {
		this.issueFiles = issueFiles;
	}

	public String getZonal_railway_fk() {
		return zonal_railway_fk;
	}

	public void setZonal_railway_fk(String zonal_railway_fk) {
		this.zonal_railway_fk = zonal_railway_fk;
	}

	public String getOther_organization() {
		return other_organization;
	}

	public void setOther_organization(String other_organization) {
		this.other_organization = other_organization;
	}

	public String getDepartment_fk() {
		return department_fk;
	}

	public void setDepartment_fk(String department_fk) {
		this.department_fk = department_fk;
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

	public String getStructure_type_fk() {
		return structure_type_fk;
	}

	public void setStructure_type_fk(String structure_type_fk) {
		this.structure_type_fk = structure_type_fk;
	}

	public String getPending() {
		return pending;
	}

	public void setPending(String pending) {
		this.pending = pending;
	}

	public String getModified_date() {
		return modified_date;
	}

	public void setModified_date(String modified_date) {
		this.modified_date = modified_date;
	}

	public MultipartFile[] getStructureFileNames() {
		return structureFileNames;
	}

	public void setStructureFileNames(MultipartFile[] structureFileNames) {
		this.structureFileNames = structureFileNames;
	}

	public String getTarget_date_of_completion() {
		return target_date_of_completion;
	}

	public void setTarget_date_of_completion(String target_date_of_completion) {
		this.target_date_of_completion = target_date_of_completion;
	}

	public String getStructure_type_completed() {
		return structure_type_completed;
	}

	public void setStructure_type_completed(String structure_type_completed) {
		this.structure_type_completed = structure_type_completed;
	}

	public String getStructure_completed() {
		return structure_completed;
	}

	public void setStructure_completed(String structure_completed) {
		this.structure_completed = structure_completed;
	}

	public String getComponent_completed() {
		return component_completed;
	}

	public void setComponent_completed(String component_completed) {
		this.component_completed = component_completed;
	}

	public String getValidation_pending() {
		return validation_pending;
	}

	public void setValidation_pending(String validation_pending) {
		this.validation_pending = validation_pending;
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

	public String [] getP6_task_codes() {
		return p6_task_codes;
	}

	public void setP6_task_codes(String [] p6_task_codes) {
		this.p6_task_codes = p6_task_codes;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	public String getDivision() {
		return Division;
	}

	public void setDivision(String division) {
		Division = division;
	}

	public String getWorkScope() {
		return WorkScope;
	}

	public void setWorkScope(String workScope) {
		WorkScope = workScope;
	}

	public String getBalance() {
		return Balance;
	}

	public void setBalance(String balance) {
		Balance = balance;
	}

	public String getWorks() {
		return works;
	}

	public void setWorks(String works) {
		this.works = works;
	}

	public String getDuring_month() {
		return during_month;
	}

	public void setDuring_month(String during_month) {
		this.during_month = during_month;
	}

	public String getCumulative() {
		return cumulative;
	}

	public void setCumulative(String cumulative) {
		this.cumulative = cumulative;
	}

	public String getWip() {
		return wip;
	}

	public void setWip(String wip) {
		this.wip = wip;
	}

	public String getcScope() {
		return cScope;
	}

	public void setcScope(String cScope) {
		this.cScope = cScope;
	}

	public String getcCompleted() {
		return cCompleted;
	}

	public void setcCompleted(String cCompleted) {
		this.cCompleted = cCompleted;
	}

	public String getcBalance() {
		return cBalance;
	}

	public void setcBalance(String cBalance) {
		this.cBalance = cBalance;
	}

	public String getcWIP() {
		return cWIP;
	}

	public void setcWIP(String cWIP) {
		this.cWIP = cWIP;
	}

	public String getwScope() {
		return wScope;
	}

	public void setwScope(String wScope) {
		this.wScope = wScope;
	}

	public String getwCompleted() {
		return wCompleted;
	}

	public void setwCompleted(String wCompleted) {
		this.wCompleted = wCompleted;
	}

	public String getwBalance() {
		return wBalance;
	}

	public void setwBalance(String wBalance) {
		this.wBalance = wBalance;
	}

	public String getwWIP() {
		return wWIP;
	}

	public void setwWIP(String wWIP) {
		this.wWIP = wWIP;
	}

	public String [] getProjects() {
		return projects;
	}

	public void setProjects(String [] projects) {
		this.projects = projects;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

}
