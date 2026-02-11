package com.wcr.dms.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;

import com.wcr.dms.entity.SubFolder;
import com.wcr.dms.repository.SubFolderRepository;
import com.wcr.dms.service.SubFolderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubFolderServiceImpl  implements SubFolderService{

	private final SubFolderRepository subFolderRepository;

	@Override
	public List<SubFolder> getSubFoldersByFolderId(Long folderId) {
		// TODO Auto-generated method stub
		return subFolderRepository.findByFolderId(folderId);
	}

	@Override
	public List<SubFolder> getsubfolderGridByFolderId(Long folderId, String userId, List<String> projects, List<String> contracts) {
		// TODO Auto-generated method stub
		return subFolderRepository.getsubfolderGridByFolderId(folderId, userId, projects, contracts);
	}

	@Override
	public List<SubFolder> getAllSubfolderGridByFolderId(Long folderId, List<String> projects, List<String> contracts) {
		// TODO Auto-generated method stub
		return subFolderRepository.getAllSubfolderGridByFolderId(folderId, projects, contracts);
	}
    
	
}
