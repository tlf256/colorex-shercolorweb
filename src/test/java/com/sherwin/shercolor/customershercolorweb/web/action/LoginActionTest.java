package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;

import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class LoginActionTest extends StrutsSpringJUnit4TestCase<LoginAction> {

	RequestObject reqObj = new RequestObject();
	String guid1 = "12345";


	
	@Test
	public void testLoginAction_success() {
		reqObj.setCustomerID("400000008");
		reqObj.setUserId("shercolortest");
		reqObj.setFirstName("SherColor");
		reqObj.setLastName("test");
		reqObj.setGuid(guid1);
		reqObj.setHomeStore(9989);
		reqObj.setTerritory("null");

		ActionProxy proxy = getActionProxy("/loginAction");
        assertNotNull(proxy);

		LoginAction target = (LoginAction) proxy.getAction();
        target.setGuid1(guid1);

        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(guid1, reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
		
		try {
			String result = proxy.execute();
			//System.out.println("loginAction result is " + result);
			assertEquals(Action.SUCCESS, result);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}

}
