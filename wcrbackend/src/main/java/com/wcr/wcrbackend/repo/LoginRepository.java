package com.wcr.wcrbackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.entity.User;

@Repository
public interface LoginRepository extends JpaRepository<User, String>{

}
