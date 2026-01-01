package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.P6Data;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.DBConnectionHandler;

@Repository
public class P6NewDataDaoImpl implements P6NewDataDao {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ; 
	
	@Autowired
	FormsHistoryDao formsHistoryDao;

	@Override
	public List<P6Data> getContractsList(P6Data obj) throws Exception {
		List<P6Data> objsList = null;
		try {
			String qry ="SELECT contract_id,contract_name,contract_short_name FROM contract c "
					+ "left join project p on c.project_id_fk = p.project_id where contract_id is not null ";
				;
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + "and project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + "and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? or"
						+ " contract_id in(select contract_id_fk from contract_executive where  executive_user_id_fk = ?) "
						+ " or contract_id in(select contract_id_fk from structure_contract_responsible_people where  responsible_people_id_fk = ?) )";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}
				
			
			Object[] pValues = new Object[arrSize]; 
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			
			}
			objsList = jdbcTemplate.query( qry,pValues,new BeanPropertyRowMapper<P6Data>(P6Data.class));	
		}catch(Exception e){ 
			throw new Exception(e.getMessage());
		}
		return objsList;
	}

	@Override
	public List<P6Data> getFobList(P6Data obj) throws Exception {
		List<P6Data> objsList = null;
		try {
			/*String qry ="SELECT fob_id,fob_name FROM fob where contract_id_fk = ?";*/
			
			String qry ="SELECT fob_id,fob_name FROM fob "
					+ "where fob_id IN (select fob_id_fk from fob_contract_responsible_people where contract_id_fk = ? group by fob_id_fk) ";
			
			objsList = jdbcTemplate.query( qry,new Object[]{obj.getContract_id_fk()}, new BeanPropertyRowMapper<P6Data>(P6Data.class));	
		}catch(Exception e){ 
		throw new Exception(e.getMessage());
		}
		return objsList;
	}
	
	@Override
	public List<P6Data> getContractsListFilter(P6Data obj) throws Exception {
		List<P6Data> objsList = null;
		try {
			String qry ="SELECT contract_id_fk as contract_id,contract_name,contract_short_name FROM p6_data "
					+ "LEFT OUTER JOIN contract ON contract_id_fk = contract_id "
					+ "WHERE (fob_id_fk is null OR fob_id_fk = '') AND contract_id_fk is not null and contract_id_fk <> '' and contract_id not like '%MS%' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + "and contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getFob_id())) {
				qry = qry + "and fob_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpload_type())) {
				qry = qry + "and upload_type = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + "and soft_delete_status_fk = ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize]; 
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getFob_id())) {
				pValues[i++] = obj.getFob_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpload_type())) {
				pValues[i++] = obj.getUpload_type();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			qry = qry + "GROUP BY contract_id_fk,contract_name,contract_short_name ";
			objsList = jdbcTemplate.query( qry,pValues,new BeanPropertyRowMapper<P6Data>(P6Data.class));	
		}catch(Exception e){ 
		throw new Exception(e.getMessage());
		}
		return objsList;
	}

	@Override
	public List<P6Data> getFobListFilter(P6Data obj) throws Exception {
		List<P6Data> objsList = null;
		try {
			String qry ="SELECT fob_id_fk as fob_id,fob_name FROM p6_data p "
					+ "LEFT OUTER JOIN fob f ON fob_id_fk = fob_id "
					+ "where (fob_id_fk is null OR fob_id_fk = '') AND and fob_id <> '' ";
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + "and p.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getFob_id())) {
				qry = qry + "and fob_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpload_type())) {
				qry = qry + "and upload_type = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + "and soft_delete_status_fk = ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize]; 
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getFob_id())) {
				pValues[i++] = obj.getFob_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpload_type())) {
				pValues[i++] = obj.getUpload_type();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			qry = qry + "GROUP BY fob_id_fk";
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<P6Data>(P6Data.class));	
		}catch(Exception e){ 
		throw new Exception(e.getMessage());
		}
		return objsList;
	}

	@Override
	public List<P6Data> getUploadTypesFilter(P6Data obj) throws Exception {
		List<P6Data> objsList = null;
		try {
			String qry ="SELECT upload_type FROM p6_data "
					+ "where (fob_id_fk is null OR fob_id_fk = '') AND upload_type is not null and upload_type <> '' ";
					
					int arrSize = 0;
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
						qry = qry + "and contract_id_fk = ? ";
						arrSize++;
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getFob_id())) {
						qry = qry + "and fob_id_fk = ? ";
						arrSize++;
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpload_type())) {
						qry = qry + "and upload_type = ? ";
						arrSize++;
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
						qry = qry + "and soft_delete_status_fk = ? ";
						arrSize++;
					}
					Object[] pValues = new Object[arrSize]; 
					int i = 0;
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
						pValues[i++] = obj.getContract_id();
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getFob_id())) {
						pValues[i++] = obj.getFob_id();
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpload_type())) {
						pValues[i++] = obj.getUpload_type();
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
						pValues[i++] = obj.getStatus_fk();
					}
					qry = qry + "GROUP BY upload_type";
			
			objsList = jdbcTemplate.query( qry,pValues,new BeanPropertyRowMapper<P6Data>(P6Data.class));	
		}catch(Exception e){ 
		throw new Exception(e.getMessage());
		}
		return objsList;
	}


	@Override
	public List<P6Data> getStatusListFilter(P6Data obj) throws Exception {
		List<P6Data> objsList = null;
		try {
			String qry ="SELECT soft_delete_status_fk FROM p6_data "
					+ "where (fob_id_fk is null OR fob_id_fk = '') AND soft_delete_status_fk is not null and soft_delete_status_fk <> '' ";
					int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + "and contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getFob_id())) {
				qry = qry + "and fob_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpload_type())) {
				qry = qry + "and upload_type = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + "and soft_delete_status_fk = ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize]; 
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getFob_id())) {
				pValues[i++] = obj.getFob_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpload_type())) {
				pValues[i++] = obj.getUpload_type();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			qry = qry + "GROUP BY soft_delete_status_fk";
			
			objsList = jdbcTemplate.query( qry,pValues,new BeanPropertyRowMapper<P6Data>(P6Data.class));	
		}catch(Exception e){ 
		throw new Exception(e.getMessage());
		}
		return objsList;
	}
	

	@Override
	public String updateP6Activities(List<P6Data> p6dataList,P6Data pobj) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String count = "";
		int counts = 0;
		String lineNos = null;
		try {
			con = dataSource.getConnection();
			
			con.setAutoCommit(false);
			String updateQry ="update p6_data set soft_delete_status_fk  = ? "
					+ "where contract_id_fk = ?";
			
			if(!StringUtils.isEmpty((pobj.getFob_id_fk()))) {
				updateQry = updateQry + " and fob_id_fk = ?";
			}
			
			stmt = con.prepareStatement(updateQry);
			int p = 1;
			stmt.setString(p++,(CommonConstants.INACTIVE));
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getContract_id_fk()))?pobj.getContract_id_fk():null);
			if(!StringUtils.isEmpty((pobj.getFob_id_fk()))) {
				stmt.setString(p++,pobj.getFob_id_fk());
			}
			stmt.executeUpdate();	
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
	
			String activityDataqry = "INSERT INTO p6_data (contract_id_fk, fob_id_fk,upload_type,data_date, "
					+ "soft_delete_status_fk, p6_file_path, uploaded_by_user_id_fk, uploaded_date)"
					+ " VALUES (?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
			
			stmt = con.prepareStatement(activityDataqry);
			p = 1;
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getContract_id_fk()))?pobj.getContract_id_fk():null);
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getFob_id_fk()))?pobj.getFob_id_fk():null);
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getUpload_type()))?pobj.getUpload_type():null);
			stmt.setString(p++,!StringUtils.isEmpty(pobj.getData_date())?pobj.getData_date():null);
			stmt.setString(p++,CommonConstants.ACTIVE);
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getP6_file_path()))?pobj.getP6_file_path():null);
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getUploaded_by_user_id_fk()))?pobj.getUploaded_by_user_id_fk():null);
			stmt.executeUpdate();
			
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
			
			String updateActivitiesQry ="UPDATE p6_activities set "
					+ "status_fk = ?,start = ?,finish = ?,float =  ? ,p6_activity_name = ?";
					if(pobj.getIsRevised().contentEquals("Yes")) {
						updateActivitiesQry = updateActivitiesQry + ",baseline_start = ?,baseline_finish = ? ";

					}
			updateActivitiesQry	= updateActivitiesQry+ ",modified_by_user_id_fk = ?, modified_date = CURRENT_TIMESTAMP where  contract_id_fk = ? and task_code = ? ";
			
			stmt = con.prepareStatement(updateActivitiesQry);
			int lineNo = 3;
			for (P6Data obj : p6dataList) {
				p = 1;				
				
				stmt.setString(p++,!StringUtils.isEmpty((obj.getStatus_fk()))?obj.getStatus_fk():null);

				stmt.setString(p++,!StringUtils.isEmpty(obj.getStart())?obj.getStart():null);
				stmt.setString(p++,!StringUtils.isEmpty(obj.getFinish())?obj.getFinish():null);
				stmt.setString(p++,!StringUtils.isEmpty((obj.getP6_float()))?obj.getP6_float():null);	
				stmt.setString(p++,!StringUtils.isEmpty((obj.getP6_activity_name()))?obj.getP6_activity_name():null);
				if(pobj.getIsRevised().contentEquals("Yes")) {
					//stmt.setString(p++,!StringUtils.isEmpty((obj.getP6_activity_name()))?obj.getP6_activity_name():null);
					stmt.setString(p++,!StringUtils.isEmpty(obj.getBaseline_start())?obj.getBaseline_start():null);
					stmt.setString(p++,!StringUtils.isEmpty(obj.getBaseline_finish())?obj.getBaseline_finish():null);
					
				}
				stmt.setString(p++,!StringUtils.isEmpty((pobj.getModified_by_user_id_fk()))?pobj.getModified_by_user_id_fk():null);
				stmt.setString(p++,!StringUtils.isEmpty((pobj.getContract_id_fk()))?pobj.getContract_id_fk():null);
				stmt.setString(p++,!StringUtils.isEmpty((obj.getP6_task_code()))?obj.getP6_task_code():null);
				stmt.addBatch();
				
			} 
			int[] c = stmt.executeBatch();
			String noErr = "",err = "";
			for (int i = 0; i < c.length; i++) {
				if((c[i] > 0)) {
					counts = counts + c[i];
					noErr = Integer.toString(counts);
				}else {
					if(lineNos != null) {
						lineNos = lineNos +", "+ lineNo;
					}else {
						lineNos = lineNo+"";
					}
					if(!lineNos.contains(",")) {
						err = "^ row "+lineNos+ " Not Updated Because Contract or Activity ID Missmatch";
					}else {
						err = "^ rows "+lineNos+ " Not Updated Because Contract or Activity ID Missmatch";
					}
			    }
				lineNo++;
			}
			count = noErr + err;
			con.commit();
			
			FormHistory formHistory = new FormHistory();
			formHistory.setCreated_by_user_id_fk(pobj.getCreated_by_user_id_fk());
			formHistory.setUser(pobj.getDesignation()+" - "+pobj.getUser_name());
			formHistory.setModule_name_fk("Execution &  Monitoring");
			formHistory.setForm_name("P6 Data");
			formHistory.setForm_action_type("Update");
			formHistory.setForm_details("Data date updated and "+ counts + " activities updated");
			//formHistory.setWork(pobj.getWork_id_fk());
			formHistory.setContract_id_fk(pobj.getContract_id_fk());
			
			boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
			
		}catch(Exception e){ 
			e.printStackTrace();
			con.rollback();
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, rs);
		}
		return count;
	
	}

	@Override
	public List<P6Data> getActivityDataList(P6Data obj) throws Exception {
		List<P6Data> objsList = null;
		try {
			String qry ="select contract_id_fk, fob_id_fk,upload_type, FORMAT(data_date,'dd-MM-yyyy') as data_date, soft_delete_status_fk,"
					+ " p6_file_path, uploaded_by_user_id_fk, FORMAT(uploaded_date,'dd-MM-yyyy  hh:mm ss') uploaded_date  "
					+ "from p6_data "
					+ "WHERE (fob_id_fk is null OR fob_id_fk = '')  ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + "and contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getFob_id_fk())) {
				qry = qry + "and fob_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpload_type())) {
				qry = qry + "and upload_type = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + "and soft_delete_status_fk = ? ";
				arrSize++;
			}
			qry = qry + " ORDER BY  FORMAT(uploaded_date,'yyyy-MM-dd HH:mm ss') desc ";
			Object[] pValues = new Object[arrSize]; 
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getFob_id_fk())) {
				pValues[i++] = obj.getFob_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUpload_type())) {
				pValues[i++] = obj.getUpload_type();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<P6Data>(P6Data.class));	
		}catch(Exception e){ 
			throw new Exception(e.getMessage());
		}finally {
			
		}
		return objsList;
	}


	@Override
	public String uploadP6WBSActivities(List<P6Data> wbsList, List<P6Data> activitiesList, P6Data pobj) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rs = null;
		String counts = null;
		String rows = null;
		String norows = null;
		int wbsCount = 0;
		int wbsCountUpdate = 0;
		int activitiesCount = 3;
		int rowNo = 3;
		boolean flag = false;
		   int recordsUploaded = 0;
		    int recordsUpdated = 0;
		try {
			con = dataSource.getConnection();
			
			con.setAutoCommit(false);
			String updateQry ="update p6_data set soft_delete_status_fk  = ? "
					+ "where contract_id_fk = ?";
			
			if(!StringUtils.isEmpty((pobj.getFob_id_fk()))) {
				updateQry = updateQry + " and fob_id_fk = ?";
			}
			
			stmt = con.prepareStatement(updateQry);
			int p = 1;
			stmt.setString(p++,(CommonConstants.INACTIVE));
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getContract_id_fk()))?pobj.getContract_id_fk():null);
			if(!StringUtils.isEmpty((pobj.getFob_id_fk()))) {
				stmt.setString(p++,pobj.getFob_id_fk());
			}
			
			stmt.executeUpdate();	
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
	
			String activityDataqry = "INSERT INTO p6_data (contract_id_fk, fob_id_fk,upload_type, data_date, "
					+ "soft_delete_status_fk, p6_file_path, uploaded_by_user_id_fk, uploaded_date)"
					+ " VALUES (?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
			
			stmt = con.prepareStatement(activityDataqry);
			p = 1;
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getContract_id_fk()))?pobj.getContract_id_fk():null);
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getFob_id_fk()))?pobj.getFob_id_fk():null);
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getUpload_type()))?pobj.getUpload_type():null);
			stmt.setString(p++,!StringUtils.isEmpty(pobj.getData_date())?pobj.getData_date():null);
			stmt.setString(p++,CommonConstants.ACTIVE);
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getP6_file_path()))?pobj.getP6_file_path():null);
			stmt.setString(p++,!StringUtils.isEmpty((pobj.getUploaded_by_user_id_fk()))?pobj.getUploaded_by_user_id_fk():null);
			stmt.executeUpdate();
			
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
			int row =3;
			String wbsQry = "INSERT INTO p6_activities (task_code, contract_id_fk, structure_id_fk, p6_activity_name, from_structure_id, to_structure_id, "
					+ "section, line, component, component_id, baseline_start, baseline_finish, start, finish, float, status_fk, unit, scope, "
					+ "completed, weightage, component_details, remarks, created_by_user_id_fk,original_duration,created_date)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CAST(? as decimal(18,2)),CAST(? as decimal(18,2)),CAST(? as decimal(18,2)),?,?,?,?,CURRENT_TIMESTAMP) ";
			
			String wbsUpdateQry = "Update p6_activities set structure_id_fk = ?, p6_activity_name = ?, from_structure_id = ?, to_structure_id = ?, "
					+ "section = ?, line = ?, component = ?, component_id = ?, baseline_start = ?, baseline_finish = ?, start = ?, finish = ?, float = ?, status_fk = ?, unit = ?, scope = CAST(? as decimal(18,2)), "
					+ "completed = CAST(? as decimal(18,2)), weightage = CAST(? as decimal(18,2)), component_details = ?, remarks = ?, modified_by_user_id_fk = ?,original_duration = ?, modified_date = CURRENT_TIMESTAMP where task_code = ? and contract_id_fk = ?";
			stmt = con.prepareStatement(wbsQry);
			stmtUpdate = con.prepareStatement(wbsUpdateQry);

			int loop=0;
			int loop1=0;
			
			
//			List<P6Data> tempList = new ArrayList<P6Data>();
//			tempList.add(activitiesList.get(0));
//			tempList.add(activitiesList.get(1));
//			tempList.add(activitiesList.get(2));
//			tempList.add(activitiesList.get(3));
//			tempList.add(activitiesList.get(4));
//			
//
//			tempList.add(activitiesList.get(activitiesList.size() - 2));
//			tempList.add(activitiesList.get(activitiesList.size() - 1));
//			
//			for (P6Data obj : tempList) {
			for (P6Data obj : activitiesList) {
						PreparedStatement pstmt = con.prepareStatement("SELECT p6_activity_id as p6_activity_id FROM p6_activities WHERE"
								+ " task_code = ? and contract_id_fk = ?");
						p = 1;
						pstmt.setString(p++,obj.getP6_task_code());
						pstmt.setString(p++,pobj.getContract_id_fk());
//						System.out.println("----- : SELECT p6_activity_id as p6_activity_id FROM p6_activities WHERE task_code = '"+obj.getP6_task_code()+"'"
//								+ " and contract_id_fk = '"+pobj.getContract_id_fk()+"'");
						
						
						
						rs = pstmt.executeQuery();	
						
						
						
						String p6_activity_id = null;
						if(rs.next()) {
							p6_activity_id = rs.getString("p6_activity_id");
						}
						DBConnectionHandler.closeJDBCResoucrs(null, pstmt, rs);
						PreparedStatement structureStmt = con.prepareStatement("SELECT top 1 structure_id as structure_id_fk FROM structure WHERE project_id_fk=(select project_id_fk from contract where contract_id = '"+pobj.getContract_id_fk()+"') and structure_type_fk = ? and structure = ? and structure_id in(select structure_id_fk from structure_contract_responsible_people where contract_id_fk='"+pobj.getContract_id_fk()+"')");
						p = 1;
						structureStmt.setString(p++,obj.getStructure_type_fk());
						structureStmt.setString(p++,obj.getStructure());
						
//						System.out.println("==== : SELECT top 1 structure_id as structure_id_fk FROM structure WHERE "
//								+ "work_id_fk=(select work_id_fk from contract where contract_id = '"+pobj.getContract_id_fk()+"') "
//										+ "and structure_type_fk = '"+obj.getStructure_type_fk()+"' and structure = '"+obj.getStructure()+"' "
//												+ "and structure_id in(select structure_id_fk from structure_contract_responsible_people "
//												+ "where contract_id_fk='"+pobj.getContract_id_fk()+"')");
						rs = structureStmt.executeQuery();		
						String val = null;
						if(rs.next()) {
							val = rs.getString("structure_id_fk");
						}
						DBConnectionHandler.closeJDBCResoucrs(null, structureStmt, rs);
						obj.setStructure_id_fk(val);
						String ContractIs_Available = "";
						if(!StringUtils.isEmpty(obj.getStructure_id_fk())) {
							DBConnectionHandler.closeJDBCResoucrs(null, pstmt, rs);
							PreparedStatement contarctStmt = con.prepareStatement("SELECT contract_id_fk as contract_id_fk FROM structure_contract_responsible_people WHERE contract_id_fk = ? and structure_id_fk = ?");
							p = 1;
							contarctStmt.setString(p++,pobj.getContract_id_fk());
							contarctStmt.setString(p++,val);
							rs = contarctStmt.executeQuery();		
							
							if(rs.next()) {
								ContractIs_Available = rs.getString("contract_id_fk");
							}
							DBConnectionHandler.closeJDBCResoucrs(null, contarctStmt, rs);
						}
				
						if(!StringUtils.isEmpty(ContractIs_Available) ) {
							if( StringUtils.isEmpty(p6_activity_id) && p6_activity_id == null) {
								p = 1;
								System.out.println(activitiesCount++);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getP6_task_code()))?obj.getP6_task_code():null);
								stmt.setString(p++,!StringUtils.isEmpty((pobj.getContract_id_fk()))?pobj.getContract_id_fk():null);
								stmt.setString(p++,obj.getStructure_id_fk());
								stmt.setString(p++,!StringUtils.isEmpty((obj.getP6_activity_name()))?obj.getP6_activity_name():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getFrom_structure_id()))?obj.getFrom_structure_id():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getTo_structure_id()))?obj.getTo_structure_id():null);
								
								stmt.setString(p++,!StringUtils.isEmpty((pobj.getSection()))?pobj.getSection():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getLine()))?obj.getLine():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getComponent()))?obj.getComponent():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getComponent_id()))?obj.getComponent_id():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getBaseline_start()))?obj.getBaseline_start():null);
								
								stmt.setString(p++,!StringUtils.isEmpty((obj.getBaseline_finish()))?obj.getBaseline_finish():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getStart()))?obj.getStart():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getFinish()))?obj.getFinish():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getP6_float()))?obj.getP6_float():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getStatus_fk()))?obj.getStatus_fk():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getUnit()))?obj.getUnit():null);
								
								stmt.setString(p++,!StringUtils.isEmpty((obj.getScope()))?obj.getScope():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getCompleted()))?obj.getCompleted():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getWeightage()))?obj.getWeightage():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getComponent_details()))?obj.getComponent_details():null);
								stmt.setString(p++,!StringUtils.isEmpty((obj.getRemarks()))?obj.getRemarks():null);
								stmt.setString(p++,!StringUtils.isEmpty((pobj.getCreated_by_user_id_fk()))?pobj.getCreated_by_user_id_fk():null);
								stmt.setString(p++,!StringUtils.isEmpty((pobj.getOriginal_duration()))?pobj.getOriginal_duration():null);
								stmt.executeUpdate();
		                        recordsUploaded++;  // Increment upload count

								loop++;
							}else {
								
								String qry = "DELETE FROM p6_activity_progress WHERE p6_activity_id_fk = ? ";
								PreparedStatement deleteExistingIDStmt = con.prepareStatement(qry);
								deleteExistingIDStmt.setString(1, p6_activity_id);
								int c = deleteExistingIDStmt.executeUpdate();  
								DBConnectionHandler.closeJDBCResoucrs(null, deleteExistingIDStmt, rs);
//								System.out.println("update "+activitiesCount++);
								p = 1;
								stmtUpdate.setString(p++,obj.getStructure_id_fk());
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getP6_activity_name()))?obj.getP6_activity_name():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getFrom_structure_id()))?obj.getFrom_structure_id():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getTo_structure_id()))?obj.getTo_structure_id():null);
								
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getSection()))?obj.getSection():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getLine()))?obj.getLine():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getComponent()))?obj.getComponent():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getComponent_id()))?obj.getComponent_id():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getBaseline_start()))?obj.getBaseline_start():null);
								
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getBaseline_finish()))?obj.getBaseline_finish():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getStart()))?obj.getStart():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getFinish()))?obj.getFinish():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getP6_float()))?obj.getP6_float():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getStatus_fk()))?obj.getStatus_fk():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getUnit()))?obj.getUnit():null);
								
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getScope()))?obj.getScope():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getCompleted()))?obj.getCompleted():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getWeightage()))?obj.getWeightage():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getComponent_details()))?obj.getComponent_details():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getRemarks()))?obj.getRemarks():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((pobj.getModified_by_user_id_fk()))?pobj.getModified_by_user_id_fk():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((pobj.getOriginal_duration()))?pobj.getOriginal_duration():null);
								
								stmtUpdate.setString(p++,!StringUtils.isEmpty((obj.getP6_task_code()))?obj.getP6_task_code():null);
								stmtUpdate.setString(p++,!StringUtils.isEmpty((pobj.getContract_id_fk()))?pobj.getContract_id_fk():null);
								stmtUpdate.executeUpdate();
		                        recordsUpdated++;  // Increment update count

								loop1++;
								
							}
							rowNo ++;
						}else {
							if(norows != null) {
								norows = norows +", "+ rowNo;
							}else {
								norows = rowNo+"";
							}
							rowNo ++;
						}
			}
			//int[] c = stmt.executeBatch();	
			//int[] d = stmtUpdate.executeBatch();	
//			for (int i = 0; i < loop; i++) {
//				wbsCount = wbsCount + (i+1);
//			}
//			for (int i = 0; i < loop1; i++) {
//				wbsCountUpdate = wbsCountUpdate + (i+1);
//			}
			DBConnectionHandler.closeJDBCResoucrs(null, stmtUpdate, rs);
			counts = recordsUploaded + "," + recordsUpdated;
//			rows = "<span style='color:green;'> "+recordsUploaded + " records Added, " + recordsUpdated+" records Updated </span><br>";	
			rows = +recordsUploaded + " records Added, " +"and" + recordsUpdated+" records Updated.";	

			System.out.println("wbscount:"+ recordsUploaded+"count update:"+recordsUpdated);
			if(!StringUtils.isEmpty(norows)) {
				if(!norows.contains(",")) {
//					rows = rows +"<span style='color:red;'> row no ( "+ norows+" )  Not Inserted Because Selected Contract & Structure Type & Structure combination is not present in Structure</span> ";
					rows = rows +" row no ( "+ norows+" )  Not Inserted Because Selected Contract & Structure Type & Structure combination is not present in Structure. ";

				}else {
//					rows = rows +"<span style='color:red;'> row nos ( "+ norows+" )  Not Inserted Because Selected Contract & Structure Type & Structure combination is not present in Structure</span> ";
					rows = rows +" row nos ( "+ norows+" )  Not Inserted Because Selected Contract & Structure Type & Structure combination is not present in Structure. ";
			}
			}
			con.commit();
			FormHistory formHistory = new FormHistory();
			formHistory.setCreated_by_user_id_fk(pobj.getCreated_by_user_id_fk());
			formHistory.setUser(pobj.getDesignation()+" - "+pobj.getUser_name());
			formHistory.setModule_name_fk("Execution &  Monitoring");
			formHistory.setForm_name("P6 Data");
			formHistory.setForm_action_type("Add");
			formHistory.setForm_details("Data date updated and "+ counts + " WBS, activities added");
			//formHistory.setWork(pobj.getWork_id_fk());
			formHistory.setContract_id_fk(pobj.getContract_id_fk());
			
			boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
		}catch(Exception e){ 
			e.printStackTrace();
			con.rollback();
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, rs);
		}
		return rows;
	}


	private boolean getP6_wbs_name(String newstr) {
		P6Data dObj = null;
		String laId = null;
		try {
			
			String qry ="SELECT p6_wbs_code as p6_wbs_code FROM p6_wbs WHERE p6_wbs_code = ? " ;
			dObj = (P6Data)jdbcTemplate.queryForObject(qry, new Object[] {newstr}, new BeanPropertyRowMapper<P6Data>(P6Data.class));
			laId = dObj.getP6_wbs_code();
			if(!StringUtils.isEmpty(laId)) {
				return true;
			}else {
				return false;	
			}
		}
		catch(Exception e){ 
				laId = null;
				return false;
		}
	}

	@Override
	public List<P6Data> getWorksList(P6Data obj) throws Exception {
		List<P6Data> objsList = null;
		try {
			String qry ="SELECT c.work_id_fk,work_id,work_name,work_short_name FROM contract c  "
					+ "left join work  on c.work_id_fk = work_id  where work_id is not null ";
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + "and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? or"
						+ " contract_id in(select contract_id_fk from contract_executive where  executive_user_id_fk = ?) "
						+ " or contract_id in(select contract_id_fk from structure_contract_responsible_people where  responsible_people_id_fk = ?) ) ";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}
			qry = qry +"group by c.work_id_fk,work_id,work_name,work_short_name ";
			Object[] pValues = new Object[arrSize]; 
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_id_fk())) {
				pValues[i++] = obj.getWork_id_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<P6Data>(P6Data.class));	
		}catch(Exception e){ 
		throw new Exception(e.getMessage());
		}
		return objsList;
	}

	@Override
	public List<P6Data> getProjectsList(P6Data obj) throws Exception {
		List<P6Data> objsList = null;
		try {
			String qry = "select distinct project_id as project_id_fk,project_name,plan_head_number,remarks from project";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<P6Data>(P6Data.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	
}
