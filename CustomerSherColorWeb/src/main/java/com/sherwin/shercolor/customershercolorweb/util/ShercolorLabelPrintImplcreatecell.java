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
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;

/* 
 * Important Comment - Adobe PDF/iText does not process nulls embedded in the generated label causing an open error in PDF.  
 * A message box titled OLE Exception with message "Cannot create the in-place editor." occurs.  it is likely a null 
 * string field passed from the request object into the label.  If a change is made to request object, look specifically at 
 * a change for a potential cause of label PDF open failure.
 * */

public class ShercolorLabelPrintImplcreatecell implements ShercolorLabelPrint{
	static Logger logger = LogManager.getLogger(ShercolorLabelPrintImpl.class);

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
	      float margin = 0;
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
				            bottomMargin, tableWidth, margin, document, page, true, drawContent);
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
					document.addPage( page );
					// Create the 2" x 4" document.
					page.setMediaBox(new PDRectangle(0, 0 , 144f, 288f));
					
					
					
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
				    cellWidth = 50;
				   
				    Cell<PDPage> cell = row.createCell(cellWidth, reqObj.getCustomerName(), HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
				    cellSettings(cell,fontSize,rowHeight ,cellWidth);
				    // createLabelCustOrdProdData(row,reqObj.getCustomerName() , fontSize,rowHeight ,cellWidth, "left");
				    

				    // Order Date
				    Date date = new Date();
				    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
				    String strDate = sdf.format(date);
				    fontSize = 7;
				    rowHeight = 8;
				    cellWidth = 50;
				  
				    Cell<PDPage> cell1 = row.createCell(cellWidth, strDate, HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
				    cellSettings(cell1,fontSize,rowHeight ,cellWidth);
				    //createLabelCustOrdProdData(row,strDate, fontSize, rowHeight,cellWidth, "right");
				   
				   //---------------------------------------------------------------------------------
				    row = table.createRow(rowHeight);
				    // Optional Field - Not yet implemented.
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
				   //  createLabelCustOrdProdData(row," ", fontSize, rowHeight,cellWidth, "left");
	
				  //   createLabelCustOrdProdData(row," ", fontSize, rowHeight,cellWidth, "right");
	
				    table.draw();
				    
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
	private void cellSettings(Cell<PDPage> cell,float cellWidth, int fontSize,  float cellHeight)
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
			    BitmapCanvasProvider canvas1 = new BitmapCanvasProvider(
			        dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0
			    );
			    //Generate the barcode
			    code128Bean.generateBarcode(canvas1, barcodeString);
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
		   void addCenteredText(String text, PDFont font, int fontSize,  PDPage page, Point2D.Float offset) throws IOException {
			// Create the output file stream.
				PDPageContentStream content = new PDPageContentStream(document, page);
			   content.setFont(font, fontSize);
			    content.beginText();

			    // Rotate the text according to the page orientation
			    boolean pageIsLandscape = isLandscape(page);
			    Point2D.Float pageCenter = getCenter(page);

			    // We use the text's width to place it at the center of the page
			    float stringWidth = getStringWidth(text, font, fontSize);
			    if (pageIsLandscape) {
			        float textX = pageCenter.x - stringWidth / 2F + offset.x;
			        float textY = pageCenter.y - offset.y;
			        // Swap X and Y due to the rotation
			        content.setTextMatrix(Matrix.getRotateInstance(Math.PI / 2, textY, textX));
			    } else {
			        float textX = pageCenter.x - stringWidth / 2F + offset.x;
			        float textY = pageCenter.y + offset.y;
			        content.setTextMatrix(Matrix.getTranslateInstance(textX, textY));
			    }

			    content.showText(text);
			    content.endText();
			    content.close();
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
