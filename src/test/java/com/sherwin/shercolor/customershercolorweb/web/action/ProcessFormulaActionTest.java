package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertNotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;
import com.sherwin.shercolor.util.domain.SwMessage;

@RunWith(Parameterized.class)
@SherColorWebTransactionalTest
public class ProcessFormulaActionTest extends StrutsSpringJUnit4TestCase<ProcessFormulaAction> {

	ProcessFormulaAction target;
	RequestObject reqObj = new RequestObject();
	TinterInfo tintInfo = new TinterInfo();
	
    @ClassRule
    public static final SpringClassRule scr = new SpringClassRule();

    @Rule
    public final SpringMethodRule smr = new SpringMethodRule();
    
    @Parameter
    public String actionName;
    
    @Parameters
    public static Object[] data() {
    	return new Object[] {"/formulaUserPrintAction", "/formulaUserPrintAsJsonAction", "/formulaUserPrintActionNoFormula", "/displayFormulaAction"};
    }
	
	@Before
	public void setup() {
		reqObj.setGuid("123456789");
		reqObj.setColorComp("SHERWIN-WILLIAMS");
		reqObj.setColorID("1001");
		reqObj.setColorName("SILVERPLATE");
		reqObj.setColorNotes("MY COOL COLOR NOTES");
		reqObj.setSalesNbr("651175523");
		reqObj.setProdNbr("A87W00001");
		reqObj.setQuality("SUPER PAINT SANITIZING");
		reqObj.setComposite("LATEX");
		reqObj.setBase("EXTRA WHITE");
		reqObj.setFinish("SATIN");
		reqObj.setKlass("ARCHITECTURAL");
		reqObj.setIntExt("INTERIOR");
		reqObj.setSizeText("ONE GALLON");
		reqObj.setClrntSys("CCE");
		reqObj.setCustomerID("LB6110");
		reqObj.setCustomerName("SW STORE");
		tintInfo.setClrntSysId(reqObj.getClrntSys());
		tintInfo.setModel("SIMULATOR");
		reqObj.setTinter(tintInfo);
		
		//Messages
		List<SwMessage> canLabelMsgs = new ArrayList<SwMessage>();
		SwMessage roomMsg = new SwMessage();
		roomMsg.setMessage("BEDROOM");
		canLabelMsgs.add(roomMsg);
		SwMessage vinylMsg = new SwMessage();
		vinylMsg.setMessage("NOT RECOMMENDED FOR USE ON VINYL");
		canLabelMsgs.add(vinylMsg);
		SwMessage primerMsg = new SwMessage();
		primerMsg.setMessage("P2 PRIMER RECOMMENDED FOR THIS COLOR");
		canLabelMsgs.add(primerMsg);
		reqObj.setCanLabelMsgs(canLabelMsgs);		
		FormulationResponse formulationResponse = new FormulationResponse();
		formulationResponse.setMessages(canLabelMsgs);
		reqObj.setFormResponse(formulationResponse);
		
		//Formula
		int [] increment = {10, 33, 1, 1};
		FormulaIngredient ind1 = new FormulaIngredient();
		ind1.setClrntSysId(reqObj.getClrntSys());
		ind1.setFbSysId("K");
		ind1.setTintSysId("B1");
		ind1.setName("BLACK");
		ind1.setShots(128);
		ind1.setIncrement(increment);		
		FormulaIngredient ind2 = new FormulaIngredient();
		ind2.setClrntSysId(reqObj.getClrntSys());
		ind2.setFbSysId("H");
		ind2.setTintSysId("L1");
		ind2.setName("BLUE");
		ind2.setShots(128);
		ind2.setIncrement(increment);		
		FormulaIngredient ind3 = new FormulaIngredient();
		ind3.setClrntSysId(reqObj.getClrntSys());
		ind3.setFbSysId("N");
		ind3.setTintSysId("G2");
		ind3.setName("GREEN");
		ind3.setShots(128);
		ind3.setIncrement(increment);		
		List<FormulaIngredient> ingredients = new ArrayList<>();
		ingredients.add(ind1);
		ingredients.add(ind2);
		ingredients.add(ind3);
		
		FormulaInfo formInfo = new FormulaInfo();
		formInfo.setColorComp(reqObj.getColorComp());
		formInfo.setColorId(reqObj.getColorID());
		formInfo.setSalesNbr(reqObj.getSalesNbr());
		formInfo.setProdNbr(reqObj.getProdNbr());
		formInfo.setProdComp("SW");
		formInfo.setClrntSysId(reqObj.getClrntSys());
		formInfo.setIngredients(ingredients);
		formInfo.setSourceDescr("SHER-COLOR FORMULA");
		formInfo.setProcOrder(9999);
		List<String> incrementHdr = Arrays.asList("OZ", "32", "64", "128");
		formInfo.setIncrementHdr(incrementHdr);
		
		reqObj.setDisplayFormula(formInfo);		
		reqObj.setColorType("SHER-COLOR FORMULA");
		reqObj.setPrimerId("P2");
		reqObj.setSizeCode("16");
		reqObj.setDisplayMsgs(canLabelMsgs);
		
	}
	
	@Test
	public void testProcessFormulaActions() {
		ActionProxy proxy = getActionProxy(actionName);
		System.out.println("actionName is " + actionName);
		target = (ProcessFormulaAction) proxy.getAction();
		
		target.setReqGuid("123456789");
		target.setAccountIsSwStore(true);
		target.setPrintLabelType("storeLabel");
		target.setPrintOrientation("PORTRAIT");
		target.setPrintLabelType("");
		target.setPrintCorrectionLabel(false);
		target.setShotList(null);
		
		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
		
		try {
			String success = executeAction(actionName);
			assertNotNull(success);
			
			if(actionName.equals("/displayFormulaAction")) {
				System.out.println("Running displayFormulaAction using Drawdown account");
				reqObj.setCustomerID("400000002");
				reqObj.setSizeCode("20");
				target.setTinter(tintInfo);
				request.setParameter("reqGuid", reqObj.getGuid());
				session = request.getSession();
				session.setAttribute(reqObj.getGuid(), reqObj);
				success = executeAction(actionName);
				assertNotNull(success);	
				
			}
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
}
