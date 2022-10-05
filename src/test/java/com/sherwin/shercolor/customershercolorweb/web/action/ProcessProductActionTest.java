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
public class ProcessProductActionTest extends StrutsSpringJUnit4TestCase<ProcessProductAction> {

	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";

	@Test
	public void testfillProdNbrActionA() {
		System.out.println("Start testfillProdNbrActionA");
		
		reqObj.setCustomerID("CCF");
		request.setParameter("shorthandProdNbr", "a6w151");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		try {
			String json = executeAction("/fillProdNbrAction");
			assertTrue(json.contains("A06W00151"));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("End testfillProdNbrActionA");
	}
	
	@Test
	public void testfillProdNbrActionB() {
		System.out.println("Start testfillProdNbrActionB");
		
		reqObj.setCustomerID("CCF");
		request.setParameter("shorthandProdNbr", "KW7730-16");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		try {
			String json = executeAction("/fillProdNbrAction");
			assertTrue(json.contains("KW0007730-16"));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("End testfillProdNbrActionB");
	}
	
	@Test
	public void testfillProdNbrActionC() {
		System.out.println("Start testfillProdNbrActionC");
		
		reqObj.setCustomerID("CCF");
		request.setParameter("shorthandProdNbr", "a8t154 20");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		try {
			String json = executeAction("/fillProdNbrAction");
			assertTrue(json.contains("A08T00154-20"));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("End testfillProdNbrActionC");
	}
	
	@Test
	public void testListProductsFillProdA() {
		System.out.println("Start testListProductsFillProdA");
		
		reqObj.setCustomerID("CCF");
		reqObj.setColorType("SW");
		reqObj.setColorID("GYPSY RED");
		reqObj.setColorID("6865");
		reqObj.setColorComp("SHERWIN-WILLIAMS");
		reqObj.setIntBases("REAL RED,CHALKY UD,ULTRADEEP,TINT BASE");
		reqObj.setExtBases("ULTRADEEP,CHROMA,DEEPTONE,REAL RED,TINT BASE");
		request.setParameter("partialProductNameOrId", "a8t154");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		try {
			String json = executeAction("/listProducts");
			assertTrue(json.contains("A08T00154"));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("End testListProductsFillProdA");
	}
	
	@Test
	public void testListProductsFillProdB() {
		System.out.println("Start testListProductsFillProdB");
		
		reqObj.setCustomerID("CCF");
		reqObj.setColorType("CUSTOM");
		reqObj.setColorID("RED");
		reqObj.setColorID("MANUAL");
		reqObj.setColorComp("CUSTOM");
		reqObj.setIntBases("");
		reqObj.setExtBases("");
		request.setParameter("partialProductNameOrId", "a8t154");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		try {
			String json = executeAction("/listProducts");
			assertTrue(json.contains("A08T00154"));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("End testListProductsFillProdB");
	}
}
