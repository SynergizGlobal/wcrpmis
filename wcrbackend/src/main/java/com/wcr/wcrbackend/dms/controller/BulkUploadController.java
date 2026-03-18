package com.wcr.wcrbackend.dms.controller;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

import com.wcr.wcrbackend.dms.dto.DocumentDTO;
import com.wcr.wcrbackend.dms.service.DocumentService;

import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/api/bulk")
@RequiredArgsConstructor
public class BulkUploadController {

    private final DocumentService documentService;

    private static Map<Long, List<DocumentDTO>> bulkStore = new HashMap<>();
    private static long counter = 1;

  
    @PostMapping("/upload-excel")
    public ResponseEntity<List<DocumentDTO>> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {

        List<DocumentDTO> docs = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                DocumentDTO dto = new DocumentDTO();

                dto.setFileName(getCellValue(row.getCell(0)));
                dto.setFileNumber(getCellValue(row.getCell(1)));
                dto.setRevisionNo(getCellValue(row.getCell(2)));
                String dateStr = getCellValue(row.getCell(3));

                if (dateStr != null && !dateStr.isBlank()) {
                    dto.setRevisionDate(LocalDate.parse(dateStr)); // expects yyyy-MM-dd
                }

                dto.setProjectName(getCellValue(row.getCell(4)));
                dto.setContractName(getCellValue(row.getCell(5)));

                dto.setDepartment(getCellValue(row.getCell(6)));
                dto.setCurrentStatus(getCellValue(row.getCell(7)));

                dto.setPath(getCellValue(row.getCell(8)));

                docs.add(dto);
            }
        }

        return ResponseEntity.ok(docs);
    }

    
    @PostMapping("/save")
    public ResponseEntity<Long> saveMetadata(@RequestBody List<DocumentDTO> docs) {

        long id = counter++;
        bulkStore.put(id, docs);

        return ResponseEntity.ok(id);
    }

    
    @PostMapping("/upload-zip/{id}")
    public ResponseEntity<String> uploadZip(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile zipFile) throws Exception {

        documentService.processBulkZip(id, zipFile, bulkStore);

        return ResponseEntity.ok("Bulk upload successful");
    }

    private String getCellValue(Cell cell) {

        if (cell == null) return "";

        switch (cell.getCellType()) {

            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue()
                            .toLocalDate()
                            .toString(); // yyyy-MM-dd
                }
                return String.valueOf((long) cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            default:
                return "";
        }
    }
}