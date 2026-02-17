package com.wcr.wcrbackend.common;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTShd;
import org.docx4j.wml.CTTblLayoutType;
import org.docx4j.wml.CTVerticalJc;
import org.docx4j.wml.Color;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Drawing;
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
import org.docx4j.wml.STHint;
import org.docx4j.wml.STTblLayoutType;
import org.docx4j.wml.STVerticalJc;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.TcPrInner.HMerge;
import org.docx4j.wml.TcPrInner.TcBorders;
import org.docx4j.wml.TcPrInner.VMerge;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.StripChart;

import jakarta.xml.bind.JAXBElement;

public class DocxTableCreationForContractReport {



	public static void createTableForContractReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, Map<String, List<Contract>> contractsData,String report_created_date,String Status) throws Exception {

		try {

			RPr titleRpr = getRPr(factory, "Calibri", "000000", "12", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Calibri", "000000", "12", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false,
					false);
			
			RPr calibriBoldRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA,
					true, false, false, false);		
			RPr calibriBoldDateRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);

			int temp = 1;
			int tempVal=0;
			for (Map.Entry<String, List<Contract>> entry : contractsData.entrySet()) 
			{
				//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
				List<Contract> contracts = entry.getValue();
				String keyName = entry.getKey();

				//addParagraph(mp, factory);
				//addHeading(wordMLPackage, mp, factory, JcEnumeration.LEFT, calibriBoldRPr, keyName);
				Tbl tableHead = factory.createTbl();
				setLandscapeTableAlign(factory, tableHead, JcEnumeration.CENTER);
				//addBorders(tableHead, "0");
				
				/**************************************************************************/
				
			
				Tr hodRow = factory.createTr();
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, keyName, calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				
				String date = "";
				if(temp == 1) {
					date = report_created_date;
				}else {
					addParagraph(mp, factory);
				}
				temp++;
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, date, calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");				
				
				tableHead.getContent().add(hodRow);
				mergeCellsHorizontal(tableHead, 0, 0, 6);
				mergeCellsHorizontal(tableHead, 0, 7, 13);
				mp.addObject(tableHead);
				if(Status!=null)
				{
					/***************************************************************/
	
					Tbl table = factory.createTbl();
					addBorders(table, "2");
					
					/************************************************************************/
					Tr titleRow0 = factory.createTr();
					List<String> tableHeader0 = new ArrayList<String>();
					tableHeader0.add("SN");
					tableHeader0.add("Short name of Contract");
					tableHeader0.add("Agency");
					tableHeader0.add("Date of Award");
					tableHeader0.add("Contract Value (Cr.)");
					tableHeader0.add("");	
					tableHeader0.add("Date of Completion");
					tableHeader0.add("");					
					tableHeader0.add("BG Valid Upto");
					tableHeader0.add("Insurance Valid Upto");
					tableHeader0.add("Expenditure (Cr.)");
					tableHeader0.add("Physical Progress (%)");
					tableHeader0.add("Target Date of Completion");
					tableHeader0.add("Status of Work");
					
					int columnNo = 1;
					for (String headerValue : tableHeader0) {
						int width = 0;
						if(1 == columnNo) {
							width = 230;
						}else if(2 == columnNo) {
							width = 830;
						}else if(3 == columnNo) {
							width = 1100;
						}else if(4 == columnNo) {
							width = 750;
						}else if(5 == columnNo) {
							width = 620;
						}else if(6 == columnNo) {
							width = 620;
						}else if(7 == columnNo) {
							width = 750;
						}else if(8 == columnNo) {
							width = 750;
						}
						else if(9 == columnNo) {
							width = 760;
						}
						else if(10 == columnNo) {
							width = 760;
						}
						else if(11 == columnNo) {
							width = 700;
						}
						else if(12 == columnNo) {
							width = 700;
						}
						else if(13 == columnNo) {
							width = 750;
						}
						else if(14 == columnNo) {
							width = 800;
						}					
						columnNo++;
						addTableCellAndWidth(factory, wordMLPackage, titleRow0, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
								"ecf2ff",width);
					}
					table.getContent().add(titleRow0);
	
					mergeCellsHorizontal(table, 0, 4, 5);	
					mergeCellsHorizontal(table, 0, 6, 7);
					
					/************************************************************************************/
					Tr titleRow = factory.createTr();
					List<String> tableHeader = new ArrayList<String>();
					tableHeader.add("");
					tableHeader.add("");
					tableHeader.add("");
					tableHeader.add("");
					tableHeader.add("Awarded");
					tableHeader.add("Revised");
					tableHeader.add("Original");
					tableHeader.add("Revised");
					tableHeader.add("");
					tableHeader.add("");
					tableHeader.add("");
					tableHeader.add("");
					tableHeader.add("");
					tableHeader.add("");
					
					columnNo = 1;
					for (String headerValue : tableHeader) {
						int width = 0;
						if(1 == columnNo) {
							width = 230;
						}else if(2 == columnNo) {
							width = 830;
						}else if(3 == columnNo) {
							width = 1100;
						}else if(4 == columnNo) {
							width = 750;
						}else if(5 == columnNo) {
							width = 620;
						}else if(6 == columnNo) {
							width = 620;
						}else if(7 == columnNo) {
							width = 750;
						}else if(8 == columnNo) {
							width = 750;
						}
						else if(9 == columnNo) {
							width = 760;
						}
						else if(10 == columnNo) {
							width = 760;
						}
						else if(11 == columnNo) {
							width = 700;
						}
						else if(12 == columnNo) {
							width = 700;
						}
						else if(13 == columnNo) {
							width = 760;
						}
						else if(14 == columnNo) {
							width = 800;
						}					
						columnNo++;
						addTableCellAndWidth(factory, wordMLPackage, titleRow, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
								"ecf2ff",width);
					}
					table.getContent().add(titleRow);
					
					mergeCellsVertically(table, 0, 0, 1);
					mergeCellsVertically(table, 1, 0, 1);
					mergeCellsVertically(table, 2, 0, 1);
					mergeCellsVertically(table, 3, 0, 1);
					
					mergeCellsVertically(table, 8, 0, 1);
					mergeCellsVertically(table, 9, 0, 1);
					mergeCellsVertically(table, 10, 0, 1);
					mergeCellsVertically(table, 11, 0, 1);				
					mergeCellsVertically(table, 12, 0, 1);
					mergeCellsVertically(table, 13, 0, 1);
	
					
					/*******************************************************************************/
	
					int sNo = 1;
					int NumVal=1;
					int Total=1;
					int Concat=NumVal+1;
					int lenGr=0;					
					for (Contract cObj : contracts) 
					{
						
						String getBG="";
						String getBG1="";
						
						if (StringUtils.isEmpty(cObj.getPbg_valid_till()) || cObj.getPbg_valid_till().isEmpty()) 
						{
							getBG="";
						}
						else
						{
							getBG=cObj.getPbg_valid_till().toString();
						}					
						

						if (StringUtils.isEmpty(cObj.getInsurance_valid_till()) || cObj.getInsurance_valid_till().isEmpty()) 
						{
							getBG1="";
						}
						else
						{
							getBG1=cObj.getInsurance_valid_till().toString();
						}						
						
						boolean hasBgColor = false;
						String backgroundColor = null;
						
						if(getBG.indexOf("<space>")==-1 && getBG1.indexOf("<space>")==-1)
						{						
							Tr contentRow = factory.createTr();
	
							addTableCell(factory, wordMLPackage, contentRow, String.valueOf(NumVal), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_short_name(), garamondRPr,
									JcEnumeration.LEFT, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getContractor_name(), garamondRPr,
									JcEnumeration.LEFT, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getLoa_date(), garamondRPr, JcEnumeration.CENTER,
									hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow,cObj.getAwarded_cost(),
									garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow,cObj.getRevised_amount(),
									garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getDoc(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getRevised_doc(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);
							
							
							addTableCell(factory, wordMLPackage, contentRow, cObj.getPbg_valid_till(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);					
							addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_valid_till(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getCumulative_expenditure(), garamondRPr,
									JcEnumeration.RIGHT, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getPhysicalProgress(), garamondRPr,
									JcEnumeration.RIGHT, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getTarget_doc(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_status_fk(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);						
							table.getContent().add(contentRow);
							Concat=Concat+1;
							Total=Total+1;
							lenGr=1;						
						}
						else
						{
							String[] search = getBG.split("<space>");
							String[] search1 = getBG1.split("<space>");

							int SerLen1 = search.length;
							int SerLen2 = search1.length;
							int GrLength = 0;
							  if(SerLen1>SerLen2)
							  {
								  GrLength=SerLen1;
								  for(int i=0; i<SerLen1-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
								  }								  
							  }
							  else
							  {
								  GrLength=SerLen2;
								  for(int i=0; i<SerLen2-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }									  
							  }
							  lenGr=0;
							  for(int i=0; i<GrLength; i++)
							  {
								  search1 = getBG1.split("<space>");
								  search = getBG.split("<space>");
								  Tr contentRow1 = factory.createTr();
								  
									
									addTableCell(factory, wordMLPackage, contentRow1, String.valueOf(NumVal), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_short_name(), garamondRPr,
											JcEnumeration.LEFT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getContractor_name(), garamondRPr,
											JcEnumeration.LEFT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getLoa_date(), garamondRPr, JcEnumeration.CENTER,
											hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1,cObj.getAwarded_cost(),
											garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1,cObj.getRevised_amount(),
											garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getDoc(), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getRevised_doc(), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);
									
									
									addTableCell(factory, wordMLPackage, contentRow1, search[i], garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);					
									addTableCell(factory, wordMLPackage, contentRow1, search1[i], garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getCumulative_expenditure(), garamondRPr,
											JcEnumeration.RIGHT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getPhysicalProgress(), garamondRPr,
											JcEnumeration.RIGHT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getTarget_doc(), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_status_fk(), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);						
									table.getContent().add(contentRow1);								  
							  }
							  Total=Total+GrLength;
							  for(int i=0; i<GrLength; i++)
							  {
								  lenGr=1;
								  
									mergeCellsVertically(table, 0,Concat, Total);
									mergeCellsVertically(table, 1, Concat, Total);
									mergeCellsVertically(table, 2, Concat, Total);
									mergeCellsVertically(table, 3, Concat,Total);
									mergeCellsVertically(table, 4, Concat,Total);
									mergeCellsVertically(table, 5, Concat,Total);
									mergeCellsVertically(table, 6, Concat,Total);
									mergeCellsVertically(table, 7, Concat,Total);
									
									
									mergeCellsVertically(table, 10, Concat,Total);
									mergeCellsVertically(table, 11, Concat,Total);
									mergeCellsVertically(table, 12, Concat,Total);
									mergeCellsVertically(table, 13, Concat,Total);
									
									String R1=search[i].replace("\n  ", "");
									String R2=search1[i].replace("\n  ", "");								
									
									  if(R1=="")
									  {
										  mergeCellsVertically(table, 8, Concat,Total);
									  }									
									  if(R2=="")
									  {
										  mergeCellsVertically(table, 9, Concat,Total);
									  }										
									
							  }
							  Concat=Concat+GrLength;								  
						}
						if(lenGr==1)
						{
							NumVal=NumVal+1;
						}
						
					}
					if (StringUtils.isEmpty(contracts) || contracts.isEmpty()) {
						boolean hasBgColor = false;
						String backgroundColor = null;
						Tr contentRow = factory.createTr();
	
						List<String> noDataRow = new ArrayList<String>();
						//noDataRow.add("Nil");
						for (int i = 0; i < 14; i++) 
						{
							noDataRow.add("Nil");
						}
	
						for (String headerValue : noDataRow) {
							addTableCell(factory, wordMLPackage, contentRow, headerValue, calibriBoldRPr, JcEnumeration.CENTER,
									hasBgColor, backgroundColor);
						}
						table.getContent().add(contentRow);
						mergeCellsHorizontal(table, 2, 0, 13);
					}
					setTableAlign(factory, table, JcEnumeration.CENTER);
					mp.addObject(table);
				}
				else
				{
					for(int i1=0;i1<2;i1++)
					{
					
						/***************************************************************/
						
						Tbl table = factory.createTbl();
						addBorders(table, "2");
						
						/************************************************************************/
						
						if(i1==0)
						{
							Tr hodRow3 = factory.createTr();

							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "OPEN CONTRACTS", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow3, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							
			
							
							tableHead.getContent().add(hodRow3);
							mergeCellsHorizontal(tableHead, 1, 0, 13);
							

						}
						
						Tr titleRow0 = factory.createTr();
						List<String> tableHeader0 = new ArrayList<String>();

						if(i1==1)
						{
							Tr hodRow4 = factory.createTr();

							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "CLOSED CONTRACTS", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow4, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
							
			
							
							table.getContent().add(hodRow4);
							mergeCellsHorizontal(table, 0, 0, 13);
							

						}
						
						
						tableHeader0.add("SN");
						tableHeader0.add("Short name of Contract");
						tableHeader0.add("Agency");
						tableHeader0.add("Date of Award");
						tableHeader0.add("Contract Value (Cr.)");
						tableHeader0.add("");	
						tableHeader0.add("Date of Completion");
						tableHeader0.add("");					
						tableHeader0.add("BG Valid Upto");
						tableHeader0.add("Insurance Valid Upto");
						tableHeader0.add("Expenditure (Cr.)");
						tableHeader0.add("Physical Progress (%)");
						tableHeader0.add("Target Date of Completion");
						tableHeader0.add("Status of Work");
						
					
						
						int columnNo = 1;
						for (String headerValue : tableHeader0) 
						{
							int width = 0;
							if(1 == columnNo) {
								width = 230;
							}else if(2 == columnNo) {
								width = 830;
							}else if(3 == columnNo) {
								width = 1100;
							}else if(4 == columnNo) {
								width = 750;
							}else if(5 == columnNo) {
								width = 620;
							}else if(6 == columnNo) {
								width = 620;
							}else if(7 == columnNo) {
								width = 750;
							}else if(8 == columnNo) {
								width = 750;
							}
							else if(9 == columnNo) {
								width = 760;
							}
							else if(10 == columnNo) {
								width = 760;
							}
							else if(11 == columnNo) {
								width = 700;
							}
							else if(12 == columnNo) {
								width = 700;
							}
							else if(13 == columnNo) {
								width = 750;
							}
							else if(14 == columnNo) {
								width = 800;
							}					
							columnNo++;
							addTableCellAndWidth(factory, wordMLPackage, titleRow0, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
									"ecf2ff",width);
						}
						table.getContent().add(titleRow0);
						
						/************************************************************************************/
						Tr titleRow = factory.createTr();
						List<String> tableHeader = new ArrayList<String>();
						
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("Awarded");
						tableHeader.add("Revised");
						tableHeader.add("Original");
						tableHeader.add("Revised");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						
						
						columnNo = 1;
						for (String headerValue : tableHeader) {
							int width = 0;
							if(1 == columnNo) {
								width = 230;
							}else if(2 == columnNo) {
								width = 830;
							}else if(3 == columnNo) {
								width = 1100;
							}else if(4 == columnNo) {
								width = 750;
							}else if(5 == columnNo) {
								width = 620;
							}else if(6 == columnNo) {
								width = 620;
							}else if(7 == columnNo) {
								width = 750;
							}else if(8 == columnNo) {
								width = 750;
							}
							else if(9 == columnNo) {
								width = 760;
							}
							else if(10 == columnNo) {
								width = 760;
							}
							else if(11 == columnNo) {
								width = 700;
							}
							else if(12 == columnNo) {
								width = 700;
							}
							else if(13 == columnNo) {
								width = 760;
							}
							else if(14 == columnNo) {
								width = 800;
							}					
							columnNo++;
							addTableCellAndWidth(factory, wordMLPackage, titleRow, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
									"ecf2ff",width);
						}
					

						
						table.getContent().add(titleRow);
						
						if(i1==0)
						{
							mergeCellsVertically(table, 0, 0, 1);
							mergeCellsVertically(table, 1, 0, 1);
							mergeCellsVertically(table, 2, 0, 1);
							mergeCellsVertically(table, 3, 0, 1);
							
							mergeCellsVertically(table, 8,0, 1);
							mergeCellsVertically(table, 9, 0, 1);
							mergeCellsVertically(table, 10, 0, 1);
							mergeCellsVertically(table, 11, 0, 1);				
							mergeCellsVertically(table, 12, 0, 1);
							mergeCellsVertically(table, 13, 0, 1);
			
							
							 mergeCellsHorizontal(table, 0, 4, 5); mergeCellsHorizontal(table, 0, 6, 7);	
						}						
						
						
						if(i1==1)
						{
						
						mergeCellsVertically(table, 0, 1, 2);
						mergeCellsVertically(table, 1, 1, 2);
						mergeCellsVertically(table, 2, 1, 2);
						mergeCellsVertically(table, 3, 1, 2);
						
						mergeCellsVertically(table, 8,1, 2);
						mergeCellsVertically(table, 9, 1, 2);
						mergeCellsVertically(table, 10, 1, 2);
						mergeCellsVertically(table, 11, 1, 2);				
						mergeCellsVertically(table, 12, 1, 2);
						mergeCellsVertically(table, 13, 1, 2);
		
						
						 mergeCellsHorizontal(table, 1, 4, 5); mergeCellsHorizontal(table, 1, 6, 7);
						}
						 						
						/*******************************************************************************/
		
						int sNo = 1;
						int NumVal=1;
						int Total=2;
						int Concat=NumVal+2;
						int lenGr=0;
						
						
						if(i1==0)
						{
							Total=1;
							Concat=NumVal+1;								
						}
						if(i1==1)
						{
							Total=2;
							Concat=NumVal+2;							
						}
						for (Contract cObj : contracts) 
						{
							String Diff=cObj.getStatus();
							String ComVal="Open";
							String ComValCl="Closed";
							
							

							
							
							
							
									if(i1==0 && (Diff.compareTo(ComVal)==0))
									{
										String getBG="";
										String getBG1="";
								
										
										
										if (StringUtils.isEmpty(cObj.getPbg_valid_till()) || cObj.getPbg_valid_till().isEmpty()) 
										{
											getBG="";
										}
										else
										{
											getBG=cObj.getPbg_valid_till().toString();
										}					
										

										if (StringUtils.isEmpty(cObj.getInsurance_valid_till()) || cObj.getInsurance_valid_till().isEmpty()) 
										{
											getBG1="";
										}
										else
										{
											getBG1=cObj.getInsurance_valid_till().toString();
										}										
										if(getBG.indexOf("<space>")==-1 && getBG1.indexOf("<space>")==-1)
										{
											boolean hasBgColor = false;
											String backgroundColor = null;
											Tr contentRow = factory.createTr();
						
											addTableCell(factory, wordMLPackage, contentRow, String.valueOf(NumVal), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_short_name(), garamondRPr,
													JcEnumeration.LEFT, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getContractor_name(), garamondRPr,
													JcEnumeration.LEFT, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getLoa_date(), garamondRPr, JcEnumeration.CENTER,
													hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow,cObj.getAwarded_cost(),
													garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow,cObj.getRevised_amount(),
													garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getDoc(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getRevised_doc(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);
											
											
											addTableCell(factory, wordMLPackage, contentRow, cObj.getPbg_valid_till(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);					
											addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_valid_till(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getCumulative_expenditure(), garamondRPr,
													JcEnumeration.RIGHT, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getPhysicalProgress(), garamondRPr,
													JcEnumeration.RIGHT, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getTarget_doc(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_status_fk(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);						
											table.getContent().add(contentRow);
											Concat=Concat+1;
											Total=Total+1;
											lenGr=1;												
										}
										else
										{
											String[] search = getBG.split("<space>");
											String[] search1 = getBG1.split("<space>");

											int SerLen1 = search.length;
											int SerLen2 = search1.length;
											int GrLength = 0;
											  if(SerLen1>SerLen2)
											  {
												  GrLength=SerLen1;
												  for(int i=0; i<SerLen1-SerLen2; i++)
												  {
													  getBG1=getBG1+"<space>\n  ";
												  }								  
											  }
											  else
											  {
												  GrLength=SerLen2;
												  for(int i=0; i<SerLen2-SerLen1; i++)
												  {
													  getBG=getBG+"<space>\n  ";
												  }									  
											  }
											  lenGr=0;
											  for(int i=0; i<GrLength; i++)
											  {
												  search1 = getBG1.split("<space>");
												  search = getBG.split("<space>");
												  Tr contentRow1 = factory.createTr();
												  
													boolean hasBgColor = false;
													String backgroundColor = null;									
													addTableCell(factory, wordMLPackage, contentRow1, String.valueOf(NumVal), garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_short_name(), garamondRPr,
															JcEnumeration.LEFT, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getContractor_name(), garamondRPr,
															JcEnumeration.LEFT, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getLoa_date(), garamondRPr, JcEnumeration.CENTER,
															hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1,cObj.getAwarded_cost(),
															garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1,cObj.getRevised_amount(),
															garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getDoc(), garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getRevised_doc(), garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);
													
													
													addTableCell(factory, wordMLPackage, contentRow1, search[i], garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);					
													addTableCell(factory, wordMLPackage, contentRow1, search1[i], garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getCumulative_expenditure(), garamondRPr,
															JcEnumeration.RIGHT, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getPhysicalProgress(), garamondRPr,
															JcEnumeration.RIGHT, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getTarget_doc(), garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_status_fk(), garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);						
													table.getContent().add(contentRow1);								  
											  }
											  Total=Total+GrLength;
											  for(int i=0; i<GrLength; i++)
											  {
												  lenGr=1;
													mergeCellsVertically(table, 0,Concat, Total);
													mergeCellsVertically(table, 1, Concat, Total);
													mergeCellsVertically(table, 2, Concat, Total);
													mergeCellsVertically(table, 3, Concat,Total);
													mergeCellsVertically(table, 4, Concat,Total);
													mergeCellsVertically(table, 5, Concat,Total);
													mergeCellsVertically(table, 6, Concat,Total);
													mergeCellsVertically(table, 7, Concat,Total);
													
													
													mergeCellsVertically(table, 10, Concat,Total);
													mergeCellsVertically(table, 11, Concat,Total);
													mergeCellsVertically(table, 12, Concat,Total);
													mergeCellsVertically(table, 13, Concat,Total);								
													
													
											  }
											  Concat=Concat+GrLength;
										}	
										if(lenGr==1)
										{
											NumVal=NumVal+1;
										}											
									
									}
									if(i1==1 && (Diff.compareTo(ComValCl)==0))
									{
										String getBG="";
										String getBG1="";
								
										
										
										if (StringUtils.isEmpty(cObj.getPbg_valid_till()) || cObj.getPbg_valid_till().isEmpty()) 
										{
											getBG="";
										}
										else
										{
											getBG=cObj.getPbg_valid_till().toString();
										}					
										

										if (StringUtils.isEmpty(cObj.getInsurance_valid_till()) || cObj.getInsurance_valid_till().isEmpty()) 
										{
											getBG1="";
										}
										else
										{
											getBG1=cObj.getInsurance_valid_till().toString();
										}										
										if(getBG.indexOf("<space>")==-1 && getBG1.indexOf("<space>")==-1)
										{										
											boolean hasBgColor = false;
											String backgroundColor = null;
											Tr contentRow = factory.createTr();
											
						
											addTableCell(factory, wordMLPackage, contentRow, String.valueOf(NumVal), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_short_name(), garamondRPr,
													JcEnumeration.LEFT, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getContractor_name(), garamondRPr,
													JcEnumeration.LEFT, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getLoa_date(), garamondRPr, JcEnumeration.CENTER,
													hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow,cObj.getAwarded_cost(),
													garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow,cObj.getRevised_amount(),
													garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getDoc(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getRevised_doc(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);
											
											
											addTableCell(factory, wordMLPackage, contentRow, cObj.getPbg_valid_till(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);					
											addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_valid_till(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getCumulative_expenditure(), garamondRPr,
													JcEnumeration.RIGHT, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getPhysicalProgress(), garamondRPr,
													JcEnumeration.RIGHT, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getTarget_doc(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);
											addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_status_fk(), garamondRPr,
													JcEnumeration.CENTER, hasBgColor, backgroundColor);						
											table.getContent().add(contentRow);
											Concat=Concat+1;
											Total=Total+1;
											lenGr=1;											
									}
										
										else
										{
											String[] search = getBG.split("<space>");
											String[] search1 = getBG1.split("<space>");

											int SerLen1 = search.length;
											int SerLen2 = search1.length;
											int GrLength = 0;
											  if(SerLen1>SerLen2)
											  {
												  GrLength=SerLen1;
												  for(int i=0; i<SerLen1-SerLen2; i++)
												  {
													  getBG1=getBG1+"<space>\n  ";
												  }								  
											  }
											  else
											  {
												  GrLength=SerLen2;
												  for(int i=0; i<SerLen2-SerLen1; i++)
												  {
													  getBG=getBG+"<space>\n  ";
												  }									  
											  }
											  lenGr=0;
											  for(int i=0; i<GrLength; i++)
											  {
												  search1 = getBG1.split("<space>");
												  search = getBG.split("<space>");
												  Tr contentRow1 = factory.createTr();
												  
													boolean hasBgColor = false;
													String backgroundColor = null;									
													addTableCell(factory, wordMLPackage, contentRow1, String.valueOf(NumVal), garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_short_name(), garamondRPr,
															JcEnumeration.LEFT, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getContractor_name(), garamondRPr,
															JcEnumeration.LEFT, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getLoa_date(), garamondRPr, JcEnumeration.CENTER,
															hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1,cObj.getAwarded_cost(),
															garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1,cObj.getRevised_amount(),
															garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getDoc(), garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getRevised_doc(), garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);
													
													
													addTableCell(factory, wordMLPackage, contentRow1, search[i], garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);					
													addTableCell(factory, wordMLPackage, contentRow1, search1[i], garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getCumulative_expenditure(), garamondRPr,
															JcEnumeration.RIGHT, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getPhysicalProgress(), garamondRPr,
															JcEnumeration.RIGHT, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getTarget_doc(), garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);
													addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_status_fk(), garamondRPr,
															JcEnumeration.CENTER, hasBgColor, backgroundColor);						
													table.getContent().add(contentRow1);
													Concat=Concat+1;
													Total=Total+1;
													lenGr=1;														
											  }
											  Total=Total+GrLength;
											  for(int i=0; i<GrLength; i++)
											  {
												  lenGr=1;
													mergeCellsVertically(table, 0,Concat, Total);
													mergeCellsVertically(table, 1, Concat, Total);
													mergeCellsVertically(table, 2, Concat, Total);
													mergeCellsVertically(table, 3, Concat,Total);
													mergeCellsVertically(table, 4, Concat,Total);
													mergeCellsVertically(table, 5, Concat,Total);
													mergeCellsVertically(table, 6, Concat,Total);
													mergeCellsVertically(table, 7, Concat,Total);
													
													
													mergeCellsVertically(table, 10, Concat,Total);
													mergeCellsVertically(table, 11, Concat,Total);
													mergeCellsVertically(table, 12, Concat,Total);
													mergeCellsVertically(table, 13, Concat,Total);								
													
													
											  }
											  Concat=Concat+GrLength;
										}
										if(lenGr==1)
										{
											NumVal=NumVal+1;
										}										
						
							}
								
						
						}
						if (StringUtils.isEmpty(contracts) || contracts.isEmpty()) {
							boolean hasBgColor = false;
							String backgroundColor = null;
							Tr contentRow = factory.createTr();
		
							List<String> noDataRow = new ArrayList<String>();
							//noDataRow.add("Nil");
							for (int i = 0; i < 14; i++) {
								noDataRow.add("Nil");
							}
		
							for (String headerValue : noDataRow) {
								addTableCell(factory, wordMLPackage, contentRow, headerValue, titleRpr, JcEnumeration.CENTER,
										hasBgColor, backgroundColor);
							}
							table.getContent().add(contentRow);
							mergeCellsHorizontal(table, 2, 0, 13);
						}
						setTableAlign(factory, table, JcEnumeration.CENTER);
						mp.addObject(table);						
					}
				}
				tempVal++;
			}

		} catch (Exception e) {
			throw new Exception(e);
		}

	}
	public static P newImage( WordprocessingMLPackage wordMLPackage,ObjectFactory factory, byte[] bytes, 
            String filenameHint, String altText, int id1, int id2) throws Exception {
    	
    	Integer HEADER_TOP_OFFSET = 0;
    	
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
        Inline inline = imagePart.createImageInline( filenameHint, altText, id1, id2,900,false);
        
        /*String anchorXml = XmlUtils.marshaltoString(inline, true, false, Context.jc, Namespaces.NS_WORD12, "anchor",
                Inline.class);
           
             org.docx4j.dml.ObjectFactory dmlFactory = new org.docx4j.dml.ObjectFactory();
            org.docx4j.dml.wordprocessingDrawing.ObjectFactory wordDmlFactory = new org.docx4j.dml.wordprocessingDrawing.ObjectFactory();

        
        Anchor anchor = (Anchor) XmlUtils.unmarshalString(anchorXml, Context.jc, Anchor.class);
        
        anchor.setSimplePos(dmlFactory.createCTPoint2D());
       anchor.getSimplePos().setX(20L);
       anchor.getSimplePos().setY(50L);
       anchor.setSimplePosAttr(false);
       anchor.setPositionH(wordDmlFactory.createCTPosH());
       anchor.getPositionH().setAlign(STAlignH.CENTER);
       anchor.getPositionH().setRelativeFrom(STRelFromH.MARGIN);
       anchor.setPositionV(wordDmlFactory.createCTPosV());
       anchor.getPositionV().setPosOffset(HEADER_TOP_OFFSET);
       anchor.getPositionV().setRelativeFrom(STRelFromV.PAGE);
       anchor.setWrapNone(wordDmlFactory.createCTWrapNone());*/
       
        
        P  p = factory.createP();
        R  run = factory.createR();
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
		jc.setVal(JcEnumeration.CENTER);
		pPr.setJc(jc);
		p.setPPr(pPr);

        return p;
    } 
	
	public static void createTableForListOfContractsReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, Map<String, List<Contract>> contractsData,String report_created_date) throws Exception {

		try {

			RPr titleRpr = getRPr(factory, "Calibri", "000000", "12", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Calibri", "000000", "12", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false,
					false);
			
			RPr calibriBoldRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr calibriBoldRPr1 = getRPr(factory, "Calibri", "808080", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr calibriBoldRPr2 = getRPr(factory, "Calibri", "000000", "26", STHint.EAST_ASIA,
					true, false, false, false);				
			
			RPr calibriBoldDateRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);

			Tbl table = factory.createTbl();
			addBorders(table, "2");
			Tr titleRow0 = factory.createTr();
			List<String> tableHeader0 = new ArrayList<String>();
			tableHeader0.add("S. No.");
			tableHeader0.add("Status of Contract");
			tableHeader0.add("Contract ID");
			tableHeader0.add("Contract Short Name");
			tableHeader0.add("Contractor Name");
			tableHeader0.add("LOA Date");	
			tableHeader0.add("Original Completion Date");
			tableHeader0.add("Revised Completion Date");					
			tableHeader0.add("Progress %");
			tableHeader0.add("Estimated Cost (Cr)");
			tableHeader0.add("Awarded Cost (Cr)");
			tableHeader0.add("Revised Cost (Cr)");
			tableHeader0.add("Expenditure (Cr)");
			tableHeader0.add("BG Valid Upto");
			tableHeader0.add("Insurance Valid Upto");
			
			int columnNo = 1;
			for (String headerValue : tableHeader0) {
				int width = 0;
				if(1 == columnNo) {
					width = 230;
				}else if(2 == columnNo) {
					width = 830;
				}else if(3 == columnNo) {
					width = 1100;
				}else if(4 == columnNo) {
					width = 750;
				}else if(5 == columnNo) {
					width = 620;
				}else if(6 == columnNo) {
					width = 620;
				}else if(7 == columnNo) {
					width = 750;
				}else if(8 == columnNo) {
					width = 750;
				}
				else if(9 == columnNo) {
					width = 760;
				}
				else if(10 == columnNo) {
					width = 760;
				}
				else if(11 == columnNo) {
					width = 700;
				}
				else if(12 == columnNo) {
					width = 700;
				}
				else if(13 == columnNo) {
					width = 750;
				}
				else if(14 == columnNo) {
					width = 800;
				}
				else if(15 == columnNo) {
					width = 800;
				}					
				columnNo++;
				addTableCellAndWidth(factory, wordMLPackage, titleRow0, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
						"ecf2ff",width);
			}
			table.getContent().add(titleRow0);

			
			/************************************************************************************/
			Tr titleRow = factory.createTr();
			List<String> tableHeader = new ArrayList<String>();
			tableHeader.add("Work:");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			tableHeader.add("");
			
			columnNo = 1;
			for (String headerValue : tableHeader) {
				int width = 0;
				if(1 == columnNo) {
					width = 230;
				}else if(2 == columnNo) {
					width = 830;
				}else if(3 == columnNo) {
					width = 1100;
				}else if(4 == columnNo) {
					width = 750;
				}else if(5 == columnNo) {
					width = 620;
				}else if(6 == columnNo) {
					width = 620;
				}else if(7 == columnNo) {
					width = 750;
				}else if(8 == columnNo) {
					width = 750;
				}
				else if(9 == columnNo) {
					width = 760;
				}
				else if(10 == columnNo) {
					width = 760;
				}
				else if(11 == columnNo) {
					width = 700;
				}
				else if(12 == columnNo) {
					width = 700;
				}
				else if(13 == columnNo) {
					width = 760;
				}
				else if(14 == columnNo) {
					width = 800;
				}	
				else if(15 == columnNo) {
					width = 800;
				}				
				columnNo++;
				addTableCellAndWidth(factory, wordMLPackage, titleRow, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,"ecf2ff",width);
			}
			table.getContent().add(titleRow);
			mp.addObject(table);
			

		} catch (Exception e) {
			throw new Exception(e);
		}

	}	
	

	public static void createTableForContractReportAll(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, Map<String, List<Contract>> contractsData,String report_created_date) throws Exception {

		try {

			RPr titleRpr = getRPr(factory, "Calibri", "000000", "12", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Calibri", "000000", "12", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false,
					false);
			
			RPr calibriBoldRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr calibriBoldRPr1 = getRPr(factory, "Calibri", "808080", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr calibriBoldRPr2 = getRPr(factory, "Calibri", "000000", "26", STHint.EAST_ASIA,
					true, false, false, false);				
			
			RPr calibriBoldDateRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);

			int temp = 1;
			int tempVal=0;
			String sArray[] = new String[] { "Open","Yet to be Awarded","Closed"};
			List<String> lList = Arrays.asList(sArray);
			for (int i2 = 0; i2 < lList.size(); i2++) 
			{		
				

				
				int repeat=0;
				int repeat1=0;

			for (Map.Entry<String, List<Contract>> entry : contractsData.entrySet()) 
			{
				//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
				List<Contract> contracts = entry.getValue();
				String keyName = entry.getKey();

				//addParagraph(mp, factory);
				//addHeading(wordMLPackage, mp, factory, JcEnumeration.LEFT, calibriBoldRPr, keyName);
				Tbl tableHead = factory.createTbl();
				setLandscapeTableAlign(factory, tableHead, JcEnumeration.CENTER);
				//addBorders(tableHead, "0");
				
				/**************************************************************************/

				if(i2==0)
				{
					if(repeat1==0)
					{
					Tr hodRow51 = factory.createTr();
					
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "OPEN CONTRACTS", calibriBoldRPr2, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow51, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
					
					
					tableHead.getContent().add(hodRow51);
					mergeCellsHorizontal(tableHead, 0, 0, 13);	
					}
				}
				
				if(i2==1)
				{
					if(repeat==0)
					{
						addPageBreak(mp);
						
						Tr hodRow5 = factory.createTr();
						
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "YET TO BE AWARDED CONTRACTS", calibriBoldRPr2, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						
						tableHead.getContent().add(hodRow5);
						mergeCellsHorizontal(tableHead, 0, 0, 13);
						
					}
				}
			
				if(i2==2)
				{
					if(repeat==0)
					{
						addPageBreak(mp);

						Tr hodRow5 = factory.createTr();
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "CLOSED CONTRACTS", calibriBoldRPr2, JcEnumeration.CENTER, true,"ffffff");
						
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow5, "", calibriBoldRPr, JcEnumeration.CENTER, true,"ffffff");
						
						tableHead.getContent().add(hodRow5);
						mergeCellsHorizontal(tableHead, 0, 0, 13);
			
						
					}
				}
			
				Tr hodRow = factory.createTr();
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, keyName, calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				
				String date = "";
				if(temp == 1) {
					date = report_created_date;
				}else {
					addParagraph(mp, factory);
				}
				temp++;
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, date, calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");				
				
				tableHead.getContent().add(hodRow);
				if(i2==0)
				{
					
					if(repeat1==0)
					{
						mergeCellsHorizontal(tableHead, 1, 0, 6);
						mergeCellsHorizontal(tableHead, 1, 7, 13);
					    repeat1++;
					}
					else
					{
						mergeCellsHorizontal(tableHead, 0, 0, 13);
						
					}					
			
				}
				
				if(i2==1)
				{
						if(repeat==0)
						{
							mergeCellsHorizontal(tableHead, 1, 0, 13);
							/*mergeCellsHorizontal(tableHead, 2, 0, 13);*/
						    repeat++;
						}
						else
						{
							mergeCellsHorizontal(tableHead, 0, 0, 13);
							
						}
										
				}
				if(i2==2)
				{
						if(repeat==0)
						{
							mergeCellsHorizontal(tableHead, 1, 0, 13);
							/*mergeCellsHorizontal(tableHead, 2, 0, 13);*/
						    repeat++;
						}
						else
						{
							mergeCellsHorizontal(tableHead, 0, 0, 13);
							
						}
										
				}
				mp.addObject(tableHead);

	
					int sNo = 1;
					int NumVal=1;
					int Total=1;
					int Concat=NumVal+1;
					int lenGr=0;

						
						/***************************************************************/
						
						Tbl table = factory.createTbl();
						addBorders(table, "2");
						
						/************************************************************************/
						Tr titleRow0 = factory.createTr();
						List<String> tableHeader0 = new ArrayList<String>();
						tableHeader0.add("SN");
						tableHeader0.add("Short name of Contract");
						tableHeader0.add("Agency");
						tableHeader0.add("Date of Award");
						tableHeader0.add("Contract Value (Cr.)");
						tableHeader0.add("");	
						tableHeader0.add("Date of Completion");
						tableHeader0.add("");					
						tableHeader0.add("BG Valid Upto");
						tableHeader0.add("Insurance Valid Upto");
						tableHeader0.add("Expenditure (Cr.)");
						tableHeader0.add("Physical Progress (%)");
						tableHeader0.add("Target Date of Completion");
						tableHeader0.add("Status of Work");
						
						int columnNo = 1;
						for (String headerValue : tableHeader0) {
							int width = 0;
							if(1 == columnNo) {
								width = 230;
							}else if(2 == columnNo) {
								width = 830;
							}else if(3 == columnNo) {
								width = 1100;
							}else if(4 == columnNo) {
								width = 750;
							}else if(5 == columnNo) {
								width = 620;
							}else if(6 == columnNo) {
								width = 620;
							}else if(7 == columnNo) {
								width = 750;
							}else if(8 == columnNo) {
								width = 750;
							}
							else if(9 == columnNo) {
								width = 760;
							}
							else if(10 == columnNo) {
								width = 760;
							}
							else if(11 == columnNo) {
								width = 700;
							}
							else if(12 == columnNo) {
								width = 700;
							}
							else if(13 == columnNo) {
								width = 750;
							}
							else if(14 == columnNo) {
								width = 800;
							}					
							columnNo++;
							addTableCellAndWidth(factory, wordMLPackage, titleRow0, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
									"ecf2ff",width);
						}
						table.getContent().add(titleRow0);
		
						mergeCellsHorizontal(table, 0, 4, 5);	
						mergeCellsHorizontal(table, 0, 6, 7);
						
						/************************************************************************************/
						Tr titleRow = factory.createTr();
						List<String> tableHeader = new ArrayList<String>();
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("Awarded");
						tableHeader.add("Revised");
						tableHeader.add("Original");
						tableHeader.add("Revised");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						tableHeader.add("");
						
						columnNo = 1;
						for (String headerValue : tableHeader) {
							int width = 0;
							if(1 == columnNo) {
								width = 230;
							}else if(2 == columnNo) {
								width = 830;
							}else if(3 == columnNo) {
								width = 1100;
							}else if(4 == columnNo) {
								width = 750;
							}else if(5 == columnNo) {
								width = 620;
							}else if(6 == columnNo) {
								width = 620;
							}else if(7 == columnNo) {
								width = 750;
							}else if(8 == columnNo) {
								width = 750;
							}
							else if(9 == columnNo) {
								width = 760;
							}
							else if(10 == columnNo) {
								width = 760;
							}
							else if(11 == columnNo) {
								width = 700;
							}
							else if(12 == columnNo) {
								width = 700;
							}
							else if(13 == columnNo) {
								width = 760;
							}
							else if(14 == columnNo) {
								width = 800;
							}					
							columnNo++;
							addTableCellAndWidth(factory, wordMLPackage, titleRow, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
									"ecf2ff",width);
						}
						table.getContent().add(titleRow);
						
						mergeCellsVertically(table, 0, 0, 1);
						mergeCellsVertically(table, 1, 0, 1);
						mergeCellsVertically(table, 2, 0, 1);
						mergeCellsVertically(table, 3, 0, 1);
						
						mergeCellsVertically(table, 8, 0, 1);
						mergeCellsVertically(table, 9, 0, 1);
						mergeCellsVertically(table, 10, 0, 1);
						mergeCellsVertically(table, 11, 0, 1);				
						mergeCellsVertically(table, 12, 0, 1);
						mergeCellsVertically(table, 13, 0, 1);
		
						
						/*******************************************************************************/						

					for (Contract cObj : contracts) 
					{
						if(lList.get(i2).compareTo(cObj.getStatus())==0)
								{
						String getBG="";
						String getBG1="";
						
						if (StringUtils.isEmpty(cObj.getPbg_valid_till()) || cObj.getPbg_valid_till().isEmpty()) 
						{
							getBG="";
						}
						else
						{
							getBG=cObj.getPbg_valid_till().toString();
						}					
						

						if (StringUtils.isEmpty(cObj.getInsurance_valid_till()) || cObj.getInsurance_valid_till().isEmpty()) 
						{
							getBG1="";
						}
						else
						{
							getBG1=cObj.getInsurance_valid_till().toString();
						}						
						
						boolean hasBgColor = false;
						String backgroundColor = null;
						
						if(getBG.indexOf("<space>")==-1 && getBG1.indexOf("<space>")==-1)
						{						
							Tr contentRow = factory.createTr();
	
							addTableCell(factory, wordMLPackage, contentRow, String.valueOf(NumVal), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_short_name(), garamondRPr,
									JcEnumeration.LEFT, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getContractor_name(), garamondRPr,
									JcEnumeration.LEFT, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getLoa_date(), garamondRPr, JcEnumeration.CENTER,
									hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow,cObj.getAwarded_cost(),
									garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow,cObj.getRevised_amount(),
									garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getDoc(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getRevised_doc(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);
							
							
							addTableCell(factory, wordMLPackage, contentRow, cObj.getPbg_valid_till(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);					
							addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_valid_till(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getCumulative_expenditure(), garamondRPr,
									JcEnumeration.RIGHT, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getPhysicalProgress(), garamondRPr,
									JcEnumeration.RIGHT, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getTarget_doc(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);
							addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_status_fk(), garamondRPr,
									JcEnumeration.CENTER, hasBgColor, backgroundColor);						
							table.getContent().add(contentRow);
							Concat=Concat+1;
							Total=Total+1;
							lenGr=1;						
						}
						else
						{
							String[] search = getBG.split("<space>");
							String[] search1 = getBG1.split("<space>");

							int SerLen1 = search.length;
							int SerLen2 = search1.length;
							int GrLength = 0;
							  if(SerLen1>SerLen2)
							  {
								  GrLength=SerLen1;
								  for(int i=0; i<SerLen1-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
								  }								  
							  }
							  else
							  {
								  GrLength=SerLen2;
								  for(int i=0; i<SerLen2-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }									  
							  }
							  lenGr=0;
							  for(int i=0; i<GrLength; i++)
							  {
								  search1 = getBG1.split("<space>");
								  search = getBG.split("<space>");
								  Tr contentRow1 = factory.createTr();
								  
									
									addTableCell(factory, wordMLPackage, contentRow1, String.valueOf(NumVal), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_short_name(), garamondRPr,
											JcEnumeration.LEFT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getContractor_name(), garamondRPr,
											JcEnumeration.LEFT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getLoa_date(), garamondRPr, JcEnumeration.CENTER,
											hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1,cObj.getAwarded_cost(),
											garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1,cObj.getRevised_amount(),
											garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getDoc(), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getRevised_doc(), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);
									
									
									addTableCell(factory, wordMLPackage, contentRow1, search[i], garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);					
									addTableCell(factory, wordMLPackage, contentRow1, search1[i], garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getCumulative_expenditure(), garamondRPr,
											JcEnumeration.RIGHT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getPhysicalProgress(), garamondRPr,
											JcEnumeration.RIGHT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getTarget_doc(), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_status_fk(), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);						
									table.getContent().add(contentRow1);								  
							  }
							  Total=Total+GrLength;
							  for(int i=0; i<GrLength; i++)
							  {
								  lenGr=1;
								  
									mergeCellsVertically(table, 0,Concat, Total);
									mergeCellsVertically(table, 1, Concat, Total);
									mergeCellsVertically(table, 2, Concat, Total);
									mergeCellsVertically(table, 3, Concat,Total);
									mergeCellsVertically(table, 4, Concat,Total);
									mergeCellsVertically(table, 5, Concat,Total);
									mergeCellsVertically(table, 6, Concat,Total);
									mergeCellsVertically(table, 7, Concat,Total);
									
									
									mergeCellsVertically(table, 10, Concat,Total);
									mergeCellsVertically(table, 11, Concat,Total);
									mergeCellsVertically(table, 12, Concat,Total);
									mergeCellsVertically(table, 13, Concat,Total);
									
									String R1=search[i].replace("\n  ", "");
									String R2=search1[i].replace("\n  ", "");								
									
									  if(R1=="")
									  {
										  mergeCellsVertically(table, 8, Concat,Total);
									  }									
									  if(R2=="")
									  {
										  mergeCellsVertically(table, 9, Concat,Total);
									  }										
									
							  }
							  Concat=Concat+GrLength;								  
						}
						if(lenGr==1)
						{
							NumVal=NumVal+1;
						}
						
					}
					}
					if (StringUtils.isEmpty(contracts) || contracts.isEmpty()) {
						boolean hasBgColor = false;
						String backgroundColor = null;
						Tr contentRow = factory.createTr();
	
						List<String> noDataRow = new ArrayList<String>();
						noDataRow.add("Nil");
						for (int i = 0; i < 7; i++) {
							noDataRow.add("");
						}
	
						for (String headerValue : noDataRow) {
							addTableCell(factory, wordMLPackage, contentRow, headerValue, titleRpr, JcEnumeration.CENTER,
									hasBgColor, backgroundColor);
						}
						table.getContent().add(contentRow);
						mergeCellsHorizontal(table, 2, 0, 7);
					}
					setTableAlign(factory, table, JcEnumeration.CENTER);
					mp.addObject(table);					
					}

				}
				tempVal++;
			

		} catch (Exception e) {
			throw new Exception(e);
		}

	}
	
	public static void createTableForContractDocReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, Map<String, List<Contract>> list,String report_created_date) throws Exception {
		try {

			RPr titleRpr = getRPr(factory, "Calibri", "000000", "18", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Calibri", "000000", "14", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false,
					false);
			
			RPr calibriBoldRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA,
					true, false, false, false);
			RPr calibriBoldDateRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);
			int temp = 1;
			for (Map.Entry<String,List<Contract>> hodEntry : list.entrySet()) {
				//addParagraph(mp, factory);
				//addHeading(wordMLPackage, mp, factory,JcEnumeration.LEFT,calibriBoldRPr,hodEntry.getKey());
				
				Tbl tableHead = factory.createTbl();
				setTableAlign(factory, tableHead, JcEnumeration.CENTER);
				//addBorders(tableHead, "0");
				
				/**************************************************************************/
				Tr hodRow = factory.createTr();
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, hodEntry.getKey(), calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				String date = "";
				if(temp == 1) {
					date = report_created_date;
				}else {
					addParagraph(mp, factory);
				}
				temp++;
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, date, calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				
				tableHead.getContent().add(hodRow);
				mergeCellsHorizontal(tableHead, 0, 0, 2);
				mergeCellsHorizontal(tableHead, 0, 3, 5);
				mp.addObject(tableHead);
				/***************************************************************/
				
				Tbl table = factory.createTbl();
				addBorders(table, "2");
				
				setTableAlign(factory, table, JcEnumeration.CENTER);
	
				Tr titleRow = factory.createTr();
				List<String> tableHeader = new ArrayList<String>();
				
				tableHeader.add("SN");
				tableHeader.add("Short name of Contract");
				tableHeader.add("Agency");
				tableHeader.add("Date of Award");
				tableHeader.add("DOC Valid Upto");
				tableHeader.add("Action Taken");
				
				int columnNo = 1;
				for (String headerValue : tableHeader) {
					int width = 0;
					if(1 == columnNo) {
						width = 250;
					}else if(2 == columnNo) {
						width = 1000;
					}else if(3 == columnNo) {
						width = 1200;
					}else if(4 == columnNo) {
						width = 630;
					}else if(5 == columnNo) {
						width = 630;
					}else if(6 == columnNo) {
						width = 750;
					}
					columnNo++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table.getContent().add(titleRow);
				int sNo = 1;
				for (Contract cObj : hodEntry.getValue()) {	
					boolean hasBgColor = false;
					String backgroundColor = null;
					Tr contentRow = factory.createTr();
	
					addTableCell(factory, wordMLPackage, contentRow, String.valueOf(sNo++), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_short_name(), garamondRPr,
							JcEnumeration.LEFT, hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getContractor_name(), garamondRPr,
							JcEnumeration.LEFT, hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getLoa_date(), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getDoc(), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getContractAlertRemarks(), garamondRPr,
							JcEnumeration.CENTER, hasBgColor, backgroundColor);					
					
					table.getContent().add(contentRow);
				}
				
				if (StringUtils.isEmpty(hodEntry.getValue()) || hodEntry.getValue().isEmpty()) {
					boolean hasBgColor = false;
					String backgroundColor = null;
					Tr contentRow = factory.createTr();

					List<String> noDataRow = new ArrayList<String>();
					noDataRow.add("Nil");
					for (int i = 0; i < 5; i++) {
						noDataRow.add("");
					}

					for (String headerValue : noDataRow) {
						addTableCellAndWidth(factory, wordMLPackage, contentRow, headerValue, garamondRPr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor,0);
					}
					table.getContent().add(contentRow);
					mergeCellsHorizontal(table, 1, 0, 5);
				}

				
				mp.addObject(table);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	public static void createTableForContractDocBGInsuranceReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, Map<String, List<Contract>> list,String report_created_date) throws Exception {
		try {

			RPr titleRpr = getRPr(factory, "Calibri", "000000", "18", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Calibri", "000000", "14", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false,
					false);
			
			RPr calibriBoldRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA,
					true, false, false, false);
			RPr calibriBoldDateRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);
			int temp = 1;
			for (Map.Entry<String,List<Contract>> hodEntry : list.entrySet()) {
				//addParagraph(mp, factory);
				//addHeading(wordMLPackage, mp, factory,JcEnumeration.LEFT,calibriBoldRPr,hodEntry.getKey());
				
				Tbl tableHead = factory.createTbl();
				setTableAlign(factory, tableHead, JcEnumeration.CENTER);
				//addBorders(tableHead, "0");
				
				/**************************************************************************/
				Tr hodRow = factory.createTr();
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, hodEntry.getKey(), calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				
				String date = "";
				if(temp == 1) {
					date = report_created_date;
				}else {
					addParagraph(mp, factory);
				}
				temp++;
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, date, calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				
				tableHead.getContent().add(hodRow);
				mergeCellsHorizontal(tableHead, 0, 0, 3);
				mergeCellsHorizontal(tableHead, 0, 4, 6);
				mp.addObject(tableHead);
				/***************************************************************/
				
				Tbl table = factory.createTbl();
				addBorders(table, "2");
				
				setTableAlign(factory, table, JcEnumeration.CENTER);
	
				Tr titleRow = factory.createTr();
				List<String> tableHeader = new ArrayList<String>();
				
				tableHeader.add("SN");
				tableHeader.add("Short name of Contract");
				tableHeader.add("Agency");
				tableHeader.add("DOC Valid Upto");
				tableHeader.add("BG Valid Upto");
				tableHeader.add("Insurance Valid Upto");
				tableHeader.add("Action Taken");
				
				int columnNo = 1;
				for (String headerValue : tableHeader) {
					int width = 0;
					if(1 == columnNo) {
						width = 230;
					}else if(2 == columnNo) {
						width = 1000;
					}else if(3 == columnNo) {
						width = 1000;
					}else if(4 == columnNo) {
						width = 650;
					}else if(5 == columnNo) {
						width = 650;
					}else if(6 == columnNo) {
						width = 650;
					}
					else if(7 == columnNo) {
						width = 750;
					}					
					columnNo++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table.getContent().add(titleRow);
				int sNo = 1;
				int NumVal=1;
				int Total=0;
				int Concat=NumVal;
				int lenGr=0;
				for (Contract cObj : hodEntry.getValue()) 
				{	lenGr=0;
				
					boolean hasBgColor = false;
					String backgroundColor = null;
					
					String getBG="";
					String getBG1="";
					String getBG2="";
			
					
					
					if (StringUtils.isEmpty(cObj.getBg_valid_upto()) || cObj.getBg_valid_upto().isEmpty()) 
					{
						getBG="";
					}
					else
					{
						getBG=cObj.getBg_valid_upto().toString();
					}					
					

					if (StringUtils.isEmpty(cObj.getInsurance_valid_upto()) || cObj.getInsurance_valid_upto().isEmpty()) 
					{
						getBG1="";
					}
					else
					{
						getBG1=cObj.getInsurance_valid_upto().toString();
					}
					
					
					if (StringUtils.isEmpty(cObj.getContractAlertRemarks()) || cObj.getContractAlertRemarks().isEmpty()) 
					{
						getBG2="";
					}
					else
					{
						getBG2=cObj.getContractAlertRemarks().toString();
					}				
					
					
					
					if(getBG.indexOf("<space>")==-1 && getBG1.indexOf("<space>")==-1 && getBG2.indexOf("<space>")==-1)
					{					
					
					Tr contentRow = factory.createTr();
	
					addTableCell(factory, wordMLPackage, contentRow, String.valueOf(NumVal), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_short_name(), garamondRPr,
							JcEnumeration.LEFT, hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getContractor_name(), garamondRPr,
							JcEnumeration.LEFT, hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getDoc(), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getBg_valid_upto(), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_valid_upto(), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);					
					addTableCell(factory, wordMLPackage, contentRow,  cObj.getContractAlertRemarks(), garamondRPr,
							JcEnumeration.LEFT, hasBgColor, backgroundColor);					
					
					table.getContent().add(contentRow);
					
					Concat=Concat+1;
					Total=Total+1;
					lenGr=1;
					
				}
				else
				{					
					String[] search = getBG.split("<space>");
					String[] search1 = getBG1.split("<space>");
					String[] search2 = getBG2.split("<space>");

					int SerLen1 = search.length;
					int SerLen2 = search1.length;
					int SerLen3 = search2.length;

					int GrLength = 0;
					  
					  if(SerLen1>SerLen2 && SerLen1>SerLen3)
					  {
						  GrLength=SerLen1;
						  if(SerLen2==SerLen3)
						  {
							  for(int i=0; i<SerLen1-SerLen2; i++)
							  {
								  getBG1=getBG1+"<space>\n  ";
								  getBG2=getBG2+"<space>\n  ";
							  }
						  }
						  else if(SerLen2>SerLen3)
						  {
							  for(int i=0; i<SerLen1-SerLen2; i++)
							  {
								  getBG1=getBG1+"<space>\n  ";
							  }							  
						  }
						  else if(SerLen3>SerLen2)
						  {
							  for(int i=0; i<SerLen1-SerLen3; i++)
							  {
								  getBG2=getBG2+"<space>\n  ";
							  }							  
						  }						  
						  
					  }
					  else
					  {
						  if(SerLen2==SerLen3)
						  {
							  for(int i=0; i<SerLen2-SerLen1; i++)
							  {
								  getBG=getBG+"<space>\n  ";
							  }
							  GrLength=SerLen2;
						  }
						  else if(SerLen2>SerLen3)
						  {
							  for(int i=0; i<SerLen2-SerLen1; i++)
							  {
								  getBG=getBG+"<space>\n  ";
							  }	
							  GrLength=SerLen2;
						  }
						  else if(SerLen3>SerLen2)
						  {
							  for(int i=0; i<SerLen3-SerLen1; i++)
							  {
								  getBG=getBG+"<space>\n  ";
							  }
							  GrLength=SerLen3;
						  }	
						  
					  }					  
					  
					  if(SerLen2>SerLen3 && SerLen2>SerLen1)
					  {
						  GrLength=SerLen2;
						  if(SerLen3==SerLen1)
						  {
							  for(int i=0; i<SerLen2-SerLen3; i++)
							  {
								  getBG=getBG+"<space>\n  ";
								  getBG2=getBG2+"<space>\n  ";
							  }
						  }
						  else if(SerLen1>SerLen3)
						  {
							  for(int i=0; i<SerLen2-SerLen1; i++)
							  {
								  getBG=getBG+"<space>\n  ";
							  }							  
						  }
						  else if(SerLen3>SerLen1)
						  {
							  for(int i=0; i<SerLen2-SerLen3; i++)
							  {
								  getBG2=getBG2+"<space>\n  ";
							  }							  
						  }						  
					  }	
					  else
					  {
						  if(SerLen3==SerLen1)
						  {
							  for(int i=0; i<SerLen3-SerLen2; i++)
							  {
								  getBG1=getBG1+"<space>\n  ";
							  }
							  GrLength=SerLen3;
						  }
						  else if(SerLen1>SerLen3)
						  {
							  for(int i=0; i<SerLen1-SerLen2; i++)
							  {
								  getBG1=getBG1+"<space>\n  ";
							  }
							  GrLength=SerLen1;
						  }
						  else if(SerLen3>SerLen1)
						  {
							  for(int i=0; i<SerLen3-SerLen2; i++)
							  {
								  getBG1=getBG1+"<space>\n  ";
							  }	
							  GrLength=SerLen3;
						  }						  
					  }					  
					  
					  
					  if(SerLen3>SerLen1 && SerLen3>SerLen2)
					  {
						  GrLength=SerLen3;
						  if(SerLen2==SerLen1)
						  {
							  for(int i=0; i<SerLen3-SerLen2; i++)
							  {
								  getBG=getBG+"<space>\n  ";
								  getBG1=getBG1+"<space>\n  ";
							  }
						  }
						  else if(SerLen1>SerLen2)
						  {
							  for(int i=0; i<SerLen3-SerLen1; i++)
							  {
								  getBG=getBG+"<space>\n  ";
							  }							  
						  }
						  else if(SerLen2>SerLen1)
						  {
							  for(int i=0; i<SerLen3-SerLen2; i++)
							  {
								  getBG2=getBG2+"<space>\n  ";
							  }							  
						  }						  
					  }
					  else
					  {
						  if(SerLen2==SerLen1)
						  {
							  for(int i=0; i<SerLen2-SerLen3; i++)
							  {
								  getBG2=getBG2+"<space>\n  ";
							  }
							  GrLength=SerLen2;
						  }
						  else if(SerLen1>SerLen2)
						  {
							  for(int i=0; i<SerLen1-SerLen3; i++)
							  {
								  getBG2=getBG2+"<space>\n  ";
							  }	
							  GrLength=SerLen1;
						  }
						  else if(SerLen2>SerLen1)
						  {
							  for(int i=0; i<SerLen3-SerLen3; i++)
							  {
								  getBG2=getBG2+"<space>\n  ";
							  }
							  GrLength=SerLen2;
						  }						  
					  }

					  lenGr=0;
						  for(int i=0; i<GrLength; i++)
						  {

							  search1 = getBG1.split("<space>");
							  search = getBG.split("<space>");
							  search2 = getBG2.split("<space>");
							  
							  
							  
							  
							  Tr contentRow1 = factory.createTr();
							  
								addTableCell(factory, wordMLPackage, contentRow1, String.valueOf(NumVal), garamondRPr, JcEnumeration.CENTER,
										hasBgColor, backgroundColor);
								addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_short_name(), garamondRPr,
										JcEnumeration.LEFT, hasBgColor, backgroundColor);
								addTableCell(factory, wordMLPackage, contentRow1, cObj.getContractor_name(), garamondRPr,
										JcEnumeration.LEFT, hasBgColor, backgroundColor);
								addTableCell(factory, wordMLPackage, contentRow1, cObj.getDoc(), garamondRPr, JcEnumeration.CENTER,
										hasBgColor, backgroundColor);
								addTableCell(factory, wordMLPackage, contentRow1, search[i], garamondRPr, JcEnumeration.CENTER,
										hasBgColor, backgroundColor);
								addTableCell(factory, wordMLPackage, contentRow1, search1[i], garamondRPr, JcEnumeration.CENTER,
										hasBgColor, backgroundColor);					
								addTableCell(factory, wordMLPackage, contentRow1,  search2[i], garamondRPr,
										JcEnumeration.LEFT, hasBgColor, backgroundColor);								  
							  
							  
								table.getContent().add(contentRow1);


						  }	
						  Total=Total+GrLength;
						  for(int i=0; i<GrLength; i++)
						  {
							  lenGr=1;
								mergeCellsVertically(table, 0,Concat, Total);
								mergeCellsVertically(table, 1, Concat, Total);
								mergeCellsVertically(table, 2, Concat, Total);
								mergeCellsVertically(table, 3, Concat,Total);
								
						  }
							Concat=Concat+GrLength;	
				}
					if(lenGr==1)
					{
						NumVal=NumVal+1;
					}
				}
				
				if (StringUtils.isEmpty(hodEntry.getValue()) || hodEntry.getValue().isEmpty()) {
				boolean hasBgColor1 = false;
					String backgroundColor1 = null;
					Tr contentRow = factory.createTr();

					List<String> noDataRow = new ArrayList<String>();
					noDataRow.add("Nil");
					for (int i = 0; i < 6; i++) {
						noDataRow.add("");
					}

					for (String headerValue : noDataRow) {
						addTableCellAndWidth(factory, wordMLPackage, contentRow, headerValue, garamondRPr, JcEnumeration.CENTER,
								hasBgColor1, backgroundColor1,0);
					}
					table.getContent().add(contentRow);
					mergeCellsHorizontal(table, 1, 0, 5);
				}

				
				mp.addObject(table);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	

	public static void createTableForContractBGReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, Map<String, List<Contract>> list,String report_created_date) throws Exception {
		try {

			RPr titleRpr = getRPr(factory, "Calibri", "000000", "18", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Calibri", "000000", "14", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false,
					false);
			
			RPr calibriBoldRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA,
					true, false, false, false);
			RPr calibriBoldDateRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);
			int temp = 1;
			for (Map.Entry<String,List<Contract>> hodEntry : list.entrySet()) {
				//addParagraph(mp, factory);
				//addHeading(wordMLPackage, mp, factory,JcEnumeration.LEFT,calibriBoldRPr,hodEntry.getKey());
				
				Tbl tableHead = factory.createTbl();
				setTableAlign(factory, tableHead, JcEnumeration.CENTER);
				//addBorders(tableHead, "0");
				
				/**************************************************************************/
				Tr hodRow = factory.createTr();
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, hodEntry.getKey(), calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				
				String date = "";
				if(temp == 1) {
					date = report_created_date;
				}else {
					addParagraph(mp, factory);
				}
				temp++;
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, date, calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				
				tableHead.getContent().add(hodRow);
				mergeCellsHorizontal(tableHead, 0, 0, 3);
				mergeCellsHorizontal(tableHead, 0, 4, 6);
				mp.addObject(tableHead);
				/***************************************************************/
				
				Tbl table = factory.createTbl();
				addBorders(table, "2");
				
				setTableAlign(factory, table, JcEnumeration.CENTER);
	
				Tr titleRow = factory.createTr();
				List<String> tableHeader = new ArrayList<String>();
				
				tableHeader.add("SN");
				tableHeader.add("Short name of Contract");
				tableHeader.add("Agency");
				tableHeader.add("BG No");
				tableHeader.add("Rs/- \n(in lacs)");
				tableHeader.add("Valid Upto");
				tableHeader.add("Action Taken");
				
				int columnNo = 1;
				for (String headerValue : tableHeader) {
					int width = 0;
					if(1 == columnNo) {
						width = 230;
					}else if(2 == columnNo) {
						width = 900;
					}else if(3 == columnNo) {
						width = 900;
					}else if(4 == columnNo) {
						width = 1100;
					}else if(5 == columnNo) {
						width = 800;
					}else if(6 == columnNo) {
						width = 650;
					}
					else if(7 == columnNo) 
					{
						width = 750;
					}
					
					columnNo++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table.getContent().add(titleRow);
				int sNo = 1;
				int NumVal=1;
				int Total=0;
				int Concat=NumVal;
				for (Contract cObj : hodEntry.getValue()) 
				{	
					boolean hasBgColor = false;
					String backgroundColor = null;
					String getBG="";
					String getBG1="";
					String getBG2="";
					
					
					if (StringUtils.isEmpty(cObj.getBg_number()) || cObj.getBg_number().isEmpty()) 
					{
						getBG="";
					}
					else
					{
						getBG=cObj.getBg_number().toString();
					}					
					

					if (StringUtils.isEmpty(cObj.getBg_value()) || cObj.getBg_value().isEmpty()) 
					{
						getBG1="";
					}
					else
					{
						getBG1=cObj.getBg_value().toString();
					}
					
					
					if (StringUtils.isEmpty(cObj.getBg_valid_upto()) || cObj.getBg_valid_upto().isEmpty()) 
					{
						getBG2="";
					}
					else
					{
						getBG2=cObj.getBg_valid_upto().toString();
					}

					if(getBG.indexOf("<space>")==-1 && getBG1.indexOf("<space>")==-1 && getBG2.indexOf("<space>")==-1)
					{
						Tr contentRow = factory.createTr();
		
						addTableCell(factory, wordMLPackage, contentRow, String.valueOf(NumVal), garamondRPr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor);
						addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_short_name(), garamondRPr,
								JcEnumeration.LEFT, hasBgColor, backgroundColor);
						addTableCell(factory, wordMLPackage, contentRow, cObj.getContractor_name(), garamondRPr,
								JcEnumeration.LEFT, hasBgColor, backgroundColor);
						
						addTableCell(factory, wordMLPackage, contentRow, cObj.getBg_number(), garamondRPr, JcEnumeration.LEFT,
								hasBgColor, backgroundColor);
						
	
						addTableCell(factory, wordMLPackage, contentRow, cObj.getBg_value(), garamondRPr, JcEnumeration.RIGHT,
								hasBgColor, backgroundColor);
						addTableCell(factory, wordMLPackage, contentRow, cObj.getBg_valid_upto(), garamondRPr,
								JcEnumeration.CENTER, hasBgColor, backgroundColor);	
						addTableCell(factory, wordMLPackage, contentRow, cObj.getContractAlertRemarks(), garamondRPr,
								JcEnumeration.CENTER, hasBgColor, backgroundColor);						
						
						table.getContent().add(contentRow);
						Concat=Concat+1;
						Total=Total+1;						

					}
					else
					{
						String[] search = getBG.split("<space>");
						String[] search1 = getBG1.split("<space>");
						String[] search2 = getBG2.split("<space>");

						int SerLen1 = search.length;
						int SerLen2 = search1.length;
						int SerLen3 = search2.length;

						int GrLength = 0;
						  
						  if(SerLen1>SerLen2 && SerLen1>SerLen3)
						  {
							  GrLength=SerLen1;
							  if(SerLen2==SerLen3)
							  {
								  for(int i=0; i<SerLen1-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
									  getBG2=getBG2+"<space>\n  ";
								  }
							  }
							  else if(SerLen2>SerLen3)
							  {
								  for(int i=0; i<SerLen1-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
								  }							  
							  }
							  else if(SerLen3>SerLen2)
							  {
								  for(int i=0; i<SerLen1-SerLen3; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }							  
							  }						  
							  
						  }
						  else
						  {
							  if(SerLen2==SerLen3)
							  {
								  for(int i=0; i<SerLen2-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }
								  GrLength=SerLen2;
							  }
							  else if(SerLen2>SerLen3)
							  {
								  for(int i=0; i<SerLen2-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }	
								  GrLength=SerLen2;
							  }
							  else if(SerLen3>SerLen2)
							  {
								  for(int i=0; i<SerLen3-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }
								  GrLength=SerLen3;
							  }	
							  
						  }					  
						  
						  if(SerLen2>SerLen3 && SerLen2>SerLen1)
						  {
							  GrLength=SerLen2;
							  if(SerLen3==SerLen1)
							  {
								  for(int i=0; i<SerLen2-SerLen3; i++)
								  {
									  getBG=getBG+"<space>\n  ";
									  getBG2=getBG2+"<space>\n  ";
								  }
							  }
							  else if(SerLen1>SerLen3)
							  {
								  for(int i=0; i<SerLen2-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }							  
							  }
							  else if(SerLen3>SerLen1)
							  {
								  for(int i=0; i<SerLen2-SerLen3; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }							  
							  }						  
						  }	
						  else
						  {
							  if(SerLen3==SerLen1)
							  {
								  for(int i=0; i<SerLen3-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
								  }
								  GrLength=SerLen3;
							  }
							  else if(SerLen1>SerLen3)
							  {
								  for(int i=0; i<SerLen1-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
								  }
								  GrLength=SerLen1;
							  }
							  else if(SerLen3>SerLen1)
							  {
								  for(int i=0; i<SerLen3-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
								  }	
								  GrLength=SerLen3;
							  }						  
						  }					  
						  
						  
						  if(SerLen3>SerLen1 && SerLen3>SerLen2)
						  {
							  GrLength=SerLen3;
							  if(SerLen2==SerLen1)
							  {
								  for(int i=0; i<SerLen3-SerLen2; i++)
								  {
									  getBG=getBG+"<space>\n  ";
									  getBG1=getBG1+"<space>\n  ";
								  }
							  }
							  else if(SerLen1>SerLen2)
							  {
								  for(int i=0; i<SerLen3-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }							  
							  }
							  else if(SerLen2>SerLen1)
							  {
								  for(int i=0; i<SerLen3-SerLen2; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }							  
							  }						  
						  }
						  else
						  {
							  if(SerLen2==SerLen1)
							  {
								  for(int i=0; i<SerLen2-SerLen3; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }
								  GrLength=SerLen2;
							  }
							  else if(SerLen1>SerLen2)
							  {
								  for(int i=0; i<SerLen1-SerLen3; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }	
								  GrLength=SerLen1;
							  }
							  else if(SerLen2>SerLen1)
							  {
								  for(int i=0; i<SerLen3-SerLen3; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }
								  GrLength=SerLen2;
							  }						  
						  }

							  for(int i=0; i<GrLength; i++)
							  {

								  search1 = getBG1.split("<space>");
								  search = getBG.split("<space>");
								  search2 = getBG2.split("<space>");
								Tr contentRow1 = factory.createTr();
								
								addTableCell(factory, wordMLPackage, contentRow1, String.valueOf(NumVal), garamondRPr, JcEnumeration.CENTER,
										hasBgColor, backgroundColor);
								addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_short_name(), garamondRPr,
										JcEnumeration.LEFT, hasBgColor, backgroundColor);
								addTableCell(factory, wordMLPackage, contentRow1, cObj.getContractor_name(), garamondRPr,
										JcEnumeration.LEFT, hasBgColor, backgroundColor);
								
								addTableCell(factory, wordMLPackage, contentRow1, search[i], garamondRPr, JcEnumeration.LEFT,
										hasBgColor, backgroundColor);

									addTableCell(factory, wordMLPackage, contentRow1, search1[i], garamondRPr, JcEnumeration.RIGHT,
										hasBgColor, backgroundColor);
								
								addTableCell(factory, wordMLPackage, contentRow1, search2[i], garamondRPr,
										JcEnumeration.CENTER, hasBgColor, backgroundColor);	
								addTableCell(factory, wordMLPackage, contentRow1, cObj.getContractAlertRemarks(), garamondRPr,
										JcEnumeration.CENTER, hasBgColor, backgroundColor);						
								
								table.getContent().add(contentRow1);
						  }	
						  Total=Total+GrLength;
						  for(int i=0; i<GrLength; i++)
						  {
							  
								
								

								mergeCellsVertically(table, 0,Concat, Total);
								mergeCellsVertically(table, 1, Concat, Total);
								mergeCellsVertically(table, 2, Concat, Total);
								//mergeCellsVertically(table, 6, Concat,Total);
								
						  }
							Concat=Concat+GrLength;					  
						  
						  
					}
					NumVal=NumVal+1;

					
				}
				
				if (StringUtils.isEmpty(hodEntry.getValue()) || hodEntry.getValue().isEmpty()) {
					boolean hasBgColor = false;
					String backgroundColor = null;
					Tr contentRow = factory.createTr();

					List<String> noDataRow = new ArrayList<String>();
					noDataRow.add("Nil");
					for (int i = 0; i < 6; i++) {
						noDataRow.add("");
					}

					for (String headerValue : noDataRow) {
						addTableCellAndWidth(factory, wordMLPackage, contentRow, headerValue, garamondRPr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor,0);
					}
					table.getContent().add(contentRow);
					mergeCellsHorizontal(table, 1, 0, 5);
				}

				
				mp.addObject(table);
			}
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public static void createTableForContractInsuranceReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, Map<String,List<Contract>> list,String report_created_date) throws Exception {
		try {
			RPr titleRpr = getRPr(factory, "Calibri", "000000", "18", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Calibri", "000000", "14", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false,false);
			
			RPr calibriBoldRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA,
					true, false, false, false);
			RPr calibriBoldDateRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);
			
			int temp = 1;
			for (Map.Entry<String,List<Contract>> hodEntry : list.entrySet()) {
				//addParagraph(mp, factory);
				//addHeading(wordMLPackage, mp, factory,JcEnumeration.LEFT,calibriBoldRPr,hodEntry.getKey());
				
				Tbl tableHead = factory.createTbl();
				setTableAlign(factory, tableHead, JcEnumeration.CENTER);
				//addBorders(tableHead, "0");
				
				/**************************************************************************/
				Tr hodRow = factory.createTr();
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, hodEntry.getKey(), calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldRPr, JcEnumeration.LEFT, true,"ffffff");
				String date = "";
				if(temp == 1) {
					date = report_created_date;
				}else {
					addParagraph(mp, factory);
				}
				temp++;
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, date, calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				addTableCellWithTopBottomSpace(factory, wordMLPackage, hodRow, "", calibriBoldDateRPr, JcEnumeration.RIGHT, true,"ffffff");
				
				tableHead.getContent().add(hodRow);
				mergeCellsHorizontal(tableHead, 0, 0, 3);
				mergeCellsHorizontal(tableHead, 0, 4, 6);
				mp.addObject(tableHead);
				/***************************************************************/
				
				Tbl table = factory.createTbl();
				addBorders(table, "2");
	
				Tr titleRow = factory.createTr();
				List<String> tableHeader = new ArrayList<String>();
				tableHeader.add("SN");
				tableHeader.add("Short name of Contract");
				tableHeader.add("Agency");
				tableHeader.add("Insurance No");
				tableHeader.add("Rs/- \n(in lacs)");
				tableHeader.add("Valid Upto");
				tableHeader.add("Action Taken");
				
				int columnNo = 1;
				for (String headerValue : tableHeader) {
					int width = 0;
					if(1 == columnNo) {
						width = 230;
					}else if(2 == columnNo) {
						width = 900;
					}else if(3 == columnNo) {
						width = 900;
					}else if(4 == columnNo) {
						width = 1100;
					}else if(5 == columnNo) {
						width = 800;
					}else if(6 == columnNo) {
						width = 630;
					}
					else if(7 == columnNo) {
						width = 750;
					}					
					columnNo++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table.getContent().add(titleRow);
	
				int sNo = 1;
				int NumVal=1;
				int Total=0;
				int Concat=NumVal;


				for (Contract cObj : hodEntry.getValue()) 
				{
					boolean hasBgColor = false;
					String backgroundColor = null;
					String getBG="";
					String getBG1="";
					String getBG2="";
					
					if (StringUtils.isEmpty(cObj.getInsurance_number()) || cObj.getInsurance_number().isEmpty()) 
					{
						getBG="";
					}
					else
					{
						getBG=cObj.getInsurance_number().toString();
					}					
					

					if (StringUtils.isEmpty(cObj.getInsurance_value()) || cObj.getInsurance_value().isEmpty()) 
					{
						getBG1="";
					}
					else
					{
						getBG1=cObj.getInsurance_value().toString();
					}
					
					
					if (StringUtils.isEmpty(cObj.getInsurance_valid_upto()) || cObj.getInsurance_valid_upto().isEmpty()) 
					{
						getBG2="";
					}
					else
					{
						getBG2=cObj.getInsurance_valid_upto().toString();
					}

					if(getBG.indexOf("<space>")==-1 && getBG1.indexOf("<space>")==-1 && getBG2.indexOf("<space>")==-1)
					{
						Tr contentRow = factory.createTr();
		
						addTableCell(factory, wordMLPackage, contentRow, String.valueOf(NumVal), garamondRPr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor);
						addTableCell(factory, wordMLPackage, contentRow, cObj.getContract_short_name(), garamondRPr,
								JcEnumeration.LEFT, hasBgColor, backgroundColor);
						addTableCell(factory, wordMLPackage, contentRow, cObj.getContractor_name(), garamondRPr,
								JcEnumeration.LEFT, hasBgColor, backgroundColor);
						addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_number(), garamondRPr,
								JcEnumeration.LEFT, hasBgColor, backgroundColor);
						addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_value(), garamondRPr,
								JcEnumeration.RIGHT, hasBgColor, backgroundColor);
						addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_valid_upto(), garamondRPr,
								JcEnumeration.CENTER, hasBgColor, backgroundColor);
						addTableCell(factory, wordMLPackage, contentRow,cObj.getContractAlertRemarks(), garamondRPr,
								JcEnumeration.CENTER, hasBgColor, backgroundColor);					
		
						table.getContent().add(contentRow);
						Concat=Concat+1;
						Total=Total+1;
						
					}
					else
					{
						String[] search = getBG.split("<space>");
						String[] search1 = getBG1.split("<space>");
						String[] search2 = getBG2.split("<space>");

						int SerLen1 = search.length;
						int SerLen2 = search1.length;
						int SerLen3 = search2.length;

						int GrLength = 0;
						  
						  if(SerLen1>SerLen2 && SerLen1>SerLen3)
						  {
							  GrLength=SerLen1;
							  if(SerLen2==SerLen3)
							  {
								  for(int i=0; i<SerLen1-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
									  getBG2=getBG2+"<space>\n  ";
								  }
							  }
							  else if(SerLen2>SerLen3)
							  {
								  for(int i=0; i<SerLen1-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
								  }							  
							  }
							  else if(SerLen3>SerLen2)
							  {
								  for(int i=0; i<SerLen1-SerLen3; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }							  
							  }						  
							  
						  }
						  else
						  {
							  if(SerLen2==SerLen3)
							  {
								  for(int i=0; i<SerLen2-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }
								  GrLength=SerLen2;
							  }
							  else if(SerLen2>SerLen3)
							  {
								  for(int i=0; i<SerLen2-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }	
								  GrLength=SerLen2;
							  }
							  else if(SerLen3>SerLen2)
							  {
								  for(int i=0; i<SerLen3-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }
								  GrLength=SerLen3;
							  }	
							  
						  }					  
						  
						  if(SerLen2>SerLen3 && SerLen2>SerLen1)
						  {
							  GrLength=SerLen2;
							  if(SerLen3==SerLen1)
							  {
								  for(int i=0; i<SerLen2-SerLen3; i++)
								  {
									  getBG=getBG+"<space>\n  ";
									  getBG2=getBG2+"<space>\n  ";
								  }
							  }
							  else if(SerLen1>SerLen3)
							  {
								  for(int i=0; i<SerLen2-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }							  
							  }
							  else if(SerLen3>SerLen1)
							  {
								  for(int i=0; i<SerLen2-SerLen3; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }							  
							  }						  
						  }	
						  else
						  {
							  if(SerLen3==SerLen1)
							  {
								  for(int i=0; i<SerLen3-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
								  }
								  GrLength=SerLen3;
							  }
							  else if(SerLen1>SerLen3)
							  {
								  for(int i=0; i<SerLen1-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
								  }
								  GrLength=SerLen1;
							  }
							  else if(SerLen3>SerLen1)
							  {
								  for(int i=0; i<SerLen3-SerLen2; i++)
								  {
									  getBG1=getBG1+"<space>\n  ";
								  }	
								  GrLength=SerLen3;
							  }						  
						  }					  
						  
						  
						  if(SerLen3>SerLen1 && SerLen3>SerLen2)
						  {
							  GrLength=SerLen3;
							  if(SerLen2==SerLen1)
							  {
								  for(int i=0; i<SerLen3-SerLen2; i++)
								  {
									  getBG=getBG+"<space>\n  ";
									  getBG1=getBG1+"<space>\n  ";
								  }
							  }
							  else if(SerLen1>SerLen2)
							  {
								  for(int i=0; i<SerLen3-SerLen1; i++)
								  {
									  getBG=getBG+"<space>\n  ";
								  }							  
							  }
							  else if(SerLen2>SerLen1)
							  {
								  for(int i=0; i<SerLen3-SerLen2; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }							  
							  }						  
						  }
						  else
						  {
							  if(SerLen2==SerLen1)
							  {
								  for(int i=0; i<SerLen2-SerLen3; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }
								  GrLength=SerLen2;
							  }
							  else if(SerLen1>SerLen2)
							  {
								  for(int i=0; i<SerLen1-SerLen3; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }	
								  GrLength=SerLen1;
							  }
							  else if(SerLen2>SerLen1)
							  {
								  for(int i=0; i<SerLen3-SerLen3; i++)
								  {
									  getBG2=getBG2+"<space>\n  ";
								  }
								  GrLength=SerLen2;
							  }						  
						  }

							  for(int i=0; i<GrLength; i++)
							  {

								  search1 = getBG1.split("<space>");
								  search = getBG.split("<space>");
								  search2 = getBG2.split("<space>");
								  
								  
								  Tr contentRow1 = factory.createTr();
								  
									addTableCell(factory, wordMLPackage, contentRow1, String.valueOf(NumVal), garamondRPr, JcEnumeration.CENTER,
											hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getContract_short_name(), garamondRPr,
											JcEnumeration.LEFT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, cObj.getContractor_name(), garamondRPr,
											JcEnumeration.LEFT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, search[i], garamondRPr,
											JcEnumeration.LEFT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, search1[i], garamondRPr,
											JcEnumeration.RIGHT, hasBgColor, backgroundColor);
									addTableCell(factory, wordMLPackage, contentRow1, search2[i], garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);								
									addTableCell(factory, wordMLPackage, contentRow1,cObj.getContractAlertRemarks(), garamondRPr,
											JcEnumeration.CENTER, hasBgColor, backgroundColor);								  
								  
								  
									table.getContent().add(contentRow1);


							  }	
							  Total=Total+GrLength;
							  for(int i=0; i<GrLength; i++)
							  {
								  
									
									

									mergeCellsVertically(table, 0,Concat, Total);
									mergeCellsVertically(table, 1, Concat, Total);
									mergeCellsVertically(table, 2, Concat, Total);
									//mergeCellsVertically(table, 6, Concat,Total);
									
							  }
							  Concat=Concat+GrLength;										  

							  
								


							  
						}	
						NumVal=NumVal+1;
				}
				if (StringUtils.isEmpty(hodEntry.getValue()) || hodEntry.getValue().isEmpty()) {
					boolean hasBgColor = false;
					String backgroundColor = null;
					Tr contentRow = factory.createTr();
	
					List<String> noDataRow = new ArrayList<String>();
					noDataRow.add("Nil");
					for (int i = 0; i < 6; i++) {
						noDataRow.add("");
					}
	
					for (String headerValue : noDataRow) {
						addTableCellAndWidth(factory, wordMLPackage, contentRow, headerValue, titleRpr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor,0);
					}
					table.getContent().add(contentRow);
					mergeCellsHorizontal(table, 1, 0, 5);
				}
	
				setTableAlign(factory, table, JcEnumeration.CENTER);
				mp.addObject(table);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public static void createTableForContractDetailsReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, Contract contractDetails, Contract progressDetailsAsOnDate,
			List<Contract> milestoneDetails, List<Contract> bgDetails, List<Contract> insuranceDetails,
			Contract contractClosureDetails, Contract contractorDetails, List<Contract> keyPersonnels) throws Exception {
		try {
			RPr titleRpr = getRPr(factory, "Calibri", "000000", "18", STHint.EAST_ASIA, true, false, false,
					false);
			RPr contentRpr = getRPr(factory, "Calibri", "000000", "18", STHint.EAST_ASIA, false, false, false,
					false);
			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);
			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA, true, false, false,
					false);

			/********************* Contract Details Starts *******************************************************/
			Tbl contractDetailsTable = factory.createTbl();
			addBorders(contractDetailsTable, "2");

			//addHeading(wordMLPackage, mp, factory, JcEnumeration.RIGHT, titleRpr, "Annexure-I");

			boolean hasBgColor3 = false;
			String backgroundColor3 = null;

			/************************/
			Tr contractContentRow0 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow0, "Project", titleRpr, JcEnumeration.LEFT, true,
					"ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow0, contractDetails.getProject_name(), titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			for (int i = 0; i < 4; i++) {
				addTableCell(factory, wordMLPackage, contractContentRow0, "", contentRpr, JcEnumeration.LEFT, true,
						"ecf2ff");
			}
			/************************/
			Tr contractContentRow1 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow1, "Work", titleRpr, JcEnumeration.LEFT, true,
					"ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow1, contractDetails.getWork_name(), titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			for (int i = 0; i < 4; i++) {
				addTableCell(factory, wordMLPackage, contractContentRow1, "", contentRpr, JcEnumeration.LEFT, true,
						"ecf2ff");
			}
			/************************/
			Tr contractContentRow2 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow2, "Contract ID", titleRpr, JcEnumeration.LEFT, true,
					"ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow2, contractDetails.getContract_id(), titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			for (int i = 0; i < 4; i++) {
				addTableCell(factory, wordMLPackage, contractContentRow2, "", contentRpr, JcEnumeration.LEFT, true,
						"ecf2ff");
			}
			/************************/
			Tr contractContentRow3 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow3, "Contract Name", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow3, contractDetails.getContract_name(), titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			for (int i = 0; i < 4; i++) {
				addTableCell(factory, wordMLPackage, contractContentRow3, "", contentRpr, JcEnumeration.LEFT, true,
						"ecf2ff");
			}
			/************************/
			Tr contractContentRow4 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow4, "Contractor Name", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow4, contractDetails.getContractor_name(), titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			for (int i = 0; i < 4; i++) {
				addTableCell(factory, wordMLPackage, contractContentRow4, "", contentRpr, JcEnumeration.LEFT, true,
						"ecf2ff");
			}
			/************************/
			/*Tr contractContentRow5 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow5, "Status", titleRpr, JcEnumeration.LEFT, true,
					"ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow5, contractDetails.getContract_status_fk(), titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			for (int i = 0; i < 4; i++) {
				addTableCell(factory, wordMLPackage, contractContentRow5, "", contentRpr, JcEnumeration.LEFT, true,
						"ecf2ff");
			}*/
			/************************/
			contractDetailsTable.getContent().add(contractContentRow0);
			contractDetailsTable.getContent().add(contractContentRow1);
			contractDetailsTable.getContent().add(contractContentRow2);
			contractDetailsTable.getContent().add(contractContentRow3);
			contractDetailsTable.getContent().add(contractContentRow4);
			//contractDetailsTable.getContent().add(contractContentRow5);

			mergeCellsHorizontal(contractDetailsTable, 0, 1, 6);
			mergeCellsHorizontal(contractDetailsTable, 1, 1, 6);
			mergeCellsHorizontal(contractDetailsTable, 2, 1, 6);
			mergeCellsHorizontal(contractDetailsTable, 3, 1, 6);
			mergeCellsHorizontal(contractDetailsTable, 4, 1, 6);
			//mergeCellsHorizontal(contractDetailsTable, 5, 1, 6);

			boolean contracthasBgColor = false;
			String contractBackgroundColor = null;
			Tr contractContentRow6 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow6, "HOD:", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow6, contractDetails.getHod_designation(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow6, "Dy HOD:", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow6, contractDetails.getDy_hod_designation(),
					contentRpr, JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow6, "Department:", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow6, contractDetails.getDepartment_name(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			contractDetailsTable.getContent().add(contractContentRow6);

			Tr contractContentRow7 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow7, "LOA Letter No:", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow7, contractDetails.getLoa_letter_number(),
					contentRpr, JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow7, "", titleRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow7, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow7, "LOA Date:", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow7, contractDetails.getLoa_date(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			contractDetailsTable.getContent().add(contractContentRow7);
			mergeCellsHorizontal(contractDetailsTable, 6, 1, 3);

			Tr contractContentRow8 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow8, "CA No:", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow8, contractDetails.getCa_no(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow8, "", titleRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow8, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow8, "CA Date:", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow8, contractDetails.getCa_date(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			contractDetailsTable.getContent().add(contractContentRow8);
			mergeCellsHorizontal(contractDetailsTable, 7, 1, 3);

			Tr contractContentRow9 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow9, "Contract Type", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow9, contractDetails.getContract_type_fk(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow9, "", titleRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow9, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow9, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow9, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			contractDetailsTable.getContent().add(contractContentRow9);
			mergeCellsHorizontal(contractDetailsTable, 8, 1, 5);

			Tr contractContentRow10 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow10, "Scope of Contract", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow10, contractDetails.getScope_of_contract(),
					contentRpr, JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow10, "", titleRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow10, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow10, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow10, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			contractDetailsTable.getContent().add(contractContentRow10);
			mergeCellsHorizontal(contractDetailsTable, 9, 1, 5);

			Tr contractContentRow11 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow11, "Awarded Cost (Rs)", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow11, contractDetails.getAwarded_cost(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow11, "Date of Completion", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow11, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow11, contractDetails.getDoc(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow11, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			contractDetailsTable.getContent().add(contractContentRow11);
			mergeCellsHorizontal(contractDetailsTable, 10, 2, 3);
			mergeCellsHorizontal(contractDetailsTable, 10, 4, 5);

			Tr contractContentRow12 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow12, "Date of Start", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow12, contractDetails.getDate_of_start(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow12, "Target Date of Completion", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow12, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow12, contractDetails.getTarget_doc(), contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow12, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			contractDetailsTable.getContent().add(contractContentRow12);
			mergeCellsHorizontal(contractDetailsTable, 11, 2, 3);
			mergeCellsHorizontal(contractDetailsTable, 11, 4, 5);
			
			Tr contractContentRow13 = factory.createTr();
			addTableCell(factory, wordMLPackage, contractContentRow13, "Contract Status", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow13, contractDetails.getStatus(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow13, "Work Status", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractContentRow13, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow13, contractDetails.getContract_status_fk(), contentRpr,
					JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
			addTableCell(factory, wordMLPackage, contractContentRow13, "", contentRpr, JcEnumeration.LEFT,
					contracthasBgColor, contractBackgroundColor);
			contractDetailsTable.getContent().add(contractContentRow13);
			mergeCellsHorizontal(contractDetailsTable, 12, 2, 3);
			mergeCellsHorizontal(contractDetailsTable, 12, 4, 5);
			
			
			
			setTableAlign(factory, contractDetailsTable, JcEnumeration.CENTER);
			mp.addObject(contractDetailsTable);
			
			
			
			/**************************************************************/
			if(!StringUtils.isEmpty(contractDetails.getRevision_requried()) && contractDetails.getRevision_requried().equals("Yes")) {
				Tbl revisionDetailsTable = factory.createTbl();
				addBorders(revisionDetailsTable, "2");
	
				addParagraph(mp, factory);
				addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, fontRPr, "Revision Details");
				
				Tr revisionContentRow0 = factory.createTr();
				addTableCell(factory, wordMLPackage, revisionContentRow0, "Revision Number", titleRpr, JcEnumeration.LEFT,
						true, "ecf2ff");
				addTableCell(factory, wordMLPackage, revisionContentRow0, "Revised Amount (Rs)", titleRpr, JcEnumeration.LEFT,
						true, "ecf2ff");
				addTableCell(factory, wordMLPackage, revisionContentRow0, "Revised DOC", titleRpr, JcEnumeration.LEFT,
						true, "ecf2ff");
				addTableCell(factory, wordMLPackage, revisionContentRow0, "Remark", titleRpr, JcEnumeration.LEFT,
						true, "ecf2ff");
				revisionDetailsTable.getContent().add(revisionContentRow0);
				for (Contract revision : contractDetails.getContract_revision()) {
					Tr contractRevisionContentRow = factory.createTr();
					addTableCell(factory, wordMLPackage, contractRevisionContentRow, revision.getRevision_number(),
							titleRpr, JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
					addTableCell(factory, wordMLPackage, contractRevisionContentRow, revision.getRevised_amount(),
							contentRpr, JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
					addTableCell(factory, wordMLPackage, contractRevisionContentRow, revision.getRevised_doc(), titleRpr,
							JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
					addTableCell(factory, wordMLPackage, contractRevisionContentRow, revision.getRemarks(), contentRpr,
							JcEnumeration.LEFT, contracthasBgColor, contractBackgroundColor);
					revisionDetailsTable.getContent().add(contractRevisionContentRow);
				}
				
				if (StringUtils.isEmpty(contractDetails.getContract_revision()) || contractDetails.getContract_revision().isEmpty()) {
					boolean hasBgColor = false;
					String backgroundColor = null;
					Tr contentRow = factory.createTr();
	
					List<String> noDataRow = new ArrayList<String>();
					noDataRow.add("NO REVISION DATA");
					for (int i = 0; i < 3; i++) {
						noDataRow.add("");
					}
	
					for (String headerValue : noDataRow) {
						addTableCell(factory, wordMLPackage, contentRow, headerValue, titleRpr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor);
					}
					revisionDetailsTable.getContent().add(contentRow);
					mergeCellsHorizontal(revisionDetailsTable, 1, 0, 3);
				}
	
				setTableAlign(factory, revisionDetailsTable, JcEnumeration.CENTER);
				mp.addObject(revisionDetailsTable);
			}
			/********************* Contract Details ends *******************************************************/

			/********************* Progress Details as on date Details Starts *******************************************************/
			Tbl progressDetailsTable = factory.createTbl();
			addBorders(progressDetailsTable, "2");

			addParagraph(mp, factory);
			addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, fontRPr, "Progress Details as on date");

			boolean hasBgColor0 = false;
			String backgroundColor0 = null;
			Tr progressContentRow0 = factory.createTr();

			addTableCell(factory, wordMLPackage, progressContentRow0, "Actual Date of Start", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, progressContentRow0, "", contentRpr, JcEnumeration.LEFT, true,
					"ecf2ff");
			addTableCell(factory, wordMLPackage, progressContentRow0, progressDetailsAsOnDate.getDate_of_start(),
					contentRpr, JcEnumeration.LEFT, hasBgColor0, backgroundColor0);

			addTableCell(factory, wordMLPackage, progressContentRow0, "Expected date of completion", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, progressContentRow0, "", contentRpr, JcEnumeration.LEFT, true,
					"ecf2ff");
			addTableCell(factory, wordMLPackage, progressContentRow0, progressDetailsAsOnDate.getDoc(), contentRpr,
					JcEnumeration.LEFT, hasBgColor0, backgroundColor0);

			progressDetailsTable.getContent().add(progressContentRow0);

			mergeCellsHorizontal(progressDetailsTable, 0, 0, 1);
			mergeCellsHorizontal(progressDetailsTable, 0, 3, 4);

			Tr progressDetailsTitleRow = factory.createTr();
			List<String> progressDetailsTableHeader = new ArrayList<String>();
			progressDetailsTableHeader.add("Actual Physical progress");
			progressDetailsTableHeader.add("");
			progressDetailsTableHeader.add("Actual Financial Progress ");
			progressDetailsTableHeader.add("");
			progressDetailsTableHeader.add("Payment made (Rs)");
			progressDetailsTableHeader.add("");

			for (String headerValue : progressDetailsTableHeader) {
				addTableCell(factory, wordMLPackage, progressDetailsTitleRow, headerValue, titleRpr, JcEnumeration.LEFT,
						true, "ecf2ff");
			}
			progressDetailsTable.getContent().add(progressDetailsTitleRow);
			mergeCellsHorizontal(progressDetailsTable, 1, 0, 1);
			mergeCellsHorizontal(progressDetailsTable, 1, 2, 3);
			mergeCellsHorizontal(progressDetailsTable, 1, 4, 5);

			boolean hasBgColor1 = false;
			String backgroundColor1 = null;
			Tr progressContentRow1 = factory.createTr();

			addTableCell(factory, wordMLPackage, progressContentRow1,
					progressDetailsAsOnDate.getActual_physical_progress(), contentRpr, JcEnumeration.LEFT, hasBgColor1,
					backgroundColor1);
			addTableCell(factory, wordMLPackage, progressContentRow1, "", contentRpr, JcEnumeration.LEFT, hasBgColor1,
					backgroundColor1);
			addTableCell(factory, wordMLPackage, progressContentRow1,
					progressDetailsAsOnDate.getActual_financial_progress(), contentRpr, JcEnumeration.LEFT, hasBgColor1,
					backgroundColor1);
			addTableCell(factory, wordMLPackage, progressContentRow1, "", contentRpr, JcEnumeration.LEFT, hasBgColor1,
					backgroundColor1);
			addTableCell(factory, wordMLPackage, progressContentRow1, progressDetailsAsOnDate.getPayment_made(),
					contentRpr, JcEnumeration.LEFT, hasBgColor1, backgroundColor1);
			addTableCell(factory, wordMLPackage, progressContentRow1, "", contentRpr, JcEnumeration.LEFT, hasBgColor1,
					backgroundColor1);

			progressDetailsTable.getContent().add(progressContentRow1);
			

			mergeCellsHorizontal(progressDetailsTable, 2, 0, 1);
			mergeCellsHorizontal(progressDetailsTable, 2, 2, 3);
			mergeCellsHorizontal(progressDetailsTable, 2, 4, 5);

			Tr remarksRow = factory.createTr();
			
			addTableCell(factory, wordMLPackage, remarksRow,
					"Remarks:", boldRPr, JcEnumeration.LEFT, hasBgColor1,
					backgroundColor1);
			addTableCell(factory, wordMLPackage, remarksRow, contractDetails.getRemarks(), contentRpr, JcEnumeration.LEFT, hasBgColor1,
					backgroundColor1);
			addTableCell(factory, wordMLPackage, remarksRow,
					"", contentRpr, JcEnumeration.LEFT, hasBgColor1,
					backgroundColor1);
			addTableCell(factory, wordMLPackage, remarksRow, "", contentRpr, JcEnumeration.LEFT, hasBgColor1,
					backgroundColor1);
			addTableCell(factory, wordMLPackage, remarksRow,"",
					contentRpr, JcEnumeration.LEFT, hasBgColor1, backgroundColor1);
			addTableCell(factory, wordMLPackage, remarksRow, "", contentRpr, JcEnumeration.LEFT, hasBgColor1,
					backgroundColor1);	
			progressDetailsTable.getContent().add(remarksRow);
			
			mergeCellsHorizontal(progressDetailsTable, 3, 1, 5);

			setTableAlign(factory, progressDetailsTable, JcEnumeration.CENTER);
			mp.addObject(progressDetailsTable);
			

			/********************* Progress Details as on date Details ends *******************************************************/
			

			/********************* Milestone Details Starts *******************************************************/
			if(!StringUtils.isEmpty(contractDetails.getMilestone_requried()) && contractDetails.getMilestone_requried().equals("Yes")) {
				Tbl milestoneTable = factory.createTbl();
				addBorders(milestoneTable, "2");
	
				addParagraph(mp, factory);
				//addPageBreak(mp);
				//addHeading(wordMLPackage, mp, factory, JcEnumeration.RIGHT, titleRpr, "Annexure-II");
				addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, fontRPr, "Milestone Details");
	
				Tr milestoneTitleRow = factory.createTr();
				List<String> milestoneTableHeader = new ArrayList<String>();
				milestoneTableHeader.add("Mile stone ID");
				milestoneTableHeader.add("Milestone Name");
				milestoneTableHeader.add("Milestone Date");
				milestoneTableHeader.add("Actual Date");
				milestoneTableHeader.add("Remarks");
	
				for (String headerValue : milestoneTableHeader) {
					addTableCell(factory, wordMLPackage, milestoneTitleRow, headerValue, titleRpr, JcEnumeration.LEFT, true,
							"ecf2ff");
				}
				milestoneTable.getContent().add(milestoneTitleRow);
	
				for (Contract cObj : milestoneDetails) {
					boolean hasBgColor = false;
					String backgroundColor = null;
					Tr contentRow = factory.createTr();
	
					addTableCell(factory, wordMLPackage, contentRow, cObj.getMilestone_id(), contentRpr, JcEnumeration.LEFT,
							hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getMilestone_name(), contentRpr,
							JcEnumeration.LEFT, hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getMilestone_date(), contentRpr,
							JcEnumeration.LEFT, hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getActual_date(), contentRpr, JcEnumeration.LEFT,
							hasBgColor, backgroundColor);
					addTableCell(factory, wordMLPackage, contentRow, cObj.getRemarks(), contentRpr, JcEnumeration.LEFT,
							hasBgColor, backgroundColor);
	
					milestoneTable.getContent().add(contentRow);
				}
				if (StringUtils.isEmpty(milestoneDetails) || milestoneDetails.isEmpty()) {
					boolean hasBgColor = false;
					String backgroundColor = null;
					Tr contentRow = factory.createTr();
	
					List<String> noDataRow = new ArrayList<String>();
					noDataRow.add("NO MILESTONE DATA");
					for (int i = 0; i < 4; i++) {
						noDataRow.add("");
					}
	
					for (String headerValue : noDataRow) {
						addTableCell(factory, wordMLPackage, contentRow, headerValue, titleRpr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor);
					}
					milestoneTable.getContent().add(contentRow);
					mergeCellsHorizontal(milestoneTable, 1, 0, 4);
				}
	
				setTableAlign(factory, milestoneTable, JcEnumeration.CENTER);
				mp.addObject(milestoneTable);
			}
			/********************* Milestone Details ends *******************************************************/

			/********************* BG Details Starts *******************************************************/
			Tbl bgTable = factory.createTbl();
			addBorders(bgTable, "2");

			addParagraph(mp, factory);
			//addPageBreak(mp);
			//addHeading(wordMLPackage, mp, factory, JcEnumeration.RIGHT, titleRpr, "Annexure-III");
			addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, fontRPr, "Bank Guarantee Details");

			Tr bgTitleRow = factory.createTr();
			List<String> bgTableHeader = new ArrayList<String>();
			bgTableHeader.add("BG Type");
			bgTableHeader.add("Issuing Bank");
			bgTableHeader.add("BG/FDR Number");
			bgTableHeader.add("Amount (Rs)");
			bgTableHeader.add("BG/FDR Date");
			bgTableHeader.add("Expiry Date");
			bgTableHeader.add("Release Date");

			for (String headerValue : bgTableHeader) {
				addTableCell(factory, wordMLPackage, bgTitleRow, headerValue, titleRpr, JcEnumeration.LEFT, true,
						"ecf2ff");
			}
			bgTable.getContent().add(bgTitleRow);

			for (Contract cObj : bgDetails) {
				boolean hasBgColor = false;
				String backgroundColor = null;
				Tr contentRow = factory.createTr();

				addTableCell(factory, wordMLPackage, contentRow, cObj.getBg_type_fk(), contentRpr, JcEnumeration.LEFT,
						hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getIssuing_bank(), contentRpr, JcEnumeration.LEFT,
						hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getBg_number(), contentRpr, JcEnumeration.LEFT,
						hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getBg_value(), contentRpr, JcEnumeration.LEFT,
						hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getBg_date(), contentRpr, JcEnumeration.LEFT,
						hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getBg_valid_upto(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getRelease_date(), contentRpr, JcEnumeration.LEFT,
						hasBgColor, backgroundColor);

				bgTable.getContent().add(contentRow);
			}
			if (StringUtils.isEmpty(bgDetails) || bgDetails.isEmpty()) {
				boolean hasBgColor = false;
				String backgroundColor = null;
				Tr contentRow = factory.createTr();

				List<String> noDataRow = new ArrayList<String>();
				noDataRow.add("NO BG DATA");
				for (int i = 0; i < 6; i++) {
					noDataRow.add("");
				}

				for (String headerValue : noDataRow) {
					addTableCell(factory, wordMLPackage, contentRow, headerValue, titleRpr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
				}
				bgTable.getContent().add(contentRow);
				mergeCellsHorizontal(bgTable, 1, 0, 6);
			}

			setTableAlign(factory, bgTable, JcEnumeration.CENTER);
			mp.addObject(bgTable);

			/********************* BG Details ends *******************************************************/

			/********************* Insurance Details Starts *******************************************************/
			Tbl insuranceTable = factory.createTbl();
			addBorders(insuranceTable, "2");

			addParagraph(mp, factory);
			addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, fontRPr, "Insurance Details");

			Tr insurancetitleRow = factory.createTr();
			List<String> tableHeader = new ArrayList<String>();
			tableHeader.add("Insurance Type");
			tableHeader.add("Issuing Agency");
			tableHeader.add("Insurance Number");
			tableHeader.add("Insurance Value (Rs)");
			tableHeader.add("Valid Upto");
			tableHeader.add("Release");

			for (String headerValue : tableHeader) {
				addTableCell(factory, wordMLPackage, insurancetitleRow, headerValue, titleRpr, JcEnumeration.LEFT, true,
						"ecf2ff");
			}
			insuranceTable.getContent().add(insurancetitleRow);

			for (Contract cObj : insuranceDetails) {
				boolean hasBgColor = false;
				String backgroundColor = null;
				Tr contentRow = factory.createTr();

				addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_type_fk(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getIssuing_agency(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_number(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_value(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_valid_upto(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getInsurance_status(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);

				insuranceTable.getContent().add(contentRow);
			}
			if (StringUtils.isEmpty(insuranceDetails) || insuranceDetails.isEmpty()) {
				boolean hasBgColor = false;
				String backgroundColor = null;
				Tr contentRow = factory.createTr();

				List<String> noDataRow = new ArrayList<String>();
				noDataRow.add("NO INSURANCE DATA");
				for (int i = 0; i < 5; i++) {
					noDataRow.add("");
				}

				for (String headerValue : noDataRow) {
					addTableCell(factory, wordMLPackage, contentRow, headerValue, titleRpr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
				}
				insuranceTable.getContent().add(contentRow);
				mergeCellsHorizontal(insuranceTable, 1, 0, 5);
			}

			setTableAlign(factory, insuranceTable, JcEnumeration.CENTER);
			mp.addObject(insuranceTable);

			/********************* Insurance Details ends *******************************************************/

			/********************* Contract Closure Details Starts *******************************************************/
			Tbl cloureDetailsTable = factory.createTbl();
			addBorders(cloureDetailsTable, "2");

			addParagraph(mp, factory);
			//addPageBreak(mp);
			//addHeading(wordMLPackage, mp, factory, JcEnumeration.RIGHT, titleRpr, "Annexure-IV");
			addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, fontRPr, "Contract Closure Details");

			boolean hasBgColor = false;
			String backgroundColor = null;
 
			/************************************************/
			Tr cloureContentRow0 = factory.createTr();

			addTableCell(factory, wordMLPackage, cloureContentRow0, "Actual Completion date", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, cloureContentRow0, contractClosureDetails.getActual_completion_date(),
					contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);

			addTableCell(factory, wordMLPackage, cloureContentRow0, "Actual Completion Cost (Rs)", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, cloureContentRow0, contractClosureDetails.getCompleted_cost(),
					contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);

			cloureDetailsTable.getContent().add(cloureContentRow0);
			/**********************************************************/
			Tr cloureContentRow1 = factory.createTr();

			addTableCell(factory, wordMLPackage, cloureContentRow1, "Final Taking over by Client", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, cloureContentRow1, "", titleRpr, JcEnumeration.LEFT, true, "ecf2ff");

			addTableCell(factory, wordMLPackage, cloureContentRow1, contractClosureDetails.getFinal_takeover(),
					contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);
			addTableCell(factory, wordMLPackage, cloureContentRow1, "", contentRpr, JcEnumeration.LEFT, hasBgColor,
					backgroundColor);

			cloureDetailsTable.getContent().add(cloureContentRow1);
			mergeCellsHorizontal(cloureDetailsTable, 1, 0, 1);
			mergeCellsHorizontal(cloureDetailsTable, 1, 2, 3);
			/**************************************************************/
			Tr cloureContentRow2 = factory.createTr();

			addTableCell(factory, wordMLPackage, cloureContentRow2, "Release of Completion Certificate", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, cloureContentRow2, "", titleRpr, JcEnumeration.LEFT, true, "ecf2ff");

			addTableCell(factory, wordMLPackage, cloureContentRow2,
					contractClosureDetails.getCompletion_certificate_release(), contentRpr, JcEnumeration.LEFT,
					hasBgColor, backgroundColor);
			addTableCell(factory, wordMLPackage, cloureContentRow2, "", contentRpr, JcEnumeration.LEFT, hasBgColor,
					backgroundColor);

			cloureDetailsTable.getContent().add(cloureContentRow2);
			mergeCellsHorizontal(cloureDetailsTable, 2, 0, 1);
			mergeCellsHorizontal(cloureDetailsTable, 2, 2, 3);
			/**************************************************************/
			Tr cloureContentRow3 = factory.createTr();

			addTableCell(factory, wordMLPackage, cloureContentRow3, "Release of Final bill", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, cloureContentRow3, "", titleRpr, JcEnumeration.LEFT, true, "ecf2ff");

			addTableCell(factory, wordMLPackage, cloureContentRow3, contractClosureDetails.getFinal_bill_release(),
					contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);
			addTableCell(factory, wordMLPackage, cloureContentRow3, "", contentRpr, JcEnumeration.LEFT, hasBgColor,
					backgroundColor);

			cloureDetailsTable.getContent().add(cloureContentRow3);
			mergeCellsHorizontal(cloureDetailsTable, 3, 0, 1);
			mergeCellsHorizontal(cloureDetailsTable, 3, 2, 3);
			/**************************************************************/
			Tr cloureContentRow4 = factory.createTr();

			addTableCell(factory, wordMLPackage, cloureContentRow4, "Defect Liability Period completed on ", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, cloureContentRow4, "", titleRpr, JcEnumeration.LEFT, true, "ecf2ff");

			addTableCell(factory, wordMLPackage, cloureContentRow4, contractClosureDetails.getDefect_liability_period(),
					contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);
			addTableCell(factory, wordMLPackage, cloureContentRow4, "", contentRpr, JcEnumeration.LEFT, hasBgColor,
					backgroundColor);

			cloureDetailsTable.getContent().add(cloureContentRow4);
			mergeCellsHorizontal(cloureDetailsTable, 4, 0, 1);
			mergeCellsHorizontal(cloureDetailsTable, 4, 2, 3);
			/**************************************************************/
			Tr cloureContentRow5 = factory.createTr();

			addTableCell(factory, wordMLPackage, cloureContentRow5, "Release of Final Retention amount/BG", titleRpr,
					JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, cloureContentRow5, "", titleRpr, JcEnumeration.LEFT, true, "ecf2ff");

			addTableCell(factory, wordMLPackage, cloureContentRow5, contractClosureDetails.getRetention_money_release(),
					contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);
			addTableCell(factory, wordMLPackage, cloureContentRow5, "", contentRpr, JcEnumeration.LEFT, hasBgColor,
					backgroundColor);

			cloureDetailsTable.getContent().add(cloureContentRow5);
			mergeCellsHorizontal(cloureDetailsTable, 5, 0, 1);
			mergeCellsHorizontal(cloureDetailsTable, 5, 2, 3);
			/**************************************************************/
			Tr cloureContentRow6 = factory.createTr();

			addTableCell(factory, wordMLPackage, cloureContentRow6, "Release of PBG", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, cloureContentRow6, "", titleRpr, JcEnumeration.LEFT, true, "ecf2ff");

			addTableCell(factory, wordMLPackage, cloureContentRow6, contractClosureDetails.getPbg_release(), contentRpr,
					JcEnumeration.LEFT, hasBgColor, backgroundColor);
			addTableCell(factory, wordMLPackage, cloureContentRow6, "", contentRpr, JcEnumeration.LEFT, hasBgColor,
					backgroundColor);

			cloureDetailsTable.getContent().add(cloureContentRow6);
			mergeCellsHorizontal(cloureDetailsTable, 6, 0, 1);
			mergeCellsHorizontal(cloureDetailsTable, 6, 2, 3);
			/**************************************************************/
			Tr cloureContentRow7 = factory.createTr();

			addTableCell(factory, wordMLPackage, cloureContentRow7, "Contract Closure on", titleRpr, JcEnumeration.LEFT,
					true, "ecf2ff");
			addTableCell(factory, wordMLPackage, cloureContentRow7, "", titleRpr, JcEnumeration.LEFT, true, "ecf2ff");

			addTableCell(factory, wordMLPackage, cloureContentRow7, contractClosureDetails.getContract_closure_date(),
					contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);
			addTableCell(factory, wordMLPackage, cloureContentRow7, "", contentRpr, JcEnumeration.LEFT, hasBgColor,
					backgroundColor);

			cloureDetailsTable.getContent().add(cloureContentRow7);
			mergeCellsHorizontal(cloureDetailsTable, 7, 0, 1);
			mergeCellsHorizontal(cloureDetailsTable, 7, 2, 3);
			/**************************************************************/

			setTableAlign(factory, cloureDetailsTable, JcEnumeration.CENTER);
			mp.addObject(cloureDetailsTable);

			/********************* Contract Closure Details ends *******************************************************/

			/********************* Contractor Details Starts *******************************************************/
			Tbl contractorDetailsTable = factory.createTbl();
			addBorders(contractorDetailsTable, "2");

			addParagraph(mp, factory);
			//addPageBreak(mp);
			//addHeading(wordMLPackage, mp, factory, JcEnumeration.RIGHT, titleRpr, "Annexure-V");
			addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, fontRPr, "Contractor Details");

			/************************************************/
			Tr contractorRow0 = factory.createTr();

			addTableCell(factory, wordMLPackage, contractorRow0, "Company name", titleRpr, JcEnumeration.LEFT, true,
					"ecf2ff");
			addTableCell(factory, wordMLPackage, contractorRow0, contractorDetails.getContractor_name(), contentRpr,
					JcEnumeration.LEFT, hasBgColor, backgroundColor);

			contractorDetailsTable.getContent().add(contractorRow0);
			/**********************************************************/
			Tr contractorRow1 = factory.createTr();	
			
			addTableCell(factory, wordMLPackage, contractorRow1, "Specialization",
					titleRpr, JcEnumeration.LEFT, true, "ecf2ff");	
			addTableCell(factory, wordMLPackage, contractorRow1, contractorDetails.getContractor_specilization_fk(),
					contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);			
			
			contractorDetailsTable.getContent().add(contractorRow1);
			/**************************************************************/			
			Tr contractorRow2 = factory.createTr();	
			
			addTableCell(factory, wordMLPackage, contractorRow2, "Address",
			titleRpr, JcEnumeration.LEFT, true, "ecf2ff");	
			addTableCell(factory, wordMLPackage, contractorRow2, contractorDetails.getAddress(),
			contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);			
			
			contractorDetailsTable.getContent().add(contractorRow2);
			/**************************************************************/
			
			Tr contractorRow3 = factory.createTr();	
			
			addTableCell(factory, wordMLPackage, contractorRow3, "Primary Contact",
			titleRpr, JcEnumeration.LEFT, true, "ecf2ff");	
			addTableCell(factory, wordMLPackage, contractorRow3, contractorDetails.getPrimary_contact_name(),
			contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);			
			
			contractorDetailsTable.getContent().add(contractorRow3);
			/**************************************************************/
			
			Tr contractorRow4 = factory.createTr();	
			
			addTableCell(factory, wordMLPackage, contractorRow4, "Phone Number",
			titleRpr, JcEnumeration.LEFT, true, "ecf2ff");
			addTableCell(factory, wordMLPackage, contractorRow4, contractorDetails.getPhone_number(),
			contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);			
			
			contractorDetailsTable.getContent().add(contractorRow4);
			/**************************************************************/
			
			Tr contractorRow5 = factory.createTr();	
			
			addTableCell(factory, wordMLPackage, contractorRow5, "Email ID",
			titleRpr, JcEnumeration.LEFT, true, "ecf2ff");		
			addTableCell(factory, wordMLPackage, contractorRow5, contractorDetails.getEmail_id(),
			contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);			
			
			contractorDetailsTable.getContent().add(contractorRow5);
			/**************************************************************/
			
			Tr contractorRow6 = factory.createTr();	
			
			addTableCell(factory, wordMLPackage, contractorRow6, "GST Number",
			titleRpr, JcEnumeration.LEFT, true, "ecf2ff");		
			addTableCell(factory, wordMLPackage, contractorRow6, contractorDetails.getGst_number(),
			contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);		
			
			contractorDetailsTable.getContent().add(contractorRow6);
			/**************************************************************/
			
			Tr contractorRow7 = factory.createTr();	
			
			addTableCell(factory, wordMLPackage, contractorRow7, "Pan Number",
			titleRpr, JcEnumeration.LEFT, true, "ecf2ff");		
			addTableCell(factory, wordMLPackage, contractorRow7, contractorDetails.getPan_number(),
			contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);		
			
			contractorDetailsTable.getContent().add(contractorRow7);
			/**************************************************************/
			Tr contractorRow8 = factory.createTr();	
			
			addTableCell(factory, wordMLPackage, contractorRow8, "Remarks",
			titleRpr, JcEnumeration.LEFT, true, "ecf2ff");		
			addTableCell(factory, wordMLPackage, contractorRow8, contractorDetails.getRemarks(),
			contentRpr, JcEnumeration.LEFT, hasBgColor, backgroundColor);		
			
			contractorDetailsTable.getContent().add(contractorRow8);
			/**************************************************************/
			setTableAlign(factory, contractorDetailsTable, JcEnumeration.CENTER);
			mp.addObject(contractorDetailsTable);

			/********************* Contractor Details ends *******************************************************/
			/********************* Key Personnel Details Starts *******************************************************/
			Tbl keyPersonnelTable = factory.createTbl();
			addBorders(keyPersonnelTable, "2");

			addParagraph(mp, factory);
			addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, fontRPr, "Key Personnel Details");

			Tr keyPersonnelTitleRow = factory.createTr();
			List<String> keyPersonnelTableHeader = new ArrayList<String>();
			keyPersonnelTableHeader.add("Name");
			keyPersonnelTableHeader.add("Mobile NO");
			keyPersonnelTableHeader.add("Email ID");
			keyPersonnelTableHeader.add("Designation");

			for (String headerValue : keyPersonnelTableHeader) {
				addTableCell(factory, wordMLPackage, keyPersonnelTitleRow, headerValue, titleRpr, JcEnumeration.LEFT, true,
						"ecf2ff");
			}
			keyPersonnelTable.getContent().add(keyPersonnelTitleRow);

			for (Contract cObj : keyPersonnels) {
				hasBgColor = false;
				backgroundColor = null;
				Tr contentRow = factory.createTr();

				addTableCell(factory, wordMLPackage, contentRow, cObj.getName(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getMobile_no(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getEmail_id(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);
				addTableCell(factory, wordMLPackage, contentRow, cObj.getDesignation(), contentRpr,
						JcEnumeration.LEFT, hasBgColor, backgroundColor);
				

				keyPersonnelTable.getContent().add(contentRow);
			}
			if (StringUtils.isEmpty(keyPersonnels) || keyPersonnels.isEmpty()) {
				hasBgColor = false;
				backgroundColor = null;
				Tr contentRow = factory.createTr();

				List<String> noDataRow = new ArrayList<String>();
				noDataRow.add("NO KEY PERSONNEL DATA");
				for (int i = 0; i < 3; i++) {
					noDataRow.add("");
				}

				for (String headerValue : noDataRow) {
					addTableCell(factory, wordMLPackage, contentRow, headerValue, titleRpr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
				}
				keyPersonnelTable.getContent().add(contentRow);
				mergeCellsHorizontal(keyPersonnelTable, 1, 0, 4);
			}

			setTableAlign(factory, keyPersonnelTable, JcEnumeration.CENTER);
			mp.addObject(keyPersonnelTable);

			/********************* Insurance Details ends *******************************************************/

		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public static void createTableForFinanceReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, List<Contract> contractsData,String report_created_date) throws Exception {

		try {

			RPr titleRpr = getRPr(factory, "Calibri", "000000", "12", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Calibri", "000000", "12", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false,
					false);
			
			RPr calibriBoldRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA,
					true, false, false, false);		
			RPr calibriBoldDateRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);

			int temp = 1;


				Tbl tableHead = factory.createTbl();
				setLandscapeTableAlign(factory, tableHead, JcEnumeration.CENTER);
				//addBorders(tableHead, "0");
				
				/**************************************************************************/


				
				
				String date = "";
				if(temp == 1) {
					date = report_created_date;
				}else {
					addParagraph(mp, factory);
				}
				temp++;
				
				

				mp.addObject(tableHead);
				
				/***************************************************************/

				Tbl table = factory.createTbl();
				addBorders(table, "2");
				
				
				/************************************************************************/
				Tr titleRow0 = factory.createTr();
				List<String> tableHeader0 = new ArrayList<String>();
				tableHeader0.add("Contract Description");
				tableHeader0.add("Contract Value");
				tableHeader0.add("Planned Progress FY 22");
				tableHeader0.add("Budget Estimate from Schedule");
				
				int columnNo = 1;
				for (String headerValue : tableHeader0) {
					int width = 0;
					if(1 == columnNo) {
						width = 1500;
					}else if(2 == columnNo) {
						width = 500;
					}else if(3 == columnNo) {
						width = 500;
					}else if(4 == columnNo) {
						width = 500;
					}
					columnNo++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow0, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table.getContent().add(titleRow0);

			
				/*******************************************************************************/

				int sNo = 1;
				for (Contract cObj1 : contractsData) 
				{
					boolean hasBgColor = false;
					String backgroundColor = null;
					Tr contentRow = factory.createTr();
					
					if (!StringUtils.isEmpty(cObj1.getValue()))
					{
							addTableCell(factory, wordMLPackage, contentRow, cObj1.getContract_name(), garamondRPr,JcEnumeration.LEFT, hasBgColor, backgroundColor);
					}
					else
					{
						addTableCell(factory, wordMLPackage, contentRow, cObj1.getContract_name(), garamondBoldRPr,JcEnumeration.LEFT, hasBgColor, backgroundColor);						
					}
					addTableCell(factory, wordMLPackage, contentRow, cObj1.getValue(), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
					if (!StringUtils.isEmpty(cObj1.getActual_physical_progress()))
					{
						addTableCell(factory, wordMLPackage, contentRow,cObj1.getActual_physical_progress()+'%',
								garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
					}
					else
					{
						addTableCell(factory, wordMLPackage, contentRow,cObj1.getActual_physical_progress(),
								garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);						
					}
					
					addTableCell(factory, wordMLPackage, contentRow,cObj1.getActual_financial_progress(),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);					
				
					table.getContent().add(contentRow);
				}
				setTableAlign(factory, table, JcEnumeration.CENTER);
				mp.addObject(table);

		} catch (Exception e) {
			throw new Exception(e);
		}

	}
	
	private static int safeParseInt(String val) {
	    try {
	        if (val == null || val.trim().isEmpty()) return 0;
	        return (int) Math.round(Double.parseDouble(val.trim()));
	    } catch (Exception e) {
	        return 0;
	    }
	}

	
	private static String safeStr(String val) {
	    return (val == null || val.trim().isEmpty()) ? "" : val;
	}

	
	
	public static void createTableForTPCStatusReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, List<StripChart> contractsData,List<StripChart> divisionData,List<StripChart> structureData,List<StripChart> structurecumData,String report_created_date) throws Exception {

		try {

			RPr titleRpr = getRPr(factory, "Calibri", "000000", "12", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Calibri", "000000", "12", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			
			RPr boldRPr1 = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA, true, false, false, false);
			
			
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false,
					false);
			
			RPr calibriBoldRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA,
					true, false, false, false);		
			RPr calibriBoldDateRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);

			int temp = 1;


				Tbl tableHead = factory.createTbl();
				setLandscapeTableAlign(factory, tableHead, JcEnumeration.CENTER);
				//addBorders(tableHead, "0");
				
				/**************************************************************************/


				
				
				String date = "";
				if(temp == 1) {
					date = report_created_date;
				}else {
					addParagraph(mp, factory);
				}
				temp++;
				
				
				addParagraph(mp, factory);

				mp.addObject(tableHead);
				
				/***************************************************************/

				Tbl table = factory.createTbl();
				addBorders(table, "2");
				
				/************************************************************************/
				Tr titleRow0 = factory.createTr();
				List<String> tableHeader0 = new ArrayList<String>();
				tableHeader0.add("S. No.");
				tableHeader0.add("Contract Short Name");
				tableHeader0.add("TPC Structure");
				tableHeader0.add("% Progress");
				tableHeader0.add("Status");
				tableHeader0.add("TDC/DOC");
				int columnNo = 1;
				for (String headerValue : tableHeader0) {
					int width = 0;
					if(1 == columnNo) {
						width = 150;
					}else if(2 == columnNo) {
						width = 700;
					}else if(3 == columnNo) {
						width = 1000;
					}else if(4 == columnNo) {
						width = 300;
					}
					else if(5 == columnNo) {
						width = 500;
					}
					else if(6 == columnNo) {
						width = 500;
					}					
					columnNo++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow0, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table.getContent().add(titleRow0);

			
				/*******************************************************************************/

				int sNo = 1;
				for (StripChart cObj1 : contractsData) 
				{
						boolean hasBgColor = false;
						String backgroundColor = null;
						Tr contentRow = factory.createTr();
						
						addTableCell(factory, wordMLPackage, contentRow, String.valueOf(sNo), garamondRPr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor);					
						
						addTableCell(factory, wordMLPackage, contentRow, safeStr(cObj1.getContract_short_name()), garamondRPr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor);
						
						addTableCell(factory, wordMLPackage, contentRow,safeStr(cObj1.getStructure()),
								garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
						
						addTableCell(factory, wordMLPackage, contentRow,safeStr(cObj1.getProgress()),
								garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);				
						
						addTableCell(factory, wordMLPackage, contentRow,safeStr(cObj1.getStatus()),
								garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
						
						addTableCell(factory, wordMLPackage, contentRow,safeStr(cObj1.getProgress_date()),
								garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);								
					
						table.getContent().add(contentRow);
						
						if(String.valueOf(sNo).compareTo("30")==0)
						{
							//table.getContent().add(titleRow0);
						}					
						
						sNo++;
					}
					
				
				
				boolean hasBgColor1 = false;
				String backgroundColor1 = null;
				Tr contentRow11 = factory.createTr();
				
				addTableCell(factory, wordMLPackage, contentRow11, String.valueOf(sNo), garamondRPr, JcEnumeration.CENTER,
						hasBgColor1, backgroundColor1);					
				
				addTableCell(factory, wordMLPackage, contentRow11, "CR-B/Wall (For AIIB Report only)", garamondRPr, JcEnumeration.CENTER,
						hasBgColor1, backgroundColor1);
				
				addTableCell(factory, wordMLPackage, contentRow11,"9/7 and 10/7 Wadala and GTBN (Boundary wall)",
						garamondRPr, JcEnumeration.CENTER, hasBgColor1, backgroundColor1);	
				
				addTableCell(factory, wordMLPackage, contentRow11,"100",
						garamondRPr, JcEnumeration.CENTER, hasBgColor1, backgroundColor1);				
				
				addTableCell(factory, wordMLPackage, contentRow11,"Completed",
						garamondRPr, JcEnumeration.CENTER, hasBgColor1, backgroundColor1);
				
				addTableCell(factory, wordMLPackage, contentRow11,"31-10-2018",
						garamondRPr, JcEnumeration.CENTER, hasBgColor1, backgroundColor1);								
			
				table.getContent().add(contentRow11);			
				
				setTableAlign(factory, table, JcEnumeration.CENTER);
				
				Tbl table2 = factory.createTbl();
				addBorders(table2, "2");
				

				

				
				/************************************************************************/
				Tr titleRow1 = factory.createTr();
				List<String> tableHeader1 = new ArrayList<String>();
				tableHeader1.add("S. No.");
				tableHeader1.add("Railway");
				tableHeader1.add("Work Scope");
				tableHeader1.add("Completed");
				tableHeader1.add("Balance");
				tableHeader1.add("WIP");
				int columnNo1 = 1;
				for (String headerValue : tableHeader1) {
					int width = 0;
					if(1 == columnNo1) {
						width = 200;
					}else if(2 == columnNo1) {
						width = 1000;
					}else if(3 == columnNo1) {
						width = 500;
					}else if(4 == columnNo1) {
						width = 500;
					}else if(5 == columnNo1) {
						width = 500;
					}else if(6 == columnNo1) {
						width = 500;
					}
					columnNo1++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow1, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table2.getContent().add(titleRow1);

			
				/*******************************************************************************/
				
				
				Tbl table3 = factory.createTbl();
				addBorders(table3, "2");				
				

				/************************************************************************/
				Tr titleRow2 = factory.createTr();
				List<String> tableHeader2 = new ArrayList<String>();
				tableHeader2.add("S. No.");
				tableHeader2.add("Structure");
				tableHeader2.add("Central Railway");
				tableHeader2.add("");
				tableHeader2.add("");
				tableHeader2.add("");
				tableHeader2.add("Western Railway");
				tableHeader2.add("");
				tableHeader2.add("");
				tableHeader2.add("");
				
				
				int columnNo2 = 1;
				for (String headerValue2 : tableHeader2) {
					int width = 0;
					if(1 == columnNo2) {
						width = 100;
					}else if(2 == columnNo2) {
						width = 1500;
					}else if(3 == columnNo2) {
						width = 400;
					}else if(4 == columnNo2) {
						width = 600;
					}if(5 == columnNo2) {
						width = 500;
					}else if(6 == columnNo2) {
						width = 500;
					}else if(7 == columnNo2) {
						width = 400;
					}else if(8 == columnNo2) {
						width = 600;
					}else if(9 == columnNo2) {
						width = 500;
					}else if(10 == columnNo2) {
						width = 500;
					}
					columnNo2++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow2, headerValue2, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table3.getContent().add(titleRow2);
				

				
				
				Tr titleRow3 = factory.createTr();
				List<String> tableHeader4 = new ArrayList<String>();
				tableHeader4.add("");
				tableHeader4.add("");
				tableHeader4.add("Scope");
				tableHeader4.add("Completed");
				tableHeader4.add("Balance");
				tableHeader4.add("WIP");
				tableHeader4.add("Scope");
				tableHeader4.add("Completed");	
				tableHeader4.add("Balance");
				tableHeader4.add("WIP");				
				
				int columnNo3 = 1;
				for (String headerValue3 : tableHeader4) {
					int width = 0;
					if(1 == columnNo3) {
						width = 100;
					}else if(2 == columnNo3) {
						width = 1500;
					}else if(3 == columnNo3) {
						width = 400;
					}else if(4 == columnNo3) {
						width = 600;
					}if(5 == columnNo3) {
						width = 500;
					}else if(6 == columnNo3) {
						width = 500;
					}else if(7 == columnNo3) {
						width = 400;
					}else if(8 == columnNo3) {
						width = 600;
					}else if(9 == columnNo3) {
						width = 500;
					}else if(10 == columnNo3) {
						width = 500;
					}				
					columnNo3++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow3, headerValue3, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table3.getContent().add(titleRow3);				

			
				/*******************************************************************************/				


				int sNo1 = 1;
				int a1=0;
				int a2=0;
				int a3=0;
				int a4=0;
				for (StripChart cObj1 : divisionData) 
				{
					boolean hasBgColor = false;
					String backgroundColor = null;
					Tr contentRow = factory.createTr();
					
					addTableCell(factory, wordMLPackage, contentRow, String.valueOf(sNo1), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);					
					
					addTableCell(factory, wordMLPackage, contentRow, cObj1.getDivision(), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
					
					addTableCell(factory, wordMLPackage, contentRow,cObj1.getWorkScope(),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
					
					addTableCell(factory, wordMLPackage, contentRow,cObj1.getCompleted(),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
					
					addTableCell(factory, wordMLPackage, contentRow,cObj1.getBalance(),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
					
					addTableCell(factory, wordMLPackage, contentRow,cObj1.getWip(),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);						
					
				
					table2.getContent().add(contentRow);
					a1=a1+safeParseInt(cObj1.getWorkScope());
					a2=a2+safeParseInt(cObj1.getCompleted());
					a3=a3+safeParseInt(cObj1.getBalance());
					a4=a4+safeParseInt(cObj1.getWip());
					
					sNo1++;
				}
				Tr contentRow1 = factory.createTr();
				boolean hasBgColor = false;
				
				String backgroundColor = null;				
				
				addTableCell(factory, wordMLPackage, contentRow1, "Total", garamondRPr, JcEnumeration.CENTER,
						hasBgColor, backgroundColor);					
				
				addTableCell(factory, wordMLPackage, contentRow1, "", garamondRPr, JcEnumeration.CENTER,
						hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow1,String.valueOf(a1),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
				
				addTableCell(factory, wordMLPackage, contentRow1,String.valueOf(a2),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow1,String.valueOf(a3),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow1,String.valueOf(a4),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);					
				
				table2.getContent().add(contentRow1);
				
				setTableAlign(factory, table2, JcEnumeration.CENTER);		
				
				int lastRowIndex2 = table2.getContent().size() - 1;
				mergeCellsHorizontal(table2, lastRowIndex2, 0, 1);
				
				mp.addObject(table);
				
				addPageBreak(mp);
				addHeading(wordMLPackage, mp, factory, JcEnumeration.LEFT, boldRPr1, "Railway-wise Summary:");				
				
				
				mp.addObject(table2);
				
				
				
				
				/*******************************************************************************/				


				int sNo2 = 1;
				int a11=0;
				int a21=0;
				int a31=0;
				int a41=0;
				int a51=0;
				int a61=0;
				int a71=0;
				int a81=0;	
			
				for (StripChart cObj1 : structureData) 
				{
					boolean hasBgColor2 = false;
					String backgroundColor2 = null;
					Tr contentRow5 = factory.createTr();
					
					addTableCell(factory, wordMLPackage, contentRow5, String.valueOf(sNo2), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);					
					
					addTableCell(factory, wordMLPackage, contentRow5, safeStr(cObj1.getStructure()), garamondRPr, JcEnumeration.CENTER,
							hasBgColor, backgroundColor);
					
					addTableCell(factory, wordMLPackage, contentRow5,safeStr(cObj1.getcScope()),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
					
					addTableCell(factory, wordMLPackage, contentRow5,safeStr(cObj1.getcCompleted()),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
					
					addTableCell(factory, wordMLPackage, contentRow5,safeStr(cObj1.getcBalance()),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
					
					addTableCell(factory, wordMLPackage, contentRow5,safeStr(cObj1.getcWIP()),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
					
					addTableCell(factory, wordMLPackage, contentRow5,safeStr(cObj1.getwScope()),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
					
					addTableCell(factory, wordMLPackage, contentRow5,safeStr(cObj1.getwCompleted()),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
					
					addTableCell(factory, wordMLPackage, contentRow5,safeStr(cObj1.getwBalance()),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
					
					addTableCell(factory, wordMLPackage, contentRow5,safeStr(cObj1.getwWIP()),
							garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);					
					
				
					table3.getContent().add(contentRow5);
					a11=a11+safeParseInt(cObj1.getcScope());
					a21=a21+safeParseInt(cObj1.getcCompleted());
					a31=a31+safeParseInt(cObj1.getcBalance());
					a41=a41+safeParseInt(cObj1.getcWIP());
					a51=a51+safeParseInt(cObj1.getwScope());
					a61=a61+safeParseInt(cObj1.getwCompleted());
					a71=a71+safeParseInt(cObj1.getwBalance());
					a81=a81+safeParseInt(cObj1.getwWIP());
					
					sNo2++;
				}
				
				
				Tr contentRow3 = factory.createTr();
				boolean hasBgColor3 = false;
				
				String backgroundColor3 = null;				
				
				addTableCell(factory, wordMLPackage, contentRow3, "Total", garamondRPr, JcEnumeration.CENTER,
						hasBgColor, backgroundColor);					
				
				addTableCell(factory, wordMLPackage, contentRow3, "", garamondRPr, JcEnumeration.CENTER,
						hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow3,String.valueOf(a11),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
				
				addTableCell(factory, wordMLPackage, contentRow3,String.valueOf(a21),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow3,String.valueOf(a31),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow3,String.valueOf(a41),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
				
				addTableCell(factory, wordMLPackage, contentRow3,String.valueOf(a51),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
				
				addTableCell(factory, wordMLPackage, contentRow3,String.valueOf(a61),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow3,String.valueOf(a71),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow3,String.valueOf(a81),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);				
				
				table3.getContent().add(contentRow3);
				
				setTableAlign(factory, table3, JcEnumeration.CENTER);		
				
//				mergeCellsHorizontal(table3, sNo2+1, 0, 1);
				
				int lastRowIndex3 = table3.getContent().size() - 1;
				mergeCellsHorizontal(table3, lastRowIndex3, 0, 1);

				mergeCellsHorizontal(table3, 0, 2, 5);
				mergeCellsHorizontal(table3, 0, 6, 9);	
				
				mergeCellsVertically(table3, 0, 0, 1);
				mergeCellsVertically(table3, 1, 0, 1);
				
				
				addParagraph(mp, factory);
				addHeading(wordMLPackage, mp, factory, JcEnumeration.LEFT, boldRPr1, "Structure-wise Summary:");				
				
				
				mp.addObject(table3);
				
				
				addParagraph(mp, factory);
				
				/*******************************************************************************/
				
				
				Tbl table4 = factory.createTbl();
				addBorders(table4, "2");				
				

				/************************************************************************/
				Tr titleRow5 = factory.createTr();
				List<String> tableHeader5 = new ArrayList<String>();
				tableHeader5.add("S. No.");
				tableHeader5.add("Structure");
				tableHeader5.add("Central + Western Railway");
				tableHeader5.add("");
				tableHeader5.add("");
				tableHeader5.add("");
				
				
				int columnNo5 = 1;
				for (String headerValue5 : tableHeader5) {
					int width = 0;
					if(1 == columnNo5) {
						width = 200;
					}else if(2 == columnNo5) {
						width = 1000;
					}else if(3 == columnNo5) {
						width = 500;
					}
					columnNo5++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow5, headerValue5, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table4.getContent().add(titleRow5);	
				
				
				
				Tr titleRow6 = factory.createTr();
				List<String> tableHeader6 = new ArrayList<String>();
				tableHeader6.add("");
				tableHeader6.add("");
				tableHeader6.add("Scope");
				tableHeader6.add("Completed");
				tableHeader6.add("Balance");
				tableHeader6.add("WIP");
			
				
				int columnNo6 = 1;
				for (String headerValue6 : tableHeader6) {
					int width = 0;
					if(1 == columnNo6) {
						width = 200;
					}else if(2 == columnNo6) {
						width = 1000;
					}else if(3 == columnNo6) {
						width = 500;
					}else if(4 == columnNo6) {
						width = 500;
					}if(5 == columnNo6) {
						width = 500;
					}else if(6 == columnNo6) {
						width = 500;
					}					
					columnNo6++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow6, headerValue6, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table4.getContent().add(titleRow6);			
				
				
					
				
				

				
				/*******************************************************************************/				


				int sNo3 = 1;
				int aa1=0;
				int aa2=0;
				int aa3=0;
				int aa4=0;
		
				for (StripChart cObj1 : structurecumData) 
				{
					boolean hasBgColor2 = false;
					String backgroundColor2 = null;
					Tr contentRow8 = factory.createTr();
					
					addTableCell(factory, wordMLPackage, contentRow8,
					        String.valueOf(sNo3),
					        garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);

					addTableCell(factory, wordMLPackage, contentRow8,
					        safeStr(cObj1.getStructure()),
					        garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);

					addTableCell(factory, wordMLPackage, contentRow8,
					        safeStr(cObj1.getScope()),
					        garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);

					addTableCell(factory, wordMLPackage, contentRow8,
					        safeStr(cObj1.getCompleted()),
					        garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);

					addTableCell(factory, wordMLPackage, contentRow8,
					        safeStr(cObj1.getBalance()),
					        garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);

					addTableCell(factory, wordMLPackage, contentRow8,
					        safeStr(cObj1.getWip()),
					        garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);


				
					
				
					table4.getContent().add(contentRow8);
					aa1=aa1+safeParseInt(cObj1.getScope());
					aa2=aa2+safeParseInt(cObj1.getCompleted());
					aa3=aa3+safeParseInt(cObj1.getBalance());
					aa4=aa4+safeParseInt(cObj1.getWip());
					
					sNo3++;
				}
				
				
				Tr contentRow9 = factory.createTr();
				boolean hasBgColor9 = false;
				
				String backgroundColor9 = null;				
				
				addTableCell(factory, wordMLPackage, contentRow9, "Total", garamondRPr, JcEnumeration.CENTER,
						hasBgColor, backgroundColor);					
				
				addTableCell(factory, wordMLPackage, contentRow9, "", garamondRPr, JcEnumeration.CENTER,
						hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow9,String.valueOf(aa1),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
				
				addTableCell(factory, wordMLPackage, contentRow9,String.valueOf(aa2),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow9,String.valueOf(aa3),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow9,String.valueOf(aa4),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
				
			
				
				table4.getContent().add(contentRow9);

				setTableAlign(factory, table4, JcEnumeration.CENTER);

				mergeCellsHorizontal(table4, 0, 2, 5);

				int lastRowIndex = table4.getContent().size() - 1;
				mergeCellsHorizontal(table4, lastRowIndex, 0, 1);

				if (table4.getContent().size() > 1) {
				    mergeCellsVertically(table4, 0, 0, 1);
				    mergeCellsVertically(table4, 1, 0, 1);
				}

				mp.addObject(table4);

				

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}	
	
	public static void createTableForListofContractsClosertoBGExpiryDateReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, List<Contract> list,String report_created_date) throws Exception {
		try {
			RPr titleRpr = getRPr(factory, "Calibri", "000000", "18", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Calibri", "000000", "14", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false,false);
			
			RPr calibriBoldRPr = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA,
					true, false, false, false);
			RPr calibriBoldDateRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);
			
			int temp = 1;
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}	
	/**************************************************************************************************************/

	private static void addParagraph(MainDocumentPart mp, ObjectFactory factory) {
		P p = factory.createP();
		R r = factory.createR();
		//Br br = factory.createBr();
		//r.getContent().add(br);
		p.getContent().add(r);

		mp.addObject(p);
	}

	/**
	 * @param titleRpr 
	 * @param alignment *********************************************************************************************************************/

	public static void addHeading(WordprocessingMLPackage wordMLPackage, MainDocumentPart t, ObjectFactory factory,
			JcEnumeration alignment, RPr titleRPr, String contentValue) throws Exception {
		P paragraph = factory.createP();
		setParagraphAlign(factory, paragraph, alignment);
		Text txt = factory.createText();
		txt.setValue(contentValue);
		R run = factory.createR();
		run.getContent().add(txt);
		run.setRPr(titleRPr);
		paragraph.getContent().add(run);
		t.addObject(paragraph);
	}

	public static void addTableCellRemarks(ObjectFactory factory, WordprocessingMLPackage wordMLPackage, Tr tableRow,
			String content, RPr rpr, JcEnumeration jcEnumeration, boolean hasBgColor, String backgroudColor) {
		Tc tableCell = factory.createTc();
		P p = factory.createP();
		setParagraphAlign(factory, p, jcEnumeration);
		Text t = factory.createText();
		t.setValue(content);
		R run = factory.createR();
		run.setRPr(rpr);

		TcPr tcPr = tableCell.getTcPr();
		if (tcPr == null) {
			tcPr = factory.createTcPr();
		}

		run.getContent().add(t);
		p.getContent().add(run);

		//Removing space in cells
		PPr pPr = factory.createPPr();
		Spacing spacing = new Spacing();
		spacing.setAfter(BigInteger.ZERO);
		pPr.setSpacing(spacing);
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

		tableCell.setTcPr(tcPr);

		tableRow.getContent().add(tableCell);
	}

	private static void addPageBreak(MainDocumentPart documentPart) {
		ObjectFactory objectFactory = new ObjectFactory();
		//P paragraph = objectFactory.createP();
		//R run = objectFactory.createR();
		P p = objectFactory.createP();
		// Create object for r
		R r = objectFactory.createR();
		p.getContent().add(r);
		// Create object for br
		Br br = objectFactory.createBr();
		r.getContent().add(br);
		br.setType(org.docx4j.wml.STBrType.PAGE);
		documentPart.addObject(p);
	}

	public static RPr getRPr(ObjectFactory factory, String fontFamily, String colorVal, String fontSize, STHint sTHint,
			boolean isBlod, boolean isUnderLine, boolean isItalic, boolean isStrike) {
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

	public static void addBorders(Tbl table, String borderSize) {
		table.setTblPr(new TblPr());
		CTBorder border = new CTBorder();
		border.setColor("auto");
		border.setSz(new BigInteger(borderSize));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);
		TblBorders borders = new TblBorders();
		borders.setBottom(border);
		borders.setLeft(border);
		borders.setRight(border);
		borders.setTop(border);
		borders.setInsideH(border);
		borders.setInsideV(border);
		table.getTblPr().setTblBorders(borders);
	}

	public static void setParagraphAlign(ObjectFactory factory, P p, JcEnumeration jcEnumeration) {
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
		tblwidth.setW(BigInteger.valueOf(5800)); // 5000 = 100%
		tblwidth.setType("pct");
		tablePr.setTblW(tblwidth);
		
		CTTblLayoutType tblLayoutType = new CTTblLayoutType();
        STTblLayoutType stTblLayoutType = STTblLayoutType.FIXED;
        tblLayoutType.setType(stTblLayoutType);
        tablePr.setTblLayout(tblLayoutType);

		table.setTblPr(tablePr);
	}
	
	public static void setLandscapeTableAlign(ObjectFactory factory, Tbl table, JcEnumeration jcEnumeration) {
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
		tblwidth.setW(BigInteger.valueOf(5400)); // 5000 = 100%
		tblwidth.setType("pct");
		tablePr.setTblW(tblwidth);
		
		CTTblLayoutType tblLayoutType = new CTTblLayoutType();
        STTblLayoutType stTblLayoutType = STTblLayoutType.FIXED;
        tblLayoutType.setType(stTblLayoutType);
        tablePr.setTblLayout(tblLayoutType);

		table.setTblPr(tablePr);
	}

	public static void addTableCell(ObjectFactory factory, WordprocessingMLPackage wordMLPackage, Tr tableRow,
			String content, RPr rpr, JcEnumeration jcEnumeration, boolean hasBgColor, String backgroudColor) {
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
	
	public static void addTableCellAndWidth(ObjectFactory factory, WordprocessingMLPackage wordMLPackage, Tr tableRow,
			String content, RPr rpr, JcEnumeration jcEnumeration, boolean hasBgColor, String backgroudColor,int columnWidth) {
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
		
		/********************************/		
		if (columnWidth > 0) {
            TblWidth tableWidth = new TblWidth();
            tableWidth.setW(BigInteger.valueOf(columnWidth));
            tableWidth.setType("dxa");
            tcPr.setTcW(tableWidth);
        }		
		/********************************/

		tableCell.setTcPr(tcPr);		
		
		tableRow.getContent().add(tableCell);
	}
	
	public static void addTableCellWithTopBottomSpace(ObjectFactory factory,
			WordprocessingMLPackage wordMLPackage, Tr tableRow, String content,
			RPr rpr, JcEnumeration jcEnumeration, boolean hasBgColor,
			String backgroudColor) {
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
		spacing.setBefore(BigInteger.ONE);
		spacing.setAfter(BigInteger.ONE);
		spacing.setAfterLines(BigInteger.valueOf(20));
		spacing.setBeforeLines(BigInteger.TEN);
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
			shd.setColor("#ecf2ff");
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
	public static void mergeCellsHorizontal(Tbl tbl, int row, int fromCell, int toCell) {

	    if (row < 0 || fromCell < 0 || toCell < 0 || fromCell > toCell) {
	        return;
	    }

	    List<Tr> trList = getTblAllTr(tbl);

	    if (row >= trList.size()) {   // ✅ FIXED
	        return;
	    }

	    Tr tr = trList.get(row);
	    List<Tc> tcList = getTrAllCell(tr);

	    if (fromCell >= tcList.size() || toCell >= tcList.size()) {   // ✅ FIXED
	        return;
	    }

	    for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {

	        Tc tc = tcList.get(cellIndex);
	        TcPr tcPr = getTcPr(tc);

	        HMerge hMerge = tcPr.getHMerge();
	        if (hMerge == null) {
	            hMerge = new HMerge();
	            tcPr.setHMerge(hMerge);
	        }

	        if (cellIndex == fromCell) {
	            hMerge.setVal("restart");
	        } else {
	            hMerge.setVal("continue");
	        }
	    }
	}


	public static Tc getTc(Tbl tbl, int row, int cell) {
		if (row < 0 || cell < 0) {
			return null;
		}
		List<Tr> trList = getTblAllTr(tbl);
		if (row >= trList.size()) {
			return null;
		}
		List<Tc> tcList = getTrAllCell(trList.get(row));
		if (cell >= tcList.size()) {
			return null;
		}
		return tcList.get(cell);
	}

	public static List<Tc> getTrAllCell(Tr tr) {
		List<Object> objList = getAllElementFromObject(tr, Tc.class);
		List<Tc> tcList = new ArrayList<Tc>();
		if (objList == null) {
			return tcList;
		}
		for (Object tcObj : objList) {
			if (tcObj instanceof Tc) {
				Tc objTc = (Tc) tcObj;
				tcList.add(objTc);
			}
		}
		return tcList;
	}

	public static TcPr getTcPr(Tc tc) {
		TcPr tcPr = tc.getTcPr();
		if (tcPr == null) {
			tcPr = new TcPr();
			tc.setTcPr(tcPr);
		}
		return tcPr;
	}

	public static List<Tr> getTblAllTr(Tbl tbl) {
		List<Object> objList = getAllElementFromObject(tbl, Tr.class);
		List<Tr> trList = new ArrayList<Tr>();
		if (objList == null) {
			return trList;
		}
		for (Object obj : objList) {
			if (obj instanceof Tr) {
				Tr tr = (Tr) obj;
				trList.add(tr);
			}
		}
		return trList;
	}

	public static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
		List<Object> result = new ArrayList<Object>();
		if (obj instanceof JAXBElement)
			obj = ((JAXBElement<?>) obj).getValue();
		if (obj.getClass().equals(toSearch))
			result.add(obj);
		else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			}
		}
		return result;
	}

	public static void mergeCellsVertically(Tbl tbl, int col, int fromRow, int toRow) {

	    if (col < 0 || fromRow < 0 || toRow < 0) return;

	    List<Tr> rows = getTblAllTr(tbl);

	    if (fromRow >= rows.size()) return;
	    if (toRow >= rows.size()) toRow = rows.size() - 1;

	    int baseCellCount = getTrAllCell(rows.get(fromRow)).size();

	    for (int r = fromRow; r <= toRow; r++) {
	        if (getTrAllCell(rows.get(r)).size() != baseCellCount) {
	            return; // Prevents broken docx
	        }
	    }

	    for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {

	        Tc tc = getTc(tbl, rowIndex, col);
	        if (tc == null) return;

	        TcPr tcPr = getTcPr(tc);

	        VMerge vMerge = tcPr.getVMerge();
	        if (vMerge == null) {
	            vMerge = new VMerge();
	            tcPr.setVMerge(vMerge);
	        }

	        vMerge.setVal(rowIndex == fromRow ? "restart" : "continue");
	    }
	}

	public static void createTableForMCDOProgressReport(WordprocessingMLPackage wordMLPackage, MainDocumentPart mp,
			ObjectFactory factory, List<StripChart> contractsData,List<StripChart> contractsData1, String report_created_date, String fDate, String tDate) throws Exception {
		try {	

			RPr titleRpr = getRPr(factory, "Verdana", "000000", "10", STHint.EAST_ASIA, true, false, false,
					false);

			RPr contentRpr = getRPr(factory, "Verdana", "000000", "10", STHint.EAST_ASIA, false, false, false,
					false);

			RPr contentRprParent = getRPr(factory, "Verdana", "000000", "18", STHint.EAST_ASIA, true, false,
					false, false);

			RPr titleRPr = getRPr(factory, "Verdana", "000000", "30", STHint.EAST_ASIA, true, true, false, false);
			RPr boldRPr = getRPr(factory, "Verdana", "000000", "20", STHint.EAST_ASIA, true, false, false, false);
			
			RPr boldRPr1 = getRPr(factory, "Verdana", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
			
			
			RPr fontRPr = getRPr(factory, "Verdana", "000000", "18", STHint.EAST_ASIA, false, false, false,
					false);
			
			RPr calibriBoldRPr = getRPr(factory, "Verdana", "000000", "22", STHint.EAST_ASIA,
					true, false, false, false);		
			RPr calibriBoldDateRPr = getRPr(factory, "Verdana", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);	
			
			RPr garamondBoldRPr = getRPr(factory, "Verdana", "000000", "18", STHint.EAST_ASIA,
					true, false, false, false);
			RPr garamondRPr = getRPr(factory, "Verdana", "000000", "20", STHint.EAST_ASIA,
					false, false, false, false);

			int temp = 1;


				Tbl tableHead = factory.createTbl();
				setLandscapeTableAlign(factory, tableHead, JcEnumeration.CENTER);
				
				String date = "";
				if(temp == 1) {
					date = report_created_date;
				}else {
					addParagraph(mp, factory);
				}
				temp++;
				
				mp.addObject(tableHead);
				
				//if (!StringUtils.isEmpty(contractsData))
				//{
				
				addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, titleRPr, "MCDO Progress Report");
				
				addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, calibriBoldRPr, "for the Period "+fDate+" to "+tDate+"");
				
				if (!StringUtils.isEmpty(contractsData))
				{
				
				addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, titleRPr, "New Suburban Railway Corridor between");
				
				addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, titleRPr, "Panvel-Karjat on Central Railway (Double line)");				
				
				addHeading(wordMLPackage, mp, factory, JcEnumeration.LEFT, calibriBoldRPr, "Works Completed");	
				}
				//}
				
				/***************************************************************/

				Tbl table = factory.createTbl();
				addBorders(table, "2");
				
				/************************************************************************/
				Tr titleRow0 = factory.createTr();
				List<String> tableHeader0 = new ArrayList<String>();
				//if (!StringUtils.isEmpty(contractsData))
				//{
				tableHeader0.add("Works");
				tableHeader0.add("During the period");
				tableHeader0.add("Cumulative");
				tableHeader0.add("Balance");
				int columnNo = 1;
				for (String headerValue : tableHeader0) 
				{
					int width = 0;
					if(1 == columnNo) {
						width = 2800;
					}else if(2 == columnNo) {
						width = 2000;
					}else if(3 == columnNo) {
						width = 2000;
					}else if(4 == columnNo) {
						width = 2000;
					}					
					columnNo++;
					addTableCellAndWidth(factory, wordMLPackage, titleRow0, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
							"ecf2ff",width);
				}
				table.getContent().add(titleRow0);
				//}

				int sNo = 1;
				if (!StringUtils.isEmpty(contractsData))
						{
								for (StripChart cObj1 : contractsData) 
								{
										boolean hasBgColor = false;
										String backgroundColor = null;
										Tr contentRow = factory.createTr();
										
										addTableCell(factory, wordMLPackage, contentRow, cObj1.getWorks(), garamondRPr, JcEnumeration.CENTER,
												hasBgColor, backgroundColor);					
										
										addTableCell(factory, wordMLPackage, contentRow, cObj1.getDuring_month(), garamondRPr, JcEnumeration.CENTER,
												hasBgColor, backgroundColor);
										
										addTableCell(factory, wordMLPackage, contentRow,cObj1.getCumulative(),
												garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
										
										addTableCell(factory, wordMLPackage, contentRow,cObj1.getBalance(),
												garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);				
										table.getContent().add(contentRow);
										sNo++;
								}
								mp.addObject(table);
						}
				
				
				
				if (!StringUtils.isEmpty(contractsData1))
				{
				addParagraph(mp, factory);
				addPageBreak(mp);
				addHeading(wordMLPackage, mp, factory, JcEnumeration.CENTER, titleRPr, "Quadrupling of Virar-Dahanu Road on Western Railway");
				
				addHeading(wordMLPackage, mp, factory, JcEnumeration.LEFT, calibriBoldRPr, "Works Completed");		
				}

				
				Tbl table2 = factory.createTbl();
				addBorders(table2, "2");

				table2.getContent().add(titleRow0);

				int sNo1 = 1;
				if (!StringUtils.isEmpty(contractsData1))
				{
				for (StripChart cObj1 : contractsData1) 
				{
						boolean hasBgColor = false;
						String backgroundColor = null;
						Tr contentRow = factory.createTr();
						
						addTableCell(factory, wordMLPackage, contentRow, cObj1.getWorks(), garamondRPr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor);					
						
						addTableCell(factory, wordMLPackage, contentRow, cObj1.getDuring_month(), garamondRPr, JcEnumeration.CENTER,
								hasBgColor, backgroundColor);
						
						addTableCell(factory, wordMLPackage, contentRow,cObj1.getCumulative(),
								garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
						
						addTableCell(factory, wordMLPackage, contentRow,cObj1.getBalance(),
								garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);				
						table2.getContent().add(contentRow);
						sNo++;
				}
				mp.addObject(table2);	
				}
						
			
				
		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}
	public static void createTableForStationImprovementsReport(WordprocessingMLPackage wordMLPackage,
			MainDocumentPart mp, ObjectFactory factory, List<StripChart> stationList, String report_created_date,List<StripChart> divisionData) {
		
		try {
	        RPr boldRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, true, false, false, false);
	        RPr regularRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA, false, false, false, false);

	        // Create the table
	        Tbl table = factory.createTbl();
	        addBorders(table, "2");
	        
			RPr garamondRPr = getRPr(factory, "Garamond", "000000", "22", STHint.EAST_ASIA,
					false, false, false, false);
			
			RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "20", STHint.EAST_ASIA,
					true, false, false, false);
			
			RPr boldRPr1 = getRPr(factory, "Calibri", "000000", "24", STHint.EAST_ASIA, true, false, false, false);			

	        // Create table headers
	        Tr headerRow = factory.createTr();
	        List<String> headers = Arrays.asList("S. No.", "Railway", "Station", "% Progress", "Status", "TDC/DOC");
	        List<Integer> widths = Arrays.asList(500, 1000, 2000, 1000, 1000, 1000);

	        for (int i = 0; i < headers.size(); i++) {
	            addTableCellAndWidth(factory, wordMLPackage, headerRow, headers.get(i), boldRPr, JcEnumeration.CENTER, true, "ecf2ff", widths.get(i));
	        }
	        table.getContent().add(headerRow);

	        // Fill table rows with station data
	        int index = 1;
	        for (StripChart station : stationList) {
	            Tr row = factory.createTr();

	            addTableCell(factory, wordMLPackage, row, String.valueOf(index++), regularRPr, JcEnumeration.CENTER, false, "ffffff");
	            addTableCell(factory, wordMLPackage, row, station.getContract_short_name(), regularRPr, JcEnumeration.CENTER, false, "ffffff");
	            addTableCell(factory, wordMLPackage, row, station.getStructure(), regularRPr, JcEnumeration.CENTER, false, "ffffff");
	            addTableCell(factory, wordMLPackage, row, station.getProgress(), regularRPr, JcEnumeration.CENTER, false, "ffffff");
	            addTableCell(factory, wordMLPackage, row, station.getStatus(), regularRPr, JcEnumeration.CENTER, false, "ffffff");
	            addTableCell(factory, wordMLPackage, row, station.getProgress_date(), regularRPr, JcEnumeration.CENTER, false, "ffffff");

	            table.getContent().add(row);
	        }
	        
	        mp.addObject(table);
	        
	        int columnNo = 1;
			Tbl table2 = factory.createTbl();
			addBorders(table2, "2");	        
			/************************************************************************/
			Tr titleRow1 = factory.createTr();
			List<String> tableHeader1 = new ArrayList<String>();
			tableHeader1.add("S. No.");
			tableHeader1.add("Railway");
			tableHeader1.add("Work Scope");
			tableHeader1.add("Completed");
			tableHeader1.add("Balance");
			tableHeader1.add("WIP");
			int columnNo1 = 1;
			for (String headerValue : tableHeader1) {
				int width = 0;
				if(1 == columnNo1) {
					width = 200;
				}else if(2 == columnNo1) {
					width = 1000;
				}else if(3 == columnNo1) {
					width = 500;
				}else if(4 == columnNo1) {
					width = 500;
				}else if(5 == columnNo1) {
					width = 500;
				}else if(6 == columnNo1) {
					width = 500;
				}
				columnNo1++;
				addTableCellAndWidth(factory, wordMLPackage, titleRow1, headerValue, garamondBoldRPr, JcEnumeration.CENTER, true,
						"ecf2ff",width);
			}
			table2.getContent().add(titleRow1);
			
			
			/*******************************************************************************/				


			int sNo1 = 1;
			int a1=0;
			int a2=0;
			int a3=0;
			int a4=0;
			for (StripChart cObj1 : divisionData) 
			{
				boolean hasBgColor = false;
				String backgroundColor = null;
				Tr contentRow = factory.createTr();
				
				addTableCell(factory, wordMLPackage, contentRow, String.valueOf(sNo1), garamondRPr, JcEnumeration.CENTER,
						hasBgColor, backgroundColor);					
				
				addTableCell(factory, wordMLPackage, contentRow, safeStr(cObj1.getDivision()), garamondRPr, JcEnumeration.CENTER,
						hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow,safeStr(cObj1.getWorkScope()),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
				
				addTableCell(factory, wordMLPackage, contentRow,safeStr(cObj1.getCompleted()),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow,safeStr(cObj1.getBalance()),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
				
				addTableCell(factory, wordMLPackage, contentRow,safeStr(cObj1.getWip()),
						garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);						
				
			
				table2.getContent().add(contentRow);
				a1=a1+safeParseInt(cObj1.getWorkScope());
				a2=a2+safeParseInt(cObj1.getCompleted());
				a3=a3+safeParseInt(cObj1.getBalance());
				a4=a4+safeParseInt(cObj1.getWip());
				
				sNo1++;
			}
			Tr contentRow1 = factory.createTr();
			boolean hasBgColor = false;
			
			String backgroundColor = null;				
			
			addTableCell(factory, wordMLPackage, contentRow1, "Total", garamondRPr, JcEnumeration.CENTER,
					hasBgColor, backgroundColor);					
			
			addTableCell(factory, wordMLPackage, contentRow1, "", garamondRPr, JcEnumeration.CENTER,
					hasBgColor, backgroundColor);
			
			addTableCell(factory, wordMLPackage, contentRow1,String.valueOf(a1),
					garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);	
			
			addTableCell(factory, wordMLPackage, contentRow1,String.valueOf(a2),
					garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
			
			addTableCell(factory, wordMLPackage, contentRow1,String.valueOf(a3),
					garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);
			
			addTableCell(factory, wordMLPackage, contentRow1,String.valueOf(a4),
					garamondRPr, JcEnumeration.CENTER, hasBgColor, backgroundColor);					
			
			table2.getContent().add(contentRow1);
			
			setTableAlign(factory, table2, JcEnumeration.CENTER);		
			
			mergeCellsHorizontal(table2, sNo1, 0, 1);
			
			
			
			addPageBreak(mp);
			addHeading(wordMLPackage, mp, factory, JcEnumeration.LEFT, boldRPr1, "Railway-wise Summary:");				
			
			
			mp.addObject(table2);					        
	        
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
