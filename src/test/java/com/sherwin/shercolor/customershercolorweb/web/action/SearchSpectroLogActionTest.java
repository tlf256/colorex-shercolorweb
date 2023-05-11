package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.SpectroInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class SearchSpectroLogActionTest extends StrutsSpringJUnit4TestCase<SearchSpectroLogAction> {
	
	SearchSpectroLogAction target;
	RequestObject reqObj = new RequestObject();


	@Before
	public void setup() {
		reqObj.setGuid("123456789");
		reqObj.setCustomerID("LB6110");
		reqObj.setUserId("TEST");
		reqObj.setCustomerName("TST CUST");
		reqObj.setCustomerType("CUSTOMER");
	}
	

	@Test
	public void testDisplayActionNoSpectro() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/searchSpectroEventLog");
		target = (SearchSpectroLogAction) proxy.getAction();
		
		target.setReqGuid("123456789");
		request.setParameter("reqGuid", reqObj.getGuid());
	
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);		
		executeAction("/searchSpectroEventLog");

		String value = findValueAfterExecute("spectroCommands").toString();
		System.out.println("testDisplayActionNoSpectro value: " + value);
		assertTrue(value.contains("Detect"));
	}
	
	@Test
	public void testDisplayActionNoSpectroNewCustomer() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/searchSpectroEventLog");
		target = (SearchSpectroLogAction) proxy.getAction();
		
		reqObj.setCustomerID("NEWCUSTOMER");
		target.setReqGuid("123456789");
		request.setParameter("reqGuid", reqObj.getGuid());
	
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);		
		executeAction("/searchSpectroEventLog");

		String value = findValueAfterExecute("spectroCommands").toString();
		System.out.println("testDisplayActionNoSpectroNewCustomer value: " + value);
		assertTrue(value.contains("No Commands Available"));
	}
	
	@Test
	public void testDisplayActionWithSpectro() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/searchSpectroEventLog");
		target = (SearchSpectroLogAction) proxy.getAction();
		
		target.setReqGuid("123456789");
		reqObj.setSpectro(new SpectroInfo());
		reqObj.getSpectro().setModel("Ci52+SWS");
		reqObj.getSpectro().setSerialNbr("005970");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		executeAction("/searchSpectroEventLog");
		String value = findValueAfterExecute("spectroCommands").toString();
		System.out.println("testDisplayActionWithSpectro value: " + value);
		assertTrue(value.contains("ReadConfig"));
	}
	
	@Test
	public void testLogLookupActionNoSpectro() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/spectroLogLookupAction");
		target = (SearchSpectroLogAction) proxy.getAction();
		
		target.setReqGuid("123456789");
		target.setFromDate("12/01/2022");
		target.setToDate("12/31/2022");
		target.setSpectroCommand("-1");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		String success = executeAction("/spectroLogLookupAction");
		System.out.println("testLogLookupActionWithSpectro Success String: " + success);
		assertNotNull(success);
		assertTrue(success.contains("\"customerId\":\"LB6110\",\"deltaE\":0,\"errorCode\":0,\"errorMsg\":null,\"guid\":\"b38e4a0e-eb64-471b-a207-b69b1a9bc95a\",\"requestTime\":\"2022-12-08T11:44:20\",\"responseCode\":0,\"responseMsg\":null,\"responseTime\":\"2022-12-08T11:44:22\",\"spectroCommand\":\"ReadConfig\",\"spectroModel\":\"SWSimSpectro\",\"spectroPort\":\"USB\",\"spectroSerialNbr\":\"simunit1\",\"targetSizeMm\":0"));
	}
	
	@Test
	public void testLogLookupActionWithSpectro() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/spectroLogLookupAction");
		target = (SearchSpectroLogAction) proxy.getAction();
		
		target.setReqGuid("123456789");
		reqObj.setSpectro(new SpectroInfo());
		reqObj.getSpectro().setModel("Ci52+SWS");
		reqObj.getSpectro().setSerialNbr("005970");
		target.setFromDate("");
		target.setToDate("");
		target.setSpectroCommand("ReadConfig");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		String success = executeAction("/spectroLogLookupAction");
		System.out.println("testLogLookupActionWithSpectro Success String: " + success);
		assertNotNull(success);
		assertTrue(success.contains("\"customerId\":\"LB6110\",\"deltaE\":0,\"errorCode\":0,\"errorMsg\":null,\"guid\":\"0b6049cc-cc03-4bc6-9882-74a8cb67c9f7\",\"requestTime\":\"2022-10-26T11:25:45\",\"responseCode\":0,\"responseMsg\":null,\"responseTime\":\"2022-10-26T11:25:46\",\"spectroCommand\":\"ReadConfig\",\"spectroModel\":\"Ci52+SWS\",\"spectroPort\":\"USB\",\"spectroSerialNbr\":\"005970\",\"targetSizeMm\":0"));
	}
}
