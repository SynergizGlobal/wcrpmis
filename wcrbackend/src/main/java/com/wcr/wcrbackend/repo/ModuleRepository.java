package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ModuleRepository implements IModuleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getModules() {
    	String sql = "SELECT module_name FROM module ORDER BY module_name";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
