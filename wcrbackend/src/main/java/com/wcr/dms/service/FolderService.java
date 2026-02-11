package com.wcr.dms.service;

import java.util.List;

import com.wcr.dms.dto.FolderDTO;



public interface FolderService {


    public List<FolderDTO> getAllFolders();

    public FolderDTO getFolderById(Long id); 

    public FolderDTO createFolder(FolderDTO folderDTO);
    
    public FolderDTO updateFolder(Long id, FolderDTO folderDTO);
    
  
    
    public void deleteFolder(Long folderId);
    
    public void deleteSubFolder(Long subFolderId);

	public FolderDTO getFolderByName(String name);

	public List<FolderDTO> getAllFoldersByProjectsAndContracts(List<String> project, List<String> contract, String userId);

	public List<FolderDTO> getAllFoldersByProjectsAndContracts(List<String> projects, List<String> contracts);
}
