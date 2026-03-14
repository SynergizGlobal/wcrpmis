package com.wcr.wcrbackend.dms.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcr.wcrbackend.dms.dto.DocumentDTO;
import com.wcr.wcrbackend.dms.dto.DocumentGridDTO;
import com.wcr.wcrbackend.dms.dto.SendDocumentDTO;
import com.wcr.wcrbackend.dms.entity.DocumentRevision;
import com.wcr.wcrbackend.dms.entity.SendDocument;
import com.wcr.wcrbackend.dms.repository.DocumentRepository;
import com.wcr.wcrbackend.dms.repository.SendDocumentRepository;
import com.wcr.wcrbackend.dms.service.DocumentService;
import com.wcr.wcrbackend.dms.service.SendDocumentService;

import com.wcr.wcrbackend.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final ObjectMapper objectMapper; 
    private final SendDocumentService sendDocumentService;
    private final SendDocumentRepository sendDocumentRepository;

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

        // ObjectMapper mapper = new ObjectMapper();
        DocumentDTO dto = objectMapper.readValue(dtoJson, DocumentDTO.class);

        // @RequestPart("dto") DocumentDTO dto,
        documentService.saveDocument(dto, file);

        return ResponseEntity.ok("Document uploaded successfully");
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) throws Exception {
        return documentService.downloadDocument(id);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendDocument(@RequestBody SendDocumentDTO dto) {

        documentService.sendDocument(dto);

        return ResponseEntity.ok("Document sent successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDocument(
            @PathVariable Long id,
            @RequestParam("dto") String dtoJson,
            @RequestParam(value="file", required=false) MultipartFile file
    ) throws Exception {

        DocumentDTO dto = objectMapper.readValue(dtoJson, DocumentDTO.class);

        documentService.updateDocument(id, dto, file);

        return ResponseEntity.ok("Document updated successfully");
    }

    @PostMapping("/send-document")
    public ResponseEntity<String> saveOrSendDocument(
            @RequestBody SendDocumentDTO dto,
            HttpSession session,
            HttpServletRequest request) throws IOException {

        User user = (User) session.getAttribute("user");

        String baseUrl = request.getScheme() + "://"
                + request.getServerName()
                + ":" + request.getServerPort()
                + request.getContextPath();

        documentService.saveOrSendDocument(dto, user.getUserId(), baseUrl);

        return ResponseEntity.ok("Success");
    }

    @GetMapping("/drafts")
    public List<SendDocument> getDrafts(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return List.of();
        }

        return sendDocumentRepository.findByCreatedByAndStatus(
                user.getUserName(),
                "Draft"
        );
    }

    @PutMapping("/not-required/{id}")
    public ResponseEntity<String> markNotRequired(@PathVariable Long id) {

        documentService.markNotRequired(id);

        return ResponseEntity.ok("Document marked as not required");
    }

    @GetMapping("/versions/{fileNumber}")
    public ResponseEntity<List<DocumentRevision>> getVersions(@PathVariable String fileNumber) {

        return ResponseEntity.ok(documentService.getDocumentVersions(fileNumber));
    }
}