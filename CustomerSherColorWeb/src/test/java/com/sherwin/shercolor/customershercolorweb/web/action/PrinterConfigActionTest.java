package com.sherwin.shercolor.customershercolorweb.web.action;
import javax.servlet.http.HttpSession;


import org.apache.struts2.StrutsSpringTestCase;

import org.junit.Test;

import org.springframework.transaction.annotation.Transactional;


import com.opensymphony.xwork2.ActionProxy;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;


@Transactional

public class PrinterConfigActionTest extends StrutsSpringTestCase{

	PrinterConfigureAction target;
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
	public void testPrinterConfigAction(){
		
		reqObj.setCustomerID("TEST");

		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);


		try {

			String result = executeAction("/printerConfigureAction");
			assertNotNull(result);


			System.out.println(result);


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

	@Test
	public void testPrinterSaveAction(){
		

		ActionProxy proxy = getActionProxy("/printerSaveAction");
		ActionProxy proxy2 = getActionProxy("/loginAction"); // this is how we handle the redirect after the save.


		PrinterConfigureAction target = (PrinterConfigureAction) proxy.getAction();
		target.setPrinterModel("TEST");
		target.setReqGuid("12345");
		reqObj.setCustomerID("TEST");

		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);


		try {

			String result = executeAction("/printerSaveAction");			
			assertNotNull(result);
		
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			//e.printStackTrace();
		}		
	}	

}
