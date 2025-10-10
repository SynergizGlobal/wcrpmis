package com.wcr.wcrbackend.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Alerts;

@Repository
public class AlertsRepository implements IAlertsRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Alerts> getAdminForms(Alerts obj) throws Exception{
		List<Alerts> alertTypes = new ArrayList<Alerts>();
		try {

			String qry = "select alert_type_fk " + "from alerts a ";
			if (!"IT Admin".equals(obj.getUser_role_name())) {
				qry = qry + "left join alerts_user au on au.alerts_id_fk = a.alert_id ";
			}

			qry = qry + "left join contract c on a.contract_id = c.contract_id "
					//+ "left join work w on c.work_id_fk = w.work_id "
					//+ "left outer join work w1 on w1.work_id = a.work_id "
					+ "left join contractor ctr on c.contractor_id_fk = ctr.contractor_id "
					+ "LEFT JOIN [user] u on c.hod_user_id_fk = u.user_id " + "where alert_status = ? and count <> 0 ";

			int arrSize = 1;
			if (!"IT Admin".equals(obj.getUser_role_name())) {
				qry = qry + " and au.user_id_fk = ? ";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_id_fk())) {
				qry = qry + " (and c.work_id_fk = ? or w1.work_id = ? )";
				arrSize++;
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and a.contract_id = ?";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				qry = qry + " and c.contractor_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getAlert_type_fk())) {
				qry = qry + " and a.alert_type_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and u.designation = ? ";
				arrSize++;
			}

			// qry = qry + " and a.alert_type_fk not in ('Issue','Risk')";

			qry = qry + " group by alert_type_fk order by alert_type_fk ASC";

			Object[] pValues = new Object[arrSize];
			int i = 0;

			pValues[i++] = "Active";
			if (!"IT Admin".equals(obj.getUser_role_name())) {
				pValues[i++] = obj.getUser_id();
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_id_fk())) {
				pValues[i++] = obj.getWork_id_fk();
				pValues[i++] = obj.getWork_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getAlert_type_fk())) {
				pValues[i++] = obj.getAlert_type_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			alertTypes = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Alerts>(Alerts.class));

		} catch (Exception e) {
			throw new Exception(e);
		}
		return alertTypes;
	}

}
