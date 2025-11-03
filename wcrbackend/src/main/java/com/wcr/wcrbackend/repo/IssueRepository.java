package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.common.CommonConstants;
@Repository
public class IssueRepository implements IIssueRepo {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Issue> getIssuesCategoryList(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "\r\n" + 
					"select distinct f.issue_category_fk as category,issues_related_to from issue_contarct_category f\r\n" + 
					"\r\n" + 
					"left join issue_category_title t on t.issue_category_fk=f.issue_category_fk  ";
			int arraSize = 0;
			if (!StringUtils.isEmpty(obj.getContract_type_fk())) {
				qry = qry + "where contract_category_fk = ? ";
				arraSize++;
			}
			qry = qry + "group by f.issue_category_fk,issues_related_to order by f.issue_category_fk ";
			Object[] pValues = new Object[arraSize];
			int i = 0;
			if (!StringUtils.isEmpty(obj.getContract_type_fk())) {
				pValues[i++] = obj.getContract_type_fk();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList; 
	}

	@Override
	public List<Issue> getIssuesPriorityList() throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "select priority from issue_priority";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getContractsListFilter(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT contract_id_fk,c.contract_id,contract_name,contract_short_name " + "from issue i "
					+ "LEFT OUTER JOIN contract c ON i.contract_id_fk  = c.contract_id "
					+ "LEFT OUTER JOIN department d ON c.department_fk  = d.department "
					+ "left join [user] u on c.hod_user_id_fk = u.user_id "
					+ "where contract_id_fk is not null and contract_id_fk <> '' ";
			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ?";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and u.designation = ?";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry
						+ " and (i.responsible_person = ? or i.escalated_to = ? or c.hod_user_id_fk = ? or c.dy_hod_user_id_fk = ? or created_by_user_id_fk = ? "
						+ "or contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ?) "
						+ "or location in(select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ?))";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}

			qry = qry + " GROUP BY contract_id_fk,c.contract_id,contract_name,contract_short_name";

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;

	}

	@Override
	public List<Issue> getDepartmentsListFilter(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT c.department_fk,department,department_name " + "from issue i "
					+ "LEFT JOIN contract c on i.contract_id_fk = c.contract_id "
					+ "LEFT OUTER JOIN department d ON c.department_fk  = d.department "
					+ "left join [user] u on c.hod_user_id_fk = u.user_id "
					+ "where c.department_fk is not null and c.department_fk <> '' ";
			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ?";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and u.designation = ?";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry
						+ " and (i.responsible_person = ? or i.escalated_to = ? or c.hod_user_id_fk = ? or c.dy_hod_user_id_fk = ? or created_by_user_id_fk = ? "
						+ "or contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ?) "
						+ "or location in(select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ?))";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}

			qry = qry + " GROUP BY c.department_fk,department,department_name";

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getCategoryListFilter(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT category_fk " + "from issue i "
					+ "LEFT JOIN contract c on i.contract_id_fk = c.contract_id "
					+ "LEFT OUTER JOIN department d ON c.department_fk  = d.department "
					+ "left join [user] u on c.hod_user_id_fk = u.user_id "
					+ " where category_fk is not null and category_fk <> '' ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ?";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and u.designation = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry
						+ " and (i.responsible_person = ? or i.escalated_to = ? or c.hod_user_id_fk = ? or c.dy_hod_user_id_fk = ? or created_by_user_id_fk = ? "
						+ "or contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ?) "
						+ "or location in(select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ?))";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}
			qry = qry + " GROUP BY category_fk ";

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getResponsiblePersonsListFilter(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT responsible_person,u.user_name " + "from issue i "
					+ "LEFT JOIN contract c on i.contract_id_fk = c.contract_id "
					+ "LEFT OUTER JOIN department d ON c.department_fk  = d.department "
					+ "left join [user] u on i.responsible_person = u.designation "
					+ "where responsible_person is not null and responsible_person <> '' ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ?";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getResponsible_person())) {
				qry = qry + " and responsible_person = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry
						+ " and (i.responsible_person = ? or i.escalated_to = ? or c.hod_user_id_fk = ? or c.dy_hod_user_id_fk = ? or created_by_user_id_fk = ? "
						+ "or contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ?) "
						+ "or location in(select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ?))";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}
			qry = qry + " GROUP BY responsible_person,u.user_name ";

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getResponsible_person())) {
				pValues[i++] = obj.getResponsible_person();
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getStatusListFilter(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT status_fk " + "from issue i "
					+ "LEFT JOIN contract c on i.contract_id_fk = c.contract_id "
					+ "LEFT OUTER JOIN department d ON c.department_fk  = d.department "
					+ "left join [user] u on c.hod_user_id_fk = u.user_id "
					+ "where status_fk is not null and status_fk <> '' ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ?";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and u.designation = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry
						+ " and (i.responsible_person = ? or i.escalated_to = ? or c.hod_user_id_fk = ? or c.dy_hod_user_id_fk = ? or created_by_user_id_fk = ? "
						+ "or contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ?) "
						+ "or location in(select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ?))";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}
			qry = qry + " GROUP BY status_fk ";

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getHODListFilterInIssue(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT hod_user_id_fk,u.designation " + "from issue i "
					+ "LEFT JOIN contract c on i.contract_id_fk = c.contract_id "
					+ "LEFT OUTER JOIN department d ON c.department_fk  = d.department "
					+ "left join [user] u on c.hod_user_id_fk = u.user_id "
					+ "where hod_user_id_fk is not null and hod_user_id_fk <> '' ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ?";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and u.designation = ?";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry
						+ " and (i.responsible_person = ? or i.escalated_to = ? or c.hod_user_id_fk = ? or c.dy_hod_user_id_fk = ? or created_by_user_id_fk = ? "
						+ "or contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ?) "
						+ "or location in(select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ?))";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}
			qry = qry + " group by u.designation,hod_user_id_fk ORDER BY case when u.designation='ED Civil' then 1 " + 
					"   when u.designation='CPM I' then 2 " + 
					"   when u.designation='CPM II' then 3" + 
					"   when u.designation='CPM III' then 4 " + 
					"   when u.designation='CPM V' then 5" + 
					"   when u.designation='CE' then 6 " + 
					"   when u.designation='ED S&T' then 7 " + 
					"   when u.designation='CSTE' then 8" + 
					"   when u.designation='GM Electrical' then 9" + 
					"   when u.designation='CEE Project I' then 10" + 
					"   when u.designation='CEE Project II' then 11" + 
					"   when u.designation='ED Finance & Planning' then 12" + 
					"   when u.designation='AGM Civil' then 13" + 
					"   when u.designation='DyCPM Civil' then 14" + 
					"   when u.designation='DyCPM III' then 15" + 
					"   when u.designation='DyCPM V' then 16" + 
					"   when u.designation='DyCE EE' then 17" + 
					"   when u.designation='DyCE Badlapur' then 18" + 
					"   when u.designation='DyCPM Pune' then 19" + 
					"   when u.designation='DyCE Proj' then 20" + 
					"   when u.designation='DyCEE I' then 21" + 
					"   when u.designation='DyCEE Projects' then 22" + 
					"   when u.designation='DyCEE PSI' then 23" + 
					"   when u.designation='DyCSTE I' then 24" + 
					"   when u.designation='DyCSTE IT' then 25" + 
					"   when u.designation='DyCSTE Projects' then 26" + 
					"   when u.designation='XEN Consultant' then 27" + 
					"   when u.designation='AEN Adhoc' then 28" + 
					"   when u.designation='AEN Project' then 29" + 
					"   when u.designation='AEN P-Way' then 30" + 
					"   when u.designation='AEN' then 31" + 
					"   when u.designation='Sr Manager Signal' then 32 " + 
					"   when u.designation='Manager Signal' then 33" + 
					"   when u.designation='Manager Civil' then 34 " + 
					"   when u.designation='Manager OHE' then 35" + 
					"   when u.designation='Manager GS' then 36" + 
					"   when u.designation='Manager Finance' then 37" + 
					"   when u.designation='Planning Manager' then 38" + 
					"   when u.designation='Manager Project' then 39" + 
					"   when u.designation='Manager' then 40 " + 
					"   when u.designation='SSE' then 41" + 
					"   when u.designation='SSE Project' then 42" + 
					"   when u.designation='SSE Works' then 43" + 
					"   when u.designation='SSE Drg' then 44" + 
					"   when u.designation='SSE BR' then 45" + 
					"   when u.designation='SSE P-Way' then 46" + 
					"   when u.designation='SSE OHE' then 47" + 
					"   when u.designation='SPE' then 48" + 
					"   when u.designation='PE' then 49" + 
					"   when u.designation='JE' then 50" + 
					"   when u.designation='Demo-HOD-Elec' then 51" + 
					"   when u.designation='Demo-HOD-Engg' then 52" + 
					"   when u.designation='Demo-HOD-S&T' then 53" + 
					"" + 
					"   end asc" ;


			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}

			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getIssuesList(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "select issue_id,contract_id_fk,d.department_name,c.contract_short_name,title,FORMAT(date,'dd-MMM-yy') AS date,location,reported_by,responsible_person,c.department_fk,"
					+ "priority_fk,category_fk,status_fk,corrective_measure,FORMAT(resolved_date,'dd-MMM-yy') AS resolved_date,escalated_to,i.remarks,contract_name,project_id_fk,project_name,i.zonal_railway_fk,r.railway_name,"
					+ "u2.designation as responsible_person_designation,u3.designation as escalated_to_designation,railway_name,FORMAT(assigned_date,'dd-MMM-yy') AS assigned_date,"
					+ "c.hod_user_id_fk,c.dy_hod_user_id_fk,created_by_user_id_fk,other_organization,FORMAT(i.created_date,'dd-MMM-yy') AS created_date,FORMAT(escalation_date,'dd-MMM-yy') AS escalation_date,"
					+ "other_org_resposible_person_name,other_org_resposible_person_designation,description,i.modified_by,FORMAT(i.modified_date,'dd-MM-yyyy') as modified_date " 
					+ "from issue i "
					+ "left outer join [user] u2 on i.responsible_person = u2.user_id "
					+ "left outer join [user] u3 on i.escalated_to = u3.user_id "
					+ "LEFT OUTER JOIN contract c ON i.contract_id_fk  = c.contract_id "
					+ "left outer join [user] u on c.hod_user_id_fk = u.user_id "
					+ "LEFT OUTER JOIN project p ON c.project_id_fk  = p.project_id "
					+ "LEFT OUTER JOIN department d ON c.department_fk  = d.department "
					+ "LEFT OUTER JOIN railway r ON i.zonal_railway_fk  = r.railway_id "
					+ "where issue_id is not null ";
			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and u.designation = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ?";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry
						+ " and (i.responsible_person = ? or i.escalated_to = ? or c.hod_user_id_fk = ? or c.dy_hod_user_id_fk = ? or created_by_user_id_fk = ? "
						+ "or contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ?) "
						+ "or location in(select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ?))";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}

			if (!StringUtils.isEmpty(obj) && !CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getProjectsListForIssueForm(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "";
			
			if (CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					|| CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = "select distinct project_id_fk,project_name "
						+ "from contract c "
						+ "LEFT JOIN project p ON c.project_id_fk = p.project_id "
						+ "where contract_status_fk IN('In Progress','Not Started') ";
			} else {
				qry = "SELECT distinct project_id_fk,project_name "
						+ "FROM contract c "
						+ "LEFT JOIN project p ON c.project_id_fk = p.project_id "
						+ "where contract_status_fk IN('In Progress','Not Started') ";
			}
			int arrSize = 0;

			if (!CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + "AND (hod_user_id_fk = ? or dy_hod_user_id_fk = ? " 
						+ "or contract_id in(select contract_id_fk from contract_executive where executive_user_id_fk = ?) " 
						+ "or contract_id in(select contract_id_fk from structure_contract_responsible_people where responsible_people_id_fk = ?) " 
						+ "or contract_id in(select contract_id_fk from fob_contract_responsible_people where fob_id_fk in(select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ?)) "
						+ ")"; 
						
				arrSize = arrSize + 5;
			}

			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if (!CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}
			qry = qry + " order by c.project_id_fk asc";

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getContractsListForIssueForm(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "";
			if (CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					|| CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = "select contract_id as contract_id_fk,contract_name,contract_short_name,project_id_fk,"
						+ "hod_user_id_fk,dy_hod_user_id_fk,contract_type_fk " 
						+ "from contract c "
						+ "where contract_status_fk IN('In Progress','Not Started','Not Awarded') ";
			} else {
				qry = "SELECT contract_id as contract_id_fk,contract_name,contract_short_name,project_id_fk,"
						+ "hod_user_id_fk,dy_hod_user_id_fk,contract_type_fk "
						+ "FROM contract c "
						+ "where contract_status_fk IN('In Progress','Not Started','Not Awarded') ";
			}
			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}

			if (!CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " AND (hod_user_id_fk = ? or dy_hod_user_id_fk = ? " 
						+ "or contract_id in(select contract_id_fk from contract_executive where executive_user_id_fk = ?) " 
						+ "or contract_id in(select contract_id_fk from structure_contract_responsible_people where responsible_people_id_fk = ?) " 
						+ "or contract_id in(select contract_id_fk from fob_contract_responsible_people where fob_id_fk in(select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ?)) "
						+ ")"; 
						
				arrSize = arrSize + 5;
			}

			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!CommonConstants.USER_TYPE_MANAGEMENT.equals(obj.getUser_type())
					&& !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}
			qry = qry + " group by contract_id,contract_name,contract_short_name,project_id_fk,hod_user_id_fk,dy_hod_user_id_fk,contract_type_fk order by contract_id asc ";
			
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));

		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getIssuesStatusList() throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "select status from issue_status";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getIssueTitlesList(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "select short_description,issues_related_to from issue_category_title ";
			int arraSize = 0;
			if (!StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + "where issue_category_fk = ? ";
				arraSize++;
			}
			qry = qry + "group by short_description,issues_related_to order by short_description ";
			Object[] pValues = new Object[arraSize];
			int i = 0;
			if (!StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getDepartmentList() throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "select department as department_fk,department_name from department where department <> 'MGMT'";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getRailwayList() throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT railway_id,railway_name from railway WHERE railway_id <> 'Con' and railway_id='MRVC' ORDER BY case when railway_id='MRVC' then 1" + 
					"when railway_id='CR' then 2 when railway_id='WR' then 3 when railway_id='Others' then 4 end asc";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getReportedByList() throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT user_id as reported_by_user_id,designation as reported_by_designation " + "FROM [user] "
					+ "where user_type_fk = ? group by user_id,designation order by designation";

			Object[] pValues = new Object[] { CommonConstants.USER_TYPE_HOD };

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getResponsiblePersonList(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT user_id as responsible_person_user_id,designation as responsible_person_designation "
					+ "FROM [user] " + "where user_type_fk = ? ";
			int arrSize = 1;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_name())) {
				qry = qry + "and department_fk = (select department from department where department_name = ?)";
				arrSize++;
			}
			qry = qry + "group by user_id,designation order by designation";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = CommonConstants.USER_TYPE_DYHOD;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_name())) {
				pValues[i++] = obj.getDepartment_name();
			}

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getEscalatedToList() throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT user_id as escalated_to_user_id,designation as escalated_to_designation "
					+ "FROM [user] "
					+ "where (user_type_fk = ? or user_type_fk = ?) group by user_id,designation order by designation";

			Object[] pValues = new Object[] { CommonConstants.USER_TYPE_HOD, CommonConstants.USER_TYPE_MANAGEMENT };

			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getOtherOrganizationsList() throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT issue_other_organization as other_organization from issue_other_organization";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getIssueFileTypes() throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "SELECT issue_file_type from issue_file_type";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getStructures(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "select distinct structure from activities_view ";
			int arraSize = 0;
			if (!StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + "where contract_id = ? ";
				arraSize++;
			}
			Object[] pValues = new Object[arraSize];
			int i = 0;
			if (!StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getComponents(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "select distinct component from activities_view ";
			int arraSize = 0;
			if (!StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + "where contract_id = ? ";
				arraSize++;
			}
			Object[] pValues = new Object[arraSize];
			int i = 0;
			if (!StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getStructureListForIssue(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "select distinct structure from p6_activities a " + 
					"left join structure s on s.structure_id=a.structure_id_fk  ";
			int arraSize = 0;
			if (!StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + "where a.contract_id_fk = ? ";
				arraSize++;
			}
			Object[] pValues = new Object[arraSize];
			int i = 0;
			if (!StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Issue> getComponentListForIssue(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "select distinct component from p6_activities a " + 
					"left join structure s on s.structure_id=a.structure_id_fk  where 0=0 ";
			int arraSize = 0;
			if (!StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + "and a.contract_id_fk = ? ";
				arraSize++;
			}
			if (!StringUtils.isEmpty(obj.getStructure())) {
				qry = qry + "and structure = ? ";
				arraSize++;
			}
			
			Object[] pValues = new Object[arraSize];
			int i = 0;
			if (!StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}

			if (!StringUtils.isEmpty(obj.getStructure())) {
				pValues[i++] = obj.getStructure();
			}
			
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

}
