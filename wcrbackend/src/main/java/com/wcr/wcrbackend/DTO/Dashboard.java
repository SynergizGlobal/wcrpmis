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
public class Dashboard {
	private List<Dashboard> tableauSubList,tableauSubListLevel2;
	private String tableauUrl,tableauDashboardId,tableauDashboardName,imagePath,priority,tableauTrustedToken,work_id_fk;
	
}
