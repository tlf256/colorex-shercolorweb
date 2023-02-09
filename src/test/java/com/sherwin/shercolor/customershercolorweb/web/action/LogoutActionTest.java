package com.sherwin.shercolor.customershercolorweb.web.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.dispatcher.SessionMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class LogoutActionTest extends StrutsSpringJUnit4TestCase<LogoutAction> {

	RequestObject reqObj = new RequestObject();
	String guid1 = "12345";
	
	@Test
	public void execute_ValidReqGuid_Success() throws Exception {

		request.setParameter("reqGuid",guid1);
		reqObj.setGuid(guid1);
		reqObj.setCustomerID(guid1);

		ActionProxy proxy = getActionProxy("/logout2Action");
        assertNotNull(proxy);

        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(guid1, reqObj);
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);

		String result = proxy.execute();
		assertEquals(Action.SUCCESS, result);
	}

}
