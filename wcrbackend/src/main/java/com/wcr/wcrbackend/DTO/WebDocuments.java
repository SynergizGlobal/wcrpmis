package com.wcr.wcrbackend.DTO;

import java.util.List;

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
public class WebDocuments {
	private String id,title,file_name,category_id_fk,upload_date,uploaded_by,type_fk,category,type,web_document_id,category_id,date_of_issue,date_of_issue_ddmmmyy,modified_type;
	
	private List<WebDocuments> webDocumentsList;
}
