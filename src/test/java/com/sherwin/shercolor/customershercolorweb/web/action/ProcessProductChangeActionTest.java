package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class ProcessProductChangeActionTest extends StrutsSpringJUnit4TestCase<ProcessProductChangeAction> {

	ProcessProductChangeAction target;
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

	@Before
	public void setup() {
		reqObj.setGuid("123456789");
		reqObj.setCustomerID("LB6110");
		reqObj.setUserId("TEST");
		reqObj.setCustomerName("SC STORE");
		reqObj.setCustomerType("CUSTOMER");
		reqObj.setColorType("CUSTOM");
		reqObj.setColorComp("SHERWIN-WILLIAMS");
		reqObj.setColorID("0001");
		reqObj.setDisplayFormula(setupFormulaInfo());
		reqObj.setSizeCode("16");
		reqObj.setClrntSys("CCE");
		FormulationResponse formResponse = new FormulationResponse();
		formResponse.setStatus("COMPLETE");
		formResponse.setMessages(null);
		List<FormulaInfo> info = new ArrayList<>();
		info.add(reqObj.getDisplayFormula());
		formResponse.setFormulas(info);
		reqObj.setFormResponse(formResponse);
	}

	private FormulaInfo setupFormulaInfo() {
		FormulaInfo formInfo = new FormulaInfo();
		formInfo.setGuid(reqObj.getGuid());
		formInfo.setColorComp("SHERWIN-WILLIAMS");
		formInfo.setColorId("0001");
		formInfo.setProdComp("SW");
		formInfo.setSalesNbr("640389151");
		formInfo.setClrntSysId("CCE");
		formInfo.setClrntSysName("Color Cast");
		formInfo.setSourceDescr("SHERCOLOR%FORMULA");
		formInfo.setProdNbr("A06W00151");
		formInfo.setSizeCode("16");
		List<String> incrHdrList = new ArrayList<>();
			incrHdrList.add("OZ");
			incrHdrList.add("32");
			incrHdrList.add("64");
			incrHdrList.add("128");
		formInfo.setIncrementHdr(incrHdrList);
		formInfo.setIngredients(setupFormulaIngredients());
		formInfo.setProjectedCurve(new Double[40]);
		formInfo.setMeasuredCurve(new Double[40]);
		formInfo.setContrastRatioThin(0.0);
		formInfo.setContrastRatioThick(0.0);
		formInfo.setAverageDeltaE(0.35);
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


	@Test
	public void testLookupProductOptionsAction() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/lookupProductOptions");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPartialProductNameOrId("640512901");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		String success = executeAction("/lookupProductOptions");
		System.out.println("Success String: " + success);
		assertTrue(StringUtils.contains(success, "WARNING - Product base type INTERIOR EXTRA WHITE is not the primary base type."));

	}
	
	@Test
	public void testLookupProductOptionsActionCannotUseClrntSys() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/lookupProductOptions");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPartialProductNameOrId("640362463");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		String success = executeAction("/lookupProductOptions");
		System.out.println("Success String: " + success);
		assertTrue(StringUtils.contains(success, "The entered product 640362463 is not able to use the selected colorant system CCE. (code: 409)."));
			
	}
	
	@Test
	public void testLookupProductOptionsActionNoCdsProduct() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/lookupProductOptions");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPartialProductNameOrId("zz99zz99");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		String success = executeAction("/lookupProductOptions");
		System.out.println("Success String: " + success);
		assertTrue(StringUtils.contains(success, "The entered product sales, UPC or rex number was not found in the Master Product table. (code : 405) zz99zz99."));

	}
	
	@Test
	public void testLookupProductOptionsActionUndefinedSizeCode() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/lookupProductOptions");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPartialProductNameOrId("100000157");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		String success = executeAction("/lookupProductOptions");
		System.out.println("Success String: " + success);
		assertTrue(StringUtils.contains(success, "WARNING. The tint info for the entered product is not defined in Sher-Color."));
	}
	
	@Test
	public void testLookupProductOptionsActionProductColorFail() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/lookupProductOptions");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPartialProductNameOrId("651085862");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		String success = executeAction("/lookupProductOptions");
		System.out.println("Success String: " + success);
		assertTrue(StringUtils.contains(success, "Formula Unavailable. This color\\/base assignment (SHERWIN-WILLIAMS-0001\\/CHALKY EW) is not optimized. Please use this color with UltraDeep base."));
	}

	@Test
	public void testUpdateProductNoAdjustmentAction() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/updateProductNoAdjustment");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("640512901");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		executeAction("/updateProductNoAdjustment");
		assertEquals("B1", reqObj.getDisplayFormula().getIngredients().get(0).getTintSysId());
		assertEquals("W1", reqObj.getDisplayFormula().getIngredients().get(1).getTintSysId());
		assertEquals("N1", reqObj.getDisplayFormula().getIngredients().get(2).getTintSysId());
		assertEquals(100, reqObj.getDisplayFormula().getIngredients().get(0).getShots());
		assertEquals(100, reqObj.getDisplayFormula().getIngredients().get(1).getShots());
		assertEquals(100, reqObj.getDisplayFormula().getIngredients().get(2).getShots());

	}

	@Test
	public void testUpdateProductSizeChangeAction() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/updateProductSizeChange");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("640512919");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		executeAction("/updateProductSizeChange");
		assertEquals(500, reqObj.getDisplayFormula().getIngredients().get(0).getShots());
		assertEquals(500, reqObj.getDisplayFormula().getIngredients().get(1).getShots());
		assertEquals(500, reqObj.getDisplayFormula().getIngredients().get(2).getShots());
	}

	@Test
	public void testUpdateProductReformulateAction() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/updateProductReformulate");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("650427503");
		target.setMakeVinylSafe(true);
		target.setRequireVinylPrompt(true);

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		executeAction("/updateProductReformulate");
		assertEquals("W1", reqObj.getDisplayFormula().getIngredients().get(0).getTintSysId());
		assertEquals("L1", reqObj.getDisplayFormula().getIngredients().get(1).getTintSysId());
		assertEquals("R4", reqObj.getDisplayFormula().getIngredients().get(2).getTintSysId());
		assertEquals(1099, reqObj.getDisplayFormula().getIngredients().get(0).getShots());
		assertEquals(52, reqObj.getDisplayFormula().getIngredients().get(1).getShots());
		assertEquals(88, reqObj.getDisplayFormula().getIngredients().get(2).getShots());
	}

	@Test
	public void testUpdateProductRematchAction() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/updateProductRematch");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("650427503");
		target.setSelectedProdFam("650427503");
		target.setUserIllum("D65");
		target.setMakeVinylSafe(true);
		target.setRequireVinylPrompt(true);
		target.setNewProdNbr("A06T00254");
		String[] curveArray = {"0", "0", "0", "0", "0", "1.2", "2.3", "3.4", "4.5", "5.6",
							   "6.7", "7.8", "8.9", "9.10", "10.11", "11.12", "12.13", "13.14", "14.15", "15.16",
							   "16.17", "17.18", "18.19", "19.20", "20.21", "21.22", "22.23", "23.24", "24.25", "25.26",
							   "26.27", "27.28", "28.29", "30.31", "31.32", "32.33", "34.35", "35.36", "36.37", "37.38"};
		BigDecimal[] bd = new BigDecimal[40];
		for (int i=0; i < 40; i++) {
			bd[i] = new BigDecimal(curveArray[i].trim());
		}
		reqObj.setCurveArray(bd);

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
		executeAction("/updateProductRematch");
		assertEquals("W1", reqObj.getDisplayFormula().getIngredients().get(0).getTintSysId());
		assertEquals("L1", reqObj.getDisplayFormula().getIngredients().get(1).getTintSysId());
		assertEquals("R2", reqObj.getDisplayFormula().getIngredients().get(2).getTintSysId());
		assertEquals(799, reqObj.getDisplayFormula().getIngredients().get(0).getShots());
		assertEquals(134, reqObj.getDisplayFormula().getIngredients().get(1).getShots());
		assertEquals(202, reqObj.getDisplayFormula().getIngredients().get(2).getShots());
		
	}

	@Test
	public void testUpdateProductTintStrength() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/updateProductTintStrength");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("640512901");
		target.setNewTintStrength(125);
		target.setOldTintStrength(100);

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		executeAction("/updateProductTintStrength");
		assertEquals(125, reqObj.getDisplayFormula().getIngredients().get(0).getShots());
		assertEquals(125, reqObj.getDisplayFormula().getIngredients().get(1).getShots());
		assertEquals(125, reqObj.getDisplayFormula().getIngredients().get(2).getShots());
	}

	@Test
	public void testUpdateProductTintStrengthSize() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/updateProductTintStrengthSize");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("640512919");
		target.setNewTintStrength(125);
		target.setOldTintStrength(100);
		target.setOldSizeCode("16");
		target.setNewSizeCode("20");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
				
		executeAction("/updateProductTintStrengthSize");
		assertEquals(625, reqObj.getDisplayFormula().getIngredients().get(0).getShots());
		assertEquals(625, reqObj.getDisplayFormula().getIngredients().get(1).getShots());
		assertEquals(625, reqObj.getDisplayFormula().getIngredients().get(2).getShots());
	}
}
