package com.wcr.wcrbackend.dms.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.wcr.wcrbackend.dms.entity.CorrespondenceLetter;
import com.wcr.wcrbackend.dms.entity.CorrespondenceReference;
import com.wcr.wcrbackend.dms.entity.CorrespondenceReferenceId;
import com.wcr.wcrbackend.dms.entity.ReferenceLetter;

import java.util.List;

public interface CorrespondenceReferenceRepository extends JpaRepository<CorrespondenceReference, CorrespondenceReferenceId> {

	@Query("SELECT cr.referenceLetter FROM CorrespondenceReference cr " +
            "WHERE cr.correspondenceLetter.correspondenceId = :correspondenceId")
    List<ReferenceLetter> findReferenceLettersByCorrespondenceId(@Param("correspondenceId") Long correspondenceId);

	@Modifying
	@Transactional
    void deleteByCorrespondenceLetter(CorrespondenceLetter letter);

}
