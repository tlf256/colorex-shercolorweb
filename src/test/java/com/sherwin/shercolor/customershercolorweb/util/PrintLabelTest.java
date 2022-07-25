package com.sherwin.shercolor.customershercolorweb.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;
import com.sherwin.shercolor.util.domain.SwMessage;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.DrawdownLabelService;
import com.sherwin.shercolor.common.service.FormulationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
@Transactional
public class PrintLabelTest  {
	
	@Autowired
	DrawdownLabelService drawdownLabelService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ColorMastService colorMastService;
	
	@Autowired
	FormulationService formulationService;
	
	private RequestObject reqObj = new RequestObject();
	private List<FormulaIngredient> listIngredients;
	private FormulaInfo formulaInfo = new FormulaInfo();
	private List<SwMessage> formulaMessages;
	private FormulationResponse formulationResponse = new FormulationResponse();
	private List<JobField> listJobField;
	ShercolorLabelPrintImpl printLabel;


	private String label1[] = {
			"label.pdf", "SHERWIN-WILLIAMS", "6385", "DOVER WHITE", "640512901", 
			"A87W00051", "SUPER PAINT", "LATEX", "EXTRA WHITE", "SATIN", "ARCHITECTURAL", 
			"INTERIOR", "ONE GALLON", "CCE", "LB6110", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", " ", "33", "16"};
	
	private String formula1[] = { 
			"B1", "BLACK", "R2", "MAROON", "Y3", "DEEP GOLD", "N1", "RAW UMBER", "R4", "RED"};

	private String message1[] = {
			" ", " ", " "};
			
	private String job1[] = {
			"CCF Main", "Location Name", "H", "Building Code", "6th", "Floor", "626", "Room", "Wall", "Surface Type", 
			"Special One", "Comment"};
			
	private String label2[] = {
			"label2.pdf", "SHERWIN-WILLIAMS", "0001", "MULBERRY SILK", "640512901", 
			"B20W02653", "PROMAR 200 ZERO VOC", "ACRYLIC LATEX", "DEEP", "EG-SHELL", "ARCHITECTURAL", 
			"INTERIOR", "ONE GALLON", "CCE", "LB6110", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", "31", "16"};
	
	private String formula2[] = { 
			"B1", "BLACK", "R2", "RED OXIDE", "Y3", "YELLOW OXIDE"};

	private String message2[] = {
			"<Room by Room message 1>", "<Colorant message 2>", "P2 PRIMER RECOMMENDED FOR THIS COLOR"};
			
	private String job2[] = {
			"CCF Main", "Location Name", "H", "Building Code", "", "", "", "Room", "Wall", "Surface Type",
			"Two", "Comment"};

	private String label3[] = {
			"label4.pdf", "SHERWIN-WILLIAMS", "01234567890", "MULBERRY90123", "650186935", 
			"B20W02653", "123456789012345678", "1234567890123456789", "DEEP5678901234567890", "EG-SHELL901234567890", "ARCHITECTURAL567890", 
			"INTERIOR90", "FIVE GALLON", "CCE", "LB6110", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", 
			"1270003", "16"};
	
	private String formula3[] = { 
			"B1", "BLACK", "R2", "RED OXIDE", "Y3", "DEEP GOLD", "XX", "XXXXXXXXXX"};

	private String message3[] = {
			"<Room by Room message 1>", "<Colorant message 2>", "P2 PRIMER RECOMMENDED FOR THIS COLOR"};
			
	private String job3[] = {
			"CCF Main", "Location Name", "H", "Building Code", "", "", "222", "Room", "Wall", "Surface Type", 
				"012345678901234567890", "Painter's Commentx"};
	
	private String label4[] = {
			"label4.pdf", "SHERWIN-WILLIAMS", "0002", "CHELSEA MAUVE456789012", "650186935", 
			"B20W02651", "123456789012345678", "1234567890123456789", "DEEP567890123456789", "EG-SHELL901234567890", "ARCHITECTURAL567890", 
			"INTERIOR90", "FIVE GALLON", "CCE", "LB6110", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", 
			"1270004", "14"};
	
	private String formula4[] = { 
			"B1", "BLACK", "R2", "RED OXIDE", "Y3", "DEEP GOLD", "XX", "XXXXXXXXXX"};

	private String message4[] = {
			"<Room by Room message 1>", "<Colorant message 2>", "P2 PRIMER RECOMMENDED FOR THIS COLOR"};
			
	private String job4[] = {
			"CCF Main", "Location Name", "H", "Building Code", "", "", "222", "Room", "Wall", "Surface Type", 
				"012345678901234567890", "Painter's Commentx"};

	private String label5[] = {
			"label25.pdf", "SHERWIN-WILLIAMS", "0003", "CABBAGE ROSE", "650096514", 
			"A96W01251", "DURATION HOME", "LATEX", "EXTRA WHITE", "MATTE", "ARCHITECTURAL", 
			"INTERIOR", "ONE GALLON", "CCE", "LB6110", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", 
			"4200001", "16"};
	
	private String formula5[] = { 
			"B1", "BLACK", "R2", "RED OXIDE", "Y3", "DEEP GOLD"};

	private String message5[] = {
			"<Room by Room message 1>", "<Colorant message 2>", "<Primer message 3>"};
			
	private String job5[] = {
			"CCF Hillcrest", "Location Name", "G", "Building Code", "", "", "555", "Room", "Wall", "Surface Type", 
				"THIS IS A NEW LABEL", "Painter's Comment"};
	
	private String label6[] = {
			"label26.pdf", "SHERWIN-WILLIAMS", "MANUAL", "CABBAGE ROSE", "650096514", 
			"A96W01251", "DURATION HOME", "LATEX", "EXTRA WHITE", "MATTE", "ARCHITECTURAL", 
			"INTERIOR", "ONE GALLON", "CCE", "LB6110", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", 
			"4200001", "16"};
	
	private String formula6[] = { 
			"B1", "BLACK", "R2", "RED OXIDE", "Y3", "DEEP GOLD", "N1", "RAW UMBER", "W1", "WHITE", "L1", "BLUE" };

	private String message6[] = {
			"<Room by Room message 1>", "<Colorant message 2>", "<Primer message 3>"};
			
	private String job6[] = {
			"CCF Hillcrest", "Location Name", "G", "Building Code", "", "", "555", "Room", "Wall", "Surface Type", 
				"THIS IS A NEW LABEL", "Painter's Comment"};
	
	private String label7[] = {
			"label26.pdf", "SHERWIN-WILLIAMS", "MANUAL", "CABBAGE ROSE", "650096514", 
			"A96W01251", "DURATION HOME", "LATEX", "EXTRA WHITE", "MATTE", "ARCHITECTURAL", 
			"INTERIOR", "ONE GALLON", "CCE", "400000002", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", 
			"4200001", "16"};
	
	private String formula7[] = { 
			"B1", "BLACK", "R2", "RED OXIDE", "Y3", "DEEP GOLD", "N1", "RAW UMBER", "W1", "WHITE", "L1", "BLUE", "R3", "MAGENTA" };

	private String message7[] = {
			"Room by Room message 1", "<Colorant message 2>", "Primer message 3"};
	
	private String job7[] = {
			"Location", "BTC", "Customer", "John Doe", "Store CCN", "999", "Job", "123", "Project Info", "Wall", "Schedule", "TBD", "Control Number", "987654"};
	
	private String formula8[] = {"",""};
	
	private String label8[] = {
			"label27.pdf", "COMPETITIVE", "PPG", "WHITEWATER", "650096514", 
			"A96W01251", "DURATION HOME", "LATEX", "EXTRA WHITE", "MATTE", "ARCHITECTURAL", 
			"INTERIOR", "ONE GALLON", "CCE", "400000002", "CLEVELAND CLINIC", "CUSTOM SHER-COLOR-MATCH", "P2", 
			"4200001", "16"};
	
	private String formula9[] = { 
			"B1", "BLACK", "G2", "NEW GREEN", "Y3", "DEEP GOLD"};

	@Before
	public void testInitPrintService() {
		printLabel = new ShercolorLabelPrintImpl(drawdownLabelService,customerService,colorMastService,formulationService);
	}
	
	//Store Labels
	@Test
	public void test()  {
		reqObj = BuildReqObject(label1, formula1, message1, job1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
	}
	
	@Test
	public void test2()  {
		reqObj = BuildReqObject(label2, formula2, message2, job2);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
	}
	
	@Test
	public void test3()  {
		reqObj = BuildReqObject(label3, formula3, message3, job3);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
	}
	
	@Test
	public void test4()  {
		reqObj = BuildReqObject(label4, formula4, message4, job4);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
	}
	
	@Test
	public void test5()  {
		reqObj = BuildReqObject(label5, formula5, message5, job5);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
	}
	
	@Test
	public void test6()  {
		reqObj = BuildReqObject(label6, formula6, message7, job6);
		reqObj.setTinter(null);
		reqObj.getDisplayFormula().setSourceDescr("VINYL SAFE FORMULA");
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
	}
	
	//Self Tinting Customer Label.
	@Test
	public void test7()  {
		reqObj = BuildReqObject(label1, formula1, message1, job1);
		printLabel.createLabelPdf(reqObj,"selfTintCustLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("selfTintCustLabel"));
	}
	
	@Test
	public void test8()  {
		reqObj = BuildReqObject(label2, formula2, message2, job2);
		printLabel.createLabelPdf(reqObj,"selfTintCustLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("selfTintCustLabel"));
	}
	
	@Test
	public void test9()  {
		reqObj = BuildReqObject(label3, formula3, message3, job3);
		printLabel.createLabelPdf(reqObj,"selfTintCustLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("selfTintCustLabel"));
	}
	
	@Test
	public void test10()  {
		reqObj = BuildReqObject(label4, formula4, message4, job4);
		printLabel.createLabelPdf(reqObj,"selfTintCustLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("selfTintCustLabel"));
	}
	
	@Test
	public void test11()  {
		reqObj = BuildReqObject(label5, formula5, message5, job5);
		printLabel.createLabelPdf(reqObj,"selfTintCustLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("selfTintCustLabel"));
	}
	
	@Test
	public void test12()  {
		reqObj = BuildReqObject(label6, formula6, message6, job6);
		reqObj.setTinter(null);
		printLabel.createLabelPdf(reqObj,"selfTintCustLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("selfTintCustLabel"));

	}
	
	//Drawdown Label
	@Test
	public void test13()  {
		reqObj = BuildReqObject(label1, formula1, message1, job1);
		printLabel.createLabelPdf(reqObj,"drawdownStoreLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("drawdownStoreLabel"));

	}
	
	@Test
	public void test14()  {
		reqObj = BuildReqObject(label2, formula2, message2, job2);
		printLabel.createLabelPdf(reqObj,"drawdownStoreLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("drawdownStoreLabel"));

	}
	
	@Test
	public void test15()  {
		reqObj = BuildReqObject(label3, formula3, message3, job3);
		printLabel.createLabelPdf(reqObj,"drawdownStoreLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("drawdownStoreLabel"));

	}
	
	@Test
	public void test16()  {
		reqObj = BuildReqObject(label4, formula4, message4, job4);
		printLabel.createLabelPdf(reqObj,"drawdownStoreLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("drawdownStoreLabel"));

	}
	
	@Test
	public void test17()  {
		reqObj = BuildReqObject(label5, formula5, message5, job5);
		printLabel.createLabelPdf(reqObj,"drawdownStoreLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("drawdownStoreLabel"));

		
	}
	
	@Test
	public void test18()  {
		reqObj = BuildReqObject(label6, formula6, message6, job6);
		printLabel.createLabelPdf(reqObj,"drawdownStoreLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("drawdownStoreLabel"));
	}
	
	//invalid label type
	@Test
	public void test19()  {
		reqObj = BuildReqObject(label7, formula7, message7, job7);
		printLabel.createLabelPdf(reqObj,"InvalidLabelType","PORTRAIT","","",false,null);
		assertFalse(validatePdf("InvalidLabelType"));
	}
	
	//drawdown label test
	@Test
	public void test20()  {
		reqObj = BuildReqObject(label7, formula7, message7, job7);
		printLabel.createLabelPdf(reqObj,"drawdownLabel","LANDSCAPE","","",false,null);
		assertTrue(validatePdf("drawdownLabel"));
	}
	
	//Sample label tests
	@Test
	public void test21()  {
		reqObj = BuildReqObject(label7, formula7, message7, job7);
		reqObj.setTinter(null);
		printLabel.createLabelPdf(reqObj,"sampleCanLabel","PORTRAIT","","B1-BLACK,1,W1-WHITE,2,,",false,null);
		assertTrue(validatePdf("sampleCanLabel"));
	}
	
	@Test
	public void test22()  {
		reqObj = BuildReqObject(label7, formula7, message7, job7);
		printLabel.createLabelPdf(reqObj,"sampleCanLabel","PORTRAIT","","B1-BLACK,1,W1-WHITE,2,L1-BLUE,3,R2-NEW RED,4,G2-GREEN,5",false,null);
		assertTrue(validatePdf("sampleCanLabel"));
	}
	
	//no formula test
	@Test
	public void test23()  {
		reqObj = BuildReqObject(label7, formula8, message7, job7);
		reqObj.setTinter(null);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
	}
	
	// drawdown competitive color test
	@Test
	public void test24()  {
		reqObj = BuildReqObject(label8, formula9, message1, job7);
		printLabel.createLabelPdf(reqObj,"drawdownLabel","LANDSCAPE","","",false,null);
		assertTrue(validatePdf("drawdownLabel"));
	}

	@After
	public void cleanUpPdfFile() {
		String directoryPath = "./";
		File pdfFile = FileUtils.getFile(new File(directoryPath), "label.pdf");
		pdfFile.delete();
		System.out.println("Label PDF files from " + PrintLabelTest2.class.getSimpleName() + " have been cleaned up.");
	}
	
	private boolean validatePdf(String labelType) {
		boolean pdfIsValid = false;
		
		try {
			String pdfDirectory = "./";
			File pdfFile = FileUtils.getFile(new File(pdfDirectory), "label.pdf");			
			PreflightParser parser = new PreflightParser(pdfFile);
			parser.parse();
			PreflightDocument pdfDocument = parser.getPreflightDocument();
			PDFTextStripper pdfStripper = new PDFTextStripper();
			String text = pdfStripper.getText(pdfDocument);
			//validation varies based on labelType
			switch(labelType) {
				case "storeLabel":
				case "sampleCanLabel":
				case "selfTintCustLabel":
				case "drawdownStoreLabel":
					if (text.contains("Order #")) {
							pdfIsValid = true;
						}
					break;
				case "drawdownLabel":
					if (text.contains("Customer:")) {
							pdfIsValid = true;
						}
					break;
				default:
					System.out.println("Non-standard label type entered.");
					break;
			}
			pdfDocument.close();
		} catch (IOException e) {
			// Couldn't load the pdf File
			System.out.println("Could not load: label.pdf");
		}
		return pdfIsValid;
	}
	
	private RequestObject BuildReqObject(String label[],  String formula[], String message[], String job[]){
		reqObj.setGuid("test");
		reqObj.setColorComp(label[1]);
		reqObj.setColorID(label[2]);
		reqObj.setColorName(label[3]);
		reqObj.setSalesNbr(label[4]);
		reqObj.setProdNbr(label[5]);
		reqObj.setQuality(label[6]);
		reqObj.setComposite(label[7]);
		reqObj.setBase(label[8]);
		reqObj.setFinish(label[9]);
		reqObj.setKlass(label[10]);
		reqObj.setIntExt(label[11]);
		reqObj.setSizeText(label[12]);
		reqObj.setClrntSys(label[13]);
		reqObj.setCustomerID(label[14]);
		reqObj.setCustomerName(label[15]);
		TinterInfo tinter = new TinterInfo();
		tinter.setModel("COROB CUSTOM MOD16HF");
		reqObj.setTinter(tinter);
		// intBases - not required.
		// extBases - not required.
		// formResponse - SW messages.
		formulaMessages = BuildFormulaMessages(message);
		reqObj.setCanLabelMsgs(formulaMessages);
		reqObj.setFormResponse(BuildFormulationResponse());
		formulaInfo = BuildFormulaInfo(formula, Integer.parseInt(label[18]));
		reqObj.setDisplayFormula(formulaInfo);
			
		// rgbHex - not required.
		// vinylExclude - not required as of yet.
		// lightSource - not required.
		reqObj.setColorType(label[16]);
		reqObj.setPrimerId(label[17]);
		// colorVinylOnly - not required.
		// validationWarning - not required yet.
		// validationWarningSalesNbr - not required yet.
		// percentageFactor - not required yet.
		// jobFileList
		listJobField = BuildJobFieldList(job);
		reqObj.setJobFieldList(listJobField);
		
		if(StringUtils.isEmpty(reqObj.getSizeCode())) {
			reqObj.setSizeCode(label[19]);
		}
		reqObj.setSizeText(label[19]);
		
		reqObj.setControlNbr(Integer.parseInt(label[18]));
		reqObj.setRoomByRoom("BEDROOM");
		
		return reqObj;
	}
	
	
	private List<SwMessage> BuildFormulaMessages(String message[]){
		List<SwMessage> swMessages = new ArrayList<SwMessage>();
		SwMessage roomMessage = new SwMessage();
		roomMessage.setMessage(message[0]); // For now, customers do not have Room by Room - leave a blank line.
		swMessages.add(roomMessage);
		SwMessage fadeMessage = new SwMessage();
		fadeMessage.setMessage(message[1]); // Leave a blank line for now.  Will fade message precede primer message?
		swMessages.add(fadeMessage);
		SwMessage primerMessage = new SwMessage();
		primerMessage.setMessage(message[2]);
		swMessages.add(primerMessage);
		return swMessages;
	}
	
	private FormulationResponse BuildFormulationResponse(){
		formulationResponse.setMessages(formulaMessages);
		return formulationResponse;
	}

	
	private FormulaInfo BuildFormulaInfo(String formula[], int orderNbr){
		listIngredients = BuildFormulaIngredients(formula);
		formulaInfo.setIngredients(listIngredients);
		formulaInfo.setProcOrder(orderNbr);
		formulaInfo.setSourceDescr("SHER-COLOR FORMULA");
		List<String> incrementHdr = Arrays.asList("OZ", "32", "64", "128");
		formulaInfo.setIncrementHdr(incrementHdr);
		return formulaInfo;
	}
	
	private List<FormulaIngredient> BuildFormulaIngredients(String formulas[]){
		FormulaIngredient ingredient = null;
		List<FormulaIngredient> listIngredients = new ArrayList<FormulaIngredient>();
			for(int i = 0; i < formulas.length; i = i + 2){
				ingredient = BuildFormulaIngredient(formulas[i], formulas[i+1]);
				listIngredients.add(ingredient);
			}
		return listIngredients;
	}

	
	private FormulaIngredient BuildFormulaIngredient(String tintSysId, String name){
		int [] increment = {10, 33, 1, 1};
		FormulaIngredient formInd = new FormulaIngredient();
		formInd.setTintSysId(tintSysId);
		formInd.setName(name);
		formInd.setIncrement(increment);
		return formInd;
	}
	
	private List<JobField> BuildJobFieldList(String job[]){
		JobField jobField = null;
		List<JobField> listJobField = new ArrayList<JobField>();
		
		for(int i = 0; i < job.length - 1; i += 2) {
			jobField = BuildJobField(job[i], job[i + 1]);
			listJobField.add(jobField);
		}
		return listJobField;
	}

	private JobField BuildJobField(String enteredValue, String screenLabel){
		JobField jobField = new JobField();
		jobField.setEnteredValue(enteredValue);
		jobField.setRequiredText(" ");
		jobField.setScreenLabel(screenLabel);
		return jobField;
	}
	
}
