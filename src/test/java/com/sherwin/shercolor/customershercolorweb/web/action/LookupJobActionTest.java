package com.sherwin.shercolor.customershercolorweb.web.action;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.StrutsSpringTestCase;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class LookupJobActionTest extends StrutsSpringJUnit4TestCase<LookupJobAction> {

	LookupJobAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";
	
	@Autowired
	private transient TranHistoryService tranHistoryService;

	@Test
	public void testLookupJobsAction() {
		System.out.println("Start Lookup Jobs Action Test");
		reqObj.setCustomerID("CCF");

		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);


		
		try {
			String json = executeAction("/listJobsAction");
			assertNotNull(json);
			LookupJobAction result = new Gson().fromJson(json,LookupJobAction.class);
			System.out.println(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	
		System.out.println("End Lookup Jobs Action Test");

	}
	
	@Test
	public void testSelectJobAction_compareColors() {
		reqObj.setCustomerID("400000008");
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("compareColors", "true");
		
		ActionProxy proxy = getActionProxy("/selectJobAction");
        assertNotNull(proxy);
        
        target = (LookupJobAction) proxy.getAction();
        target.setLookupControlNbr(5744);
        
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
}
