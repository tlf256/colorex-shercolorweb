package com.sherwin.shercolor.customershercolorweb.web.action;

import javax.servlet.http.HttpSession;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import com.google.gson.Gson;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class LookupJobActionTest extends StrutsSpringJUnit4TestCase<LookupJobAction> {

	LookupJobAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";

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
