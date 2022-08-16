package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Before;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.SpectroInfo;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/spring/shercolorcommon-test.xml")
public class ProcessColorActionTest extends StrutsSpringJUnit4TestCase<ProcessColorAction> {

	ProcessColorAction target;
	RequestObject reqObj = new RequestObject();
	SpectroInfo spectro = new SpectroInfo();
	
	@Before
	public void setup() {
		reqObj.setGuid("123456789");
		reqObj.setClrntSys("CCE");
		reqObj.setCustomerID("LB6110");
		reqObj.setCustomerName("SW STORE");
		reqObj.setCustomerType("STORE");
		spectro.setSerialNbr(null);
		reqObj.setSpectro(spectro);
	}

	@Test
	public void testListColors() {
		ActionProxy proxy = getActionProxy("/listColors");
		target = (ProcessColorAction) proxy.getAction();
		
		//SW autocomplete
		target.setPartialColorNameOrId("6000");
		request.setParameter("selectedCoType", "SW");
		target.setColorName("SNOWFALL");
		request.setParameter("reqGuid", reqObj.getGuid());
		request.setParameter("colorData", "6000");
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
		
		try {
			String success = executeAction("/listColors");
			assertNotNull(success);
			
			//Compet autocomplete w/ company
			target.setPartialColorNameOrId("101-5");
			target.setColorName("TINT OF TURQUOISE");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("selectedCoType", "COMPET");
			request.setParameter("selectedCompany", "PPG");
			request.setParameter("colorData", "101-5");
			request.setSession(session);
			
			success = executeAction("/listColors");
			assertNotNull(success);
			
			//Compet autocomplete w/o company
			target.setPartialColorNameOrId("101-5");
			target.setColorName("TINT OF TURQUOISE");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("selectedCoType", "COMPET");
			request.setParameter("selectedCompany", "ALL");
			request.setParameter("colorData", "101-5");
			request.setSession(session);
			
			success = executeAction("/listColors");
			assertNotNull(success);
			
			//Nat. Acct. autocomplete w/ company
			target.setPartialColorNameOrId("CL-5");
			target.setColorName("");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "CL-5");
			request.setParameter("selectedCompany", "ALDI INC");
			request.setParameter("selectedCoType", "NAT");
			request.setSession(session);
			
			success = executeAction("/listColors");
			assertNotNull(success);
			
			//Nat. Acct. autocomplete w/ company
			target.setPartialColorNameOrId("CL-5");
			target.setColorName("");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("selectedCompany", "ALL");
			request.setParameter("colorData", "CL-5");
			request.setSession(session);
			
			success = executeAction("/listColors");
			assertNotNull(success);
			
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
    }
 }

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
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testDisplay() {
		ActionProxy proxy = getActionProxy("/displayColorAction");
		target = (ProcessColorAction) proxy.getAction();
		
		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
		
		try {
			String success = executeAction("/displayColorAction");
			assertNotNull(success);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testExecute() {
		ActionProxy proxy = getActionProxy("/colorUserNextAction");
		target = (ProcessColorAction) proxy.getAction();
		
		//SW test execute
		target.setSelectedCoTypes("SW");
		target.setPartialColorNameOrId("1015");
		target.setColorName("SKYLINE STEEL");
		
		request.setParameter("reqGuid", reqObj.getGuid());
		request.setParameter("colorData", "1015");
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
		
		try {
			String success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//SW test execute simulating autocomplete selection
			target.setColorName("SILVERPLATE");
			target.setPartialColorNameOrId("SHERWIN-WILLIAMS 1001");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "[{\"colorNumber\":\"1001\",\"companyName\":\"SHERWIN-WILLIAMS\",\"label\":\"1001 SILVERPLATE\",\"value\":\"SHERWIN-WILLIAMS 1001\"}]");
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//SW test execute simulating autocomplete selection failure on match
			target.setColorName("SILVERPLATE");
			target.setPartialColorNameOrId(" 1001");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "[{\"colorNumber\":\"1001\",\"companyName\":\"SHERWIN-WILLIAMS\",\"label\":\"1001 SILVERPLATE\",\"value\":\"SHERWIN-WILLIAMS 1001\"}]");
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Nat Acct. execute w/ interior forced product.
			target.setPartialColorNameOrId("CL-5");
			
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "CL-5");
			request.setParameter("selectedCompany", "ALDI INC");
			request.setParameter("selectedCoTypes", "NAT");
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Nat Acct. execute w/ Int/Ext forced product.
			target.setPartialColorNameOrId("CL-1");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "CL-1");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Nat. Acct. execute w/ Exterior forced product.
			target.setPartialColorNameOrId("EP3");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "EP3");
			request.setParameter("selectedCompany", "CHILIS RESTAURANTS");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Nat. Acct. execute with xref-id xref-comp values.
			target.setPartialColorNameOrId("C-2");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "C-2");
			request.setParameter("selectedCompany", "BUFFALO WILD WINGS");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Custom execute.
			target.setPartialColorNameOrId("CUSTOM");
			request.setParameter("selectedCoTypes", "CUSTOM");
			request.setParameter("reqGuid", reqObj.getGuid());
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Competitive execute
			target.setPartialColorNameOrId("101-5");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "101-5");
			request.setParameter("selectedCoTypes", "COMPET");
			request.setParameter("selectedCompany", "PPG");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
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
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testColorLookupAction() {
		ActionProxy proxy = getActionProxy("/colorLookupSearchAction");
		target = (ProcessColorAction) proxy.getAction();
		
		//Competitive lookup
		target.setReqGuid("123456789");
		target.setSelectedCompany("VYTEC");
		target.setSelectedCoTypes("COMPET");
		target.setColorID("VP30");
		target.setColorName("BUTTERCREAM");
		
		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
		
		try {
			String success = executeAction("/colorLookupSearchAction");
			assertNotNull(success);	
			
			//Nat. Acct. lookup
			target.setColorID("1631");
			target.setColorName("MIDNIGHT OIL");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("selectedCompany", "BEST BUY");
			request.setParameter("selectedCoTypes", "NAT");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorLookupSearchAction");
			assertNotNull(success);
			
			//SW Lookup
			target.setColorID("1015");
			target.setColorName("SKYLINE STEEL");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("locatorId", "283-C3");
			request.setParameter("selectedCoTypes", "SW");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorLookupSearchAction");
			assertNotNull(success);
			
			
			//Invalid Lookup
			target.setColorID("1631");
			target.setColorName("MIDNIGHT OIL");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("selectedCompany", "ALL");
			request.setParameter("selectedCoTypes", "INVALID");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorLookupSearchAction");
			assertNotNull(success);
						
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
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
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
}
