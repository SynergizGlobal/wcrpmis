package com.wcr.wcrbackend.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcr.wcrbackend.DTO.FileFormatModel;
import com.wcr.wcrbackend.DTO.P6Data;
import com.wcr.wcrbackend.DTO.UDFDataDTO;
import com.wcr.wcrbackend.DTO.UDFDataDTO1;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.common.CommonConstants2;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.common.FileUploads;
import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.service.P6NewDataService;
import com.wcr.wcrbackend.service.UserService;

import jakarta.servlet.http.HttpSession;


@RestController
public class P6NewDataController {
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	Logger logger = Logger.getLogger(P6NewDataController.class);

	@Autowired
	P6NewDataService p6newdataService;
	
	@Autowired
	UserService userService;

	@Value("${template.upload.common.error}")
	public String uploadCommonError;

	@Value("${template.upload.formatError}")
	public String uploadformatError;

	@RequestMapping(value = "/p6-new-data", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView P6Data(@ModelAttribute P6Data obj, HttpSession session) {
		ModelAndView model = new ModelAndView(PageConstants.P6NewData);
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());

			List<P6Data> projectsList = p6newdataService.getProjectsList(obj);
			model.addObject("projectsList", projectsList);			
			
			List<P6Data> contractsList = p6newdataService.getContractsList(obj);
			model.addObject("contractsList", contractsList);
			
			List<P6Data> fobList = p6newdataService.getFobList(obj);
			model.addObject("fobList", fobList);

			/*
			 * List<P6Data> contractsListFilter =
			 * p6newdataService.getContractsListFilter(obj);
			 * model.addObject("contractsListFilter", contractsListFilter);
			 * 
			 * List<P6Data> fobListFilter = p6newdataService.getFobListFilter(obj);
			 * model.addObject("fobListFilter", fobListFilter);
			 * 
			 * List<P6Data> uploadTypes = p6newdataService.getUploadTypesFilter(obj);
			 * model.addObject("uploadTypes", uploadTypes);
			 * 
			 * List<P6Data> statusList = p6newdataService.getStatusListFilter(obj);
			 * model.addObject("statusList", statusList);
			 */

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("P6Data : " + e.getMessage());
		}
		return model;
	}
	
	@PostMapping("/p6-new-data-new")
    public Map<String, Object> P6Datanew(HttpSession session) {
		P6Data obj = new P6Data();
        Map<String, Object> response = new HashMap<>();
        try {
            User uObj = (User) session.getAttribute("user");

			if (uObj != null) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				// setting the User role code directly to object if UserRole is IT Admin
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));

				obj.setUser_id(uObj.getUserId());
			}

			List<P6Data> projectsList = p6newdataService.getProjectsList(obj);
			response.put("projectsList", projectsList);

            List<P6Data> contractsList = p6newdataService.getContractsList(obj);
            response.put("contractsList", contractsList);
            
            
            List<P6Data> fobList = p6newdataService.getFobList(obj);
            response.put("fobList", fobList);


            // Optional (uncomment if needed)
            /*
            response.put("fobList", p6newdataService.getFobList(obj));
            response.put("contractsListFilter", p6newdataService.getContractsListFilter(obj));
            response.put("fobListFilter", p6newdataService.getFobListFilter(obj));
            response.put("uploadTypes", p6newdataService.getUploadTypesFilter(obj));
            response.put("statusList", p6newdataService.getStatusListFilter(obj));
            */

            response.put("status", "SUCCESS");

        } catch (Exception e) {
            logger.error("P6Data API error", e);
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
        }

        return response;
    }
	

	@RequestMapping(value = "/ajax/getProjectListInP6New", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<P6Data> getProjectListInP6New(@ModelAttribute P6Data obj, HttpSession session) {
		List<P6Data> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = p6newdataService.getProjectsList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractListInP6New : " + e.getMessage());
		}
		return objList;
	}


	@RequestMapping(value = "/ajax/getContractListInP6New", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<P6Data> getContractListInP6New(@ModelAttribute P6Data obj, HttpSession session) {
		List<P6Data> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			System.out.println("The : "+uObj.getUserTypeFk() );
			System.out.println("The : "+obj.getUser_type_fk() );
			System.out.println();
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));

			System.out.println("The : "+uObj.getUserRoleNameFk() );
			System.out.println("The : "+obj.getUser_role_code() );
System.out.println();
			obj.setUser_id(uObj.getUserId());
			System.out.println("The : "+uObj.getUserId() );
			System.out.println("The : "+obj.getUser_id() );
System.out.println();
			objList = p6newdataService.getContractsList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractListInP6New : " + e.getMessage());
		}
		return objList;
	}

	@RequestMapping(value = "/ajax/getContractsListFilterInP6New", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<P6Data> getContractsListFilterInP6(@ModelAttribute P6Data obj) {
		List<P6Data> objList = null;
		try {
			objList = p6newdataService.getContractsListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractsListFilterInP6 : " + e.getMessage());
		}
		return objList;
	}

	@RequestMapping(value = "/ajax/getFobListFilterInP6New", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<P6Data> getFobListFilterInP6(@ModelAttribute P6Data obj) {
		List<P6Data> objList = null;
		try {
			objList = p6newdataService.getFobListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getFobListFilterInP6 : " + e.getMessage());
		}
		return objList;
	}

	@RequestMapping(value = "/ajax/getUploadTypesFilterInP6New", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<P6Data> getUploadTypesFilterInP6(@ModelAttribute P6Data obj) {
		List<P6Data> objList = null;
		try {
			objList = p6newdataService.getUploadTypesFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUploadTypesFilterInP6 : " + e.getMessage());
		}
		return objList;
	}

	@RequestMapping(value = "/ajax/getStatusListFilterInP6New", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<P6Data> getStatusListFilterInP6(@ModelAttribute P6Data obj) {
		List<P6Data> objList = null;
		try {
			objList = p6newdataService.getStatusListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getStatusListFilterInP6 : " + e.getMessage());
		}
		return objList;
	}

	@RequestMapping(value = "/ajax/getP6NewActivityData", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<P6Data> getP6NewActivityData(@RequestBody P6Data obj) {
		List<P6Data> objList = null;
		try {
			objList = p6newdataService.getActivityDataList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getP6NewActivityData : " + e.getMessage());
		}
		return objList;
	}

	@RequestMapping(value = "/ajax/getFobListInP6New", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<P6Data> getFobListInP6(@ModelAttribute P6Data obj) {
		List<P6Data> objList = null;
		try {
			objList = p6newdataService.getFobList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getFobListInP6 : " + e.getMessage());
		}
		return objList;
	}




	public Map<String, Object> uploadP6BaselineCSVFile(@ModelAttribute P6Data p6data, RedirectAttributes attributes, HttpSession session) {
//	    ModelAndView model = new ModelAndView();
//	    model.setViewName("redirect:/p6-new-data");

		Map<String, Object> res = new HashMap<>();
		
	    XSSFWorkbook workbook = null;
	    XSSFSheet sheet = null;
	    String fob_mismatch = null;
	    List<P6Data> wbsList = new ArrayList<>();
	    List<P6Data> activitiesList = new ArrayList<>();

	    try {
	        User uObj = (User) session.getAttribute("user");
	        String userId = uObj.getUserId();
	        p6data.setCreated_by_user_id_fk(userId);
	        p6data.setUser_name(uObj.getUserName());
	        p6data.setDesignation(uObj.getDesignation());

	        MultipartFile multipartFile = p6data.getP6dataFile();
	        if (multipartFile != null && multipartFile.getSize() > 0) {
	            String fileName = multipartFile.getOriginalFilename();
	            workbook = new XSSFWorkbook(multipartFile.getInputStream());
	            sheet = workbook.getSheetAt(0);

	            if (sheet != null) {
	                // Define date columns dynamically, e.g., by header names
	                List<String> dateColumnHeaders = Arrays.asList("base_start_date", "base_end_date", "start_date", "end_date");
	                Map<Integer, String> dateColumns = new HashMap<>();

	                // Identify date columns by header names
	                XSSFRow headerRow = sheet.getRow(0);
	                for (Cell cell : headerRow) {
	                    if (dateColumnHeaders.contains(cell.getStringCellValue().trim())) {
	                        dateColumns.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
	                    }
	                }

	                List<String> modifiedCells = new ArrayList<>();
	                for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Skip header row
	                    XSSFRow row = sheet.getRow(rowIndex);
	                    if (row != null) {
	                        for (Map.Entry<Integer, String> entry : dateColumns.entrySet()) {
	                            XSSFCell cell = row.getCell(entry.getKey());
	                            if (cell != null && cell.getCellType() == CellType.STRING) {
	                                String cellValue = cell.getStringCellValue().trim();
	                                if (!isValidDate(cellValue)) {
	                                    modifiedCells.add(cell.getAddress().formatAsString());
	                                }
	                            } else if (cell != null && cell.getCellType() == CellType.FORMULA) {
	                                try {
	                                    cell.getDateCellValue();
	                                } catch (Exception e) {
	                                    modifiedCells.add(cell.getAddress().formatAsString());
	                                }
	                            }
	                        }
	                    }
	                }

	                if (!modifiedCells.isEmpty()) {
	                    String errorMessage = "Mismatch found in the cell(s): " + String.join(", ", modifiedCells);
//	                    attributes.addFlashAttribute("error", errorMessage);
	                    res.put("error", errorMessage);
	                    workbook.close();
	                    return res;
	                }

	                p6data.setP6_file_path(fileName);
	                activitiesList = baselineActivitiesUpload(p6data, workbook);

	                if (activitiesList.size() == 0) {
	                    fob_mismatch = "Sheet is empty.";
	                }

	                workbook.close();
	                String saveDirectory = CommonConstants2.P6_FILE_SAVING_PATH;
	                FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName);
	            }
	        }

	        p6data.setUploaded_by_user_id_fk(userId);
	        p6data.setCreated_by_user_id_fk(userId);
	        p6data.setModified_by_user_id_fk(userId);
	        p6data.setData_date(DateParser.parse(p6data.getData_date()));
	        p6data.setUpload_type("Baseline");

	        if (StringUtils.isEmpty(fob_mismatch)) {
	            try {
	                String counts = p6newdataService.uploadP6WBSActivities(wbsList, activitiesList, p6data);
//	                attributes.addFlashAttribute("error", counts);
	                res.put("success", counts);
	            } catch (Exception e) {
	                handleException(e, attributes);
	                res.put("error", e);
	            }
	        } else {
	    //        attributes.addFlashAttribute("error", "<br><span style='color:red;'>" + fob_mismatch + "</span> ");
	            res.put("error",fob_mismatch);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	      //  attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
	        res.put("error", "Something went wrong. Please try after some time");
	        logger.fatal("uploadP6Baseline() : " + e.getMessage());
	    }
	    return res;
	}

	private boolean isValidDate(String date) {
	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	        sdf.setLenient(false);
	        sdf.parse(date);
	        return true;
	    } catch (ParseException e) {
	        return false;
	    }
	}

	private void handleException(Exception e, RedirectAttributes attributes) {
	    String lineErr = e.getMessage();
	    String[] err = lineErr.split("column");
	    String[] err2 = err[1].split(" ");
	    String column = err2[1];
	    String rNo = err2[4];
	    int row = Integer.parseInt(rNo) + 2;
	    column = column.replace("_", " ");
	    column = StringUtils.capitalize(column);
	    String fob_mismatch = null;

	    if (lineErr.contains("Cannot add or update a child row")) {
	        fob_mismatch = "Incorrect  Value identified, Please check and try again.  ";
	    } else if (lineErr.contains("Incorrect integer value") || lineErr.contains("Data truncated for column") || lineErr.contains("Incorrect decimal value")) {
	        fob_mismatch = "Incorrect Format for column <b>" + column + "</b>, Please check and try again.  ";
	    } else if (lineErr.contains("Incorrect date value")) {
	        fob_mismatch = "Incorrect Date for column <b>" + column + "</b>, Please check and try again.  ";
	    } else if (lineErr.contains("Data too long for column")) {
	        fob_mismatch = "Data too long for column <b>" + column + "</b>, Please check and try again.  ";
	    }

	    attributes.addFlashAttribute("error", "<br><span style='color:red;'>" + fob_mismatch + "</span> ");
	}

	
	
	
	
	private List<P6Data> baselineWBSUpload(P6Data obj,XSSFWorkbook workbook, String fob_mismatch) throws Exception {
		P6Data p6data = null;
		List<P6Data> wbsList = new ArrayList<P6Data>();
		XSSFSheet uploadFilesSheet = null;
		try {	
			int sheetsCount = workbook.getNumberOfSheets();
			if(sheetsCount > 0) {
				uploadFilesSheet = workbook.getSheetAt(0);
				for(int i = 2; i<= uploadFilesSheet.getLastRowNum();i++){
					XSSFRow row = uploadFilesSheet.getRow(i);
					// Sets the Read data to the model class
					DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
					// Cell cell = row.getCell(0);
					// String j_username = formatter.formatCellValue(row.getCell(0));
					
					p6data = new P6Data();
					/*if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(0)).trim()))
						p6data.setContract_id_fk(formatter.formatCellValue(row.getCell(0)).trim());*/
					/*if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(1)).trim()))
						p6data.setFob_id_fk(formatter.formatCellValue(row.getCell(1)).trim());*/
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(0)).trim()))
						p6data.setP6_wbs_code(formatter.formatCellValue(row.getCell(0)).trim());
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(1)).trim()))
						p6data.setP6_wbs_name(formatter.formatCellValue(row.getCell(1)).trim());
					/*if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(3)).trim()))
						p6data.setP6_wbs_parent_code(formatter.formatCellValue(row.getCell(3)).trim());*/
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(2)).trim()))
						p6data.setP6_wbs_category_fk(formatter.formatCellValue(row.getCell(2)).trim());
					
					
					if(!StringUtils.isEmpty(p6data) && !StringUtils.isEmpty(obj.getContract_id_fk()) && !StringUtils.isEmpty(p6data.getP6_wbs_code())) {
						wbsList.add(p6data);
					} 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("baselineWBSUpload() : "+e.getMessage());
			throw new Exception(e);	
		}		
		return wbsList;
	}
	
	private List<P6Data> baselineActivitiesUpload(P6Data pObj,XSSFWorkbook workbook) throws Exception {
		P6Data p6data = null;
		List<P6Data> pObjList = new ArrayList<P6Data>();
		XSSFSheet uploadFilesSheet = null;
		try {		
			int sheetsCount = workbook.getNumberOfSheets();
			if(sheetsCount > 0) {
				uploadFilesSheet = workbook.getSheetAt(0);
				//System.out.println(uploadFilesSheet.getSheetName());
				//header row
				//XSSFRow headerRow = uploadFilesSheet.getRow(0);							
				
				for(int i = 2; i<= uploadFilesSheet.getLastRowNum();i++){
					XSSFRow row = uploadFilesSheet.getRow(i);
					// Sets the Read data to the model class
					DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
					// Cell cell = row.getCell(0);
					// String j_username = formatter.formatCellValue(row.getCell(0));
					
					p6data = new P6Data();
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(0)).trim()))
						p6data.setP6_task_code(formatter.formatCellValue(row.getCell(0)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(1)).trim()))
						p6data.setStructure_type_fk(formatter.formatCellValue(row.getCell(1)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(2)).trim()))
						p6data.setStructure(formatter.formatCellValue(row.getCell(2)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(3)).trim()))
						p6data.setComponent(formatter.formatCellValue(row.getCell(3)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(4)).trim()))
						p6data.setComponent_id(formatter.formatCellValue(row.getCell(4)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(5)).trim()))
						p6data.setP6_activity_name(formatter.formatCellValue(row.getCell(5)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(6)).trim()))
						p6data.setWeightage(formatter.formatCellValue(row.getCell(6)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(7)).trim()))
						p6data.setUnit(formatter.formatCellValue(row.getCell(7)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(8)).trim()))
						p6data.setScope(formatter.formatCellValue(row.getCell(8)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(9)).trim()))
						p6data.setCompleted(formatter.formatCellValue(row.getCell(9)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(10)).trim()))
						p6data.setBaseline_start(formatter.formatCellValue(row.getCell(10)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(11)).trim()))
						p6data.setBaseline_finish(formatter.formatCellValue(row.getCell(11)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(12)).trim()))
						p6data.setStart(formatter.formatCellValue(row.getCell(12)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(13)).trim()))
						p6data.setFinish(formatter.formatCellValue(row.getCell(13)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(14)).trim()))
						p6data.setSection(formatter.formatCellValue(row.getCell(14)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(15)).trim()))
						p6data.setLine(formatter.formatCellValue(row.getCell(15)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(16)).trim()))
						p6data.setFrom_structure_id(formatter.formatCellValue(row.getCell(16)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(17)).trim()))
						p6data.setTo_structure_id(formatter.formatCellValue(row.getCell(17)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(18)).trim()))
						p6data.setRemarks(formatter.formatCellValue(row.getCell(18)).trim());
					
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(19)).trim()))
						p6data.setStatus_fk(formatter.formatCellValue(row.getCell(19)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(20)).trim()))
						p6data.setP6_wbs_code_fk(formatter.formatCellValue(row.getCell(20)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(21)).trim()))
						p6data.setOriginal_duration(formatter.formatCellValue(row.getCell(21)).trim());
					
					if(!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(22)).trim()))
						p6data.setP6_float(formatter.formatCellValue(row.getCell(22)).trim());
					
					p6data.setBaseline_start(DateParser.parse(p6data.getBaseline_start()));
					p6data.setBaseline_finish(DateParser.parse(p6data.getBaseline_finish()));
					p6data.setStart(DateParser.parse(p6data.getStart()));
					p6data.setFinish(DateParser.parse(p6data.getFinish()));
					
					
					if(!StringUtils.isEmpty(p6data) && !StringUtils.isEmpty(p6data.getP6_task_code())) {
						pObjList.add(p6data);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("baselineActivitiesUpload() : "+e.getMessage());
			throw new Exception(e);	
		}
		
		return pObjList;
	}


//
	@PostMapping("/upload-p6-new-data")
	@ResponseBody
	public Map<String, Object> uploadP6Baseline(
	        @ModelAttribute P6Data p6data,
	        HttpSession session
	) {

	    Map<String, Object> res = new HashMap<>();

	    MultipartFile file = p6data.getP6dataFile();

	    try {
	        if (file == null || file.isEmpty()) {
	            res.put("error", "Please select a file");
	            return res;
	        }

	        String filename = file.getOriginalFilename().toLowerCase();

	        if (filename.endsWith(".xer")) {
	            return uploadP6BaselineXERFile(p6data, null, session);
	        } else {
	            return uploadP6BaselineCSVFile(p6data, null, session);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        res.put("status", "ERROR");
	        res.put("message", e.getMessage());
	        return res;
	    }
	}


	public Map<String, Object> uploadP6BaselineXERFile(@ModelAttribute P6Data p6data, RedirectAttributes attributes,
			HttpSession session) throws Exception {
//		ModelAndView model = new ModelAndView();
//
//		model.setViewName("redirect:/p6-new-data");
		 Map<String, Object> res = new HashMap<>();
		
//		System.out.println("entered to file upload ");

		String expectedHeader = "%F\ttask_id\tproj_id\twbs_id\tclndr_id\tphys_complete_pct\t"
				+ "rev_fdbk_flag\test_wt\tlock_plan_flag\tauto_compute_act_flag\t"
				+ "complete_pct_type\ttask_type\tduration_type\tstatus_code\t"
				+ "task_code\ttask_name\trsrc_id\ttotal_float_hr_cnt\tfree_float_hr_cnt\t"
				+ "remain_drtn_hr_cnt\tact_work_qty\tremain_work_qty\t"
				+ "target_work_qty\ttarget_drtn_hr_cnt\ttarget_equip_qty\t"
				+ "act_equip_qty\tremain_equip_qty\tcstr_date\tact_start_date\t"
				+ "act_end_date\tlate_start_date\tlate_end_date\texpect_end_date\t"
				+ "early_start_date\tearly_end_date\trestart_date\treend_date\t"
				+ "target_start_date\ttarget_end_date\trem_late_start_date\trem_late_end_date\t"
				+ "cstr_type\tpriority_type\tsuspend_date\tresume_date\tfloat_path\t"
				+ "float_path_order\tguid\ttmpl_guid\tcstr_date2\tcstr_type2\t"
				+ "driving_path_flag\tact_this_per_work_qty\tact_this_per_equip_qty\t"
				+ "external_early_start_date\texternal_late_end_date\tcreate_date\t"
				+ "update_date\tcreate_user\tupdate_user\tlocation_id\tcrt_path_num";

		String header = "";
		MultipartFile multipartFile = p6data.getP6dataFile();
		if (multipartFile != null && multipartFile.getSize() > 0) {
//			System.out.println("entered to not null ");
			List<P6Data> wbsList = new ArrayList<>();

			List<UDFDataDTO> dataDTOs = new ArrayList<>();
			List<UDFDataDTO1> data = new ArrayList<>();

			List<Map<String, String>> dataRows = new ArrayList<>(); // List of maps to store row data by column name
//			System.out.println("Received file: " + multipartFile.getOriginalFilename());
//			System.out.println("File size: " + multipartFile.getSize());
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8))) {
				String line;
//				System.out.println("entered buffer ");

				String udfHeader = "%F	udf_type_id	fk_id	proj_id	udf_date	udf_number	udf_text	udf_code_id";

				boolean status = false;
				boolean TASKPRED = false;

				while ((line = reader.readLine()) != null) {
					if (line.contains("%F")) {
						
					}
					if (line.contains(expectedHeader)) {
						header = line; // Store the header

					}

					else if (line.contains("%T	TASKPRED")) {
						TASKPRED = true;
					} else if (TASKPRED) {
						if (udfHeader.equals(line)) {
							status = true;
							TASKPRED = false;
						}
						continue;
					} else if (line.contains("%R") && status) {
						String[] columns = line.split("\t");
						UDFDataDTO dataDTO = new UDFDataDTO(columns);

						if (dataDTO.getUdf_type_id() == null || dataDTO.getUdf_type_id().trim().equals("")) {

							/*
							 * System.out.println("null object:  " + dataDTO);
							 */						}

						dataDTOs.add(dataDTO);
					}

					else if (line.contains("%R") && !header.isEmpty()) {
						// Split the row into columns
						String[] columns = line.split("\t");
						String[] headerColumns = expectedHeader.split("\t"); // Split the expected header into columns

						// Map the columns to their corresponding header names
						Map<String, String> rowMap = new HashMap<>();
						for (int i = 0; i < headerColumns.length; i++) {
							if (i < columns.length) {

								rowMap.put(headerColumns[i], columns[i]); // Associate column name with the value

							}
						}
						dataRows.add(rowMap); // Store the map in the list of rows

					}
				}

				// step2:
				Map<String, String> map = UDFUtil.getData(dataDTOs);

				// step3:
				data = UDFUtil.convertData(map);


			} catch (IOException e) {
				System.err.println("Error reading the file: " + e.getMessage());
				e.printStackTrace();
			}
		

			// Convert the dataRows to JSON for debugging purposes
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				String jsonOutput = objectMapper.writeValueAsString(dataRows); // Convert List<Map> to JSON string
//				System.out.println("json output:" + jsonOutput);
			} catch (IOException e) {
				System.err.println("Error converting to JSON: " + e.getMessage());
				e.printStackTrace();
			}

			// Set user information
			String userId = (String) session.getAttribute("USER_ID");
			String userName = (String) session.getAttribute("USER_NAME");
			String userDesignation = (String) session.getAttribute("USER_DESIGNATION");

			p6data.setCreated_by_user_id_fk(userId);
			p6data.setUser_name(userName);
			p6data.setDesignation(userDesignation);
		

			String fileName = multipartFile.getOriginalFilename();
			p6data.setP6_file_path(fileName);

			List<P6Data> activitiesList = new ArrayList<>();
//			int abc = 0;
			Map<String, UDFDataDTO1> map = UDFUtil.convertToMap(data);

		
			for (Map<String, String> rowData : dataRows) {
				P6Data activity = new P6Data();
				activity.setP6_task_code(rowData.get("task_code"));
				activity.setP6_activity_name(rowData.get("task_name"));
				activity.setFob_id(rowData.get("proj_id"));
				activity.setP6_wbs_code(rowData.get("wbs_id"));
				activity.setTask_id(rowData.get("task_id"));
				activity.setBaseline_start(rowData.get("restart_date"));
				activity.setBaseline_finish(rowData.get("reend_date"));
				activity.setStart(rowData.get("target_start_date"));
				activity.setFinish(rowData.get("target_end_date"));
				activity.setCreated_date(rowData.get("create_date"));

				activity.setContract_id_fk(rowData.get("contract_id_fk"));

				UDFDataDTO1 dataDTO1 = map.get(activity.getTask_id());

				if (dataDTO1 != null) {
					activity.setComponent(dataDTO1.getComponent());
					activity.setComponent_id(dataDTO1.getComponent_id());
					activity.setStructure(dataDTO1.getStructure());
					activity.setStructure_type_fk(dataDTO1.getStructure_type_fk());
					activity.setWeightage(dataDTO1.getWeightages());
					activity.setUnit(dataDTO1.getUnit());
					activity.setScope(dataDTO1.getScope());

				} else {
					System.out.println(activity.getTask_id() + " is not present in UDF map");
				}


				activitiesList.add(activity);
			}

			String fob_mismatch = null;
			if (activitiesList.size() == 0) {
				fob_mismatch = "Sheet is empty.";
			}

			p6data.setUploaded_by_user_id_fk(userId);
			p6data.setCreated_by_user_id_fk(userId);
			p6data.setModified_by_user_id_fk(userId);
			p6data.setData_date(DateParser.parse(p6data.getData_date()));
			p6data.setUpload_type("Baseline");

			if (StringUtils.isEmpty(fob_mismatch)) {
				try {
//					System.out.println("upload the p6 data before");
					String counts = p6newdataService.uploadP6WBSActivities(wbsList, activitiesList, p6data);
//					System.out.println("uploaded successfully after");
//					attributes.addFlashAttribute("error", counts);
					res.put("error", counts);

				} catch (Exception e) {
					handleException(e, attributes);
					System.out.println("file failed ");

				}
			} else {
			//	attributes.addFlashAttribute("error", "<br><span style='color:red;'>" + fob_mismatch + "</span> ");
				res.put("error", fob_mismatch);
			}

			// Save the file to the specified directory
			try {
				System.out.println("before path");

				String saveDirectory = CommonConstants2.P6_FILE_SAVING_PATH;
				FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName);
				System.out.println("file uploaded:" + fileName);
			} catch (Exception e) {
				System.err.println("Error saving file: " + e.getMessage());
				e.printStackTrace();
			}
		}

		return res;
	}

	@RequestMapping(value = "/revised-p6-new-activities", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> revisedP6NewActivities(
			@ModelAttribute P6Data p6data, RedirectAttributes attributes,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		XSSFWorkbook workbook = null;
		String fob_mismatch = null;
		XSSFSheet uploadFilesSheet = null;
		List<P6Data> activitiesList = new ArrayList<P6Data>();
		try {

			User uObj = (User) session.getAttribute("user");
			
			
 			String userId = (String) uObj.getUserId();
			String userName = (String) uObj.getUserName();
			String userDesignation = (String) uObj.getDesignation();

			p6data.setCreated_by_user_id_fk(userId);
			p6data.setUser_name(userName);
			p6data.setDesignation(userDesignation);
			p6data.setIsRevised("Yes");
			if (!StringUtils.isEmpty(p6data.getP6dataFile())) {
				MultipartFile multipartFile = p6data.getP6dataFile();
				// Creates a workbook object from the uploaded excelfile
				if (null != multipartFile && multipartFile.getSize() > 0) {
					workbook = new XSSFWorkbook(multipartFile.getInputStream());
					// Creates a worksheet object representing the first sheet
					if (workbook != null) {
						String fileName = multipartFile.getOriginalFilename();
						int sheetsCount = workbook.getNumberOfSheets();
						if (sheetsCount > 0) {
							uploadFilesSheet = workbook.getSheetAt(0);
							// System.out.println(uploadFilesSheet.getSheetName());
							// header row
							XSSFRow headerRow = uploadFilesSheet.getRow(0);
							// checking given file format
							if (headerRow != null) {
								List<String> fileFormat = FileFormatModel.getP6RevisedFileFormat();
								int noOfColumns = headerRow.getLastCellNum();
								if (noOfColumns == fileFormat.size()) {
									for (int i = 0; i < fileFormat.size(); i++) {
										// System.out.println(headerRow.getCell(i).getStringCellValue().trim());
										// if(!fileFormat.get(i).trim().equals(headerRow.getCell(i).getStringCellValue().trim())){
										String columnName = headerRow.getCell(i).getStringCellValue().trim();
										if (!columnName.contains(fileFormat.get(i).trim())) {
											response.put("error", uploadformatError);
											return response;
										}
									}
								} else {									
									response.put("error", uploadformatError);
									return response;
								}
							} else {
								response.put("error", uploadformatError);
								return response;
							}

							p6data.setP6_file_path(fileName);
							int i = 2;
							activitiesList = revisedP6NewActivities(p6data, workbook);
							if (activitiesList.size() == 0) {
								fob_mismatch = "Sheet is empty.";
							}
						}
						workbook.close();

						String saveDirectory = CommonConstants2.P6_FILE_SAVING_PATH;
						FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName);
					}
				}
			}
			p6data.setUploaded_by_user_id_fk(userId);
			p6data.setModified_by_user_id_fk(userId);
			p6data.setData_date(DateParser.parse(p6data.getData_date()));
			p6data.setUpload_type("Update");
			if (StringUtils.isEmpty(fob_mismatch)) {
				String count = null;
				try {
					count = p6newdataService.updateP6Activities(activitiesList, p6data);
					System.out.println("The count is : "+count);
					System.out.println("The count is : "+count);
					System.out.println("The count is : "+count);
					System.out.println("The count is : "+count);

					if (count.contains("^")) {
						String[] counts = count.split("\\^");
						fob_mismatch = "Contract or Activity ID Missmatch. ar row  ";
//						attributes.addFlashAttribute("error",
//								counts[0] + " Activities updated successfully. <br><span style='color:red;'>"
//										+ counts[1] + "</span> ");
						response.put("error", counts[0] + " Activities updated successfully. "+counts[1]);
					} else {
//						attributes.addFlashAttribute("success",
//								"Data date updated and " + count + " Activities updated successfully.");
						
						response.put("Success", "Data date updated and " + count + " Activities updated successfully." );
					}
				} catch (Exception e) {
					String lineErr = e.getMessage();
					String[] err = lineErr.split("column");
					String[] err2 = err[1].split(" ");
					String column = err2[1];
					String rNo = err2[4];
					int row = Integer.parseInt(rNo) + 2;
					column = column.replace("_", " ");
					column = StringUtils.capitalize(column); // at row["+row+"]
					if (lineErr.contains("Cannot add or update a child row")) {
						fob_mismatch = "Incorrect  Value identified, Please check and try again.  ";
//						attributes.addFlashAttribute("error",
//								"<br><span style='color:red;'>" + fob_mismatch + "</span> ");
						response.put("error", fob_mismatch);
					} else if (lineErr.contains("Incorrect integer value")
							|| lineErr.contains("Data truncated for column")
							|| lineErr.contains("Incorrect decimal value")) {
						fob_mismatch = "Incorrect Format for column <b>" + column
								+ "</b> , Please check and try again.  ";
//						attributes.addFlashAttribute("error",
//								"<br><span style='color:red;'>" + fob_mismatch + "</span> ");
						response.put("error", fob_mismatch);

					} else if (lineErr.contains("Incorrect date value")) {
						fob_mismatch = "Incorrect Date for column <b>" + column + "</b>, Please check and try again.  ";
//						attributes.addFlashAttribute("error",
//								"<br><span style='color:red;'>" + fob_mismatch + "</span> ");
						response.put("error", fob_mismatch);

					} else if (lineErr.contains("Data too long for column")) {
						fob_mismatch = "Data too long for column <b>" + column + "</b>, Please check and try again.  ";
//						attributes.addFlashAttribute("error",
//								"<br><span style='color:red;'>" + fob_mismatch + "</span> ");
						response.put("error", fob_mismatch);

					}

				}

			} else {
	//			attributes.addFlashAttribute("error", "<br><span style='color:red;'>" + fob_mismatch + "</span> ");
				response.put("Failed","fob_mismatch: "+fob_mismatch );
				return response;
			}

		} catch (Exception e) {
			e.printStackTrace();
		//	attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
			response.put("error", "Something went wrong. Please try again after some time.");
			logger.fatal("updateP6NewActivities() : " + e.getMessage());
		}
		return response;
	}

	private List<P6Data> revisedP6NewActivities(P6Data uObj, XSSFWorkbook workbook) throws Exception {
		P6Data p6data = null;
		List<P6Data> p6dataList = new ArrayList<P6Data>();
		XSSFSheet uploadFilesSheet = null;
		try {
			int sheetsCount = workbook.getNumberOfSheets();
			if (sheetsCount > 0) {
				uploadFilesSheet = workbook.getSheetAt(0);
				for (int i = 2; i <= uploadFilesSheet.getLastRowNum(); i++) {
					XSSFRow row = uploadFilesSheet.getRow(i);
					// Sets the Read data to the model class
					DataFormatter formatter = new DataFormatter(); // creating formatter using the default locale

					p6data = new P6Data();
					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(0)).trim()))
						p6data.setP6_task_code(formatter.formatCellValue(row.getCell(0)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(1)).trim()))
						p6data.setStatus_fk(formatter.formatCellValue(row.getCell(1)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(2)).trim()))
						p6data.setP6_wbs_code_fk(formatter.formatCellValue(row.getCell(2)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(3)).trim()))
						p6data.setP6_activity_name(formatter.formatCellValue(row.getCell(3)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(4)).trim()))
						p6data.setBaseline_start(formatter.formatCellValue(row.getCell(4)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(5)).trim()))
						p6data.setBaseline_finish(formatter.formatCellValue(row.getCell(5)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(6)).trim()))
						p6data.setStart(formatter.formatCellValue(row.getCell(6)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(7)).trim()))
						p6data.setFinish(formatter.formatCellValue(row.getCell(7)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(8)).trim()))
						p6data.setP6_float(formatter.formatCellValue(row.getCell(8)).trim());

					p6data.setBaseline_start(DateParser.parse(p6data.getBaseline_start()));
					p6data.setBaseline_finish(DateParser.parse(p6data.getBaseline_finish()));
					p6data.setStart(DateParser.parse(p6data.getStart()));
					p6data.setFinish(DateParser.parse(p6data.getFinish()));

					if (!StringUtils.isEmpty(p6data) && !StringUtils.isEmpty(p6data.getP6_task_code())) {
						p6dataList.add(p6data);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateP6NewActivities() : " + e.getMessage());
			throw new Exception(e);
		}
		return p6dataList;
	}

	@RequestMapping(value = "/update-p6-new-activities", method = { RequestMethod.POST })
	public Map<String, Object> updateP6NewActivities(@ModelAttribute P6Data p6data, RedirectAttributes attributes,
			HttpSession session) {
	//	ModelAndView model = new ModelAndView();
		Map<String, Object> response = new HashMap<>();
		
		XSSFWorkbook workbook = null;
		String fob_mismatch = null;
		XSSFSheet uploadFilesSheet = null;
		List<P6Data> activitiesList = new ArrayList<P6Data>();
		try {
		//	model.setViewName("redirect:/p6-new-data");
			User uObj = (User) session.getAttribute("user");
			
			String userId = (String) uObj.getUserId();
			String userName = (String) uObj.getUserName();
			String userDesignation = (String) uObj.getDesignation();

			p6data.setCreated_by_user_id_fk(userId);
			p6data.setUser_name(userName);
			p6data.setDesignation(userDesignation);
			p6data.setIsRevised("No");
			if (!StringUtils.isEmpty(p6data.getP6dataFile())) {
				MultipartFile multipartFile = p6data.getP6dataFile();
				// Creates a workbook object from the uploaded excelfile
				if (null != multipartFile && multipartFile.getSize() > 0) {
					workbook = new XSSFWorkbook(multipartFile.getInputStream());
					// Creates a worksheet object representing the first sheet
					if (workbook != null) {
						String fileName = multipartFile.getOriginalFilename();
						int sheetsCount = workbook.getNumberOfSheets();
						if (sheetsCount > 0) {
							uploadFilesSheet = workbook.getSheetAt(0);
							// System.out.println(uploadFilesSheet.getSheetName());
							// header row
							XSSFRow headerRow = uploadFilesSheet.getRow(1);
							// checking given file format
							if (headerRow != null) {
								List<String> fileFormat = FileFormatModel.getP6UpdateFileFormat();
								int noOfColumns = headerRow.getLastCellNum();
								if (noOfColumns == fileFormat.size()) {
									for (int i = 0; i < fileFormat.size(); i++) {
										// System.out.println(headerRow.getCell(i).getStringCellValue().trim());
										// if(!fileFormat.get(i).trim().equals(headerRow.getCell(i).getStringCellValue().trim())){
										String columnName = headerRow.getCell(i).getStringCellValue().trim();
										if (!columnName.contains(fileFormat.get(i).trim())) {
										//	 attributes.addFlashAttribute("error", uploadformatError);
											response.put("Error", uploadformatError);
											return response;
										}
									}
								} else {
								//	attributes.addFlashAttribute("error", uploadformatError);
								//	return model;
									response.put("Error", uploadformatError);
									return response;
								}
							} else {
//								attributes.addFlashAttribute("error", uploadformatError);
//								return model;
								response.put("Error", uploadformatError);
								return response;
							}

							p6data.setP6_file_path(fileName);
							int i = 2;
							activitiesList = updateP6NewActivities(p6data, workbook);

							if (activitiesList.size() == 0) {
								fob_mismatch = "Sheet is empty.";
							}
						}

						workbook.close();

						String saveDirectory = CommonConstants2.P6_FILE_SAVING_PATH;
						FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName);
					}

				}
			}
			p6data.setUploaded_by_user_id_fk(userId);
			p6data.setModified_by_user_id_fk(userId);
			p6data.setData_date(DateParser.parse(p6data.getData_date()));
			p6data.setUpload_type("Update");
			if (StringUtils.isEmpty(fob_mismatch)) {
				String count = null;
				try {
					count = p6newdataService.updateP6Activities(activitiesList, p6data);
					if (count.contains("^")) {
						String[] counts = count.split("\\^");
						fob_mismatch = "Contract or Activity ID Missmatch. ar row  ";
					//	attributes.addFlashAttribute("error", "<br><span style='color:red;'>" + counts[1] + "</span> ");
						response.put("error", counts[1]);
					} else {
					//	attributes.addFlashAttribute("success",
					//			"Data date updated and " + count + " Activities updated successfully.");
						response.put("success", "Data date updated and " + count + " Activities updated successfully.");
					}
				} catch (Exception e) {
					String lineErr = e.getMessage();
					String[] err = lineErr.split("column");
					String[] err2 = err[1].split(" ");
					String column = err2[1];
					String rNo = err2[4];
					int row = Integer.parseInt(rNo) + 2;
					column = column.replace("_", " ");
					column = StringUtils.capitalize(column);
					if (lineErr.contains("Cannot add or update a child row")) {
						fob_mismatch = "Incorrect  Value identified, Please check and try again.  ";
//						attributes.addFlashAttribute("error",
//								"<br><span style='color:red;'>" + fob_mismatch + "</span> ");
						response.put("errr", fob_mismatch);
					} else if (lineErr.contains("Incorrect integer value")
							|| lineErr.contains("Data truncated for column")
							|| lineErr.contains("Incorrect decimal value")) {
						fob_mismatch = "Incorrect Format for column <b>" + column
								+ "</b>, Please check and try again.  ";
//						attributes.addFlashAttribute("error",
//								"<br><span style='color:red;'>" + fob_mismatch + "</span> ");
						
						response.put("error", fob_mismatch);

					} else if (lineErr.contains("Incorrect date value")) {
						fob_mismatch = "Incorrect Date for column " + column + " Please check and try again.  ";
//						attributes.addFlashAttribute("error",
//								"<br><span style='color:red;'>" + fob_mismatch + "</span> ");
						response.put("error", fob_mismatch);

					} else if (lineErr.contains("Data too long for column")) {
						fob_mismatch = "Data too long for column " + column + ", Please check and try again.  ";
//						attributes.addFlashAttribute("error",
//								"<br><span style='color:red;'>" + fob_mismatch + "</span> ");
						response.put("error", fob_mismatch);

					}

				}

			} else {
			// attributes.addFlashAttribute("error", "<br><span style='color:red;'>" + fob_mismatch + "</span> ");
				response.put("error", fob_mismatch);
			}
		} catch (Exception e) {
			e.printStackTrace();
		//  attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
			response.put("error", "Something went wrong. Please try after some time");
			logger.fatal("updateP6NewActivities() : " + e.getMessage());
		}
		return response;
	}

	private List<P6Data> updateP6NewActivities(P6Data uObj, XSSFWorkbook workbook) throws Exception {
		P6Data p6data = null;
		List<P6Data> p6dataList = new ArrayList<P6Data>();
		XSSFSheet uploadFilesSheet = null;
		try {
			int sheetsCount = workbook.getNumberOfSheets();
			if (sheetsCount > 0) {
				uploadFilesSheet = workbook.getSheetAt(0);
				for (int i = 2; i <= uploadFilesSheet.getLastRowNum(); i++) {
					XSSFRow row = uploadFilesSheet.getRow(i);
					// Sets the Read data to the model class
					DataFormatter formatter = new DataFormatter(); // creating formatter using the default locale

					p6data = new P6Data();
					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(0)).trim()))
						p6data.setP6_task_code(formatter.formatCellValue(row.getCell(0)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(1)).trim()))
						p6data.setStatus_fk(formatter.formatCellValue(row.getCell(1)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(2)).trim()))
						p6data.setP6_wbs_code_fk(formatter.formatCellValue(row.getCell(2)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(3)).trim()))
						p6data.setP6_activity_name(formatter.formatCellValue(row.getCell(3)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(4)).trim()))
						p6data.setStart(formatter.formatCellValue(row.getCell(4)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(5)).trim()))
						p6data.setFinish(formatter.formatCellValue(row.getCell(5)).trim());

					if (!StringUtils.isEmpty(formatter.formatCellValue(row.getCell(6)).trim()))
						p6data.setP6_float(formatter.formatCellValue(row.getCell(6)).trim());

					p6data.setStart(DateParser.parse(p6data.getStart()));
					p6data.setFinish(DateParser.parse(p6data.getFinish()));

					if (!StringUtils.isEmpty(p6data) && !StringUtils.isEmpty(p6data.getP6_wbs_code_fk())
							&& !StringUtils.isEmpty(p6data.getP6_task_code())) {
						p6dataList.add(p6data);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateP6NewActivities() : " + e.getMessage());
			throw new Exception(e);
		}
		return p6dataList;
	}
}
