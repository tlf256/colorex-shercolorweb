package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertNotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
	public void testDisplayActionNoSpectro() {
		ActionProxy proxy = getActionProxy("/searchSpectroEventLog");
		target = (SearchSpectroLogAction) proxy.getAction();
		
		target.setReqGuid("123456789");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/searchSpectroEventLog");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testDisplayActionWithSpectro() {
		ActionProxy proxy = getActionProxy("/searchSpectroEventLog");
		target = (SearchSpectroLogAction) proxy.getAction();
		
		target.setReqGuid("123456789");
		reqObj.setSpectro(new SpectroInfo());
		reqObj.getSpectro().setModel("Ci52+SWS");
		reqObj.getSpectro().setSerialNbr("123456");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/searchSpectroEventLog");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testLogLookupActionNoSpectro() {
		ActionProxy proxy = getActionProxy("/spectroLogLookupAction");
		target = (SearchSpectroLogAction) proxy.getAction();
		
		target.setReqGuid("123456789");
		target.setFromDate("01/01/2022");
		target.setToDate("01/31/2022");
		target.setSpectroCommand("-1");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/spectroLogLookupAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testLogLookupActionWithSpectro() {
		ActionProxy proxy = getActionProxy("/spectroLogLookupAction");
		target = (SearchSpectroLogAction) proxy.getAction();
		
		target.setReqGuid("123456789");
		reqObj.setSpectro(new SpectroInfo());
		reqObj.getSpectro().setModel("Ci52+SWS");
		reqObj.getSpectro().setSerialNbr("123456");
		target.setFromDate("");
		target.setToDate("");
		target.setSpectroCommand("CalibrateWhite");

		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);

		try {
			String success = executeAction("/spectroLogLookupAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
}
