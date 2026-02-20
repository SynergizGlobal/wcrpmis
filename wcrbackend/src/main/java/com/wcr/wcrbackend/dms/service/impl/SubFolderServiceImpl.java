package com.wcr.wcrbackend.dms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.dms.entity.Folder;
import com.wcr.wcrbackend.dms.entity.SubFolder;
import com.wcr.wcrbackend.dms.repository.FolderRepository;
import com.wcr.wcrbackend.dms.repository.SubFolderRepository;
import com.wcr.wcrbackend.dms.service.SubFolderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubFolderServiceImpl implements SubFolderService {

    private final SubFolderRepository subFolderRepository;
    private final FolderRepository folderRepository;

    @Override
    public List<SubFolder> getSubFoldersByFolderId(Long folderId) {
        return subFolderRepository.findByFolderId(folderId);
    }

    @Override
    public SubFolder createSubFolder(Long folderId, String name) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found: " + folderId));
        SubFolder subFolder = new SubFolder(name, folder);
        return subFolderRepository.save(subFolder);
    }

    @Override
    public void deleteSubFolder(Long subFolderId) {
        subFolderRepository.deleteById(subFolderId);
    }

    @Override
    public List<SubFolder> getsubfolderGridByFolderId(Long folderId, String userId, List<String> projects, List<String> contracts) {
        return subFolderRepository.getsubfolderGridByFolderId(folderId, userId, projects, contracts);
    }

    @Override
    public List<SubFolder> getAllSubfolderGridByFolderId(Long folderId, List<String> projects, List<String> contracts) {
        return subFolderRepository.getAllSubfolderGridByFolderId(folderId, projects, contracts);
    }
}