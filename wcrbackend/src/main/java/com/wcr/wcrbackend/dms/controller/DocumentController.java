package com.wcr.wcrbackend.dms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcr.wcrbackend.dms.dto.DocumentDTO;
import com.wcr.wcrbackend.dms.dto.DocumentGridDTO;
import com.wcr.wcrbackend.dms.service.DocumentService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/list")
    public ResponseEntity<List<DocumentGridDTO>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("dto") String dtoJson,
            // @RequestPart("dto") DocumentDTO dto,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        DocumentDTO dto = mapper.readValue(dtoJson, DocumentDTO.class);

        // @RequestPart("dto") DocumentDTO dto,
        documentService.saveDocument(dto, file);

        return ResponseEntity.ok("Document uploaded successfully");
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) throws Exception {
        return documentService.downloadDocument(id);
    }
}