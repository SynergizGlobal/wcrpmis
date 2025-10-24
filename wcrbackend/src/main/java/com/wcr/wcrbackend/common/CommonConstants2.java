package com.wcr.wcrbackend.common;

public class CommonConstants2 {

	static UrlGenerator ugObj = new UrlGenerator();
	static String context_path = ugObj.getContextPath();
	static String base_path = ugObj.getNGINXFilesBasePath();

	static String user_login_report_mail_id = ugObj.getUser_login_report_mail_id();
	static String managers_mail_ids = ugObj.getManagers_mail_ids_for_alerts();

	public final static String STATUS_COMPLETED = "Completed";
	public final static String STATUS_DROPPED = "Dropped";
	public final static String STATUS_IN_PROGRESS = "In Progress";
	public final static String STATUS_NOT_STARTED = "Not Started";
	public final static String STATUS_ON_HOLD = "On Hold";

	public final static String LOGIN_EVENT_TYPE_LOGIN = "Login";
	public final static String LOGIN_EVENT_TYPE_LOGOUT = "Logout";

	public final static String LOGOUT_TYPE_TIMEOUT = "Timeout";
	public final static String LOGOUT_TYPE_LOGOUT = "Logout";

	public final static String ALERT_TYPE_CONTRACT = "Bank Guarantee,Insurance,Contract Period,Contract Value";
	public final static String ALERT_TYPE_BANK_GUARANTEE = "Bank Guarantee";
	public final static String ALERT_TYPE_CONTRACT_PERIOD = "Contract Period";
	public final static String ALERT_TYPE_CONTRACT_VALUE = "Contract Value";
	public final static String ALERT_TYPE_INSURANCE = "Insurance";
	public final static String ALERT_TYPE_ISSUE = "Issue";
	public final static String ALERT_TYPE_PHYSICAL_PROGRESS = "Physical Progress";
	public final static String ALERT_TYPE_RISK = "Risk";
	public final static String ALERT_TYPE_SAFETY = "Safety";

	public static final String USER_IMAGES = "/" + context_path + "/USER_IMAGES/";
	public static final String CONTRACT_FILES = "/" + context_path + "/CONTRACT_FILES/";
	public static final String FOB_GALLERY = "/" + context_path + "/FOB_GALLERY/";
	public static final String FOB_FILES = "/" + context_path + "/FOB_FILES/";
	public static final String DRONE_SURVEY = "/"+context_path+"/DRONE_SURVEY/";
	public static final String ISSUE_FILES = "/" + context_path + "/ISSUE_FILES/";
	public static final String SAFETY_FILES = "/" + context_path + "/SAFETY_FILES/";
	public static final String WORK_FILES = "/" + context_path + "/WORK_FILES/";
	public static final String WORK_CHAINAGES_UPLOADED_FILES = "/" + context_path + "/WORK_CHAINAGES_UPLOADED_FILES/";
	public static final String DESIGN_FILES = "/" + context_path + "/DESIGN_FILES/";
	public static final String P6_FILES = "/" + context_path + "/P6_FILES/";
	public static final String STRIPCHART_FILES = "/" + context_path + "/STRIPCHART_FILES/";
	public final static String WEB_DOCUMENTS = "/" + context_path + "/WEB_DOCUMENTS/";
	public final static String PROJECT_GALLERY = "/" + context_path + "/PROJECT_GALLERY/";
	public final static String ACTIVITY_PROGRESS_REPORT = "/" + context_path + "/ACTIVITY_PROGRESS_REPORTS/";
	public final static String ACTIVITIES_UPLOAD_FILES = "/" + context_path + "/ACTIVITIES_UPLOAD_FILES/";
	public final static String RISK_ASSESSMENT_UPLOADED_FILES = "/" + context_path + "/RISK_ASSESSMENT_UPLOADED_FILES/";
	public final static String PMIS_MANUALS = "/" + context_path + "/PMIS_MANUALS/";
	public final static String STRUCTURE_FILES = "/" + context_path + "/STRUCTURE_FILES/";
	public final static String UTILITY_SHIFTING_FILES = "/" + context_path + "/UTILITY_SHIFTING_FILES/";
	public final static String RRBSES_FILES = "/" + context_path + "/RRBSES_FILES/";

	/*******************************************************************************************/

	public final static String USER_LOGIN_REPORT_MAIL = user_login_report_mail_id;
	public final static String ALERTS_EMAIL = user_login_report_mail_id;

	public final static String ISSUE_FILE_SAVING_PATH = base_path + "/ISSUE_FILES/";
	public static final String SAFETY_FILE_SAVING_PATH = base_path + "/SAFETY_FILES/";
	public static final String FOB_GALLERY_SAVING_PATH = base_path + "/FOB_GALLERY/";
	public static final String FOB_FILE_SAVING_PATH = base_path + "/FOB_FILES/";
	public static final String DRONE_SURVEY_SAVING_PATH = base_path+"/DRONE_SURVEY/";
	public static final String USER_IMAGE_SAVING_PATH = base_path + "/USER_IMAGES/";
	public static final String DESIGN_FILE_SAVING_PATH = base_path + "/DESIGN_FILES/";
	public static final String P6_FILE_SAVING_PATH = base_path + "/P6_FILES/";
	public static final String FORTNIGHT_PLAN_UPLOAD_REMARKS_PATH = base_path + "/FORTNIGHT_PLAN_UPLOAD_REMARKS_PATH/";
	public static final String QUARTERLY_PLAN_UPLOAD_PATH = base_path + "/QUARTERLY_PLAN_UPLOAD_PATH/";
	public static final String DOCX_LOGO = base_path + "/resources/images";
	public final static String WEB_DOCUMENTS_FILE_SAVING_PATH = base_path + "/WEB_DOCUMENTS/";
	public final static String PROJECT_GALLERY_FILE_SAVING_PATH = base_path + "/PROJECT_GALLERY/";
	public final static String TRAINING_GALLERY_FILE_SAVING_PATH = base_path + "/TRAINING_GALLERY/";
	public final static String ACTIVITY_PROGRESS_FILE_SAVING_PATH = base_path + "/ACTIVITY_PROGRESS_REPORTS/";
	public final static String ACTIVITIES_UPLOAD_FILE_SAVING_PATH = base_path + "/ACTIVITIES_UPLOAD_FILES/";
	public final static String RISK_ASSESSMENT_UPLOADED_FILE_SAVING_PATH = base_path
			+ "/RISK_ASSESSMENT_UPLOADED_FILES/";
	public final static String PMIS_MANUAL_SAVING_PATH = base_path + "/PMIS_MANUALS/";
	public final static String STRUCTURE_FILE_SAVING_PATH = base_path + "/STRUCTURE_FILES/";
	public static final String UTILITY_SHIFTING_FILE_SAVING_PATH = base_path + "/UTILITY_SHIFTING_FILES/";
	public static final String RRBSES_FILE_SAVING_PATH = base_path + "/RRBSES_FILES/";

	/*********** LOCAL ****************************************************/
	/*
	 * public static String DIRECTORY_PATH = "C:";
	 * 
	 * public final static String USER_LOGIN_REPORT_MAIL =
	 * "gkalluri@ghsltechnologies.com"; public final static String ALERTS_EMAIL =
	 * "kallurigurappa@gmail.com,gkalluri@ghsltechnologies.com"; public final static
	 * String ISSUE_FILE_SAVING_PATH =
	 * DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"ISSUE_FILES"+
	 * File.separator; public static final String SAFETY_FILE_SAVING_PATH =
	 * DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"SAFETY_FILES"+
	 * File.separator; public static final String FOB_GALLERY_SAVING_PATH =
	 * DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"FOB_GALLERY"+
	 * File.separator; public static final String FOB_FILE_SAVING_PATH =
	 * DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"FOB_FILES"+File
	 * .separator; public static final String STRUCTURE_FILE_SAVING_PATH =
	 * DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+
	 * "STRUCTURE_FILES"+File.separator; public static final String
	 * UTILITY_SHIFTING_FILE_SAVING_PATH =
	 * DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+
	 * "UTILITY_SHIFTING_FILES"+File.separator;
	 * 
	 * 
	 * public static final String USER_IMAGE_SAVING_PATH =
	 * DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"USER_IMAGES"+
	 * File.separator; public static final String DESIGN_FILE_SAVING_PATH =
	 * DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"DESIGN_FILES"+
	 * File.separator; public static final String P6_FILE_SAVING_PATH =
	 * DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"P6_FILES"+File.
	 * separator; public static final String DOCX_LOGO = DIRECTORY_PATH +
	 * "/Development/nginx-1.9.9/html/"+context_path+"/resources/images"; public
	 * final static String WEB_DOCUMENTS_FILE_SAVING_PATH = DIRECTORY_PATH +
	 * "/Development/nginx-1.9.9/html/"+context_path+"/WEB_DOCUMENTS/"; public final
	 * static String PROJECT_GALLERY_FILE_SAVING_PATH = DIRECTORY_PATH +
	 * "/Development/nginx-1.9.9/html/"+context_path+"/PROJECT_GALLERY/"; //public
	 * final static String ACTIVITY_PROGRESS_FILE_SAVING_PATH = DIRECTORY_PATH +
	 * "/Development/nginx-1.9.9/html/"+context_path+"/ACTIVITY_PROGRESS_REPORTS/";
	 * public final static String ACTIVITIES_UPLOAD_FILE_SAVING_PATH =
	 * DIRECTORY_PATH +
	 * "/Development/nginx-1.9.9/html/"+context_path+"/ACTIVITIES_UPLOAD_FILES/";
	 * public final static String RISK_ASSESSMENT_UPLOADED_FILE_SAVING_PATH =
	 * DIRECTORY_PATH + "/Development/nginx-1.9.9/html/"+context_path+
	 * "/RISK_ASSESSMENT_UPLOADED_FILES/"; public final static String
	 * PMIS_MANUAL_SAVING_PATH = DIRECTORY_PATH +
	 * "/Development/nginx-1.9.9/html/"+context_path+"/PMIS_MANUALS/";
	 * 
	 * public static final String RRBSES_FILE_SAVING_PATH =
	 * DIRECTORY_PATH+File.separator+"Development"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"RRBSES_FILES"+
	 * File.separator;
	 */
	/*********** Syntrack AWS ****************************************************/
	/*
	 * public final static String USER_LOGIN_REPORT_MAIL =
	 * "raviteja.reddy@synergizglobal.com"; public final static String ALERTS_EMAIL
	 * = "rajiv.dhupkar@synergizglobal.com,raviteja.reddy@synergizglobal.com";
	 * public final static String ISSUE_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/ISSUE_FILES/"; public final static
	 * String SAFETY_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/SAFETY_FILES/"; public final static
	 * String FOB_GALLERY_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/FOB_GALLERY/"; public final static
	 * String FOB_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/FOB_FILES/"; public final static
	 * String USER_IMAGE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/USER_IMAGES/"; public final static
	 * String DESIGN_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/DESIGN_FILES/"; public final static
	 * String P6_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/P6_FILES/"; public static final String
	 * DOCX_LOGO = "usr/share/nginx/html/"+context_path+"/resources/images"; public
	 * final static String WEB_DOCUMENTS_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/WEB_DOCUMENTS/"; public final static
	 * String PROJECT_GALLERY_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/PROJECT_GALLERY/"; public final static
	 * String ACTIVITY_PROGRESS_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/ACTIVITY_PROGRESS_REPORTS/"; public
	 * final static String ACTIVITIES_UPLOAD_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/ACTIVITIES_UPLOAD_FILES/"; public
	 * final static String RISK_ASSESSMENT_UPLOADED_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/RISK_ASSESSMENT_UPLOADED_FILES/";
	 * public final static String PMIS_MANUAL_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/PMIS_MANUALS/"; public final static
	 * String STRUCTURE_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/STRUCTURE_FILES/"; public static final
	 * String UTILITY_SHIFTING_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/UTILITY_SHIFTING_FILES/"; public
	 * static final String RRBSES_FILE_SAVING_PATH =
	 * "usr/share/nginx/html/"+context_path+"/RRBSES_FILES/";
	 * 
	 */

	/*********** MRVC Server ****************************************************/
	/*
	 * public final static String USER_LOGIN_REPORT_MAIL = "dycste2@mrvc.gov.in";
	 * public final static String ALERTS_EMAIL =
	 * "rajiv.dhupkar@synergizglobal.com,raviteja.reddy@synergizglobal.com"; public
	 * final static String ISSUE_FILE_SAVING_PATH =
	 * "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"ISSUE_FILES"+
	 * File.separator; public static final String SAFETY_FILE_SAVING_PATH =
	 * "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"SAFETY_FILES"+
	 * File.separator; public static final String FOB_GALLERY_SAVING_PATH =
	 * "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"FOB_GALLERY"+
	 * File.separator; public static final String FOB_FILE_SAVING_PATH =
	 * "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"FOB_FILES"+File
	 * .separator; public static final String USER_IMAGE_SAVING_PATH =
	 * "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"USER_IMAGES"+
	 * File.separator; public static final String DESIGN_FILE_SAVING_PATH =
	 * "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"DESIGN_FILES"+
	 * File.separator; public static final String P6_FILE_SAVING_PATH =
	 * "D:"+File.separator+"PMISApplicationServer"+File.separator+"nginx-1.9.9"+File
	 * .separator+"html"+File.separator+context_path+File.separator+"P6_FILES"+File.
	 * separator; public static final String DOCX_LOGO =
	 * "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+
	 * "/resources/images"; public final static String
	 * WEB_DOCUMENTS_FILE_SAVING_PATH =
	 * "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+"/WEB_DOCUMENTS/";
	 * public final static String PROJECT_GALLERY_FILE_SAVING_PATH =
	 * "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+
	 * "/PROJECT_GALLERY/"; public final static String
	 * ACTIVITY_PROGRESS_FILE_SAVING_PATH =
	 * "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+
	 * "/ACTIVITY_PROGRESS_REPORTS/"; public final static String
	 * ACTIVITIES_UPLOAD_FILE_SAVING_PATH =
	 * "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+
	 * "/ACTIVITIES_UPLOAD_FILES/"; public final static String
	 * RISK_ASSESSMENT_UPLOADED_FILE_SAVING_PATH =
	 * "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+
	 * "/RISK_ASSESSMENT_UPLOADED_FILES/"; public final static String
	 * PMIS_MANUAL_SAVING_PATH =
	 * "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+"/PMIS_MANUALS/";
	 * public final static String STRUCTURE_FILE_SAVING_PATH =
	 * "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+
	 * "/STRUCTURE_FILES/"; public static final String
	 * UTILITY_SHIFTING_FILE_SAVING_PATH =
	 * "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+
	 * "/UTILITY_SHIFTING_FILES/"; public static final String
	 * RRBSES_FILE_SAVING_PATH =
	 * "D:/PMISApplicationServer/nginx-1.9.9/html/"+context_path+"/RRBSES_FILES/";
	 * 
	 */

}
