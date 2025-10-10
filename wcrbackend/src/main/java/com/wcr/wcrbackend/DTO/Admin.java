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
public class Admin {
	
	private String admin_form_id, form_name, url, priority, soft_delete_status_fk;
	private List<Admin> formsSubMenu;
}