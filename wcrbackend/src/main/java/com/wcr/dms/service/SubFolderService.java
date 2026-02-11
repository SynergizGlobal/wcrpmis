package com.wcr.dms.service;

import java.util.List;

import com.wcr.dms.entity.SubFolder;


public interface SubFolderService {

	List<SubFolder> getSubFoldersByFolderId(Long folderId);

	List<SubFolder> getsubfolderGridByFolderId(Long folderId, String userId, List<String> projects, List<String> contracts);

	List<SubFolder> getAllSubfolderGridByFolderId(Long folderId, List<String> projects, List<String> contracts);

}
