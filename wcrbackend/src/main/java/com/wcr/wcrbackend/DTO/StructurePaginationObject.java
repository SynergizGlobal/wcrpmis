package com.wcr.wcrbackend.DTO;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StructurePaginationObject {
	private int iTotalDisplayRecords; 
	private int iTotalRecords;
	private List<Structure> aaData;
}