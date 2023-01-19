package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class MeasureColorActionTest extends StrutsSpringJUnit4TestCase<MeasureColorAction> {
	
	MeasureColorAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";
	double[] curve = {8.5454D,9.8891D,13.0628D,20.4314D,31.6782D,39.3078D,41.9917D,43.7609D,45.9493D,
			47.5962D,48.1183D,48.3551D,48.7558D,49.5168D,50.8593D,52.5787D,54.5094D,56.5196D,58.3763D,
			59.9248D,61.3052D,62.8414D,64.1409D,64.8976D,65.1403D,65.0295D,64.8936D,64.6806D,64.5444D,
			64.4543D,64.3829D,64.3577D,64.229D,64.0975D,63.898D,63.7873D,63.8011D,63.9099D,63.9851D,63.8023D};
	
	@Test
	public void testMeasureColorNextAction_result() {

		reqObj.setCustomerID("TEST");
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("compareColors", "true");
		request.setParameter("measureSample", "true");
		
		ActionProxy proxy = getActionProxy("/MeasureColorNextAction");
        assertNotNull(proxy);
        
        target = (MeasureColorAction) proxy.getAction();
        target.setMeasuredCurve(Arrays.toString(curve).replace("[", "").replace("]", ""));

        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			
			assertEquals("result", result);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Test
	public void testMeasureColorReturnAction_success() {
		reqObj.setCustomerID("TEST");
		
		request.setParameter("reqGuid",reqGuid);
		
		ActionProxy proxy = getActionProxy("/measureColorReturnAction");
        assertNotNull(proxy);

        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			
			assertEquals(Action.SUCCESS, result);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Test
	public void testMeasureColorReturnAction_ciUtility() {
		reqObj.setCustomerID("TEST");
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("compareColors", "true");
		
		ActionProxy proxy = getActionProxy("/measureColorReturnAction");
        assertNotNull(proxy);

        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			
			assertEquals("ciUtility", result);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Test
	public void testMeasureColorReturnAction_getSample() {
		reqObj.setCustomerID("TEST");
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("measureSample", "true");
		
		ActionProxy proxy = getActionProxy("/measureColorReturnAction");
        assertNotNull(proxy);

        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			
			assertEquals("getSample", result);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}

}
