package com.wcr.wcrbackend.repo;

import java.util.List;
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
	
	
	 @Query("SELECT c FROM Contractor c WHERE " +
	           "(:searchStr IS NULL OR " +
	           "c.contractorId LIKE %:searchStr% OR " +
	           "c.contractorName LIKE %:searchStr% OR " +
	           "c.panNumber LIKE %:searchStr% OR " +
	           "c.specilaization LIKE %:searchStr% OR " +
	           "c.address LIKE %:searchStr% OR " +
	           "c.primaryContact LIKE %:searchStr% OR " +
	           "c.phoneNumber LIKE %:searchStr% OR " +
	           "c.email LIKE %:searchStr%)")
	    List<Contractor> searchContractors(@Param("searchStr") String searchStr);
}
	

