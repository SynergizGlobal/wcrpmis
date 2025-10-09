package com.wcr.wcrbackend.DTO;

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
public class LoginResponse {
	private String userId;
	private String userName; 
	private String emailId; 
	private String userRoleNameFk;
	private String userTypeFk; 
	private String departmentFk;
}
