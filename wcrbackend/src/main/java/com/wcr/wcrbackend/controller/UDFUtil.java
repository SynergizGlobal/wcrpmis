package com.wcr.wcrbackend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.wcr.wcrbackend.DTO.UDFDataDTO;
import com.wcr.wcrbackend.DTO.UDFDataDTO1;

public class UDFUtil {

	public static Map<String, String> getData(List<UDFDataDTO> list) {
		Map<String, String> map = new HashMap<String, String>();

		for (UDFDataDTO dataDTO : list) {
//			System.out.println("dataDTO : "+dataDTO);
			StringBuffer buffer = new StringBuffer(dataDTO.getUdf_type_id());
			buffer.append("_").append(dataDTO.getFk_id()).append("_").append(dataDTO.getProj_id());

			map.put(buffer.toString(), dataDTO.getUdf_text());

		}

		return map;
	}

	public static List<UDFDataDTO1> convertData(Map<String, String> map) {
		List<UDFDataDTO1> dataDTO1s = new ArrayList<UDFDataDTO1>();

		for (Map.Entry<String, String> entry : map.entrySet()) {

			String key = entry.getKey();

			if (key.startsWith("813_")) {

				UDFDataDTO1 dataDTO1 = new UDFDataDTO1();

				dataDTO1.setFk_id("");
				dataDTO1.setProj_id("");

				String[] keyParts = key.split("_");
				dataDTO1.setFk_id(keyParts[1]);
				dataDTO1.setProj_id(keyParts[2]);
				dataDTO1.setComponent(entry.getValue()); // 813
				
				
				String key2 = key.replaceFirst("813_", "814_");
				String key3 = key.replaceFirst("813_", "815_");
				String key4 = key.replaceFirst("813_", "816_");
				
				String key5 = key.replaceFirst("813_", "839_");
				String key6 = key.replaceFirst("813_", "840_");
				String key7 = key.replaceFirst("813_", "841_");
				
				String componentId = map.getOrDefault(key2, ""); // 814
				String structure = map.getOrDefault(key3, ""); // 815
				String structureTypeFk = map.getOrDefault(key4, ""); // 816
				String weightages = map.getOrDefault(key5, ""); // 839
				String unit = map.getOrDefault(key6, ""); // 840
				String scope = map.getOrDefault(key7, "");
				
				
				dataDTO1.setStructure(structure);
				dataDTO1.setComponent_id(componentId);
				
				if(structureTypeFk.contains("Kasara")) {
					dataDTO1.setStructure_type_fk("Kasara");
				} else if(structureTypeFk.contains("Neral")) {
					dataDTO1.setStructure_type_fk("Neral");
				} else {
					dataDTO1.setStructure_type_fk(structureTypeFk);
				}
				
				
				dataDTO1.setWeightages(weightages);
				dataDTO1.setUnit(unit);
				dataDTO1.setScope(scope);
				
				dataDTO1s.add(dataDTO1);

			}
			

		}
		
		return dataDTO1s;

	}
	
	public static Map<String, UDFDataDTO1> convertToMap(List<UDFDataDTO1> list) {
	    return list.stream()
	               .collect(Collectors.toMap(
	                   UDFDataDTO1::getFk_id,
	                   e -> e
	               ));
	}

}
