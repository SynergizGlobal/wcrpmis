package com.wcr.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wcr.dms.entity.SendCorrespondenceLetter;

import java.util.List;

public interface SendCorrespondenceLetterRepository extends JpaRepository<SendCorrespondenceLetter, Long> {

    @Query("from SendCorrespondenceLetter l where l.correspondenceLetter.correspondenceId=:id")
    List<SendCorrespondenceLetter> findBySendCorrespondenceLetter(@Param("id") Long id);
    
    @Query("SELECT s FROM SendCorrespondenceLetter s WHERE s.correspondenceLetter.correspondenceId = :correspondenceId")
    List<SendCorrespondenceLetter> findAllByCorrespondenceId(@Param("correspondenceId") Long correspondenceId);

}
