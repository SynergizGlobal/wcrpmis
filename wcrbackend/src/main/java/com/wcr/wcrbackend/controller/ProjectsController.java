package com.wcr.wcrbackend.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import jakarta.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.wcr.wcrbackend.DTO.FileFormatModel;
import com.wcr.wcrbackend.DTO.Project;
import com.wcr.wcrbackend.DTO.Year;
import com.wcr.wcrbackend.dms.dto.ProjectDTO;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IProjectService;

import java.io.IOException;
import java.io.Writer;
import org.apache.poi.ss.usermodel.DataFormatter;


@RestController
@RequestMapping("/projects")
public class ProjectsController {
	
	Logger logger = Logger.getLogger(ProjectsController.class);

	
	@Autowired
	private IProjectService projectService;
	
	@Value("${template.upload.formatError}")
	public String uploadformatError;
	
	
    
    @GetMapping("/api/projectTypes")
    public ResponseEntity<List<Project>> getProjectTypes() throws Exception {
        return ResponseEntity.ok(projectService.getProjectTypeDetails());
    }

    @GetMapping("/api/railwayZones")
    public ResponseEntity<List<Project>> getRailwayZones() throws Exception {
        return ResponseEntity.ok(projectService.getRailwayZones());
    }

    @GetMapping("/api/yearList")
    public ResponseEntity<List<Year>> getYearList() throws Exception {
        return ResponseEntity.ok(projectService.getYearList());
    }

    @GetMapping("/api/divisions")
    public ResponseEntity<List<Project>> getDivisions() throws Exception {
        return ResponseEntity.ok(projectService.getAllDivisions());
    }

    @GetMapping("/api/sections")
    public ResponseEntity<List<Project>> getSections() throws Exception {
        return ResponseEntity.ok(projectService.getAllSections());
    }

    @GetMapping("/api/fileTypes")
    public ResponseEntity<List<Project>> getProjectFileTypes() throws Exception {
        return ResponseEntity.ok(projectService.getProjectFileTypes());
    }  

    @GetMapping("/api/getProjects")
    public ResponseEntity<?> getProjects(Project project) {
        try {
            List<Project> projectList = projectService.getProjectList(project);
            return ResponseEntity.ok(projectList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error fetching projects: " + e.getMessage());
        }
    }
    
    @GetMapping("/api/getProjectList")
    public List<Project> getProjectList() throws Exception{
        try {
            return projectService.getProjectList(new Project()); // or pass filter
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    } 
    
    @GetMapping("/api/getProject/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable("projectId") String projectId) {
        try {
            Project project = projectService.getProject(projectId, new Project());
            if (project == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
            }
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error fetching project: " + e.getMessage());
        }
    }

    @PostMapping(value = "/api/addProject", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> addProjectAjax(@RequestBody Project project, HttpSession session) {
        try {
            String user_Id = (String) session.getAttribute("userId");
            String userName = (String) session.getAttribute("userName");
            String userDesignation = (String) session.getAttribute("designation");

            project.setCreated_by_user_id_fk(user_Id);
            project.setUser_name(userName);
            project.setDesignation(userDesignation);
            project.setCreated_by(userName);

            boolean flag = projectService.addProject(project);

            if (flag) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Project added successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("success", false, "message", "Adding project failed"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error: " + e.getMessage()));
        }
    }

    @PutMapping("/api/updateProject/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable("projectId") String projectId, @RequestBody Project project) {
        try {
            project.setProject_id(projectId);
            boolean flag = projectService.updateProject(project);
            if (flag) {
                return ResponseEntity.ok("Project updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update project");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error updating project: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/deleteProject/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable("projectId") String projectId) {
        try {
            boolean flag = projectService.deleteProject(projectId, new Project());
            if (flag) {
                return ResponseEntity.ok("Project deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete project");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error deleting project: " + e.getMessage());
        }
    }
    
    @GetMapping("/get-project-name")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(HttpSession session) {
    	User user = (User) session.getAttribute("user");
    	List<ProjectDTO> dtos =  projectService.getProjects(user.getUserId(), user.getUserRoleNameFk());
    	dtos.sort(Comparator.comparing(ProjectDTO::getName));
    	return ResponseEntity.ok(dtos);
    }
    
    
    @PostMapping("/api/get-divisions")
    public Map<String, Object> getDivisionsForRailwayZone(String railwayZone, HttpSession session) throws Exception {
    	
    	Map<String, Object> res = new HashMap<>();
    	
    	List<Project> list = projectService.getAllDivisionsForRailWayZone(railwayZone);
//    	if(!list.isEmpty()) {
//        	res.put("divisionsList", list);
//        	return res;
//    	}
//    	else return null;
//    }
    	
    	res.put("divisionsList", list);
    	return res;
    }
    
    
    
    
    private  String[]  uploadProjectChainages(Project obj, String userId, String userName) throws Exception {
		Project project = null;
		List<Project> workChainagesList = new ArrayList<Project>();
		String[] result = new String[5];
		Writer w = null;
		int count = 0;
		try {	
			MultipartFile excelfile = obj.getProjectChainagesFile();
			if (!StringUtils.isEmpty(excelfile) && excelfile.getSize() > 0 ){
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				int sheetsCount = workbook.getNumberOfSheets();
				if(sheetsCount > 0) {
					
					XSSFSheet laSheet = workbook.getSheetAt(0);
					
					DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
					for(int i = 1; i <= laSheet.getLastRowNum();i++){
						int v = laSheet.getLastRowNum();
						XSSFRow row = laSheet.getRow(i);
						project = new Project();
						
						String val = null;
						if(!StringUtils.isEmpty(row)) 
						{	
							val = formatter.formatCellValue(row.getCell(0)).trim();
							if(!StringUtils.isEmpty(val)) { project.setSrno(val);}							
							val = formatter.formatCellValue(row.getCell(1)).trim();
							if(!StringUtils.isEmpty(val)) { project.setProject_id_fk(val);project.setProject_id(val);}
							val = formatter.formatCellValue(row.getCell(2)).trim();
							if(!StringUtils.isEmpty(val)) { project.setChainages(val);}
							val = formatter.formatCellValue(row.getCell(3)).trim();
							if(!StringUtils.isEmpty(val)) { project.setLatitude(val);}
							val = formatter.formatCellValue(row.getCell(4)).trim();
							if(!StringUtils.isEmpty(val)) { project.setLongitude(val);}							
						}
				
						workChainagesList.add(project);
					}
					
					if(!workChainagesList.isEmpty() && workChainagesList != null){
						String[] arr  = projectService.uploadProjectChainagesData(workChainagesList,project);
						result[0] = arr[0];
						result[1] = arr[1];
						result[2] = arr[2];
						result[3] = arr[3];
						result[4] = arr[4];
					}
					
				}
				workbook.close();
			}
						
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uploadChainages() : "+e.getMessage());
			throw new Exception(e);	
		}finally{
		    try{
		        if ( w != null)
		        	w.close( );
		    }catch ( IOException e){
		    	e.printStackTrace();
		    	logger.error("uploadChainages() : "+e.getMessage());
		    	throw new Exception(e);
		    }
		}
		
		return result;
	}
    
    
    
    
    
    
    @PostMapping("/upload-chainages")
	public Map<String, Object> uploadChainages(@ModelAttribute Project obj, HttpSession session) {
//	ModelAndView model = new ModelAndView();
    	Map<String, Object> resp = new HashMap<>();
    	
    	String msg = "";String userId = null;
	
    	try {
		userId = (String) session.getAttribute("userId");
		String userName = (String) session.getAttribute("userName");
        String userDesignation = (String) session.getAttribute("designation");
		
//		model.setViewName("redirect:/get-project/"+obj.getProject_id_fk());
		obj.setProject_id(obj.getProject_id_fk());
		
		obj.setUser_id(userId);
		obj.setUser_name(userName);
		obj.setDesignation(userDesignation);
		
		if(!StringUtils.isEmpty(obj.getProjectChainagesFile())){
			MultipartFile multipartFile = obj.getProjectChainagesFile();
			// Creates a workbook object from the uploaded excelfile
			if (multipartFile.getSize() > 0){					
				XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
				// Creates a worksheet object representing the first sheet
				int sheetsCount = workbook.getNumberOfSheets();
				if(sheetsCount > 0) {
					XSSFSheet laSheet = workbook.getSheetAt(0);
					//System.out.println(uploadFilesSheet.getSheetName());
					//header row
					XSSFRow headerRow = laSheet.getRow(0);
					//checking given file format
					if(headerRow != null){
						List<String> fileFormat = FileFormatModel.getWorkChainagesFileFormat();	
						int noOfColumns = headerRow.getLastCellNum();
						if(noOfColumns == fileFormat.size()){
							for (int i = 0; i < fileFormat.size();i++) {
			                	//System.out.println(headerRow.getCell(i).getStringCellValue().trim());
			                	//if(!fileFormat.get(i).trim().equals(headerRow.getCell(i).getStringCellValue().trim())){
								String columnName = headerRow.getCell(i).getStringCellValue().trim();
								if(!columnName.equals(fileFormat.get(i).trim()) && !columnName.contains(fileFormat.get(i).trim())){
									
//			                		attributes.addFlashAttribute("error",uploadformatError);
			                		resp.put("error", uploadformatError);
			                		msg = uploadformatError;
			                		obj.setUploaded_by_user_id_fk(userId);
			                		obj.setStatus("Fail");
			                		obj.setRemarks(msg);
									boolean flag = projectService.saveProjectChainagesDataUploadFile(obj);
			                		return resp;
			                	}
							}
						}else{
//							attributes.addFlashAttribute("error",uploadformatError);
	                		resp.put("error", uploadformatError);
							msg = uploadformatError;
	                		obj.setUploaded_by_user_id_fk(userId);
	                		obj.setStatus("Fail");
	                		obj.setRemarks(msg);
							boolean flag = projectService.saveProjectChainagesDataUploadFile(obj);
	                		return resp;
						}
					}else{
//						attributes.addFlashAttribute("error",uploadformatError);
                		resp.put("error", uploadformatError);
						msg = uploadformatError;
                		obj.setUploaded_by_user_id_fk(userId);
                		obj.setStatus("Fail");
                		obj.setRemarks(msg);
                		boolean flag = projectService.saveProjectChainagesDataUploadFile(obj);
                		return resp;
					}
					String[]  result = uploadProjectChainages(obj,userId,userName);
					String errMsg = result[0];String actualVal = "";
					int count = 0,row = 0,sheet = 0,subRow = 0;
					List<String> fileFormat = FileFormatModel.getRRFileFormat();
					if(!StringUtils.isEmpty(result[1])){count = Integer.parseInt(result[1]);}
					if(!StringUtils.isEmpty(result[2])){row = Integer.parseInt(result[2]);}
					if(!StringUtils.isEmpty(result[3])){sheet = Integer.parseInt(result[3]);}
					if(!StringUtils.isEmpty(result[4])){subRow = Integer.parseInt(result[4]);}
					//System.out.println(errMsg);
					if(!StringUtils.isEmpty(errMsg)) {
						if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Duplicate entry")) {
//							attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;<b>Work and RR Id Mismatch at row: ("+row+")</b> please check and Upload again.</span>");
							
							resp.put("error", "Work and R & R Id Mismatch at row: "+row);
							msg = "Work and R & R Id Mismatch at row: "+row;
						}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Data truncated")) {
							actualVal = Integer.toString(subRow);
							if(sheet == 1) {subRow = row; 
								String error = "Data truncated";
								actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
							} 
//							attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect Value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
							msg = "Incorrect value identified in Sheet: "+sheet+" at row: "+actualVal;
							resp.put("error", msg);

						}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Cannot add or update a child row")) {
							actualVal = Integer.toString(subRow);
							if(sheet == 1) {subRow = row;
								String error = "Cannot add or update a child row";
								actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
							}
//							attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect Value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
							msg = "Incorrect value identified in Sheet: "+sheet+" at row: "+actualVal;
							resp.put("error", msg);
						}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Incorrect date value")) {
							actualVal = Integer.toString(subRow);
							if(sheet == 1) {subRow = row;
								String error = "Incorrect date value";
								actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
							}
//							attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect date value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
							msg = "Incorrect date value identified in Sheet: "+sheet+" at row: "+actualVal;
							resp.put("error", msg);
						}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Incorrect integer value")) {
							actualVal = Integer.toString(subRow);
							if(sheet == 1) {subRow = row; 
								String error = "Incorrect integer value";
								actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
							}
//							attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect integer value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
							msg = "Incorrect integer value identified in Sheet: "+sheet+" at row: "+actualVal;
							resp.put("error", msg);
						}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Incorrect decimal value")) {
							actualVal = Integer.toString(subRow);
							if(sheet == 1) {subRow = row;
								String error = "Incorrect decimal value";
								actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
							}
//							attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect decimal value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
							msg = "Incorrect decimal value identified in Sheet: "+sheet+" at row: "+actualVal;
							resp.put("error", msg);
						}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Data too long for column")) {
							actualVal = Integer.toString(subRow);
							if(sheet == 1) {subRow = row;
								String error = "Data too long for column";
								actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
							}
//							attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Data too long for value in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
							msg = "Incorrect decimal value identified in Sheet: "+sheet+" at row: "+actualVal;
							resp.put("error", msg);
						}
					
                		obj.setUploaded_by_user_id_fk(userId);
                		obj.setStatus("Fail");
                		obj.setRemarks(msg);
						boolean flag = projectService.saveProjectChainagesDataUploadFile(obj);
                		return resp;
					}
				
					if(count > 0) {
//						attributes.addFlashAttribute("success","<i class='fa fa-check'></i>&nbsp;"+ count + "<span style='color:green;'> records Uploaded successfully.</span>");
						resp.put("success", count + " records Uploaded successfully.");
						msg = count + " records Uploaded successfully.";
						
					}else {
//						attributes.addFlashAttribute("success"," No records found.");	
						resp.put("success"," No records found.");	
						msg = " No records found.";
					}
					obj.setUploaded_by_user_id_fk(userId);
            		obj.setStatus("Success");
            		obj.setRemarks(msg);
					boolean flag = projectService.saveProjectChainagesDataUploadFile(obj);
				}
				workbook.close();
			}
		} else {
//			attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
			resp.put("error", "Something went wrong. Please try after some time");
			msg = "No file exists";
			obj.setUploaded_by_user_id_fk(userId);
    		obj.setStatus("Fail");
    		obj.setRemarks(msg);
			boolean flag = projectService.saveProjectChainagesDataUploadFile(obj);
		}
		
	} catch (Exception e) {
		e.printStackTrace();
//		attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
		resp.put("error", "Something went wrong. Please try after some time");

		logger.fatal("updateDataDate() : "+e.getMessage());
		msg = "Something went wrong. Please try after some time";
		obj.setUploaded_by_user_id_fk(userId);
		obj.setStatus("Fail");
		obj.setRemarks(msg);
	
		try {
			boolean flag = projectService.saveProjectChainagesDataUploadFile(obj);
		} catch (Exception e1) {
//			attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
			resp.put("error", "Something went wrong. Please try after some time");

			logger.fatal("saveDesignDataUploadFile() : "+e.getMessage());
		}
	}
	return resp;
}
    
    
    
    
    
    
}
