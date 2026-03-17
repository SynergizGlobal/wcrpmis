package com.wcr.wcrbackend.dms.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wcr.wcrbackend.dms.dto.FolderDTO;
import com.wcr.wcrbackend.dms.dto.SubFolderDTO;
import com.wcr.wcrbackend.dms.entity.Folder;
import com.wcr.wcrbackend.dms.entity.SubFolder;
import com.wcr.wcrbackend.dms.repository.FolderRepository;
import com.wcr.wcrbackend.dms.repository.SubFolderRepository;
import com.wcr.wcrbackend.dms.service.FolderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final SubFolderRepository subFolderRepository;

    // read

    @Override
    @Transactional(readOnly = true)
    public List<FolderDTO> getAllFolders() {
        // Only return root folders (parentId is null)
        return folderRepository.findByParentIdIsNull()
                .stream()
                .map(this::toDTOWithSubFolders)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FolderDTO getFolderById(Long id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
        return toDTOWithSubFolders(folder);
    }

    @Override
    @Transactional(readOnly = true)
    public FolderDTO getFolderByName(String name) {
        return folderRepository.findByNameWithSubFolders(name)
                .map(this::toDTOWithSubFolders)
                .orElse(null);
    }

    // WRITE 

    @Override
    public FolderDTO createFolder(FolderDTO folderDTO) {
        Folder folder = new Folder();
        folder.setName(folderDTO.getName());
        folder.setParentId(folderDTO.getParentId());
        Folder saved = folderRepository.save(folder);
        return toDTO(saved);
    }

    @Override
    public FolderDTO updateFolder(Long id, FolderDTO folderDTO) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        folder.setName(folderDTO.getName());

        if (folderDTO.getSubFolders() != null) {
            Map<Long, SubFolder> existing =
                    folder.getSubFolders().stream()
                            .collect(Collectors.toMap(SubFolder::getId, sf -> sf));

            for (SubFolderDTO sfDto : folderDTO.getSubFolders()) {
                if (sfDto.getId() != null && existing.containsKey(sfDto.getId())) {
                    existing.get(sfDto.getId()).setName(sfDto.getName());
                    existing.remove(sfDto.getId());
                } else {
                    folder.getSubFolders()
                          .add(new SubFolder(sfDto.getName(), folder));
                }
            }
        }

        return toDTOWithSubFolders(folderRepository.save(folder));
    }

    @Override
    public void deleteFolder(Long folderId) {
        folderRepository.deleteById(folderId);
    }

    @Override
    public void deleteSubFolder(Long subFolderId) {
        subFolderRepository.deleteById(subFolderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FolderDTO> getAllFoldersByProjectsAndContracts(
            List<String> project, List<String> contract, String userId) {

        return folderRepository
                .getAllFoldersByProjectsAndContracts(project, contract, userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FolderDTO> getAllFoldersByProjectsAndContracts(
            List<String> projects, List<String> contracts) {

        return folderRepository
                .getAllFoldersByProjectsAndContractsWithSubFolders(projects, contracts)
                .stream()
                .map(this::toDTOWithSubFolders)
                .toList();
    }

    private FolderDTO toDTO(Folder folder) {
        return new FolderDTO(folder.getId(), folder.getName());
    }

    private FolderDTO toDTOWithSubFolders(Folder folder) {
        List<SubFolderDTO> subFolders = folder.getSubFolders() == null
                ? List.of()
                : folder.getSubFolders().stream()
                        .map(sf -> new SubFolderDTO(sf.getId(), sf.getName()))
                        .toList();

        return new FolderDTO(folder.getId(), folder.getName(), subFolders);
    }

    @Override
    public List<Folder> getFolderTree() {
        return folderRepository.findByParentIdIsNull();
    }

    @Override
    public void saveFolder(Folder folder) {
        folderRepository.save(folder);
    }

    @Override
    public List<Folder> searchFolders(String name) {
        return folderRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<FolderDTO> getChildFolders(Long parentId) {
        List<Folder> folders = folderRepository.findByParentId(parentId);
        return folders.stream().map(this::toDTO).toList();
    }
}