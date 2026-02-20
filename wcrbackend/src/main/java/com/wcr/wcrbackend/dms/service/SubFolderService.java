package com.wcr.wcrbackend.dms.service;

import java.util.List;

import com.wcr.wcrbackend.dms.entity.SubFolder;

public interface SubFolderService {

    List<SubFolder> getSubFoldersByFolderId(Long folderId);

    SubFolder createSubFolder(Long folderId, String name);

    void deleteSubFolder(Long subFolderId);

    List<SubFolder> getsubfolderGridByFolderId(Long folderId, String userId, List<String> projects, List<String> contracts);

    List<SubFolder> getAllSubfolderGridByFolderId(Long folderId, List<String> projects, List<String> contracts);
}
