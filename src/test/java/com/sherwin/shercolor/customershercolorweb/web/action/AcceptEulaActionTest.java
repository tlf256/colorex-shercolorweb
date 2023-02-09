package com.sherwin.shercolor.customershercolorweb.web.action;

import com.google.gson.Gson;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class AcceptEulaActionTest extends StrutsSpringJUnit4TestCase<AcceptEulaAction> {

	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";

	@Test
	public void execute_ReqObjNull_LoginReturned() throws Exception {

		//arrange
		ActionProxy proxy = getActionProxy("/acceptEulaAction.action");
		assertNotNull(proxy);

		AcceptEulaAction target = (AcceptEulaAction) proxy.getAction();
		assertNotNull(target);

	 	target.setReqGuid(reqGuid);
		int eulaSeqNbr = 1;
		target.setEulaSeqNbr(eulaSeqNbr);

		//act
		String result = proxy.execute();

		//assert
		assertTrue(StringUtils.isNotBlank(result));
		assertEquals(Action.LOGIN, result);
	}
}
