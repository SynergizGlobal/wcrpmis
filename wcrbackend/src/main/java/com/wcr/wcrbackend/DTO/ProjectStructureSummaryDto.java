package com.wcr.wcrbackend.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectStructureSummaryDto {
    private String projectId;
    private List<StructureSummaryDto> structureTypes = new ArrayList<>();

    public ProjectStructureSummaryDto(String projectId) {
        this.projectId = projectId;
    }

    public void addType(String type, Long count) {
        structureTypes.add(new StructureSummaryDto(type, count));
    }
}

