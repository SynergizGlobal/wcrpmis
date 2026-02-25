package com.wcr.wcrbackend.DTO;

import java.math.BigDecimal;
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
public class Project {
	private String project_id,project_code,project_name,plan_head_number,pink_book_item_number,remarks,project_description,project_status,attachment
	,sanctioned_estimated_cost,sanctioned_year_fk,sanctioned_completion_cost,year_of_completion,projected_completion_year,latest_revised_cost,
	completion_cost,work_short_name,completion_date,benefits,galleryFileNames,financial_year_fk,pb_item_no,project_pinkbook_id,user_id,created_by_user_id_fk,designation,user_name;
	
	private String id,file_name,division_id,section_id,division_name,upload_kmz_file,section_name,project_type_id_fk,date_of_sanction,pb_item_number,project_id_fk,actual_completion_cost,actual_completion_date,entry_date,estimated_completion_cost,revised_completion_date,project_type_id,work_type,project_type_name,railway_zone,railway_id,sanctioned_year,sanctioned_amount,sanctioned_commissioning_date,
	revised_target_date,latest_sanctioned_cost,division,sections,
	
	created_date,created_by,railway,commissioned_length,length,project_file_type_fk,project_file_type,project_file_id,financial_progress,physical_progress,
	uploaded_by_user_id_fk,chainages,latitude,longitude,status,srno,uploaded_file,project_data_id,	ongoing_projects,	total_length,	total_earthwork,	completed_track,	completed_major_bridges,	total_major_bridges,	completed_minor_bridges,total_minor_bridges,	completed_rob,	total_rob,	completed_rub,	total_rub , proposed_length

	;	
	
	 private String structure_details;
	 private BigDecimal from_chainage;
	 private BigDecimal to_chainage;
	
	private MultipartFile[] projectGalleryFiles,projectFiles;
	private List<Project> projectFilesList,projectGalleryFilesList,projectPinkBooks,projectGallery,projectDocs;
	private String[] projectFileNames,attachemnts,project_file_types,project_file_ids,projectGalleryFileNames,created_dates;
	private String[] financial_years,pink_book_item_numbers,railways;
	private String[] completion_dates,estimated_completion_costs,revised_completion_dates;
	
	private MultipartFile ProjectChainagesFile;
	
}
