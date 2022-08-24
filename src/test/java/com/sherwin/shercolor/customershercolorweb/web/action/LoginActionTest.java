package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class LoginActionTest extends StrutsSpringJUnit4TestCase<LoginAction> {
	
	LoginAction target;
	RequestObject reqObj = new RequestObject();
	String guid1 = "12345";

	@Test
	public void testLoginAction() {
		reqObj.setCustomerID("400000008");
		reqObj.setUserId("shercolortest");
		reqObj.setFirstName("SherColor");
		reqObj.setLastName("test");
		reqObj.setGuid(guid1);
		reqObj.setHomeStore(9989);
		reqObj.setTerritory("null");
		
		request.setParameter("guid1",guid1);
		HttpSession session = request.getSession();
		session.setAttribute(guid1, reqObj);
		
		try {
			executeAction("/loginAction");
			
			String reqGuid = (String) findValueAfterExecute("reqGuid");
			//System.out.println("loginAction reqGuid is " + reqGuid);
			assertTrue(reqGuid != null);
			
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}

}
