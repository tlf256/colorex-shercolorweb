package com.sherwin.shercolor.customershercolorweb.web.action;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.sherwin.shercolor.customershercolorweb.annotations.SherColorWebTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.StrutsSpringTestCase;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionProxy;


import com.sherwin.shercolor.customershercolorweb.web.action.TinterEventAction;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;


@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTest
public class TinterEventActionTest extends StrutsSpringJUnit4TestCase<TinterEventAction> {

	TinterEventAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";

	@Test
	public void testTinterEventsAction(){
		ActionProxy proxy = getActionProxy("/logTinterEventAction");
		TinterInfo tinter = new TinterInfo();
		Map<String,Object> tinterMessage = new HashMap<String,Object>();
		
		target = (TinterEventAction) proxy.getAction();

		target.setReqGuid("12345");



		reqObj.setCustomerID("TEST");

		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		tinter.setClrntSysId("CCE");
		tinter.setModel("FM UNITTEST");
		tinter.setSerialNbr("TESTFMSERIAL");
		reqObj.setTinter(tinter);
		
		
		
		tinterMessage.put("command", "TestTinter");
		tinterMessage.put("errorNumber", "88");
		tinterMessage.put("errorSeverity", "8");
		target.setTinterMessage(tinterMessage);

		try {
			String json = executeAction("/logTinterEventAction");
			
			ProcessTinterConfigAction result = new Gson().fromJson(json,ProcessTinterConfigAction.class);
			//String result = proxy.execute();
			assertEquals("12345",result.getReqGuid());

			
			//System.out.println("Got my Cal for " + cal.getId());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}	
		//String theResult = target.execute();
		//assertEquals("SUCCESS",theResult);
	}
	@Test
	public void testTinterEventsConfigAction(){
		ActionProxy proxy = getActionProxy("/logTinterEventConfigAction");
		TinterInfo tinter = new TinterInfo();
		Map<String,Object> tinterMessage = new HashMap<String,Object>();
		
		target = (TinterEventAction) proxy.getAction();

		target.setReqGuid("12345");



		reqObj.setCustomerID("TEST");

		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		tinter.setClrntSysId("CCE");
		tinter.setModel("COROB UNITTEST");
		tinter.setSerialNbr("TESTSERIAL");
		target.setNewTinter(tinter);
		
		
		tinterMessage.put("command", "TestConfig");
		tinterMessage.put("errorNumber", "99");
		tinterMessage.put("errorSeverity", "9");
		target.setTinterMessage(tinterMessage);

		

		try {
			String json = executeAction("/logTinterEventConfigAction");
			
			ProcessTinterConfigAction result = new Gson().fromJson(json,ProcessTinterConfigAction.class);
			//String result = proxy.execute();
			assertEquals("12345",result.getReqGuid());

			
			//System.out.println("Got my Cal for " + cal.getId());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}	
		
			
	}	


}
