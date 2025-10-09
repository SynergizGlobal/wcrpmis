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
public class Forms {
private String formId;
private String formName;
private String webFormUrl;
private String mobileFormUrl;
private String priority;
private String statusId;
private String parentId;
private String parentName;
private String displayInMobile;
	
private List<Forms> formsSubMenu;
private List<Forms> formsSubMenuLevel2;
}
