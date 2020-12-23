package com.sherwin.shercolor.customershercolorweb.util;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
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
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.stereotype.Service;

import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CustWebCustomerProfile;
import com.sherwin.shercolor.common.domain.CustWebDrawdownLabelProfile;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.DrawdownLabelService;
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

	public ShercolorLabelPrintImpl(DrawdownLabelService drawdownLabelService, CustomerService customerService, ColorMastService colorMastService) {
		this.drawdownLabelService = drawdownLabelService;
		this.customerService = customerService;
		this.colorMastService = colorMastService;
	}
	
	//Creating exception string
	String errorLocation = "";

	// Create a new empty document
	PDDocument document = new PDDocument();
	String filename;
	RequestObject reqObj;

	// Set all fonts to bold  to resolve light printing issue.
	
	final PDFont helvetica    = PDType1Font.HELVETICA_BOLD;
	PDFont fontBold 	= helvetica;
	PDFont unicode = null; //font to use for unicode chars


	// Cell Alignment Variables
	HorizontalAlignment haLeft = HorizontalAlignment.LEFT;
	HorizontalAlignment haRight = HorizontalAlignment.RIGHT;
	HorizontalAlignment haCenter = HorizontalAlignment.CENTER;
	VerticalAlignment vaMiddle = VerticalAlignment.MIDDLE;
				
	private static float WIDTH = 144f;

	public void DrawLabel(List<FormulaIngredient> formulaIngredients, String partMessage, String labelType, String canType, String clrntAmtList) {
		switch(labelType) {
		case "storeLabel":
			DrawStoreLabelPdf(  formulaIngredients, partMessage);
			break;
		case "drawdownStoreLabel":
			DrawDrawdownStoreLabel(formulaIngredients, partMessage);
			break;
		case "sampleCanLabel":
			DrawDrawdownCanLabel(formulaIngredients, partMessage, canType, clrntAmtList);
			break;
		}
	}
	
	public void CreateLabelPdf(String filename,RequestObject reqObj, String printLabelType, String printOrientation, String canType, String clrntAmtList) {
		this.filename = filename;
		this.reqObj=reqObj;


		String partMessage = null;

		try {

			if (printLabelType.equals("drawdownLabel")) {
				DrawDrawdownLabelPdf();
				
			} else {
				
				// Get formula ingredients (colorants) for processing.
				errorLocation = "Retrieving Formula Ingredients";
				List<FormulaIngredient> listFormulaIngredients = reqObj.getDisplayFormula().getIngredients();
				// Determine the number of ingredient (colorant) lines in the formula.
				int formulaSize = reqObj.getDisplayFormula().getIngredients().size();

				// 5 or less lines in a formula - pass the one part label formula to formatting method.
				if (formulaSize <= 5){
					partMessage = " ";
					errorLocation = "Drawing Label";
					DrawLabel(listFormulaIngredients, partMessage, printLabelType, canType, clrntAmtList);
				}
				else {
					// Split the label colorant lines for 2 separate labels and proceed to create 2 labels that print simultaneously.
					int counter = 0;
					List<FormulaIngredient>partAListFormulaIngredients = new ArrayList<FormulaIngredient>(); 
					List<FormulaIngredient>partBListFormulaIngredients = new ArrayList<FormulaIngredient>();
					for(FormulaIngredient ingredient : listFormulaIngredients){
						if (counter <= 4 ){
							partAListFormulaIngredients.add(ingredient);
						}
						else{
							partBListFormulaIngredients.add(ingredient);
						}
						counter++;
					}
					// Write the part A label on first page.
					if(partAListFormulaIngredients != null && partAListFormulaIngredients.size() > 0){
						partMessage = "* PART A - SEE PART B OF FORMULA *";
						errorLocation = "Drawing Label Part A";
						DrawLabel(partAListFormulaIngredients, partMessage, printLabelType, canType, clrntAmtList);
					}	
					// Skip to next page to write part B label.

					// Write the part B label on second page.
					if(partBListFormulaIngredients != null && partBListFormulaIngredients.size() > 0){
						partMessage = "* PART B - SEE PART A OF FORMULA *";
						errorLocation = "Drawing Label Part B";
						DrawLabel(partBListFormulaIngredients, partMessage, printLabelType, canType, clrntAmtList);
					}	
				}
				// Label Pdf is completed.  1 or 2 labels will print.  Close the document.
				// Save the results and ensure that the document is properly closed:
				
			}
			

			document.save(filename);
		}

		catch(IOException ie) {
			logger.error(ie.getMessage() + ": [CreateLabelPdf, " + errorLocation + "]", ie);
			//logger.error(ie);
		}
		catch(RuntimeException re){
			logger.error(re.getMessage() + ": [CreateLabelPdf, " + errorLocation + "]", re);
			//logger.error(re);
		}
		finally {
			try {
				errorLocation = "Closing Document";
				document.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage() + ": [CreateLabelPdf, " + errorLocation + "]", e);
			}
		}
	}

	private void createTableCell(Row<PDPage> row,int  rowHeight, float cellWidth, int fontSize, HorizontalAlignment cellAlign) {


		Cell<PDPage> cell = row.createCell(cellWidth, reqObj.getCustomerName(), cellAlign, VerticalAlignment.MIDDLE);
		cellSettings(cell,fontSize,rowHeight );
	}
	
	private void DrawStoreLabelPdf(List<FormulaIngredient> listFormulaIngredients, String partMessage ) 
	{
		int rowHeight = 2;
		// Create a new blank page and add it to the document
		PDPage page = new PDPage();

		// Create the 2" x 4" document.
		// step 1 - width and height - 2 (144) x 4 (288) inches.  One inch = 72 points.
		page.setMediaBox(new PDRectangle(0, 0 , 144f, 288f));
		PDPageContentStream content = null;
		try {
			errorLocation = "Opening Content Stream";
			content = new PDPageContentStream(document, page);
		} catch (IOException e1) {
			logger.error(e1.getMessage() + ": [DrawStoreLabelPdf, " + errorLocation + "]", e1);
		}

		try{
			
			errorLocation = "Table Build";
			//Setup table
			BaseTable table = createTopTable(page);
			
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
			errorLocation = "Interior/Exterior";
			createTwoColumnRow(table, 6, 8, 30, haLeft, vaMiddle, reqObj.getIntExt(), 70, haRight, vaMiddle, reqObj.getKlass());
			
			// ================================================================================================================================================
			// 12/07/2020 | Quality and Composite
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
			setColorIdAndNameRow();
			createOneColumnRow(table, 10, 12, 100, haCenter, vaMiddle, reqObj.getColorID() + " " + reqObj.getColorName() );
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
			List<JobField> listJobField = reqObj.getJobFieldList();
			setJobFieldRows(table, listJobField);
			
			// ================================================================================================================================================
			// 12/07/2020 | Bar Code and Order and Line Numbers (part of bar code).
			errorLocation = "Bar Code & Order Number";

			createBarcode(content);
			String barCodeChars = String.format("%08d-%03d",reqObj.getControlNbr(), 1);
			addCenteredText(content,barCodeChars,fontBold,8,page,4.0f);
			
			// ================================================================================================================================================
			try {
				table.draw();
			}
			catch(java.lang.IllegalArgumentException ex) {
				logger.error(ex.getMessage() + ": [DrawStoreLabelPdf, " + errorLocation + "]", ex);
			}

			content.close();
			document.addPage( page );
		}
		catch(IOException ie) {
			logger.error(ie.getMessage() + ": [DrawStoreLabelPdf, " + errorLocation + "]", ie);
			//logger.error(ie);
		}
		catch(RuntimeException re){
			logger.error(re.getMessage() + ": [DrawStoreLabelPdf, " + errorLocation + "]", re);
			//logger.error(re);
		}
	}
	
	private void DrawDrawdownLabelPdf() {
		// Create a new blank page and add it to the document
		PDPage page = new PDPage();

		// Create the 4" x 2" document.
		// step 1 - width and height - 4 (288) x 2 (144) inches.  One inch = 72 points.
		page.setMediaBox(new PDRectangle(0, 0 , 288f, 144f));
		PDPageContentStream content = null;
		try {
			errorLocation = "Opening Content Stream";
			content = new PDPageContentStream(document, page);
		} catch (IOException e1) {
			logger.error(e1.getMessage() + ": [DrawDrawdownLabelPdf, " + errorLocation + "]", e1);
		}

		try{

			//setup Table
			errorLocation = "Table Setup";
			BaseTable table = createTopTable(page);
			
			
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
			
			errorLocation = "Customer";
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Customer:",cell2Width,haLeft,vaMiddle,customer);
			errorLocation = "Store CNN";
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Store CNN:",cell2Width,haLeft,vaMiddle,storeCCN);
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
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"",cell2Width,haLeft,vaMiddle,"");
			errorLocation = "Color";
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Color:",
							   cell2Width,haLeft,vaMiddle,reqObj.getColorComp() + " " + reqObj.getColorID() + " " + reqObj.getColorName());
			errorLocation = "Product";
			createTwoColumnRow(table,fontSize,rowHeight,cell1Width,haRight,vaMiddle,"Product:",
							   cell2Width,haLeft,vaMiddle,reqObj.getQuality() + " " + reqObj.getFinish() + " " + reqObj.getBase() + " " + reqObj.getProdNbr());
			
			try {
				table.draw();
			}
			catch(java.lang.IllegalArgumentException ex) {
				logger.error(ex.getMessage() + ": [DrawDrawdownLabelPdf, " + errorLocation + "]", ex);
			}
			
			content.close();
			document.addPage( page );
		}
		catch(IOException ie) {
			logger.error(ie.getMessage() + ": [DrawDrawdownLabelPdf, " + errorLocation + "]", ie);
			//logger.error(ie);
		}
		catch(RuntimeException re){
			logger.error(re.getMessage() + ": [DrawDrawdownLabelPdf, " + errorLocation + "]", re);
			//logger.error(re);
		}
	}
	
	private void DrawDrawdownStoreLabel(List<FormulaIngredient> listFormulaIngredients, String partMessage ) {

		int rowHeight = 2;
		// Create a new blank page and add it to the document
		PDPage page = new PDPage();

		// Create the 2" x 4" document.
		// step 1 - width and height - 2 (144) x 4 (288) inches.  One inch = 72 points.
		page.setMediaBox(new PDRectangle(0, 0 , 144f, 288f));
		PDPageContentStream content = null;
		try {
			content = new PDPageContentStream(document, page);
		} catch (IOException e1) {
			logger.error(e1.getMessage() + ": ", e1);
		}

		try{
			
			errorLocation = "Table Build";
			//Setup table
			BaseTable table = createTopTable(page);
			
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
			setColorIdAndNameRow();
			
			CustWebCustomerProfile custProfile = customerService.getCustWebCustomerProfile(reqObj.getCustomerID());
			CdsColorMast colorMast = colorMastService.read(reqObj.getColorComp(), reqObj.getColorID());
			if (custProfile.isUseLocatorId() && colorMast.getLocId() != null) {
				createOneColumnRow(table, 10, 12, 100, haCenter, vaMiddle, colorMast.getLocId() + " " + reqObj.getColorID() + " " + reqObj.getColorName() );
			} else {
				createOneColumnRow(table, 10, 12, 100, haCenter, vaMiddle, reqObj.getColorID() + " " + reqObj.getColorName() );
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
			List<JobField> listJobField = reqObj.getJobFieldList();
			setJobFieldRows(table, listJobField);
			
			// ================================================================================================================================================
			try {
				table.draw();
			}
			catch(java.lang.IllegalArgumentException ex) {
				logger.error(ex.getMessage() + ": [DrawDrawdownStoreLabelPdf, " + errorLocation + "]", ex);
			}

			content.close();
			document.addPage( page );
		}
		catch(IOException ie) {
			logger.error(ie.getMessage() + ": [DrawDrawdownStoreLabelPdf, " + errorLocation + "]", ie);
			//logger.error(ie);
		}
		catch(RuntimeException re){
			logger.error(re.getMessage() + ": [DrawDrawdownStoreLabelPdf, " + errorLocation + "]", re);
			//logger.error(re);
		}
	}
	
	public void DrawDrawdownCanLabel(List<FormulaIngredient> listFormulaIngredients, String partMessage, String canType, String clrntAmtList ) {
		int rowHeight = 2;
		// Create a new blank page and add it to the document
		PDPage page = new PDPage();

		// Create the 2" x 4" document.
		// step 1 - width and height - 2 (144) x 4 (288) inches.  One inch = 72 points.
		page.setMediaBox(new PDRectangle(0, 0 , 144f, 288f));
		PDPageContentStream content = null;
		try {
			errorLocation = "Opening Content Stream";
			content = new PDPageContentStream(document, page);
		} catch (IOException e1) {
			logger.error(e1.getMessage() + ": ", e1);
		}

		try{
			
			errorLocation = "Table Build";
			//Setup table
			BaseTable table = createTopTable(page);
			
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
			setColorIdAndNameRow();
			createOneColumnRow(table, 10, 12, 100, haCenter, vaMiddle, reqObj.getColorID() + " " + reqObj.getColorName() );
			// ------------------------------------------------------------------------------------------------------------------------------------------------
			errorLocation = "Formula Type";
			createOneColumnRow(table, 8, 8, 100, haCenter, vaMiddle, reqObj.getDisplayFormula().getSourceDescr());
			
			// ================================================================================================================================================
			// 12/07/2020 | Formula Heading and 5 Colorant Lines maximum.
			errorLocation = "Formula Heading and Colorant Lines";
			rowHeight = 8;
			createTwoColumnRow(table, 8, 8, 50, haLeft, vaMiddle, reqObj.getClrntSys() + " Colorant", 50, haLeft, vaMiddle, reqObj.getDisplayFormula().getIncrementHdr().get(0));
			int numIngredients = 0;
			String[] clrntAmtString = new String[5];
			clrntAmtString = clrntAmtList.split(",");
			List<String> clrntAmtDouble = new ArrayList<String>();
			List<String> clrntIngredientList = new ArrayList<String>();
			int counter = 0;
			for (String amt : clrntAmtString) {
				if (amt != null || !amt.equals("")) {
					if(counter%2==0) {
						clrntIngredientList.add(amt.replace("-", " "));
					} else {
						clrntAmtDouble.add(String.format("%,.8f", Double.parseDouble(amt)));
					}
				}
				counter++;
			}
			
			for (FormulaIngredient ingredient : listFormulaIngredients) {
				createTwoColumnRow(table, 8, 8, 50, haLeft, vaMiddle, clrntIngredientList.get(numIngredients), 50, haLeft, vaMiddle, clrntAmtDouble.get(numIngredients));
				numIngredients++;
			}
			for (int index=numIngredients; index<5; index++) {
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
			try {
				table.draw();
			}
			catch(java.lang.IllegalArgumentException ex) {
				logger.error(ex.getMessage() + ": [DrawDrawdownCanLabelPdf, " + errorLocation + "]", ex);
			}

			content.close();
			document.addPage( page );
		}
		catch(IOException ie) {
			logger.error(ie.getMessage() + ": [DrawDrawdownCanLabelPdf, " + errorLocation + "]", ie);
			//logger.error(ie);
		}
		catch(RuntimeException re){
			logger.error(re.getMessage() + ": [DrawDrawdownCanLabelPdf, " + errorLocation + "]", re);
			//logger.error(re);
		}
	}
	
	// ==========================================================================================================================================
	// Label Row Creation Methods
	// 11/24/2020 - Create Row methods to allow greater flexibility when making different labels. Parameters are neccessary
	//				to help assist in providing more instruction and less static values 
	// ==========================================================================================================================================

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
		Row<PDPage> row = table.createRow(rowHeight);
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
		createTwoColumnRow(table, 6, 8, setQualityParm, haLeft, vaMiddle, reqObj.getQuality(), setCompositeParm, haRight, vaMiddle, reqObj.getComposite());
	}
	
	public void setColorIdAndNameRow() {
		int totalCharsLength = 0;
		// Color i.d. and name.
		// 01/20/2017 - Begin Color I.D. and Color Name field build.
		if (reqObj.getColorID()==null) {reqObj.setColorID("");}
		if (reqObj.getColorName()==null) {reqObj.setColorName("");}
		totalCharsLength = reqObj.getColorID().length() + reqObj.getColorName().length();

		//Modify input values, replace / and " with -
		if(!StringUtils.isEmpty(reqObj.getColorID()) && !StringUtils.isEmpty(reqObj.getColorName())){
			reqObj.setColorID(reqObj.getColorID().replaceAll("\"|\\\\|\\~", "-"));
			reqObj.setColorName(StringEscapeUtils.unescapeHtml(reqObj.getColorName().replaceAll("\"|\\\\|\\~", "-")));
		}

		// Truncate the Color Name to fit the space in line.  Color I.D. is maximum of 10.  Use the remaining space
		// for the Color name.
		if (totalCharsLength >= 22){
			int colorNameLength = 22 - reqObj.getColorID().length();
			if (reqObj.getColorName().length() > colorNameLength){
				reqObj.setColorName(reqObj.getColorName().substring(0, colorNameLength));
			}
		}
		// 01/20/2017 - Begin Color I.D. and Color Name field build.
		totalCharsLength = reqObj.getColorID().length() + reqObj.getColorName().length();

		//Modify input values, replace / and " with -
		if(!StringUtils.isEmpty(reqObj.getColorID()) && !StringUtils.isEmpty(reqObj.getColorName())){
			reqObj.setColorID(reqObj.getColorID().replaceAll("\"|\\\\|\\~", "-"));
			reqObj.setColorName(StringEscapeUtils.unescapeHtml(reqObj.getColorName().replaceAll("\"|\\\\|\\~", "-")));
		}

		// Truncate the Color Name to fit the space in line.  Color I.D. is maximum of 10.  Use the remaining space
		// for the Color name.
		if (totalCharsLength >= 22){
			int colorNameLength = 22 - reqObj.getColorID().length();
			if (reqObj.getColorName().length() > colorNameLength){
				reqObj.setColorName(reqObj.getColorName().substring(0, colorNameLength));
			}
		}
	}
	
	public void setStandardFormulaTable(List<FormulaIngredient> listFormulaIngredients, BaseTable table, Row<PDPage> row, int rowHeight) {
		errorLocation = "Formula Label Line Headings";
		// Formula Label Line Headings
		List<String> listIncrementHdr = reqObj.getDisplayFormula().getIncrementHdr();
		Cell<PDPage> fcellHead1 =  createFormulaHeading(row,reqObj.getClrntSys() + " Colorant",52f, 7,  "left");
		Cell<PDPage> fcellHead3 =  createFormulaHeading(row,listIncrementHdr.get(0),11f, 8, "center");
		Cell<PDPage> fcellHead4 =  createFormulaHeading(row,listIncrementHdr.get(1), 11f,8, "center");
		Cell<PDPage> fcellHead5 =  createFormulaHeading(row,listIncrementHdr.get(2), 11f,8, "center");
		Cell<PDPage> fcellHead6 =  createFormulaHeading(row,listIncrementHdr.get(3), 14f,8, "center");

		// Formula Label Line Item.
		errorLocation = "Formula Label Line Items";
		int lineCtr = 0;
		Cell<PDPage> fcellLine1;
		Cell<PDPage> fcellLine3;
		Cell<PDPage> fcellLine4;
		Cell<PDPage> fcellLine5;
		Cell<PDPage> fcellLine6;
		if(listFormulaIngredients != null && listFormulaIngredients.size() > 0){
			// Process each instance of the listFormulaIngredients objects.
			for(FormulaIngredient line : listFormulaIngredients){
				int [] amount = line.getIncrement();
				// Add the available formula lines to the label.
				row = table.createRow(rowHeight);

				fcellLine1 =  createFormulaLine(row,line.getTintSysId() + " " + line.getName(),51f,7,"left");

				if (! Integer.toString(amount[0]).equals("0")){
					fcellLine3 =  createFormulaLine(row,Integer.toString(amount[0]), 11f, 7, "right");
				}
				else {
					fcellLine3 =  createFormulaLine(row,"-", 11f, 7, "right");
				}

				if (! Integer.toString(amount[1]).equals("0")){
					fcellLine4 =  createFormulaLine(row,Integer.toString(amount[1]), 11f, 7, "right");
				}
				else {
					fcellLine4 =  createFormulaLine(row,"-", 11f, 7, "right");
				}

				if (! Integer.toString(amount[2]).equals("0")){
					fcellLine5 =  createFormulaLine(row,Integer.toString(amount[2]), 11f,7, "right");
				}
				else {
					fcellLine5 =  createFormulaLine(row,"-", 11f, 7, "right");
				}


				if (! Integer.toString(amount[3]).equals("0")){
					fcellLine6 =  createFormulaLine(row,Integer.toString(amount[3]), 14f, 7, "right");
				}
				else {
					fcellLine6 =  createFormulaLine(row,"-", 14f,7, "right");
				}


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
		if(listSwMessages != null && listSwMessages.size() > 0){
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
	
	public void setJobFieldRows(BaseTable table, List<JobField> listJobField) {
		//Modify input values, replace / and " with -
		if(!listJobField.isEmpty()){
			for (JobField jobField : listJobField) {
				jobField.setEnteredValue(StringEscapeUtils.unescapeHtml(jobField.getEnteredValue().replaceAll("\"|\\\\|\\~", "-")));
			}
		}

		if(listJobField != null && listJobField.size() > 0){
			// Process each instance of the listJobField objects.
			//TODO - Will these always be in order?  Will there always be 4?
			for(JobField job : listJobField){
				// Only process defined job data.	
				if (job.getScreenLabel().length() > 0 && job.getEnteredValue().length() > 0 ){
					// 01/20/2017 - Begin Job
					// Truncate Screen Label to fit the line space.
					if (job.getScreenLabel().length() > 13){
						job.setScreenLabel(job.getScreenLabel().substring(0, 13));
					}
					// Truncate Entered Value to fit the line space.
					if (job.getEnteredValue().length() > 16){
						job.setEnteredValue(job.getEnteredValue().substring(0, 16));
					}
					// 01/20/2017 - End Job 
					createTwoColumnRow(table, 7, 8, 50, haRight, vaMiddle, job.getScreenLabel() + ": ", 50, haLeft, vaMiddle, job.getEnteredValue());
				}
			}
		}	
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
			logger.error(e.getMessage() + ": ", e);
		}
	
		return retString;
	
	}
	void cellSettings(Cell<PDPage> cell, int fontSize, float cellHeight )
	{
		try {
			String text = cell.getText();
	
			//get new font if unicode text found.
			//if font not available replace unicode chars with '#'
			//if no unicode chars, just use helvetica
			if(text!=null && text.length()>0) {
				if(hasUnicode(text)){
					fontBold = getUnicode();
					if(fontBold == null) {
						fontBold = helvetica;
						cell.setText(replaceUnicode(text));
					}
				}
				else {
					fontBold = helvetica;
				}
			}
			else { //null case, must have helvetica to avoid error.
				fontBold = helvetica;
			}
			cell.setFontBold(fontBold);
			cell.setFont(fontBold);
	
			cell.setFontSize(fontSize);
	
			//	cell.setHeight(cellHeight); // DJM setting the height seems to make everything not print.
	
	
			cell.setLeftPadding(0);
			cell.setRightPadding(0);
			cell.setTopPadding(0);
			cell.setBottomPadding(0);
		} catch(Exception e) {
			logger.error(e.getMessage() + ": ", e);
		}


	}
	private BaseTable createTopTable(PDPage page) {
		float margin = 3;
		float bottomMargin = 0;
		// starting y position is whole page height subtracted by top and bottom margin
		float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
		// we want table across whole page width (subtracted by left and right margin ofcourse)
		float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
		float yPosition = yStartNewPage;
		boolean drawContent = true;
		boolean drawLines = false;
		BaseTable table = null;
		try {
			table = new BaseTable(yPosition, yStartNewPage,
					bottomMargin, tableWidth, margin, document, page, drawLines, drawContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage() + ": ", e);
			e.printStackTrace();
		}
		return table;
	}
	private BaseTable createTableFormula(PDPage page) {
		float margin = 2f;
		float bottomMargin = 0;
		// starting y position is whole page height subtracted by top and bottom margin
		float yStartNewPage = page.getMediaBox().getHeight();
		// we want table across whole page width (subtracted by left and right margin ofcourse)
		float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
		float yPosition = 200f;
		boolean drawContent = true;
		boolean drawLines = false;
		BaseTable table = null;
		try {
			table = new BaseTable(yPosition, yStartNewPage,
					bottomMargin, tableWidth, margin, document, page, drawLines, drawContent);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage() + ": ", e);
			e.printStackTrace();
		}
		return table;
	}
	private Cell<PDPage> createFormulaHeading(Row<PDPage> row, String cellValue, float cellWidth,int fontSize, String cellAlign)
	{
		row.setHeaderRow(true);

		Cell<PDPage> cell = row.createCell(cellWidth,cellValue);
		cell.setFontBold(fontBold);
		cell.setFontSize(fontSize);
		cell.setValign(VerticalAlignment.MIDDLE);
		//cell.setHeight(cellHeight);

		//cell.setMinimumHeight(cellHeight);
		if (cellAlign.equals("left"))
			cell.setAlign(HorizontalAlignment.LEFT);
		if (cellAlign.equals("right"))
			cell.setAlign(HorizontalAlignment.RIGHT);
		if (cellAlign.equals("center"))
			cell.setAlign(HorizontalAlignment.CENTER);
		cell.setLeftPadding(0);
		cell.setRightPadding(2f);
		cell.setTopPadding(0);
		cell.setBottomPadding(0);
		return cell;
	};

	private Cell<PDPage> createFormulaLine(Row<PDPage> row, String cellValue, float cellWidth,int fontSize, String cellAlign)
	{
		row.setHeaderRow(false);

		Cell<PDPage> cell = row.createCell(cellWidth,cellValue);
		cell.setFontBold(fontBold);
		cell.setFont(fontBold);
		cell.setFontSize(fontSize);
		cell.setValign(VerticalAlignment.MIDDLE);
		//cell.setHeight(cellHeight);

		//cell.setMinimumHeight(cellHeight);
		if (cellAlign.equals("left"))
			cell.setAlign(HorizontalAlignment.LEFT);
		if (cellAlign.equals("right"))
			cell.setAlign(HorizontalAlignment.RIGHT);
		if (cellAlign.equals("center"))
			cell.setAlign(HorizontalAlignment.CENTER);
		cell.setLeftPadding(0);
		cell.setRightPadding(2f);
		cell.setTopPadding(0);
		cell.setBottomPadding(0);
		return cell;
	};
	private BaseTable createTableProd(PDPage page) {
		float margin = 2f;
		float bottomMargin = 0;
		// starting y position is whole page height subtracted by top and bottom margin
		float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
		// we want table across whole page width (subtracted by left and right margin ofcourse)
		float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
		float yPosition = 140f;
		boolean drawContent = true;
		boolean drawLines = false;
		BaseTable table = null;
		try {
			table = new BaseTable(yPosition, yStartNewPage,
					bottomMargin, tableWidth, margin, document, page, drawLines, drawContent);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage() + ": ", e);
			e.printStackTrace();
		}
		return table;
	}

	private void createBarcode(PDPageContentStream content ){
		try {
			String barCodeChars = String.format("%08d-%03d",reqObj.getControlNbr(), 1);
			BufferedImage bufferedImage = geBufferedImageForCode128Bean(barCodeChars);
			PDImageXObject pdImage = JPEGFactory.createFromImage(document, bufferedImage);

			content.drawImage(pdImage, 20, 12, 100, 15);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage() + ": ", e);
			e.printStackTrace();
		}
	}

	private  BufferedImage geBufferedImageForCode128Bean(String barcodeString) {
		Code128Bean code128Bean = new Code128Bean();
		final int dpi = 150;
		code128Bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar 
		code128Bean.doQuietZone(false);
		code128Bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
		BitmapCanvasProvider canvas1 = new BitmapCanvasProvider(
				dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0
				);
		//Generate the barcode
		code128Bean.generateBarcode(canvas1, barcodeString);

		try {
			canvas1.finish();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage() + ": ", e);
			e.printStackTrace();
		}
		return canvas1.getBufferedImage();
	}

	void addCenteredText(PDPageContentStream content, String text, PDFont font, int fontSize,  PDPage page, float yOffset) throws IOException {
		// Create the output file stream.
		//	PDPageContentStream content = new PDPageContentStream(document, page);
		content.setFont(font, fontSize);
		content.beginText();

		// Rotate the text according to the page orientation
		boolean pageIsLandscape = isLandscape(page);
		Point2D.Float pageCenter = getCenter(page);

		// We use the text's width to place it at the center of the page
		float stringWidth = getStringWidth(text, font, fontSize);
		if (pageIsLandscape) {
			float textX = pageCenter.x - stringWidth / 2F ;
			float textY = pageCenter.y - yOffset;
			// Swap X and Y due to the rotation
			content.setTextMatrix(Matrix.getRotateInstance(Math.PI / 2, yOffset, textX));
		} else {
			float textX = pageCenter.x - stringWidth / 2F ;
			float textY = pageCenter.y + yOffset;
			content.setTextMatrix(Matrix.getTranslateInstance(textX, yOffset));
		}

		content.showText(text);
		content.endText();
		// content.close();
	}

	boolean isLandscape(PDPage page) {
		int rotation = page.getRotation();
		final boolean isLandscape;
		if (rotation == 90 || rotation == 270) {
			isLandscape = true;
		} else if (rotation == 0 || rotation == 360 || rotation == 180) {
			isLandscape = false;
		} else {
			logger.error("Can only handle pages that are rotated in 90 degree steps. This page is rotated  " + rotation + " degrees. Will treat the page as in portrait format");
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
	public PDFont getUnicode() {
		PDFont unicodeFont=null;
		if(unicode == null) { //only get font once and read it into memory
			//solaris
			
			File f = new File("/usr/share/fonts/TrueType/arphic-uming/uming.ttf");
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
					logger.error(e.getMessage() + ": ", e);
				}
			}
		}

		return unicode;
	}
	public void setUnicode(PDFont unicode) {
		this.unicode = unicode;
	}
	
}
