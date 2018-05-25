package com.sherwin.shercolor.common.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.net.Severity;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.dao.CdsColorStandDao;
import com.sherwin.shercolor.common.dao.CdsProdCharzdDao;
import com.sherwin.shercolor.common.dao.CdsProdDao;
import com.sherwin.shercolor.common.dao.CustWebParmsDao;
import com.sherwin.shercolor.common.dao.PosProdDao;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.common.domain.OeFormInputRequest;
import com.sherwin.shercolor.common.domain.OeServiceColorDataSet;
import com.sherwin.shercolor.common.domain.OeServiceProdDataSet;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.util.domain.SwMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class FormulationServiceTest {

	@Autowired
	FormulationService target;
	
	@Autowired
	CustWebParmsDao custWebParmsDao;
	
	@Autowired
	CdsColorStandDao colorStandDao;
	
	@Autowired
	CdsProdDao cdsProdDao;
	
	@Autowired
	PosProdDao posProdDao;
	
	@Autowired
	CdsProdCharzdDao prodCharzdDao;
	
	@Autowired
	ColorService colorService;
	
	@Autowired
	ProductService productService;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	

	@Test 
	public void testSherColorFormula(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6257";
		String salesNbr = "650406317";
		String[] illums = new String[3];
		illums[0] = "D65";
		illums[1] = "A";
		illums[2] = "F2";
		PosProd posProd = posProdDao.read(salesNbr);
		CdsProdCharzd prodCharzd = prodCharzdDao.read(posProd.getProdNbr(), clrntSysId);
		
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), prodCharzd.getColorEngVer()).getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		oeRequest.setIllum(illums);
		oeRequest.setPctFormula(0);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("1.0 SherColor Formula for CCE SW 6257 650406317");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE")); // TODO test for ...  && curveMatch);
		
	}
	
	@Test 
	public void testSherColorFormulaNoParms(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6257";
		String salesNbr = "650406317";
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("2.0 SherColor Formula with No Parms for CCE SW 6257 650406317");
		FormulationResponse result = target.formulate(oeRequest,null);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE")); // TODO test for ...  && curveMatch);
		
	}
	
	@Test 
	public void testVinylSafeFormula(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6257";
		String salesNbr = "650406317";
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(true);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("3.0 Vinyl Safe SherColor Formula for CCE SW 6257 650406317");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE")); // TODO test for ...  && curveMatch);
		
	}
	
	@Test 
	public void testFormulaBook2(){
		String clrntSysId = "BAC";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3511";
		String salesNbr = "640359261";
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",2);

		System.out.println("------------------------------------------------------------------");
		System.out.println("4.1 FormBook Formula for BAC SW 3511 640359261");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETFBBASE)");
		assertTrue(result.getStatus().equalsIgnoreCase("GETFBBASE"));
		
	}
	
	@Test 
	public void testFormulaBook(){
		String clrntSysId = "BAC";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3103";
		String salesNbr = "650089998";
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",2);

		System.out.println("------------------------------------------------------------------");
		System.out.println("4.0 FormBook Formula for BAC SW 3103 650089998");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE"));
		
	}
	
	@Test 
	public void testFormulaBookReturnTwoChoices(){
		String clrntSysId = "BAC";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3113";
		String salesNbr = "640329199";
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",2);

		System.out.println("------------------------------------------------------------------");
		System.out.println("5.0 FormBook Formula (return Six choices) for BAC SW 3113 640329199");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCTFBBASE)");
		assertTrue(result.getStatus().equalsIgnoreCase("GETPCTFBBASE"));
		
	}
	
	@Test 
	public void testFormulaBookPickOneOfTwoChoices(){
		String clrntSysId = "BAC";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3113";
		String salesNbr = "640329199";
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		oeRequest.setFbBase("A49N00202");
		oeRequest.setFbSubBase("WD CL STN INT (P&B)");
		oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",2);

		System.out.println("------------------------------------------------------------------");
		System.out.println("6.0 FormBook Formula Pick One of Two choices for BAC SW 3113 640329199");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCT)");
		assertTrue(result.getStatus().equalsIgnoreCase("GETPCT"));
		
	}
	
	@Test
	public void testPercentSherColor(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6611";
		String salesNbr = "650276827";
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("7.0 Percent SherColor Formula for CCE SW 6611 605276827");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCT)");
		assertTrue(result.getStatus().equalsIgnoreCase("GETPCT")); // TODO test for ... && curveMatch 
		
	}

	@Test
	public void testPercentSherColorReturnIntExtAndPct(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6611";
		String salesNbr = "9110602";
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("8.0 Percent SherColor Formula (return int/ext) for CCE SW 6611 9110602");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCTINTEXT)");
	//LEG
	//	assertTrue(result.getStatus().equalsIgnoreCase("GETPCTINTEXT"));
		
	}

	@Test
	public void testPercentSherColorForceInterior(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6611";
		String salesNbr = "9110602";
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		oeRequest.setPctFormula(100);
		oeRequest.setForceIntExt("INTERIOR");
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("9.0 Percent SherColor Formula Force Interior for CCE SW 6611 9110602");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCT)");
		assertTrue(result.getStatus().equalsIgnoreCase("GETPCT"));
		
	}

	@Test
	public void testPercentSherColorForceExterior(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6611";
		String salesNbr = "9110602";
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		oeRequest.setPctFormula(100);
		oeRequest.setForceIntExt("EXTERIOR");
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("10.0 Percent SherColor Formula Force Exterior for CCE SW 6611 9110602");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCT)");
		assertTrue(result.getStatus().equalsIgnoreCase("GETPCT"));
		
	}

	@Test
	public void testPercentFormulaBook(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "0101";
		String salesNbr = "640421640"; //rich lux accent
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("11.0 Percent FormBook Formula for CCE SW 0101 640421640");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCT)");
		assertTrue(result.getStatus().equalsIgnoreCase("GETPCT"));
		
	}

	@Test
	public void testPercentFormulaBookGetMultChoicesProdOnFile(){
		String clrntSysId = "BAC";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3111";
		String salesNbr = "640315768"; //stain on file
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",2);

		System.out.println("------------------------------------------------------------------");
		System.out.println("12.0 Percent FormBook Formula (returns mult choices) Prod On File for BAC SW 3111 6403315768");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCTFBBASE)");
		assertTrue(result.getStatus().equalsIgnoreCase("GETPCTFBBASE"));
		
	}

	@Test
	public void testPercentFormulaBookGetMultChoicesProdNOF(){
		String clrntSysId = "BAC";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3111";
		String salesNbr = "1543453"; //no cds prod polyurethane
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",2);

		System.out.println("------------------------------------------------------------------");
		System.out.println("13.0 Percent FormBook Formula (returns mult choices) Prod NOF for BAC SW 3111 1543453");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCTFBBASE)");
		//LEG
		//assertTrue(result.getStatus().equalsIgnoreCase("GETPCTFBBASE"));
	}

	@Test
	public void testPercentFormulaBookPickOneOfTwoSalesNbrNOF(){
		String clrntSysId = "BAC";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3113";
		String salesNbr = "1543453"; //no cds prod polyurethane
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		//oeRequest.setForceIntExt("INTERIOR");
		//oeRequest.setFbBase("A49NQ8202");
		//oeRequest.setFbSubBase("WD CL STN INT (OAK)");
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",2);

		System.out.println("------------------------------------------------------------------");
		System.out.println("14.0 Percent FormBook Formula Pick One of Two SalesNbr NOF for BAC SW 3113 1543453");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		
		
		
		// ALL DONE IN ONE PASS OF OUR FORMULATE CALL NOW (convert GETPCTINTEXT TO GETPCTFBBASE)
//		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCT)");
//
//		// retry sending in pct and int/ext
//		oeRequest.setPctFormula(100);
//		oeRequest.setForceIntExt("INTERIOR");
//		//oeRequest.setFbBase("A49NQ8202");
//		//oeRequest.setFbSubBase("WD CL STN INT (OAK)");
//		
//		System.out.println("----------------------CONTINUED-----------------------------------");
//		System.out.println("14.1 Percent FormBook Formula Pick One of Two SalesNbr NOF for BAC SW 3113 1543453");
//		result = target.formulate(oeRequest,custWebParms);
//		if(result.getMessages()!=null){
//			System.out.println("Returned " + result.getMessages().size() + " messages.");
//			for(SwMessage msg : result.getMessages()){
//				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
//			}
//		}
//		if(result.getFormulas()!=null){
//			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
//			int i = 1;
//			for(FormulaInfo form : result.getFormulas()){
//				System.out.println("Choice " + i + " guid=" + form.getGuid());
//				i++;
//				System.out.println(form.getSource() + " - " + form.getSourceDescr());
//				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
//				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
//				for(FormulaIngredient colorant : form.getIngredients()){
//					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
//				}
//			}
//		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCTFBBASE)");
		
		oeRequest.setFbBase("A49NQ8202");
		oeRequest.setFbSubBase("WD CL STN INT (OAK)");
		
		System.out.println("----------------------CONTINUED-----------------------------------");
		System.out.println("14.2 Percent FormBook Formula Pick One of Two SalesNbr NOF for BAC SW 3113 1543453");
		result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCT)");
		
		// TODO once Rod puts his fix in uncomment this line -> 
		//LEG
		//assertTrue(result.getStatus().equalsIgnoreCase("GETPCT"));
		//assertTrue(true);
	}

	@Test
	public void testPercentFormulaBookPickOneOfTwoSalesNbrOnFile(){
		String clrntSysId = "BAC";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3113";
		String salesNbr = "640516282"; // ultradeep 
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		//oeRequest.setForceIntExt("INTERIOR");
		//oeRequest.setFbBase("A49NQ8202");
		//oeRequest.setFbSubBase("WD CL STN INT (OAK)");
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",2);

		System.out.println("------------------------------------------------------------------");
		System.out.println("15.0 Percent FormBook Formula Pick One of Two SalesNbr On File for BAC SW 3113 1543453");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCTFBBASE)");

		// retry sending in pct and int/ext
		oeRequest.setPctFormula(100);
		//oeRequest.setForceIntExt("INTERIOR");
		oeRequest.setFbBase("A49NQ8202");
		oeRequest.setFbSubBase("WD CL STN INT (OAK)");
		
		System.out.println("----------------------CONTINUED-----------------------------------");
		System.out.println("15.1 Percent FormBook Formula Pick One of Two SalesNbr On File for BAC SW 3113 1543453");
		result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCT)");
		
		
		// TODO once Rod puts his fix in uncomment this line -> 
		assertTrue(result.getStatus().equalsIgnoreCase("GETPCT"));
		//assertTrue(true);
		
	}

	@Test 
	public void testCompetitiveFormula(){
		String clrntSysId = "CCE";
		String colorComp = "PPG";
		String colorId = "104-7"; // Adventure Green
		String salesNbr = "640399754"; // A100 Latex Satin Ultradeep
		String[] illums = {"A","D65","F2"};
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		oeRequest.setIllum(illums);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("16.0 Compet SherColor Formula for CCE PPG 104-7 640399754");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE")); // TODO test for ...  && curveMatch);
		
		
	}
	
	@Test 
	public void testCompetFormulaWithStandardChanged(){
		String clrntSysId = "CCE";
		String colorComp = "IOWA PAINT";
		String colorId = "8546N"; // Adventure Green
		String salesNbr = "640399754"; // A100 Latex Satin Ultradeep
		String[] illums = {"A","D65","F2"};
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		oeRequest.setIllum(illums);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("17.0 Compet SherColor Formula Where Standard Changed for CCE IOWA PAINT 8546N 640399754");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE")); // TODO test for ...  && curveMatch);
		
		
	}
	
	@Test 
	public void testProdFamilyFormulation(){
		String clrntSysId = "CCE";
		String colorComp = "BEHR";
		String colorId = "UL140-7";
		String salesNbr = "650046329";
		String[] illums = {"A","D65","F2"};
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion("BEHR", "UL140-7", new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		oeRequest.setIllum(illums);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("18.0 Product Family Formula for CCE BEHR UL140-7 650046329");
		FormulationResponse result = target.prodFamilyFormulate(oeRequest,custWebParms);
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b PICKPRODFAM)");
		assertTrue(result.getStatus().equalsIgnoreCase("PICKPRODFAM")); // TODO test for ...  && curveMatch);
		
	}
	
	@Test 
	public void testProdFamilyFormulationForceCurve(){
		String clrntSysId = "CCE";
		String colorComp = "1";
		String colorId = "MTCHUL1407";
		String salesNbr = "650046329";
		String[] illums = {"A","D65","F2"};
		boolean curveMatch=false;
		
		BigDecimal[] standard = colorStandDao.readByEffectiveVersion("BEHR", "UL140-7", new Date(), "2DK").getCurve();
		double[] colorCurve = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		for(int i=0;i<40;i++){
			colorCurve[i] = standard[i].doubleValue();
		}

		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		oeRequest.setColorCurve(colorCurve);
		oeRequest.setIllum(illums);
		
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("19.0 Product Family Formula Force Curve for CCE BEHR UL140-7 650046329");
		FormulationResponse result = target.prodFamilyFormulate(oeRequest,custWebParms);
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(colorCurve));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != colorCurve[j]) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b PICKPRODFAM)");
		assertTrue(result.getStatus().equalsIgnoreCase("PICKPRODFAM") && curveMatch);
		
	}

	@Test
	public void testPercentSherColor75pct(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6255";
		String salesNbr = "601214489";
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("20.0 Percent SherColor Formula (default 75%) for CCE SW 6255 601214489");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b GETPCT)");
		assertTrue(result.getStatus().equalsIgnoreCase("GETPCT")); // TODO test for ... && curveMatch 
		
	}

	@Test
	public void testTricornBlackAsVinylSafe(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6258";
		String salesNbr = "640413423";
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(true);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("21.0 TricornBlackAsVinylSafe CCE SW 6258 640413423");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b ERROR)");
		assertTrue(result.getStatus().equalsIgnoreCase("ERROR")); // TODO test for ... && curveMatch 
		
	}

	@Test
	public void testPpg101_5_DeWarn(){
		String clrntSysId = "CCE";
		String colorComp = "PPG";
		String colorId = "101-5";
		String salesNbr = "650370091";
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("22.0 PPG DE Warn CCE PPG 101-5 650370091");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				if(form.getDeltaEs()!=null){
					for(int j=0;j<form.getDeltaEs().length;j++){
						System.out.println(" DE" + j + " = " + form.getDeltaEs()[j]);
					}
						
				}
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE")); // TODO test for ... && curveMatch 
		
	}

	@Test
	public void testPpg101_5_DeWarn_prodfamilyformulate(){
		String clrntSysId = "CCE";
		String colorComp = "PPG";
		String colorId = "101-5";
		String salesNbr = "650370091";
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		String[] illums = new String[3];
		illums[0] = "D65";
		illums[1] = "A";
		illums[2] = "F2";

		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setIllum(illums);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("23.0 PPG DE Warn Prod Family Formulate CCE PPG 101-5 650370091");
		FormulationResponse result = target.prodFamilyFormulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				if(form.getDeltaEs()!=null){
					for(int j=0;j<form.getDeltaEs().length;j++){
						System.out.println(" DE" + j + " = " + form.getDeltaEs()[j]);
					}
						
				}
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE")); // TODO test for ... && curveMatch 
		
	}

	@Test
	public void testShotsToIncrCase1(){
		int shots = 512;
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		FormulaIngredient item = new FormulaIngredient();
		item.setClrntSysId("CCE");
		item.setShots(shots);
		input.add(item);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestShotsToIncr " + input.get(0).getClrntSysId() + " " + input.get(0).getShots());
		boolean result = target.convertShotsToIncr(input);
		System.out.println(Arrays.toString(input.get(0).getIncrement()));
		
		assertTrue(result);
	}

	@Test
	public void testShotsToIncrCase2(){
		int shots = 560;
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		FormulaIngredient item = new FormulaIngredient();
		item.setClrntSysId("CCE");
		item.setShots(shots);
		input.add(item);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestShotsToIncr " + input.get(0).getClrntSysId() + " " + input.get(0).getShots());
		boolean result = target.convertShotsToIncr(input);
		System.out.println(Arrays.toString(input.get(0).getIncrement()));
		
		assertTrue(result);
	}

	@Test
	public void testShotsToIncrCase3(){
		int shots = 563;
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		FormulaIngredient item = new FormulaIngredient();
		item.setClrntSysId("CCE");
		item.setShots(shots);
		input.add(item);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestShotsToIncr " + input.get(0).getClrntSysId() + " " + input.get(0).getShots());
		boolean result = target.convertShotsToIncr(input);
		System.out.println(Arrays.toString(input.get(0).getIncrement()));
		
		assertTrue(result);
	}

	@Test
	public void testShotsToIncrCase4(){
		int shots = 691;
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		FormulaIngredient item = new FormulaIngredient();
		item.setClrntSysId("CCE");
		item.setShots(shots);
		input.add(item);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestShotsToIncr " + input.get(0).getClrntSysId() + " " + input.get(0).getShots());
		boolean result = target.convertShotsToIncr(input);
		System.out.println(Arrays.toString(input.get(0).getIncrement()));
		
		assertTrue(result);
	}

	@Test
	public void testShotsToIncrCase5(){
		int shots = 255;
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		FormulaIngredient item = new FormulaIngredient();
		item.setClrntSysId("CCE");
		item.setShots(shots);
		input.add(item);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestShotsToIncr " + input.get(0).getClrntSysId() + " " + input.get(0).getShots());
		boolean result = target.convertShotsToIncr(input);
		System.out.println(Arrays.toString(input.get(0).getIncrement()));
		
		assertTrue(result);
	}

	
	@Test
	public void testScaleFormulaByPercent(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60B1");
		ingredient.setTintSysId("B1");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testScaleFormulaByPercent");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("at 100%");
		int countIn=formInfo.getIngredients().size();
		for(int i=0;i<countIn;i++){
			System.out.println(formInfo.getIngredients().get(i).getEngSysId() + " " 
					         + formInfo.getIngredients().get(i).getTintSysId() + " "
					         + formInfo.getIngredients().get(i).getShots() + " "
					         + Arrays.toString(formInfo.getIngredients().get(i).getIncrement()));
		}
		
		boolean countMatch = true;
		FormulaInfo result = target.scaleFormulaByPercent(formInfo, 125);
		System.out.println("at 125%");
		int countOut=result.getIngredients().size();
		for(int i=0;i<countOut;i++){
			System.out.println(result.getIngredients().get(i).getEngSysId() + " " 
					         + result.getIngredients().get(i).getTintSysId() + " "
					         + result.getIngredients().get(i).getShots() + " "
					         + Arrays.toString(result.getIngredients().get(i).getIncrement()));
		}
		if(countIn!=countOut) countMatch = false;
		
		result = target.scaleFormulaByPercent(formInfo, 75);
		System.out.println("at 75%");
		countOut=result.getIngredients().size();
		for(int i=0;i<countOut;i++){
			System.out.println(result.getIngredients().get(i).getEngSysId() + " " 
					         + result.getIngredients().get(i).getTintSysId() + " "
					         + result.getIngredients().get(i).getShots() + " "
					         + Arrays.toString(result.getIngredients().get(i).getIncrement()));
		}
		if(countIn!=countOut) countMatch = false;
		
		assertTrue(countMatch);
	}


	@Test
	public void testIncrToShotsCase1(){
		int[] incrs = {4,0,0,0};
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		FormulaIngredient item = new FormulaIngredient();
		item.setClrntSysId("CCE");
		item.setIncrement(incrs);
		input.add(item);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestIncrToShots " + input.get(0).getClrntSysId() + " " + Arrays.toString(input.get(0).getIncrement()));
		boolean result = target.convertIncrToShots(input);
		System.out.println(input.get(0).getShots());
		
		assertTrue(result && input.get(0).getShots()==512);
	}


	@Test
	public void testIncrToShotsCase2(){
		int[] incrs = {4, 12, 0, 0};
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		FormulaIngredient item = new FormulaIngredient();
		item.setClrntSysId("CCE");
		item.setIncrement(incrs);
		input.add(item);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestIncrToShots " + input.get(0).getClrntSysId() + " " + Arrays.toString(input.get(0).getIncrement()));
		boolean result = target.convertIncrToShots(input);
		System.out.println(input.get(0).getShots());
		
		assertTrue(result && input.get(0).getShots()==560);
	}

	
	@Test
	public void testIncrToShotsCase3(){
		int[] incrs = {4, 12, 1, 1};
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		FormulaIngredient item = new FormulaIngredient();
		item.setClrntSysId("CCE");
		item.setIncrement(incrs);
		input.add(item);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestIncrToShots " + input.get(0).getClrntSysId() + " " + Arrays.toString(input.get(0).getIncrement()));
		boolean result = target.convertIncrToShots(input);
		System.out.println(input.get(0).getShots());
		
		assertTrue(result && input.get(0).getShots()==563);
	}

	
	@Test
	public void testIncrToShotsCase4(){
		int[] incrs = {4, 44, 1, 1};
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		FormulaIngredient item = new FormulaIngredient();
		item.setClrntSysId("CCE");
		item.setIncrement(incrs);
		input.add(item);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestIncrToShots " + input.get(0).getClrntSysId() + " " + Arrays.toString(input.get(0).getIncrement()));
		boolean result = target.convertIncrToShots(input);
		System.out.println(input.get(0).getShots());
		
		assertTrue(result && input.get(0).getShots()==691);
	}

	
	@Test
	public void testIncrToShotsCase5(){
		int[] incrs = {0, 63, 1, 1};
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		FormulaIngredient item = new FormulaIngredient();
		item.setClrntSysId("CCE");
		item.setIncrement(incrs);
		input.add(item);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestIncrToShots " + input.get(0).getClrntSysId() + " " + Arrays.toString(input.get(0).getIncrement()));
		boolean result = target.convertIncrToShots(input);
		System.out.println(input.get(0).getShots());
		
		assertTrue(result && input.get(0).getShots()==255);
	}

	@Test
	public void testFillIngredientInfoFromTintSysId1(){
		int[] incrs = {1, 1, 1, 1};
		
		List<FormulaIngredient> input = new ArrayList<FormulaIngredient>();
		for(int i=2;i<=4;i++){
			FormulaIngredient item = new FormulaIngredient();
			item.setClrntSysId("CCE");
			item.setTintSysId("R" + String.valueOf(i));
			item.setIncrement(incrs);
			input.add(item);
		}
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("TestFillIngredientInfoFromTintSysId ");
		for(FormulaIngredient item : input){
			System.out.println(" IN->" + item.getClrntSysId() + " " + item.getTintSysId());
		}
		System.out.println("");
		boolean result = target.fillIngredientInfoFromTintSysId(input);
		for(FormulaIngredient item : input){
			System.out.println("OUT->" + item.getClrntSysId() + " " + item.getTintSysId() + " " + item.getFbSysId() + " " + item.getEngSysId() + " " + item.getName() + " " + item.getOrganicInd());
		}
		
		assertTrue(result && input.get(0).getName().equalsIgnoreCase("maroon"));
	}
	
	@Test
	public void testValidateFormula(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("Y1");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		//target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testValidateFormula");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("Single Organic - CCE");

		List<SwMessage> result = target.validateFormulation(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	
	@Test
	public void testValidateFormulaBACPrimer(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("BAC");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(1.0);
		formInfo.setColorComp("SHERWIN-WILLIAMS");
		formInfo.setColorId("6863");
		
		FormulaIngredient ingredient = new FormulaIngredient();

		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(200);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		//target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testValidateFormula");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("BAC Primer ID");

		List<SwMessage> result = target.validateFormulation(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}

	
	@Test
	public void testValidateFormulaCCEPrimerNoPrimerId(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(1.0);
		formInfo.setColorComp("SHERWIN-WILLIAMS");
		formInfo.setColorId("6385");
		
		FormulaIngredient ingredient = new FormulaIngredient();

		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		//target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testValidateFormula");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("CCE Primer ID - no primer id needed.");

		List<SwMessage> result = target.validateFormulation(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==0) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	
	
	@Test
	public void testValidateFormulaCCEPrimer(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(1.0);
		formInfo.setColorComp("SHERWIN-WILLIAMS");
		formInfo.setColorId("6863");
		
		FormulaIngredient ingredient = new FormulaIngredient();

		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		//target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testValidateFormula");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("CCE Primer ID");

		List<SwMessage> result = target.validateFormulation(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	
	@Test
	public void testValidateFormulaCase2(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("Y1");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		//target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testValidateFormula");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("Multiple Organic - CCE");

		List<SwMessage> result = target.validateFormulation(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==2) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	@Test
	public void testValidateFormulaCLB(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CLB");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("AX");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);

		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		//target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testValidateFormula");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("Single Organic - CLB");

		List<SwMessage> result = target.validateFormulation(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}

	//Commenting out - 
//	@Test
//	public void testValidateFormulaComexAlkali(){
//		FormulaInfo formInfo = new FormulaInfo();
//		
//		formInfo.setClrntSysId("888");
//		formInfo.setClrntSysName("Color Cast");
//		
//		FormulaIngredient ingredient = new FormulaIngredient();
//		ingredient.setClrntSysId(formInfo.getClrntSysId());
//		ingredient.setEngSysId("A60Y1");
//		ingredient.setTintSysId("V");
//		ingredient.setShots(100);
//		ingredient.setShotSize(128);
//		formInfo.getIngredients().add(ingredient);
//		
//		ingredient = new FormulaIngredient();
//		ingredient.setClrntSysId(formInfo.getClrntSysId());
//		ingredient.setEngSysId("A60R2");
//		ingredient.setTintSysId("R2");
//		ingredient.setShots(1);
//		ingredient.setShotSize(128);
//		formInfo.getIngredients().add(ingredient);
//		
//		ingredient = new FormulaIngredient();
//		ingredient.setClrntSysId(formInfo.getClrntSysId());
//		ingredient.setEngSysId("A60Y3");
//		ingredient.setTintSysId("Y3");
//		ingredient.setShots(7);
//		ingredient.setShotSize(128);
//		formInfo.getIngredients().add(ingredient);
//		
//		ingredient = new FormulaIngredient();
//		ingredient.setClrntSysId(formInfo.getClrntSysId());
//		ingredient.setEngSysId("A60R3");
//		ingredient.setTintSysId("R3");
//		ingredient.setShots(32);
//		ingredient.setShotSize(128);
//		formInfo.getIngredients().add(ingredient);
//		
//		ingredient = new FormulaIngredient();
//		ingredient.setClrntSysId(formInfo.getClrntSysId());
//		ingredient.setEngSysId("A60R4");
//		ingredient.setTintSysId("R4");
//		ingredient.setShots(64);
//		ingredient.setShotSize(128);
//		formInfo.getIngredients().add(ingredient);
//		
//		ingredient = new FormulaIngredient();
//		ingredient.setClrntSysId(formInfo.getClrntSysId());
//		ingredient.setEngSysId("A60L1");
//		ingredient.setTintSysId("L1");
//		ingredient.setShots(128);
//		ingredient.setShotSize(128);
//		formInfo.getIngredients().add(ingredient);
//		
//		ingredient = new FormulaIngredient();
//		ingredient.setClrntSysId(formInfo.getClrntSysId());
//		ingredient.setEngSysId("A60W1");
//		ingredient.setTintSysId("W1");
//		ingredient.setShots(256);
//		ingredient.setShotSize(128);
//		formInfo.getIngredients().add(ingredient);
//		
//		//target.convertShotsToIncr(formInfo.getIngredients());
//		
//		System.out.println("------------------------------------------------------------------");
//		System.out.println("testValidateFormula");
//		System.out.println(formInfo.getClrntSysId());
//		System.out.println("Single Alkali - Comex");
//
//		List<SwMessage> result = target.validateFormulation(formInfo);
//
//		int countOut=result.size();
//		boolean countMatch = false;
//		if (countOut==1) {
//			countMatch = true;
//		}
//		for(SwMessage i: result){
//			System.out.println(i.getCode() + " " + i.getMessage());
//		}
//
//		assertTrue(countMatch);
//	}
	
	@Test
	public void testValidateFormulaComexSingleExt(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("888");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("AX");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		//target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testValidateFormula");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("Single Exterior - Comex");

		List<SwMessage> result = target.validateFormulation(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	@Test
	public void testValidateFormulaComexMultipleExt(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("888");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("AX");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		//target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testValidateFormula");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("Multiple Exterior - Comex");

		List<SwMessage> result = target.validateFormulation(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==2) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	
	@Test
	public void testValidateFormulaAFCD(){
		// Test to find a color/product that gives a formula change warning.
		// NOTE - these warnings expire so the test may need to be updated from time to time (e.g. pick a different color and product).
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		formInfo.setColorComp("SHERWIN-WILLIAMS");
		formInfo.setColorId("6107");
		formInfo.setSalesNbr("650927387");
		
		FormulaIngredient ingredient = new FormulaIngredient();

		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		//target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testValidateFormula");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("AFCD");

		List<SwMessage> result = target.validateFormulation(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}
		//LEG
		//	assertTrue(countMatch);
	}

	
	@Test
	public void testCanLabelMsg_NONE(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setColorComp("SHERWIN-WILLIAMS");
		formInfo.setColorId("6611");
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("Y1");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testCanLabelMsg_NONE");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("Single Organic - CCE - NO WARN");

		List<SwMessage> result = target.canLabelFormulationWarnings(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==0) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	
	@Test
	public void testCanLabelBACPrimer(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("BAC");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(1.0);
		formInfo.setColorComp("SHERWIN-WILLIAMS");
		formInfo.setColorId("6863");
		
		FormulaIngredient ingredient = new FormulaIngredient();
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(200);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testCanLabelBACPrimer");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("BAC Primer ID");

		List<SwMessage> result = target.canLabelFormulationWarnings(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
		
	@Test
	public void testCanLabelCCEPrimer(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(1.0);
		formInfo.setColorComp("SHERWIN-WILLIAMS");
		formInfo.setColorId("6863");
		
		FormulaIngredient ingredient = new FormulaIngredient();

		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testCanLabelCCEPrimer");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("CCE Primer ID");

		List<SwMessage> result = target.canLabelFormulationWarnings(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	
	@Test
	public void testCanLabelCase2(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("Y1");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testCanLabelCase2");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("Multiple Organic - CCE - NOT SHOWN ON CAN LABEL");

		List<SwMessage> result = target.canLabelFormulationWarnings(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==0) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	@Test
	public void testCanLabelCLB(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CLB");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("AX");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testCanLabelCLB");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("Single Organic - CLB");

		List<SwMessage> result = target.canLabelFormulationWarnings(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==2) {  // 1 error needs two messages b/c can label width too small and needed to simulate line break
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}

	@Test
	public void testCanLabelComexSingleExt(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("888");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("AX");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		//target.convertShotsToIncr(formInfo.getIngredients());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testCanLabelComexSingleExt");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("Single Exterior - Comex");

		List<SwMessage> result = target.canLabelFormulationWarnings(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	@Test
	public void testCanLabelComexMultipleExt(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("888");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setContrastRatioThin(0.0);
		
		FormulaIngredient ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("AX");
		ingredient.setShots(100);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R");
		ingredient.setShots(1);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(7);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("T60R3");
		ingredient.setTintSysId("V");
		ingredient.setShots(32);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R4");
		ingredient.setTintSysId("R4");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60L1");
		ingredient.setTintSysId("L1");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60W1");
		ingredient.setTintSysId("W1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testCanLabelComexMultipleExt");
		System.out.println(formInfo.getClrntSysId());
		System.out.println("Multiple Exterior - Comex");

		List<SwMessage> result = target.canLabelFormulationWarnings(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==2) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}

	@Test
	public void testFillLevelPaintShieldFail(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setColorComp("CUSTOM");
		formInfo.setColorId("MyFav");
		formInfo.setSalesNbr("650861685");
		
		FormulaIngredient ingredient = new FormulaIngredient();
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testFillLevelPaintShieldFail");
		System.out.println(formInfo.getClrntSysId());

		List<SwMessage> result = target.manualFormulationWarnings(formInfo);

		boolean countMatch = false;
		if(result.size()>0){
			for (SwMessage msg : result){
				if(msg.getSeverity()==Level.ERROR) countMatch=true;
			}
		}

		for(SwMessage i: result){
			System.out.println(i.getSeverity() + " " + i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}
	
	@Test
	public void testFillLevelPaintShieldWarn(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setColorComp("CUSTOM");
		formInfo.setColorId("MyFav");
		formInfo.setSalesNbr("650861685");
		
		FormulaIngredient ingredient = new FormulaIngredient();
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testFillLevelPaintShieldWarn");
		System.out.println(formInfo.getClrntSysId());

		List<SwMessage> result = target.manualFormulationWarnings(formInfo);

		boolean countMatch = false;
		if(result.size()>0){
			for (SwMessage msg : result){
				if(msg.getSeverity()==Level.WARN) countMatch=true;
			}
		}

		for(SwMessage i: result){
			System.out.println(i.getSeverity() + " " + i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}

	@Test
	public void testFillLevelSuperPaintFail(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setColorComp("CUSTOM");
		formInfo.setColorId("MyFav");
		formInfo.setSalesNbr("640512877");
		
		FormulaIngredient ingredient = new FormulaIngredient();
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(512);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y3");
		ingredient.setTintSysId("Y3");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testFillLevelSuperPaintFail");
		System.out.println(formInfo.getClrntSysId());

		List<SwMessage> result = target.manualFormulationWarnings(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getSeverity() + " " + i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}

	@Test
	public void testCustomManualFadeCheckWarn(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setColorComp("CUSTOM");
		formInfo.setColorId("MyFav");
		formInfo.setSalesNbr("640514873");
		
		FormulaIngredient ingredient = new FormulaIngredient();
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(128);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("Y1");
		ingredient.setShots(187);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(64);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testCustomManualFadeCheckWarn");
		System.out.println(formInfo.getClrntSysId());

		List<SwMessage> result = target.manualFormulationWarnings(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==1) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getSeverity() + " " + i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}

	@Test
	public void testCustomManualFadeCheckWarnAndFillWarn(){
		FormulaInfo formInfo = new FormulaInfo();
		
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setColorComp("CUSTOM");
		formInfo.setColorId("MyFav");
		formInfo.setSalesNbr("640514873");
		
		FormulaIngredient ingredient = new FormulaIngredient();
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R2");
		ingredient.setTintSysId("R2");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60Y1");
		ingredient.setTintSysId("Y1");
		ingredient.setShots(256);
		ingredient.setShotSize(128);
		ingredient.setOrganicInd("OR");
		formInfo.getIngredients().add(ingredient);
		
		ingredient = new FormulaIngredient();
		ingredient.setClrntSysId(formInfo.getClrntSysId());
		ingredient.setEngSysId("A60R3");
		ingredient.setTintSysId("R3");
		ingredient.setShots(275);
		ingredient.setShotSize(128);
		formInfo.getIngredients().add(ingredient);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("testCustomManualFadeCheckWarnAndFillWarn");
		System.out.println(formInfo.getClrntSysId());

		List<SwMessage> result = target.manualFormulationWarnings(formInfo);

		int countOut=result.size();
		boolean countMatch = false;
		if (countOut==2) {
			countMatch = true;
		}
		for(SwMessage i: result){
			System.out.println(i.getSeverity() + " " + i.getCode() + " " + i.getMessage());
		}

		assertTrue(countMatch);
	}

	@Test 
	public void testScenarioFormula(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6611";
		String salesNbr = "640512901";
		String[] illums = new String[3];
		illums[0] = "D65";
		illums[1] = "A";
		illums[2] = "F2";
		
		OeServiceProdDataSet dsProd = productService.getDsProdFromOracleBySalesNbr(salesNbr);
		OeServiceColorDataSet dsColor = colorService.getDsColorFromOracle(colorComp, colorId);
		
		//change interior to exterior, remember cdsProd in dsProd is a list so they all need to change
		for(int i = 0;i<dsProd.getCdsProd().size();i++){
			dsProd.getCdsProd().get(i).setIntExt("EXTERIOR");
		}
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		oeRequest.setIllum(illums);
		oeRequest.setPctFormula(0);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("24.0 Scenario Formula for CCE SW 6611 640512901 as Exterior");
		FormulationResponse result = target.formulate(oeRequest,custWebParms,dsProd,dsColor);
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
			}
		}
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE"));
		
	}
	
	@Test 
	public void testScenarioFormulaNoChanges(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6611";
		String salesNbr = "640512901";
		String[] illums = new String[3];
		illums[0] = "D65";
		illums[1] = "A";
		illums[2] = "F2";
		
		OeServiceProdDataSet dsProd = productService.getDsProdFromOracleBySalesNbr(salesNbr);
		OeServiceColorDataSet dsColor = colorService.getDsColorFromOracle(colorComp, colorId);
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		oeRequest.setIllum(illums);
		oeRequest.setPctFormula(0);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("25.0 Scenario Formula for CCE SW 6611 640512901 no changes so it will run as interior");
		FormulationResponse result = target.formulate(oeRequest,custWebParms,dsProd,dsColor);
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
			}
		}
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE"));
		
	}

	
	@Test 
	public void testVinylSafeFormulaBook(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6521";
		String salesNbr = "640413720";

		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(true);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("26.0 Vinyl Safe Formula Book for CCE SW 6521 640413720");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE"));
		
	}
	
	@Test 
	public void testProdFamilyFormulationCase2(){
		String clrntSysId = "CCE";
		String colorComp = "BEAU MONDE II";
		String colorId = "BM-1-23";
		String salesNbr = "640399697";
		String[] illums = {"D65","A","F2"};
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion("BEAU MONDE II", "BM-1-23", new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		oeRequest.setIllum(illums);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("27.0 Product Family Formula - Case 2 - CCE BM-1-23 A06T00154 (640399697)");
		FormulationResponse result = target.prodFamilyFormulate(oeRequest,custWebParms);
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE")); // TODO test for ...  && curveMatch);
		
	}
	
	@Test 
	public void testProdFamilyFormulationWithNoPrimaryProdFamily(){
		String clrntSysId = "CCE";
		String colorComp = "ACE";
		String colorId = "01A-5";
		String salesNbr = "650187529";
		String[] illums = {"A","D65","F2"};
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion("ACE", "01A-5", new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		oeRequest.setIllum(illums);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("28.0 Product Family Formula - Case 2 - CCE ACE 01A-5 A96T01254 (650187529)");
		FormulationResponse result = target.prodFamilyFormulate(oeRequest,custWebParms);
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE")); // TODO test for ...  && curveMatch);
		
	}
	
	@Test 
	public void testProdFamilyFormulationCase3(){
		String clrntSysId = "CCE";
		String colorComp = "BENJAMIN MOORE";
		String colorId = "2020-70";
		String salesNbr = "650096514";
		String[] illums = {"A","D65","F2"};
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion("BENJAMIN MOORE", "2020-70", new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		oeRequest.setIllum(illums);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("29.0 Product Family Formula - Case 3 - CCE 2020-70 A96W01251 (650096514)");
		FormulationResponse result = target.prodFamilyFormulate(oeRequest,custWebParms);
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b PICKPRODFAM)");
		assertTrue(result.getStatus().equalsIgnoreCase("PICKPRODFAM")); // TODO test for ...  && curveMatch);
		
	}
	
	@Test 
	public void testProdFamilyFormulationFailure(){
		String clrntSysId = "CCE";
		String colorComp = "MAB";
		String colorId = "200-1361";
		String salesNbr = "650096514";
		String[] illums = {"A","D65","F2"};
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		oeRequest.setIllum(illums);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("30.0 Product Family Formula Failure - CCE 200-1361 A96W01251 (650096514)");
		FormulationResponse result = target.prodFamilyFormulate(oeRequest,custWebParms);
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b ERROR)");
		assertTrue(result.getStatus().equalsIgnoreCase("ERROR"));;
		
	}
	
	@Test 
	public void testProdFamilyFormulationCase4(){
		String clrntSysId = "CCE";
		String colorComp = "BENJAMIN MOORE";
		String colorId = "INT RM 4";
		String salesNbr = "640373866";
		String[] illums = {"A","D65","F2"};
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		oeRequest.setIllum(illums);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("31.0 Product Family Formula Case 4 - CCE INT RM 4 A91W00253 QT (640373866)");
		FormulationResponse result = target.prodFamilyFormulate(oeRequest,custWebParms);
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
			}
		}
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b PICKPRODFAM)");
		assertTrue(result.getStatus().equalsIgnoreCase("PICKPRODFAM"));;
		
	}
	

	@Test 
	public void testProjectionCase1(){
		String clrntSysId = "CCE";
		String colorComp = "CUSTOM";
		String colorId = "MANUAL";
		String salesNbr = "640373866";
		List<FormulaIngredient> ingredientList = new ArrayList<FormulaIngredient>();
		
		FormulaIngredient item1 = new FormulaIngredient();
		item1.setClrntSysId(clrntSysId);
		item1.setTintSysId("B1");
		item1.setShots(15);
		ingredientList.add(item1);
		
		FormulaIngredient item2 = new FormulaIngredient();
		item2.setClrntSysId(clrntSysId);
		item2.setTintSysId("R2");
		item2.setShots(48);
		ingredientList.add(item2);
		
		FormulaIngredient item3 = new FormulaIngredient();
		item3.setClrntSysId(clrntSysId);
		item3.setTintSysId("Y3");
		item3.setShots(17);
		ingredientList.add(item3);
		
		FormulaInfo formInfo = new FormulaInfo();
		formInfo.setColorComp(colorComp);
		formInfo.setColorId(colorId);
		formInfo.setClrntSysId(clrntSysId);
		formInfo.setSalesNbr(salesNbr);
		formInfo.setIngredients(ingredientList);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("32.0 Projection Case 4 - B1/R2/Y3");
		Double[] result = target.projectCurve(formInfo, custWebParms);
		if(result!=null){
			System.out.println("Returned " + result.length + " point curve.");
			System.out.println("-------> " + Arrays.toString(result));
		}
		assertNotNull(result);;
		
	}
	
	@Test
	public void testAutoPercentSherColor(){
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6385";
		String salesNbr = "650519234";
		BigDecimal[] verifyStandard = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK").getCurve();
		boolean curveMatch=false;
		
		OeFormInputRequest oeRequest = new OeFormInputRequest();
		oeRequest.setClrntSysId(clrntSysId);
		oeRequest.setColorComp(colorComp);
		oeRequest.setColorId(colorId);
		oeRequest.setSalesNbr(salesNbr);
		oeRequest.setVinylSafe(false);
		//oeRequest.setPctFormula(100);
		
		CustWebParms custWebParms = custWebParmsDao.read("400000000",1);

		System.out.println("------------------------------------------------------------------");
		System.out.println("33.0 Percent SherColor Formula Case 2 for CCE SW 6385 650519234");
		FormulationResponse result = target.formulate(oeRequest,custWebParms);
		if(result.getMessages()!=null){
			System.out.println("Returned " + result.getMessages().size() + " messages.");
			for(SwMessage msg : result.getMessages()){
				System.out.println("Message -> " + msg.getSeverity() + " " + msg.getCode() + " " + msg.getMessage());
			}
		}
		if(result.getFormulas()!=null){
			System.out.println("Returned " + result.getFormulas().size() + " formulas.");
			int i = 1;
			for(FormulaInfo form : result.getFormulas()){
				System.out.println("Choice " + i + " guid=" + form.getGuid());
				i++;
				System.out.println(form.getSource() + " - " + form.getSourceDescr());
				System.out.println(form.getFbBase() + " and " + form.getFbSubBase());
				System.out.println(form.getClrntSysId() + " " + form.getClrntSysName() + " " + form.getIncrementHdr());
				for(FormulaIngredient colorant : form.getIngredients()){
					System.out.println(colorant.getTintSysId() + " " + colorant.getName() + " " + Arrays.toString(colorant.getIncrement()));
				}
				System.out.println("EngineCurve-> " + Arrays.toString(form.getMeasuredCurve()));
				System.out.println("VerifyCurve-> " + Arrays.toString(verifyStandard));
				curveMatch=true;
				for(int j=0;i<40;i++){
					if(form.getMeasuredCurve()[j] != verifyStandard[j].doubleValue()) curveMatch=false;
				}
				if(curveMatch){
					System.out.println("Curve Matches");
				} else {
					System.out.println("Curve DOES NOT Match");
				}
			}
		}
		System.out.println("Next Action = " + result.getStatus() + " (s/b COMPLETE)");
		assertTrue(result.getStatus().equalsIgnoreCase("COMPLETE")); // TODO test for ... && curveMatch 
		
	}


}
