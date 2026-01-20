package com.wcr.wcrbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMenuDTO {
    private int menuId;
    private String menuName;
    private String url;
    private boolean hasSubMenu;
    private List<DashboardSubMenuDTO> subMenus; 
}
