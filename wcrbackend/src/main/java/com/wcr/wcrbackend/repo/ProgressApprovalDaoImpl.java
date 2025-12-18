package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

// import com.synergizglobal.wrpmis.common.DBConnectionHandler;
import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.entity.Activity;

import java.math.BigDecimal;

@Repository
public class ProgressApprovalDaoImpl implements ProgressApprovalDao{
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;
	
	/*
	 * @Autowired DataSourceTransactionManager transactionManager;
	 */
	
	@Autowired
	FormHistoryDao formsHistoryDao;
	
	@Override
	public List<Activity> getApprovableActivities(Activity obj) throws Exception {
		List<Activity> objsList = null;
		NumberFormat numberFormatter = new DecimalFormat("#0.00");
		try {
			String qry1 ="";
			
			String qry2 ="";
			
			int arrSize = 0;			
			
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry2=qry2+"	inner JOIN structure_contract_responsible_people sr ON sr.responsible_people_id_fk = '"+obj.getUser_id()+"' and sr.contract_id_fk=a.contract_id_fk and sr.contract_id_fk=c.contract_id and a.structure_id_fk=sr.structure_id_fk and s.structure_id=sr.structure_id_fk ";
			}
		
			
			String qry="SELECT " + 
					"    * " + 
					"FROM " + 
					"    ( " + 
					"        SELECT " + 
					"            distinct a.task_code AS p6_task_code, " + 
					"            progress_id, " + 
					"            v.created_date, " + 
					"            u1.department_fk, " + 
					"            progress_date, " + 
					"            a.p6_activity_id AS activity_id_fk, " + 
					"            a.scope AS total_scope, " + 
					"            a.completed AS cumulative_completed, " + 
					"            v.completed_scope AS actual_for_the_day, " + 
					"            (ISNULL(a.scope, 0) - ISNULL(a.completed, 0)) AS remaining_scope, " + 
					"            attachment_url, " + 
					"            v.remarks, " + 
					"            FORMAT(v.created_date, 'dd-MM-yyyy') AS updated_on, " + 
					"            v.created_by_user_id_fk, " + 
					"            u.user_name AS updated_by, " + 
					"            c.hod_user_id_fk, " + 
					"            approved_or_rejected_by, " + 
					"            FORMAT(approved_on, 'dd-MM-yyyy') AS approved_on, " + 
					"            FORMAT(rejected_on, 'dd-MM-yyyy') AS rejected_on, " + 
					"            approval_status_fk, " + 
					"            c.project_id_fk, " + 
					"            p.project_name, " + 
					"            a.contract_id_fk, " + 
					"            c.contract_short_name, " + 
					"            a.component, " + 
					"            a.component_id, " + 
					"            s.structure, " + 
					"            p6_activity_name AS activity_name, " + 
					"            updated_scope, " + 
					"            unit, " + 
					"            ROUND( " + 
					"                (SUM(a.weightage * (a.completed) / a.scope) / SUM(d.baseline_weight)) * 100, " + 
					"                1 " + 
					"            ) AS component_per_prior, " + 
					"            ROUND( " + 
					"                (SUM(a.weightage * (a.completed) / a.scope) / SUM(d1.baseline_weight)) * 100, " + 
					"                1 " + 
					"            ) AS structure_per_prior, " + 
					"            ROUND( " + 
					"                (SUM(a.weightage * (a.completed + actual_to_be_approved) / a.scope) / " + 
					"                    SUM(d.baseline_weight)) * 100, " + 
					"                1 " + 
					"            ) AS component_per_post, " + 
					"            ROUND( " + 
					"                (SUM(a.weightage * (a.completed + actual_to_be_approved) / a.scope) / " + 
					"                    SUM(d1.baseline_weight)) * 100, " + 
					"                1 " + 
					"            ) AS structure_per_post " + 
					"        FROM " + 
					"            p6_validation v " + 
					"            LEFT JOIN p6_validation_dyhod aph ON aph.progress_id_fk = v.progress_id " + 
					"            LEFT JOIN p6_activities a ON p6_activity_id_fk = p6_activity_id " + 
					"            LEFT JOIN structure s ON structure_id_fk = structure_id " + 
					"            LEFT JOIN activities_component_weight d ON " + 
					"                a.contract_id_fk = d.contract_id_fk AND " + 
					"                s.structure_type_fk = d.structure_type AND " + 
					"                s.structure = d.structure AND " + 
					"                a.component = d.component " + 
					"            LEFT JOIN activities_component_weight d1 ON " + 
					"                a.contract_id_fk = d1.contract_id_fk AND " + 
					"                s.structure_type_fk = d1.structure_type AND " + 
					"                s.structure = d1.structure " + 
					"            LEFT JOIN [user] u ON v.created_by_user_id_fk = u.user_id and u.user_type_fk in('DyHOD','HOD') " + 
					"            LEFT JOIN contract c ON a.contract_id_fk = c.contract_id   "+qry2+" "  
					+ "LEFT  JOIN project p ON c.project_id_fk  = p.project_id "+

					"            LEFT JOIN [user] u1 ON u1.user_id = c.hod_user_id_fk " + 
					"            LEFT JOIN ( " + 
					"                SELECT " + 
					"                    SUM(ISNULL(completed_scope, 0)) AS actual_to_be_approved, " + 
					"                    p6_activity_id_fk " + 
					"                FROM " + 
					"                    p6_validation pv " + 
					"                WHERE " + 
					"                    pv.approval_status_fk = '"+obj.getApproval_status_fk()+"' " + 
					"                GROUP BY " + 
					"                    p6_activity_id_fk " + 
					"            ) m ON m.p6_activity_id_fk = a.p6_activity_id " + 
					"           where approval_status_fk = '"+obj.getApproval_status_fk()+"' "+qry1+"" + 
					"        GROUP BY " + 
					"            a.task_code, " + 
					"            progress_id, " + 
					"            v.created_date, " + 
					"            u1.department_fk, " + 
					"            v.progress_date, " + 
					"            a.p6_activity_id, " + 
					"            a.scope, " + 
					"            a.completed, " + 
					"            v.completed_scope, " + 
					"            v.attachment_url, " + 
					"            v.remarks, " + 
					"            v.created_by_user_id_fk, " + 
					"            aph.dyhod_user_id_fk, " + 
					"            c.hod_user_id_fk, " + 
					"            u.user_name, " + 
					"            v.approved_or_rejected_by, " + 
					"            v.approved_on, " + 
					"            v.rejected_on, " + 
					"            v.approval_status_fk, " + 
					"            c.project_id_fk, " + 
					"            p.project_name, " + 
					"            a.contract_id_fk, " + 
					"            c.contract_short_name, " + 
					"            a.component, " + 
					"            a.component_id, " + 
					"            s.structure, " + 
					"            a.p6_activity_name, " + 
					"            v.updated_scope, " + 
					"            a.unit " + 
					"    ) AS qc " + 
					"WHERE " + 
					"    0 = 0  ";
			
			

			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				qry = qry + " and structure = ?";
				arrSize++;
			}
			/*if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and department_fk = ?";
				arrSize++;
			}*/
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				qry = qry + " and created_by_user_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				qry = qry + " and approval_status_fk = ?";
				arrSize++;
			}
			
			//qry = qry + " group by ap.progress_id ";
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) 
			{
				if(obj.getApproval_status_fk().equals("Approved"))
				{
					qry = qry + " and convert(datetime,approved_on,105) >= DATEADD(day,-30,GETDATE()) " + 
							"and   convert(datetime,approved_on,105) <= getdate() order by activity_id_fk ";
					 
				}
				else if(obj.getApproval_status_fk().equals("Rejected"))
				{
					qry = qry + " and convert(datetime,approved_on,105) >= DATEADD(day,-30,GETDATE()) " + 
							"and   convert(datetime,approved_on,105) <= getdate() order by activity_id_fk ";
				}	
				else if(obj.getApproval_status_fk().equals("Pending"))
				{
					qry = qry + " order by activity_id_fk,DATENAME(dw, created_date)+','+convert(varchar, created_date, 106) desc ";

				}					
			}			
			
			
			Object[] pValues = new Object[arrSize];			
			
			int i = 0;
			
			/*
			 * if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
			 * pValues[i++] = obj.getDyhod_user_id_fk(); pValues[i++] =
			 * obj.getDyhod_user_id_fk(); }
			 */
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				pValues[i++] = obj.getStructure();
			}
			/*if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getDepartment_fk();
			}*/
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				pValues[i++] = obj.getUpdated_by_user_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				pValues[i++] = obj.getApproval_status_fk();
			}
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<Activity>(Activity.class));		
			for (Activity aObj : objsList) {
				if(!StringUtils.isEmpty(aObj.getCumulative_completed())) {
					aObj.setCumulative_completed(numberFormatter.format(Double.parseDouble(aObj.getCumulative_completed())));
				}
				if(!StringUtils.isEmpty(aObj.getRemaining_scope())) {
					aObj.setRemaining_scope(numberFormatter.format(Double.parseDouble(aObj.getRemaining_scope())));
				}
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	
	@Override
	public List<Activity> getWorksInApprovableActivities(Activity obj) throws Exception {
		List<Activity> objsList = null;
		try {
			String qry = "select c.project_id_fk,work_short_name "
					+ "from p6_validation_dyhod aph "
					+ "LEFT JOIN p6_validation ap ON aph.progress_id_fk = ap.progress_id "
					+ "LEFT JOIN p6_activities a ON ap.p6_activity_id_fk = a.p6_activity_id "
					+ "left join structure s on s.structure_id = a.structure_id_fk "
					+ "LEFT JOIN contract c ON a.contract_id_fk = c.contract_id "
					+ "LEFT JOIN work w ON c.project_id_fk = w.work_id "
					+ "where progress_id is not null";
			
			int arrSize = 0;			
			
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and aph.dyhod_user_id_fk = ?";
				arrSize++;
			}
					
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and a.contract_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				qry = qry + " and s.structure = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				qry = qry + " and ap.created_by_user_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				qry = qry + " and ap.approval_status_fk = ?";
				arrSize++;
			}
			
			qry = qry + " group by c.project_id_fk,work_short_name order by c.project_id_fk";
			
			Object[] pValues = new Object[arrSize];			
			
			int i = 0;
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getDyhod_user_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				pValues[i++] = obj.getStructure();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				pValues[i++] = obj.getUpdated_by_user_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				pValues[i++] = obj.getApproval_status_fk();
			}
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<Activity>(Activity.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Activity> getContractsInApprovableActivities(Activity obj) throws Exception {
		List<Activity> objsList = null;
		try {
			String qry = "select a.contract_id_fk,contract_short_name "
					+ "from p6_validation_dyhod aph "
					+ "LEFT JOIN p6_validation ap ON aph.progress_id_fk = ap.progress_id "
					+ "LEFT JOIN p6_activities a ON ap.p6_activity_id_fk = a.p6_activity_id "
					+ "left join structure s on s.structure_id = a.structure_id_fk "
					+ "LEFT JOIN contract c ON a.contract_id_fk = c.contract_id "
					+ "where progress_id is not null";
					
			int arrSize = 0;			
			
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and aph.dyhod_user_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and a.contract_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				qry = qry + " and s.structure = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				qry = qry + " and ap.created_by_user_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				qry = qry + " and ap.approval_status_fk = ?";
				arrSize++;
			}
			
			qry = qry + " group by a.contract_id_fk,contract_short_name order by a.contract_id_fk";
			
			Object[] pValues = new Object[arrSize];			
			
			int i = 0;
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getDyhod_user_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				pValues[i++] = obj.getStructure();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				pValues[i++] = obj.getUpdated_by_user_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				pValues[i++] = obj.getApproval_status_fk();
			}
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<Activity>(Activity.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	@Override
	public List<Activity> getStructuresInApprovableActivities(Activity obj) throws Exception {
		List<Activity> objsList = null;
		try {
			String qry = "select structure "
					+ "from p6_validation_dyhod aph "
					+ "LEFT JOIN p6_validation ap ON aph.progress_id_fk = ap.progress_id "
					+ "LEFT JOIN p6_activities a ON ap.p6_activity_id_fk = a.p6_activity_id "
					+ "left join structure s on s.structure_id = a.structure_id_fk "
					+ "LEFT JOIN contract c ON a.contract_id_fk = c.contract_id "
					+ "where progress_id is not null";
					
			int arrSize = 0;			
			
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and aph.dyhod_user_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and a.contract_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				qry = qry + " and s.structure = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				qry = qry + " and ap.created_by_user_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				qry = qry + " and ap.approval_status_fk = ?";
				arrSize++;
			}
			
			qry = qry + " group by s.structure order by s.structure";
			
			Object[] pValues = new Object[arrSize];			
			
			int i = 0;
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getDyhod_user_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				pValues[i++] = obj.getStructure();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				pValues[i++] = obj.getUpdated_by_user_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				pValues[i++] = obj.getApproval_status_fk();
			}
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<Activity>(Activity.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	@Override
	public List<Activity> getDepartmentsInApprovableActivities(Activity obj) throws Exception {
		List<Activity> objsList = null;
		try {
			String qry = "select c.department_fk,department_name "
					+ "from p6_validation_dyhod aph "
					+ "LEFT JOIN p6_validation ap ON aph.progress_id_fk = ap.progress_id "
					+ "LEFT JOIN p6_activities a ON ap.p6_activity_id_fk = a.p6_activity_id "
					+ "left join structure s on s.structure_id = a.structure_id_fk "
					+ "LEFT JOIN contract c ON a.contract_id_fk = c.contract_id "
					+ "LEFT JOIN department d ON c.department_fk = d.department "
					+ "where progress_id is not null";
					
			int arrSize = 0;			
			
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and aph.dyhod_user_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and a.contract_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				qry = qry + " and s.structure = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				qry = qry + " and ap.created_by_user_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				qry = qry + " and ap.approval_status_fk = ?";
				arrSize++;
			}
			
			qry = qry + " group by c.department_fk order by c.department_fk";
			
			Object[] pValues = new Object[arrSize];			
			
			int i = 0;
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getDyhod_user_id_fk();
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				pValues[i++] = obj.getStructure();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				pValues[i++] = obj.getUpdated_by_user_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				pValues[i++] = obj.getApproval_status_fk();
			}
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<Activity>(Activity.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	@Override
	public List<Activity> getUpdatedByListInApprovableActivities(Activity obj) throws Exception {
		List<Activity> objsList = null;
		try {
			String qry = "select ap.created_by_user_id_fk as user_id,user_name "
					+ "from p6_validation_dyhod aph "
					+ "LEFT JOIN p6_validation ap ON aph.progress_id_fk = ap.progress_id "
					+ "LEFT JOIN p6_activities a ON ap.p6_activity_id_fk = a.p6_activity_id "
					+ "left join structure s on s.structure_id = a.structure_id_fk "
					+ "LEFT JOIN contract c ON a.contract_id_fk = c.contract_id "
					+ "LEFT JOIN [user] u ON ap.created_by_user_id_fk = u.user_id "
					+ "where progress_id is not null";
					
			int arrSize = 0;			
			
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and aph.dyhod_user_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and a.contract_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				qry = qry + " and s.structure = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				qry = qry + " and ap.created_by_user_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				qry = qry + " and ap.approval_status_fk = ?";
				arrSize++;
			}
			
			qry = qry + " group by ap.created_by_user_id_fk,user_name order by ap.created_by_user_id_fk";
			
			Object[] pValues = new Object[arrSize];			
			
			int i = 0;
			if(!CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getDyhod_user_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				pValues[i++] = obj.getStructure();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpdated_by_user_id_fk())) {
				pValues[i++] = obj.getUpdated_by_user_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getApproval_status_fk())) {
				pValues[i++] = obj.getApproval_status_fk();
			}
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<Activity>(Activity.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}


	@Override
	public Activity approveActivityProgress(Activity obj) throws Exception {
		Activity aObj = new Activity();
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		try {
			
			String qry = "SELECT  " + 
					"    ap.progress_id, " + 
					"    ap.progress_date, " + 
					"    ap.p6_activity_id_fk AS activity_id, " + 
					"    COALESCE(a.scope, 0) AS scope, " + 
					"    COALESCE(a.completed, 0) AS completed, " + 
					"    ap.completed_scope AS actual_for_the_day, " + 
					"    (COALESCE(a.scope, 0) - COALESCE(a.completed, 0)) AS remaining_scope, " + 
					"    ap.attachment_url, " + 
					"    ap.remarks, " + 
					"    FORMAT(ap.created_date, 'dd-MM-yyyy') AS updated_on, " + 
					"    ap.created_by_user_id_fk, " + 
					"    approved_or_rejected_by, " + 
					"    u.user_name AS updated_by, " + 
					"    FORMAT(approved_on, 'dd-MM-yyyy') AS approved_on, " + 
					"    FORMAT(rejected_on, 'dd-MM-yyyy') AS rejected_on, " + 
					"    ap.approval_status_fk, " + 
					"    c.project_id_fk, " + 
					"    p.project_name, " + 
					"    a.contract_id_fk, " + 
					"    c.contract_short_name, " + 
					"    a.component, " + 
					"    a.component_id, " + 
					"    s.structure, " + 
					"    a.p6_activity_name AS activity_name, " + 
					"    updated_scope " + 
					"FROM p6_validation ap " + 
					"LEFT JOIN [user] u ON ap.created_by_user_id_fk = u.user_id " + 
					"LEFT JOIN p6_activities a ON ap.p6_activity_id_fk = a.p6_activity_id " + 
					"LEFT JOIN structure s ON s.structure_id = a.structure_id_fk " + 
					"LEFT JOIN contract c ON a.contract_id_fk = c.contract_id " 
					+ "LEFT  JOIN project p ON c.project_id_fk  = p.project_id "
					+ "where progress_id  = ? ";			
			
			Object[] pValues = new Object[] {obj.getProgress_id()};	
			
			List<Activity> objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<Activity>(Activity.class));
			for (Activity activity : objsList) {
				aObj = activity;	
			}
			
			if(!StringUtils.isEmpty(aObj)) {
				BigDecimal scope = new BigDecimal(aObj.getScope());
				BigDecimal completed = new BigDecimal(getCompletedValue(aObj.getActivity_id()));
				BigDecimal remaining = new BigDecimal(aObj.getRemaining_scope());
				BigDecimal actual_for_the_day = new BigDecimal(aObj.getActual_for_the_day()==null?"0":aObj.getActual_for_the_day());

				if ((completed.add(actual_for_the_day)).compareTo(scope) <= 0) {
					String updateQry = "UPDATE p6_activities SET completed = cast(? as decimal(10,2)) + cast(? as decimal(10,2))";	
					int arrSize = 3;
					
					if (completed.compareTo(BigDecimal.ZERO) == 0) {
						updateQry = updateQry + ", start = ?";
						arrSize++;
					}
					
	
					
					if (scope.compareTo(completed.add(actual_for_the_day)) == 0) 
					{
						updateQry = updateQry + ", finish = ?";
						arrSize++;
					}					
					
					if(aObj.getUpdated_scope()!=null && Float.parseFloat(aObj.getUpdated_scope())>=0)
					{
						updateQry = updateQry + ", Scope = ?";
						arrSize++;						
					}
					
					if(aObj.getPlanned_start()!=null)
					{
						
						updateQry = updateQry + ", baseline_start = ?";
						arrSize++;						
					}
					
					if(aObj.getPlanned_finish()!=null)
					{
						
						updateQry = updateQry + ", baseline_finish = ?";
						arrSize++;						
					}					
					
					updateQry = updateQry + " WHERE p6_activity_id = ?";
					
					pValues = new Object[arrSize];
					int i = 0;	
					if(aObj.getUpdated_scope()==null)
					{
						pValues[i++] = aObj.getCompleted();	

					}
					else
					{
						if(Float.parseFloat(aObj.getUpdated_scope())==0)
						{
							pValues[i++] = 0;	
						}
						else
						{
							pValues[i++] = aObj.getCompleted();	
						}
					}
					
					if(aObj.getActual_for_the_day()!=null)
					{
						pValues[i++] = aObj.getActual_for_the_day();
					}
					else
					{
						pValues[i++] = 0;
					}
					if (completed.compareTo(BigDecimal.ZERO) == 0) 
					{
						if(aObj.getUpdated_scope()==null)
						{
							pValues[i++] = aObj.getProgress_date();

						}
						else
						{
							if(Float.parseFloat(aObj.getUpdated_scope())==0)
							{
								pValues[i++] = null;	
							}
							else
							{
								pValues[i++] = aObj.getProgress_date();	
							}
						}
					}
			
					
					if (scope.compareTo(completed.add(actual_for_the_day)) == 0) 
					{
						
						if(aObj.getUpdated_scope()==null)
						{
							pValues[i++] = getActivityMaxProgressDate(aObj.getActivity_id());

						}
						else
						{
							if(Float.parseFloat(aObj.getUpdated_scope())==0)
							{
								pValues[i++] = null;	
							}
							else
							{
								pValues[i++] = getActivityMaxProgressDate(aObj.getActivity_id());
							}
						}					
					}					
					
					if(aObj.getUpdated_scope()!=null && Float.parseFloat(aObj.getUpdated_scope())>=0)
					{
						pValues[i++] = aObj.getUpdated_scope();
					}
					
					if(aObj.getPlanned_start()!=null)
					{
						
						pValues[i++] = aObj.getPlanned_start();						
					}
					
					if(aObj.getPlanned_finish()!=null)
					{
						
						pValues[i++] = aObj.getPlanned_finish();						
					}						
					
					pValues[i++] = aObj.getActivity_id();
					
					int count = jdbcTemplate.update( updateQry, pValues);			
					if(count > 0) {
						jdbcTemplate.update( "UPDATE p6_validation set approval_status_fk = ?,approved_or_rejected_by = ?, approved_on = CURRENT_TIMESTAMP where progress_id = ?",
								new Object[]{"Approved",obj.getDyhod_user_id_fk(),aObj.getProgress_id()});	
						
						String pQry = "INSERT INTO p6_activity_progress(progress_date,p6_activity_id_fk,completed_scope,remarks,created_by_user_id_fk,approval_datails_id_fk)"
								+ "SELECT progress_date,p6_activity_id_fk,completed_scope,remarks,created_by_user_id_fk,progress_id "
								+ "FROM p6_validation "
								+ "WHERE progress_id = ?";
						jdbcTemplate.update( pQry,new Object[]{aObj.getProgress_id()});
						
						
						if(aObj.getUpdated_scope()!=null)
						{
							if(Float.parseFloat(aObj.getUpdated_scope())==0)
							{
								Connection con = null;
								PreparedStatement stmt = null;
								con = dataSource.getConnection();
								
								String deleteQry = "DELETE FROM p6_activity_progress where p6_activity_id_fk = ? ";
								stmt = con.prepareStatement(deleteQry);
								stmt.setString(1,aObj.getActivity_id());
								stmt.executeUpdate();
								if(stmt != null){stmt.close();}
							}
						}

												
						
						aObj.setMessage_flag(true);
						aObj.setMessage("Activity progress approved");
						
						FormHistory formHistory = new FormHistory();
						formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
						formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
						formHistory.setModule_name_fk("Execution &  Monitoring");
						formHistory.setForm_name("Validate Data");
						formHistory.setForm_action_type("Approved");
						formHistory.setForm_details("1 activity progress updated for "+obj.getStructure());
						formHistory.setProject_id_fk(obj.getProject_id_fk());
						formHistory.setContract_id_fk(obj.getContract_id_fk());
						
						boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
					}else {
						aObj.setMessage_flag(false);
						aObj.setMessage("Please try again after sometime.");
					}
				}else{
					aObj.setMessage_flag(false);
					aObj.setMessage("Acceptable progress is "+remaining+". But actual for the day is "+actual_for_the_day);
				}
			}
			//transactionManager.commit(status);
		}catch(Exception e){ 
			//transactionManager.rollback(status);
			aObj.setMessage_flag(false);
			aObj.setMessage("Please try again after sometime.");
			throw new Exception(e);
		}
		return aObj;
	}
	
	private String getCompletedValue(String activity_id) throws Exception
	{
		String Completed="";
		try {
			String qry = "select ISNULL(Completed,0) as Completed from p6_activities where p6_activity_id = ?";
			Completed = (String) jdbcTemplate.queryForObject(qry, new Object[] { activity_id }, String.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return Completed;
	}	
	
	@Override
	public Activity rejectActivityProgress(Activity obj) throws Exception {
		Activity aObj = new Activity();
		try {
			int c = jdbcTemplate.update( "UPDATE p6_validation set approval_status_fk = ?,approved_or_rejected_by = ?, rejected_on = CURRENT_TIMESTAMP where progress_id = ?",
						new Object[]{"Rejected",obj.getDyhod_user_id_fk(),obj.getProgress_id()});	
			if(c > 0) {
				aObj.setMessage_flag(true);
				aObj.setMessage("Activity progress rejected");
				
				FormHistory formHistory = new FormHistory();
				formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
				formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
				formHistory.setModule_name_fk("Execution &  Monitoring");
				formHistory.setForm_name("Validate Data");
				formHistory.setForm_action_type("Rejected");
				formHistory.setForm_details("1 activity progress rejected for "+obj.getStructure());
				formHistory.setProject_id_fk(obj.getProject_id_fk());
				formHistory.setContract_id_fk(obj.getContract_id_fk());
				
				boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
			}else {
				aObj.setMessage_flag(false);
				aObj.setMessage("Please try again after sometime.");
			}
		}catch(Exception e){ 
			aObj.setMessage_flag(false);
			aObj.setMessage("Please try again after sometime.");
			throw new Exception(e);
		}
		return aObj;
	}


	@Override
	public Activity approveMultipleActivityProgress(Activity obj) throws Exception {
		Activity aObj = new Activity();
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dataSource.getConnection();
			List<Activity> approvableList = new ArrayList<Activity>();
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProgress_id())) {
				String qry = "select progress_id,progress_date,ap.p6_activity_id_fk as activity_id,ISNULL(a.scope,0) as scope,ISNULL(a.completed,0) as completed,"
						+ "cast(ap.completed_scope as varchar) as actual_for_the_day,(ISNULL(a.scope,0) - ISNULL(a.completed,0)) as remaining_scope,"
						+ "attachment_url,ap.remarks,FORMAT(ap.created_date,'dd-MM-yyyy') as updated_on,"
						+ "ap.created_by_user_id_fk,approved_or_rejected_by,u.user_name as updated_by,"
						+ "FORMAT(approved_on,'dd-MM-yyyy') as approved_on,FORMAT(rejected_on,'dd-MM-yyyy') as rejected_on,approval_status_fk,"
						+ "c.project_id_fk,p.project_name,a.contract_id_fk,c.contract_short_name,a.component,a.component_id,structure,p6_activity_name as activity_name,updated_scope "
						+ "from p6_validation ap "
						+ "LEFT JOIN [user] u ON ap.created_by_user_id_fk = u.user_id "
						+ "LEFT JOIN p6_activities a ON ap.p6_activity_id_fk = a.p6_activity_id "
						+ "left join structure s on s.structure_id = a.structure_id_fk "
						+ "LEFT JOIN contract c ON a.contract_id_fk = c.contract_id "
						+ "LEFT  JOIN project p ON c.project_id_fk  = p.project_id "
						+ "where progress_id  in (";
				String placeholders = "";
				String progress_id = obj.getProgress_id().replaceAll("'", "");
				String[] progress_ids = progress_id.split(",");
				for(int p=0;p<progress_ids.length;p++) {
					if(placeholders.length() > 0) {
						placeholders = placeholders + ",";
					}else{
						placeholders = placeholders + "";
					}
					placeholders = placeholders + "?";
				}
				
				qry = qry + placeholders+")";			
				
				Object[] pValues = new Object[progress_ids.length];	
				for(int p=0;p<progress_ids.length;p++) {
					pValues[p] = progress_ids[p].trim();
				}
				List<Activity> objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<Activity>(Activity.class));
				for (Activity activityRows : objsList) {
					approvableList.add(activityRows);	
				}
			}
			
			String successMessage = "";
			String failureMessage = "";
			int successCount = 0;
			int failureCount = 0;
			
			if(!StringUtils.isEmpty(approvableList) && approvableList.size() > 0) {
				for (Activity activity : approvableList) {
					BigDecimal scope = new BigDecimal(activity.getScope());
					BigDecimal completed = new BigDecimal(getCompletedValue(activity.getActivity_id()));
					BigDecimal remaining = new BigDecimal(activity.getRemaining_scope());
					BigDecimal actual_for_the_day = new BigDecimal(activity.getActual_for_the_day()==null?"0":activity.getActual_for_the_day());

					if ((completed.add(actual_for_the_day)).compareTo(scope) <= 0) {
						
						String updateQry = "UPDATE p6_activities SET completed = cast(? as decimal(10,2)) + cast(? as decimal(10,2))";	
						int arrSize = 3;
						
						
						if(activity.getUpdated_scope()!=null && Float.parseFloat(activity.getUpdated_scope())>=0)
						{
							
							updateQry = updateQry + ", Scope = ?";
							arrSize++;						
						}
						
						if(activity.getPlanned_start()!=null)
						{
							
							updateQry = updateQry + ", baseline_start = ?";
							arrSize++;						
						}
						
						if(activity.getPlanned_finish()!=null)
						{
							
							updateQry = updateQry + ", baseline_finish = ?";
							arrSize++;						
						}						
						
						
						
						updateQry = updateQry + " WHERE p6_activity_id = ?";
						
						Object[] pValues = new Object[arrSize];
						int i = 0;			
					
						if(activity.getUpdated_scope()==null)
						{
							pValues[i++] = completed;

						}
						else
						{
							if(Float.parseFloat(activity.getUpdated_scope())==0)
							{
								pValues[i++] = 0;	
							}
							else
							{
								pValues[i++] = activity.getCompleted();	
							}
						}						
						
						if(activity.getActual_for_the_day()!=null)
						{
							pValues[i++] = activity.getActual_for_the_day();
						}
						else
						{
							pValues[i++] = 0;
						}

						if(activity.getUpdated_scope()!=null && Float.parseFloat(activity.getUpdated_scope())>=0)
						{
							pValues[i++] = activity.getUpdated_scope();
						}	
						
						
						if(activity.getPlanned_start()!=null)
						{
							
							pValues[i++] = activity.getPlanned_start();						
						}
						
						if(activity.getPlanned_finish()!=null)
						{
							
							pValues[i++] = activity.getPlanned_finish();						
						}						
						
						
						pValues[i++] = activity.getActivity_id();
						
						int count = jdbcTemplate.update( updateQry, pValues);			
						if(count > 0) {							
							jdbcTemplate.update( "UPDATE p6_validation set approval_status_fk = ?,approved_or_rejected_by = ?, approved_on = CURRENT_TIMESTAMP where progress_id = ?",
									new Object[]{"Approved",obj.getDyhod_user_id_fk(),activity.getProgress_id()});	
							
							String pQry = "INSERT INTO p6_activity_progress(progress_date,p6_activity_id_fk,completed_scope,remarks,created_by_user_id_fk,approval_datails_id_fk)"
									+ "SELECT progress_date,p6_activity_id_fk,completed_scope,remarks,created_by_user_id_fk,progress_id "
									+ "FROM p6_validation "
									+ "WHERE progress_id = ?";
							jdbcTemplate.update( pQry,new Object[]{activity.getProgress_id()});
						
							
							if(activity.getUpdated_scope()!=null)
							{
								if(Float.parseFloat(activity.getUpdated_scope())==0)
								{
									String deleteQry = "DELETE FROM p6_activity_progress where p6_activity_id_fk = ? ";
									stmt = con.prepareStatement(deleteQry);
									stmt.setString(1,activity.getActivity_id());
									stmt.executeUpdate();
									if(stmt != null){stmt.close();}
								}
							}							
							
							successCount++;
						}else {
							failureCount++;
						}
					}else{
						failureCount++;
					}
				}
				aObj.setMessage_flag(true);
				if(successCount > 0) {
					successMessage = successMessage + successCount+ " Activities approved";
					
					FormHistory formHistory = new FormHistory();
					formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
					formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
					formHistory.setModule_name_fk("Execution &  Monitoring");
					formHistory.setForm_name("Validate Data");
					formHistory.setForm_action_type("Approved");
					formHistory.setForm_details(successCount + " activities progress approved for "+obj.getStructure());
					formHistory.setProject_id_fk(obj.getProject_id_fk());
					formHistory.setContract_id_fk(obj.getContract_id_fk());
					
					boolean history_flag = formsHistoryDao.saveValidityFormHistory(formHistory);
				}
				if(failureCount > 0 && !StringUtils.isEmpty(successMessage)) {
					successMessage = successMessage + "And "+failureCount+" activities failed to approve";
				}else if(failureCount > 0){
					successMessage = successMessage + failureCount+" activities failed to approve";
				}
				
				aObj.setMessage_flag(true);
				aObj.setMessage(successMessage);
			}else{
				aObj.setMessage_flag(false);
				aObj.setMessage("There are no activities to approve");
			}
		}catch(Exception e){ 
			aObj.setMessage_flag(false);
			aObj.setMessage("Please try after sometime.");
			throw new Exception(e);
		}
		return aObj;
	}

	private String getActivityMaxProgressDate(String activity_id) throws Exception
	{
		String progressdate="";
		try {
			String qry = "select Max(progress_date) as  progress_date from p6_activity_progress where p6_activity_id_fk= ?";
			progressdate = (String) jdbcTemplate.queryForObject(qry, new Object[] { activity_id }, String.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return progressdate;
	}

	@Override
	public Activity rejectMultipleActivityProgress(Activity obj) throws Exception {
		Activity aObj = new Activity();
		try {
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProgress_id())) {
				String qry  = "UPDATE p6_validation set approval_status_fk = ?,approved_or_rejected_by = ?, rejected_on = CURRENT_TIMESTAMP where progress_id in(";				
				
				String progress_id = obj.getProgress_id().replaceAll("'", "");
				String[] progress_ids = progress_id.split(",");
				String placeholders = "";
				for(int p=0;p<progress_ids.length;p++) {
					if(placeholders.length() > 0) {
						placeholders = placeholders + ",";
					}else{
						placeholders = placeholders + "";
					}
					placeholders = placeholders + "?";
				}
				
				qry = qry + placeholders+")";			
				
				Object[] pValues = new Object[2+(progress_ids.length)];	
				int i = 0;
				pValues[i++] = "Rejected";
				pValues[i++] = obj.getDyhod_user_id_fk();
				for(int p=0;p<progress_ids.length;p++) {
					pValues[i++] = progress_ids[p].trim();
				}
				
				int c = jdbcTemplate.update( qry,pValues);	
				if(c > 0) {
					aObj.setMessage_flag(true);
					aObj.setMessage("Rejected "+ progress_ids.length +" activities" );
					
					FormHistory formHistory = new FormHistory();
					formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
					formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
					formHistory.setModule_name_fk("Execution &  Monitoring");
					formHistory.setForm_name("Validate Data");
					formHistory.setForm_action_type("Rejected");
					formHistory.setForm_details(c + " activities progress rejected for "+obj.getStructure());
					formHistory.setProject_id_fk(obj.getProject_id_fk());
					formHistory.setContract_id_fk(obj.getContract_id_fk());
					
					boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
					
				}else {
					aObj.setMessage_flag(false);
					aObj.setMessage("Please try after sometime.");
				}
			}
		}catch(Exception e){ 
			aObj.setMessage_flag(false);
			aObj.setMessage("Please try after sometime.");
			throw new Exception(e);
		}
		return aObj;
	}

}
