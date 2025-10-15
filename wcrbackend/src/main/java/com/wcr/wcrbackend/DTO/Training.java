package com.wcr.wcrbackend.DTO;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class Training {

	private String period_fk, conduct_by_fk, provide_to_fk, contract_short_name_fk, contractor_name, training_id,
			training_type_fk, training_category_fk, faculty_name, designation, title, description, training_center,
			status_fk, remarks, training_attendees_id, training_id_fk, training_session_id_fk, department_fk, attendee,
			mobile_no, required_fk, participated_fk, training_session_id, session_no, start_time, end_time, hours,
			department_name, is_there_issue, category, issue_description, created_by_user_id_fk, issue_priority_id,
			issue_category_id, hod_user_id_fk, user_name, date, session_remarks, nominated, attended, attachment,
			file_name, user_id, reporting_to, reporting_to_designation, trainee_designation, is_new_user, email,
			user_type_fk, existing_status_fk, periodicity, conducted_by, provided_to, contract_name, created_date,
			no_of_Participants, no_of_Absentees, work_short_name, form_designation;

	public String getNo_of_Participants() {
		return no_of_Participants;
	}

	public void setNo_of_Participants(String no_of_Participants) {
		this.no_of_Participants = no_of_Participants;
	}

	public String getNo_of_Absentees() {
		return no_of_Absentees;
	}

	public void setNo_of_Absentees(String no_of_Absentees) {
		this.no_of_Absentees = no_of_Absentees;
	}

	private MultipartFile[] projectGalleryFiles, projectFiles;

	private MultipartFile trainingFile;
	private List<Training> trainingSessions;
	private List<Training> trainingAttendees;
	private MultipartFile[] trainingSessionFiles;
	private int[] filecounts;
	private List<Training> trainingFilesList;
	private List<Training> trainingNewList;
	private List<Training> attendeesList;
	private List<Training> HODsList;
	private List<Training> deptList;
	private List<Training> statusList;
	private List<Training> employeeReportList;
	private String mail_body_header;

	private String[] training_attendees_ids, training_id_fks, training_session_id_fks, department_fks, attendees,
			trainee_designations, mobile_nos, required_fks, participated_fks, training_session_ids, session_nos,
			start_times, end_times, remarkss, hod_user_id_fks, trainingSessionFileNames, is_new_users, emails,
			created_dates, num_participants, num_absentees, projectGalleryFileNames, new_attendees, new_required_fks,
			new_participated_fks;

	public String getProvided_to() {
		return provided_to;
	}

	public void setProvided_to(String provided_to) {
		this.provided_to = provided_to;
	}

	public String getContract_name() {
		return contract_name;
	}

	public void setContract_name(String contract_name) {
		this.contract_name = contract_name;
	}

	public List<Training> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Training> statusList) {
		this.statusList = statusList;
	}

	public List<Training> getEmployeeReportList() {
		return employeeReportList;
	}

	public void setEmployeeReportList(List<Training> employeeReportList) {
		this.employeeReportList = employeeReportList;
	}

	public String getUser_type_fk() {
		return user_type_fk;
	}

	public void setUser_type_fk(String user_type_fk) {
		this.user_type_fk = user_type_fk;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String[] getEmails() {
		return emails;
	}

	public void setEmails(String[] emails) {
		this.emails = emails;
	}

	public List<Training> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<Training> deptList) {
		this.deptList = deptList;
	}

	public List<Training> getAttendeesList() {
		return attendeesList;
	}

	public void setAttendeesList(List<Training> attendeesList) {
		this.attendeesList = attendeesList;
	}

	public List<Training> getHODsList() {
		return HODsList;
	}

	public void setHODsList(List<Training> hODsList) {
		HODsList = hODsList;
	}

	public List<Training> getTrainingNewList() {
		return trainingNewList;
	}

	public void setTrainingNewList(List<Training> trainingNewList) {
		this.trainingNewList = trainingNewList;
	}

	public String getIs_new_user() {
		return is_new_user;
	}

	public void setIs_new_user(String is_new_user) {
		this.is_new_user = is_new_user;
	}

	public String[] getIs_new_users() {
		return is_new_users;
	}

	public void setIs_new_users(String[] is_new_users) {
		this.is_new_users = is_new_users;
	}

	public List<Training> getTrainingFilesList() {
		return trainingFilesList;
	}

	public void setTrainingFilesList(List<Training> trainingFilesList) {
		this.trainingFilesList = trainingFilesList;
	}

	public int[] getFilecounts() {
		return filecounts;
	}

	public void setFilecounts(int[] filecounts) {
		this.filecounts = filecounts;
	}

	public String getNominated() {
		return nominated;
	}

	public void setNominated(String nominated) {
		this.nominated = nominated;
	}

	public String getAttended() {
		return attended;
	}

	public void setAttended(String attended) {
		this.attended = attended;
	}

	private int[] rowCounts, attendeesRowCount, rowsCounts;

	public int[] getRowsCounts() {
		return rowsCounts;
	}

	public void setRowsCounts(int[] rowsCounts) {
		this.rowsCounts = rowsCounts;
	}

	public String getSession_remarks() {
		return session_remarks;
	}

	public void setSession_remarks(String session_remarks) {
		this.session_remarks = session_remarks;
	}

	public int[] getAttendeesRowCount() {
		return attendeesRowCount;
	}

	public void setAttendeesRowCount(int[] attendeesRowCount) {
		this.attendeesRowCount = attendeesRowCount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public MultipartFile getTrainingFile() {
		return trainingFile;
	}

	public void setTrainingFile(MultipartFile trainingFile) {
		this.trainingFile = trainingFile;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getHod_user_id_fk() {
		return hod_user_id_fk;
	}

	public void setHod_user_id_fk(String hod_user_id_fk) {
		this.hod_user_id_fk = hod_user_id_fk;
	}

	public String[] getHod_user_id_fks() {
		return hod_user_id_fks;
	}

	public void setHod_user_id_fks(String[] hod_user_id_fks) {
		this.hod_user_id_fks = hod_user_id_fks;
	}

	public int[] getRowCounts() {
		return rowCounts;
	}

	public void setRowCounts(int[] rowCounts) {
		this.rowCounts = rowCounts;
	}

	public String getIssue_description() {
		return issue_description;
	}

	public void setIssue_description(String issue_description) {
		this.issue_description = issue_description;
	}

	public String getCreated_by_user_id_fk() {
		return created_by_user_id_fk;
	}

	public void setCreated_by_user_id_fk(String created_by_user_id_fk) {
		this.created_by_user_id_fk = created_by_user_id_fk;
	}

	public String getIssue_priority_id() {
		return issue_priority_id;
	}

	public void setIssue_priority_id(String issue_priority_id) {
		this.issue_priority_id = issue_priority_id;
	}

	public String getIssue_category_id() {
		return issue_category_id;
	}

	public void setIssue_category_id(String issue_category_id) {
		this.issue_category_id = issue_category_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIs_there_issue() {
		return is_there_issue;
	}

	public void setIs_there_issue(String is_there_issue) {
		this.is_there_issue = is_there_issue;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public List<Training> getTrainingAttendees() {
		return trainingAttendees;
	}

	public void setTrainingAttendees(List<Training> trainingAttendees) {
		this.trainingAttendees = trainingAttendees;
	}

	public List<Training> getTrainingSessions() {
		return trainingSessions;
	}

	public void setTrainingSessions(List<Training> trainingSessions) {
		this.trainingSessions = trainingSessions;
	}

	public String[] getTraining_attendees_ids() {
		return training_attendees_ids;
	}

	public void setTraining_attendees_ids(String[] training_attendees_ids) {
		this.training_attendees_ids = training_attendees_ids;
	}

	public String[] getTraining_id_fks() {
		return training_id_fks;
	}

	public void setTraining_id_fks(String[] training_id_fks) {
		this.training_id_fks = training_id_fks;
	}

	public String[] getTraining_session_id_fks() {
		return training_session_id_fks;
	}

	public void setTraining_session_id_fks(String[] training_session_id_fks) {
		this.training_session_id_fks = training_session_id_fks;
	}

	public String[] getDepartment_fks() {
		return department_fks;
	}

	public void setDepartment_fks(String[] department_fks) {
		this.department_fks = department_fks;
	}

	public String[] getAttendees() {
		return attendees;
	}

	public void setAttendees(String[] attendees) {
		this.attendees = attendees;
	}

	public String[] getMobile_nos() {
		return mobile_nos;
	}

	public void setMobile_nos(String[] mobile_nos) {
		this.mobile_nos = mobile_nos;
	}

	public String[] getRequired_fks() {
		return required_fks;
	}

	public void setRequired_fks(String[] required_fks) {
		this.required_fks = required_fks;
	}

	public String[] getParticipated_fks() {
		return participated_fks;
	}

	public void setParticipated_fks(String[] participated_fks) {
		this.participated_fks = participated_fks;
	}

	public String[] getTraining_session_ids() {
		return training_session_ids;
	}

	public void setTraining_session_ids(String[] training_session_ids) {
		this.training_session_ids = training_session_ids;
	}

	public String[] getSession_nos() {
		return session_nos;
	}

	public void setSession_nos(String[] session_nos) {
		this.session_nos = session_nos;
	}

	public String[] getStart_times() {
		return start_times;
	}

	public void setStart_times(String[] start_times) {
		this.start_times = start_times;
	}

	public String[] getEnd_times() {
		return end_times;
	}

	public void setEnd_times(String[] end_times) {
		this.end_times = end_times;
	}

	public String[] getRemarkss() {
		return remarkss;
	}

	public void setRemarkss(String[] remarkss) {
		this.remarkss = remarkss;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getTraining_id() {
		return training_id;
	}

	public void setTraining_id(String training_id) {
		this.training_id = training_id;
	}

	public String getTraining_type_fk() {
		return training_type_fk;
	}

	public void setTraining_type_fk(String training_type_fk) {
		this.training_type_fk = training_type_fk;
	}

	public String getTraining_category_fk() {
		return training_category_fk;
	}

	public void setTraining_category_fk(String training_category_fk) {
		this.training_category_fk = training_category_fk;
	}

	public String getFaculty_name() {
		return faculty_name;
	}

	public void setFaculty_name(String faculty_name) {
		this.faculty_name = faculty_name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTraining_center() {
		return training_center;
	}

	public void setTraining_center(String training_center) {
		this.training_center = training_center;
	}

	public String getStatus_fk() {
		return status_fk;
	}

	public void setStatus_fk(String status_fk) {
		this.status_fk = status_fk;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTraining_attendees_id() {
		return training_attendees_id;
	}

	public void setTraining_attendees_id(String training_attendees_id) {
		this.training_attendees_id = training_attendees_id;
	}

	public String getTraining_id_fk() {
		return training_id_fk;
	}

	public void setTraining_id_fk(String training_id_fk) {
		this.training_id_fk = training_id_fk;
	}

	public String getTraining_session_id_fk() {
		return training_session_id_fk;
	}

	public void setTraining_session_id_fk(String training_session_id_fk) {
		this.training_session_id_fk = training_session_id_fk;
	}

	public String getDepartment_fk() {
		return department_fk;
	}

	public void setDepartment_fk(String department_fk) {
		this.department_fk = department_fk;
	}

	public String getAttendee() {
		return attendee;
	}

	public void setAttendee(String attendee) {
		this.attendee = attendee;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getRequired_fk() {
		return required_fk;
	}

	public void setRequired_fk(String required_fk) {
		this.required_fk = required_fk;
	}

	public String getParticipated_fk() {
		return participated_fk;
	}

	public void setParticipated_fk(String participated_fk) {
		this.participated_fk = participated_fk;
	}

	public String getTraining_session_id() {
		return training_session_id;
	}

	public void setTraining_session_id(String training_session_id) {
		this.training_session_id = training_session_id;
	}

	public String getSession_no() {
		return session_no;
	}

	public void setSession_no(String session_no) {
		this.session_no = session_no;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public boolean checkNullOrEmpty() throws IllegalAccessException {
		boolean flag = true;
		try {
			for (Field f : getClass().getDeclaredFields())
				if (!StringUtils.isEmpty(f.get(this)))
					flag = false;
		} catch (Exception e) {

		}

		return flag;
	}

	public String getContractor_name() {
		return contractor_name;
	}

	public void setContractor_name(String contractor_name) {
		this.contractor_name = contractor_name;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public MultipartFile[] getTrainingSessionFiles() {
		return trainingSessionFiles;
	}

	public void setTrainingSessionFiles(MultipartFile[] trainingSessionFiles) {
		this.trainingSessionFiles = trainingSessionFiles;
	}

	public String[] getTrainingSessionFileNames() {
		return trainingSessionFileNames;
	}

	public void setTrainingSessionFileNames(String[] trainingSessionFileNames) {
		this.trainingSessionFileNames = trainingSessionFileNames;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getReporting_to() {
		return reporting_to;
	}

	public void setReporting_to(String reporting_to) {
		this.reporting_to = reporting_to;
	}

	public String getReporting_to_designation() {
		return reporting_to_designation;
	}

	public void setReporting_to_designation(String reporting_to_designation) {
		this.reporting_to_designation = reporting_to_designation;
	}

	public String getTrainee_designation() {
		return trainee_designation;
	}

	public void setTrainee_designation(String trainee_designation) {
		this.trainee_designation = trainee_designation;
	}

	public String[] getTrainee_designations() {
		return trainee_designations;
	}

	public void setTrainee_designations(String[] trainee_designations) {
		this.trainee_designations = trainee_designations;
	}

	public String getExisting_status_fk() {
		return existing_status_fk;
	}

	public void setExisting_status_fk(String existing_status_fk) {
		this.existing_status_fk = existing_status_fk;
	}

	public String getMail_body_header() {
		return mail_body_header;
	}

	public void setMail_body_header(String mail_body_header) {
		this.mail_body_header = mail_body_header;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public String getConducted_by() {
		return conducted_by;
	}

	public void setConducted_by(String conducted_by) {
		this.conducted_by = conducted_by;
	}

	public String getPeriod_fk() {
		return period_fk;
	}

	public void setPeriod_fk(String period_fk) {
		this.period_fk = period_fk;
	}

	public String getConduct_by_fk() {
		return conduct_by_fk;
	}

	public void setConduct_by_fk(String conduct_by_fk) {
		this.conduct_by_fk = conduct_by_fk;
	}

	public String getProvide_to_fk() {
		return provide_to_fk;
	}

	public void setProvide_to_fk(String provide_to_fk) {
		this.provide_to_fk = provide_to_fk;
	}

	public String getContract_short_name_fk() {
		return contract_short_name_fk;
	}

	public void setContract_short_name_fk(String contract_short_name_fk) {
		this.contract_short_name_fk = contract_short_name_fk;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String[] getCreated_dates() {
		return created_dates;
	}

	public void setCreated_dates(String[] created_dates) {
		this.created_dates = created_dates;
	}

	public String[] getNum_participants() {
		return num_participants;
	}

	public void setNum_participants(String[] num_participants) {
		this.num_participants = num_participants;
	}

	public String[] getNum_absentees() {
		return num_absentees;
	}

	public void setNum_absentees(String[] num_absentees) {
		this.num_absentees = num_absentees;
	}

	public String[] getProjectGalleryFileNames() {
		return projectGalleryFileNames;
	}

	public void setProjectGalleryFileNames(String[] projectGalleryFileNames) {
		this.projectGalleryFileNames = projectGalleryFileNames;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public MultipartFile[] getProjectGalleryFiles() {
		return projectGalleryFiles;
	}

	public void setProjectGalleryFiles(MultipartFile[] projectGalleryFiles) {
		this.projectGalleryFiles = projectGalleryFiles;
	}

	public MultipartFile[] getProjectFiles() {
		return projectFiles;
	}

	public void setProjectFiles(MultipartFile[] projectFiles) {
		this.projectFiles = projectFiles;
	}

	public String[] getNew_attendees() {
		return new_attendees;
	}

	public void setNew_attendees(String[] new_attendees) {
		this.new_attendees = new_attendees;
	}

	public String[] getNew_required_fks() {
		return new_required_fks;
	}

	public void setNew_required_fks(String[] new_required_fks) {
		this.new_required_fks = new_required_fks;
	}

	public String[] getNew_participated_fks() {
		return new_participated_fks;
	}

	public void setNew_participated_fks(String[] new_participated_fks) {
		this.new_participated_fks = new_participated_fks;
	}

	public String getWork_short_name() {
		return work_short_name;
	}

	public void setWork_short_name(String work_short_name) {
		this.work_short_name = work_short_name;
	}

	public String getForm_designation() {
		return form_designation;
	}

	public void setForm_designation(String form_designation) {
		this.form_designation = form_designation;
	}

	@Override
	public String toString() {
		return "Training [period_fk=" + period_fk + ", conduct_by_fk=" + conduct_by_fk + ", provide_to_fk="
				+ provide_to_fk + ", contract_short_name_fk=" + contract_short_name_fk + ", training_id=" + training_id
				+ ", training_type_fk=" + training_type_fk + ", training_category_fk=" + training_category_fk
				+ ", faculty_name=" + faculty_name + ", designation=" + designation + ", title=" + title
				+ ", description=" + description + ", training_center=" + training_center + ", status_fk=" + status_fk
				+ ", remarks=" + remarks + ", training_attendees_id=" + training_attendees_id + ", training_id_fk="
				+ training_id_fk + ", training_session_id_fk=" + training_session_id_fk + ", department_fk="
				+ department_fk + ", attendee=" + attendee + ", mobile_no=" + mobile_no + ", required_fk=" + required_fk
				+ ", participated_fk=" + participated_fk + ", training_session_id=" + training_session_id
				+ ", session_no=" + session_no + ", start_time=" + start_time + ", end_time=" + end_time + ", hours="
				+ hours + ", department_name=" + department_name + ", is_there_issue=" + is_there_issue + ", category="
				+ category + ", issue_description=" + issue_description + ", created_by_user_id_fk="
				+ created_by_user_id_fk + ", issue_priority_id=" + issue_priority_id + ", issue_category_id="
				+ issue_category_id + ", hod_user_id_fk=" + hod_user_id_fk + ", user_name=" + user_name + ", date="
				+ date + ", session_remarks=" + session_remarks + ", nominated=" + nominated + ", attended=" + attended
				+ ", attachment=" + attachment + ", file_name=" + file_name + ", user_id=" + user_id + ", reporting_to="
				+ reporting_to + ", reporting_to_designation=" + reporting_to_designation + ", trainee_designation="
				+ trainee_designation + ", is_new_user=" + is_new_user + ", email=" + email + ", user_type_fk="
				+ user_type_fk + ", existing_status_fk=" + existing_status_fk + ", periodicity=" + periodicity
				+ ", conducted_by=" + conducted_by + ", provided_to=" + provided_to + ", contract_name=" + contract_name
				+ ", created_date=" + created_date + ", no_of_Participants=" + no_of_Participants + ", no_of_Absentees="
				+ no_of_Absentees + ", work_short_name=" + work_short_name + ", projectGalleryFiles="
				+ Arrays.toString(projectGalleryFiles) + ", projectFiles=" + Arrays.toString(projectFiles)
				+ ", trainingFile=" + trainingFile + ", trainingSessions=" + trainingSessions + ", trainingAttendees="
				+ trainingAttendees + ", trainingSessionFiles=" + Arrays.toString(trainingSessionFiles)
				+ ", filecounts=" + Arrays.toString(filecounts) + ", trainingFilesList=" + trainingFilesList
				+ ", trainingNewList=" + trainingNewList + ", attendeesList=" + attendeesList + ", HODsList=" + HODsList
				+ ", deptList=" + deptList + ", statusList=" + statusList + ", employeeReportList=" + employeeReportList
				+ ", mail_body_header=" + mail_body_header + ", training_attendees_ids="
				+ Arrays.toString(training_attendees_ids) + ", training_id_fks=" + Arrays.toString(training_id_fks)
				+ ", training_session_id_fks=" + Arrays.toString(training_session_id_fks) + ", department_fks="
				+ Arrays.toString(department_fks) + ", attendees=" + Arrays.toString(attendees)
				+ ", trainee_designations=" + Arrays.toString(trainee_designations) + ", mobile_nos="
				+ Arrays.toString(mobile_nos) + ", required_fks=" + Arrays.toString(required_fks)
				+ ", participated_fks=" + Arrays.toString(participated_fks) + ", training_session_ids="
				+ Arrays.toString(training_session_ids) + ", session_nos=" + Arrays.toString(session_nos)
				+ ", start_times=" + Arrays.toString(start_times) + ", end_times=" + Arrays.toString(end_times)
				+ ", remarkss=" + Arrays.toString(remarkss) + ", hod_user_id_fks=" + Arrays.toString(hod_user_id_fks)
				+ ", trainingSessionFileNames=" + Arrays.toString(trainingSessionFileNames) + ", is_new_users="
				+ Arrays.toString(is_new_users) + ", emails=" + Arrays.toString(emails) + ", created_dates="
				+ Arrays.toString(created_dates) + ", num_participants=" + Arrays.toString(num_participants)
				+ ", num_absentees=" + Arrays.toString(num_absentees) + ", projectGalleryFileNames="
				+ Arrays.toString(projectGalleryFileNames) + ", new_attendees=" + Arrays.toString(new_attendees)
				+ ", new_required_fks=" + Arrays.toString(new_required_fks) + ", new_participated_fks="
				+ Arrays.toString(new_participated_fks) + ", rowCounts=" + Arrays.toString(rowCounts)
				+ ", attendeesRowCount=" + Arrays.toString(attendeesRowCount) + ", rowsCounts="
				+ Arrays.toString(rowsCounts) + "]";
	}

}
