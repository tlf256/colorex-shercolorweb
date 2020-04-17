package com.sherwin.shercolor.customershercolorweb.web.action;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringTestCase;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class LookupJobActionTest extends StrutsSpringTestCase {

	LookupJobAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";
	
	@Override
	public String[] getContextLocations() {
		String[] arrStr =  {
				"classpath:config/spring/shercolorcommon.xml"

		} ;  
		return arrStr;
	}
	
	
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
}
