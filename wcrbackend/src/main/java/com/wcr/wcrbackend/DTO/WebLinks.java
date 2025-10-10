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
public class WebLinks {
	private String id,name,link,priority,removableWebLinkIds;
	
	private String[] ids,names,links,prioritys;
}
