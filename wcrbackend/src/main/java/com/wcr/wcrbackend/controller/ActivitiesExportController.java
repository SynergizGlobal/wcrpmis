package com.wcr.wcrbackend.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.utils.BufferUtil;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTShd;
import org.docx4j.wml.CTTblLayoutType;
import org.docx4j.wml.CTVerticalJc;
import org.docx4j.wml.Color;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.FldChar;
import org.docx4j.wml.FooterReference;
import org.docx4j.wml.Ftr;
import org.docx4j.wml.Hdr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.HeaderReference;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.Spacing;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.STFldCharType;
import org.docx4j.wml.STHint;
import org.docx4j.wml.STTblLayoutType;
import org.docx4j.wml.STVerticalJc;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.TcPrInner.TcBorders;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.wcr.wcrbackend.DTO.StripChart;
import com.wcr.wcrbackend.common.CommonConstants2;
import com.wcr.wcrbackend.common.DocxTableCreationForContractReport;
import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.ActivitiesExportService;
import com.wcr.wcrbackend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@RestController
public class ActivitiesExportController {
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	public static Logger logger = Logger.getLogger(ActivitiesExportController.class);

	@Autowired
	ActivitiesExportService service;

	@Value("${common.error.message}")
	public String commonError;

	@Value("${record.dataexport.success}")
	public String dataExportSucess;

	@Value("${record.dataexport.invalid.directory}")
	public String dataExportInvalid;

	@Value("${record.dataexport.error}")
	public String dataExportError;

	@Value("${record.dataexport.nodata}")
	public String dataExportNoData;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/activities-export-report", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView activitiesExportReport(@ModelAttribute StripChart obj, RedirectAttributes attributes,HttpSession session) {
		ModelAndView model = new ModelAndView(PageConstants.activitiesExportReport);
		try {
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_name(uObj.getUserName());
			
			List<StripChart> projectsList = service.getProjectsFilterListInActivitiesExportReport(obj);
			model.addObject("projectsList", projectsList);	

			List<StripChart> worksList = service.getWorksFilterListInActivitiesExportReport(obj);
			model.addObject("worksList", worksList);		

			List<StripChart> contractList = service.getContractListInActivitiesExportReport(obj);
			model.addObject("contractList", contractList);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ActivitiesExportReport : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/mcdo-progress-report", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mcdoReport(@ModelAttribute StripChart obj, RedirectAttributes attributes) {
		ModelAndView model = new ModelAndView(PageConstants.mcdoProgressReport);
		try {
			List<StripChart> projectsList = service.getProjectsFilterListInActivitiesExportReport(obj);
			model.addObject("projectsList", projectsList);

			List<StripChart> worksList = service.getWorksFilterListInActivitiesExportReport(obj);
			model.addObject("worksList", worksList);
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ActivitiesExportReport : " + e.getMessage());
		}
		return model;
	}	

	public RPr getRPr(ObjectFactory factory, String fontFamily,
			String colorVal, String fontSize, STHint sTHint, boolean isBlod,
			boolean isUnderLine, boolean isItalic, boolean isStrike) {
		RPr rPr = factory.createRPr();
		RFonts rf = new RFonts();
		rf.setHint(sTHint);
		rf.setAscii(fontFamily);
		rf.setHAnsi(fontFamily);
		rPr.setRFonts(rf);

		BooleanDefaultTrue bdt = factory.createBooleanDefaultTrue();
		rPr.setBCs(bdt);
		if (isBlod) {
			rPr.setB(bdt);
		}
		if (isItalic) {
			rPr.setI(bdt);
		}
		if (isStrike) {
			rPr.setStrike(bdt);
		}
		if (isUnderLine) {
			U underline = new U();
			underline.setVal(UnderlineEnumeration.SINGLE);
			rPr.setU(underline);
		}

		Color color = new Color();
		color.setVal(colorVal);
		rPr.setColor(color);

		HpsMeasure sz = new HpsMeasure();
		sz.setVal(new BigInteger(fontSize));
		rPr.setSz(sz);
		rPr.setSzCs(sz);
		return rPr;
	}
	
	public P newImage(WordprocessingMLPackage wordMLPackage,
			ObjectFactory factory, HeaderPart sourcePart, byte[] bytes,
			String filenameHint, String altText, int id1, int id2,
			JcEnumeration jcEnumeration) throws Exception {
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage,sourcePart, bytes);
		Inline inline = imagePart.createImageInline(filenameHint, altText, id1,
				id2, false);
		P p = factory.createP();
		R run = factory.createR();
		p.getContent().add(run);
		Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		PPr pPr = p.getPPr();
		if (pPr == null) {
			pPr = factory.createPPr();
		}
		Jc jc = pPr.getJc();
		if (jc == null) {
			jc = new Jc();
		}
		if(!StringUtils.isEmpty(jcEnumeration)) {
			jc.setVal(jcEnumeration);
			pPr.setJc(jc);
		}
		
		p.setPPr(pPr);
		return p;
	}	
	
	
	public Relationship createHeaderPart(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory,
			String imagePath, JcEnumeration imageAlignment, String headerTextMiddle, String headerTextRight,RPr titleRPr) throws Exception {
		HeaderPart headerPart = new HeaderPart();
		Relationship rel = t.addTargetPart(headerPart);
		// After addTargetPart, so image can be added properly
		headerPart.setJaxbElement(getHdr(wordprocessingMLPackage, factory,
				headerPart,imagePath,imageAlignment,headerTextMiddle,headerTextRight,titleRPr));
		return rel;
	}
	public Hdr getHdr(WordprocessingMLPackage wordprocessingMLPackage,
			ObjectFactory factory, HeaderPart sourcePart,
			String imagePath, JcEnumeration imageAlignment, String headerTextMiddle, String headerTextRight,RPr titleRPr) throws Exception {
		Hdr hdr = factory.createHdr();
		//String path = CommonConstants.DOCX_LOGO+"/docx-logo.png";
		//String path = CommonConstants.DOCX_LOGO+"/"+"ircon-report-header.png";
		P p = factory.createP();
		R r = factory.createR();
		if(!StringUtils.isEmpty(imagePath)) {
			File file = new File(imagePath);
			java.io.InputStream is = new java.io.FileInputStream(file);
			
			String filenameHint = null;
	        String altText = null;

	        int id1 = 0;
	        int id2 = 1;
			p = newImage(wordprocessingMLPackage, factory, sourcePart,
					BufferUtil.getBytesFromInputStream(is), altText,
					filenameHint, id1, id2, imageAlignment);
		}
		
		hdr.getContent().add(p);
		
		/*RPr boldRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA,
				true, false, false, false);
		
		
		if(!StringUtils.isEmpty(headerTextMiddle)) {
			for (int i = 0; i < 0; i++) {
				R.Tab rtab = factory.createRTab();
		        JAXBElement<org.docx4j.wml.R.Tab> rtabWrapped = factory.createRTab(rtab);
		        r.getContent().add( rtabWrapped);
			}			
			p.getContent().add(r);
		
			Text txt = factory.createText();
			txt.setValue(headerTextMiddle);
			r = factory.createR();
			r.getContent().add(txt);
			r.setRPr(boldRPr);
			p.getContent().add(r);
		}
		
		if(!StringUtils.isEmpty(headerTextRight)) {
			for (int i = 0; i < 3; i++) {
				R.Tab rtab = factory.createRTab();
		        JAXBElement<org.docx4j.wml.R.Tab> rtabWrapped = factory.createRTab(rtab);
		        r.getContent().add( rtabWrapped);
			}
			Text txt = factory.createText();
			txt.setValue(headerTextRight);
			r = factory.createR();
			r.getContent().add(txt);
			r.setRPr(boldRPr);
			p.getContent().add(r);
		}
		hdr.getContent().add(p);*/	
		
		/*******************************************************************************/

		Tbl table = factory.createTbl();
		
		TblPr tableProps = new TblPr();
        CTTblLayoutType tblLayoutType = new CTTblLayoutType();
        STTblLayoutType stTblLayoutType = STTblLayoutType.FIXED;
        tblLayoutType.setType(stTblLayoutType);
        tableProps.setTblLayout(tblLayoutType);
        table.setTblPr(tableProps);
        
		Tr titleRow = factory.createTr();
		addTableCell(factory, wordprocessingMLPackage, titleRow, "", titleRPr, JcEnumeration.CENTER, true, "ffffff");
		addTableCell(factory, wordprocessingMLPackage, titleRow, headerTextMiddle, titleRPr, JcEnumeration.CENTER, true,
				"ffffff");
		addTableCell(factory, wordprocessingMLPackage, titleRow, headerTextRight, titleRPr, JcEnumeration.RIGHT, true,
				"ffffff");
		table.getContent().add(titleRow);
		setTableAlign(factory, table, JcEnumeration.CENTER);

		hdr.getContent().add(table);
		
		return hdr;
	}
	
	public static void setTableAlign(ObjectFactory factory, Tbl table, JcEnumeration jcEnumeration) {
		TblPr tablePr = table.getTblPr();
		if (tablePr == null) {
			tablePr = factory.createTblPr();
		}
		Jc jc = tablePr.getJc();
		if (jc == null) {
			jc = new Jc();
		}
		jc.setVal(jcEnumeration);
		tablePr.setJc(jc);

		TblWidth tblwidth = factory.createTblWidth();
		tblwidth.setW(BigInteger.valueOf(5000)); // 5000 = 100%
		tblwidth.setType("pct");
		tablePr.setTblW(tblwidth);

		table.setTblPr(tablePr);
	}	
	
	public void setParagraphAlign(ObjectFactory factory, P p,
			JcEnumeration jcEnumeration) {
		PPr pPr = p.getPPr();
		if (pPr == null) {
			pPr = factory.createPPr();
		}
		Jc jc = pPr.getJc();
		if (jc == null) {
			jc = new Jc();
		}
		jc.setVal(jcEnumeration);
		pPr.setJc(jc);
		p.setPPr(pPr);
	}	
	
	public void addTableCell(ObjectFactory factory, WordprocessingMLPackage wordMLPackage, Tr tableRow, String content,
			RPr rpr, JcEnumeration jcEnumeration, boolean hasBgColor, String backgroudColor) {
		Tc tableCell = factory.createTc();
		P p = factory.createP();
		setParagraphAlign(factory, p, jcEnumeration);
		//Text t = factory.createText();
		//t.setValue(content);
		R run = factory.createR();
		run.setRPr(rpr);

		//run.getContent().add(t);

		p.getContent().add(run);
		if (content != null) {
			String[] contentArr = content.split("\n");
			Text text = factory.createText();
			text.setSpace("preserve");
			text.setValue(contentArr[0]);
			run.getContent().add(text);

			for (int i = 1, len = contentArr.length; i < len; i++) {
				Br br = factory.createBr();
				run.getContent().add(br);
				text = factory.createText();
				text.setSpace("preserve");
				text.setValue(contentArr[i]);
				run.getContent().add(text);
			}
		}

		TcPr tcPr = tableCell.getTcPr();
		if (tcPr == null) {
			tcPr = factory.createTcPr();
		}

		CTVerticalJc valign = factory.createCTVerticalJc();
		valign.setVal(STVerticalJc.CENTER);
		tcPr.setVAlign(valign);

		//Removing space in cells
		PPr pPr = factory.createPPr();
		Spacing spacing = new Spacing();
		spacing.setBefore(new BigInteger("2"));
		spacing.setAfter(new BigInteger("2"));
		//spacing.setAfterLines(BigInteger.TEN);
		//spacing.setBeforeLines(BigInteger.TEN);
		pPr.setSpacing(spacing);

		Jc justification = factory.createJc();
		justification.setVal(jcEnumeration);
		pPr.setJc(justification);

		p.setPPr(pPr);

		tableCell.getContent().add(p);
		if (hasBgColor) {
			CTShd shd = tcPr.getShd();
			if (shd == null) {
				shd = factory.createCTShd();
			}
			shd.setColor("auto");
			shd.setFill(backgroudColor);
			tcPr.setShd(shd);
		}

		TcBorders tcb = factory.createTcPrInnerTcBorders();
		CTBorder ctb = factory.createCTBorder();
		STBorder stb = STBorder.NONE;
		ctb.setVal(stb);
		tcb.setBottom(ctb);
		tcb.setRight(ctb);
		tcb.setLeft(ctb);
		tcb.setTop(ctb);
		tcPr.setTcBorders(tcb);

		tableCell.setTcPr(tcPr);

		tableRow.getContent().add(tableCell);
	}	
	
	public Relationship createHeaderPart(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory,String headerText) throws Exception {
		HeaderPart headerPart = new HeaderPart();
		Relationship rel = t.addTargetPart(headerPart);
		// After addTargetPart, so image can be added properly
		headerPart.setJaxbElement(getHdr(wordprocessingMLPackage, factory,
				headerPart,headerText));
		return rel;
	}
	
	
	public Hdr getHdr(WordprocessingMLPackage wordprocessingMLPackage,
			ObjectFactory factory, HeaderPart sourcePart,String headerText) throws Exception {
		Hdr hdr = factory.createHdr();
		
		P p = factory.createP();
		R r = factory.createR();		
		
		RPr boldRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA,
				true, false, false, false);
		
		
		if(!StringUtils.isEmpty(headerText)) {			
			PPr pPr = p.getPPr();
			if (pPr == null) {
				pPr = factory.createPPr();
			}
			Jc jc = pPr.getJc();
			if (jc == null) {
				jc = new Jc();
			}
			jc.setVal(JcEnumeration.RIGHT);
			pPr.setJc(jc);
			p.setPPr(pPr);

			Text txt = factory.createText();
			txt.setValue(headerText);
			r = factory.createR();
			r.getContent().add(txt);
			r.setRPr(boldRPr);
			p.getContent().add(r);
		}
		
		
		
		hdr.getContent().add(p);	
		
		return hdr;
	}	
	public void createHeaderReference(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory, Relationship relationship)
			throws InvalidFormatException {
		List<SectionWrapper> sections = wordprocessingMLPackage
				.getDocumentModel().getSections();
		SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectPr == null) {
			sectPr = factory.createSectPr();
			t.addObject(sectPr);
			sections.get(sections.size() - 1).setSectPr(sectPr);
		}
		/*HeaderReference headerReference = factory.createHeaderReference();
		headerReference.setId(relationship.getId());
		headerReference.setType(HdrFtrRef.FIRST);
		sectPr.getEGHdrFtrReferences().add(headerReference);*/
		
		HeaderReference headerReference = factory.createHeaderReference();
		headerReference = factory.createHeaderReference();
		headerReference.setId(relationship.getId());
		//headerReference.setType(HdrFtrRef.DEFAULT);
		headerReference.setType(HdrFtrRef.FIRST);
		sectPr.getEGHdrFtrReferences().add(headerReference);
		
		headerReference = factory.createHeaderReference();
		headerReference.setId(relationship.getId());
		headerReference.setType(HdrFtrRef.DEFAULT);
		//headerReference.setType(HdrFtrRef.FIRST);
		sectPr.getEGHdrFtrReferences().add(headerReference);
		
		BooleanDefaultTrue value = new BooleanDefaultTrue();
        value.setVal(Boolean.TRUE);
        sectPr.setTitlePg(value);        
	}	
	
	
	public Relationship createFooterPageNumPart(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory) throws Exception {
		FooterPart footerPart = new FooterPart();
		footerPart.setPackage(wordprocessingMLPackage);
		footerPart.setJaxbElement(createFooterWithPageNr(factory));
		return t.addTargetPart(footerPart);
	}
	
	public Ftr createFooterWithPageNr(ObjectFactory factory) {
		Ftr ftr = factory.createFtr();
		P paragraph = factory.createP();
		RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA,
				false, false, false, false);
		R run = factory.createR();
		run.setRPr(fontRPr);
		paragraph.getContent().add(run);

		addPageTextField(factory, paragraph, "Page ");
		addFieldBegin(factory, paragraph);
		addPageNumberField(factory, paragraph);
		addFieldEnd(factory, paragraph);
		addPageTextField(factory, paragraph, " of ");

		//addPageTextField(factory, paragraph, "Test");
		addFieldBegin(factory, paragraph);
		addTotalPageNumberField(factory, paragraph);
		addFieldEnd(factory, paragraph);
		//addPageTextField(factory, paragraph, "Test");
		setParagraphAlign(factory, paragraph, JcEnumeration.CENTER);
		ftr.getContent().add(paragraph);
		return ftr;
	}
	
	public void addFieldBegin(ObjectFactory factory, P paragraph) {
		R run = factory.createR();
		FldChar fldchar = factory.createFldChar();
		fldchar.setFldCharType(STFldCharType.BEGIN);
		run.getContent().add(fldchar);
		paragraph.getContent().add(run);
	}

	public void addFieldEnd(ObjectFactory factory, P paragraph) {
		FldChar fldcharend = factory.createFldChar();
		fldcharend.setFldCharType(STFldCharType.END);
		R run3 = factory.createR();
		run3.getContent().add(fldcharend);
		paragraph.getContent().add(run3);
	}

	public void addPageNumberField(ObjectFactory factory, P paragraph) {
		R run = factory.createR();
		Text txt = new Text();
		txt.setSpace("preserve");
		txt.setValue("PAGE  \\* MERGEFORMAT ");
		run.getContent().add(factory.createRInstrText(txt));
		paragraph.getContent().add(run);
	}

	public void addTotalPageNumberField(ObjectFactory factory, P paragraph) {
		R run = factory.createR();
		Text txt = new Text();
		txt.setSpace("preserve");
		txt.setValue("NUMPAGES  \\* MERGEFORMAT ");
		run.getContent().add(factory.createRInstrText(txt));
		paragraph.getContent().add(run);
	}

	private void addPageTextField(ObjectFactory factory, P paragraph,
			String value) {
		R run = factory.createR();
		Text txt = new Text();
		txt.setSpace("preserve");
		txt.setValue(value);
		run.getContent().add(txt);
		paragraph.getContent().add(run);
	}
	
	public void createFooterReference(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory, Relationship relationship)
			throws InvalidFormatException {
		List<SectionWrapper> sections = wordprocessingMLPackage
				.getDocumentModel().getSections();
		SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectPr == null) {
			sectPr = factory.createSectPr();
			t.addObject(sectPr);
			sections.get(sections.size() - 1).setSectPr(sectPr);
		}
		FooterReference footerReference = factory.createFooterReference();
		footerReference.setId(relationship.getId());
		footerReference.setType(HdrFtrRef.FIRST);
		sectPr.getEGHdrFtrReferences().add(footerReference);
		
		footerReference = factory.createFooterReference();
		footerReference.setId(relationship.getId());
		footerReference.setType(HdrFtrRef.DEFAULT);
		sectPr.getEGHdrFtrReferences().add(footerReference);
	}
	
	
//	@RequestMapping(value = "/station-improvements-report", method = {RequestMethod.GET,RequestMethod.POST})
//	public ResponseEntity<?> stationImprovementsReport(HttpServletResponse response, StripChart obj, HttpSession session, RedirectAttributes attributes) {
//	    
//	    byte[] byteArray;        
//	    try {
//	        SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
//	        Date date = new Date();
//	        String currentDate = sqlDate.format(date);
//
//	        List<StripChart> stationList = service.generateStationImprovementsReport(obj);
//	        
//	        List<StripChart> divisionlist = service.getStationImprovementDivisionList(obj);
//
//
//	        boolean landscape = false;
//	        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
//	        
//	        MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
//	        ObjectFactory factory = Context.getWmlObjectFactory();
//	        
//	        DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
//	        String report_created_date = df.format(new Date()); 
//	        
//	        String imagePath = CommonConstants2.DOCX_LOGO + "/" + "report_logo.png";
//	        JcEnumeration imageAlignment = JcEnumeration.CENTER;
//	        
//	        String headerTextMiddle = "Station Improvements Progress Report";
//	        String headerTextRight = report_created_date;
//	        
//	        RPr titleRpr = getRPr(factory, "Calibri", "000000", "26", STHint.EAST_ASIA, true, false, false, false);
//	        Relationship relationship = createHeaderPart(wordMLPackage, mp, factory, imagePath, imageAlignment, headerTextMiddle, headerTextRight, titleRpr);
//	        createHeaderReference(wordMLPackage, mp, factory, relationship);
//	        relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
//	        createFooterReference(wordMLPackage, mp, factory, relationship);
//
//	        // Create table for Station Improvements Report
//	        DocxTableCreationForContractReport.createTableForStationImprovementsReport(wordMLPackage, mp, factory, stationList, report_created_date,divisionlist);
//	        
//	        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {    
//	            wordMLPackage.save(bos);
//	            byteArray = bos.toByteArray();
//	            InputStream targetStream = new ByteArrayInputStream(byteArray);
//	            String FILE_EXTENSION = ".docx";
//	            String fileName = "Station Improvements Progress Report-" + currentDate + FILE_EXTENSION;
//
//	            response.setContentType("application/msword");
//	            response.setContentType("application/vnd.ms-word");
//	            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
//	            IOUtils.copy(targetStream, response.getOutputStream());
//	            response.getOutputStream().flush();
//	            return targetStream
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            logger.error("stationImprovementsReport >> FileNotFoundException occurs.." + e.getMessage());
//	        }    
//	        
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        logger.error("stationImprovementsReport >> " + e.getMessage());
//	    }        
//	}	
	
	@PostMapping("/station-improvements-report")
	public ResponseEntity<byte[]> stationImprovementsReport(@RequestBody StripChart obj) {

	    try {
	        SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
	        String currentDate = sqlDate.format(new Date());

	        List<StripChart> stationList = service.generateStationImprovementsReport(obj);
	        List<StripChart> divisionlist = service.getStationImprovementDivisionList(obj);

	        boolean landscape = false;
	        WordprocessingMLPackage wordMLPackage =
	                WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);

	        MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
	        ObjectFactory factory = Context.getWmlObjectFactory();

	        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
	        String reportCreatedDate = df.format(new Date());

	        String imagePath = CommonConstants2.DOCX_LOGO + "/report_logo_wcrpmis.png";
	        JcEnumeration imageAlignment = JcEnumeration.CENTER;

	        String headerTextMiddle = "Station Improvements Progress Report";
	        String headerTextRight = reportCreatedDate;

	        RPr titleRpr = getRPr(factory, "Calibri", "000000", "26",
	                              STHint.EAST_ASIA, true, false, false, false);

	        Relationship relationship = createHeaderPart(
	                wordMLPackage, mp, factory,
	                imagePath, imageAlignment,
	                headerTextMiddle, headerTextRight, titleRpr);

	        createHeaderReference(wordMLPackage, mp, factory, relationship);

	        relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
	        createFooterReference(wordMLPackage, mp, factory, relationship);

	        DocxTableCreationForContractReport.createTableForStationImprovementsReport(
	                wordMLPackage, mp, factory,
	                stationList, reportCreatedDate, divisionlist);

	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        wordMLPackage.save(bos);

	        byte[] documentBytes = bos.toByteArray();

	        String fileName = "Station-Improvements-" + currentDate + ".docx";

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION,
	                        "attachment; filename=\"" + fileName + "\"")
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(documentBytes);

	    } catch (Exception e) {
	    	e.printStackTrace();
	    	logger.error("stationImprovementsReport >> " + e.getMessage(), e);
	        return ResponseEntity.internalServerError().build();
	    }
	}
	
//	@RequestMapping(value = "/tpc-status-report", method = {RequestMethod.GET,RequestMethod.POST})
//	public void TPCStatusReport(HttpServletResponse response,StripChart obj, HttpSession session,RedirectAttributes attributes) {
//		
//		
////		ModelAndView model = new ModelAndView("redirect:/tpc-status-report");
//		byte[] byteArray;        
//		try{
//			SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = new Date();
//            String currentDate = sqlDate.format(date);
//						
//			List<StripChart> list = service.generateTPCStatusReport(obj);
//			
//			List<StripChart> divisionlist = service.getDivisionList(obj);
//			
//			
//			List<StripChart> strlist = service.generateTPCStructureList(obj);
//			
//			List<StripChart> strcumlist = service.generateTPCStructureCumList(obj);			
//			
//			
//			boolean landscape = false;
//			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
//			
//			MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
//			ObjectFactory factory = Context.getWmlObjectFactory();
//			
//			//DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
//			DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
//			String report_created_date = df.format(new Date()); 
//			
//			
//			String imagePath = CommonConstants2.DOCX_LOGO+"/"+"report_logo_mrvc.png";
//			
//			JcEnumeration imageAlignment = JcEnumeration.CENTER;
//			
//			//String headerTextMiddle = "Summary of Risk Assessment of Projects";
//			String headerTextMiddle = "TPC Progress Report";
//
//			String headerTextRight = report_created_date;
//			
//			RPr titleRpr = getRPr(factory, "Calibri", "000000", "26", STHint.EAST_ASIA, true, false, false, false);
//			Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,imagePath,imageAlignment,headerTextMiddle,headerTextRight,titleRpr);		
//			//Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,headerTextRight);
//			createHeaderReference(wordMLPackage, mp, factory, relationship);
//			relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
//			createFooterReference(wordMLPackage, mp, factory, relationship);
//			 			  
//			DocxTableCreationForContractReport.createTableForTPCStatusReport(wordMLPackage, mp, factory, list,divisionlist,strlist,strcumlist, report_created_date);
//	    	  
//						
//			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){	
//				wordMLPackage.save(bos);
//				byteArray = bos.toByteArray();
//				InputStream targetStream = new ByteArrayInputStream(byteArray);
//				String FILE_EXTENSION = ".docx";
//				String fileName = "MUTP 3 TPC FOBs Status-" + currentDate + FILE_EXTENSION;
//				
//				response.setContentType("application/msword");
//				response.setContentType("application/vnd.ms-word");
//				// add response header
//				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
//				//copies all bytes from a file to an output stream
//				IOUtils.copy(targetStream, response.getOutputStream());
//				//flushes output stream
//				response.getOutputStream().flush();
//		    }catch (Exception e) {
//				e.printStackTrace();
//				logger.error("TPCStatusReport >> FileNotFoundException occurs.." + e.getMessage());
//		    }	
//		 	
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("TPCStatusReport >> " + e.getMessage());
//		}		
//    }
	
	
	
	@PostMapping("/tpc-status-report")
	public ResponseEntity<byte[]> TPCStatusReport(@RequestBody StripChart obj) {

	    try {
	        SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
	        String currentDate = sqlDate.format(new Date());

	        List<StripChart> list = service.generateTPCStatusReport(obj);
	        List<StripChart> divisionlist = service.getDivisionList(obj);
	        List<StripChart> strlist = service.generateTPCStructureList(obj);
	        List<StripChart> strcumlist = service.generateTPCStructureCumList(obj);

	        boolean landscape = false;
	        WordprocessingMLPackage wordMLPackage =
	                WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);

	        MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
	        ObjectFactory factory = Context.getWmlObjectFactory();

	        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
	        String reportCreatedDate = df.format(new Date());

	        String imagePath = CommonConstants2.DOCX_LOGO + "/report_logo_wcrpmis.png";
	        JcEnumeration imageAlignment = JcEnumeration.CENTER;

	        String headerTextMiddle = "TPC Progress Report";
	        String headerTextRight = reportCreatedDate;

	        RPr titleRpr = getRPr(factory, "Calibri", "000000", "26",
	                              STHint.EAST_ASIA, true, false, false, false);

	        Relationship relationship = createHeaderPart(
	                wordMLPackage, mp, factory,
	                imagePath, imageAlignment,
	                headerTextMiddle, headerTextRight, titleRpr);

	        createHeaderReference(wordMLPackage, mp, factory, relationship);

	        relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
	        createFooterReference(wordMLPackage, mp, factory, relationship);

	        DocxTableCreationForContractReport.createTableForTPCStatusReport(
	                wordMLPackage, mp, factory,
	                list, divisionlist, strlist, strcumlist, reportCreatedDate);

	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        wordMLPackage.save(bos);

	        byte[] documentBytes = bos.toByteArray();

	        String fileName = "MUTP 3 TPC FOBs Status-" + currentDate + ".docx";

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION,
	                        "attachment; filename=\"" + fileName + "\"")
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(documentBytes);
	        
	        

	    } catch (Exception e) {
	    	e.printStackTrace();
	        logger.error("TPCStatusReport FAILED", e);
	        return ResponseEntity.internalServerError().build();
	    }
	}


	@PostMapping("/generate-mcdo-progress-report")
	public ResponseEntity<byte[]> MCDOProgressReport(@RequestBody StripChart obj, HttpSession session) {

	    try {

	        /* -------- VALIDATION (CRITICAL) -------- */

	        if (obj.getFrom_date() == null || obj.getTo_date() == null) {
	            return ResponseEntity.badRequest().body(null);
	        }

	        if (obj.getWork_id_fk() == null) {
	            return ResponseEntity.badRequest().body(null);
	        }

	        /* -------- DATE CONVERSION -------- */

	        // incoming format: dd-MM-yyyy
	        String[] fromParts = obj.getFrom_date().split("-");
	        String[] toParts = obj.getTo_date().split("-");

	        String fDate = fromParts[2] + "-" + fromParts[1] + "-" + fromParts[0];
	        String tDate = toParts[2] + "-" + toParts[1] + "-" + toParts[0];

	        obj.setFrom_date(fDate);
	        obj.setTo_date(tDate);

	        List<StripChart> list = null;
	        List<StripChart> list1 = null;

	        /* -------- BUSINESS LOGIC -------- */

	        if ("P04W02".equals(obj.getWork_id_fk())) {
	            list = service.generateMCDOProgressReport(obj);
	        }

	        if ("P04W01".equals(obj.getWork_id_fk())) {
	            list1 = service.generateMCDOProgressReport1(obj);
	        }

	        if (obj.getWork_id_fk().contains("P04W02") && obj.getWork_id_fk().contains("P04W01")) {

	            obj.setWork_id_fk("P04W02");
	            list = service.generateMCDOProgressReport(obj);

	            obj.setWork_id_fk("P04W01");
	            list1 = service.generateMCDOProgressReport1(obj);
	        }

	        /* -------- DOCX CREATION -------- */

	        boolean landscape = false;

	        WordprocessingMLPackage wordMLPackage =
	                WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);

	        MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
	        ObjectFactory factory = Context.getWmlObjectFactory();

	        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
	        String report_created_date = df.format(new Date());

	        String imagePath = CommonConstants2.DOCX_LOGO + "/report_logo_wcrpmis.png";

	        RPr titleRpr = getRPr(factory, "Calibri", "000000", "26",
	                STHint.EAST_ASIA, true, false, false, false);

	        Relationship relationship =
	                createHeaderPart(wordMLPackage, mp, factory, imagePath,
	                        JcEnumeration.CENTER, "", "", titleRpr);

	        createHeaderReference(wordMLPackage, mp, factory, relationship);

	        relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
	        createFooterReference(wordMLPackage, mp, factory, relationship);

	        DocxTableCreationForContractReport.createTableForMCDOProgressReport(
	                wordMLPackage, mp, factory, list, list1,
	                report_created_date, fDate, tDate
	        );

	        /* -------- STREAM TO BYTE[] -------- */

	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        wordMLPackage.save(bos);

	        byte[] byteArray = bos.toByteArray();

	        String fileName = "MCDO-Progress-Report.docx";

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION,
	                        "attachment; filename=" + fileName)
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(byteArray);

	    } catch (Exception e) {

	        logger.error("MCDOProgressReport >> " + e.getMessage(), e);

	        return ResponseEntity.internalServerError().body(null);
	    }
	}

	
	
	@RequestMapping(value = "/ajax/getWorksListForMCDOProgressReport", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getWorksListForMCDOProgressreporttReportForm(@ModelAttribute StripChart obj) {
		List<StripChart> objList = null;
		try {
			objList = service.getWorksListForSelectedProject(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getWorksListForMCDOProgressreporttReportForm : " + e.getMessage());
		}
		return objList;
	}		
	

	@RequestMapping(value = "/ajax/getProjectListForActivitiesExportReportForm", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getProjectListForActivitiesExportReportForm(@ModelAttribute StripChart obj,HttpSession session) {
		List<StripChart> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_name(uObj.getUserName());			
			objList = service.getProjectsFilterListInActivitiesExportReport(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getProjectListForActivitiesExportReportForm : " + e.getMessage());
		}
		return objList;
	}

	@RequestMapping(value = "/ajax/getContractListInActivitiesExportReport", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getLocationsListForActivitiesExportReportForm(@ModelAttribute StripChart obj,HttpSession session) {
		List<StripChart> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_name(uObj.getUserName());			
			objList = service.getContractListInActivitiesExportReport(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getLocationsListForActivitiesExportReportForm : " + e.getMessage());
		}
		return objList;
	}

	@RequestMapping(value = "/generate-activities-export-report", method = {RequestMethod.GET,RequestMethod.POST})
	public void generateActivitiesExportReport(@ModelAttribute StripChart obj,HttpServletRequest request, HttpServletResponse response,HttpSession session,RedirectAttributes attributes){
		//ModelAndView model = new ModelAndView("redirect:/activities-progress-report");
		try{
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			String report_created_date = df.format(new Date());
			
			SimpleDateFormat formatter = new SimpleDateFormat("d-MMM-YY");
		
			//List<StripChart> structuresList = service.getStructuresList(obj);
			
			StripChart reportData = service.generateActivitiesExportReport(obj);
			
			XSSFWorkbook  workBook = new XSSFWorkbook();
			
			/***************************************************************************/
	        
			byte[] blueRGB = new byte[]{(byte)214, (byte)255, (byte)255};
			byte[] yellowRGB = new byte[]{(byte)255, (byte)255, (byte)153};
	        byte[] greenRGB = new byte[]{(byte)146, (byte)208, (byte)80};
	        byte[] redRGB = new byte[]{(byte)255, (byte)0, (byte)0};
	        byte[] whiteRGB = new byte[]{(byte)255, (byte)255, (byte)255};
	        byte[] greyRGB = new byte[]{(byte)211, (byte)211, (byte)211};
	        
	        
	        boolean isWrapText = true;boolean isBoldText = true;boolean isItalicText = false; int fontSize = 11;String fontName = "Calibri";
	        CellStyle greenStyle = cellFormating(workBook,greenRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle greenStyle1 = cellFormating(workBook,greenRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle bluetyle = cellFormating(workBook,blueRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        CellStyle whiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        
	        CellStyle indexWhiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	       
	        CellStyle indexWhiteStyle1 = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle indexWhiteStyle2 = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle activityNameStyle3 = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        
	        isWrapText = true;isBoldText = false;isItalicText = false; fontSize = 11;fontName = "Calibri";
	        CellStyle numberStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle activityNameStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle activityNameStyle1 = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        CellStyle activityNameStyle2 = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        CellStyle indexShadedStyle = cellFormating(workBook,greyRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,true,isItalicText,11,fontName);


	        /********************************************************/
          
	        if(!(StringUtils.isEmpty(reportData))){
	        	
       		 	XSSFSheet rrSheet1 = workBook.createSheet(WorkbookUtil.createSafeSheetName(obj.getContract_id_fk() +" - Activities Report"));
		        XSSFRow headRow = rrSheet1.createRow(0);
		        
		        Cell cell = headRow.createCell(0);
		        
		        XSSFRow mainHeadingRow = rrSheet1.createRow(1);
		        
		        cell = mainHeadingRow.createCell(0);
		        cell.setCellStyle(bluetyle);
		        String contract_d = "";
				if(!StringUtils.isEmpty(obj.getContract_id_fk())) {
					contract_d = reportData.getReport1List().get(0).getContract_short_name()+" - " ;
				}
		        cell.setCellValue(contract_d+"Activities Report");
		        
		        for (int i = 1; i < 5; i++) {		        	
			        cell = mainHeadingRow.createCell(i);
			        cell.setCellStyle(bluetyle);
					cell.setCellValue("");
				}	
		        rrSheet1.addMergedRegion(new CellRangeAddress(1, 1, 0,4));
		int rowNo = 3;

        XSSFRow structureRow = rrSheet1.createRow(rowNo);

        /**********************************************************************/
		String headerString = "Primavera Activity Code^Type of Structure^Unique Structure ID " + 
				"(X axis in Strip Chart) ^Type of Component" + 
				"(Y axis in Strip Chart) ^Unique Component ID (Structure to Structure ID)" + 
				" ^Name of the Activity ^Unit of scope^Scope Quantity^Completed Quantity^"
				+ "Weightage points of the Activity in structure^Baseline start date of the activity (dd-mm-yyyy)\"\r\n" + 
				"				+ \"^Baseline finish date of the activity (dd-mm-yyyy)^Actual start date of the activity (dd-mm-yyyy)^"
				+ "Actual finish date of the activity (dd-mm-yyyy)^Expected Start date of the activity (dd-mm-yyyy)"
				+ "^Expected finish date of the activity (dd-mm-yyyy)^Status^Detail of component if Any^for filtering of Section of Line  on Map	^^^"
				+ "Line of the activity (eg Up Line Down Line)^Any Additional Remarks";
        String[] headerStringArr = headerString.split("\\^");
        
        XSSFRow headingRow = rrSheet1.createRow(rowNo);
        for (int i = 0; i < headerStringArr.length ; i++) {	
        	
    			 cell = headingRow.createCell(i);
    	    	 cell.setCellStyle(whiteStyle);
				 cell.setCellValue(headerStringArr[i]);
		}	

        rowNo++;
        structureRow = rrSheet1.createRow(rowNo);
        
        String headerString1 = "P6 Task Code^Structure Type^Structure ID^Component^Component ID^Activity^Unit^"
        		+ "Total Scope^Completed^Weightage Point^Baseline Start Date^Baseline Finish Date^Actual Start Date^Actual Finish Date"
        		+ "^Expected Start Date^Expected  Finish Date^P6 Status^Components Detail^"
        		+ "Section^From Structure ID^To Structure ID^Line^Remarks";
        String[] headerStringArr1 = headerString1.split("\\^");
        
        XSSFRow headingRow1 = rrSheet1.createRow(rowNo);
        for (int i = 0; i < headerStringArr1.length ; i++) {	
        	
    			 cell = headingRow1.createCell(i);
    	    	 cell.setCellStyle(greenStyle1);
				 cell.setCellValue(headerStringArr1[i]);
		}	
        rowNo++;
        int x = 0,z=0;
	        	 for (StripChart zObj : reportData.getReport1List()) {  
	        		  XSSFRow row = rrSheet1.createRow(rowNo);
				            int c = 0;
				       
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getP6_task_code());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getStructure_type_fk());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getStructure());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getComponent());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getComponent_id());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getActivity_name());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getUnit());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getScope());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getCompleted());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getWeightage());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getBaseline_start());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getBaseline_finish());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getActual_start());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getActual_finish());							

						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getStart());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getFinish());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getStatus());
						
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getComponent_details());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getSection());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getFrom_structure_id());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getTo_structure_id());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getLine());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getRemarks());
						
					        rowNo++;
	        	 }
				  for(int columnIndex = 1; columnIndex < headerStringArr.length; columnIndex++) {
				  	rrSheet1.setColumnWidth(columnIndex, 25 * 150);
				  	//rrSheet1.autoSizeColumn(columnIndex);
				  	rrSheet1.setColumnWidth(4, 35 * 235);
					rrSheet1.setColumnWidth(5, 35 * 235);
				   }

            /*******************************************************************************/
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
            Date date = new Date();
            String fileName = "Activities_Export_Report_"+dateFormat.format(date);
            
            try{
                /*FileOutputStream fos = new FileOutputStream(fileDirectory +fileName+".xls");
                workBook.write(fos);
                fos.flush();*/
            	
               response.setContentType("application/.csv");
 			   response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
 			   response.setContentType("application/vnd.ms-excel");
 			   // add response header
 			   response.addHeader("Content-Disposition", "attachment; filename=" + fileName+".xlsx");
 			   
 			    //copies all bytes from a file to an output stream
 			   workBook.write(response.getOutputStream()); // Write workbook to response.
	           workBook.close();
 			    //flushes output stream
 			    response.getOutputStream().flush();
            	
                
                //attributes.addFlashAttribute("success",dataExportSucess);
            	//response.setContentType("application/vnd.ms-excel");
            	//response.setHeader("Content-Disposition", "attachment; filename=filename.xls");
            	//XSSFWorkbook  workbook = new XSSFWorkbook ();
            	// ...
            	// Now populate workbook the usual way.
            	// ...
            	//workbook.write(response.getOutputStream()); // Write workbook to response.
            	//workbook.close();
            }catch(FileNotFoundException e){
                e.printStackTrace();
                logger.error("generateActivitiesExportMain : " + e.getMessage());
                //attributes.addFlashAttribute("error",dataExportInvalid);
            }catch(IOException e){
                e.printStackTrace();
                logger.error("generateActivitiesExportMain : " + e.getMessage());
                //attributes.addFlashAttribute("error",dataExportError);
            }
	      }
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generateActivitiesExportMain : " + e.getMessage());
		}
		//return model;
    }

	private ModelAndView noDataAlertCall(RedirectAttributes attributes) {
		ModelAndView model = new ModelAndView();
		try {
			attributes.addFlashAttribute("error", "No updates in this period");
		} catch (Exception e) {

		}
		return model;
	}

	private CellStyle cellFormating(XSSFWorkbook workBook, byte[] rgb, HorizontalAlignment hAllign,
			VerticalAlignment vAllign, boolean isWrapText, boolean isBoldText, boolean isItalicText, int fontSize,
			String fontName) {
		CellStyle style = workBook.createCellStyle();
		// Setting Background color
		// style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		if (style instanceof XSSFCellStyle) {
			XSSFCellStyle xssfcellcolorstyle = (XSSFCellStyle) style;
			xssfcellcolorstyle.setFillForegroundColor(new XSSFColor(rgb, null));
		}
		// style.setFillPattern(FillPatternType.ALT_BARS);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setAlignment(hAllign);
		style.setVerticalAlignment(vAllign);
		style.setWrapText(isWrapText);

		Font font = workBook.createFont();
		// font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		font.setFontHeightInPoints((short) fontSize);
		font.setFontName(fontName); // "Calibri"

		font.setItalic(isItalicText);
		font.setBold(isBoldText);
		// Applying font to the style
		style.setFont(font);

		return style;
	}
}
