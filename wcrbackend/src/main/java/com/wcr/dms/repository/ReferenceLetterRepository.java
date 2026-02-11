package com.wcr.dms.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import com.wcr.dms.entity.ReferenceLetter;


public interface ReferenceLetterRepository extends JpaRepository<ReferenceLetter, Long> {

	List<ReferenceLetter>  findDistinctByRefLettersContainingIgnoreCase(String query);
	
}
