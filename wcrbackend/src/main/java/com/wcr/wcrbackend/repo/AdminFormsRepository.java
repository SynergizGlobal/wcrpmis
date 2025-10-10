package com.wcr.wcrbackend.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Admin;
import com.wcr.wcrbackend.DTO.Forms;

@Repository
public class AdminFormsRepository implements IAdminFormsRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Admin> getAdminForms() {
		String sql = """
			    SELECT 
			        form_id AS admin_form_id,
			        form_name,
			        web_form_url AS url,
			        ISNULL(priority, 0) AS priority,
			        parent_form_id_sr_fk,
			        soft_delete_status_fk
			    FROM form
			    WHERE parent_form_id_sr_fk = form_id
			      AND soft_delete_status_fk = ?
			      AND url_type = ?
			    ORDER BY priority ASC
			    """;
		return jdbcTemplate.query(sql, new Object[] { "Active", "Admin" },
				new RowMapper<Admin>() {
					@Override
					public Admin mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						Admin obj = new Admin();
						obj.setAdmin_form_id(resultSet.getString("admin_form_id"));
						obj.setForm_name(resultSet.getString("form_name"));
						obj.setUrl(resultSet.getString("url"));
						//obj.setMobileFormUrl(CommonConstants.CONTEXT_PATH+"/"+resultSet.getString("mobile_form_url"));
						obj.setPriority(resultSet.getString("priority"));
						obj.setSoft_delete_status_fk(resultSet.getString("soft_delete_status_fk"));
						
						String parentId = resultSet.getString("parent_form_id_sr_fk");
						
						List<Admin> subList = getAdminFormsSubList("web",parentId);
						if(!StringUtils.isEmpty(subList)) {
							obj.setFormsSubMenu(subList);
						}
						return obj;
					}
				});
	}
	private List<Admin> getAdminFormsSubList(String web, String parentId) {
		String sql = """
			    SELECT DISTINCT 
			        form_id AS admin_form_id,
			        form_name,
			        web_form_url AS url,
			        ISNULL(priority, 0) AS priority,
			        parent_form_id_sr_fk,
			        soft_delete_status_fk
			    FROM form f
			    WHERE parent_form_id_sr_fk <> f.form_id
			      AND parent_form_id_sr_fk = ?
			      AND f.soft_delete_status_fk = ?
			    """;
		return jdbcTemplate.query(sql, new Object[] { parentId, "Active" },
				new RowMapper<Admin>() {
					@Override
					public Admin mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						Admin obj = new Admin();
						obj = new Admin();
						obj.setAdmin_form_id(resultSet.getString("admin_form_id"));
						obj.setForm_name(resultSet.getString("form_name"));
						obj.setUrl(resultSet.getString("url"));

						obj.setPriority(resultSet.getString("priority"));
						obj.setSoft_delete_status_fk(resultSet.getString("soft_delete_status_fk"));
						return obj;
					}
				});
	}
}
