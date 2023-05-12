package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
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
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@RunWith(Parameterized.class)
@SherColorWebTransactionalTest
public class ProcessJobFieldsActionTest extends StrutsSpringJUnit4TestCase<ProcessJobFieldsAction> {
	RequestObject reqObj = new RequestObject();
	ProcessJobFieldsAction target;
	
	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();
	
	@Parameter
    public String actionName;
    
    @Parameters
    public static Object[] data() {
    	return new Object[] {"/displayJobFieldUpdateAction"};
    }
    
    @Before
	public void setup() {
		reqObj.setGuid("123456789");
		reqObj.setCustomerID("10110001");
		reqObj.setCustomerName("TEST CUSTOMER");
		reqObj.setJobFieldList(new java.util.ArrayList<JobField>());
    }
    
    @Test
    public void testAllProcessJobFieldsActions_success() {
		request.setParameter("reqGuid", reqObj.getGuid());
		
    	ActionProxy proxy = getActionProxy(actionName);
		System.out.println("actionName is " + actionName);
		target = (ProcessJobFieldsAction) proxy.getAction();
		
		target.setReqGuid("123456789");
		
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
    
    @Test
    public void testAllProcessJobFieldsActions_input() {
		request.setParameter("reqGuid", reqObj.getGuid());
		
    	ActionProxy proxy = getActionProxy(actionName);
		System.out.println("actionName is " + actionName);
		target = (ProcessJobFieldsAction) proxy.getAction();
		
		target.setReqGuid("123456789");
		
		try {
			if(actionName.contains("/displayJobFieldUpdateAction")) {
	    		java.util.List<JobField> jobFieldList = new java.util.ArrayList<>();
	    		for(int i = 0; i < 5; i++) {
	    			JobField job = new JobField();
	    			job.setEnteredValue("Entered Value" + i);
	    			job.setRequiredText("Required Text" + i);
	    			job.setScreenLabel("Screen Label" + i);
	    			jobFieldList.add(job);
	    		}
	    		
	    		reqObj.setJobFieldList(jobFieldList);
	    	}
			
			Map<String, Object> sessionMap = new HashMap<>();
	        sessionMap.put(reqObj.getGuid(), reqObj);
			proxy.getInvocation().getInvocationContext().setSession(sessionMap);
			
			String result = proxy.execute();
			assertEquals(Action.INPUT, result);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}
