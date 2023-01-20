package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.*;

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
public class SpectroCalibrateActionTest extends StrutsSpringJUnit4TestCase<SpectroCalibrateAction> {
	
	SpectroCalibrateAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";
	
	@Test
	public void testSpectroCalibrateAction_measureStandardRedirect_success() {
		reqObj.setCustomerID("TEST");
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("measureStandard","true");
		request.setParameter("compareColors","true");
		
		ActionProxy proxy = getActionProxy("/spectroCalibrateAction");
        assertNotNull(proxy);
        
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        target = (SpectroCalibrateAction) proxy.getAction();
        target.setMeasureStandard(true);
        target.setCompareColors(true);
        target.setSession(sessionMap);
        
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
	public void testSpectroCalibrateAction_measureSampleRedirect_success() {
		reqObj.setCustomerID("TEST");
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("measureSample","true");
		request.setParameter("compareColors","true");
		
		ActionProxy proxy = getActionProxy("/spectroCalibrateAction");
        assertNotNull(proxy);
        
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        target = (SpectroCalibrateAction) proxy.getAction();
        target.setMeasureSample(true);
        target.setCompareColors(true);
        target.setSession(sessionMap);
        
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

}
