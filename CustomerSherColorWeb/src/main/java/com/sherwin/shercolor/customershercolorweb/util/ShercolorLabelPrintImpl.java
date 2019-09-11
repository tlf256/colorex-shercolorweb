package com.sherwin.shercolor.customershercolorweb.util;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.beans.factory.annotation.Autowired;


import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Paragraph;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;

/* 
 * Important Comment - Adobe PDF/iText does not process nulls embedded in the generated label causing an open error in PDF.  
 * A message box titled OLE Exception with message "Cannot create the in-place editor." occurs.  it is likely a null 
 * string field passed from the request object into the label.  If a change is made to request object, look specifically at 
 * a change for a potential cause of label PDF open failure.
 * */

public class ShercolorLabelPrintImpl implements ShercolorLabelPrint{
	static Logger logger = LogManager.getLogger(ShercolorLabelPrintImplGood.class);

	@Autowired
	ProductService productService;
	
	//Creating exception string
	String errorLocation = "";
	
	// Create a new empty document
	PDDocument document = new PDDocument();
	String filename;
	RequestObject reqObj;
	
	  PDFont courierBold 	= PDType1Font.COURIER_BOLD;

	private float cellWidth;
	private static float WIDTH = 144f;
	

	public void CreateLabelPdf(String filename,RequestObject reqObj) {
		this.filename = filename;
		this.reqObj=reqObj;
		CreateLabelPdf();
	}
		public void CreateLabelPdf() {

		String partMessage = null;
		
		try {
			
			
			// Get formula ingredients (colorants) for processing.
			List<FormulaIngredient> listFormulaIngredients = reqObj.getDisplayFormula().getIngredients();
			// Determine the number of ingredient (colorant) lines in the formula.
			int formulaSize = reqObj.getDisplayFormula().getIngredients().size();

			// 5 or less lines in a formula - pass the one part label formula to formatting method.
			if (formulaSize <= 5){
				partMessage = " ";
				CreateLabelPdf( listFormulaIngredients, partMessage);
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
					CreateLabelPdf(  partAListFormulaIngredients, partMessage);
				}	
				// Skip to next page to write part B label.
				
				// Write the part B label on second page.
				if(partBListFormulaIngredients != null && partBListFormulaIngredients.size() > 0){
					partMessage = "* PART B - SEE PART A OF FORMULA *";
					CreateLabelPdf( partBListFormulaIngredients, partMessage);
				}	
			}
			// Label Pdf is completed.  1 or 2 labels will print.  Close the document.
		    // Save the results and ensure that the document is properly closed:
	      
	        document.save(filename);
	        document.close();
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
	private BaseTable createTable(PDPage page) {
	      float margin = 2;
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
				e.printStackTrace();
			}
			   return table;
	}
	public void CreateLabelPdf(
					
					List<FormulaIngredient> listFormulaIngredients,  
					String partMessage ) 
					 {
					int fontSize=2;
		   			int rowHeight = 2;
		// Create a new blank page and add it to the document
					PDPage page = new PDPage();

					// Create the 2" x 4" document.
					page.setMediaBox(new PDRectangle(0, 0 , 144f, 288f));
					 PDPageContentStream content = null;
					try {
						content = new PDPageContentStream(document, page);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				try{
					
					// step 1 - width and height - 2 (144) x 4 (288) inches.  One inch = 72 points.
					int lineSpacing8 = 8;   
					int lineSpacing2 = 2;   
					//Document document = new Document(new Rectangle(144f, 288f));
				   	// Label document set-up.
					//document.setMargins(3, 3, 3, 3);
					errorLocation = "Font Setup";
					/*
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
	*/
				    // Create the output file stream.
				    //PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
				    // Open document for formatting data.
				    //document.open();
	
					errorLocation = "Table Build";
				    // Build the label top 4 lines and 8 cells in a table.
				  //  Paragraph labelSection1 = new Paragraph(0);
				 //   labelSection1.setSpacingBefore(1);
				//    labelSection1.setSpacingAfter(1);
					//setup Table
					BaseTable table = createTable(page);
				  
				    errorLocation = "Name and Date";
				    // Customer Name
				   // contents.setFont(courierBold, 7);
				    rowHeight = 8;
				    fontSize = 7;
				    Row<PDPage> row = table.createRow(rowHeight);
				    cellWidth = 66;
				   
				    Cell<PDPage> cell = row.createCell(cellWidth, reqObj.getCustomerName(), HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
				    cellSettings(cell,fontSize,rowHeight ,cellWidth);
				    // createLabelCustOrdProdData(row,reqObj.getCustomerName() , fontSize,rowHeight ,cellWidth, "left");
				    

				    // Order Date
				    Date date = new Date();
				    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
				    String strDate = sdf.format(date);
				    fontSize = 7;
				    rowHeight = 8;
				    cellWidth = 34;
				  
				    Cell<PDPage> cell1 = row.createCell(cellWidth, strDate, HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
				    cellSettings(cell1,fontSize,rowHeight ,cellWidth);
				    //createLabelCustOrdProdData(row,strDate, fontSize, rowHeight,cellWidth, "right");
				   
				   //---------------------------------------------------------------------------------
				    row = table.createRow(rowHeight);
				    // Optional Field - Not yet implemented.
				    cellWidth = 50;
				    cell = row.createCell(cellWidth, "", HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
				    cellSettings(cell,fontSize,rowHeight ,cellWidth);
				    // createLabelCustOrdProdData(row," ", fontSize, rowHeight, cellWidth,"left");
				   
	
				    errorLocation = "Order Number & Blank Line";
				    fontSize = 7;
				    rowHeight = 7;
				    cellWidth = 50;
				    
				    // Order Number
				    Cell<PDPage> cell2 = row.createCell(cellWidth, "Order # " + 
				    		Integer.toString(reqObj.getControlNbr()), HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
				    cellSettings(cell2,fontSize,rowHeight ,cellWidth);
				  //   createLabelCustOrdProdData(row,"Order # " + 
		    		//Integer.toString(reqObj.getControlNbr()), 
		    		//fontSize, rowHeight,cellWidth, "right");
				    //-------------------------------------------------------------------
				    row = table.createRow(rowHeight);
				    
				    // Blank line
				    fontSize = 2;
				    rowHeight = 2;
				      Cell<PDPage> cell5 = row.createCell(cellWidth, "", HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
				    cellSettings(cell5,fontSize,rowHeight ,cellWidth);
				   //  createLabelCustOrdProdData(row," ", fontSize, rowHeight,cellWidth, "left");
				    Cell<PDPage> cell7 = row.createCell(cellWidth, "", HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
				    cellSettings(cell7,fontSize,rowHeight ,cellWidth);
				  //   createLabelCustOrdProdData(row,"", fontSize, rowHeight,cellWidth, "right");
	
				   // table.draw();
				    //----------------------------------------------------------------------------
				      //----------------------------------------------------------------------------------------------------------
				    errorLocation = "Use & Class";
				    // 01/20/2017 - Begin Use and Class
				  //  labelSection1.add(tbl);
				    float margin = 0;
			        float bottomMargin = 0;
			        // starting y position is whole page height subtracted by top and bottom margin
			        float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
			        // we want table across whole page width (subtracted by left and right margin ofcourse)
			        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
			        float yPosition = yStartNewPage;
			        boolean drawContent = true;
			        boolean drawLines = false;
				
			        /*BaseTable tbl0 = null;
					try {
						tbl0 = new BaseTable(250f, yStartNewPage,
						            bottomMargin, tableWidth, margin, document, page, true, drawContent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
				  
				    //----------------------------------------------------------------------------------------
				    // Use Interior/Exterior
				    
				    fontSize = 6;
				    rowHeight = 8;
				    cellWidth = 30;
				    row = table.createRow(rowHeight);
				      Cell<PDPage> intExt = row.createCell(cellWidth,reqObj.getIntExt(), HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
				    cellSettings(intExt,fontSize,rowHeight ,cellWidth);
				   // Cell<PDPage> intExt =  createLabelCustOrdProdData(row,reqObj.getIntExt(), fontSize, rowHeight, cellWidth, "left");
				 // Class
				    cellWidth = 70;
				     Cell<PDPage> klass = row.createCell(cellWidth, reqObj.getKlass(), HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
				    cellSettings(klass,fontSize,rowHeight ,cellWidth);
				    //Cell<PDPage> klass =  createLabelCustOrdProdData(row,reqObj.getKlass(), fontSize, rowHeight,cellWidth, "right");
				    
				//-----------------------------------------------------------------------------------------
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
			    	  errorLocation = "Quality";
					    // Quality
					    fontSize = 6;
					    rowHeight = 8;
					    cellWidth = setQualityParm;
					    row = table.createRow(rowHeight);
			        Cell<PDPage> quality = row.createCell(cellWidth,reqObj.getQuality(), HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
				    cellSettings(quality,fontSize,rowHeight ,cellWidth);
				   // Cell<PDPage> intExt =  createLabelCustOrdProdData(row,reqObj.getIntExt(), fontSize, rowHeight, cellWidth, "left");
				 // Class
				    errorLocation = "Composite";
				    // Composite
				    cellWidth = setCompositeParm;
				     Cell<PDPage> composite = row.createCell(cellWidth, reqObj.getComposite(), HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
				    cellSettings(composite,fontSize,rowHeight ,cellWidth);
				    //----------------------------------------------------------------------------------------------------
				    //labelSection1.add(tbl1);
	
				    // 01/20/2017 - End Quality and Composite
		
				    errorLocation = "Finish & Tinter Type";
				    // 01/20/2017 - Begin Finish and Tinter Type.
				    row = table.createRow(rowHeight);
				    cellWidth = 30f;
			        Cell<PDPage> finish = row.createCell(cellWidth,reqObj.getFinish(), HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
				    cellSettings(finish,fontSize,rowHeight ,cellWidth);
				   // Cell<PDPage> intExt =  createLabelCustOrdProdData(row,reqObj.getIntExt(), fontSize, rowHeight, cellWidth, "left");
				 // Class
				    errorLocation = "Tinter";
				    // Composite
				    cellWidth = 70f;
				     Cell<PDPage> tinter = row.createCell(cellWidth, reqObj.getTinter().getModel(), HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
				    cellSettings(tinter,fontSize,rowHeight ,cellWidth);
				    //----------------------------------------------------------------------
				    //====================================================================================================
				    // Color Id, Color Name and Formula Type
				    //====================================================================================================
	
				    errorLocation = "Color I.D. & Name";
	
	
				    // Color i.d. and name.
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
				    fontSize = 10;
				    rowHeight=12;
				    cellWidth=100f;
				 /*   //DJM trying boxable paragraph
				    Paragraph paragraph = new  Paragraph(reqObj.getColorID() + " " + reqObj.getColorName(), courierBold,  fontSize, WIDTH, HorizontalAlignment.CENTER); 
				   paragraph.setLineSpacing(lineSpacing8);
				    // paragraph.write(stream, cursorX, cursorY)
				    // Put the text at the page's center, no offset
				    Point2D.Float center = new Point2D.Float(0, 0);
				    //DJM's function replacing with paragraph above addCenteredText(reqObj.getColorID() + " " + reqObj.getColorName(), courierBold,  fontSize, page, center); 
				   */
				    row = table.createRow(rowHeight);
				    Cell<PDPage> color = row.createCell(cellWidth, reqObj.getColorID() + " " + reqObj.getColorName(), HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
				    cellSettings(color,fontSize,rowHeight ,cellWidth);
				    //--------------------------------------------------------------------------------------------------
				    errorLocation = "Formula Type";
				    // Formula Type
				    fontSize = 8;
				    rowHeight=8;
				    cellWidth=100f;
				    row = table.createRow(rowHeight);
				    Cell<PDPage> formulaType = row.createCell(cellWidth, reqObj.getDisplayFormula().getSourceDescr(), HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
				    cellSettings(formulaType,fontSize,rowHeight ,cellWidth);
				    table.draw();
				    //---------------------------------------------------------------------------------------------------------
				    //====================================================================================================
				    // Formula Heading and 5 Colorant Lines maximum.
				    //====================================================================================================
				    errorLocation = "Formula Heading & Colorant Lines";
				    //Paragraph formulaLines = new Paragraph(0);
				    BaseTable tbl2 = createTableFormula(page);
				    //tbl2.setWidthPercentage(98);
				    //tbl2.setWidths(new float[] { 58, 10, 10, 10, 12 });
				    //tbl2.setSpacingBefore(0);
				    //tbl2.setSpacingAfter(lineSpacing2);
				    
				    errorLocation = "Formula Label Line Headings";
				    // Formula Label Line Headings
				    row = tbl2.createRow(rowHeight);
				    fontSize = 7;
				    List<String> listIncrementHdr = reqObj.getDisplayFormula().getIncrementHdr();
				    //(Row<PDPage> row, String cellValue, int fontSize,  float cellHeight, float cellWidth, String cellAlign)
				    Cell<PDPage> fcellHead1 =  createFormulaHeading(row,reqObj.getClrntSys() + " Colorant",52f, 7,  "left");
				    
				    Cell<PDPage> fcellHead3 =  createFormulaHeading(row,listIncrementHdr.get(0),11f, 8, "center");
				   
				    Cell<PDPage> fcellHead4 =  createFormulaHeading(row,listIncrementHdr.get(1), 11f,8, "center");
				   
				    Cell<PDPage> fcellHead5 =  createFormulaHeading(row,listIncrementHdr.get(2), 11f,8, "center");
				    
				    Cell<PDPage> fcellHead6 =  createFormulaHeading(row,listIncrementHdr.get(3), 14f,8, "center");
				   //--------------------------------------------------------------------------------------------------------
				    // Formula Label Line Item.
				    //List<FormulaIngredient> listFormulaIngredients = reqObj.getDisplayFormula().getIngredients();
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
							  row = tbl2.createRow(rowHeight);
						    fcellLine1 =  createFormulaLine(row,line.getTintSysId() + " " + line.getName(),51f,8,"left");
						   
	
						    if (! Integer.toString(amount[0]).equals("0")){
						    	fcellLine3 =  createFormulaLine(row,Integer.toString(amount[0]), 11f, 8, "right");
						    }
						    else {
						    	fcellLine3 =  createFormulaLine(row," ", 11f, 8, "right");
						    }
						   
						    
						    if (! Integer.toString(amount[1]).equals("0")){
						    	fcellLine4 =  createFormulaLine(row,Integer.toString(amount[1]), 11f, 8, "right");
						    }
						    else {
							    fcellLine4 =  createFormulaLine(row," ", 11f, 8, "right");
						    }
				
						    if (! Integer.toString(amount[2]).equals("0")){
						    	fcellLine5 =  createFormulaLine(row,Integer.toString(amount[2]), 11f,8, "right");
						    }
						    else {
							    fcellLine5 =  createFormulaLine(row," ", 11f, 8, "right");
						    }
						 
	
						    if (! Integer.toString(amount[3]).equals("0")){
						    	fcellLine6 =  createFormulaLine(row,Integer.toString(amount[3]), 12f, 8, "right");
						    }
						    else {
							    fcellLine6 =  createFormulaLine(row," ", 16f,8, "right");
						    }
						 
	
						    lineCtr++;
						}
						// Complete adding 5 lines in total to label by adding blank lines.
						while (lineCtr < 5){
						    fcellLine1 =  createFormulaLine(row," ", 50f, 8, "left");
						   
						    fcellLine3 =  createFormulaLine(row," ", 14f, 8, "right");
						 
						    fcellLine4 =  createFormulaLine(row," ", 14f, 8, "right");
						    
						    fcellLine5 =  createFormulaLine(row," ", 14f, 8, "right");
						   
						    fcellLine6 =  createFormulaLine(row," ", 16f, 8, "right");
						   
						    lineCtr++;
						}
					}
				    tbl2.draw();
				    //***********************************************************************************
				    //====================================================================================================
				    // Product Information
				    //====================================================================================================
				    // Build the label product and size lines 2 columns by 2 rows.
				    BaseTable prodInfoMsgTable = createTableProd(page);
					  
				    errorLocation = "Size and Base";
				    // Customer Name
				   // contents.setFont(courierBold, 7);
				    //---------------------------------------------------------------------------------------
				    rowHeight = 8;
				    fontSize = 8;
				    Row<PDPage> prodRow = prodInfoMsgTable.createRow(rowHeight);
				    cellWidth = 50;
				   
				    Cell<PDPage> sizeCell = prodRow.createCell(cellWidth, reqObj.getSizeText(), HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
				    cellSettings(sizeCell,fontSize,rowHeight ,cellWidth);
				    // createLabelCustOrdProdData(row,reqObj.getCustomerName() , fontSize,rowHeight ,cellWidth, "left");
				   
				  
				    fontSize = 8;
				    rowHeight = 8;
				    cellWidth = 50;
				    // Abbreviating base type name to 16 characters to keep font size on label line.
				    if (reqObj.getBase().length() > 15)
				    	reqObj.setBase(reqObj.getBase().substring(0, 16));
				    
				    Cell<PDPage> baseCell = prodRow.createCell(cellWidth, reqObj.getBase(), HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
				    cellSettings(baseCell,fontSize,rowHeight ,cellWidth);
				    //---------------------------------------------------------------------------------------
				    rowHeight = 8;
				    fontSize = 8;
				    Row<PDPage> prodNumRow = prodInfoMsgTable.createRow(rowHeight);
				    cellWidth = 50;
				   
				    Cell<PDPage> prodNumCell = prodNumRow.createCell(cellWidth, reqObj.getProdNbr(), HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
				    cellSettings(prodNumCell,fontSize,rowHeight ,cellWidth);
				    // createLabelCustOrdProdData(row,reqObj.getCustomerName() , fontSize,rowHeight ,cellWidth, "left");
				    
				    //---------------------------------------------------------------------------------------
				  
				    fontSize = 8;
				    rowHeight = 8;
				    cellWidth = 50;
				    // Abbreviating base type name to 16 characters to keep font size on label line.
				    if (reqObj.getBase().length() > 15)
				    	reqObj.setBase(reqObj.getBase().substring(0, 16));
				    
				    Cell<PDPage> salesCell = prodNumRow.createCell(cellWidth, reqObj.getSalesNbr(), HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
				    cellSettings(salesCell,fontSize,rowHeight ,cellWidth);
				
				    //====================================================================================================
				    // Formula Messages.
				    //====================================================================================================
				    errorLocation = "Formula Messages";
				    //---------------------------------------------------------------------
				    fontSize = 6;
				    rowHeight=8;
				    cellWidth=98f;
				    row = prodInfoMsgTable.createRow(rowHeight);
				    
				    List<SwMessage> listSwMessages = reqObj.getCanLabelMsgs();
					int messageCount = 0;
					if (partMessage != null){
						//Cell<PDPage> mcellLine =  createMessageLine(partMessage, boldFont5, 8, "center");
						  row = prodInfoMsgTable.createRow(rowHeight);
						  Cell<PDPage> partCell = row.createCell(cellWidth, partMessage, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
						  cellSettings(partCell,fontSize,rowHeight ,cellWidth);
						messageCount++;
					}
					if(listSwMessages != null && listSwMessages.size() > 0){
						// Process each instance of the message list objects - maximum 3 messages.
						// Messages include Room by Room (not yet implemented), colorant warning and primer message.
						   fontSize = 7;
						    rowHeight=8;
						for(SwMessage message : listSwMessages){
							if (messageCount < 4 && message.getMessage().length() > 0){
								  row = prodInfoMsgTable.createRow(rowHeight);
								  Cell<PDPage> msgCell = row.createCell(cellWidth, message.getMessage(), HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
								  cellSettings(msgCell,fontSize,rowHeight ,cellWidth);
								//Cell<PDPage> mcellLine =  createMessageLine(message.getMessage(), boldFont5, 8, "center");
								//tbl4.addCell(mcellLine);
								messageCount++;
							}
						}
					}	
					  fontSize = 7;
					    rowHeight=8;
					while (messageCount < 4){
						  row = prodInfoMsgTable.createRow(rowHeight);
						  Cell<PDPage> msgCell = row.createCell(cellWidth, " ", HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
						  cellSettings(msgCell,fontSize,rowHeight ,cellWidth);
						messageCount++;
						}
//------------------------------djm-------------------------
					List<JobField> listJobField = reqObj.getJobFieldList();
				    
				    //Modify input values, replace / and " with -
				    if(!listJobField.isEmpty()){
				    	for (JobField jobField : listJobField) {
					    	jobField.setEnteredValue(jobField.getEnteredValue().replaceAll("\"|\\\\|\\~", "-"));
						}
				    }
				    
				    Cell<PDPage> jcellLine1;
				    Cell<PDPage> jcellLine2;
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
								cellWidth=50f;
								  fontSize = 7;
								  rowHeight=8;
								  row = prodInfoMsgTable.createRow(rowHeight);
								  Cell<PDPage> jobCell = row.createCell(cellWidth, job.getScreenLabel()+":", HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
								  cellSettings(jobCell,fontSize,rowHeight ,cellWidth);
								//-----------------------------------------------------------------------------------  
								//jcellLine1 =  createJobLine(job.getScreenLabel() + ":", courierBold, 8, "right");
								//tbl5.addCell(jcellLine1);
								//jcellLine2 =  createJobLine(job.getEnteredValue(), regularFont7, 8, "left");
								//tbl5.addCell(jcellLine2);
								
								  Cell<PDPage> enteredCell = row.createCell(cellWidth, job.getEnteredValue(), HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
								  cellSettings(enteredCell,fontSize,rowHeight ,cellWidth);
								  //-----------------------------------------------------------------
								jobCount++;
							}
						}
					}	
				    while (jobCount < 5){
				    	  fontSize = 7;
						  rowHeight=8;
				  	  Cell<PDPage> enteredCell = row.createCell(cellWidth, " ", HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
					  cellSettings(enteredCell,fontSize,rowHeight ,cellWidth);
						jobCount++;
				    }
				    prodInfoMsgTable.draw();
				    //******************************************************************************************
				    //====================================================================================================
				    // Bar Code and Order and Line Numbers (part of bar code).
				    //====================================================================================================
				    errorLocation = "Bar Code & Order Number";
				    
				    
				   
				    String barCodeChars = String.format("%08d-%03d",reqObj.getControlNbr(), 1);
		    		BufferedImage bufferedImage = geBufferedImageForCode128Bean(barCodeChars);
		    		PDImageXObject pdImage = JPEGFactory.createFromImage(document, bufferedImage);
		    		
		    		content.drawImage(pdImage, 20, 12, 100, 15);
		    		
		    		
		    		//(String text, PDFont font, int fontSize,  PDPage page, Point2D.Float offset)
		    		//add human readable under barcode
		    		
		    		addCenteredText(content,barCodeChars,courierBold,8,page,4.0f);
		    		content.close();
		    		document.addPage( page );
				}
				
				catch(IOException ie) {
					System.out.println("Failed in " + errorLocation);
				   System.out.println("IOException: " + ie.getMessage() + ie.getCause() + ie.getStackTrace());
				   logger.error(ie.getMessage());
				}
		   	    catch(RuntimeException re){
		   	    	System.out.println("Failed in " + errorLocation);
		   		   System.out.println("RuntimeException: " + re.getMessage() + re.getCause() + re.getStackTrace());
		   		   re.printStackTrace();
		   		   logger.error(re.getMessage());
			   	}
			}
	
	private void cellSettings(Cell<PDPage> cell, int fontSize, float cellHeight, float cellWidth )
	{
		
		cell.setFontBold(courierBold);
		cell.setFont(courierBold);
		
		cell.setFontSize(fontSize);
		
	//	cell.setHeight(cellHeight);

		
		cell.setLeftPadding(0);
		cell.setRightPadding(0);
		cell.setTopPadding(0);
		cell.setBottomPadding(0);
	
		

	}
	private BaseTable createTableFormula(PDPage page) {
	      float margin = 2f;
	        float bottomMargin = 0;
	        // starting y position is whole page height subtracted by top and bottom margin
	        float yStartNewPage = page.getMediaBox().getHeight();
	        // we want table across whole page width (subtracted by left and right margin ofcourse)
	        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
	        float yPosition = 211f;
	        boolean drawContent = true;
	        boolean drawLines = false;
			BaseTable table = null;
			try {
				table = new BaseTable(yPosition, yStartNewPage,
				            bottomMargin, tableWidth, margin, document, page, drawLines, drawContent);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   return table;
	}
	   private Cell<PDPage> createFormulaHeading(Row<PDPage> row, String cellValue, float cellWidth,int fontSize, String cellAlign)
		  {
		   row.setHeaderRow(true);
	 
	 Cell<PDPage> cell = row.createCell(cellWidth,cellValue);
	 cell.setFontBold(courierBold);
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
			 cell.setFontBold(courierBold);
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
				e.printStackTrace();
			}
			   return table;
	}
		/*	
		 private void createLabelCustOrdProdData(Row<PDPage> row, String cellValue, int fontSize,  float cellHeight, float cellWidth, String cellAlign)
				  {
			 Cell<PDPage> cell = row.createCell(cellWidth,cellValue);
			 cell.setFontBold(courierBold);
			 cell.setFontSize(fontSize);
			 cell.setValign(VerticalAlignment.MIDDLE);
			 cell.setHeight(cellHeight);
			
			//cell.setMinimumHeight(cellHeight);
			   if (cellAlign.equals("left"))
				   cell.setAlign(HorizontalAlignment.LEFT);
			   if (cellAlign.equals("right"))
				   cell.setAlign(HorizontalAlignment.RIGHT);
			   if (cellAlign.equals("center"))
				   cell.setAlign(HorizontalAlignment.CENTER);
			//   cell.disableBorderSide(Rectangle.BOX);
		       cell.setLeftPadding(0);
			   cell.setRightPadding(0);
			   cell.setTopPadding(0);
			   cell.setBottomPadding(0);
			  
				  
			};
*/
	
	/*  DJM original
		   private Cell<PDPage> createFormulaHeading(Row<PDPage> row, String cellValue, int fontSize,  float cellHeight, String cellAlign)
			  {
		 Cell<PDPage> cell = row.createCell(cellValue);
		 cell.setFontBold(courierBold);
		 cell.setFontSize(fontSize);
		 cell.setValign(VerticalAlignment.MIDDLE);
		 cell.setHeight(cellHeight);
		
		//cell.setMinimumHeight(cellHeight);
		   if (cellAlign.equals("left"))
			   cell.setAlign(HorizontalAlignment.LEFT);
		   if (cellAlign.equals("right"))
			   cell.setAlign(HorizontalAlignment.RIGHT);
		   if (cellAlign.equals("center"))
			   cell.setAlign(HorizontalAlignment.CENTER);
		   cell.setLeftPadding(0);
		   cell.setRightPadding(0);
		   cell.setTopPadding(0);
		   cell.setBottomPadding(0);
			   return cell;
		};

		   private Cell<PDPage> createFormulaLine(Row<PDPage> row, String cellValue, int fontSize,  float cellHeight, String cellAlign)
			  {
		 Cell<PDPage> cell = row.createCell(cellValue);
		 cell.setFontBold(courierBold);
		 cell.setFontSize(fontSize);
		 cell.setValign(VerticalAlignment.MIDDLE);
		 cell.setHeight(cellHeight);
		
		//cell.setMinimumHeight(cellHeight);
		   if (cellAlign.equals("left"))
			   cell.setAlign(HorizontalAlignment.LEFT);
		   if (cellAlign.equals("right"))
			   cell.setAlign(HorizontalAlignment.RIGHT);
		   if (cellAlign.equals("center"))
			   cell.setAlign(HorizontalAlignment.CENTER);
		   cell.setLeftPadding(0);
		   cell.setRightPadding(0);
		   cell.setTopPadding(0);
		   cell.setBottomPadding(0);
			   return cell;
		};
*/
		   private Cell<PDPage> createMessageLine(Row<PDPage> row, String cellValue, int fontSize,  float cellHeight, String cellAlign)
			  {
		 Cell<PDPage> cell = row.createCell(cellValue);
		 cell.setFontBold(courierBold);
		 cell.setFontSize(fontSize);
		 cell.setValign(VerticalAlignment.MIDDLE);
		 cell.setHeight(cellHeight);
		
		//cell.setMinimumHeight(cellHeight);
		   if (cellAlign.equals("left"))
			   cell.setAlign(HorizontalAlignment.LEFT);
		   if (cellAlign.equals("right"))
			   cell.setAlign(HorizontalAlignment.RIGHT);
		   if (cellAlign.equals("center"))
			   cell.setAlign(HorizontalAlignment.CENTER);
		   cell.setLeftPadding(0);
		   cell.setRightPadding(0);
		   cell.setTopPadding(0);
		   cell.setBottomPadding(0);
			   return cell;
		};

		   private Cell<PDPage> createJobLine(Row<PDPage> row, String cellValue, int fontSize,  float cellHeight, String cellAlign)
			  {
		 Cell<PDPage> cell = row.createCell(cellValue);
		 cell.setFontBold(courierBold);
		 cell.setFontSize(fontSize);
		 cell.setValign(VerticalAlignment.MIDDLE);
		 cell.setHeight(cellHeight);
		
		//cell.setMinimumHeight(cellHeight);
		   if (cellAlign.equals("left"))
			   cell.setAlign(HorizontalAlignment.LEFT);
		   if (cellAlign.equals("right"))
			   cell.setAlign(HorizontalAlignment.RIGHT);
		   if (cellAlign.equals("center"))
			   cell.setAlign(HorizontalAlignment.CENTER);
		   cell.setLeftPadding(0);
		   cell.setRightPadding(0);
		   cell.setTopPadding(0);
		   cell.setBottomPadding(0);
			   return cell;
		};

		   private Cell<PDPage> createProductLine(Row<PDPage> row, String cellValue, int fontSize,  float cellHeight, String cellAlign)
			  {
		 Cell<PDPage> cell = row.createCell(cellValue);
		 cell.setFontBold(courierBold);
		 cell.setFontSize(fontSize);
		 cell.setValign(VerticalAlignment.MIDDLE);
		 cell.setHeight(cellHeight);
		
		//cell.setMinimumHeight(cellHeight);
		   if (cellAlign.equals("left"))
			   cell.setAlign(HorizontalAlignment.LEFT);
		   if (cellAlign.equals("right"))
			   cell.setAlign(HorizontalAlignment.RIGHT);
		   if (cellAlign.equals("center"))
			   cell.setAlign(HorizontalAlignment.CENTER);
		   cell.setLeftPadding(0);
		   cell.setRightPadding(0);
		   cell.setTopPadding(0);
		   cell.setBottomPadding(0);
			   return cell;
		};
		   
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
					e.printStackTrace();
				}
			    return canvas1.getBufferedImage();
			}
		   /*
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
		   */
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
			        System.out.println("Can only handle pages that are rotated in 90 degree steps. This page is rotated  " + rotation + " degrees. Will treat the page as in portrait format");
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
	
	}
