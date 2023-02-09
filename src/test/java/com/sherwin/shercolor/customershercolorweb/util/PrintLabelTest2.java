package com.sherwin.shercolor.customershercolorweb.util;


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.common.domain.OeFormInputRequest;
import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.DrawdownLabelService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.common.service.ProductService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class PrintLabelTest2  {
	
	private RequestObject reqObj = new RequestObject();
	
	@Autowired
	private ProductService productService;
	@Autowired
	private FormulationService formulationService;
	@Autowired
	private DrawdownLabelService drawdownLabelService;
	@Autowired
	CustomerService customerService;
	@Autowired
	ColorMastService colorMastService;
	
	private CdsProd cdsProd;
	
	private List<SwMessage> validationMsgs;
	private List<SwMessage> displayLabelMsgs;
	private FormulaInfo displayFormula;
	ShercolorLabelPrintImpl printLabel;

	private String request1[] = {"SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "0001", "MULBERRY SILK", "650186935", "CCE", 
			"01/05/17", "false", "16", "421001", "LB6110"};
	
	private String request2[] = {"SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "6385", "DOVER WHITE", "650096514", "CCE", 
			"01/05/17", "false", "16", "4216385", "LB6110"};
	
	private String request3[] = {"SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "0002", "CHELSEA MAUVE", "650096522", "CCE", 
			"01/05/17", "false", "20", "4210002", "LB6110"};
	
	private String request4[] = {"SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "6108", "LATTE", "650139587", "CCE", 
			"01/05/17", "false", "14", "426108", "LB6110"};
	
	private String request5[] = {"SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "0065", "VOGUE GREEN", "650187529", "CCE", 
			"01/05/17", "false", "16", "4210065", "LB6110"};
	
	private String request6[] = {"SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "6612", "RAVISHING CORAL", "640399697", "CCE", 
			"01/05/17", "false", "16", "4216612", "LB6110"};
	
	private String request7[] = {"SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "0064", "BLUE PEACOCK", "640398616", "CCE", 
			"01/05/17", "false", "16", "4210064", "LB6110"};
	
	private String request8[] = {"SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "0057", "CHINESE RED", "640399176", "CCE", 
			"01/05/17", "false", "16", "4211057", "LB6110"};
	
	private String request9[] = {"SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "6353", "CHIVALRY COPPER", "650131519", "CCE", 
			"01/05/17", "false", "16", "4216353", "LB6110"};
	
	private String request10[] = {"SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "6600", "ENTICING RED", "640398665", "CCE", 
			"01/05/17", "false", "16", "4216600", "LB6110"};
	
	private String parms1[] = {"1", "S-W", "SHERWIN-WILLIAMS", "SHERWIN-WILLIAMS", "SW", "CCE", "true", 
			"SW", "SHERWIN COLOR", "SWMZDP", "SW", "true", "6,4", "10,12,14", "8", "true", "true", "true",
			"true"};

	private String job1[] = {
			"CCF Main", "Location Name", "H", "Building Code", "6th", "Floor", "626", "Room", "Wall", "Surface Type", 
			"Test Formula", "Comment"};
	
	@Before
	public void testInitPrintService() {
		printLabel = new ShercolorLabelPrintImpl(drawdownLabelService,customerService,colorMastService,formulationService, true);
	}
	
	@Test
	public void testLabel5()  {
		reqObj = GetShercolorFormula(request1, parms1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
		System.out.println("Label 5 image created from " + reqObj.getProdNbr());
	}
	
	@Test
	public void testLabel6()  {
		reqObj = GetShercolorFormula(request2, parms1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
		System.out.println("Label 6 image created from " + reqObj.getProdNbr());
	}
	
	@Test
	public void testLabel7()  {
		reqObj = GetShercolorFormula(request3, parms1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
		System.out.println("Label 7 image created from " + reqObj.getProdNbr());
	}
	
	@Test
	public void testLabel8()  {
		reqObj = GetShercolorFormula(request4, parms1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
		System.out.println("Label 8 image created from " + reqObj.getProdNbr());
	}
	
	@Test
	public void testLabel9()  {
		reqObj = GetShercolorFormula(request5, parms1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
		System.out.println("Label 9 image created from " + reqObj.getProdNbr());
	}
	
	
	@Test
	public void testLabel10()  {
		reqObj = GetShercolorFormula(request6, parms1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
		System.out.println("Label 10 image created from " + reqObj.getProdNbr());
	}
	
	@Test
	public void testLabel11()  {
		reqObj = GetShercolorFormula(request7, parms1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
		System.out.println("Label 11 image created from " + reqObj.getProdNbr());
	}
	
	@Test
	public void testLabel12()  {
		reqObj = GetShercolorFormula(request8, parms1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
		System.out.println("Label 12 image created from " + reqObj.getProdNbr());
	}
	
	@Test
	public void testLabel13()  {
		reqObj = GetShercolorFormula(request9, parms1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
		System.out.println("Label 13 image created from " + reqObj.getProdNbr());
	}
	
	@Test
	public void testLabel14()  {
		reqObj = GetShercolorFormula(request10, parms1);
		printLabel.createLabelPdf(reqObj,"storeLabel","PORTRAIT","","",false,null);
		assertTrue(validatePdf("storeLabel"));
		System.out.println("Label 14 image created from " + reqObj.getProdNbr());
	}
	
	@After
	public void cleanUpPdfFile() {
		String directoryPath = "./";
		File pdfFile = FileUtils.getFile(new File(directoryPath), "label.pdf");
		pdfFile.delete();
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
			
			if (text.contains("Order #")) {
					pdfIsValid = true;
			}	
			
			pdfDocument.close();
		} catch (IOException e) {
			// Couldn't load the pdf File
			System.out.println("Could not load: label.pdf");
		}
		return pdfIsValid;
	}
	
	private RequestObject GetShercolorFormula(String [] request, String[] parms) {
		RequestObject reqObj = new RequestObject();
		reqObj.setColorType(request[0]);
		reqObj.setColorComp(request[1]);
		reqObj.setColorID(request[2]);
		reqObj.setColorName(request[3]);
		reqObj.setSalesNbr(request[4]);
		reqObj.setClrntSys(request[5]);
		reqObj.setVinylExclude(Boolean.parseBoolean(request[7]));
		reqObj.setCustomerID(request[10]);
		
		cdsProd = productService.readCdsProd(request[4]).get();
		reqObj.setProdNbr(cdsProd.getPrepComment().substring(0, 9));
		reqObj.setQuality(cdsProd.getQuality());
		reqObj.setComposite(cdsProd.getComposite());
		reqObj.setBase(cdsProd.getBase());
		reqObj.setFinish(cdsProd.getFinish());
		reqObj.setKlass(cdsProd.getKlass());
		reqObj.setIntExt(cdsProd.getIntExt());
		reqObj.setSizeText(cdsProd.getPrepComment().substring(10, 11));
		reqObj.setVinylExclude(Boolean.parseBoolean(request[7]));
		if (cdsProd.getIntExt().equals("INT")){
			reqObj.setLightSource("A");
		}
		else
		{
			reqObj.setLightSource("D65");
		}

		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(reqObj.getClrntSys());
		oeRequest.setColorComp(reqObj.getColorComp());
		oeRequest.setColorId(reqObj.getColorID());
		oeRequest.setSalesNbr(reqObj.getSalesNbr());
		oeRequest.setVinylSafe(false);
		String[] illums = new String[3];
		if (reqObj.getLightSource().equals("A")) {
				illums[0] = "A";
				illums[1] = "D65";
				illums[2] = "F2";
				oeRequest.setIllum(illums);
		}
		else {
			if (reqObj.getLightSource().equals("D65")) {
				illums[0] = "D65";
				illums[1] = "A";
				illums[2] = "F2";
				oeRequest.setIllum(illums);
			}
			else {
				if (reqObj.getLightSource().equals("A")) {
					illums[0] = "A";
					illums[1] = "D65";
					illums[2] = "F2";
					oeRequest.setIllum(illums);
				}
				else {
					illums[0] = "D65";
					illums[1] = "A";
					illums[2] = "F2";
					oeRequest.setIllum(illums);
				}
			}
		}
		// CustWebParms
		CustWebParms custWebParms = new CustWebParms();
		custWebParms.setSeqNbr(Integer.parseInt(parms[0]));
		custWebParms.setAbbrev(parms[1]);
		custWebParms.setStoreComp(parms[2]);
		custWebParms.setColorComp(parms[3]);
		custWebParms.setProdComp(parms[4]);
		custWebParms.setClrntSysId(parms[5]);
		custWebParms.setActive(Boolean.parseBoolean(parms[6]));
		custWebParms.setSwuiTitle(parms[7]);
		custWebParms.setSwuiTitle(parms[8]);
		custWebParms.setAltProdComp1(parms[9]);
		custWebParms.setFormRule(parms[10]);
		custWebParms.setBulkDeep(Boolean.parseBoolean(parms[11]));
		custWebParms.setBulkDn(parms[12]);
		custWebParms.setBulkUp(parms[13]);
		custWebParms.setBulkStart(parms[14]);
		custWebParms.setColorPrime(Boolean.parseBoolean(parms[15]));
		custWebParms.setOpacityCtrl(Boolean.parseBoolean(parms[16]));
		custWebParms.setFormQtrShot(Boolean.parseBoolean(parms[17]));
		custWebParms.setTargetCr2(Boolean.parseBoolean(parms[18]));
		custWebParms.setCustomerId("1234567890");
		
		FormulationResponse formula = null;
		
		if (reqObj.getColorType().equalsIgnoreCase("SHERWIN-WILLIAMS")){
			formula = formulationService.formulate(oeRequest, custWebParms);
		}
		if (reqObj.getColorType().equalsIgnoreCase("COMPETITIVE")){
			formula = formulationService.formulate(oeRequest, custWebParms);
		}
		
		if (formula.getFormulas().size() > 0){
			reqObj.setFormResponse(formula);
		}
		
		if (formula.getStatus().equals("COMPLETE")){
			reqObj.setDisplayFormula(formula.getFormulas().get(0));
			displayFormula = formula.getFormulas().get(0);
			validationMsgs = formulationService.validateFormulation(displayFormula);
			displayLabelMsgs = formulationService.canLabelFormulationWarnings(displayFormula);
		}

		List<JobField> listJobField = BuildJobFieldList(job1);
		reqObj.setJobFieldList(listJobField);

		reqObj.setCustomerName("CLEVELAND CLINIC");

		if(StringUtils.isEmpty(reqObj.getSizeCode())) {
			reqObj.setSizeCode(request[8]);
		}
		reqObj.setSizeText(request[8]);
		
		reqObj.setControlNbr(Integer.parseInt(request[9]));
		
			return reqObj;
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
	

