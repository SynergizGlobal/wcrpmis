package com.wcr.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wcr.dms.entity.DocumentFile;


public interface DocumentFileRepository extends JpaRepository<DocumentFile, Long> {

}
