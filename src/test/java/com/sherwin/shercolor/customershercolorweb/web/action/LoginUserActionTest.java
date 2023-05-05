package com.sherwin.shercolor.customershercolorweb.web.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class LoginUserActionTest extends StrutsSpringJUnit4TestCase<LoginUserAction> {

	RequestObject reqObj = new RequestObject();
	String guid1 = "12345";

	// Note: all methods in this test will not be able to actually log the user in because they do not have access to
	// the JBoss security module

	@Test
	public void execute_LoginExists_InputReturned() throws Exception {

		request.setParameter("userId","shercolortest");
		request.setParameter("userPass", "Spring2019#");

		ActionProxy proxy = getActionProxy("/loginUserAction");
        assertNotNull(proxy);

		String result = proxy.execute();
		assertEquals(Action.INPUT, result);
	}

	@Test
	public void execute_LoginDoesNotExist_InputReturnedAndFieldErrors() throws Exception {

		request.setParameter("userId","wackadoodle");

		ActionProxy proxy = getActionProxy("/loginUserAction");
		assertNotNull(proxy);
		LoginUserAction action = (LoginUserAction) proxy.getAction();

		String result = proxy.execute();
		assertThat(action.getFieldErrors()).isNotEmpty();
		assertThat(action.getFieldErrors().get("userId")).isNotEmpty();
		assertThat(action.getFieldErrors().get("userId").get(0)).contains("Login/password combination failed");
		assertEquals(Action.INPUT, result);
	}

}
