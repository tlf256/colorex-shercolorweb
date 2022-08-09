package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class ProcessColorActionTest extends StrutsSpringJUnit4TestCase<ProcessColorAction> {
	
	ProcessColorAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";
	
	@Test
	public void testColorUserNextAction_compareColors() {
		reqObj.setCustomerID("TEST");
		reqObj.setCustomerType("CUSTOMER");
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("compareColors", "true");
		
		ActionProxy proxy = getActionProxy("/colorUserNextAction");
        assertNotNull(proxy);
        
        target = (ProcessColorAction) proxy.getAction();
        target.setCompareColors(true);
        target.setSelectedCoTypes("SW");
        target.setPartialColorNameOrId("SHERWIN-WILLIAMS 6385");
        target.setColorData("%5B%7B%22colorNumber%22%3A%226385%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226385%20DOVER%20WHITE%20261-C2%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206385%22%7D%5D");        
        
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			
			assertEquals("compareColors", result);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Test
	public void testColorUserNextAction_measure() {
		reqObj.setCustomerID("TEST");
		reqObj.setCustomerType("CUSTOMER");
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("compareColors", "true");
		
		ActionProxy proxy = getActionProxy("/colorUserNextAction");
        assertNotNull(proxy);
        
        target = (ProcessColorAction) proxy.getAction();
        target.setCompareColors(true);
        target.setSelectedCoTypes("CUSTOMMATCH");
        target.setPartialColorNameOrId("MATCH");
        
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			
			assertEquals("measure", result);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Test
	public void testColorUserNextAction_existingMatch() {
		reqObj.setCustomerID("TEST");
		reqObj.setCustomerType("CUSTOMER");
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("compareColors", "true");
		
		ActionProxy proxy = getActionProxy("/colorUserNextAction");
        assertNotNull(proxy);
        
        target = (ProcessColorAction) proxy.getAction();
        target.setCompareColors(true);
        target.setSelectedCoTypes("EXISTING_MATCH");
        target.setPartialColorNameOrId("MATCH");
        
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			
			assertEquals("existingMatch", result);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}

}
