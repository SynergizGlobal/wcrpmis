package com.wcr.wcrbackend.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wcr.wcrbackend.dms.entity.DocumentFile;


public interface DocumentFileRepository extends JpaRepository<DocumentFile, Long> {

}
