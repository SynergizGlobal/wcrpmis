package com.wcr.wcrbackend.repo;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.common.CommonConstants;

@Repository
public class IssuesDetailsReportRepository implements IIssuesDetailsReportRepository{
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;

	@Override
	public Issue getIssue(Issue obj) throws Exception {
		Issue iObj = new Issue();
		try {
			String qry = "select issue_id,contract_id_fk,title,i.description,FORMAT(date,'dd-MM-yyyy') AS date,location,cast(latitude as CHAR) as latitude,cast(longitude as CHAR) as longitude,reported_by,responsible_person,c.department_fk," 
					+ "d.department_name,priority_fk,category_fk,status_fk,corrective_measure,FORMAT(resolved_date,'dd-MM-yyyy') AS resolved_date,escalated_to,i.remarks,contract_name,"
					+ "c.contract_short_name,c.project_id_fk,p.project_name,i.zonal_railway_fk,r.railway_name,other_organization,"
					+ "FORMAT(escalation_date,'dd-MM-yyyy') AS escalation_date,FORMAT(assigned_date,'dd-MM-yyyy') AS assigned_date,DATEDIFF(day,CONVERT(date, getdate()),date) AS pending_Since, "
					+ "u2.designation as responsible_person_designation,u3.designation as escalated_to_designation,ctr.contractor_id,ctr.contractor_name,"
					+ "c.hod_user_id_fk,c.dy_hod_user_id_fk,u4.designation as hod_designation,u5.designation as dyHod_designation,i.status_fk as existing_status_fk,"
					+ "FORMAT(i.created_date,'dd-MM-yyyy') AS created_date,other_org_resposible_person_name,other_org_resposible_person_designation "
					+ "from issue i "
					+ "LEFT OUTER JOIN [user] u2 on i.responsible_person = u2.user_id "
					+ "LEFT OUTER JOIN [user] u3 on i.escalated_to = u3.user_id "
					+ "LEFT OUTER JOIN contract c ON i.contract_id_fk  = c.contract_id "
					+ "LEFT OUTER JOIN [user] u4 on c.hod_user_id_fk = u4.user_id "
					+ "LEFT OUTER JOIN [user] u5 on c.dy_hod_user_id_fk = u5.user_id "
					+ "LEFT OUTER JOIN contractor ctr ON c.contractor_id_fk= ctr.contractor_id "
					+ "LEFT OUTER JOIN department d ON c.department_fk  = d.department "
					+ "LEFT OUTER JOIN project p ON c.project_id_fk  = p.project_id "
					+ "LEFT OUTER JOIN railway r ON i.zonal_railway_fk  = r.railway_id "
					+ "where issue_id = ? " ;
			int arrSize = 1;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getPriority_fk())) {
				qry = qry + " and priority_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_id_fk())) {
				qry = qry + " and w.work_id = ?";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ?";
				arrSize++;
			}
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getIssue_id();
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getPriority_fk())) {
				pValues[i++] = obj.getPriority_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_id_fk())) {
				pValues[i++] = obj.getWork_id_fk();
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			
			List<Issue> iObjList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
			for (Issue issue : iObjList) {
				if(!StringUtils.isEmpty(issue)) {
					iObj = issue;
					String filesQry ="select file_name,issue_id_fk as issue_id from issue_files where issue_id_fk = ? ";					
					List<Issue> objsList = jdbcTemplate.query( filesQry,new Object[] {obj.getIssue_id()}, new BeanPropertyRowMapper<Issue>(Issue.class));					
					if(!StringUtils.isEmpty(objsList)) {
						iObj.setIssueFilesList(objsList);
					}
					
					String fileNamesQry ="select STRING_AGG(file_name,',') as attachments from issue_files where issue_id_fk = ? ";					
					Issue fileNames = jdbcTemplate.queryForObject( fileNamesQry,new Object[] {obj.getIssue_id()}, new BeanPropertyRowMapper<Issue>(Issue.class));					
					if(!StringUtils.isEmpty(fileNames)) {
						iObj.setAttachments(fileNames.getAttachments());
					}
					
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_id())) {
						if(obj.getUser_id().equals(iObj.getResponsible_person()) || obj.getUser_id().equals(iObj.getEscalated_to()) 
								|| obj.getUser_id().equals(iObj.getHod_user_id_fk()) || obj.getUser_id().equals(iObj.getDy_hod_user_id_fk()) 
								|| obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN) ){
							iObj.setReadonlyForm(false);
						}else {
							iObj.setReadonlyForm(true);
						}
						
					}
					
					if(!StringUtils.isEmpty(obj.getMessage_id())) {
						NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);			 
						String msgUpdateqry = "UPDATE messages SET read_time=CURRENT_TIMESTAMP where message_id = :message_id" ;	
						
						BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
						template.update(msgUpdateqry, paramSource);	
					}
					
				}
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return iObj;
	}

	@Override
	public List<Issue> getIssueHistory(Issue obj) throws Exception {
		List<Issue> objList = null;
		try {
			String qry = "select id, issue_id_fk, issue_status_fk as status_fk, u.designation,assigned_person_user_id_fk,"
					+ "FORMAT(created_date,'%d-%b-%Y %h:%m %p') AS created_date,comment,u2.designation as created_by "
					+ "from issue_history ih "
					+ "LEFT OUTER JOIN [user] u on ih.assigned_person_user_id_fk = u.user_id "
					+ "LEFT OUTER JOIN [user] u2 on ih.created_by = u2.user_id "
					+ "where issue_id_fk = ? ORDER by created_date ASC" ;
			
			objList = jdbcTemplate.query( qry, new Object[] {obj.getIssue_id()}, new BeanPropertyRowMapper<Issue>(Issue.class));
	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objList;
	}

}
