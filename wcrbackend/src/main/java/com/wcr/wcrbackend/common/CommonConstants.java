package com.wcr.wcrbackend.common;


public class CommonConstants {
	
	
	static UrlGenerator ugObj = new UrlGenerator();
	static String context_path = ugObj.getContextPath();
	static String base_path = ugObj.getNGINXFilesBasePath();
	
	 
	public final static String BASE_URL_SYNTRACK = "https://infoviz.syntrackpro.com/trusted/{0}/";  //{1} is the placeholder for tableau trusted token
	public final static String BASE_URL_MRVC = "http://203.153.40.44:8000/trusted/{0}/";  //{0} is the placeholder for tableau trusted token
//	public final static String BASE_URL_SYNTRACK = "https://{0}/trusted/{1}/";  //{0} is the placeholder for IP Address And {1} is the placeholder for tableau trusted token
//	public final static String BASE_URL_MRVC = "http://{0}:8000/trusted/{1}/";  //{0} is the placeholder for IP Address And {1} is the placeholder for tableau trusted token
//	
	public final static String TABLEAU_PARAMS = "&:tabs=no&:toolbar=no";
	
	
	public final static int  RANDOM_NUMERIC_NUMBER_LENGTH = 5;
	
	public final static String  BCC_MAIL = "support_pmis@mrvc.gov.in";
	
	public final static String  ROLE_CODE_DATA_ADMIN = "DA";
	public final static String  ROLE_CODE_INPUT_USER = "IU";
	public final static String  ROLE_CODE_IT_ADMIN = "IT";
	public final static String  ROLE_CODE_REGULAR_USER = "RU";
	public final static String  ROLE_CODE_SUPER_USER = "SU";
	
	public final static String  ACTIVE = "Active";
	public final static String  INACTIVE = "Inactive";
	
	public final static String  YES = "Yes";
	public final static String  USER = "Synergiz";
	public final static String  USER_TYPE = "HOD";
	public final static String  USER_TYPE2 = "DyHOD";
	
	public final static String  USER_TYPE_HOD = "HOD";
	public final static String  USER_TYPE_DYHOD = "DyHOD";
	public final static String  USER_TYPE_MANAGEMENT = "Management";
	public final static String  USER_TYPE_OTHERS = "Others";
	public final static String  DEPARTMENT_ID_MANAGEMENT = "MGMT";
	
	public final static String  ISSUE_STATUS_RAISED = "Raised";
	
	public static String LOGIN_BACKGROUND_IMAGE_URL = "/"+context_path+"/resources/images/login-background.jpg";
	
	public final static String SAFETY_EQUIPMENT_FILES = "/"+context_path+"/SAFETY_EQUIPMENT_FILES/";
	public final static String BUDGET_FILES = "/"+context_path+"/BUDGET_FILES/";
	public final static String FUND_FILES = "/"+context_path+"/FUND_FILES/";
	public final static String DOCUMENT_FILES = "/"+context_path+"/DOCUMENT_FILES/";
	public final static String SAFETY_FILES = "/"+context_path+"/SAFETY_FILES/";
	public final static String SAFETY_INSTRUCTIONS_FILES = "/"+context_path+"/SAFETY_INSTRUCTIONS_FILES/";
	public final static String MANUAL_FILES = "/"+context_path+"/MANUAL_FILES/";
	public static final String PROJECT_FILES = "/"+context_path+"/PROJECT_FILES/";
	public static final String WORK_FILES = "/"+context_path+"/WORK_FILES/";
	public static final String DELIVERABLES_FILES = "/"+context_path+"/DELIVERABLES_FILES/";
	public static final String TRAINING_SESSIONS = "/"+context_path+"/TRAINING_SESSIONS/";
	public static final String LAND_ACQUISITION_FILES = "/"+context_path+"/LAND_ACQUISITION_FILES/";
	public final static String DESIGN_UPLOADED_FILES = "/"+context_path+"/DESIGN_UPLOADED_FILES/";
	public final static String STRUCTURE_UPLOADED_FILES = "/"+context_path+"/STRUCTURE_UPLOADED_FILES/";
	public final static String TEMPLATE_FILE_SAVE_PATH = "/"+context_path+"/TEMPLATES_OLD/";
	public final static String RR_UPLOADED_FILES = "/"+context_path+"/RR_UPLOADED_FILES/";
	public final static String UTILITY_UPLOADED_FILES = "/"+context_path+"/UTILITY_UPLOADED_FILES/";
	public final static String RR_AGENCY_FILES = "/"+context_path+"/RR_AGENCY_FILES/";
	public final static String APPROVAL_BY_BANK_FILES = "/"+context_path+"/APPROVAL_BY_BANK_FILES/";
	
	/*********************************************************************************************/	
	public final static String LOGIN_BACKGROUND_IMAGE = base_path+"/resources/images/";
	
	public final static String STRIPCHART_FILE_SAVING_PATH = base_path+"/STRIPCHART_FILES/";
	public final static String WORK_FILE_SAVING_PATH = base_path+"/WORK_FILES/";
	public final static String CONTRACT_FILE_SAVING_PATH = base_path+"/CONTRACT_FILES/";
	public final static String DESIGN_REVISION_FILES = base_path+"/DESIGN_REVISION_FILES/";
	public final static String DESIGN_FILE_SAVING_PATH = base_path+"/DESIGN_FILES/";
	public final static String SAFETYEQUIPMENT_FILE_SAVING_PATH = base_path+"/SAFETY_EQUIPMENT_FILES/";
	public final static String BUDGET_FILE_SAVING_PATH = base_path+"/BUDGET_FILES/";
	public final static String FUND_FILE_SAVING_PATH = base_path+"/FUND_FILES/";
	public final static String DOCUMENT_FILES_SAVING_PATH = base_path+"/DOCUMENT_FILES/";
	public final static String SAFETY_INSTRUCTIONS_FILE_SAVING_PATH = base_path+"/SAFETY_INSTRUCTIONS_FILES/";
	public final static String MANUAL_FILE_SAVING_PATH = base_path+"/MANUAL_FILES/";
	public final static String PROJECT_FILE_SAVING_PATH = base_path+"/PROJECT_FILES/";
	public final static String DELIVERABLES_FILE_SAVING_PATH = base_path+"/DELIVERABLES_FILES/";
	public final static String TRAINING_SESSION_FILE_SAVING_PATH = base_path+"/TRAINING_SESSIONS/";
	public final static String LAND_ACQUISITION_FILE_SAVING_PATH = base_path+"/LAND_ACQUISITION_FILES/";
	public final static String RR_AGENCY_FILE_SAVING_PATH = base_path+"/RR_AGENCY_FILES/";
	public final static String APPROVAL_BY_BANK_FILE_SAVING_PATH = base_path+"/APPROVAL_BY_BANK_FILES/";
	
	public final static String DESIGN_UPLOADED_FILE_SAVING_PATH = base_path+"/DESIGN_UPLOADED_FILES/";
	public final static String TEMPLATE_FILEPATH = base_path+"/";
	public final static String TEMPLATE_OLD_FILEPATH = base_path+"/TEMPLATES_OLD/";
	public final static String STRUCTURE_UPLOADED_FILE_SAVING_PATH = base_path+"/STRUCTURE_UPLOADED_FILES/";
	public final static String RR_UPLOADED_FILE_SAVING_PATH = base_path+"/RR_UPLOADED_FILES/";
	public final static String WORK_CHAINAGES_UPLOADED_FILE_SAVING_PATH = base_path+"/WORK_CHAINAGES_UPLOADED_FILES/";
	
	public final static String UTILITY_UPLOADED_FILE_SAVING_PATH = base_path+"/UTILITY_UPLOADED_FILES/";

	/*********** LOCAL ****************************************************/	
	/*public static String DIRECTORY_PATH = "E:";
	
	public final static String LOGIN_BACKGROUND_IMAGE = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"resources"+File.separator+"images"+File.separator;
	public static String LOGIN_BACKGROUND_IMAGE_URL = "/"+context_path+"/resources/images/login-background.jpg";
	public final static String CONTEXT_PATH = "http://localhost/"+context_path+"";
	public final static String STRIPCHART_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"STRIPCHART_FILES"+File.separator;
	public final static String WORK_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"WORK_FILES"+File.separator;
	public final static String CONTRACT_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"CONTRACT_FILES"+File.separator;
	public final static String DESIGN_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"DESIGN_FILES"+File.separator;
	public final static String SAFETYEQUIPMENT_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"SAFETY_EQUIPMENT_FILES"+File.separator;
	public final static String BUDGET_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"BUDGET_FILES"+File.separator;
	public final static String FUND_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"FUND_FILES"+File.separator;
	public final static String DOCUMENT_FILES_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"DOCUMENT_FILES"+File.separator;
	public static final String SAFETY_INSTRUCTIONS_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"SAFETY_INSTRUCTIONS_FILES"+File.separator;
	public final static String MANUAL_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"MANUAL_FILES"+File.separator;
	public final static String PROJECT_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"PROJECT_FILES"+File.separator;
	public final static String DELIVERABLES_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"DELIVERABLES_FILES"+File.separator;
	public final static String TRAINING_SESSION_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"TRAINING_SESSIONS"+File.separator;
	public final static String LAND_ACQUISITION_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"LAND_ACQUISITION_FILES"+File.separator;
	public final static String DESIGN_UPLOADED_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development/nginx-1.9.9/html/"+context_path+"/DESIGN_UPLOADED_FILES/";
	public final static String TEMPLATE_FILEPATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator;
	public final static String TEMPLATE_OLD_FILEPATH = DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"TEMPLATES_OLD"+File.separator;
	public final static String STRUCTURE_UPLOADED_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development/nginx-1.9.9/html/"+context_path+"/STRUCTURE_UPLOADED_FILES/";
	public final static String RR_UPLOADED_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development/nginx-1.9.9/html/"+context_path+"/RR_UPLOADED_FILES/";
	public final static String UTILITY_UPLOADED_FILE_SAVING_PATH = DIRECTORY_PATH+File.separator+"Development/nginx-1.9.9/html/"+context_path+"/UTILITY_UPLOADED_FILES/";
	*/
	/*********** Syntract AWS ****************************************************/	
	/*public final static String LOGIN_BACKGROUND_IMAGE = "usr/share/nginx/html/"+context_path+"/resources/images/";
	public static String LOGIN_BACKGROUND_IMAGE_URL = "/"+context_path+"/resources/images/login-background.jpg";
	public final static String CONTEXT_PATH = "http://syntrackpro.com/"+context_path+"";
	public final static String STRIPCHART_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/STRIPCHART_FILES/";
	public final static String WORK_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/WORK_FILES/";
	public final static String CONTRACT_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/CONTRACT_FILES/";
	public final static String DESIGN_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/DESIGN_FILES/";
	public final static String SAFETYEQUIPMENT_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/SAFETY_EQUIPMENT_FILES/";
	public final static String BUDGET_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/BUDGET_FILES/";
	public final static String FUND_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/FUND_FILES/";
	public final static String DOCUMENT_FILES_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/DOCUMENT_FILES/";
	public final static String SAFETY_INSTRUCTIONS_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/SAFETY_INSTRUCTIONS_FILES/";
	public final static String MANUAL_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/MANUAL_FILES/";
	public final static String PROJECT_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/PROJECT_FILES/";
	public final static String DELIVERABLES_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/DELIVERABLES_FILES/";
	public final static String TRAINING_SESSION_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/TRAINING_SESSIONS/";
	public final static String LAND_ACQUISITION_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/LAND_ACQUISITION_FILES/";
	public final static String DESIGN_UPLOADED_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/DESIGN_UPLOADED_FILES/";
	public final static String STRUCTURE_UPLOADED_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/STRUCTURE_UPLOADED_FILES/";
	public final static String TEMPLATE_FILEPATH = "usr/share/nginx/html/"+context_path+"/";
	public final static String TEMPLATE_OLD_FILEPATH = "usr/share/nginx/html/"+context_path+"/TEMPLATES_OLD/";
	public final static String RR_UPLOADED_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/RR_UPLOADED_FILES/";
	public final static String UTILITY_UPLOADED_FILE_SAVING_PATH = "usr/share/nginx/html/"+context_path+"/UTILITY_UPLOADED_FILES/";

	*/
	
	/*********** MRVC Server ****************************************************/	
	/*public final static String LOGIN_BACKGROUND_IMAGE = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"resources"+File.separator+"images"+File.separator;
	public static String LOGIN_BACKGROUND_IMAGE_URL = "/"+context_path+"/resources/images/login-background.jpg";
	public final static String CONTEXT_PATH = "http://10.203.10.157/"+context_path+"";
	public final static String STRIPCHART_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"STRIPCHART_FILES"+File.separator;
	public final static String WORK_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"WORK_FILES"+File.separator;
	public final static String CONTRACT_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"CONTRACT_FILES"+File.separator;
	public final static String DESIGN_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"DESIGN_FILES"+File.separator;
	public final static String SAFETYEQUIPMENT_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"SAFETY_EQUIPMENT_FILES"+File.separator;
	public final static String BUDGET_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"BUDGET_FILES"+File.separator;
	public final static String FUND_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"FUND_FILES"+File.separator;
	public final static String DOCUMENT_FILES_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"DOCUMENT_FILES"+File.separator;
	public final static String SAFETY_INSTRUCTIONS_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"SAFETY_INSTRUCTIONS_FILES"+File.separator;
	public final static String MANUAL_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"MANUAL_FILES"+File.separator;
	public final static String PROJECT_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"PROJECT_FILES"+File.separator;
	public final static String DELIVERABLES_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"DELIVERABLES_FILES"+File.separator;
	public final static String TRAINING_SESSION_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"TRAINING_SESSIONS"+File.separator;
	public final static String LAND_ACQUISITION_FILE_SAVING_PATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"LAND_ACQUISITION_FILES"+File.separator;
	public final static String DESIGN_UPLOADED_FILE_SAVING_PATH = "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+"/DESIGN_UPLOADED_FILES/";
	public final static String TEMPLATE_FILEPATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator;
	public final static String TEMPLATE_OLD_FILEPATH = "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File.separator+"html"+File.separator+context_path+File.separator+"TEMPLATES_OLD"+File.separator;
	public final static String STRUCTURE_UPLOADED_FILE_SAVING_PATH = "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+"/STRUCTURE_UPLOADED_FILES/";
	public final static String RR_UPLOADED_FILE_SAVING_PATH = "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+"/RR_UPLOADED_FILES/";
	public final static String UTILITY_UPLOADED_FILE_SAVING_PATH = "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+"/UTILITY_UPLOADED_FILES/";

	*/
}
