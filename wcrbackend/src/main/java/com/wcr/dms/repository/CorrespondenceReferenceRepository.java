package com.wcr.dms.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.wcr.dms.entity.CorrespondenceReference;
import com.wcr.dms.entity.CorrespondenceReferenceId;
import com.wcr.dms.entity.ReferenceLetter;
import java.util.List;

public interface CorrespondenceReferenceRepository extends JpaRepository<CorrespondenceReference, CorrespondenceReferenceId> {

	@Query("SELECT cr.referenceLetter FROM CorrespondenceReference cr " +
            "WHERE cr.correspondenceLetter.correspondenceId = :correspondenceId")
    List<ReferenceLetter> findReferenceLettersByCorrespondenceId(@Param("correspondenceId") Long correspondenceId);

}
