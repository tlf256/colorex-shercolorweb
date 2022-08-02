package com.sherwin.shercolor.customershercolorweb.util;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CustWebCustomerProfile;
import com.sherwin.shercolor.common.domain.CustWebDrawdownLabelProfile;
import com.sherwin.shercolor.common.domain.FormulaConversion;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.DrawdownLabelService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;
import com.sherwin.shercolor.util.domain.SwMessage;

/* Boxable is a library that can be used to easily create tables in pdf documents. http://dhorions.github.io/boxable/
 * for examples of boxable, go to:  https://github.com/dhorions/boxable/blob/master/src/test/java/be/quodlibet/boxable/TableTest.java
 */
import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;
import be.quodlibet.boxable.line.LineStyle;

/* 
 * Important Comment - Adobe PDF/iText does not process nulls embedded in the generated label causing an open error in PDF.  
 * A message box titled OLE Exception with message "Cannot create the in-place editor." occurs.  it is likely a null 
 * string field passed from the request object into the label.  If a change is made to request object, look specifically at 
 * a change for a potential cause of label PDF open failure.
 * */

@Service
public class ShercolorLabelPrintImpl implements ShercolorLabelPrint{
	static Logger logger = LogManager.getLogger(ShercolorLabelPrintImpl.class);

	DrawdownLabelService drawdownLabelService;
	
	CustomerService customerService;
	
	ColorMastService colorMastService;
	
	FormulationService formulationService;

	public ShercolorLabelPrintImpl(DrawdownLabelService drawdownLabelService, CustomerService customerService, ColorMastService colorMastService, FormulationService formulationService) {
		this.drawdownLabelService = drawdownLabelService;
		this.customerService = customerService;
		this.colorMastService = colorMastService;
		this.formulationService = formulationService;
	}
	
	//Creating exception string
	String errorLocation = "";
	String colorNameLog = "null";
	String productNbrLog = "null";
	String exceptionDetail = ": [ {},  Color: {} Product: {}, Method Location: [ {} ] ]";
		
	// Create a new empty document
	PDDocument document = new PDDocument();
	String filename;
	RequestObject reqObj;
	boolean success = false;

	// Set all fonts to bold  to resolve light printing issue.
	
	static final PDFont helvetica    = PDType1Font.HELVETICA_BOLD;
	PDFont fontBold 	= helvetica;
	PDFont unicode = null; //font to use for unicode chars


	// Cell Alignment Variables
	HorizontalAlignment haLeft = HorizontalAlignment.LEFT;
	HorizontalAlignment haRight = HorizontalAlignment.RIGHT;
	HorizontalAlignment haCenter = HorizontalAlignment.CENTER;
	VerticalAlignment vaMiddle = VerticalAlignment.MIDDLE;
				

	public void drawLabel(List<FormulaIngredient> formulaIngredients, String partMessage, String labelType, String canType, String clrntAmtList) {
		switch(labelType) {
		case "storeLabel":
			drawStoreLabelPdf(formulaIngredients, partMessage);
			break;
		case "selfTintCustLabel":
			drawSelfTintCustLabelPdf(formulaIngredients, partMessage);
			break;
		case "drawdownStoreLabel":
			drawDrawdownStoreLabel(formulaIngredients, partMessage);
			break;
		case "sampleCanLabel":
			drawDrawdownCanLabel(partMessage, canType, clrntAmtList);
			break;
		default:
			logger.warn("Undefined labelType detected: {}", labelType);
			break;
		}
	}
	
	// New method neccessary to help merge the new correction formula so that the labels that print when the containers are accepted
	// will display the new corrected formula. This partially merges the formula into the starting correction form
	public List<FormulaIngredient> mergeCorrectionIngredients(List<FormulaIngredient> ingredients, List<Map<String,Object>> correctionShotList) {
		String clrntSysId = ingredients.get(0).getClrntSysId();
		FormulaConversion formulaConverter = formulationService.buildFormulaConversion(clrntSysId);
		for (Map<String,Object> correction : correctionShotList) {
			FormulaIngredient item = new FormulaIngredient();
			item.setClrntSysId(clrntSysId);
			item.setTintSysId(correction.get("code").toString());
			item.setShots(Integer.parseInt(correction.get("shots").toString()));
			item.setShotSize(Integer.parseInt(correction.get("uom").toString()));
			ingredients.add(item);
		}
		
		formulationService.fillIngredientInfoFromTintSysId(ingredients, formulaConverter);
		formulationService.convertShotsToIncr(ingredients, formulaConverter);
		
		return ingredients;
	}
	
	public void createLabelPdf(RequestObject reqObj, String printLabelType, String printOrientation, String canType, String clrntAmtList, boolean isCorrectionDispense, List<Map<String,Object>> correctionShotList) {
		this.filename = "label.pdf";
		this.reqObj = reqObj;


		String partMessage = null;

		try {
			if (reqObj != null) {
				if (reqObj.getColorName() != null) {
					colorNameLog = Encode.forJava("[ " + reqObj.getColorName() + " ]");
				}
				if (reqObj.getProdNbr() != null) {
					productNbrLog = Encode.forJava("[ " + reqObj.getProdNbr() + " ]");
				}
			}
			
			if (printLabelType.equals("drawdownLabel")) {
				drawDrawdownLabelPdf();
				document.save(filename);
				closeDocument();
				return;
			}
				
			// Get formula ingredients (colorants) for processing.
			errorLocation = "Retrieving Formula Ingredients";
			List<FormulaIngredient> listFormulaIngredients = new ArrayList<>();
			for (FormulaIngredient displayIngredient : reqObj.getDisplayFormula().getIngredients()) {
				listFormulaIngredients.add(displayIngredient);
			}
			// Merge correction ingredients if the dispense is from an accepted correction dispense
			if (isCorrectionDispense) {
				errorLocation = "Merging Correction Ingredients";
				listFormulaIngredients = mergeCorrectionIngredients(listFormulaIngredients, correctionShotList);
			}
				
			// Determine the number of ingredient (colorant) lines in the formula.
			int formulaSize = reqObj.getDisplayFormula().getIngredients().size();

			// 5 or less lines in a formula - pass the one part label formula to formatting method.
			if (formulaSize <= 5){
				partMessage = " ";
				errorLocation = "Drawing Label";
				drawLabel(listFormulaIngredients, partMessage, printLabelType, canType, clrntAmtList);
			} else {
				// Split the label colorant lines for 2 separate labels and proceed to create 2 labels that print simultaneously.
				int counter = 0;
				List<FormulaIngredient>partAListFormulaIngredients = new ArrayList<>(); 
				List<FormulaIngredient>partBListFormulaIngredients = new ArrayList<>();
				for(FormulaIngredient ingredient : listFormulaIngredients){
					if (counter <= 4 ){
						partAListFormulaIngredients.add(ingredient);
					} else {
						partBListFormulaIngredients.add(ingredient);
					}
					counter++;
				}
				// Write the part A label on first page.
				if(partAListFormulaIngredients != null && !partAListFormulaIngredients.isEmpty()){
					partMessage = "* PART A - SEE PART B OF FORMULA *";
					errorLocation = "Drawing Label Part A";
					drawLabel(partAListFormulaIngredients, partMessage, printLabelType, canType, clrntAmtList);
				}	
				// Skip to next page to write part B label.

				// Write the part B label on second page.
				if(partBListFormulaIngredients != null && !partBListFormulaIngredients.isEmpty()){
					partMessage = "* PART B - SEE PART A OF FORMULA *";
					errorLocation = "Drawing Label Part B";
					drawLabel(partBListFormulaIngredients, partMessage, printLabelType, canType, clrntAmtList);
				}	
			}
			// Label Pdf is completed.  1 or 2 labels will print.  Close the document.
			// Save the results and ensure that the document is properly closed:			
			document.save(filename);
		}

		catch(IOException ie) {
			logger.error(exceptionDetail, ie.getMessage(), colorNameLog, productNbrLog, errorLocation);

		}
		catch(RuntimeException re){
			logger.error(exceptionDetail, re.getMessage(), colorNameLog, productNbrLog, errorLocation);

		}
		finally {
			closeDocument();
		}
	}

	private void closeDocument() {
		try {
			errorLocation = "Closing Document";
			document.close();
		} catch (IOException e) {
			logger.error(exceptionDetail, e.getMessage(), colorNameLog, productNbrLog, errorLocation);

		}
	}
	private void drawStoreLabelPdf(List<FormulaIngredient> listFormulaIngredients, String partMessage ) {
		// Create a new blank page and add it to the document
		PDPage page = new PDPage();

		// Create the 2" x 4" document.
		// step 1 - width and height - 2 (144) x 4 (288) inches.  One inch = 72 points.
		page.setMediaBox(new PDRectangle(0, 0, 144f, 288f));
		PDPageContentStream content = null;

		try{
			
			errorLocation = "Opening Content Stream";
			content = new PDPageContentStream(document, page);
			
			//Setup table. Draw lines must be true to allow background colors of cells to be filled.
			errorLocation = "Table Build";
			BaseTable table = createTopTable(page, true, true);
			
			

			errorLocation = "Name and Date";
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
			String strDate = sdf.format(date);
			Row<PDPage> row = createRow(table, 8);
			

			Cell<PDPage> cell = createCell(row, 75, "Sherwin-Williams " + reqObj.getCustomerID(), haLeft, vaMiddle);
			cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 0, 0, Color.WHITE, 0);
			//Order Date
			cell = createCell(row, 25, strDate, haRight, vaMiddle);
			cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 2f, 0, Color.WHITE, 0);
			
			errorLocation = "Order Number and phone number";
			row = createRow(table, 8);
			//Future place for store phone number.
			cell = createCell(row, 50, "XXX-XXX-XXXX", haLeft, vaMiddle);
			cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 1f, 0, 1f, Color.WHITE, 0);
			//Order Number
			cell = createCell(row, 50, "Order # " + Integer.toString(reqObj.getControlNbr()), haRight, vaMiddle);
			cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 1f, 2f, 1f, Color.WHITE, 0);			

			errorLocation = "Interior/Exterior";
			row = createRow(table, 8);
			cell = createCell(row, 50, reqObj.getIntExt(), haLeft, vaMiddle);
			cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 2f, 0, Color.WHITE, 0);

			cell = createCell(row, 50, reqObj.getKlass(), haRight, vaMiddle);
			cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 2f, 0, Color.WHITE, 0);
			
			
			setQualityAndCompositeRow(table);
			
			errorLocation = "Finish & Tinter Type";
			row = createRow(table, 8);
			cell = createCell(row, 50, reqObj.getFinish(), haLeft, vaMiddle);
			cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 0, 2f, Color.WHITE, 0);
			
			if(reqObj.getTinter() == null) {
				cell = createCell(row, 50, "STANDALONE", haRight, vaMiddle);
			} else {
				cell = createCell(row, 50, reqObj.getTinter().getModel(), haRight, vaMiddle);
			}
			cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 0, 2f, Color.WHITE, 0);
			
			errorLocation = "Color I.D. & Name";
			String rowData = setColorIdAndNameRow();
			row = createRow(table, 12);
			cell = createCell(row, 100, rowData, haCenter, vaMiddle);
			cellFontAndColorSettings(cell, 10, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 0, 0, Color.WHITE, 0);
						
			errorLocation = "Formula Type";
			row = createRow(table, 8);
			cell = createCell(row, 100, reqObj.getDisplayFormula().getSourceDescr(), haCenter, vaMiddle);
			if (reqObj.getDisplayFormula().getSourceDescr().equalsIgnoreCase("VINYL SAFE FORMULA")) {
				cellFontAndColorSettings(cell, 8, Color.WHITE, Color.BLACK);
				cellPaddingAndBorderSettings(cell, 1f, 1f, 1f, 1f, Color.WHITE, 0);
			} else {
				cellFontAndColorSettings(cell, 8, Color.BLACK, Color.WHITE);
				cellPaddingAndBorderSettings(cell, 0, 0, 0, 1f, Color.WHITE, 0);
			}
			
			row = createRow(table, 8);
			setStandardFormulaTable(listFormulaIngredients, table, row, 8);
			
			if(partMessage != null) {
				row = createRow(table, 8);
				cell = createCell(row, 100, partMessage, haCenter, vaMiddle);
				cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
				cellPaddingAndBorderSettings(cell, 0, 0, 0, 0, Color.WHITE, 0);
			}			
			
			errorLocation = "Size and Base";
			// Abbreviating base type name to 16 characters to keep font size on label line.
			if (reqObj.getBase().length() > 15)
				reqObj.setBase(reqObj.getBase().substring(0, 16));
			row = createRow(table, 8);
			cell = createCell(row, 50, reqObj.getSizeText(), haLeft, vaMiddle);
			cellFontAndColorSettings(cell, 8, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 0, 0, Color.WHITE, 0);
			
			cell = createCell(row, 50, reqObj.getBase(), haRight, vaMiddle);
			cellFontAndColorSettings(cell, 8, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 4f, 0, Color.WHITE, 0);
			
			errorLocation = "Sales and Product Number";
			row = createRow(table, 8);
			cell = createCell(row, 50, reqObj.getProdNbr(), haLeft, vaMiddle);
			cellFontAndColorSettings(cell, 8, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 0, 0, Color.WHITE, 0);
			
			cell = createCell(row, 50, reqObj.getSalesNbr(), haRight, vaMiddle);
			cellFontAndColorSettings(cell, 8, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 0, 4f, 0, Color.WHITE, 0);

			errorLocation = "Room Type";
			row = createRow(table, 8);
			cell = createCell(row, 100, reqObj.getRoomByRoom(), haCenter, vaMiddle);
			cellFontAndColorSettings(cell, 8, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 4f, 0, 2f, Color.WHITE, 0);
			
			errorLocation = "Formula Messages";
			setFormulaMessageRowsForStores(table);
			
			errorLocation = "Non-Return msg";
			row = createRow(table, 8);
			cell = createCell(row, 100, "Non Returnable Tinted Color", haCenter, vaMiddle);
			cellFontAndColorSettings(cell, 8, Color.BLACK, Color.WHITE);
			cellPaddingAndBorderSettings(cell, 0, 4f, 0, 4f, Color.WHITE, 0);
			
			errorLocation = "QR Code & Order Number";
			createQRCode(content);
			String qrCodeChars = String.format("%s-%08d-%03d", reqObj.getCustomerID(), reqObj.getControlNbr(), reqObj.getLineNbr());
			addCenteredText(content, qrCodeChars, fontBold, 8, page, 4.0f);
			
			table.draw();
			content.close();
			document.addPage(page);
			success = true;
			
		} catch(IOException ie) {
			logger.error(exceptionDetail, ie.getMessage(), colorNameLog, productNbrLog, errorLocation);

		} catch(IllegalArgumentException ex) {
			logger.error(exceptionDetail, ex.getMessage(), colorNameLog, productNbrLog, errorLocation);

		} catch(RuntimeException re){
			logger.error(exceptionDetail, re.getMessage(), colorNameLog, productNbrLog, errorLocation);

		}
	}
	
	private void drawSelfTintCustLabelPdf(List<FormulaIngredient> listFormulaIngredients, String partMessage ) {
		int rowHeight;
		// Create a new blank page and add it to the document
		PDPage page = new PDPage();

		// Create the 2" x 4" document.
		// step 1 - width and height - 2 (144) x 4 (288) inches.  One inch = 72 points.
		page.setMediaBox(new PDRectangle(0, 0 , 144f, 288f));
		PDPageContentStream content = null;

		try{
			
			errorLocation = "Opening Content Stream";
			content = new PDPageContentStream(document, page);
			
			//Setup table
			errorLocation = "Table Build";
			BaseTable table = createTopTable(page, true, false);
			
			//Customer Name and Order Date
			errorLocation = "Name and Date";
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
			String strDate = sdf.format(date);
			createTwoColumnRow(table, 7, 8, 66, haLeft, vaMiddle, reqObj.getCustomerName(), 34, haRight, vaMiddle, strDate);
			
			//Order Number
			errorLocation = "Order Number";
			createTwoColumnRow(table, 7, 7, 50, haLeft, vaMiddle, "", 50, haRight, vaMiddle, "Order # " + Integer.toString(reqObj.getControlNbr()));
			
			//Blank line
			createBlankRow(table, 2);

			//Use Interior/Exterior
			errorLocation = "Interior/Exterior";
			createTwoColumnRow(table, 6, 8, 30, haLeft, vaMiddle, reqObj.getIntExt(), 70, haRight, vaMiddle, reqObj.getKlass());
			
			//Quality and Composite
			setQualityAndCompositeRow(table);
			
			//Finish & Tinter Type
			errorLocation = "Finish & Tinter Type";
			// 01/20/2017 - Begin Finish and Tinter Type.
			if(reqObj.getTinter() == null) {
				TinterInfo tinter = new TinterInfo();
				tinter.setModel("Standalone");
				reqObj.setTinter(tinter);
			}
			createTwoColumnRow(table, 6, 8, 30, haLeft, vaMiddle, reqObj.getFinish(), 70, haRight, vaMiddle, reqObj.getTinter().getModel());
			
			//Color Id, Color Name and Formula Type
			errorLocation = "Color I.D. & Name";
			String rowData = setColorIdAndNameRow();
			createOneColumnRow(table, 10, 12, 100, haCenter, vaMiddle, rowData);

			errorLocation = "Formula Type";
			createOneColumnRow(table, 8, 8, 100, haCenter, vaMiddle, reqObj.getDisplayFormula().getSourceDescr());
			
			//Formula Heading and 5 Colorant Lines maximum.
			rowHeight = 8;
			Row<PDPage> row = table.createRow(rowHeight);
			setStandardFormulaTable(listFormulaIngredients, table, row, rowHeight);
			
			//Product Information
			errorLocation = "Size and Base";
			// Abbreviating base type name to 16 characters to keep font size on label line.
			if (reqObj.getBase().length() > 15)
				reqObj.setBase(reqObj.getBase().substring(0, 16));
			createTwoColumnRow(table, 8, 8, 50, haLeft, vaMiddle, reqObj.getSizeText(), 50, haRight, vaMiddle, reqObj.getBase());

			errorLocation = "Sales and Product Number";
			createTwoColumnRow(table, 8, 8, 50, haLeft, vaMiddle, reqObj.getProdNbr(), 50, haRight, vaMiddle, reqObj.getSalesNbr());

			//Formula Messages
			errorLocation = "Formula Messages";
			setFormulaMessagesRows(table,partMessage);

			//Job Field
			List<JobField> listJobField = reqObj.getJobFieldList();
			setJobFieldRows(table, listJobField);
			
			// Bar Code and Order and Line Numbers (part of bar code).
			errorLocation = "Bar Code & Order Number";
			createBarcode(content);
			String barCodeChars = String.format("%08d-%03d",reqObj.getControlNbr(), 1);
			addCenteredText(content,barCodeChars,fontBold,8,page,4.0f);
			
			//Create label
			table.draw();
			content.close();
			document.addPage( page );
			success = true;
			
		} catch(IOException ie) {
			logger.error(exceptionDetail, ie.getMessage(), colorNameLog, productNbrLog, errorLocation);

		} catch(IllegalArgumentException ex) {
			logger.error(exceptionDetail, ex.getMessage(), colorNameLog, productNbrLog, errorLocation);

		} catch(RuntimeException re){
			logger.error(exceptionDetail, re.getMessage(), colorNameLog, productNbrLog, errorLocation);
		}
	}
	
	private void drawDrawdownLabelPdf() {
		// Create a new blank page and add it to the document
		PDPage page = new PDPage();

		// Create the 4" x 2" document.
		// step 1 - width and height - 4 (288) x 2 (144) inches.  One inch = 72 points.
		page.setMediaBox(new PDRectangle(0, 0 , 288f, 144f));
		PDPageContentStream content = null;
		
		try{

			errorLocation = "Opening Content Stream";
			content = new PDPageContentStream(document, page);
			
			//setup Table
			errorLocation = "Table Setup";
			BaseTable table = createTopTable(page, true, false);
			
			
			int fontSize = 9;
			int rowHeight = 10;
			int cell1Width = 25;
			int cell2Width = 75;
			
			// Must retrieve the jobFieldList and the Customer ID's Label Profile in order to query the below fields and
			// pair the information up with their corresponding job field column
			errorLocation = "Retrieving Job Field List";
			List<JobField> jobFieldList = reqObj.getJobFieldList();
			List<CustWebDrawdownLabelProfile> labelProfileList = drawdownLabelService.listDrawdownLabelProfilesForCustomer(reqObj.getCustomerID());
			errorLocation = "Setting Label Info Based on Customer Job Field Sequence Nbr";
			String customer = jobFieldList.get(labelProfileList.get(0).getJobFieldDataSourceSeqNbr()-1).getEnteredValue();
			String storeCCN = jobFieldList.get(labelProfileList.get(1).getJobFieldDataSourceSeqNbr()-1).getEnteredValue();
			String controlNbr = jobFieldList.get(labelProfileList.get(2).getJobFieldDataSourceSeqNbr()-1).getEnteredValue();
			String jobDescr = jobFieldList.get(labelProfileList.get(3).getJobFieldDataSourceSeqNbr()-1).getEnteredValue();
			String projectInfo = jobFieldList.get(labelProfileList.get(4).getJobFieldDataSourceSeqNbr()-1).getEnteredValue();
			String schedule = jobFieldList.get(labelProfileList.get(5).getJobFieldDataSourceSeqNbr()-1).getEnteredValue();
			String roomByRoom = reqObj.getRoomByRoom();
			
			errorLocation = "Customer";
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Customer:",cell2Width,haLeft,vaMiddle,customer);
			errorLocation = "Store CCN";
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Store CCN:",cell2Width,haLeft,vaMiddle,storeCCN);
			errorLocation = "Date Prepared and Control Nbr";
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
			String strDate = sdf.format(date);
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Date Prepared:",
							   cell2Width,haLeft,vaMiddle,strDate + "  " + "Control Number: " + controlNbr);
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"",cell2Width,haLeft,vaMiddle,"");
			errorLocation = "Job";
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Job:",cell2Width,haLeft,vaMiddle,jobDescr);
			errorLocation = "Project Info";
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Project Info:",cell2Width,haLeft,vaMiddle,projectInfo);
			errorLocation = "Schedule";
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Schedule:",cell2Width,haLeft,vaMiddle,schedule);
			errorLocation = "Room/Use"; //KXK PSCWEB-703
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Room/Use:",cell2Width,haLeft,vaMiddle,roomByRoom);
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"",cell2Width,haLeft,vaMiddle,"");
			errorLocation = "Color";
			String colorPrint = "";
			if (reqObj.getColorComp().equals("SHERWIN-WILLIAMS")) {
				colorPrint = "SW " + reqObj.getColorID() + " " + reqObj.getColorName();
			} else {
				colorPrint = reqObj.getColorID() + " " + reqObj.getColorName();
			}
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Color:",
							   cell2Width,haLeft,vaMiddle,colorPrint);
			errorLocation = "Product";
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Product:",
							   cell2Width,haLeft,vaMiddle,reqObj.getQuality() + " " + reqObj.getFinish() + " " + reqObj.getBase() + " " + reqObj.getProdNbr());
			
			table.draw();
			
			errorLocation = "Closing Content Stream";
			content.close();
			
			document.addPage( page );
			success = true;
			
		} catch(IllegalArgumentException ex) {
			logger.error(exceptionDetail, ex.getMessage(), colorNameLog, productNbrLog, errorLocation);

		} catch(IOException ie) {
			logger.error(exceptionDetail, ie.getMessage(), colorNameLog, productNbrLog, errorLocation);

		} catch (Exception e) {
			logger.error(exceptionDetail, e.getMessage(), colorNameLog, productNbrLog, errorLocation);
			
		}
	}
	
	private void drawDrawdownStoreLabel(List<FormulaIngredient> listFormulaIngredients, String partMessage ) {

		int rowHeight = 2;
		// Create a new blank page and add it to the document
		PDPage page = new PDPage();

		// Create the 2" x 4" document.
		// step 1 - width and height - 2 (144) x 4 (288) inches.  One inch = 72 points.
		page.setMediaBox(new PDRectangle(0, 0 , 144f, 288f));
		PDPageContentStream content = null;

		try{
			
			content = new PDPageContentStream(document, page);
			
			errorLocation = "Table Build";
			//Setup table
			BaseTable table = createTopTable(page, true, false);
			
			// ================================================================================================================================================
			// 12/07/2020 | Customer Name and Order Date
			errorLocation = "Name and Date";
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
			String strDate = sdf.format(date);
			createTwoColumnRow(table, 7, 8, 66, haLeft, vaMiddle, reqObj.getCustomerName(), 34, haRight, vaMiddle, strDate);
			
			// ================================================================================================================================================
			// 12/07/2020 | Optional Field - Not yet implemented and Order Number
			errorLocation = "Order Number";
			createTwoColumnRow(table, 7, 7, 50, haLeft, vaMiddle, reqObj.getCustomerID(), 50, haRight, vaMiddle, "Order # " + Integer.toString(reqObj.getControlNbr()));
			
			// ================================================================================================================================================
			// 12/07/2020 | Blank line
			createBlankRow(table,2);

			// ================================================================================================================================================
			// 12/07/2020 | Use Interior/Exterior
			errorLocation = "Interior Exterior";
			createTwoColumnRow(table, 6, 8, 30, haLeft, vaMiddle, reqObj.getIntExt(), 70, haRight, vaMiddle, reqObj.getKlass());
			
			// ================================================================================================================================================
			// 12/07/2020 | Quality and Composite
			errorLocation = "Quality and Composite";
			setQualityAndCompositeRow(table);
			
			// ================================================================================================================================================
			// 12/07/2020 | Finish & Tinter Type
			errorLocation = "Finish & Tinter Type";
			// 01/20/2017 - Begin Finish and Tinter Type.
			if(reqObj.getTinter() == null) {
				TinterInfo tinter = new TinterInfo();
				tinter.setModel("Standalone");
				reqObj.setTinter(tinter);
			}
			createTwoColumnRow(table, 6, 8, 30, haLeft, vaMiddle, reqObj.getFinish(), 70, haRight, vaMiddle, reqObj.getTinter().getModel());
			// ================================================================================================================================================
			// 12/07/2020 | Color Id, Color Name and Formula Type
			errorLocation = "Color I.D. & Name";
			String rowData = setColorIdAndNameRow();
			
			CustWebCustomerProfile custProfile = customerService.getCustWebCustomerProfile(reqObj.getCustomerID());
			CdsColorMast colorMast = colorMastService.read(reqObj.getColorComp(), reqObj.getColorID());
			createOneColumnRow(table, 10, 12, 100, haCenter, vaMiddle, rowData);
			
			if (custProfile.isUseLocatorId() && colorMast != null && colorMast.getLocId() != null) {
				errorLocation = "Locator ID";
				createOneColumnRow(table, 8, 8, 100, haCenter, vaMiddle, colorMast.getLocId());
			}
			
			// ------------------------------------------------------------------------------------------------------------------------------------------------
			errorLocation = "Formula Type";
			createOneColumnRow(table, 8, 8, 100, haCenter, vaMiddle, reqObj.getDisplayFormula().getSourceDescr());
			
			// ================================================================================================================================================
			// 12/07/2020 | Formula Heading and 5 Colorant Lines maximum.
			
			rowHeight = 8;
			Row<PDPage> row = table.createRow(rowHeight);
			setStandardFormulaTable(listFormulaIngredients, table, row, rowHeight);
			
			// ================================================================================================================================================
			// 12/07/2020 | Product Information
			errorLocation = "Size and Base";
			// Abbreviating base type name to 16 characters to keep font size on label line.
			if (reqObj.getBase().length() > 15)
				reqObj.setBase(reqObj.getBase().substring(0, 16));
			createTwoColumnRow(table, 8, 8, 50, haLeft, vaMiddle, reqObj.getSizeText(), 50, haRight, vaMiddle, reqObj.getBase());
			//-------------------------------------------------------------------------------------------------------------------------------------------------
			errorLocation = "Sales and Product Number";
			createTwoColumnRow(table, 8, 8, 50, haLeft, vaMiddle, reqObj.getProdNbr(), 50, haRight, vaMiddle, reqObj.getSalesNbr());

			// ================================================================================================================================================
			// 12/07/2020 | Formula Messages
			errorLocation = "Formula Messages";
			setFormulaMessagesRows(table,partMessage);

			//------------------------------djm-------------------------
			//KXK PSCWEB-703
			errorLocation = "Job Field List";
			List<JobField> listJobField = new ArrayList<>(reqObj.getJobFieldList());
			JobField roomByRoom = new JobField();
			roomByRoom.setScreenLabel("Room/Use");
			roomByRoom.setEnteredValue(reqObj.getRoomByRoom());

			//add roomByRoom beneath Schedule (2nd to last) if possible
			if(listJobField.size() > 0) {
				listJobField.add(listJobField.size()-1, roomByRoom); 
			} else {
				listJobField.add(roomByRoom);
			}
			setJobFieldRows(table, listJobField);
			
			// ================================================================================================================================================

			table.draw();


			content.close();
			document.addPage( page );
			success = true;
			
		} catch(IOException ie) {
			logger.error(exceptionDetail, ie.getMessage(), colorNameLog, productNbrLog, errorLocation);
			
		} catch(IllegalArgumentException ex) {
			logger.error(exceptionDetail, ex.getMessage(), colorNameLog, productNbrLog, errorLocation);
			
		} catch(Exception e){
			logger.error(exceptionDetail, e.getMessage(), colorNameLog, productNbrLog, errorLocation);
		}
	}
	
	public void drawDrawdownCanLabel(String partMessage, String canType, String clrntAmtList ) {
		// Create a new blank page and add it to the document
		PDPage page = new PDPage();

		// Create the 2" x 4" document.
		// step 1 - width and height - 2 (144) x 4 (288) inches.  One inch = 72 points.
		page.setMediaBox(new PDRectangle(0, 0 , 144f, 288f));
		PDPageContentStream content = null;

		try{
			
			errorLocation = "Opening Content Stream";
			content = new PDPageContentStream(document, page);
			
			errorLocation = "Table Build";
			//Setup table
			BaseTable table = createTopTable(page, true, false);
			
			// ================================================================================================================================================
			// 12/07/2020 | Customer Name and Order Date
			errorLocation = "Name and Date";
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
			String strDate = sdf.format(date);
			createTwoColumnRow(table, 7, 8, 66, haLeft, vaMiddle, reqObj.getCustomerName(), 34, haRight, vaMiddle, strDate);
			
			// ================================================================================================================================================
			// 12/07/2020 | Optional Field - Not yet implemented and Order Number
			errorLocation = "Order Number";
			createTwoColumnRow(table, 7, 7, 50, haLeft, vaMiddle, "", 50, haRight, vaMiddle, "Order # " + Integer.toString(reqObj.getControlNbr()));
			
			// ================================================================================================================================================
			// 12/07/2020 | Blank line
			createBlankRow(table,2);

			// ================================================================================================================================================
			// 12/07/2020 | Use Interior/Exterior
			errorLocation = "Interior Exterior";
			createTwoColumnRow(table, 6, 8, 30, haLeft, vaMiddle, reqObj.getIntExt(), 70, haRight, vaMiddle, reqObj.getKlass());
			
			// ================================================================================================================================================
			// 12/07/2020 | Quality and Composite
			errorLocation = "Quality and Composite";
			setQualityAndCompositeRow(table);
			
			// ================================================================================================================================================
			// 12/07/2020 | Finish & Tinter Type
			errorLocation = "Finish & Tinter Type";
			// 01/20/2017 - Begin Finish and Tinter Type.
			if(reqObj.getTinter() == null) {
				TinterInfo tinter = new TinterInfo();
				tinter.setModel("Standalone");
				reqObj.setTinter(tinter);
			}
			createTwoColumnRow(table, 6, 8, 30, haLeft, vaMiddle, reqObj.getFinish(), 70, haRight, vaMiddle, reqObj.getTinter().getModel());
			// ================================================================================================================================================
			// 12/07/2020 | Color Id, Color Name and Formula Type
			errorLocation = "Color I.D. & Name";
			String rowData = setColorIdAndNameRow();
			createOneColumnRow(table, 10, 12, 100, haCenter, vaMiddle, rowData);
			// ------------------------------------------------------------------------------------------------------------------------------------------------
			errorLocation = "Formula Type";
			createOneColumnRow(table, 8, 8, 100, haCenter, vaMiddle, reqObj.getDisplayFormula().getSourceDescr());
			
			// ================================================================================================================================================
			// 12/07/2020 | Formula Heading and 5 Colorant Lines maximum.
			errorLocation = "Formula Heading and Colorant Lines";
			createTwoColumnRow(table, 8, 8, 50, haLeft, vaMiddle, reqObj.getClrntSys() + " Colorant", 50, haLeft, vaMiddle, reqObj.getDisplayFormula().getIncrementHdr().get(0));
			int numIngredients = 0;			
			String[] clrntAmtString = clrntAmtList.split(",");
			List<String> clrntIngedientsAndAmts = Arrays.asList(clrntAmtString);
			List<String> clrntAmtDouble = new ArrayList<>();
			List<String> clrntIngredientList = new ArrayList<>();
			int counter = 0;
			for (int i = 0; i < clrntIngedientsAndAmts.size(); i++) {
				if(!clrntIngedientsAndAmts.get(i).trim().equals("") && counter % 2 == 0) {
					clrntIngredientList.add(clrntIngedientsAndAmts.get(i).replace("-", " "));
				} else if (!clrntIngedientsAndAmts.get(i).trim().equals("") && counter % 2 != 0) {
					clrntAmtDouble.add(String.format("%,.8f", Double.parseDouble(clrntIngedientsAndAmts.get(i))));
				}
				counter++;
			}
			
			for(int i = 0; i < clrntIngredientList.size(); i++) {
				createTwoColumnRow(table, 8, 8, 50, haLeft, vaMiddle, clrntIngredientList.get(i), 50, haLeft, vaMiddle, clrntAmtDouble.get(i));
				numIngredients++;
			}
			
			for(int i = numIngredients; i < 5; i++) {
				createBlankRow(table, 8);
			}
			
			// ================================================================================================================================================
			// 12/07/2020 | Product Information
			errorLocation = "Size and Base";
			// Abbreviating base type name to 16 characters to keep font size on label line.
			if (reqObj.getBase().length() > 15)
				reqObj.setBase(reqObj.getBase().substring(0, 16));

			createTwoColumnRow(table, 8, 8, 50, haLeft, vaMiddle, canType, 50, haRight, vaMiddle, reqObj.getBase());
			//-------------------------------------------------------------------------------------------------------------------------------------------------
			errorLocation = "Sales and Product Number";
			createTwoColumnRow(table, 8, 8, 50, haLeft, vaMiddle, reqObj.getProdNbr(), 50, haRight, vaMiddle, reqObj.getSalesNbr());

			// ================================================================================================================================================
			// 12/07/2020 | Formula Messages
			errorLocation = "Formula Messages";
			setFormulaMessagesRows(table,partMessage);

			//------------------------------djm-------------------------
			List<JobField> listJobField = reqObj.getJobFieldList();
			setJobFieldRows(table, listJobField);
			
			// ================================================================================================================================================
			// 12/07/2020 | Bar Code and Order and Line Numbers (part of bar code).
			errorLocation = "Bar Code & Order Number";

			createBarcode(content);
			String barCodeChars = String.format("%08d-%03d",reqObj.getControlNbr(), 1);
			addCenteredText(content,barCodeChars,fontBold,8,page,4.0f);
			
			// ================================================================================================================================================

			table.draw();
			content.close();
			document.addPage( page );
			success = true;
			
		}  catch(IOException ie) {
			logger.error(exceptionDetail, ie.getMessage(), colorNameLog, productNbrLog, errorLocation);

		} catch(IllegalArgumentException ex) {
			logger.error(exceptionDetail, ex.getMessage(), colorNameLog, productNbrLog, errorLocation);

		} catch(Exception e){
			logger.error(exceptionDetail, e.getMessage(), colorNameLog, productNbrLog, errorLocation);

		}
	}
	
	// ==========================================================================================================================================
	// Label Row Creation Methods
	// 11/24/2020 - Create Row methods to allow greater flexibility when making different labels. Parameters are necessary
	//				to help assist in providing more instruction and less static values 
	//
	// 06/13/2022 - createRow, createCell, cellFontAndColorSettings, and cellPaddingAndBorderSettings added for more fine-grained control.
	//
	// ==========================================================================================================================================
	
	private Row<PDPage> createRow(BaseTable table, int rowHeight) {
		return table.createRow(rowHeight);
	}
	
	private Cell<PDPage> createCell(Row<PDPage> row, int cellWidth, String cellData, HorizontalAlignment cellHAlign, VerticalAlignment cellVAlign) {
		return row.createCell(cellWidth, cellData, cellHAlign, cellVAlign);
	}
	
	private void cellFontAndColorSettings(Cell<PDPage> cell, int fontSize, Color textColor, Color backgroundColor) {
		try {
			String text = cell.getText();
	
			checkForUnicode(cell, text);
			cell.setFontBold(fontBold);
			cell.setFont(fontBold);
			cell.setTextColor(textColor);
			cell.setFillColor(backgroundColor);
			cell.setFontSize(fontSize);
			
		} catch(Exception e) {
			logger.error(exceptionDetail, e.getMessage(), colorNameLog, productNbrLog, errorLocation);
		}		
	}
	
	private void cellPaddingAndBorderSettings(Cell<PDPage> cell, float leftPadding, float topPadding, float rightPadding, float bottomPadding, Color borderColor, float borderWidth) {
		cell.setLeftPadding(leftPadding);
		cell.setTopPadding(topPadding);
		cell.setRightPadding(rightPadding);
		cell.setBottomPadding(bottomPadding);
		
		LineStyle lineStyle = new LineStyle(borderColor, borderWidth);
		cell.setTopBorderStyle(lineStyle);
		cell.setBottomBorderStyle(lineStyle);
		cell.setLeftBorderStyle(lineStyle);
		cell.setRightBorderStyle(lineStyle);
	}

	private void createTwoColumnRow(BaseTable table, int fontSize, int rowHeight,
									int cell1Width, HorizontalAlignment cell1HAlign, VerticalAlignment cell1VAlign, String cell1Data,
									int cell2Width, HorizontalAlignment cell2HAlign, VerticalAlignment cell2VAlign, String cell2Data) {
		// ROW
		Row<PDPage> row = table.createRow(rowHeight);
		
		// CELL 1
		Cell<PDPage> r1cell1= row.createCell(cell1Width, cell1Data, cell1HAlign, cell1VAlign);
		cellSettings(r1cell1,fontSize,rowHeight);
		// CELL 2
		Cell<PDPage> r1cell2 = row.createCell(cell2Width, cell2Data, cell2HAlign, cell2VAlign);
		cellSettings(r1cell2,fontSize,rowHeight);
	}
	
	private void createOneColumnRow(BaseTable table, int fontSize, int rowHeight,
			int cellWidth, HorizontalAlignment cellHAlign, VerticalAlignment cellVAlign, String cellData) {
		// ROW
		Row<PDPage> row = table.createRow(rowHeight);
				
		// CELL
		Cell<PDPage> cell = row.createCell(cellWidth, cellData, cellHAlign, cellVAlign);
		cellSettings(cell,fontSize,rowHeight);
	}
	
	private void createBlankRow(BaseTable table, int rowHeight) {
		table.createRow(rowHeight);
	}
	
	public void setQualityAndCompositeRow(BaseTable table) {
		errorLocation = "Quality & Composite";
		int setQualityParm = 50;
		int setCompositeParm = 50;
		int totalCharsLength = 0;
		if(reqObj.getQuality()==null) {reqObj.setQuality("");}
		if(reqObj.getComposite()==null) {reqObj.setComposite("");}
		totalCharsLength = reqObj.getQuality().length() + reqObj.getComposite().length();
		if (totalCharsLength >= 35 || reqObj.getQuality().length() > 17 || 
				reqObj.getComposite().length() > 17){
			// Calculate with intent to keep the Quality field complete.  36 chars maximum on line.
			Double x = (double) reqObj.getQuality().length() / 35 * 100;
			// Quality and Composite fields as the percent of line on label.
			setQualityParm = x.intValue();
			setCompositeParm = 100 - setQualityParm;
			// Truncate Composite field to ensure it fits in remaining space.
			int calcCompositeLength = 35 - reqObj.getQuality().length();
			// Truncate Composite field for available field size on the line.
			if (reqObj.getComposite().length() > calcCompositeLength){
				reqObj.setComposite(reqObj.getComposite().substring(0,calcCompositeLength));	
			}
		}
		Row<PDPage> row = createRow(table, 8);
		Cell<PDPage> cell = createCell(row, setQualityParm, reqObj.getQuality(), haLeft, vaMiddle);
		cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
		cellPaddingAndBorderSettings(cell, 0, 0, 0, 0, Color.WHITE, 0);
		cell = createCell(row, setCompositeParm, reqObj.getComposite(), haRight, vaMiddle);
		cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
		cellPaddingAndBorderSettings(cell, 0, 0, 2f, 0, Color.WHITE, 0);
				
	}
	
	public String setColorIdAndNameRow() {
		int charsAllowedRemaining = 30;
		String colorIDLabelText = "";
		String colorNameLabelText = "";
		
		// Color i.d. and name.
		// 01/20/2017 - Begin Color I.D. and Color Name field build.
		if (reqObj.getColorID()==null) {reqObj.setColorID("");}
			else{colorIDLabelText = reqObj.getColorID() + " ";}
		if (reqObj.getColorName()==null) {reqObj.setColorName("");}
			else {colorNameLabelText = reqObj.getColorName();}
		
		if (reqObj.getColorType().equals("COMPETITIVE")) {
			// Combine ColorID with the ColorCompPrint ID to make a complete Color ID for Competitive Colors
			colorIDLabelText = "COMP(" + colorMastService.getColorCompPrintId(reqObj.getColorComp()) + ") "
								+ colorIDLabelText;
		}
		

		//Modify input values, replace / and " with -
		if(!StringUtils.isEmpty(colorIDLabelText) && !StringUtils.isEmpty(colorNameLabelText)){
			colorIDLabelText = colorIDLabelText.replaceAll("\"|\\\\|\\~", "-");
			colorNameLabelText = StringEscapeUtils.unescapeHtml4(colorNameLabelText.replaceAll("\"|\\\\|\\~", "-"));
		}
		//Subtract the number of characters from the full ColorID to find out how many characters are left for the colorName
		charsAllowedRemaining = charsAllowedRemaining - colorIDLabelText.length();
		
		// check customer profile
		CustWebCustomerProfile profile = customerService.getCustWebCustomerProfile(reqObj.getCustomerID());
		if (profile != null && profile.getCustomerType().trim().equalsIgnoreCase("DRAWDOWN") ||
				charsAllowedRemaining - colorNameLabelText.length() >= 0){
		// don't truncate for drawdown customers or if the character limit has not been reached
			return colorIDLabelText + colorNameLabelText;
		} else {
			//Trim the Color Name if it exceeds the 30 character allowable limit on the label 
			// (The amount trimmed depends on the colorID and colorName lengths)
			return colorIDLabelText + colorNameLabelText.substring(0,charsAllowedRemaining);
		}
	}
	
	public void setStandardFormulaTable(List<FormulaIngredient> listFormulaIngredients, BaseTable table, Row<PDPage> row, int rowHeight) {
		errorLocation = "Formula Label Line Headings";
		// Formula Label Line Headings
		List<String> listIncrementHdr = reqObj.getDisplayFormula().getIncrementHdr();
		if(!listIncrementHdr.isEmpty()) {
			Cell<PDPage> fcellHead1 =  createFormulaHeading(row,reqObj.getClrntSys() + " Colorant", 52f, 7,  haLeft);
			cellPaddingAndBorderSettings(fcellHead1, 0, 2f, 2f, 0, Color.WHITE, 0);
						
			Cell<PDPage> fcellHead2 =  createFormulaHeading(row,listIncrementHdr.get(0), 11f, 8, haCenter);
			cellPaddingAndBorderSettings(fcellHead2, 0, 0, 2f, 0, Color.WHITE, 0);
			
			Cell<PDPage> fcellHead3 =  createFormulaHeading(row,listIncrementHdr.get(1), 11f,8, haCenter);
			cellPaddingAndBorderSettings(fcellHead3, 0, 0, 2f, 0, Color.WHITE, 0);
			
			Cell<PDPage> fcellHead4 =  createFormulaHeading(row,listIncrementHdr.get(2), 11f,8, haCenter);
			cellPaddingAndBorderSettings(fcellHead4, 0, 0, 2f, 0, Color.WHITE, 0);
			
			Cell<PDPage> fcellHead5 =  createFormulaHeading(row,listIncrementHdr.get(3), 14f,8, haCenter);
			cellPaddingAndBorderSettings(fcellHead5, 0, 0, 2f, 0, Color.WHITE, 0);
		}
		
		// Formula Label Line Item.
		errorLocation = "Formula Label Line Items";
		int lineCtr = 0;
		Cell<PDPage> fcellLine1;
		Cell<PDPage> fcellLine2;
		Cell<PDPage> fcellLine3;
		Cell<PDPage> fcellLine4;
		Cell<PDPage> fcellLine5;
		if(listFormulaIngredients != null && !listFormulaIngredients.isEmpty()){
			// Process each instance of the listFormulaIngredients objects.
			for(FormulaIngredient line : listFormulaIngredients){
				int [] amount = line.getIncrement();
				// Add the available formula lines to the label.
				row = table.createRow(rowHeight);

				fcellLine1 =  createFormulaLine(row,line.getTintSysId() + " " + line.getName(), 51f, 7, haLeft);
				cellPaddingAndBorderSettings(fcellLine1, 0, 0, 2f, 0, Color.WHITE, 0);

				if (! Integer.toString(amount[0]).equals("0")){
					fcellLine2 =  createFormulaLine(row,Integer.toString(amount[0]), 11f, 7, haRight);
				} else {
					fcellLine2 =  createFormulaLine(row,"-", 11f, 7, haRight);
				}
				cellPaddingAndBorderSettings(fcellLine2, 0, 0, 2f, 0, Color.WHITE, 0);

				if (! Integer.toString(amount[1]).equals("0")){
					fcellLine3 =  createFormulaLine(row,Integer.toString(amount[1]), 11f, 7, haRight);
				} else {
					fcellLine3 =  createFormulaLine(row,"-", 11f, 7, haRight);
				}
				cellPaddingAndBorderSettings(fcellLine3, 0, 0, 2f, 0, Color.WHITE, 0);

				if (! Integer.toString(amount[2]).equals("0")){
					fcellLine4 =  createFormulaLine(row,Integer.toString(amount[2]), 11f,7, haRight);
				} else {
					fcellLine4 =  createFormulaLine(row,"-", 11f, 7, haRight);
				}
				cellPaddingAndBorderSettings(fcellLine4, 0, 0, 2f, 0, Color.WHITE, 0);

				if (! Integer.toString(amount[3]).equals("0")){
					fcellLine5 =  createFormulaLine(row,Integer.toString(amount[3]), 14f, 7, haRight);
				} else {
					fcellLine5 =  createFormulaLine(row,"-", 14f,7, haRight);
				}
				cellPaddingAndBorderSettings(fcellLine5, 0, 0, 2f, 0, Color.WHITE, 0);


				lineCtr++;
			}
		}
		
		for (int index = lineCtr; index < 5; index++) {
			createBlankRow(table,rowHeight);
		}
	}
	
	public void setFormulaMessagesRows(BaseTable table, String partMessage) {
		int messageCount = 0;
		if (partMessage != null){
			createOneColumnRow(table, 6,8,100, haCenter, vaMiddle, partMessage);
			messageCount++;
		}
		
		List<SwMessage> listSwMessages = reqObj.getCanLabelMsgs();
		if(listSwMessages != null && !listSwMessages.isEmpty()){
			// Process each instance of the message list objects - maximum 3 messages.
			// Messages include Room by Room (not yet implemented), colorant warning and primer message.
			for(SwMessage message : listSwMessages){
				if (messageCount < 4 && message.getMessage().length() > 0){
					if (message.getMessage().length() > 30) {
						createOneColumnRow(table, 6,8,100, haCenter, vaMiddle, message.getMessage());
					} else {
						createOneColumnRow(table, 7,8,100, haCenter, vaMiddle, message.getMessage());
					}
					messageCount++;
				}
			}
		}
	}
	
	public void setFormulaMessageRowsForStores(BaseTable table) {
		int messageCount = 0;
		Row<PDPage> row;
		Cell<PDPage> cell;
		
		List<SwMessage> listSwMessages = reqObj.getCanLabelMsgs();
		if(listSwMessages != null && !listSwMessages.isEmpty()){
			// Process each instance of the message list objects - maximum 3 messages.
			// Messages include Room by Room (not yet implemented), colorant warning and primer message.
			for(SwMessage message : listSwMessages){
				if (messageCount < 4 && message.getMessage().length() > 0 && !message.getMessage().equalsIgnoreCase("VINYL SAFE FORMULA")){
					row = createRow(table, 8);
					cell = createCell(row, 100, message.getMessage(), haCenter, vaMiddle);
					if (message.getMessage().length() > 30) {
						if(message.getMessage().equalsIgnoreCase("NOT RECOMMENDED FOR USE ON VINYL")) {
							cellFontAndColorSettings(cell, 6, Color.WHITE, Color.BLACK);
							cellPaddingAndBorderSettings(cell, 1f, 1f, 1f, 1f, Color.WHITE, 0);
						} else {
							cellFontAndColorSettings(cell, 6, Color.BLACK, Color.WHITE);
							cellPaddingAndBorderSettings(cell, 0, 0, 0, 0, Color.WHITE, 0);
						}
					} else {
						cellFontAndColorSettings(cell, 7, Color.BLACK, Color.WHITE);
						cellPaddingAndBorderSettings(cell, 0, 0, 0, 0, Color.WHITE, 0);
					}
					messageCount++;
				}
			}
		}
	}
	
	public void setJobFieldRows(BaseTable table, List<JobField> listJobField) {
		//Only process non-null jobs
		if(listJobField != null && listJobField.size() > 0){
			// Process each instance of the listJobField objects.
			for(JobField job : listJobField){
				// use local strings so we don't modify the reqObj contents for other labels
				String screenLabel = job.getScreenLabel();
				String enteredValue = job.getEnteredValue();
				//only process non-null values	
				if (job.getScreenLabel() != null &&  job.getEnteredValue() != null){
					//Modify input values, replace / and " with -
					job.setEnteredValue(StringEscapeUtils.unescapeHtml4(job.getEnteredValue().replaceAll("\"|\\\\|\\~", "-")));
					// Only process defined job data.
					if(job.getScreenLabel().length() > 0 && job.getEnteredValue().length() > 0 ) {
						// Truncate Screen Label to fit the line space.
						if (job.getScreenLabel().length() > 13){
							screenLabel = job.getScreenLabel().substring(0, 13);
						}
						// left justify job fields for drawdown customers
						CustWebCustomerProfile profile = customerService.getCustWebCustomerProfile(reqObj.getCustomerID());
						if (profile != null && profile.getCustomerType() != null && profile.getCustomerType().trim().toUpperCase().equals("DRAWDOWN")){
							if (job.getEnteredValue().length() > 21){
								enteredValue = job.getEnteredValue().substring(0, 21);
							}
							createTwoColumnRow(table, 7, 8, 43, haLeft, vaMiddle, screenLabel + ": ", 57, haLeft, vaMiddle, enteredValue);
						} else {
							// Truncate Entered Value to fit the line space.
							if (job.getEnteredValue().length() > 16){
								enteredValue = job.getEnteredValue().substring(0, 16);
							}
							createTwoColumnRow(table, 7, 8, 50, haRight, vaMiddle, screenLabel + ": ", 50, haLeft, vaMiddle, enteredValue);
						}
					}
				}
			}
		}	
	}
	
	public void checkForUnicode(Cell<PDPage> cell, String text) {
		//get new font if unicode text found.
		//if font not available replace unicode chars with '#'
		//if no unicode chars, just use helvetica
		if(text != null && text.length() > 0) {
			if(hasUnicode(text)){
				fontBold = getUnicode(text);
				if(fontBold == null) {
					fontBold = helvetica;
					cell.setText(replaceUnicode(text));
				}
			} else {
				fontBold = helvetica;
			}
		} else { //null case, must have helvetica to avoid error.
			fontBold = helvetica;
		}
	}
	
	boolean hasArabicCharacters(String string) {
		boolean ret = false;
		for (char ch : string.toCharArray()) {
			if(ch >= 0x600 && ch <= 0x6FF){
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	boolean hasUnicode(String string) {
		boolean ret = false;
		for (char ch : string.toCharArray()) {
			if(!CharUtils.isAscii(ch)){
				ret = true;
				break;
			}
		}
		return ret;
	}
	/* 
	 * function to check string for unicode chars and 
	 * replace unicode chars with '#' in case font was 
	 * avail to print said char.
	 */
	String replaceUnicode(String input) {
		String retString="";
		try {
			StringBuilder sb = new StringBuilder(input.length());
			int count = 0;
			for (Character c : input.toCharArray()) {
				
			    if (!CharUtils.isAscii(c)) {
			        if((count % 2)==0){
			        sb.append('#');
			        }
			        count++;
			    } else {
			        sb.append(c);
			    }
			}
			retString = sb.toString();
		} catch(Exception e) {
			logger.error(exceptionDetail, e.getMessage(), colorNameLog, productNbrLog, errorLocation);

		}
	
		return retString;
	
	}
	void cellSettings(Cell<PDPage> cell, int fontSize, float cellHeight )
	{
		try {
			String text = cell.getText();
	
			checkForUnicode(cell, text);
			cell.setFontBold(fontBold);
			cell.setFont(fontBold);
	
			cell.setFontSize(fontSize);
	
			//	cell.setHeight(cellHeight); // DJM setting the height seems to make everything not print.
	
	
			cell.setLeftPadding(0);
			cell.setRightPadding(0);
			cell.setTopPadding(0);
			cell.setBottomPadding(0);
		} catch(Exception e) {
			logger.error(exceptionDetail, e.getMessage(), colorNameLog, productNbrLog, errorLocation);
		}

	}
	
	private BaseTable createTopTable(PDPage page, boolean drawContent, boolean drawLines) {
		float margin = 3;
		float bottomMargin = 0;
		// starting y position is whole page height subtracted by top and bottom margin
		float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
		// we want table across whole page width (subtracted by left and right margin ofcourse)
		float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
		float yPosition = yStartNewPage;
		BaseTable table = null;
		try {
			table = new BaseTable(yPosition, yStartNewPage,
					bottomMargin, tableWidth, margin, document, page, drawLines, drawContent);
		} catch (IOException e) {
			logger.error(exceptionDetail, e.getMessage(), colorNameLog, productNbrLog, errorLocation);
		}
		return table;
	}
	
	private Cell<PDPage> createFormulaHeading(Row<PDPage> row, String cellValue, float cellWidth,int fontSize, HorizontalAlignment cellAlign) {
		row.setHeaderRow(true);

		Cell<PDPage> cell = row.createCell(cellWidth,cellValue);
		cell.setFontBold(fontBold);
		cell.setFontSize(fontSize);
		cell.setValign(VerticalAlignment.MIDDLE);
		cell.setAlign(cellAlign);
		return cell;
	}	

	private Cell<PDPage> createFormulaLine(Row<PDPage> row, String cellValue, float cellWidth,int fontSize, HorizontalAlignment cellAlign) {
		row.setHeaderRow(false);

		Cell<PDPage> cell = row.createCell(cellWidth,cellValue);
		cell.setFontBold(fontBold);
		cell.setFont(fontBold);
		cell.setFontSize(fontSize);
		cell.setValign(VerticalAlignment.MIDDLE);
		cell.setAlign(cellAlign);
		return cell;
	}

	private void createBarcode(PDPageContentStream content ){
		try {
			String barCodeChars = String.format("%08d-%03d",reqObj.getControlNbr(), 1);
			BufferedImage bufferedImage = generateBarcodeImage(barCodeChars);
			PDImageXObject pdImage = JPEGFactory.createFromImage(document, bufferedImage);

			content.drawImage(pdImage, 20, 12, 100, 15);
		} catch (IOException e) {
			logger.error(exceptionDetail, e.getMessage(), colorNameLog, productNbrLog, errorLocation);

		}
	}
	
	private void createQRCode(PDPageContentStream content ){
		try {
			String qrCodeChars = String.format("%s-%08d-%03d", reqObj.getCustomerID(), reqObj.getControlNbr(), reqObj.getLineNbr());
			BufferedImage bufferedImage = generateQRCodeImage(qrCodeChars);
			PDImageXObject pdImage = JPEGFactory.createFromImage(document, bufferedImage);

			content.drawImage(pdImage, 20, 12, 100, 90);
		} catch (IOException e) {
			logger.error(e.getMessage(), exceptionDetail, "createQRCode", colorNameLog, productNbrLog, errorLocation, e);
		}
	}
	
	private static BufferedImage generateBarcodeImage(String barcodeText) {
		Code128Writer barcodeWriter = new Code128Writer();
	    BitMatrix bitMatrix = null;
		try {
			bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, 300, 150);
		} catch (WriterException e) {
			logger.error(e.getMessage(),"generateBarcodeImage");
		}

	    return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	

	private static BufferedImage generateQRCodeImage(String qrCodeText) {
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = null;
		try {
			bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 100, 90);
		} catch (WriterException e) {
			logger.error(e.getMessage(),"generateQRCodeImage");
		}

	    return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	void addCenteredText(PDPageContentStream content, String text, PDFont font, int fontSize,  PDPage page, float yOffset) throws IOException {
		// Create the output file stream.
		content.setFont(font, fontSize);
		content.beginText();

		// Rotate the text according to the page orientation
		boolean pageIsLandscape = isLandscape(page);
		Point2D.Float pageCenter = getCenter(page);

		// We use the text's width to place it at the center of the page
		float stringWidth = getStringWidth(text, font, fontSize);
		if (pageIsLandscape) {
			float textX = pageCenter.x - stringWidth / 2F ;
			// Swap X and Y due to the rotation
			content.setTextMatrix(Matrix.getRotateInstance(Math.PI / 2, yOffset, textX));
		} else {
			float textX = pageCenter.x - stringWidth / 2F ;
			content.setTextMatrix(Matrix.getTranslateInstance(textX, yOffset));
		}

		content.showText(text);
		content.endText();
	}

	boolean isLandscape(PDPage page) {
		int rotation = page.getRotation();
		final boolean isLandscape;
		if (rotation == 90 || rotation == 270) {
			isLandscape = true;
		} else if (rotation == 0 || rotation == 360 || rotation == 180) {
			isLandscape = false;
		} else {
			logger.error("Can only handle pages that are rotated in 90 degree steps. This page is rotated {} degrees. Will treat the page as in portrait format", rotation);
			isLandscape = false;
		}
		return isLandscape;
	}

	Point2D.Float getCenter(PDPage page) {
		PDRectangle pageSize = page.getMediaBox();
		boolean rotated = isLandscape(page);
		float pageWidth = rotated ? pageSize.getHeight() : pageSize.getWidth();
		float pageHeight = rotated ? pageSize.getWidth() : pageSize.getHeight();

		return new Point2D.Float(pageWidth / 2F, pageHeight / 2F);
	}

	float getStringWidth(String text, PDFont font, int fontSize) throws IOException {
		return font.getStringWidth(text) * fontSize / 1000F;
	}
	public PDFont getFontBold() {
		return fontBold;
	}
	public void setFontBold(PDFont fontBold) {
		this.fontBold = fontBold;
	}
	public PDFont getUnicode(String text) {
		PDFont unicodeFont=null;
		if(unicode == null) { //only get font once and read it into memory
			//solaris
			
			File f = null;
			
			if(hasArabicCharacters(text)) {
				f = new File("/usr/share/fonts/TrueType/arabeyes/ae_Arab.ttf");
			} 

			if(f == null || !f.exists()){
				f = new File("/usr/share/fonts/TrueType/arphic-uming/uming.ttf");
			}
			if(!f.exists()) {
				f = new File("/usr/share/fonts/TrueType/dejavu/DejaVuSansMono-Bold.ttf");
			}
			if(!f.exists()) {
				f = new File("c:\\Windows\\Fonts\\l_10646.ttf"); // Lucida Sans Unicode
			}
			if(!f.exists()) {
				f = new File("c:\\Windows\\Fonts\\ARIALUNI.TTF"); // Arial Unicode
			}
			if(f.exists()) {
				try {
					unicodeFont = PDType0Font.load(document,f);
					setUnicode(unicodeFont);
				} catch (IOException e) {
					logger.error(Encode.forJava(e.getMessage() + exceptionDetail),"setFontBold",colorNameLog,productNbrLog,errorLocation, e);
				}
			}
		}

		return unicode;
	}
	public void setUnicode(PDFont unicode) {
		this.unicode = unicode;
	}
	
}
