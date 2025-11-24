package com.wcr.wcrbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullStructureResponse {
    private String projectId;
    private String projectName;
    private List<StructureTypeDto> structureTypes;

}
