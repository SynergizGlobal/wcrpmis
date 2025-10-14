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
public class LandAcquisition {

	private String work_id, la_id,work_code, project_id,project_id_fk,project_name,work_id_fk,work_name,work_short_name,survey_number, la_sub_category_fk,la_sub_category,la_category,village_id,status, status_of, type_of_land, sub_category_of_land, village, taluka, dy_slr, sdo, collector, proposal_submission_date_to_collector, area_of_plot, jm_fee_amount, chainage_from, chainage_to, jm_fee_letter_received_date, jm_fee_paid_date, 
	jm_start_date, jm_completion_date,is_there_issue,category_id,category_fk, jm_sheet_date_to_sdo, jm_remarks, jm_approval, issues,attachment,category,issue_description,issue_priority_id,issue_category_id,hod_user_id_fk
	,jm_fee_amount_units,value,unit,la_file_type,la_land_status,executive_user_id_fk,modified_by,modified_date,created_by_user_id_fk,user_id,user_name,designation,
	//gov
	id, la_id_fk, area_to_be_acquired, proposal_submission, proposal_submission_status_fk, valuation_date, letter_for_payment, amount_demanded, lfp_status_fk, approval_for_payment, payment_date, amount_paid, payment_status_fk, possession_date, possession_status_fk, special_feature,
    area_acquired, remarks,gov_remarks,amount_demanded_units,amount_paid_units,user_type_fk,user_role_code,balance_area,
    
    //private
    name_of_the_owner,private_area_to_be_acquired,private_area_acquired, basic_rate,private_attachment_no, attachment_no,  agriculture_tree_rate,  forest_tree_rate, consent_from_owner, legal_search_report, date_of_registration, date_of_possession, 
    private_possession_status_fk, private_special_feature, hundred_percent_Solatium, extra_25_percent, total_rate_divide_m2, land_compensation, agriculture_tree_compensation, forest_tree_compensation, structure_compensation, borewell_compensation, total_compensation,
    forest_tree_survey, forest_tree_valuation, forest_tree_valuation_status_fk, horticulture_tree_survey, horticulture_tree_valuation, structure_survey,private_remarks,
    structure_valuation, borewell_survey, borewell_valuation, horticulture_tree_valuation_status_fk, structure_valuation_status_fk, borewell_valuation_status_fk, 
    rfp_to_adtp_status_fk, date_of_rfp_to_adtp, date_of_rate_fixation_of_land, sdo_demand_for_payment, date_of_approval_for_payment, payment_amount, private_payment_date
    ,payment_amount_units,basic_rate_units,agriculture_tree_rate_units,forest_tree_rate_units
    
    //forest
    ,forest_area_to_be_acquired,forest_area_acquired, forest_online_submission, forest_submission_date_to_dycfo,la_land_status_fk,  forest_submission_date_to_ccf_thane, forest_submission_date_to_nodal_officer, forest_submission_date_to_revenue_secretary_mantralaya, forest_submission_date_to_regional_office_nagpur, forest_date_of_approval_by_regional_office_nagpur, 
    forest_valuation_by_dycfo, forest_demanded_amount, forest_payment_amount,forest_survey_number,forest_remarks,
    forest_approval_for_payment, forest_payment_date, forest_possession_date, forest_possession_status_fk, forest_payment_status_fk, forest_special_feature, forest_attachment_No,
    demanded_amount_units_forest,payment_amount_units_forest,
    //railway
    railway_area_to_be_acquired,railway_remarks,  railway_online_submission,railway_area_acquired,  railway_submission_date_to_DyCFO,  railway_submission_date_to_CCF_Thane, railway_submission_date_to_nodal_officer_CCF_Nagpur, 
    railway_submission_date_to_revenue_secretary_mantralaya, railway_submission_date_to_regional_office_nagpur,  railway_date_of_approval_by_Rregional_Office_agpur,  railway_valuation_by_DyCFO, railway_demanded_amount, railway_approval_for_payment, railway_payment_date, railway_payment_amount, railway_payment_status, railway_possession_date,  railway_possession_status, railway_special_feature, railway_attachment_no
    ,demanded_amount_units,payment_amount_units_railway,private_land_process,date_of_submission_of_draft_notification_to_CALA_20ff,balance,planned_date_of_possession,issue_id,searchStr;
	
	
	//private indian Act
	private String submission_of_proposal_to_GM,la_file_id, requried_area,approval_of_GM, draft_letter_to_con_for_approval_rp, date_of_approval_of_construction_rp, date_of_uploading_of_gazette_notification_rp, publication_in_gazette_rp, date_of_proposal_to_DC_for_nomination, date_of_nomination_of_competenta_authority, draft_letter_to_con_for_approval_ca, date_of_approval_of_construction_ca, date_of_uploading_of_gazette_notification_ca, publication_in_gazette_ca, date_of_submission_of_draft_notification_to_CALA, approval_of_CALA_20a, draft_letter_to_con_for_approval_20a, date_of_approval_of_construction_20a, date_of_uploading_of_gazette_notification_20a, publication_in_gazette_20a, publication_in_2_local_news_papers_20a, pasting_of_notification_in_villages_20a, receipt_of_grievances, disposal_of_grievances, date_of_submission_of_draft_notification_to_CALA_20e, approval_of_CALA_20e, draft_letter_to_con_for_approval_20e, date_of_approval_of_construction_20e, date_of_uploading_of_gazette_notification_20e, publication_in_gazette_20e, publication_of_notice_in_2_local_news_papers_20e, date_of_submission_of_draft_notification_to_CALA_20f, approval_of_CALA_20f, draft_letter_to_con_for_approval_20f, date_of_approval_of_construction_20f, date_of_uploading_of_gazette_notification_20f, 
	publication_in_gazette_20f, publication_of_notice_in_2_local_news_papers_20f,la_file_type_fk, name,private_ira_collector,mail_body_header,latitude,longitude,contract_id_fk;
	private MultipartFile laUploadFile;
	private MultipartFile [] laFiles;
	private List<LandAcquisition> laFilesList,report1List,report2List;
	private List<LandAcquisition> privateIRAList;
	private List<LandAcquisition> privateLVList;
	private List<LandAcquisition> privateLAList;
	private List<LandAcquisition> railwayList;
	private List<LandAcquisition> forestList;
	private List<LandAcquisition> govList;
	private String[] laFileNames,laDocumentFileNames,laDocumentNames,la_file_typess;
	
	private Double agriculture_tree_nos,forest_tree_nos;
	
}
