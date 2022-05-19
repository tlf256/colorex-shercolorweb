package com.sherwin.shercolor.customershercolorweb.web.action;
import javax.servlet.http.HttpSession;


import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.StrutsSpringTestCase;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


import com.opensymphony.xwork2.ActionProxy;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

import static org.junit.Assert.assertNotNull;


@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class PrinterConfigActionTest extends StrutsSpringJUnit4TestCase<PrinterConfigureAction> {

	PrinterConfigureAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";	


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
