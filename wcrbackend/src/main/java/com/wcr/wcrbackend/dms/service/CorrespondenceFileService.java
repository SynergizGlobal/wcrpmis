package com.wcr.wcrbackend.dms.service;

import java.util.List;

import com.wcr.wcrbackend.dms.dto.CorrespondenceFileDTO;
import com.wcr.wcrbackend.dms.dto.CorrespondenceFolderFileDTO;


public interface CorrespondenceFileService {
	
	

	 List<CorrespondenceFolderFileDTO> getFiles(
	            List<String> projectNames,
	            List<String> contractNames,
	            String type,
	            String action,
	            String baseUrl,
	            String userId,
	            boolean isAdmin);

	 List<CorrespondenceFolderFileDTO> getFilesForAdminIncoming(List<String> projectNames, List<String> contractNames,
			String type, String baseUrl);

	 List<CorrespondenceFolderFileDTO> getFilesForAdminOutgoing(List<String> projectNames, List<String> contractNames,
			String type, String baseUrl);

	 List<CorrespondenceFolderFileDTO> getFilesIncoming(List<String> projectNames, List<String> contractNames,
			String type, String baseUrl, String userId);

	 List<CorrespondenceFolderFileDTO> getFilesOutgoing(List<String> projectNames, List<String> contractNames,
			String type, String baseUrl, String userId);
	 
	 List<CorrespondenceFileDTO> getFilesByCorrespondenceId(Long correspondenceId);
}
