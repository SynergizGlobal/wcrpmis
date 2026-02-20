package com.wcr.wcrbackend.dms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.dms.common.CommonUtil;
import com.wcr.wcrbackend.dms.dto.FolderGridDTO;
import com.wcr.wcrbackend.dms.dto.SubFolderDTO;
import com.wcr.wcrbackend.dms.entity.SubFolder;
import com.wcr.wcrbackend.dms.service.SubFolderService;
import com.wcr.wcrbackend.entity.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subfolders")
@RequiredArgsConstructor
public class SubFolderController {

    private final SubFolderService subFolderService;

    @GetMapping("/{folderId}")
    public ResponseEntity<List<SubFolder>> getSubFoldersByFolderId(@PathVariable("folderId") Long folderId) {
        return ResponseEntity.ok(subFolderService.getSubFoldersByFolderId(folderId));
    }

    @PostMapping("/create/{folderId}")
    public ResponseEntity<SubFolder> createSubFolder(
            @PathVariable("folderId") Long folderId,
            @RequestBody SubFolderDTO subFolderDTO) {
        SubFolder created = subFolderService.createSubFolder(folderId, subFolderDTO.getName());
        return ResponseEntity.ok(created);
    }

    
    @DeleteMapping("/{subFolderId}")
    public ResponseEntity<String> deleteSubFolder(@PathVariable("subFolderId") Long subFolderId) {
        subFolderService.deleteSubFolder(subFolderId);
        return ResponseEntity.ok("SubFolder deleted successfully");
    }

    // Grid endpoints (unchanged)
    @PostMapping("/grid/{folderId}")
    public ResponseEntity<List<SubFolder>> getsubfolderGridByFolderId(
            @PathVariable("folderId") Long folderId,
            @RequestBody FolderGridDTO folderGridDto,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (CommonUtil.isITAdminOrSuperUser(user)) {
            return ResponseEntity.ok(subFolderService.getAllSubfolderGridByFolderId(
                    folderId, folderGridDto.getProjects(), folderGridDto.getContracts()));
        }
        return ResponseEntity.ok(subFolderService.getsubfolderGridByFolderId(
                folderId, user.getUserId(), folderGridDto.getProjects(), folderGridDto.getContracts()));
    }
}