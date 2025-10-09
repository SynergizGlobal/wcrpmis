package com.wcr.wcrbackend.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.DTO.Forms;

@Repository
public class FormsRepository implements IFormsRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Forms> getUpdateForms() {
		String sql = """
				SELECT DISTINCT
				    form_id,
				    module_name_fk,
				    form_name,
				    parent_form_id_sr_fk,
				    web_form_url,
				    mobile_form_url,
				    priority,
				    f.soft_delete_status_fk,
				    f.display_in_mobile
				FROM form f
				LEFT JOIN user_module m ON f.module_name_fk = m.module_fk
				WHERE m.executive_id_fk = ?
				  AND m.soft_delete_status = ?
				  AND parent_form_id_sr_fk = f.form_id
				  AND f.soft_delete_status_fk = ?
				  AND (
				      (SELECT COUNT(*) FROM form_access
				       WHERE form_id_fk = f.form_id
				         AND (access_value = ? OR access_value = ? OR access_value = ?)) > 0
				      OR web_form_url IS NULL
				      OR web_form_url = ''
				  )
				  AND f.url_type = 'Update Forms'
				ORDER BY priority ASC
				""";
		return jdbcTemplate.query(sql, new Object[] { "Pratik", "Active", "Active", "Pratik", "IT Admin", "" },
				new RowMapper<Forms>() {
					@Override
					public Forms mapRow(ResultSet rs, int rowNum) throws SQLException {
						Forms form = new Forms();
						form.setFormId(rs.getString("form_id"));
						/// form.setMo(rs.getString("module_name_fk"));
						form.setFormName(rs.getString("form_name"));
						form.setParentId(rs.getString("parent_form_id_sr_fk"));
						form.setWebFormUrl(rs.getString("web_form_url"));
						form.setMobileFormUrl(rs.getString("mobile_form_url"));
						form.setPriority(rs.getString("priority"));
						form.setStatusId(rs.getString("soft_delete_status_fk"));
						form.setDisplayInMobile(rs.getString("display_in_mobile"));
						String parentId = rs.getString("parent_form_id_sr_fk");
						form.setFormsSubMenu(getSubMenuUpdateForms(parentId));
						return form;
					}
				});
	}

	private List<Forms> getSubMenuUpdateForms(String parentId) {
		String qry = """
				SELECT DISTINCT
				    form_id,
				    module_name_fk,
				    form_name,
				    parent_form_id_sr_fk,
				    web_form_url,
				    mobile_form_url,
				    priority,
				    soft_delete_status_fk,
				    f.display_in_mobile
				FROM form f
				WHERE parent_form_id_sr_fk <> f.form_id
				  AND parent_form_id_sr_fk = ?
				  AND f.soft_delete_status_fk = ?
				  AND (
				      (SELECT COUNT(*) FROM form_access
				       WHERE form_id_fk = f.form_id
				         AND (access_value = ? OR access_value = ? OR access_value = ?)) > 0
				      OR web_form_url IS NULL
				      OR web_form_url = ''
				  )
				ORDER BY priority ASC
				""";
		return jdbcTemplate.query(qry, new Object[] { parentId, "Active", "Pratik", "IT Admin", "" },
				new RowMapper<Forms>() {
					@Override
					public Forms mapRow(ResultSet rs, int rowNum) throws SQLException {
						Forms form = new Forms();
						form.setFormId(rs.getString("form_id"));
						/// form.setMo(rs.getString("module_name_fk"));
						form.setFormName(rs.getString("form_name"));
						form.setParentId(rs.getString("parent_form_id_sr_fk"));
						form.setWebFormUrl(rs.getString("web_form_url"));
						form.setMobileFormUrl(rs.getString("mobile_form_url"));
						form.setPriority(rs.getString("priority"));
						form.setStatusId(rs.getString("soft_delete_status_fk"));
						form.setDisplayInMobile(rs.getString("display_in_mobile"));
						String parentIdLevel2 = rs.getString("form_id");
						form.setFormsSubMenuLevel2(getSubMenuLevel2UpdateForms(parentIdLevel2));
						return form;
					}
				});
	}

	private List<Forms> getSubMenuLevel2UpdateForms(String parentIdLevel2) {
		String qry = """
			    SELECT DISTINCT 
			        form_id,
			        module_name_fk,
			        form_name,
			        parent_form_id_sr_fk,
			        web_form_url,
			        mobile_form_url,
			        priority,
			        soft_delete_status_fk,
			        f.display_in_mobile
			    FROM form f
			    WHERE parent_form_id_sr_fk <> f.form_id
			      AND parent_form_id_sr_fk = ?
			      AND f.soft_delete_status_fk = ?
			      AND (
			          SELECT COUNT(*) 
			          FROM form_access 
			          WHERE form_id_fk = f.form_id 
			            AND (access_value = ? OR access_value = ? OR access_value = ?)
			      ) > 0
			    ORDER BY priority ASC
			    """;
		return jdbcTemplate.query(qry, new Object[] { parentIdLevel2, "Active", "Pratik", "IT Admin", "" },
				new RowMapper<Forms>() {
					@Override
					public Forms mapRow(ResultSet rs, int rowNum) throws SQLException {
						Forms form = new Forms();
						form.setFormId(rs.getString("form_id"));
						/// form.setMo(rs.getString("module_name_fk"));
						form.setFormName(rs.getString("form_name"));
						form.setParentId(rs.getString("parent_form_id_sr_fk"));
						form.setWebFormUrl(rs.getString("web_form_url"));
						form.setMobileFormUrl(rs.getString("mobile_form_url"));
						form.setPriority(rs.getString("priority"));
						form.setStatusId(rs.getString("soft_delete_status_fk"));
						form.setDisplayInMobile(rs.getString("display_in_mobile"));
						//String parentIdLevel2 = rs.getString("form_id");
						//form.setFormsSubMenuLevel2(getSubMenuLevel2UpdateForms(parentIdLevel2));
						return form;
					}
				});
			}

	@Override
	public List<Forms> getReportForms() {
		String sql = """
				SELECT DISTINCT
				    form_id,
				    module_name_fk,
				    form_name,
				    parent_form_id_sr_fk,
				    web_form_url,
				    mobile_form_url,
				    priority,
				    f.soft_delete_status_fk,
				    f.display_in_mobile
				FROM form f
				LEFT JOIN user_module m ON f.module_name_fk = m.module_fk
				WHERE m.executive_id_fk = ?
				  AND m.soft_delete_status = ?
				  AND parent_form_id_sr_fk = f.form_id
				  AND f.soft_delete_status_fk = ?
				  AND (
				      (SELECT COUNT(*) FROM form_access
				       WHERE form_id_fk = f.form_id
				         AND (access_value = ? OR access_value = ? OR access_value = ?)) > 0
				      OR web_form_url IS NULL
				      OR web_form_url = ''
				  )
				  AND f.url_type = 'Reports'
				ORDER BY priority ASC
				""";
		return jdbcTemplate.query(sql, new Object[] { "Pratik", "Active", "Active", "Pratik", "IT Admin", "" },
				new RowMapper<Forms>() {
					@Override
					public Forms mapRow(ResultSet rs, int rowNum) throws SQLException {
						Forms form = new Forms();
						form.setFormId(rs.getString("form_id"));
						/// form.setMo(rs.getString("module_name_fk"));
						form.setFormName(rs.getString("form_name"));
						form.setParentId(rs.getString("parent_form_id_sr_fk"));
						form.setWebFormUrl(rs.getString("web_form_url"));
						form.setMobileFormUrl(rs.getString("mobile_form_url"));
						form.setPriority(rs.getString("priority"));
						form.setStatusId(rs.getString("soft_delete_status_fk"));
						form.setDisplayInMobile(rs.getString("display_in_mobile"));
						String parentId = rs.getString("parent_form_id_sr_fk");
						form.setFormsSubMenu(getSubMenuUpdateForms(parentId));
						return form;
					}
				});
	}

}
