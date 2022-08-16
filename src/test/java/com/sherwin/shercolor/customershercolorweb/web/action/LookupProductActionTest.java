package com.sherwin.shercolor.customershercolorweb.web.action;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class LookupProductActionTest extends StrutsSpringJUnit4TestCase<LookupJobAction> {

	LookupJobAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";

	@Test
	public void testLookupJobsAction() {
		System.out.println("Start testLookupJobsAction");
		reqObj.setCustomerID("CCF");
		reqObj.setExtBases("ULTRADEEP,ACCENT,CHALKY UD");
		reqObj.setIntBases("ULTRADEEP,DEEPTONE,ACCENT");
		reqObj.setColorID("2802");
		reqObj.setColorName("ROOKWOOD RED");
		reqObj.setColorComp("SHERWIN-WILLIAMS");
		
		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		try {
			String json = executeAction("/loadProductLookupAction");
			assertTrue(json.contains("productList"));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("End testLookupJobsAction");
	}
	
	@Test
	public void testLookupJobsActionNullExt() {
		System.out.println("Start testLookupJobsActionNullExt");
		reqObj.setCustomerID("CCF");
		reqObj.setExtBases(null);
		reqObj.setIntBases("ULTRADEEP,DEEPTONE,ACCENT");
		reqObj.setColorID("2802");
		reqObj.setColorName("ROOKWOOD RED");
		reqObj.setColorComp("SHERWIN-WILLIAMS");
		
		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		try {
			String json = executeAction("/loadProductLookupAction");
			assertTrue(json.contains("productList"));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("End testLookupJobsActionNullExt");
	}
	
	@Test
	public void testLookupJobsActionNullInt() {
		System.out.println("Start testLookupJobsActionNullInt");
		reqObj.setCustomerID("CCF");
		reqObj.setExtBases("ULTRADEEP,ACCENT,CHALKY UD");
		reqObj.setIntBases(null);
		reqObj.setColorID("2802");
		reqObj.setColorName("ROOKWOOD RED");
		reqObj.setColorComp("SHERWIN-WILLIAMS");
		
		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		try {
			String json = executeAction("/loadProductLookupAction");
			assertTrue(json.contains("productList"));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("End testLookupJobsActionNullInt");
	}
}
