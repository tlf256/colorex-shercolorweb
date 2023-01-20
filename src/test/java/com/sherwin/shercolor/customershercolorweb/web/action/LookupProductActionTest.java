package com.sherwin.shercolor.customershercolorweb.web.action;

import javax.servlet.http.HttpSession;

import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class LookupProductActionTest extends StrutsSpringJUnit4TestCase<ProcessProductLookupAction> {

	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";

	@Test
	public void testLookupProductAction() {
		System.out.println("Start testLookupProductAction");
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
		System.out.println("End testLookupProductAction");
	}
	
	@Test
	public void testLookupProductActionNullExt() {
		System.out.println("Start testLookupProductActionNullExt");
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
		System.out.println("End testLookupProductActionNullExt");
	}
	
	@Test
	public void testLookupProductActionNullInt() {
		System.out.println("Start testLookupProductActionNullInt");
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
		System.out.println("End testLookupProductActionNullInt");
	}
}
