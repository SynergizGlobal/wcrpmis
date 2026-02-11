package com.wcr.dms.repository;

 
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.wcr.dms.entity.Status;


@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
   
	boolean existsByName(String name);
	Optional<Status> findByName(String currentStatus);
}
