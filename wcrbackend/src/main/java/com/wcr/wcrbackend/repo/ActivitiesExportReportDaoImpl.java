package com.wcr.wcrbackend.repo;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.StripChart;
@Repository
public class ActivitiesExportReportDaoImpl implements ActivitiesExportReportDao{
	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<StripChart> getProjectsFilterListInActivitiesExportReport(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT distinct project_id,project_name from p6_activities a "
					+ "LEFT JOIN contract c ON a.contract_id_fk = c.contract_id "
					+ "left join project p on c.project_id_fk = p.project_id "
					+ "left join contractor c1 on c1.contractor_id=c.contractor_id_fk "
					+ " where project_id is not null and project_id <> '' ";
			
			int arrSize = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				qry = qry + " and contractor_name=? "; 
				arrSize++;
			}			
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
		
			
			qry = qry + " GROUP BY project_id,project_name";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_name();
			}			
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}	

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id())) {
				pValues[i++] = obj.getProject_id();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getWorksFilterListInActivitiesExportReport(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT  distinct work_id as work_id_fk,work_short_name from p6_activities a "
					+ "LEFT JOIN contract c ON a.contract_id_fk = c.contract_id "
					+ "left join contractor c1 on c1.contractor_id=c.contractor_id_fk "
					+ "left join project p on c.project_id_fk = p.project_id "
					+ " where c.work_id_fk is not null and c.work_id_fk <> '' ";
			
			int arrSize = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				qry = qry + " and contractor_name=? "; 
				arrSize++;
			}			
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
		
			
			qry = qry + " GROUP BY work_id,work_short_name";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_name();
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id())) {
				pValues[i++] = obj.getProject_id();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getContractListInActivitiesExportReport(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT distinct project_id as project_id_fk,project_name,contract_id_fk,contract_short_name from p6_activities a "
					+ "LEFT JOIN contract c ON a.contract_id_fk = c.contract_id "
					+ "left join contractor c1 on c1.contractor_id=c.contractor_id_fk "
					+ "left join project p on c.project_id_fk = p.project_id "
					+ " where contract_id_fk is not null and contract_id_fk <> '' ";
			
			int arrSize = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				qry = qry + " and contractor_name=? "; 
				arrSize++;
			}			
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
		
			
			//qry = qry + " GROUP BY contract_id_fk";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_name();
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id())) {
				pValues[i++] = obj.getProject_id();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public StripChart generateActivitiesExportReport(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT a.p6_activity_id as activity_id, FORMAT(a.baseline_start,'dd-MM-yyyy') as baseline_start, FORMAT(a.baseline_finish,'dd-MM-yyyy') as baseline_finish," + 
					"					 FORMAT(a.start,'dd-MM-yyyy') as start, FORMAT(a.finish,'dd-MM-yyyy') as finish, a.contract_id_fk,contract_short_name, a.from_structure_id," + 
					"					 a.to_structure_id, s11.structure_type_fk, a.section, a.line, s11.structure, a.component, a.component_id, a.order_x, a.order_y, " + 
					"					a.p6_activity_name as activity_name, FORMAT(a.baseline_start,'dd-MM-yyyy') as planned_start, FORMAT(a.baseline_finish,'dd-MM-yyyy') as planned_finish, " + 
					"					case  " + 
					"					 when (ISNULL(completed, 0)=0 or completed is null) then ''  " + 
					"					 when ISNULL(completed, 0)>=ISNULL(scope, 0) then (select FORMAT(min(progress_date),'dd-MM-yyyy') from p6_activity_progress where p6_activity_id_fk=a.p6_activity_id)  " + 
					"					 else (select FORMAT(min(progress_date),'dd-MM-yyyy') from p6_activity_progress where p6_activity_id_fk=a.p6_activity_id) end as actual_start,case   " + 
					"					 when (ISNULL(completed, 0)=0 or completed is null) then '' " + 
					"					 when ISNULL(completed, 0)>=ISNULL(scope, 0) then (select FORMAT(max(progress_date),'dd-MM-yyyy') from p6_activity_progress where p6_activity_id_fk=a.p6_activity_id)  " + 
					"					 else '' end as actual_finish, a.unit, a.scope, a.completed, CAST(a.weightage AS DECIMAL(10,2)) as weightage, a.component_details, a.remarks, " + 
					"					a.created_date, a.created_by_user_id_fk, a.modified_date, a.modified_by_user_id_fk, a.task_code as p6_task_code  " + 
					"					from p6_activities a left join structure s11 on s11.structure_id = a.structure_id_fk " + 
					"					LEFT JOIN contract c ON a.contract_id_fk = c.contract_id " + 
					"					left join project p on c.project_id_fk = p.project_id " + 
					"					 where a.p6_activity_id is not null and a.p6_activity_id <> '' ";
			
			int arrSize = 0;
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and a.contract_id_fk = ?";
				arrSize++;
			}
		if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
			
			qry = qry + "  order by ISNULL(a.task_code,''), a.task_code ASC,a.p6_activity_id";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id())) {
				pValues[i++] = obj.getProject_id();
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
			obj.setReport1List(objsList);
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return obj;
	}

	@Override
	public List<StripChart> generateTPCStatusReport(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select contract_short_name,structure,progress,Status,FORMAT(progress_date,'dd-MM-yyyy') as progress_date from( " + 
					"SELECT c.contract_short_name,structure,cast((round(cast((isnull(SUM((completed * weightage)*100 / scope) / SUM(weightage),0)) as decimal(10,2)),0)) as int) AS progress,  " + 
					"					case when SUM((completed*weightage)*100 / scope) / SUM(weightage)=100 then 'Completed' when SUM((completed * weightage)*100 / scope) / SUM(weightage)<100 and SUM((completed *  weightage)*100 / scope) / SUM(weightage)*100>0 then 'In Progress' else 'Not Started' end as Status,   " + 
					"					case when SUM((completed*weightage)*100 / scope) / SUM(weightage)=100 then max(a.actual_finish) else max(a.expected_finish) end as progress_date   " + 
					"					FROM activities_view a   " + 
					"					LEFT join contract c on c.contract_id=a.contract_id   " + 
//					"					where a.contract_id like 'P04W04EN%' and structure not in('Badlapur (Deck)') and structure not in('Khar Road (New FOB)')  " + 
					"					 " + 
					"					GROUP BY c.contract_short_name,structure) as a  " + 
					"					 " + 
					"					GROUP BY contract_short_name,structure,progress,Status,progress_date " + 
					"					order by contract_short_name,status ";
			

			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<StripChart>(StripChart.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getDivisionList(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "				select Division,sum(StructureCount) as 'WorkScope',sum(CompletedCount) as Completed,sum(StructureCount)-sum(CompletedCount) as 'Balance',sum(wip) as wip from(    " + 
					"				SELECT  contract_short_name  as 'Division',    " + 
					"				count(structure) as StructureCount,case when Status='Completed' then count(Status) else 0 end AS 'CompletedCount',case when Status='In Progress' then count(Status) else 0 end AS wip    " + 
					"				from(    " + 
					"				SELECT substring(c.contract_short_name,CHARINDEX('(',c.contract_short_name),CHARINDEX(')',c.contract_short_name)-CHARINDEX('(',c.contract_short_name)+1) as contract_short_name,structure,round(cast((isnull(SUM((completed * weightage)*100 / scope) / SUM(weightage),0)) as decimal(10,2)),2) AS progress,      " + 
					"				case when SUM((completed * weightage)*100 / scope) / SUM(weightage)=100 then 'Completed' when SUM((completed * weightage)*100 / scope) / SUM(weightage)<100 and SUM((completed * weightage)*100 / scope) / SUM(weightage)*100>0 then 'In Progress' else 'Not Started' end as Status,      " + 
					"				case when SUM((completed * weightage)*100 / scope) / SUM(weightage)=100 then Max(actual_finish) else Max(expected_finish) end as progress_date " + 
					"				FROM activities_view a      " + 
					"				LEFT join contract c on c.contract_id=a.contract_id      " + 
//					"				where a.contract_id like 'P04W04EN%' and structure not in('Badlapur (Deck)') and structure not in('Khar Road (New FOB)')    " + 
					" 				GROUP BY c.contract_short_name,structure) as a "+
					"				group by contract_short_name,Status) as b    " + 
					"				group by b.Division ";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<StripChart>(StripChart.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> generateMCDOProgressReport(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select * from( select distinct works+' ('+unit+')' as works,cast(during_month as int) as during_month,cast(cumulative as int) as cumulative,cast(balance as int) as balance from  vf_mcdo_pk('"+obj.getProject_id_fk()+"','"+obj.getFrom_date()+"','"+obj.getTo_date()+"') ) as a where works is not null   and works in('Earthwork Filling (Cum)','Rock cutting (Cum)','Blanketing (Cum)','Drain (Rm)','TSS Formation (Cum)','Flyover (Nos)','Major Bridge (Nos)','Minor Bridge (Nos)','ROB (Nos)','RUB (Nos)','Approach Cutting (Cum)','Heading (Rm)','Benching (Rm)','Final Lining  (Rm)','Railway Earth Filling (Cum)','Panvel Station Improvement (Nos)','Chowk Station Improvement (Nos)','Chikhale Station Improvement (Nos)','Mohape Station Improvement (Nos)','Karjat Station Improvement (Nos)')  order by (case when works='Earthwork Filling (Cum)' then 1 when works='Railway Earth Filling (Cum)' then 2 when works='Rock cutting (Cum)' then 3 when works='Blanketing (Cum)' then 4 when works='Drain (Rm)' then 5 when works='TSS Formation (Cum)' then 6   when works='Flyover (Nos)' then 7 when works='Major Bridge (Nos)' then 8 when works='Minor Bridge (Nos)' then 9 when works='ROB (Nos)' then 10 when works='RUB (Nos)' then 11   when works='Approach Cutting (Cum)' then 12 when works='Heading (Rm)' then 13 when works='Benching (Rm)' then 14 when works='Final Lining  (Rm)' then 15   " + 
					" " + 
					"when works='Railway Earth Filling (Cum)' then 16 when works='Panvel Station Improvement (Nos)' then 17 when works='Chowk Station Improvement (Nos)' then 18 when works='Chikhale Station Improvement (Nos)' then 19 when works='Mohape Station Improvement (Nos)' then 20  when works='Karjat Station Improvement (Nos)' then 21 " + 
					" " + 
					"else 0 end)";
			

			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<StripChart>(StripChart.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> generateMCDOProgressReport1(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select * from( " + 
					"select distinct works+' ('+unit+')' as works,cast(during_month as int) as during_month,cast(cumulative as int) as cumulative,cast(balance as int) as balance  from  vf_mcdo_virar('"+obj.getProject_id_fk()+"','"+obj.getFrom_date()+"','"+obj.getTo_date()+"') " +
					") as a where works is not null  " + 
					" " + 
					"and works in('Earthwork Filling (Cum)','Earthwork Cutting (Cum)','Rock cutting (Cum)','Blanketing (Cum)','Construction of Drain (Rm)','Major Bridge (Nos)','Minor Bridge (Nos)','ROB (Nos)','RUB (Nos)','Boisar station (Nos)',\r\n" + 
					"'Kelve Station (Nos)',\r\n" + 
					"'Vangaon station (Nos)',\r\n" + 
					"'Virar-Vaitarna(East) (Nos)',\r\n" + 
					"'Vaitarna Station (Nos)',\r\n" + 
					"'Virar Car Shed North (Nos)',\r\n" + 
					"'Dahanu station (Nos)',\r\n" + 
					"'Umroli station (Nos)',\r\n" + 
					"'Palghar Station (Nos)',\r\n" + 
					"'Saphale Station (Nos)',\r\n" + 
					"'Virar Station (Nos)',\r\n" + 
					"'Relay Hut (Nos)',\r\n" + 
					"'Addl Relay Hut (Nos)') " + 
					" " + 
					"order by (case when works='Earthwork Filling (Cum)' then 1 when works='Earthwork Cutting (Cum)' then 2 when works='Blanketing (Cum)' then 3 when works='Construction of Drain (Rm)' then 4  when works='Major Bridge (Nos)' then 5 when works='Minor Bridge (Nos)' then 6 when works='ROB (Nos)' then 7 when works='RUB (Nos)' then 8  when works='Boisar station (Nos)' then 9\r\n" + 
					"when works='Kelve Station (Nos)' then 10\r\n" + 
					"when works='Vangaon station (Nos)' then 11 \r\n" + 
					"when works='Virar-Vaitarna(East) (Nos)' then 12\r\n" + 
					"when works='Vaitarna Station (Nos)' then 13\r\n" + 
					"when works='Virar Car Shed North (Nos)' then 14\r\n" + 
					"when works='Dahanu station (Nos)' then 15\r\n" + 
					"when works='Umroli station (Nos)' then 16\r\n" + 
					"when works='Palghar Station (Nos)' then 17\r\n" + 
					"when works='Saphale Station (Nos)'  then 18\r\n" + 
					"when works='Virar Station (Nos)' then 19\r\n" + 
					"when works='Relay Hut (Nos)' then 20\r\n" + 
					"when works='Addl Relay Hut (Nos)' then 21\r\n" + 
					" " + 
					"else 0 end) ";
			

			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<StripChart>(StripChart.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> generateTPCStructureList(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select structure,sum(CStructureCount) as 'CScope',sum(CCompletedCount) as CCompleted,sum(CStructureCount)-sum(CCompletedCount) as 'CBalance',sum(CWIP) as CWIP, " + 
					"sum(WStructureCount) as 'WScope',sum(WCompletedCount) as 'WCompleted',sum(WStructureCount)-sum(WCompletedCount) as 'WBalance',sum(WWIP) as WWIP from( " + 
					"select replace(replace(contract_short_name,'(',''),')','') as contract_short_name,replace(replace(structure,'(',''),')','') as structure,cprogress,wprogress,CStatus,WStatus, " + 
					" " + 
					"case when contract_short_name like '%CR%' and CStatus='Completed' then count(CStatus) else 0 end AS 'CCompletedCount',case when contract_short_name like '%CR%' and CStatus='In Progress' then count(CStatus) else 0 end AS 'CWIP',  " + 
					"case when contract_short_name like '%WR%' and WStatus='Completed' then count(WStatus) else 0 end AS 'WCompletedCount',case when contract_short_name like '%WR%' and WStatus='In Progress' then count(WStatus) else 0 end AS 'WWIP'  " + 
					",case when contract_short_name like '%CR%' THEN count(Cstructure) else 0 end as CStructureCount " + 
					",case when contract_short_name like '%WR%' THEN count(Wstructure) else 0 end as WStructureCount " + 
					" " + 
					" " + 
					"from( " + 
					" " + 
					"select  substring(c.contract_short_name,CHARINDEX('(',c.contract_short_name),CHARINDEX(')',c.contract_short_name)-CHARINDEX('(',c.contract_short_name)+1) as contract_short_name,substring(structure,LEN(structure) - CHARINDEX('(',REVERSE(structure)), LEN(structure) - CHARINDEX(')',REVERSE(structure)))   as structure, " + 
					" " + 
					"case when c.contract_short_name like '%CR%' then round(cast((isnull(SUM((completed * weightage)*100 / scope) / SUM(weightage),0)) as decimal(10,2)),2) else 0 end AS cprogress, " + 
					"case when c.contract_short_name like '%WR%' then round(cast((isnull(SUM((completed * weightage)*100 / scope) / SUM(weightage),0)) as decimal(10,2)),2) else 0 end AS wprogress, " + 
					"case when c.contract_short_name like '%CR%' and SUM((completed * weightage)*100 / scope) / SUM(weightage)=100 then 'Completed' when SUM((completed * weightage)*100 / scope) / SUM(weightage)<100 and SUM((completed * weightage)*100 / scope) / SUM(weightage)*100>0 then 'In Progress' else 'Not Started' end as CStatus, " + 
					"case when c.contract_short_name like '%WR%' and SUM((completed * weightage)*100 / scope) / SUM(weightage)=100 then 'Completed' when SUM((completed * weightage)*100 / scope) / SUM(weightage)<100 and SUM((completed * weightage)*100 / scope) / SUM(weightage)*100>0 then 'In Progress' else 'Not Started' end as WStatus " + 
					",case when c.contract_short_name like '%CR%' then count(structure) else 0 end as Cstructure " + 
					",case when c.contract_short_name like '%WR%' then count(structure) else 0 end as Wstructure " + 
					" " + 
					"FROM activities_view a     " + 
					"LEFT join contract c on c.contract_id=a.contract_id     " + 
//					"where a.contract_id like 'P04W04EN%' and structure not in('Badlapur (Deck)') and structure not in('Khar Road (New FOB)')  " + 
					"and substring(structure,CHARINDEX('(',structure),CHARINDEX(')',structure)-CHARINDEX('(',structure)+1)!=''  group by contract_short_name,structure) as m " + 
					" " + 
					" " + 
					"group by m.contract_short_name,m.structure,m.cprogress,m.wprogress,m.CStatus,m.WStatus,M.Cstructure,M.Wstructure) AS l " + 
					" " + 
					"group by structure ";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<StripChart>(StripChart.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> generateTPCStructureCumList(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select structure,sum(CStructureCount+WStructureCount) as 'Scope',sum(CCompletedCount+WCompletedCount) as Completed,sum(CStructureCount+WStructureCount)-sum(CCompletedCount+WCompletedCount) as 'Balance',sum(CWIP+WWIP) as WIP from( " + 
					"select replace(replace(contract_short_name,'(',''),')','') as contract_short_name,replace(replace(structure,'(',''),')','') as structure,cprogress,wprogress,CStatus,WStatus, " + 
					" " + 
					"case when contract_short_name like '%CR%' and CStatus='Completed' then count(CStatus) else 0 end AS 'CCompletedCount',case when contract_short_name like '%CR%' and CStatus='In Progress' then count(CStatus) else 0 end AS 'CWIP',  " + 
					"case when contract_short_name like '%WR%' and WStatus='Completed' then count(WStatus) else 0 end AS 'WCompletedCount',case when contract_short_name like '%WR%' and WStatus='In Progress' then count(WStatus) else 0 end AS 'WWIP'  " + 
					",case when contract_short_name like '%CR%' THEN count(Cstructure) else 0 end as CStructureCount " + 
					",case when contract_short_name like '%WR%' THEN count(Wstructure) else 0 end as WStructureCount " + 
					" " + 
					" " + 
					"from( " + 
					" " + 
					"select  substring(c.contract_short_name,CHARINDEX('(',c.contract_short_name),CHARINDEX(')',c.contract_short_name)-CHARINDEX('(',c.contract_short_name)+1) as contract_short_name,substring(structure,LEN(structure) - CHARINDEX('(',REVERSE(structure)), LEN(structure) - CHARINDEX(')',REVERSE(structure)))   as structure, " + 
					" " + 
					"case when c.contract_short_name like '%CR%' then round(cast((isnull(SUM((completed * weightage)*100 / scope) / SUM(weightage),0)) as decimal(10,2)),2) else 0 end AS cprogress, " + 
					"case when c.contract_short_name like '%WR%' then round(cast((isnull(SUM((completed * weightage)*100 / scope) / SUM(weightage),0)) as decimal(10,2)),2) else 0 end AS wprogress, " + 
					"case when c.contract_short_name like '%CR%' and SUM((completed * weightage)*100 / scope) / SUM(weightage)=100 then 'Completed' when SUM((completed * weightage)*100 / scope) / SUM(weightage)<100 and SUM((completed * weightage)*100 / scope) / SUM(weightage)*100>0 then 'In Progress' else 'Not Started' end as CStatus, " + 
					"case when c.contract_short_name like '%WR%' and SUM((completed * weightage)*100 / scope) / SUM(weightage)=100 then 'Completed' when SUM((completed * weightage)*100 / scope) / SUM(weightage)<100 and SUM((completed * weightage)*100 / scope) / SUM(weightage)*100>0 then 'In Progress' else 'Not Started' end as WStatus " + 
					",case when c.contract_short_name like '%CR%' then count(structure) else 0 end as Cstructure " + 
					",case when c.contract_short_name like '%WR%' then count(structure) else 0 end as Wstructure " + 
					" " + 
					"FROM activities_view a     " + 
					"LEFT join contract c on c.contract_id=a.contract_id     " + 
//					"where a.contract_id like 'P04W04EN%' and structure not in('Badlapur (Deck)') and structure not in('Khar Road (New FOB)')  " + 
					"and substring(structure,CHARINDEX('(',structure),CHARINDEX(')',structure)-CHARINDEX('(',structure)+1)!=''  group by contract_short_name,structure) as m " + 
					" " + 
					" " + 
					"group by m.contract_short_name,m.structure,m.cprogress,m.wprogress,m.CStatus,m.WStatus,M.Cstructure,M.Wstructure) as l " + 
					" " + 
					"group by structure ";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<StripChart>(StripChart.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getWorksListForSelectedProject(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String Str=obj.getProject_id().toString();
			String StrVal=Str.replaceAll("%2C", "','");
			String qry = "SELECT  work_id as work_id_fk,work_short_name from p6_activities a "
					+ "LEFT JOIN contract c ON a.contract_id_fk = c.contract_id "
					+ "left join work w on work_id = c.work_id_fk "
					+ "left join project p on c.project_id_fk = p.project_id "
					+ " where c.work_id_fk is not null and c.work_id_fk <> '' and c.project_id_fk in('"+StrVal+"') ";
			
			int arrSize = 0;
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_id_fk())) {
				qry = qry + " and c.work_id_fk = ?";
				arrSize++;
			}
			/*if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProjects())) {
				qry = qry + " and c.project_id_fk in (?";
				arrSize++;
				int length = obj.getProjects().length;
				if(length > 1) {
					for(int i =0; i< (length-1); i++) {
						qry = qry + ",?";
						arrSize++;
					}
				}
				qry = qry + " ) ";
			}*/
		
			qry = qry + " GROUP BY work_id,work_short_name";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_id_fk())) {
				pValues[i++] = obj.getWork_id_fk();
			}
			/*if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProjects())) {
				pValues[i++] = obj.getProjects()[0];
				int length = obj.getProjects().length;
				if(length > 1) {
					for(int j =1; j< length; j++) {
						pValues[i++] = obj.getProjects()[j];
					}
				}
			}*/
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	

	@Override
	public List<StripChart> generateStationImprovementsReport(StripChart obj) throws Exception {
	    List<StripChart> objsList = null;
	    try {
	        String qry = "SELECT  " + 
	        		"	CASE " + 
	        		"      WHEN c.contract_short_name LIKE '%(CR)%' THEN 'CR' " + 
	        		"      WHEN c.contract_short_name LIKE '%(WR)%' THEN 'WR' " + 
	        		"      WHEN c.contract_short_name LIKE '%(HBR)%' THEN 'HBR' " + 
	        		"      ELSE c.contract_short_name " + 
	        		"     END AS contract_short_name, " + 
	        		"    a.structure_type AS structure, " + 
	        		"	round(cast((isnull(SUM((completed * weightage)*100 / scope) / SUM(weightage),0)) as decimal(10,2)),2) progress, " + 
	        		"    CASE  " + 
	        		"        WHEN SUM(a.completed * a.weightage) * 100.0 / NULLIF(SUM(a.scope * a.weightage), 0) = 100 THEN 'Completed' " + 
	        		"        WHEN SUM(a.completed * a.weightage) * 100.0 / NULLIF(SUM(a.scope * a.weightage), 0) > 0 THEN 'In Progress' " + 
	        		"        ELSE 'Not Started' " + 
	        		"    END AS status, " + 
	        		"    FORMAT( " + 
	        		"        CASE  " + 
	        		"            WHEN SUM(a.completed * a.weightage) * 100.0 / NULLIF(SUM(a.scope * a.weightage), 0) = 100 THEN MAX(a.actual_finish) " + 
	        		"            ELSE MAX(a.expected_finish) " + 
	        		"        END, 'dd-MM-yyyy' " + 
	        		"    ) AS progress_date " + 
	        		"FROM  " + 
	        		"    activities_view a " + 
	        		"LEFT JOIN  " + 
	        		"    contract c ON c.contract_id = a.contract_id  " + 
//	        		"WHERE  " + 
//	        		"     a.contract_id like '%P05W06EN%' " + 
//	        		"    AND (c.contract_short_name LIKE '%(CR)%'  " + 
//	        		"         OR c.contract_short_name LIKE '%(WR)%'  " + 
//	        		"         OR c.contract_short_name LIKE '%(HBR)%')  and a.contract_id in('P05W06EN03','P05W06EN05','P05W06EN10','P05W06EN02','P05W06EN07','P05W06EN08','P05W06EN09') " + 
	        		"GROUP BY  " + 
	        		"    CASE " + 
	        		"        WHEN c.contract_short_name LIKE '%(CR)%' THEN 'CR' " + 
	        		"        WHEN c.contract_short_name LIKE '%(WR)%' THEN 'WR' " + 
	        		"        WHEN c.contract_short_name LIKE '%(HBR)%' THEN 'HBR' " + 
	        		"        ELSE c.contract_short_name " + 
	        		"    END, " + 
	        		"    a.structure_type " + 
	        		"ORDER BY  " + 
	        		"    CASE " + 
	        		"      WHEN c.contract_short_name LIKE '%(CR)%' THEN 'CR' " + 
	        		"      WHEN c.contract_short_name LIKE '%(WR)%' THEN 'WR' " + 
	        		"      WHEN c.contract_short_name LIKE '%(HBR)%' THEN 'HBR' " + 
	        		"      ELSE c.contract_short_name " + 
	        		"     END,status";

	        objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<>(StripChart.class));

	    } catch (Exception e) {
	        throw new Exception(e);
	    }
	    return objsList;
	}

	@Override
	public List<StripChart> getStationImprovementDivisionList(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT  " + 
					"    CASE  " + 
					"        WHEN Division = '(CR)' THEN 'Station-CR' " + 
					"        WHEN Division = '(HBR)' THEN 'Station-HBR' " + 
					"        WHEN Division = '(WR)' THEN 'Station-WR' " + 
					"        ELSE Division " + 
					"    END AS 'Division', " + 
					"    SUM(StructureCount) AS 'WorkScope', " + 
					"    SUM(CompletedCount) AS Completed, " + 
					"    SUM(StructureCount) - SUM(CompletedCount) AS 'Balance', " + 
					"    SUM(wip) AS wip  " + 
					"FROM ( " + 
					"    SELECT   " + 
					"        contract_short_name AS 'Division',      " + 
					"        COUNT(structure) AS StructureCount, " + 
					"        CASE WHEN Status = 'Completed' THEN COUNT(Status) ELSE 0 END AS 'CompletedCount', " + 
					"        CASE WHEN Status = 'In Progress' THEN COUNT(Status) ELSE 0 END AS wip      " + 
					"    FROM (      " + 
					"        SELECT  " + 
					"            substring(c.contract_short_name, CHARINDEX('(', c.contract_short_name), CHARINDEX(')', c.contract_short_name) - CHARINDEX('(', c.contract_short_name) + 1) AS contract_short_name, " + 
					"           structure_type as structure, " + 
					"            round(cast((isnull(SUM((completed * weightage) * 100 / scope) / SUM(weightage), 0)) AS decimal(10, 2)), 2) AS progress,       " + 
					"            CASE  " + 
					"                WHEN SUM((completed * weightage) * 100 / scope) / SUM(weightage) = 100 THEN 'Completed'  " + 
					"                WHEN SUM((completed * weightage) * 100 / scope) / SUM(weightage) < 100 AND SUM((completed * weightage) * 100 / scope) / SUM(weightage) * 100 > 0 THEN 'In Progress'  " + 
					"                ELSE 'Not Started'  " + 
					"            END AS Status,        " + 
					"            CASE  " + 
					"                WHEN SUM((completed * weightage) * 100 / scope) / SUM(weightage) = 100 THEN MAX(actual_finish)  " + 
					"                ELSE MAX(expected_finish)  " + 
					"            END AS progress_date   " + 
					"        FROM activities_view a        " + 
					"        LEFT JOIN contract c ON c.contract_id = a.contract_id       " + 
//					"        WHERE a.contract_id LIKE 'P05W06EN%'  " + 
//					"          AND structure NOT IN ('Badlapur (Deck)', 'Khar Road (New FOB)')  " + 
					"        GROUP BY c.contract_short_name, structure_type " + 
					"    ) AS a      " + 
					"    GROUP BY contract_short_name, Status " + 
					") AS b      " + 
					"GROUP BY  " + 
					"    CASE  " + 
					"        WHEN Division = '(CR)' THEN 'Station-CR' " + 
					"        WHEN Division = '(HBR)' THEN 'Station-HBR' " + 
					"        WHEN Division = '(WR)' THEN 'Station-WR' " + 
					"        ELSE Division " + 
					"    END";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<StripChart>(StripChart.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}	
	
	
}
