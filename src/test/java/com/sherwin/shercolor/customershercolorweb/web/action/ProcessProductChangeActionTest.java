package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertNotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
	public void testLookupProductOptionsAction() {
		ActionProxy proxy = getActionProxy("/lookupProductOptions");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPartialProductNameOrId("640512901");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/lookupProductOptions");
			assertNotNull(success);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}

	}
	
	@Test
	public void testLookupProductOptionsActionCannotUseClrntSys() {
		ActionProxy proxy = getActionProxy("/lookupProductOptions");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPartialProductNameOrId("640362463");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/lookupProductOptions");
			assertNotNull(success);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}

	}
	
	@Test
	public void testLookupProductOptionsActionNoPosProduct() {
		ActionProxy proxy = getActionProxy("/lookupProductOptions");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPartialProductNameOrId("zz99zz99");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/lookupProductOptions");
			assertNotNull(success);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}

	}
	
	@Test
	public void testLookupProductOptionsActionUndefinedSizeCode() {
		ActionProxy proxy = getActionProxy("/lookupProductOptions");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPartialProductNameOrId("100000157");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/lookupProductOptions");
			assertNotNull(success);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}

	}
	
	@Test
	public void testLookupProductOptionsActionProductColorFail() {
		ActionProxy proxy = getActionProxy("/lookupProductOptions");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setPartialProductNameOrId("651085862");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/lookupProductOptions");
			assertNotNull(success);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}

	}

	@Test
	public void testUpdateProductNoAdjustmentAction() {
		ActionProxy proxy = getActionProxy("/updateProductNoAdjustment");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("640512901");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/updateProductNoAdjustment");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}

	@Test
	public void testUpdateProductSizeChangeAction() {
		ActionProxy proxy = getActionProxy("/updateProductSizeChange");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("640512919");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/updateProductSizeChange");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}

	@Test
	public void testUpdateProductReformulateAction() {
		ActionProxy proxy = getActionProxy("/updateProductReformulate");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("640512901");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/updateProductReformulate");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}

	@Test
	public void testUpdateProductRematchAction() {
		ActionProxy proxy = getActionProxy("/updateProductRematch");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("640389151");
		target.setSelectedProdFam("640389151");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/updateProductRematch");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}

	@Test
	public void testUpdateProductTintStrength() {
		ActionProxy proxy = getActionProxy("/updateProductTintStrength");
		target = (ProcessProductChangeAction) proxy.getAction();

		target.setReqGuid("123456789");
		target.setSalesNbr("640512901");
		target.setNewTintStrength(125);
		target.setOldTintStrength(100);

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/updateProductTintStrength");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}

	@Test
	public void testUpdateProductTintStrengthSize() {
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

		try {
			String success = executeAction("/updateProductTintStrengthSize");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
}
