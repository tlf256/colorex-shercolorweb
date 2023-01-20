package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;


@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class CompareColorsActionTest extends StrutsSpringJUnit4TestCase<CompareColorsAction> {
	
	CompareColorsAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";

	@Test
	public void testCompareColors() {
		reqObj.setCustomerID("TEST");
		
		request.setParameter("reqGuid",reqGuid);
		
		//actionproxy is being used for this particular test because
		//the success result is a redirectAction. executeAction("/action")
		//will return a 302 error and throw a null pointer exception
		ActionProxy proxy = getActionProxy("/spectroCompareColorsAction");
        assertNotNull(proxy);
        
        //this isn't necessary since none of the action properties need set
        //but will add it for reference
        target = (CompareColorsAction) proxy.getAction();

        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        //this is necessary for keeping login interceptor happy!
        //setting the request object in the http session does not work
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			//System.out.println("CompareColorsAction proxy.execute result: " + result);
			
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
