package com.wcr.wcrbackend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wcr.wcrbackend.entity.Contractor;

public interface ContractorRepository extends JpaRepository<Contractor, String> {

	Contractor findTopByOrderByContractorIdDesc();

	@Query(value = """
            SELECT c.contractor_short_code
            FROM contractor c
            JOIN [user] u ON LOWER(LTRIM(RTRIM(u.user_name))) = LOWER(LTRIM(RTRIM(c.contractor_name)))
            WHERE u.user_id = :userId
            """,
            nativeQuery = true)
    Optional<String> findContractorShortCodeByUserId(@Param("userId") String userId);
}
	

