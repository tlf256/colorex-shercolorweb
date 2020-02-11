package com.sherwin.shercolor.customershercolorweb.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;
import com.sherwin.shercolor.util.domain.SwMessage;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.domain.FormulationResponse;


public class PrintLabelTest  {
	
	private RequestObject reqObj = new RequestObject();
	private List<FormulaIngredient> listIngredients;
	private FormulaInfo formulaInfo = new FormulaInfo();
	private List<SwMessage> formulaMessages;
	private FormulationResponse formulationResponse = new FormulationResponse();
	private List<JobField> listJobField;


	private String label1[] = {
			"label.pdf", "SHERWIN-WILLIAMS", "6385", "DOVER WHITE", "640512901", 
			"A87W00051", "SUPER PAINT", "LATEX", "EXTRA WHITE", "SATIN", "ARCHITECTURAL", 
			"INTERIOR", "ONE GALLON", "CCE", "CCF", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", " ", "33", "16"};
	
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
			"INTERIOR", "ONE GALLON", "CCE", "CCF", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", "31", "16"};
	
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
			"INTERIOR90", "FIVE GALLON", "CCE", "CCF", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", 
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
			"INTERIOR90", "FIVE GALLON", "CCE", "CCF", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", 
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
			"INTERIOR", "ONE GALLON", "CCE", "CCF", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", 
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
			"INTERIOR", "ONE GALLON", "CCE", "CCF", "CLEVELAND CLINIC", "SHER-COLOR FORMULA", "P2", 
			"4200001", "16"};
	
	private String formula6[] = { 
			"B1", "BLACK", "R2", "RED OXIDE", "Y3", "DEEP GOLD", "N1", "RAW UMBER", "W1", "WHITE", "L1", "BLUE" };

	private String message6[] = {
			"<Room by Room message 1>", "<Colorant message 2>", "<Primer message 3>"};
			
	private String job6[] = {
			"CCF Hillcrest", "Location Name", "G", "Building Code", "", "", "555", "Room", "Wall", "Surface Type", 
				"THIS IS A NEW LABEL", "Painter's Comment"};
	
	@Test
	public void test()  {
		reqObj = BuildReqObject(label1, formula1, message1, job1);
		ShercolorLabelPrintImpl printLabel = new ShercolorLabelPrintImpl();
		printLabel.CreateLabelPdf("label1.pdf", reqObj);
	}
	
	@Test
	public void test2()  {
		reqObj = BuildReqObject(label2, formula2, message2, job2);
		ShercolorLabelPrintImpl printLabel = new ShercolorLabelPrintImpl();
		
		printLabel.CreateLabelPdf("label2.pdf", reqObj);
	}
	
	@Test
	public void test3()  {
		reqObj = BuildReqObject(label3, formula3, message3, job3);
		ShercolorLabelPrintImpl printLabel = new ShercolorLabelPrintImpl();
		printLabel.CreateLabelPdf("label3.pdf", reqObj);
	}
	
	@Test
	public void test4()  {
		reqObj = BuildReqObject(label4, formula4, message4, job4);
		ShercolorLabelPrintImpl printLabel = new ShercolorLabelPrintImpl();
		printLabel.CreateLabelPdf("label4.pdf", reqObj);
	}
	
	@Test
	public void test5()  {
		reqObj = BuildReqObject(label5, formula5, message5, job5);
		ShercolorLabelPrintImpl printLabel = new ShercolorLabelPrintImpl();
		printLabel.CreateLabelPdf("label25.pdf", reqObj);
	}
	
	@Test
	public void test6()  {
		reqObj = BuildReqObject(label6, formula6, message6, job6);
		ShercolorLabelPrintImpl printLabel = new ShercolorLabelPrintImpl();
		printLabel.CreateLabelPdf("label26.pdf", reqObj);
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

		jobField = BuildJobField(job[0], job[1]);
		listJobField.add(jobField);
		jobField = BuildJobField(job[2], job[3]);
		listJobField.add(jobField);
		jobField = BuildJobField(job[4], job[5]);
		listJobField.add(jobField);
		jobField = BuildJobField(job[6], job[7]);
		listJobField.add(jobField);
		jobField = BuildJobField(job[8], job[9]);
		listJobField.add(jobField);
		jobField = BuildJobField(job[10], job[11]);
		listJobField.add(jobField);
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
