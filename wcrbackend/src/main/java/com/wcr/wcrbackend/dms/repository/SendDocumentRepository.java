package com.wcr.wcrbackend.dms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wcr.wcrbackend.dms.entity.SendDocument;

public interface SendDocumentRepository extends JpaRepository<SendDocument, Long>{

    Page<SendDocument> findByCreatedByAndStatus(String createdBy, String status, Pageable pageable);

	long countByCreatedByAndStatus(String createdBy, String status);

}
