package com.sherwin.shercolor.customershercolorweb.web.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class DownloadPdfActionTest extends StrutsSpringJUnit4TestCase<DownloadPdfAction> {

	RequestObject reqObj = new RequestObject();
	private static final String reqGuid = "12345";
	private static final String customerId = "678910";


	@Test
	public void execute_NoInputsUnhandledPdfType_ErrorReturned() {

		//arrange
		// get action proxy and action object
		ActionProxy proxy = getActionProxy("/downloadPdfAction.action");
		assertNotNull(proxy);
		DownloadPdfAction action = (DownloadPdfAction) proxy.getAction();
		assertNotNull(action);

		// set sessionMap into the action's session
		Map<String, Object> sessionMap = new HashMap<>();
		reqObj.setCustomerID(customerId);
		sessionMap.put(reqGuid, reqObj);
		action.setSession(sessionMap);
		action.setReqGuid(reqGuid);
		action.setPdfType("woah");

		//act
		String result = action.execute();

		//assert
		assertTrue(StringUtils.isNotBlank(result));
		assertEquals(Action.ERROR, result);
	}
}
