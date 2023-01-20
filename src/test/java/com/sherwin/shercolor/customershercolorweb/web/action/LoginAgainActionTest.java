package com.sherwin.shercolor.customershercolorweb.web.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class LoginAgainActionTest extends StrutsSpringJUnit4TestCase<LoginAgainAction> {

	RequestObject reqObj = new RequestObject();
	String guid1 = "12345";


	
	@Test
	public void invalidLogin_PropsLoadedFromClasspath_Success() throws Exception {
		reqObj.setCustomerID("400000008");
		reqObj.setGuid(guid1);

		ActionProxy proxy = getActionProxy("/invalidLoginAction");
        assertNotNull(proxy);

		LoginAgainAction target = (LoginAgainAction) proxy.getAction();
        target.setReqGuid(guid1);

        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(guid1, reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);

		String result = proxy.execute();
		//System.out.println("loginAction result is " + result);
		assertEquals(Action.SUCCESS, result);
	}

}
