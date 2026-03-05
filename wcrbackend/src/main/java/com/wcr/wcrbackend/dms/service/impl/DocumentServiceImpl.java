package com.wcr.wcrbackend.dms.service.impl;

import com.wcr.wcrbackend.dms.dto.DocumentDTO;
import com.wcr.wcrbackend.dms.dto.DocumentGridDTO;
import com.wcr.wcrbackend.dms.entity.Document;
import com.wcr.wcrbackend.dms.entity.DocumentFile;
import com.wcr.wcrbackend.dms.entity.DocumentRevision;
import com.wcr.wcrbackend.dms.repository.DocumentFileRepository;
import com.wcr.wcrbackend.dms.repository.DocumentRepository;
import com.wcr.wcrbackend.dms.repository.DocumentRevisionRepository;
import com.wcr.wcrbackend.dms.service.DocumentService;

import com.wcr.wcrbackend.dms.entity.Folder;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    @Qualifier("dmsDocumentRepository")
    private final DocumentRepository documentRepository;
    private final DocumentFileRepository documentFileRepository;
    private final DocumentRevisionRepository documentRevisionRepository;

    private final String uploadDir = "uploads/";

    @Override
    public List<DocumentGridDTO> getAllDocuments() {

        List<Document> docs = documentRepository.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return docs.stream().map(d -> {

            DocumentGridDTO dto = new DocumentGridDTO();

            dto.setId(String.valueOf(d.getId()));
            dto.setFileName(d.getFileName());
            dto.setFileNumber(d.getFileNumber());

            dto.setRevisionNumber(
                d.getRevisionNo() != null ? d.getRevisionNo() : ""
            );

            dto.setRevisionDate(
                d.getRevisionDate() != null ? d.getRevisionDate().toString() : ""
            );

            dto.setRevisionDate(
                d.getRevisionDate() != null ? d.getRevisionDate().toString() : ""
            );

            dto.setProjectName(d.getProjectName());
            dto.setContractName(d.getContractName());

            dto.setPath(buildFolderPath(d.getFolder()));

            dto.setDepartment(
                d.getDepartment() != null ? d.getDepartment().getName() : ""
            );

            dto.setStatus(
                d.getCurrentStatus() != null ? d.getCurrentStatus().getName() : ""
            );

            dto.setCreatedBy(d.getCreatedBy());

            dto.setDateUploaded(
                d.getCreatedAt() != null ? d.getCreatedAt().format(formatter) : ""
            );

            dto.setCreatedAt(
                d.getCreatedAt() != null ? d.getCreatedAt().format(formatter) : ""
            );

            dto.setUpdatedAt(
                d.getUpdatedAt() != null ? d.getUpdatedAt().format(formatter) : ""
            );

            if (d.getDocumentFiles() != null && !d.getDocumentFiles().isEmpty()) {
                dto.setFileType(d.getDocumentFiles().get(0).getFileType());
            }

            return dto;

        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveDocument(DocumentDTO dto, MultipartFile file) throws Exception {

        Document document = new Document();

        document.setFileName(dto.getFileName());
        document.setFileNumber(dto.getFileNumber());
        document.setRevisionNo(dto.getRevisionNo());
        document.setRevisionDate(dto.getRevisionDate());

        document.setProjectName(dto.getProjectName());
        document.setContractName(dto.getContractName());

        document.setCreatedBy("SYSTEM");
        document.setCreatedAt(LocalDateTime.now());

        document = documentRepository.save(document);

        if (file != null && !file.isEmpty()) {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            DocumentFile docFile = new DocumentFile();

            docFile.setFileName(fileName);
            docFile.setFileType(file.getContentType());
            docFile.setFilePath(filePath.toString());
            docFile.setDocument(document);

            documentFileRepository.save(docFile);
        }

        DocumentRevision revision = new DocumentRevision();

        revision.setRevisionNo(dto.getRevisionNo());
        revision.setRevisionDate(dto.getRevisionDate());
        revision.setDocument(document);

        documentRevisionRepository.save(revision);
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(Long id) throws Exception {

        DocumentFile file = documentFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Path path = Paths.get(file.getFilePath());

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

    private String buildFolderPath(Folder folder) {

    if (folder == null) return "";

    StringBuilder path = new StringBuilder();

    while (folder != null) {
        path.insert(0, folder.getName() + "/");
        folder = folder.getParent();
    }

    return path.toString();
}
}