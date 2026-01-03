package com.wcr.wcrbackend.reference.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public class TrainingType {
	
	private String training_type,binary,dashboard_type,id, la_sub_category,bg_type_old,bg_type_fk, la_category_fk,la_category,contractor_specialization,user_role_name,bg_type,status,contract_type,department,deliverable_type, department_name, contract_id_code,document_type, user_role_code,user_access_type,soft_delete_status,source_of_funds, user_access_table,requirement_stage,unit_type,execution_agency,old_training_type,new_training_type,insurance_type,training_status,training_category,drawing_type,utility_type;

	private String Table_name,project_type_name,project_type,project_type_id,column_name,constraint_name,user_name,work_id_fk_old,risk_area_fk_old,category_new,la_land_status,approval_authority,resource_type,area_short_name,area_short_name_new,referenced_table_name,risk_work_completed,referenced_column_name,count,tName,bg_type_new,binary_new,binary_old;
	
	private String structure_file_type,design_file_type,department_new,issue_category_fk_old,stage,department_old,type_fk_old,department_name_new,risk_work_completed_new,department_name_old,department_code_old,department_code_new,contractor_specialization_old
	,contractor_specialization_new,contract_type_old,contract_type_new,dashboard_type_old,dashboard_type_new,status_old,status_new,deliverable_type_old,
	deliverable_type_new,document_type_old,document_type_new,value_old,value_new,general_status,execution_status,category,priority,status_of_new,status_of,module_name,module_incharge,incharge_user_id_fk
	,p6_wbs_category,project_priority,railway_id,section_id,section_name,division_id,division_name,railway_zone, railway_name,railway_name_new,railway_id_val,report_type,revision_status,risk_priority,approval_status,land_type
	,impact,root_cause,structure_type, user_role_code_new,user_access_table_new,user_access_type_val,short_description,short_description_new,area, item_no,
	item_no_new,risk_area_fk,risk_area_fk_new,sub_area,sub_area_new,la_sub_category_new,la_category_fk_new,la_category_fk_old,la_sub_category_old,sub_category,
	zonal_railway_funds,risk_minimum_new,risk_maximum_new,risk_classification_id, resource_type_fk_new,classification, minimum, maximum,alert_level,alert_type,as_built_status,user_type,
	login_event_type,yesorno,type,notification_type, notification_type_icon,notification_type_icon_new,type_fk_new,notification_type_new, type_fk,web_documents_category,financial_year,
	risk_work_hod_id, work_id_fk, hod_user_id_fk,designation,work_short_name,work_id_fk_new,hod_user_id_fk_new,sub_work,sub_work_new,
	 name, order, icon, parent_id, link_url,order_text_update,parent_text,url_text_update,status_fk,order_text,url_text,name_text,statuss,parent_texts,
	contract_category_fk,risk_revision_id,risk_id_pk_fk, date, issue_category_fk,contract_file_type,issue_file_type,fob_file_type,contract_category_fk_new,issue_other_organization,issue_category_fk_new,project_file_type,work_file_type
	, template_name, attachment,captiliszedTableName, resource_type_fk,submission_purpose,design_status_submit,la_file_type, sub_resource_type, uploaded_on, uploaded_by,contract_status,contract_status_new,commonAttachment,short_name,
	soft_delete_status_fk,rr_verification_by, rr_location,rr_boundarywall_Status, bses_file_type, rr_occupancy_status, rr_tenure_status, caste, mother_tongue, rr_type_of_family,marital_status
	,phase,rr_type_of_use, rr_location_fk, rr_sub_location,work_type,rr_location_fk_old,rr_location_fk_new,executive_user_id_fk,user_id,station_name;
    
	private String sub_resource_type_new,resource_type_fk_old,rr_sub_location_new,dashboard_id,dashboard_name,dashboard_icon,dashboard_url,source_table_name,source_field_name,source_field_value,show_left_menu,user_types,user_roles,users,Created_by_user_id_fk,archive_dates,archive_urls;
	
	private List<String> access_user_types,access_user_roles,access_users;

	
	private String [] work_id_fks,executive_user_id_fks,archive_date,archive_url;
	List<TrainingType> rrExecutivesList;
	List<TrainingType> landExecutivesList;
	List<TrainingType> utilityExecutivesList;
	
	public String getIssue_category_fk_old() {
		return issue_category_fk_old;
	}

	public void setIssue_category_fk_old(String issue_category_fk_old) {
		this.issue_category_fk_old = issue_category_fk_old;
	}

	public String getType_fk_old() {
		return type_fk_old;
	}

	public void setType_fk_old(String type_fk_old) {
		this.type_fk_old = type_fk_old;
	}

	public String getCategory_new() {
		return category_new;
	}

	public void setCategory_new(String category_new) {
		this.category_new = category_new;
	}

	public String getRisk_area_fk_old() {
		return risk_area_fk_old;
	}

	public void setRisk_area_fk_old(String risk_area_fk_old) {
		this.risk_area_fk_old = risk_area_fk_old;
	}

	public String getResource_type_fk_old() {
		return resource_type_fk_old;
	}

	public void setResource_type_fk_old(String resource_type_fk_old) {
		this.resource_type_fk_old = resource_type_fk_old;
	}

	public String getSub_resource_type_new() {
		return sub_resource_type_new;
	}

	public void setSub_resource_type_new(String sub_resource_type_new) {
		this.sub_resource_type_new = sub_resource_type_new;
	}

	public String getRr_sub_location_new() {
		return rr_sub_location_new;
	}

	public void setRr_sub_location_new(String rr_sub_location_new) {
		this.rr_sub_location_new = rr_sub_location_new;
	}

	public String getDashboard_id() {
		return dashboard_id;
	}

	public void setDashboard_id(String dashboard_id) {
		this.dashboard_id = dashboard_id;
	}

	public String getDashboard_name() {
		return dashboard_name;
	}

	public void setDashboard_name(String dashboard_name) {
		this.dashboard_name = dashboard_name;
	}

	public String getDashboard_icon() {
		return dashboard_icon;
	}

	public void setDashboard_icon(String dashboard_icon) {
		this.dashboard_icon = dashboard_icon;
	}

	public String getDashboard_url() {
		return dashboard_url;
	}

	public void setDashboard_url(String dashboard_url) {
		this.dashboard_url = dashboard_url;
	}

	public String getSource_table_name() {
		return source_table_name;
	}

	public void setSource_table_name(String source_table_name) {
		this.source_table_name = source_table_name;
	}

	public String getSource_field_name() {
		return source_field_name;
	}

	public void setSource_field_name(String source_field_name) {
		this.source_field_name = source_field_name;
	}

	public String getSource_field_value() {
		return source_field_value;
	}

	public void setSource_field_value(String source_field_value) {
		this.source_field_value = source_field_value;
	}

	public String getShow_left_menu() {
		return show_left_menu;
	}

	public void setShow_left_menu(String show_left_menu) {
		this.show_left_menu = show_left_menu;
	}

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	public String getWork_id_fk_old() {
		return work_id_fk_old;
	}

	public void setWork_id_fk_old(String work_id_fk_old) {
		this.work_id_fk_old = work_id_fk_old;
	}

	public String[] getWork_id_fks() {
		return work_id_fks;
	}

	public void setWork_id_fks(String[] work_id_fks) {
		this.work_id_fks = work_id_fks;
	}

	public String[] getExecutive_user_id_fks() {
		return executive_user_id_fks;
	}

	public void setExecutive_user_id_fks(String[] executive_user_id_fks) {
		this.executive_user_id_fks = executive_user_id_fks;
	}

	public List<TrainingType> getRrExecutivesList() {
		return rrExecutivesList;
	}

	public void setRrExecutivesList(List<TrainingType> rrExecutivesList) {
		this.rrExecutivesList = rrExecutivesList;
	}

	public List<TrainingType> getLandExecutivesList() {
		return landExecutivesList;
	}

	public void setLandExecutivesList(List<TrainingType> landExecutivesList) {
		this.landExecutivesList = landExecutivesList;
	}

	public List<TrainingType> getUtilityExecutivesList() {
		return utilityExecutivesList;
	}

	public void setUtilityExecutivesList(List<TrainingType> utilityExecutivesList) {
		this.utilityExecutivesList = utilityExecutivesList;
	}

	public String getExecutive_user_id_fk() {
		return executive_user_id_fk;
	}

	public void setExecutive_user_id_fk(String executive_user_id_fk) {
		this.executive_user_id_fk = executive_user_id_fk;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRr_location_fk_old() {
		return rr_location_fk_old;
	}

	public void setRr_location_fk_old(String rr_location_fk_old) {
		this.rr_location_fk_old = rr_location_fk_old;
	}

	public String getRr_location_fk_new() {
		return rr_location_fk_new;
	}

	public void setRr_location_fk_new(String rr_location_fk_new) {
		this.rr_location_fk_new = rr_location_fk_new;
	}

	public String getRr_type_of_family() {
		return rr_type_of_family;
	}

	public void setRr_type_of_family(String rr_type_of_family) {
		this.rr_type_of_family = rr_type_of_family;
	}

	public String getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getRr_type_of_use() {
		return rr_type_of_use;
	}

	public void setRr_type_of_use(String rr_type_of_use) {
		this.rr_type_of_use = rr_type_of_use;
	}

	public String getRr_location_fk() {
		return rr_location_fk;
	}

	public void setRr_location_fk(String rr_location_fk) {
		this.rr_location_fk = rr_location_fk;
	}

	public String getRr_sub_location() {
		return rr_sub_location;
	}

	public void setRr_sub_location(String rr_sub_location) {
		this.rr_sub_location = rr_sub_location;
	}

	public String getRr_occupancy_status() {
		return rr_occupancy_status;
	}

	public void setRr_occupancy_status(String rr_occupancy_status) {
		this.rr_occupancy_status = rr_occupancy_status;
	}

	public String getRr_tenure_status() {
		return rr_tenure_status;
	}

	public void setRr_tenure_status(String rr_tenure_status) {
		this.rr_tenure_status = rr_tenure_status;
	}

	public String getCaste() {
		return caste;
	}

	public void setCaste(String caste) {
		this.caste = caste;
	}

	public String getMother_tongue() {
		return mother_tongue;
	}

	public void setMother_tongue(String mother_tongue) {
		this.mother_tongue = mother_tongue;
	}

	public String getBses_file_type() {
		return bses_file_type;
	}

	public void setBses_file_type(String bses_file_type) {
		this.bses_file_type = bses_file_type;
	}

	public String getRr_boundarywall_Status() {
		return rr_boundarywall_Status;
	}

	public void setRr_boundarywall_Status(String rr_boundarywall_Status) {
		this.rr_boundarywall_Status = rr_boundarywall_Status;
	}

	public String getRr_location() {
		return rr_location;
	}

	public void setRr_location(String rr_location) {
		this.rr_location = rr_location;
	}

	List<TrainingType> tableHistoryList;
	List<TrainingType> subResourceDeatails;
	
	
	public String getRr_verification_by() {
		return rr_verification_by;
	}

	public void setRr_verification_by(String rr_verification_by) {
		this.rr_verification_by = rr_verification_by;
	}

	public String getLa_land_status() {
		return la_land_status;
	}

	public void setLa_land_status(String la_land_status) {
		this.la_land_status = la_land_status;
	}

	public String getParent_texts() {
		return parent_texts;
	}

	public void setParent_texts(String parent_texts) {
		this.parent_texts = parent_texts;
	}

	public String getStatuss() {
		return statuss;
	}

	public void setStatuss(String statuss) {
		this.statuss = statuss;
	}

	public String getOrder_text_update() {
		return order_text_update;
	}

	public void setOrder_text_update(String order_text_update) {
		this.order_text_update = order_text_update;
	}

	public String getParent_text() {
		return parent_text;
	}

	public void setParent_text(String parent_text) {
		this.parent_text = parent_text;
	}

	public String getUrl_text_update() {
		return url_text_update;
	}

	public void setUrl_text_update(String url_text_update) {
		this.url_text_update = url_text_update;
	}

	public String getStatus_fk() {
		return status_fk;
	}

	public void setStatus_fk(String status_fk) {
		this.status_fk = status_fk;
	}

	public String getOrder_text() {
		return order_text;
	}

	public void setOrder_text(String order_text) {
		this.order_text = order_text;
	}

	public String getUrl_text() {
		return url_text;
	}

	public void setUrl_text(String url_text) {
		this.url_text = url_text;
	}

	public String getName_text() {
		return name_text;
	}

	public void setName_text(String name_text) {
		this.name_text = name_text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getLink_url() {
		return link_url;
	}

	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}

	public String getLa_file_type() {
		return la_file_type;
	}

	public void setLa_file_type(String la_file_type) {
		this.la_file_type = la_file_type;
	}

	public String getSubmission_purpose() {
		return submission_purpose;
	}

	public void setSubmission_purpose(String submission_purpose) {
		this.submission_purpose = submission_purpose;
	}

	public String getDesign_file_type() {
		return design_file_type;
	}

	public void setDesign_file_type(String design_file_type) {
		this.design_file_type = design_file_type;
	}

	public String getDesign_status_submit() {
		return design_status_submit;
	}

	public void setDesign_status_submit(String design_status_submit) {
		this.design_status_submit = design_status_submit;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getApproval_authority() {
		return approval_authority;
	}

	public void setApproval_authority(String approval_authority) {
		this.approval_authority = approval_authority;
	}

	public String getStructure_file_type() {
		return structure_file_type;
	}

	public void setStructure_file_type(String structure_file_type) {
		this.structure_file_type = structure_file_type;
	}

	private MultipartFile templateFile;
	private MultipartFile templateFileOld;
	

	public String getCaptiliszedTableName() {
		return captiliszedTableName;
	}

	public void setCaptiliszedTableName(String captiliszedTableName) {
		this.captiliszedTableName = captiliszedTableName;
	}

	public String getResource_type_fk_new() {
		return resource_type_fk_new;
	}

	public void setResource_type_fk_new(String resource_type_fk_new) {
		this.resource_type_fk_new = resource_type_fk_new;
	}

	public String getResource_type_fk() {
		return resource_type_fk;
	}

	public void setResource_type_fk(String resource_type_fk) {
		this.resource_type_fk = resource_type_fk;
	}

	public String getSub_resource_type() {
		return sub_resource_type;
	}

	public void setSub_resource_type(String sub_resource_type) {
		this.sub_resource_type = sub_resource_type;
	}

	public List<TrainingType> getSubResourceDeatails() {
		return subResourceDeatails;
	}

	public void setSubResourceDeatails(List<TrainingType> subResourceDeatails) {
		this.subResourceDeatails = subResourceDeatails;
	}

	public String getCommonAttachment() {
		return commonAttachment;
	}

	public void setCommonAttachment(String commonAttachment) {
		this.commonAttachment = commonAttachment;
	}

	public String getContract_status() {
		return contract_status;
	}

	public void setContract_status(String contract_status) {
		this.contract_status = contract_status;
	}

	public String getContract_status_new() {
		return contract_status_new;
	}

	public void setContract_status_new(String contract_status_new) {
		this.contract_status_new = contract_status_new;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	public String getArea_short_name() {
		return area_short_name;
	}

	public void setArea_short_name(String area_short_name) {
		this.area_short_name = area_short_name;
	}

	public String getArea_short_name_new() {
		return area_short_name_new;
	}

	public void setArea_short_name_new(String area_short_name_new) {
		this.area_short_name_new = area_short_name_new;
	}

	public String getRisk_work_completed_new() {
		return risk_work_completed_new;
	}

	public void setRisk_work_completed_new(String risk_work_completed_new) {
		this.risk_work_completed_new = risk_work_completed_new;
	}

	public String getRisk_work_completed() {
		return risk_work_completed;
	}

	public void setRisk_work_completed(String risk_work_completed) {
		this.risk_work_completed = risk_work_completed;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public List<TrainingType> getTableHistoryList() {
		return tableHistoryList;
	}

	public void setTableHistoryList(List<TrainingType> tableHistoryList) {
		this.tableHistoryList = tableHistoryList;
	}

	public MultipartFile getTemplateFileOld() {
		return templateFileOld;
	}

	public void setTemplateFileOld(MultipartFile templateFileOld) {
		this.templateFileOld = templateFileOld;
	}

	public MultipartFile getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(MultipartFile templateFile) {
		this.templateFile = templateFile;
	}

	public String getTemplate_name() {
		return template_name;
	}

	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getUploaded_on() {
		return uploaded_on;
	}

	public void setUploaded_on(String uploaded_on) {
		this.uploaded_on = uploaded_on;
	}

	public String getUploaded_by() {
		return uploaded_by;
	}

	public void setUploaded_by(String uploaded_by) {
		this.uploaded_by = uploaded_by;
	}

	private int counts;

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	public String getRisk_revision_id() {
		return risk_revision_id;
	}

	public void setRisk_revision_id(String risk_revision_id) {
		this.risk_revision_id = risk_revision_id;
	}

	public String getRisk_id_pk_fk() {
		return risk_id_pk_fk;
	}

	public void setRisk_id_pk_fk(String risk_id_pk_fk) {
		this.risk_id_pk_fk = risk_id_pk_fk;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFob_file_type() {
		return fob_file_type;
	}

	public void setFob_file_type(String fob_file_type) {
		this.fob_file_type = fob_file_type;
	}

	public String getContract_file_type() {
		return contract_file_type;
	}

	public void setContract_file_type(String contract_file_type) {
		this.contract_file_type = contract_file_type;
	}

	public String getIssue_file_type() {
		return issue_file_type;
	}

	public void setIssue_file_type(String issue_file_type) {
		this.issue_file_type = issue_file_type;
	}

	public String getIssue_other_organization() {
		return issue_other_organization;
	}

	public void setIssue_other_organization(String issue_other_organization) {
		this.issue_other_organization = issue_other_organization;
	}

	public String getProject_file_type() {
		return project_file_type;
	}

	public void setProject_file_type(String project_file_type) {
		this.project_file_type = project_file_type;
	}

	public String getWork_file_type() {
		return work_file_type;
	}

	public void setWork_file_type(String work_file_type) {
		this.work_file_type = work_file_type;
	}

	public String getContract_category_fk_new() {
		return contract_category_fk_new;
	}

	public void setContract_category_fk_new(String contract_category_fk_new) {
		this.contract_category_fk_new = contract_category_fk_new;
	}

	public String getIssue_category_fk_new() {
		return issue_category_fk_new;
	}

	public void setIssue_category_fk_new(String issue_category_fk_new) {
		this.issue_category_fk_new = issue_category_fk_new;
	}

	public String getContract_category_fk() {
		return contract_category_fk;
	}

	public void setContract_category_fk(String contract_category_fk) {
		this.contract_category_fk = contract_category_fk;
	}

	public String getIssue_category_fk() {
		return issue_category_fk;
	}

	public void setIssue_category_fk(String issue_category_fk) {
		this.issue_category_fk = issue_category_fk;
	}

	public String getSub_work_new() {
		return sub_work_new;
	}

	public void setSub_work_new(String sub_work_new) {
		this.sub_work_new = sub_work_new;
	}

	private List<TrainingType> bankGaurenteeList;
	private List<TrainingType> bankGaurenteeList1;
	private List<TrainingType> tablesList;
	private List<TrainingType> countList;
	private List<TrainingType> dList;
	private List<TrainingType> dList1;

	public String getSub_work() {
		return sub_work;
	}

	public void setSub_work(String sub_work) {
		this.sub_work = sub_work;
	}

	public String getWork_id_fk_new() {
		return work_id_fk_new;
	}

	public void setWork_id_fk_new(String work_id_fk_new) {
		this.work_id_fk_new = work_id_fk_new;
	}

	public String getHod_user_id_fk_new() {
		return hod_user_id_fk_new;
	}

	public void setHod_user_id_fk_new(String hod_user_id_fk_new) {
		this.hod_user_id_fk_new = hod_user_id_fk_new;
	}

	public String getWork_short_name() {
		return work_short_name;
	}

	public void setWork_short_name(String work_short_name) {
		this.work_short_name = work_short_name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getRisk_work_hod_id() {
		return risk_work_hod_id;
	}

	public void setRisk_work_hod_id(String risk_work_hod_id) {
		this.risk_work_hod_id = risk_work_hod_id;
	}

	public String getWork_id_fk() {
		return work_id_fk;
	}

	public void setWork_id_fk(String work_id_fk) {
		this.work_id_fk = work_id_fk;
	}

	public String getHod_user_id_fk() {
		return hod_user_id_fk;
	}

	public void setHod_user_id_fk(String hod_user_id_fk) {
		this.hod_user_id_fk = hod_user_id_fk;
	}

	public String getType_fk_new() {
		return type_fk_new;
	}

	public void setType_fk_new(String type_fk_new) {
		this.type_fk_new = type_fk_new;
	}

	public String getFinancial_year() {
		return financial_year;
	}

	public void setFinancial_year(String financial_year) {
		this.financial_year = financial_year;
	}

	public String getWeb_documents_category() {
		return web_documents_category;
	}

	public void setWeb_documents_category(String web_documents_category) {
		this.web_documents_category = web_documents_category;
	}

	public String getType_fk() {
		return type_fk;
	}

	public void setType_fk(String type_fk) {
		this.type_fk = type_fk;
	}

	public String getNotification_type_new() {
		return notification_type_new;
	}

	public void setNotification_type_new(String notification_type_new) {
		this.notification_type_new = notification_type_new;
	}

	public String getNotification_type() {
		return notification_type;
	}

	public void setNotification_type(String notification_type) {
		this.notification_type = notification_type;
	}

	public String getNotification_type_icon() {
		return notification_type_icon;
	}

	public void setNotification_type_icon(String notification_type_icon) {
		this.notification_type_icon = notification_type_icon;
	}

	public String getNotification_type_icon_new() {
		return notification_type_icon_new;
	}

	public void setNotification_type_icon_new(String notification_type_icon_new) {
		this.notification_type_icon_new = notification_type_icon_new;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLogin_event_type() {
		return login_event_type;
	}

	public String getYesorno() {
		return yesorno;
	}

	public void setYesorno(String yesorno) {
		this.yesorno = yesorno;
	}

	public void setLogin_event_type(String login_event_type) {
		this.login_event_type = login_event_type;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getAs_built_status() {
		return as_built_status;
	}

	public void setAs_built_status(String as_built_status) {
		this.as_built_status = as_built_status;
	}

	public String getAlert_type() {
		return alert_type;
	}

	public void setAlert_type(String alert_type) {
		this.alert_type = alert_type;
	}

	public String getAlert_level() {
		return alert_level;
	}

	public void setAlert_level(String alert_level) {
		this.alert_level = alert_level;
	}

	public String getRisk_classification_id() {
		return risk_classification_id;
	}

	public void setRisk_classification_id(String risk_classification_id) {
		this.risk_classification_id = risk_classification_id;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getMinimum() {
		return minimum;
	}

	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}

	public String getMaximum() {
		return maximum;
	}

	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}

	public String getRisk_minimum_new() {
		return risk_minimum_new;
	}

	public void setRisk_minimum_new(String risk_minimum_new) {
		this.risk_minimum_new = risk_minimum_new;
	}

	public String getRisk_maximum_new() {
		return risk_maximum_new;
	}

	public void setRisk_maximum_new(String risk_maximum_new) {
		this.risk_maximum_new = risk_maximum_new;
	}

	public String getZonal_railway_funds() {
		return zonal_railway_funds;
	}

	public void setZonal_railway_funds(String zonal_railway_funds) {
		this.zonal_railway_funds = zonal_railway_funds;
	}

	public String getSub_category() {
		return sub_category;
	}

	public void setSub_category(String sub_category) {
		this.sub_category = sub_category;
	}

	public String getLa_sub_category_new() {
		return la_sub_category_new;
	}

	public void setLa_sub_category_new(String la_sub_category_new) {
		this.la_sub_category_new = la_sub_category_new;
	}

	public String getLa_category_fk_new() {
		return la_category_fk_new;
	}

	public void setLa_category_fk_new(String la_category_fk_new) {
		this.la_category_fk_new = la_category_fk_new;
	}

	public String getLa_category_fk_old() {
		return la_category_fk_old;
	}

	public void setLa_category_fk_old(String la_category_fk_old) {
		this.la_category_fk_old = la_category_fk_old;
	}

	public String getLa_sub_category_old() {
		return la_sub_category_old;
	}

	public void setLa_sub_category_old(String la_sub_category_old) {
		this.la_sub_category_old = la_sub_category_old;
	}

	public String getRisk_area_fk() {
		return risk_area_fk;
	}

	public void setRisk_area_fk(String risk_area_fk) {
		this.risk_area_fk = risk_area_fk;
	}

	public String getRisk_area_fk_new() {
		return risk_area_fk_new;
	}

	public void setRisk_area_fk_new(String risk_area_fk_new) {
		this.risk_area_fk_new = risk_area_fk_new;
	}

	public String getSub_area() {
		return sub_area;
	}

	public void setSub_area(String sub_area) {
		this.sub_area = sub_area;
	}

	public String getSub_area_new() {
		return sub_area_new;
	}

	public void setSub_area_new(String sub_area_new) {
		this.sub_area_new = sub_area_new;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getItem_no() {
		return item_no;
	}

	public void setItem_no(String item_no) {
		this.item_no = item_no;
	}

	public String getItem_no_new() {
		return item_no_new;
	}

	public void setItem_no_new(String item_no_new) {
		this.item_no_new = item_no_new;
	}

	public String getShort_description() {
		return short_description;
	}

	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}

	public String getShort_description_new() {
		return short_description_new;
	}

	public void setShort_description_new(String short_description_new) {
		this.short_description_new = short_description_new;
	}

	public String getUser_access_type_val() {
		return user_access_type_val;
	}

	public void setUser_access_type_val(String user_access_type_val) {
		this.user_access_type_val = user_access_type_val;
	}

	public String getUser_access_table_new() {
		return user_access_table_new;
	}

	public void setUser_access_table_new(String user_access_table_new) {
		this.user_access_table_new = user_access_table_new;
	}

	public String getUser_role_code_new() {
		return user_role_code_new;
	}

	public void setUser_role_code_new(String user_role_code_new) {
		this.user_role_code_new = user_role_code_new;
	}

	public String getStructure_type() {
		return structure_type;
	}

	public void setStructure_type(String structure_type) {
		this.structure_type = structure_type;
	}

	public String getRoot_cause() {
		return root_cause;
	}

	public void setRoot_cause(String root_cause) {
		this.root_cause = root_cause;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getLand_type() {
		return land_type;
	}

	public void setLand_type(String land_type) {
		this.land_type = land_type;
	}

	public String getApproval_status() {
		return approval_status;
	}

	public void setApproval_status(String approval_status) {
		this.approval_status = approval_status;
	}

	public String getRisk_priority() {
		return risk_priority;
	}

	public void setRisk_priority(String risk_priority) {
		this.risk_priority = risk_priority;
	}

	public String getRevision_status() {
		return revision_status;
	}

	public void setRevision_status(String revision_status) {
		this.revision_status = revision_status;
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

	public String getRailway_id_val() {
		return railway_id_val;
	}

	public void setRailway_id_val(String railway_id_val) {
		this.railway_id_val = railway_id_val;
	}

	public String getProject_priority() {
		return project_priority;
	}

	public String getRailway_id() {
		return railway_id;
	}

	public void setRailway_id(String railway_id) {
		this.railway_id = railway_id;
	}

	public String getRailway_name() {
		return railway_name;
	}

	public void setRailway_name(String railway_name) {
		this.railway_name = railway_name;
	}

	public String getRailway_name_new() {
		return railway_name_new;
	}

	public void setRailway_name_new(String railway_name_new) {
		this.railway_name_new = railway_name_new;
	}

	public void setProject_priority(String project_priority) {
		this.project_priority = project_priority;
	}

	public String getP6_wbs_category() {
		return p6_wbs_category;
	}

	public void setP6_wbs_category(String p6_wbs_category) {
		this.p6_wbs_category = p6_wbs_category;
	}

	public String getModule_name() {
		return module_name;
	}

	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	public String getStatus_of() {
		return status_of;
	}

	public void setStatus_of(String status_of) {
		this.status_of = status_of;
	}

	public String getStatus_of_new() {
		return status_of_new;
	}

	public void setStatus_of_new(String status_of_new) {
		this.status_of_new = status_of_new;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getGeneral_status() {
		return general_status;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setGeneral_status(String general_status) {
		this.general_status = general_status;
	}

	public String getValue_old() {
		return value_old;
	}

	public void setValue_old(String value_old) {
		this.value_old = value_old;
	}

	public String getValue_new() {
		return value_new;
	}

	public void setValue_new(String value_new) {
		this.value_new = value_new;
	}

	public String getDocument_type_old() {
		return document_type_old;
	}

	public void setDocument_type_old(String document_type_old) {
		this.document_type_old = document_type_old;
	}

	public String getDocument_type_new() {
		return document_type_new;
	}

	public void setDocument_type_new(String document_type_new) {
		this.document_type_new = document_type_new;
	}

	public String getDeliverable_type_old() {
		return deliverable_type_old;
	}

	public void setDeliverable_type_old(String deliverable_type_old) {
		this.deliverable_type_old = deliverable_type_old;
	}

	public String getDeliverable_type_new() {
		return deliverable_type_new;
	}

	public void setDeliverable_type_new(String deliverable_type_new) {
		this.deliverable_type_new = deliverable_type_new;
	}

	public String getStatus_old() {
		return status_old;
	}

	public void setStatus_old(String status_old) {
		this.status_old = status_old;
	}

	public String getStatus_new() {
		return status_new;
	}

	public void setStatus_new(String status_new) {
		this.status_new = status_new;
	}

	public String getDashboard_type_old() {
		return dashboard_type_old;
	}

	public void setDashboard_type_old(String dashboard_type_old) {
		this.dashboard_type_old = dashboard_type_old;
	}

	public String getDashboard_type_new() {
		return dashboard_type_new;
	}

	public void setDashboard_type_new(String dashboard_type_new) {
		this.dashboard_type_new = dashboard_type_new;
	}

	public String getContract_type_old() {
		return contract_type_old;
	}

	public void setContract_type_old(String contract_type_old) {
		this.contract_type_old = contract_type_old;
	}

	public String getContract_type_new() {
		return contract_type_new;
	}

	public void setContract_type_new(String contract_type_new) {
		this.contract_type_new = contract_type_new;
	}

	public String getContractor_specialization_old() {
		return contractor_specialization_old;
	}

	public void setContractor_specialization_old(String contractor_specialization_old) {
		this.contractor_specialization_old = contractor_specialization_old;
	}

	public String getContractor_specialization_new() {
		return contractor_specialization_new;
	}

	public void setContractor_specialization_new(String contractor_specialization_new) {
		this.contractor_specialization_new = contractor_specialization_new;
	}

	public String getBinary_new() {
		return binary_new;
	}

	public void setBinary_new(String binary_new) {
		this.binary_new = binary_new;
	}

	public String getBinary_old() {
		return binary_old;
	}

	public void setBinary_old(String binary_old) {
		this.binary_old = binary_old;
	}

	public String getDepartment_new() {
		return department_new;
	}

	public void setDepartment_new(String department_new) {
		this.department_new = department_new;
	}

	public String getDepartment_old() {
		return department_old;
	}

	public void setDepartment_old(String department_old) {
		this.department_old = department_old;
	}

	public String getDepartment_name_new() {
		return department_name_new;
	}

	public void setDepartment_name_new(String department_name_new) {
		this.department_name_new = department_name_new;
	}

	public String getDepartment_name_old() {
		return department_name_old;
	}

	public void setDepartment_name_old(String department_name_old) {
		this.department_name_old = department_name_old;
	}

	public String getDepartment_code_old() {
		return department_code_old;
	}

	public void setDepartment_code_old(String department_code_old) {
		this.department_code_old = department_code_old;
	}

	public String getDepartment_code_new() {
		return department_code_new;
	}

	public void setDepartment_code_new(String department_code_new) {
		this.department_code_new = department_code_new;
	}

	public List<TrainingType> getdList() {
		return dList;
	}

	public void setdList(List<TrainingType> dList) {
		this.dList = dList;
	}

	public List<TrainingType> getdList1() {
		return dList1;
	}

	public void setdList1(List<TrainingType> dList1) {
		this.dList1 = dList1;
	}

	public String getBg_type_new() {
		return bg_type_new;
	}

	public void setBg_type_new(String bg_type_new) {
		this.bg_type_new = bg_type_new;
	}

	public List<TrainingType> getCountList() {
		return countList;
	}

	public void setCountList(List<TrainingType> countList) {
		this.countList = countList;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<TrainingType> getTablesList() {
		return tablesList;
	}

	public void setTablesList(List<TrainingType> tablesList) {
		this.tablesList = tablesList;
	}

	public List<TrainingType> getBankGaurenteeList1() {
		return bankGaurenteeList1;
	}

	public void setBankGaurenteeList1(List<TrainingType> bankGaurenteeList1) {
		this.bankGaurenteeList1 = bankGaurenteeList1;
	}

	public String getBg_type_fk() {
		return bg_type_fk;
	}

	public void setBg_type_fk(String bg_type_fk) {
		this.bg_type_fk = bg_type_fk;
	}

	public String getBg_type_old() {
		return bg_type_old;
	}

	public void setBg_type_old(String bg_type_old) {
		this.bg_type_old = bg_type_old;
	}

	public List<TrainingType> getBankGaurenteeList() {
		return bankGaurenteeList;
	}

	public void setBankGaurenteeList(List<TrainingType> bankGaurenteeList) {
		this.bankGaurenteeList = bankGaurenteeList;
	}
	
	public String getTable_name() {
		return Table_name;
	}

	public void setTable_name(String table_name) {
		Table_name = table_name;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getConstraint_name() {
		return constraint_name;
	}

	public void setConstraint_name(String constraint_name) {
		this.constraint_name = constraint_name;
	}

	public String getReferenced_table_name() {
		return referenced_table_name;
	}

	public void setReferenced_table_name(String referenced_table_name) {
		this.referenced_table_name = referenced_table_name;
	}

	public String getReferenced_column_name() {
		return referenced_column_name;
	}

	public void setReferenced_column_name(String referenced_column_name) {
		this.referenced_column_name = referenced_column_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLa_sub_category() {
		return la_sub_category;
	}

	public void setLa_sub_category(String la_sub_category) {
		this.la_sub_category = la_sub_category;
	}

	public String getLa_category_fk() {
		return la_category_fk;
	}

	public void setLa_category_fk(String la_category_fk) {
		this.la_category_fk = la_category_fk;
	}

	public String getLa_category() {
		return la_category;
	}

	public void setLa_category(String la_category) {
		this.la_category = la_category;
	}

	public String getContractor_specialization() {
		return contractor_specialization;
	}

	public void setContractor_specialization(String contractor_specialization) {
		this.contractor_specialization = contractor_specialization;
	}

	public String getDashboard_type() {
		return dashboard_type;
	}

	public void setDashboard_type(String dashboard_type) {
		this.dashboard_type = dashboard_type;
	}

	public String getBg_type() {
		return bg_type;
	}

	public void setBg_type(String bg_type) {
		this.bg_type = bg_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBinary() {
		return binary;
	}

	public void setBinary(String binary) {
		this.binary = binary;
	}

	public String getContract_type() {
		return contract_type;
	}

	public void setContract_type(String contract_type) {
		this.contract_type = contract_type;
	}

	public String getDeliverable_type() {
		return deliverable_type;
	}

	public void setDeliverable_type(String deliverable_type) {
		this.deliverable_type = deliverable_type;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getContract_id_code() {
		return contract_id_code;
	}

	public void setContract_id_code(String contract_id_code) {
		this.contract_id_code = contract_id_code;
	}

	public String getDocument_type() {
		return document_type;
	}

	public void setDocument_type(String document_type) {
		this.document_type = document_type;
	}

	public String getSoft_delete_status() {
		return soft_delete_status;
	}

	public void setSoft_delete_status(String soft_delete_status) {
		this.soft_delete_status = soft_delete_status;
	}

	public String getSource_of_funds() {
		return source_of_funds;
	}

	public void setSource_of_funds(String source_of_funds) {
		this.source_of_funds = source_of_funds;
	}

	public String getUser_access_type() {
		return user_access_type;
	}

	public void setUser_access_type(String user_access_type) {
		this.user_access_type = user_access_type;
	}

	public String getUser_access_table() {
		return user_access_table;
	}

	public void setUser_access_table(String user_access_table) {
		this.user_access_table = user_access_table;
	}

	public String getUser_role_name() {
		return user_role_name;
	}

	public void setUser_role_name(String user_role_name) {
		this.user_role_name = user_role_name;
	}

	public String getUser_role_code() {
		return user_role_code;
	}

	public void setUser_role_code(String user_role_code) {
		this.user_role_code = user_role_code;
	}

	public String getExecution_agency() {
		return execution_agency;
	}

	public void setExecution_agency(String execution_agency) {
		this.execution_agency = execution_agency;
	}

	public String getRequirement_stage() {
		return requirement_stage;
	}

	public void setRequirement_stage(String requirement_stage) {
		this.requirement_stage = requirement_stage;
	}

	public String getUtility_type() {
		return utility_type;
	}

	public void setUtility_type(String utility_type) {
		this.utility_type = utility_type;
	}

	public String getDrawing_type() {
		return drawing_type;
	}

	public void setDrawing_type(String drawing_type) {
		this.drawing_type = drawing_type;
	}

	public String getInsurance_type() {
		return insurance_type;
	}

	public void setInsurance_type(String insurance_type) {
		this.insurance_type = insurance_type;
	}

	public String getUnit_type() {
		return unit_type;
	}

	public void setUnit_type(String unit_type) {
		this.unit_type = unit_type;
	}

	public String getTraining_category() {
		return training_category;
	}

	public void setTraining_category(String training_category) {
		this.training_category = training_category;
	}

	public String getTraining_status() {
		return training_status;
	}

	public void setTraining_status(String training_status) {
		this.training_status = training_status;
	}

	public String getOld_training_type() {
		return old_training_type;
	}

	public void setOld_training_type(String old_training_type) {
		this.old_training_type = old_training_type;
	}

	public String getNew_training_type() {
		return new_training_type;
	}

	public void setNew_training_type(String new_training_type) {
		this.new_training_type = new_training_type;
	}

	public String getTraining_type() {
		return training_type;
	}

	public void setTraining_type(String training_type) {
		this.training_type = training_type;
	}

	public String getModule_incharge() {
		return module_incharge;
	}

	public void setModule_incharge(String module_incharge) {
		this.module_incharge = module_incharge;
	}

	public String getIncharge_user_id_fk() {
		return incharge_user_id_fk;
	}

	public void setIncharge_user_id_fk(String incharge_user_id_fk) {
		this.incharge_user_id_fk = incharge_user_id_fk;
	}

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public String getExecution_status() {
		return execution_status;
	}

	public void setExecution_status(String execution_status) {
		this.execution_status = execution_status;
	}

	public String getSoft_delete_status_fk() {
		return soft_delete_status_fk;
	}

	public void setSoft_delete_status_fk(String soft_delete_status_fk) {
		this.soft_delete_status_fk = soft_delete_status_fk;
	}

	public List<String> getAccess_user_types() {
		return access_user_types;
	}

	public void setAccess_user_types(List<String> access_user_types) {
		this.access_user_types = access_user_types;
	}

	public List<String> getAccess_user_roles() {
		return access_user_roles;
	}

	public void setAccess_user_roles(List<String> access_user_roles) {
		this.access_user_roles = access_user_roles;
	}

	public List<String> getAccess_users() {
		return access_users;
	}

	public void setAccess_users(List<String> access_users) {
		this.access_users = access_users;
	}

	public String getUser_types() {
		return user_types;
	}

	public void setUser_types(String user_types) {
		this.user_types = user_types;
	}

	public String getUser_roles() {
		return user_roles;
	}

	public void setUser_roles(String user_roles) {
		this.user_roles = user_roles;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String [] getArchive_date() {
		return archive_date;
	}

	public void setArchive_date(String [] archive_date) {
		this.archive_date = archive_date;
	}

	public String [] getArchive_url() {
		return archive_url;
	}

	public void setArchive_url(String [] archive_url) {
		this.archive_url = archive_url;
	}

	public String getCreated_by_user_id_fk() {
		return Created_by_user_id_fk;
	}

	public void setCreated_by_user_id_fk(String created_by_user_id_fk) {
		Created_by_user_id_fk = created_by_user_id_fk;
	}

	public String getArchive_dates() {
		return archive_dates;
	}

	public void setArchive_dates(String archive_dates) {
		this.archive_dates = archive_dates;
	}

	public String getArchive_urls() {
		return archive_urls;
	}

	public void setArchive_urls(String archive_urls) {
		this.archive_urls = archive_urls;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public String getProject_type_name() {
		return project_type_name;
	}

	public void setProject_type_name(String project_type_name) {
		this.project_type_name = project_type_name;
	}

	public String getProject_type_id() {
		return project_type_id;
	}

	public void setProject_type_id(String project_type_id) {
		this.project_type_id = project_type_id;
	}

	public String getProject_type() {
		return project_type;
	}

	public void setProject_type(String project_type) {
		this.project_type = project_type;
	}

	public String getSection_id() {
		return section_id;
	}

	public void setSection_id(String section_id) {
		this.section_id = section_id;
	}

	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public String getDivision_id() {
		return division_id;
	}

	public void setDivision_id(String division_id) {
		this.division_id = division_id;
	}

	public String getDivision_name() {
		return division_name;
	}

	public void setDivision_name(String division_name) {
		this.division_name = division_name;
	}

	public String getRailway_zone() {
		return railway_zone;
	}

	public void setRailway_zone(String railway_zone) {
		this.railway_zone = railway_zone;
	}

}
