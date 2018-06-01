package com.sherwin.shercolor.customershercolorweb.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;

/* 
 * Important Comment - Adobe PDF/iText does not process nulls embedded in the generated label causing an open error in PDF.  
 * A message box titled OLE Exception with message "Cannot create the in-place editor." occurs.  it is likely a null 
 * string field passed from the request object into the label.  If a change is made to request object, look specifically at 
 * a change for a potential cause of label PDF open failure.
 * */

public class ShercolorLabelPrintImpl implements ShercolorLabelPrint{
	static Logger logger = LogManager.getLogger(ShercolorLabelPrintImpl.class);

	@Autowired
	ProductService productService;
	
	//Creating exception string
	String errorLocation = "";

	public void CreateLabelPdf(String filename, RequestObject reqObj) throws DocumentException, IOException {

		String partMessage = null;
		
		try {
			// Create the 2" x 4" document.
			Document document = new Document(new Rectangle(144f, 288f));
			// Set outside margins.
			document.setMargins(3, 3, 3, 3);
			// Create the output file stream.
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
			// Open document for creating the label or labels.
			document.open();
			// Get formula ingredients (colorants) for processing.
			List<FormulaIngredient> listFormulaIngredients = reqObj.getDisplayFormula().getIngredients();
			// Determine the number of ingredient (colorant) lines in the formula.
			int formulaSize = reqObj.getDisplayFormula().getIngredients().size();

			// 5 or less lines in a formula - pass the one part label formula to formatting method.
			if (formulaSize <= 5){
				partMessage = " ";
				CreateLabelPdf(filename, reqObj, pdfWriter, document, listFormulaIngredients, partMessage);
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
					CreateLabelPdf(filename, reqObj, pdfWriter, document, partAListFormulaIngredients, partMessage);
				}	
				// Skip to next page to write part B label.
				document.newPage();
				// Write the part B label on second page.
				if(partBListFormulaIngredients != null && partBListFormulaIngredients.size() > 0){
					partMessage = "* PART B - SEE PART A OF FORMULA *";
					CreateLabelPdf(filename, reqObj, pdfWriter, document, partBListFormulaIngredients, partMessage);
				}	
			}
			// Label Pdf is completed.  1 or 2 labels will print.  Close the document.
			document.close();
		}
		catch(DocumentException de) {
			System.out.println("DocumentException: " + de.getMessage() + de.getCause() + de.getStackTrace());
			logger.error(de.getMessage());
		}
		catch(IOException ie) {
		   System.out.println("IOException: " + ie.getMessage() + ie.getCause() + ie.getStackTrace());
		   logger.error(ie.getMessage());
		}
   	    catch(RuntimeException re){
   		   System.out.println("RuntimeException: " + re.getMessage() + re.getCause() + re.getStackTrace());
   		   logger.error(re.getMessage());
	   	}

	}

	public void CreateLabelPdf(String filename, 
					RequestObject reqObj, 
					PdfWriter pdfWriter,	
					Document document, 
					List<FormulaIngredient> listFormulaIngredients,  
					String partMessage ) 
					throws DocumentException, IOException {
		   
				try{
					
					// step 1 - width and height - 2 (144) x 4 (288) inches.  One inch = 72 points.
					int lineSpacing8 = 8;   
					int lineSpacing2 = 2;   
					//Document document = new Document(new Rectangle(144f, 288f));
				   	// Label document set-up.
					//document.setMargins(3, 3, 3, 3);
					errorLocation = "Font Setup";
				    Font regularFont2 	= new Font(Font.FontFamily.COURIER, 2);
				    Font regularFont8 	= new Font(Font.FontFamily.COURIER, 8);
				    Font boldFont5 		= new Font(Font.FontFamily.COURIER, 6);
				    boldFont5.setStyle(Font.BOLD);
			        Font regularFont6 	= new Font(Font.FontFamily.COURIER, 6);
			        Font regularFont7 	= new Font(Font.FontFamily.COURIER, 7);
			        Font regularFont10 	= new Font(Font.FontFamily.COURIER, 10);
			        // Set all fonts to bold temporarily to resolve light printing issue.
				    regularFont2.setStyle(Font.BOLD);
				    regularFont8.setStyle(Font.BOLD);
				    regularFont6.setStyle(Font.BOLD);
				    regularFont7.setStyle(Font.BOLD);
				    regularFont10.setStyle(Font.BOLD);
	
				    // Create the output file stream.
				    //PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
				    // Open document for formatting data.
				    //document.open();
	
					errorLocation = "Table Build";
				    // Build the label top 4 lines and 8 cells in a table.
				    Paragraph labelSection1 = new Paragraph(0);
				    labelSection1.setSpacingBefore(1);
				    labelSection1.setSpacingAfter(1);
	
				    PdfPTable tbl = new PdfPTable(2);
				    tbl.setWidthPercentage(98);
				    tbl.setSpacingBefore(0);
				    tbl.setSpacingAfter(0);
	
				    errorLocation = "Name and Date";
				    // Customer Name
				    PdfPCell custName =  createLabelCustOrdProdData(reqObj.getCustomerName(), regularFont7, 8, "left");
				    tbl.addCell(custName);
	
				    // Order Date
				    Date date = new Date();
				    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
				    String strDate = sdf.format(date);
				    PdfPCell orderDate =  createLabelCustOrdProdData(strDate, regularFont7, 8, "right");
				    tbl.addCell(orderDate);
	
				    // Optional Field - Not yet implemented.
				    PdfPCell custId =  createLabelCustOrdProdData(" ", regularFont7, 8, "left");
				    tbl.addCell(custId);
	
				    errorLocation = "Order Number & Blank Line";
				    // Order Number
				    PdfPCell orderNumber =  createLabelCustOrdProdData("Order # " + 
		    		Integer.toString(reqObj.getControlNbr()), 
		    		regularFont7, 7, "right");
				    tbl.addCell(orderNumber);
	
				    // Blank line
				    PdfPCell blank1 =  createLabelCustOrdProdData(" ", regularFont2, 2, "left");
				    tbl.addCell(blank1);
	
				    PdfPCell blank2 =  createLabelCustOrdProdData("", regularFont2, 2, "right");
				    tbl.addCell(blank2);
	
				    errorLocation = "Use & Class";
				    // 01/20/2017 - Begin Use and Class
				    labelSection1.add(tbl);
				    
				    PdfPTable tbl0 = new PdfPTable(2);
				    tbl0.setWidthPercentage(98);
				    tbl0.setWidths(new float[] {30, 70});
				    tbl0.setSpacingBefore(0);
				    tbl0.setSpacingAfter(0);
				    
				    // Use Interior/Exterior
				    PdfPCell intExt =  createLabelCustOrdProdData(reqObj.getIntExt(), regularFont6, 8, "left");
				    tbl0.addCell(intExt);
	
				    // Class
				    PdfPCell klass =  createLabelCustOrdProdData(reqObj.getKlass(), regularFont6, 8, "right");
				    tbl0.addCell(klass);
	
				    labelSection1.add(tbl0);
				    // 01/20/2017 - End Use and Class
				    
				    // 01/20/2017 - Quality and Composite - Begin.
				    errorLocation = "Quality & Composite";
				    int setQualityParm = 50;
				    int setCompositeParm = 50;
				    int totalCharsLength = 0;
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
 
				    PdfPTable tbl1 = new PdfPTable(2);
				    tbl1.setWidthPercentage(98);
				    tbl1.setWidths(new float[] { setQualityParm, setCompositeParm });
				    tbl1.setSpacingBefore(0);
				    tbl1.setSpacingAfter(0);
				    
				    errorLocation = "Quality";
				    // Quality
				    PdfPCell quality =  createLabelCustOrdProdData(reqObj.getQuality(), regularFont6, 8, "left");
				    tbl1.addCell(quality);
				    
				    errorLocation = "Composite";
				    // Composite
				    PdfPCell composite =  createLabelCustOrdProdData(reqObj.getComposite(), regularFont6, 8, "right");
				    tbl1.addCell(composite);
				    labelSection1.add(tbl1);
	
				    // 01/20/2017 - End Quality and Composite
		
				    errorLocation = "Finish & Tinter Type";
				    // 01/20/2017 - Begin Finish and Tinter Type.
				    PdfPTable tbl6 = new PdfPTable(2);
				    tbl6.setWidthPercentage(98);
				    tbl6.setWidths(new float[] { 60, 40});
				    tbl6.setSpacingBefore(0);
				    tbl6.setSpacingAfter(0);
				    		    
				    errorLocation = "Finish";
				    // Finish
				    PdfPCell finish =  createLabelCustOrdProdData(reqObj.getFinish(), regularFont6, 8, "left");
				    tbl6.addCell(finish);
				    
				    errorLocation = "Tinter Type";
				    // Tinter Type
			    	PdfPCell tinterType =  createLabelCustOrdProdData(reqObj.getTinter().getModel(), regularFont6, 8, "right");
			    	tbl6.addCell(tinterType);
				    
				    labelSection1.add(tbl6);
				    // 01/20/2017 - Enter Finish and Tinter Type.
				    
				    document.add(labelSection1);
	
				    //====================================================================================================
				    // Color Id, Color Name and Formula Type
				    //====================================================================================================
	
				    errorLocation = "Color I.D. & Name";
				    // Color i.d. and name.
				    Paragraph labelColorIdName = new Paragraph();
				    labelColorIdName.setFont(regularFont10);
				    labelColorIdName.setAlignment(Element.ALIGN_CENTER);
				    labelColorIdName.setLeading(lineSpacing8);
				    labelColorIdName.setSpacingAfter(lineSpacing2);
	
				    // 01/20/2017 - Begin Color I.D. and Color Name field build.
				    totalCharsLength = reqObj.getColorID().length() + reqObj.getColorName().length();
				    
				    //Modify input values, replace / and " with -
				    if(!StringUtils.isEmpty(reqObj.getColorID()) && !StringUtils.isEmpty(reqObj.getColorName())){
				    	reqObj.setColorID(reqObj.getColorID().replaceAll("\"|\\\\|\\~", "-"));
				    	reqObj.setColorName(reqObj.getColorName().replaceAll("\"|\\\\|\\~", "-"));
				    }
				    
				    // Truncate the Color Name to fit the space in line.  Color I.D. is maximum of 10.  Use the remaining space
				    // for the Color name.
				    if (totalCharsLength >= 22){
				    	int colorNameLength = 22 - reqObj.getColorID().length();
				    	if (reqObj.getColorName().length() > colorNameLength){
				    		reqObj.setColorName(reqObj.getColorName().substring(0, colorNameLength));
				    	}
				    }
				    Chunk colorIdName = new Chunk(reqObj.getColorID() + " " + reqObj.getColorName(), regularFont10);
				    // 01/20/2017 - End Color I.D. and Color Name.
				    // iText - add Chunk to Paragraph and Paragraph to Document.
				    labelColorIdName.add(colorIdName);
				    document.add(labelColorIdName);
				    
				    errorLocation = "Formula Type";
				    // Formula Type
				    Paragraph labelFormulaType = new Paragraph();
				    labelFormulaType.setFont(regularFont8);
				    labelFormulaType.setAlignment(Element.ALIGN_CENTER);
				    labelFormulaType.setLeading(lineSpacing8);
				    labelFormulaType.setSpacingAfter(lineSpacing2);
				    Chunk formulaType = new Chunk(reqObj.getDisplayFormula().getSourceDescr());
				    labelFormulaType.add(formulaType);
				    document.add(labelFormulaType);
	
				    errorLocation = "Blank line after formula";
				    // Blank Line
				    Paragraph blankLine9 = new Paragraph();
				    blankLine9.setFont(regularFont8);
				    blankLine9.setIndentationLeft(2);
				    blankLine9.setLeading(lineSpacing8);
				    Chunk blank3 = new Chunk(" ");
				    blankLine9.add(blank3);
				    document.add(blankLine9);
	
				    //====================================================================================================
				    // Formula Heading and 5 Colorant Lines maximum.
				    //====================================================================================================
				    errorLocation = "Formula Heading & Colorant Lines";
				    Paragraph formulaLines = new Paragraph(0);
				    PdfPTable tbl2 = new PdfPTable(5);
				    tbl2.setWidthPercentage(98);
				    tbl2.setWidths(new float[] { 58, 10, 10, 10, 12 });
				    tbl2.setSpacingBefore(0);
				    tbl2.setSpacingAfter(lineSpacing2);
				    
				    errorLocation = "Formula Label Line Headings";
				    // Formula Label Line Headings
				    List<String> listIncrementHdr = reqObj.getDisplayFormula().getIncrementHdr();
				    PdfPCell fcellHead1 =  createFormulaHeading(reqObj.getClrntSys() + " Colorant", regularFont7, 8, "left");
				    tbl2.addCell(fcellHead1);
				    PdfPCell fcellHead3 =  createFormulaHeading(listIncrementHdr.get(0), regularFont8, 8, "center");
				    tbl2.addCell(fcellHead3);
				    PdfPCell fcellHead4 =  createFormulaHeading(listIncrementHdr.get(1), regularFont8, 8, "center");
				    tbl2.addCell(fcellHead4);
				    PdfPCell fcellHead5 =  createFormulaHeading(listIncrementHdr.get(2), regularFont8, 8, "center");
				    tbl2.addCell(fcellHead5);
				    PdfPCell fcellHead6 =  createFormulaHeading(listIncrementHdr.get(3), regularFont8, 8, "center");
				    tbl2.addCell(fcellHead6);
		    
				    // Formula Label Line Item.
				    //List<FormulaIngredient> listFormulaIngredients = reqObj.getDisplayFormula().getIngredients();
				    errorLocation = "Formula Label Line Items";
				    int lineCtr = 0;
				    PdfPCell fcellLine1;
				    PdfPCell fcellLine3;
				    PdfPCell fcellLine4;
				    PdfPCell fcellLine5;
				    PdfPCell fcellLine6;
					if(listFormulaIngredients != null && listFormulaIngredients.size() > 0){
						// Process each instance of the listFormulaIngredients objects.
						for(FormulaIngredient line : listFormulaIngredients){
							int [] amount = line.getIncrement();
							// Add the available formula lines to the label.	
						    fcellLine1 =  createFormulaLine(line.getTintSysId() + " " + line.getName(), 
						    		regularFont7, 8, "left");
						    tbl2.addCell(fcellLine1);
	
						    if (! Integer.toString(amount[0]).equals("0")){
						    	fcellLine3 =  createFormulaLine(Integer.toString(amount[0]), regularFont8, 8, "right");
						    }
						    else {
						    	fcellLine3 =  createFormulaLine(" ", regularFont8, 8, "right");
						    }
						    tbl2.addCell(fcellLine3);
						    
						    if (! Integer.toString(amount[1]).equals("0")){
						    	fcellLine4 =  createFormulaLine(Integer.toString(amount[1]), regularFont8, 8, "right");
						    }
						    else {
							    fcellLine4 =  createFormulaLine(" ", regularFont8, 8, "right");
						    }
				    	    tbl2.addCell(fcellLine4);
	
						    if (! Integer.toString(amount[2]).equals("0")){
						    	fcellLine5 =  createFormulaLine(Integer.toString(amount[2]), regularFont8, 8, "right");
						    }
						    else {
							    fcellLine5 =  createFormulaLine(" ", regularFont8, 8, "right");
						    }
						    tbl2.addCell(fcellLine5);
	
						    if (! Integer.toString(amount[3]).equals("0")){
						    	fcellLine6 =  createFormulaLine(Integer.toString(amount[3]), regularFont8, 8, "center");
						    }
						    else {
							    fcellLine6 =  createFormulaLine(" ", regularFont8, 8, "right");
						    }
						    tbl2.addCell(fcellLine6);
	
						    lineCtr++;
						}
						// Complete adding 5 lines in total to label by adding blank lines.
						while (lineCtr < 5){
						    fcellLine1 =  createFormulaLine(" ", regularFont8, 8, "left");
						    tbl2.addCell(fcellLine1);
						    fcellLine3 =  createFormulaLine(" ", regularFont8, 8, "right");
						    tbl2.addCell(fcellLine3);
						    fcellLine4 =  createFormulaLine(" ", regularFont8, 8, "right");
						    tbl2.addCell(fcellLine4);
						    fcellLine5 =  createFormulaLine(" ", regularFont8, 8, "right");
						    tbl2.addCell(fcellLine5);
						    fcellLine6 =  createFormulaLine(" ", regularFont8, 8, "center");
						    tbl2.addCell(fcellLine6);
						    lineCtr++;
						}
					}	
					formulaLines.add(tbl2);
				    document.add(formulaLines);
	
				    //====================================================================================================
				    // Product Information
				    //====================================================================================================
				    // Build the label product and size lines 2 columns by 2 rows.
				    errorLocation = "Product Information";
				    Paragraph productInfo = new Paragraph(0);
				    productInfo.setSpacingBefore(2);
				    productInfo.setSpacingAfter(2);
	
				    PdfPTable tbl3 = new PdfPTable(2);
				    tbl3.setWidthPercentage(98);
				    tbl3.setWidths(new float[] { 40, 60});
				    tbl3.setSpacingBefore(0);
				    tbl3.setSpacingAfter(2);
				    
				    PdfPCell sizeText = createProductLine(reqObj.getSizeText(), regularFont8, 8, "left");
				    tbl3.addCell(sizeText);
	
				    // Abbreviating base type name to 16 characters to keep font size on label line.
				    if (reqObj.getBase().length() > 15)
				    	reqObj.setBase(reqObj.getBase().substring(0, 16));
				    
				    PdfPCell base = createProductLine(reqObj.getBase(), regularFont8, 8, "right");
				    tbl3.addCell(base);
	
				    PdfPCell prodNbr = createProductLine(reqObj.getProdNbr(), regularFont8, 8, "left");
				    tbl3.addCell(prodNbr);
	
				    PdfPCell salesNbr = createProductLine(reqObj.getSalesNbr(), regularFont8, 8, "right");
				    tbl3.addCell(salesNbr);
	
				    productInfo.add(tbl3);
				    document.add(productInfo);
	
				    //====================================================================================================
				    // Formula Messages.
				    //====================================================================================================
				    errorLocation = "Formula Messages";
				    Paragraph formulaMessages = new Paragraph(0);
				    formulaMessages.setSpacingBefore(1);
				    formulaMessages.setSpacingAfter(1);
	
				    PdfPTable tbl4 = new PdfPTable(1);
				    tbl4.setWidthPercentage(100);
				    tbl4.setSpacingBefore(1);
				    tbl4.setSpacingAfter(1);
				    
				    List<SwMessage> listSwMessages = reqObj.getCanLabelMsgs();
					int messageCount = 0;
					if (partMessage != null){
						PdfPCell mcellLine =  createMessageLine(partMessage, boldFont5, 8, "center");
						tbl4.addCell(mcellLine);
						messageCount++;
					}
					if(listSwMessages != null && listSwMessages.size() > 0){
						// Process each instance of the message list objects - maximum 3 messages.
						// Messages include Room by Room (not yet implemented), colorant warning and primer message.
						for(SwMessage message : listSwMessages){
							if (messageCount < 4 && message.getMessage().length() > 0){
								PdfPCell mcellLine =  createMessageLine(message.getMessage(), boldFont5, 8, "center");
								tbl4.addCell(mcellLine);
								messageCount++;
							}
						}
					}	
					
					while (messageCount < 4){
						PdfPCell mcellLine =  createMessageLine(" ", boldFont5, 8, "center");
						tbl4.addCell(mcellLine);
						messageCount++;
						}
					
					formulaMessages.add(tbl4);
				    document.add(formulaMessages);
				    
				    //====================================================================================================
				    // Customer Information - Building Code, Floor #, Room # and Surface.
				    //====================================================================================================
				    errorLocation = "Customer Information";
				    Paragraph customerInfo = new Paragraph(0);
				    customerInfo.setSpacingBefore(0);
				    customerInfo.setSpacingAfter(0); // chg
	
				    PdfPTable tbl5 = new PdfPTable(2);
				    tbl5.setWidthPercentage(100);
				    tbl5.setSpacingBefore(1);
				    tbl5.setSpacingAfter(1);
				    
				    
				    List<JobField> listJobField = reqObj.getJobFieldList();
				    
				    //Modify input values, replace / and " with -
				    if(!listJobField.isEmpty()){
				    	for (JobField jobField : listJobField) {
					    	jobField.setEnteredValue(jobField.getEnteredValue().replaceAll("\"|\\\\|\\~", "-"));
						}
				    }
				    
				    PdfPCell jcellLine1;
				    PdfPCell jcellLine2;
				    int jobCount = 0;
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
								jcellLine1 =  createJobLine(job.getScreenLabel() + ":", regularFont7, 8, "right");
								tbl5.addCell(jcellLine1);
								jcellLine2 =  createJobLine(job.getEnteredValue(), regularFont7, 8, "left");
								tbl5.addCell(jcellLine2);
								jobCount++;
							}
						}
					}	
				    while (jobCount < 5){
						jcellLine1 =  createJobLine(" ", regularFont7, 8, "right");
						tbl5.addCell(jcellLine1);
						jcellLine2 =  createJobLine(" ", regularFont7, 8, "left");
						tbl5.addCell(jcellLine2);
						jobCount++;
				    }
				    
					customerInfo.add(tbl5);
				    document.add(customerInfo);
	
				    //====================================================================================================
				    // Bar Code and Order and Line Numbers (part of bar code).
				    //====================================================================================================
				    errorLocation = "Bar Code & Order Number";
				    Paragraph barCodeLine = new Paragraph();
				    barCodeLine.setSpacingBefore(18); // chg
				    barCodeLine.setSpacingAfter(0);
	
				    PdfPTable barcodeTbl = new PdfPTable(1);
				    barcodeTbl.setWidthPercentage(80);
				    barcodeTbl.setSpacingBefore(0);
				    barcodeTbl.setSpacingAfter(0);
	
				    barCodeLine.setFont(regularFont8);
				    barCodeLine.setAlignment(Element.ALIGN_CENTER);
				    barCodeLine.setLeading(lineSpacing8);
				    
				    // TODO - Order and line numbers.  Is order number correct?  Where is line number available?
				    PdfPCell barcodeCell =  createBarcode(pdfWriter, String.format("%08d-%03d", 
				    		reqObj.getControlNbr(), 1));
				    barcodeTbl.addCell(barcodeCell);
	
					barCodeLine.add(barcodeTbl);
					document.add(barCodeLine);
				}

/*				// Label Pdf is completed.
			    document.close();
			   }*/
				catch(DocumentException de) {
					System.out.println("Failed in " + errorLocation);
					System.out.println("DocumentException: " + de.getMessage() + de.getCause() + de.getStackTrace());
					logger.error(de.getMessage());
				}
				catch(IOException ie) {
					System.out.println("Failed in " + errorLocation);
				   System.out.println("IOException: " + ie.getMessage() + ie.getCause() + ie.getStackTrace());
				   logger.error(ie.getMessage());
				}
		   	    catch(RuntimeException re){
		   	    	System.out.println("Failed in " + errorLocation);
		   		   System.out.println("RuntimeException: " + re.getMessage() + re.getCause() + re.getStackTrace());
		   		   logger.error(re.getMessage());
			   	}
			}
			
		 private PdfPCell createLabelCustOrdProdData(String cellValue, Font cellFont, int cellHeight, String cellAlign)
				 throws DocumentException, IOException {
			PdfPCell cell = new PdfPCell(new Phrase(cellValue, cellFont));
			cell.setMinimumHeight(cellHeight);
			   if (cellAlign.equals("left"))
				   cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			   if (cellAlign.equals("right"))
				   cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			   if (cellAlign.equals("center"))
				   cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   cell.disableBorderSide(Rectangle.BOX);
			   cell.setPaddingLeft(0);
			   cell.setPaddingRight(0);
			   cell.setPaddingTop(0);
			   cell.setPaddingBottom(0);
				   return cell;
			};

		   private PdfPCell createFormulaHeading(String cellValue, Font cellFont, int cellHeight, String cellAlign ) 
				   throws DocumentException, IOException {
			   PdfPCell cell = new PdfPCell(new Phrase(cellValue, cellFont));
			   cell.setMinimumHeight(cellHeight);
			   if (cellAlign.equals("left"))
				   cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			   if (cellAlign.equals("right"))
				   cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			   if (cellAlign.equals("center"))
				   cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   cell.disableBorderSide(Rectangle.BOX);
			   cell.setPaddingLeft(0);
			   cell.setPaddingRight(0);
			   cell.setPaddingTop(0);
			   cell.setPaddingBottom(0);
			   return cell;
		   }

		   private PdfPCell createFormulaLine(String cellValue, Font cellFont, int cellHeight, String cellAlign )
				   throws DocumentException, IOException {
			   PdfPCell cell = new PdfPCell(new Phrase(cellValue, cellFont));
			   cell.setMinimumHeight(cellHeight);
			   if (cellAlign.equals("left"))
				   cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			   if (cellAlign.equals("right"))
				   cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			   if (cellAlign.equals("center"))
				   cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   cell.disableBorderSide(Rectangle.BOX);
			   cell.setPaddingLeft(0);
			   cell.setPaddingRight(0);
			   cell.setPaddingTop(0);
			   cell.setPaddingBottom(0);
			   return cell;
		   }

		   private PdfPCell createMessageLine(String cellValue, Font cellFont, int cellHeight, String cellAlign )
				   throws DocumentException, IOException {
			   PdfPCell cell = new PdfPCell(new Phrase(cellValue, cellFont));
			   cell.setMinimumHeight(cellHeight);
			   if (cellAlign.equals("left"))
				   cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			   if (cellAlign.equals("right"))
				   cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			   if (cellAlign.equals("center"))
				   cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   cell.disableBorderSide(Rectangle.BOX);
			   cell.setPaddingLeft(0);
			   cell.setPaddingRight(0);
			   cell.setPaddingTop(0);
			   cell.setPaddingBottom(0);
			   return cell;
		   }

		   private PdfPCell createJobLine(String cellValue, Font cellFont, int cellHeight, String cellAlign )
				   throws DocumentException, IOException {
			   PdfPCell cell = new PdfPCell(new Phrase(cellValue, cellFont));
			   cell.setMinimumHeight(cellHeight);
			   if (cellAlign.equals("left"))
				   cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			   if (cellAlign.equals("right"))
				   cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			   if (cellAlign.equals("center"))
				   cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   cell.disableBorderSide(Rectangle.BOX);
			   cell.setPaddingLeft(0);
			   cell.setPaddingRight(0);
			   cell.setPaddingTop(0);
			   cell.setPaddingBottom(0);
			   return cell;
		   }

		   private PdfPCell createProductLine(String cellValue, Font cellFont, int cellHeight, String cellAlign )
				   throws DocumentException, IOException  {
			   PdfPCell cell = new PdfPCell(new Phrase(cellValue, cellFont));
			   cell.setMinimumHeight(cellHeight);
			   if (cellAlign.equals("left"))
				   cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			   if (cellAlign.equals("right"))
				   cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			   if (cellAlign.equals("center"))
				   cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   cell.disableBorderSide(Rectangle.BOX);
			   cell.setPaddingLeft(0);
			   cell.setPaddingRight(0);
			   cell.setPaddingTop(0);
			   cell.setPaddingBottom(0);
			   return cell;
		   }
		   
		   private PdfPCell createBarcode(PdfWriter writer, String code) throws DocumentException, IOException {
			   //Generate the barcode for label bottom. 
			   Barcode128 code128 = new Barcode128();
			   code128.setGenerateChecksum(true);
			   code128.setSize(8);
			   code128.setBarHeight(15);
			   code128.setCode(code);
		       PdfPCell cell = new PdfPCell(code128.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY), true);
			   cell.disableBorderSide(Rectangle.BOX);
			   cell.setPaddingLeft(0);
			   cell.setPaddingRight(0);
			   cell.setPaddingTop(0);
			   cell.setPaddingBottom(0);
		       return cell;
		   }
	
	}
