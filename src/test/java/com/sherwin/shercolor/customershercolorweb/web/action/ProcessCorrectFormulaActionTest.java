package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertNotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterCanister;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class ProcessCorrectFormulaActionTest extends StrutsSpringJUnit4TestCase<ProcessCorrectFormulaAction> {

	ProcessCorrectFormulaAction target;
	RequestObject reqObj = new RequestObject();
	private static String[] clrntSysIds = {"CCE","CCE","CCE"};
	private static String[] tintSysIds = {"B1","W1","N1"};
	private static String[] fbSysIds = {"B1","W1","N1"};
	private static String[] names = {"Black","White","Raw Umber"};
	private static String[] engSysIds = {"A60B1","A60W1","A60N1"};
	private static String[] statusInds = {"A","A","A"};
	private static int[] shots = {100,100,100};
	private static int[] shotSizes = {128,128,128};
	private static int[] incrs = {0,25,0,0};

	TinterInfo tintInfo = new TinterInfo();
	private static int[] position = {1,2,3,4,5,6,7,8,9,10,11,12};
	private static double[] maxCanisterFill = {512,512,192,192,192,192,512,512,192,192,192,192};
	private static double[] fillAlarmLevel = {64,64,32,32,32,32,64,64,32,32,32,32};
	private static double[] currentColorantAmt = {512,512,192,192,192,192,512,512,192,192,192,192};
	private static double[] fillStopLevel = {1.2,1.2,1.2,1.2,1.2,1.2,1.2,1.2,1.2,1.2,1.2,1.2};
	private static String[] colorants = {"W1","N1","R4","R3","G2","NA","B1","Y3","L1","R2","Y1","NA"};
	private static String[] colorantName = {"White", "Raw Umber", "New Red", "Magenta", "New Green", null, "Black", "Deep Gold", "Blue", "Maroon", "Yellow", null};
	private static String[] rgbHex = {"#ffffff","#996633","#ff0000","#ff3399","#009933","#000000","#000000","#ffcc00","#0000ff","#990000","#ffff00","#000000"};
	
	@Before
	public void setup() {
		reqObj.setGuid("123456789");
		reqObj.setCustomerID("LB6110");
		reqObj.setUserId("TEST");
		reqObj.setCustomerName("SC STORE");
		reqObj.setCustomerType("CUSTOMER");
		reqObj.setDisplayFormula(setupFormulaInfo());
		reqObj.setClrntSys("CCE");
		reqObj.setControlNbr(1);
		reqObj.setLineNbr(1);
		tintInfo.setClrntSysId(reqObj.getClrntSys());
		tintInfo.setModel("SIMULATOR");
		tintInfo.setCanisterList(loadCanisterInfo());
		reqObj.setTinter(tintInfo);

	}

	private FormulaInfo setupFormulaInfo() {
		FormulaInfo formInfo = new FormulaInfo();
		formInfo.setGuid(reqObj.getGuid());
		formInfo.setColorComp("CUSTOM");
		formInfo.setColorId("MANUAL");
		formInfo.setProdComp("SW");
		formInfo.setSalesNbr("640389151");
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		List<String> incrHdrList = new ArrayList<>();
			incrHdrList.add("OZ");
			incrHdrList.add("32");
			incrHdrList.add("64");
			incrHdrList.add("128");
		formInfo.setIncrementHdr(incrHdrList);
		formInfo.setIngredients(setupFormulaIngredients());
		formInfo.setProjectedCurve(new Double[40]);
		formInfo.setMeasuredCurve(new Double[40]);
		return formInfo;
	}

	private List<FormulaIngredient> setupFormulaIngredients() {
		List<FormulaIngredient> ingredients = new ArrayList<>();
		for(int i = 0; i < clrntSysIds.length; i++) {
			FormulaIngredient ingredient = new FormulaIngredient();
			ingredient.setClrntSysId(clrntSysIds[i]);
			ingredient.setTintSysId(tintSysIds[i]);
			ingredient.setFbSysId(fbSysIds[i]);
			ingredient.setName(names[i]);
			ingredient.setEngSysId(engSysIds[i]);
			ingredient.setStatusInd(statusInds[i]);
			ingredient.setShots(shots[i]);
			ingredient.setShotSize(shotSizes[i]);
			ingredient.setIncrement(incrs);
			ingredients.add(ingredient);
		}
		return ingredients;
	}

	private List<TinterCanister> loadCanisterInfo() {
		List<TinterCanister> sortedCanList = new ArrayList<>();
		for(int i = 0; i < position.length; i++) {
			TinterCanister canister = new TinterCanister();
			canister.setPosition(position[i]);
			canister.setMaxCanisterFill(maxCanisterFill[i]);
			canister.setFillAlarmLevel(fillAlarmLevel[i]);
			canister.setCurrentClrntAmount(currentColorantAmt[i]);
			canister.setFillStopLevel(fillStopLevel[i]);
			canister.setClrntCode(colorants[i]);
			canister.setClrntName(colorantName[i]);
			canister.setRgbHex(rgbHex[i]);
			sortedCanList.add(canister);
		}

		return sortedCanList;
	}

	@Test
	public void testDisplayAction() {
		ActionProxy proxy = getActionProxy("/formulaUserCorrectAction");
		target = (ProcessCorrectFormulaAction) proxy.getAction();

		target.setReqGuid("123456789");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/formulaUserCorrectAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}

	}

	@Test
	public void testPercentAddition() {
		ActionProxy proxy = getActionProxy("/percentOfFormulaAction");
		target = (ProcessCorrectFormulaAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPercentOfFormula(10);

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/percentOfFormulaAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}

	@Test
	public void testSaveCorrectionStep() {
		ActionProxy proxy = getActionProxy("/saveCorrectionStepAction");
		target = (ProcessCorrectFormulaAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setStepStatus("ACCEPTED");
		target.setNextUnitNbr(1);
		reqObj.setQuantityDispensed(1);


		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/saveCorrectionStepAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}

	@Test
	public void testPostContainerStatus() {
		ActionProxy proxy = getActionProxy("/postCorrectionStatusAction");
		target = (ProcessCorrectFormulaAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setStepStatus("ACCEPTED");
		target.setNextUnitNbr(1);
		target.setCycle(1);
		reqObj.setQuantityDispensed(1);


		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/postCorrectionStatusAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testColorantLookupForManual() {
		ActionProxy proxy = getActionProxy("/getColorantsForManualCorrection");
		target = (ProcessCorrectFormulaAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setClrntSysId("CCE");
		reqObj.setQuantityDispensed(1);


		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/getColorantsForManualCorrection");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testConvertFormulaToDispenseItems() {
		ActionProxy proxy = getActionProxy("/correctionConvertIncrementsAction");
		target = (ProcessCorrectFormulaAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setStepStatus("ACCEPTED");
		target.setNextUnitNbr(1);
		target.setCycle(1);
		reqObj.setQuantityDispensed(1);
		
		List<Map<String,Object>> rawCorrectList = new ArrayList<>();
		Map<String,Object> listItems = new HashMap<>();
		List<Long> values = new ArrayList<>();
		values.add(Long.valueOf(1));
		values.add(Long.valueOf(0));
		values.add(Long.valueOf(1));
		values.add(Long.valueOf(0));
		listItems.put("incrArray", values);
		listItems.put("clrntString", "B1-Black");
		rawCorrectList.add(listItems);
		target.setCorrectionList(rawCorrectList);
		

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/correctionConvertIncrementsAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testConvertFormulaToDispenseItemsTinterWithDiffClrntSys() {
		ActionProxy proxy = getActionProxy("/correctionConvertIncrementsAction");
		target = (ProcessCorrectFormulaAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setStepStatus("ACCEPTED");
		target.setNextUnitNbr(1);
		target.setCycle(1);
		reqObj.getTinter().setClrntSysId("BAC");
		reqObj.setQuantityDispensed(1);
		
		List<Map<String,Object>> rawCorrectList = new ArrayList<>();
		Map<String,Object> listItems = new HashMap<>();
		List<Long> values = new ArrayList<>();
		values.add(Long.valueOf(1));
		values.add(Long.valueOf(0));
		values.add(Long.valueOf(1));
		values.add(Long.valueOf(0));
		listItems.put("incrArray", values);
		listItems.put("clrntString", "B1-Black");
		rawCorrectList.add(listItems);
		target.setCorrectionList(rawCorrectList);
		

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/correctionConvertIncrementsAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testConvertFormulaToDispenseItemsNoList() {
		ActionProxy proxy = getActionProxy("/correctionConvertIncrementsAction");
		target = (ProcessCorrectFormulaAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setStepStatus("ACCEPTED");
		target.setNextUnitNbr(1);
		target.setCycle(1);
		reqObj.getTinter().setClrntSysId("CCE");
		reqObj.setQuantityDispensed(1);
		

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/correctionConvertIncrementsAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}

}