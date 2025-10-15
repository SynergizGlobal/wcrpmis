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
public class RandRMain {
private String rr_id, work_id,work_id_fk,work_short_name,work_name, identification_no,bses_email,project_id, map_sr_no, location_name, sub_location_name, phase, structure_id, type_of_structure_roof, type_of_structure_wall, type_of_structure_floor, carpet_area, year_of_construction, name_of_the_owner, type_of_use, document_type, document_no, physical_verification, verification_by, approval_by_committee, rr_approval_status_by_mrvc, estimation_amount, estimate_approval_date, letter_to_mmrda, estimates_by_mmrda, payment_to_mmrda, alternate_housing_allotment,
relocation, encroachment_removal, boundary_wall_status, boundary_wall_doc, handed_over_to_execution, occupier_name_during_verification,stage,id, rr_id_fk, name_of_activity, 
year_of_establishment, monthly_turnover_amount, monthly_turnover_amount_units, number_of_employees, remarks,
 employee_name, employee_age, employee_gender, employee_literacy, employee_travel_time, employee_salary, employee_salary_units, employee_nature_of_work,
 occupancy_status, gender, tenure_status, caste, mother_tongue, type_of_family, family_size, employee_attended,family_income_amount_units,
number_of_married_couple, family_income_amount, vulnerable_category,project_id_fk,project_name,structure,work_code,
residential_name, residential_relation_with_head, residential_age, rr_location_fk,maritua_status, rr_sub_location,rr_tenure_status,residential_gender, 
residential_maritual_status, residential_education, residential_employment, residential_salary, unit, value,com_carpet_area,com_remarks,estimated_by_mmrda_amount_units,estimation_amount_units,
residential_salary_units,created_by_user_id_fk,modified_by,modified_date,user_id,user_name,designation,user_type_fk,user_role_code,executive_user_id_fk,rr_data_id, uploaded_file, status, uploaded_by_user_id_fk, uploaded_on,mail_body_header,
 hod, mrvc_responsible_person, bses_agency_name, agency_responsible_person,rrbses_id,
 rr_agency_id_fk, date_of_appointment, name_of_representative, phone_no,committee_name,
 contact_number, email_id, submission_date_report_ca, actual_submission_date_bses_report_to_mrvc,res_designation,res_user_name,
 approval_by_mrvc_responsible_person, report_submission_date_to_mrvc, approval_date_by_mrvc,res_user_id,planned_date_of_completion,chainage,latitude,longitude,searchStr,attachment_file,agency_id;

private List<RandRMain> residentialList,rrBSESLIst,commercialList,comList,comFamList,resList,resFamList,report1List,report2List;
private MultipartFile RandRFile;
private MultipartFile [] rragencyFiles;
private String [] values,genders,ids,name_of_representatives,committee_names,date_of_appointments,phone_nos,email_ids, rr_id_fks, employee_names, employee_ages, employee_genders, employee_literacys,employee_salary_unitsss, employee_travel_times
, employee_salarys, employee_salary_unitss, employee_nature_of_works,
residential_names, residential_relation_with_heads, residential_ages, residential_genders, residential_maritual_statuss,employee_attendeds, residential_educations, residential_employments
, residential_salarys, residential_salary_unitss,rrDocumentFileNames;

}
