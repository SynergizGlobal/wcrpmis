package com.wcr.wcrbackend.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao implements IUserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public String getRoleCode(String userRoleNameFk) {
		String sql = "SELECT user_role_code FROM user_role WHERE user_role_name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userRoleNameFk}, String.class);
	}

}
