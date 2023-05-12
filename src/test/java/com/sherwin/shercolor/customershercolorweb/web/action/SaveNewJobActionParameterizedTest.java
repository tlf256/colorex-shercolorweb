package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@RunWith(Parameterized.class)
@SherColorWebTransactionalTest
public class SaveNewJobActionParameterizedTest extends StrutsSpringJUnit4TestCase<SaveNewJobAction> {
	RequestObject reqObj = new RequestObject();
	SaveNewJobAction target;
	
	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();
	
	@Parameter
    public String actionName;
    
    @Parameters
    public static Object[] data() {
    	return new Object[] {"/formulaUserSaveAndContinueAction"};
    }
    
    @Before
	public void setup() {
		reqObj.setGuid("123456789");
		reqObj.setCustomerID("10110001");
		reqObj.setCustomerName("TEST CUSTOMER");
		reqObj.setJobFieldList(new java.util.ArrayList<JobField>());
		
		java.util.List<JobField> jobFieldList = new java.util.ArrayList<>();
		for(int i = 0; i < 9; i++) {
			JobField job = new JobField();
			job.setEnteredValue("Entered Value" + i);
			job.setRequiredText("Required Text" + i);
			job.setScreenLabel("Screen Label" + i);
			jobFieldList.add(job);
		}
		
		reqObj.setJobFieldList(jobFieldList);
		
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
    }
    
    @Test
    public void testAllSaveNewJobActions() {
    	request.setParameter("reqGuid", reqObj.getGuid());
		
    	ActionProxy proxy = getActionProxy(actionName);
		System.out.println("actionName is " + actionName);
		target = (SaveNewJobAction) proxy.getAction();
		
		target.setReqGuid(reqObj.getGuid());
		
		try {
			
			Map<String, Object> sessionMap = new HashMap<>();
	        sessionMap.put(reqObj.getGuid(), reqObj);
			proxy.getInvocation().getInvocationContext().setSession(sessionMap);
			
			String result = proxy.execute();
			assertEquals(Action.SUCCESS, result);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}
