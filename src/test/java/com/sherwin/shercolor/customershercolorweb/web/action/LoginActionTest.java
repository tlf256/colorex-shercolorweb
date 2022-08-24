package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class LoginActionTest extends StrutsSpringJUnit4TestCase<LoginAction> {
	
	LoginAction target;
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
        
        target = (LoginAction) proxy.getAction();
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
