package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.DTO.Project;
@Repository
public class ProjectRepository implements IProjectRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Project> getProjectTypes() throws Exception{
		List<Project> objsList = null;
		try {
			String projectsQry = "select project_type_id,project_type_name from project_type";
			objsList = jdbcTemplate.query( projectsQry,new Object[] {}, new BeanPropertyRowMapper<Project>(Project.class));	
		}catch(Exception e){ 
			throw new Exception(e.getMessage());
		}
		return objsList;
	}
	@Override
	public List<Project> getProjects() throws Exception {
		List<Project> objsList = null;
	    try {
	        String projectsQry =
	            "SELECT " +
	            "    p.project_id, " +
	            "    p.project_name, " +
	            "    p.project_type_id_fk AS project_type_id, " +
	            "    pt.project_type_name, " +

	            "    (SELECT SUM(ISNULL(scope,0)) " +
	            "     FROM p6_activities a2 " +
	            "     INNER JOIN contract c2 ON c2.contract_id = a2.contract_id_fk " +
	            "     WHERE c2.project_id_fk = p.project_id " +
	            "       AND a2.p6_activity_name = 'Track Laying') AS length, " +

	            "    (SELECT SUM(ISNULL(completed,0)) " +
	            "     FROM p6_activities a3 " +
	            "     INNER JOIN contract c3 ON c3.contract_id = a3.contract_id_fk " +
	            "     WHERE c3.project_id_fk = p.project_id " +
	            "       AND a3.p6_activity_name = 'Commissioning') AS commissioned_length, " +

	            "    COUNT(DISTINCT p.project_id) AS total_projects, " +
	            "    COUNT(DISTINCT CASE \n" + 
	            "               WHEN UPPER(LTRIM(RTRIM(p.project_status))) = 'OPEN' \n" + 
	            "               THEN p.project_id \n" + 
	            "             END) AS ongoing_projects, " +

	            "        (SELECT SUM(a1.scope)\n" + 
	            "     FROM project p1\n" + 
	            "     INNER JOIN contract c1 ON c1.project_id_fk = p1.project_id\n" + 
	            "     INNER JOIN p6_activities a1 ON a1.contract_id_fk = c1.contract_id\n" + 
	            "     WHERE a1.p6_activity_name = 'Track Laying'\n" + 
	            "       AND p1.project_type_id_fk = pt.project_type_id\n" + 
	            "    ) AS total_length, " +
	            "    SUM(CASE WHEN a.p6_activity_name IN ('Earthwork cutting','Earthwork filling') THEN ISNULL(a.scope,0) END) AS total_earthwork, " +
	            "    SUM(CASE WHEN a.p6_activity_name = 'Track Laying' THEN ISNULL(a.completed,0) END) AS completed_track, " +
	            "    SUM(CASE WHEN a.p6_activity_name = 'Commissioning' THEN ISNULL(a.completed,0) END) AS commissioned_length2, " +

	            "    SUM(DISTINCT CASE WHEN s.structure_type_fk = 'Major Bridge' AND a.scope = a.completed THEN 1 ELSE 0 END) AS completed_major_bridges, " +
	            "    COUNT(DISTINCT CASE WHEN s.structure_type_fk = 'Major Bridge' THEN s.structure_id END) AS total_major_bridges, " +

	            "    SUM(DISTINCT CASE WHEN s.structure_type_fk = 'Minor Bridge' AND a.scope = a.completed THEN 1 ELSE 0 END) AS completed_minor_bridges, " +
	            "    COUNT(DISTINCT CASE WHEN s.structure_type_fk = 'Minor Bridge' THEN s.structure_id END) AS total_minor_bridges, " +

	            "    SUM(DISTINCT CASE WHEN s.structure_type_fk = 'ROB' AND a.scope = a.completed THEN 1 ELSE 0 END) AS completed_rob, " +
	            "    COUNT(DISTINCT CASE WHEN s.structure_type_fk = 'ROB' THEN s.structure_id END) AS total_rob, " +

	            "    SUM(DISTINCT CASE WHEN s.structure_type_fk = 'RUB' AND a.scope = a.completed THEN 1 ELSE 0 END) AS completed_rub, " +
	            "    COUNT(DISTINCT CASE WHEN s.structure_type_fk = 'RUB' THEN s.structure_id END) AS total_rub " +

	            "FROM project p " +
	            "INNER JOIN project_type pt ON p.project_type_id_fk = pt.project_type_id " +
	            "LEFT JOIN contract c ON c.project_id_fk = p.project_id " +
	            "LEFT JOIN p6_activities a ON c.contract_id = a.contract_id_fk " +
	            "LEFT JOIN structure s ON p.project_id = s.project_id_fk and s.structure_id=a.structure_id_fk  " +

	            "GROUP BY p.project_id, p.project_name,project_type_id, p.project_type_id_fk, pt.project_type_name, p.project_status " +
	            "ORDER BY p.project_id";

	        objsList = jdbcTemplate.query(projectsQry, new Object[] {}, new BeanPropertyRowMapper<Project>(Project.class));

	    } catch (Exception e) {
	        throw new Exception(e.getMessage());
	    }
	    return objsList;
	}

}
