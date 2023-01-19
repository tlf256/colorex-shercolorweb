package com.sherwin.shercolor.customershercolorweb.web.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class DownloadExeActionTest extends StrutsSpringJUnit4TestCase<DownloadExeAction> {


	RequestObject reqObj = new RequestObject();
	private static final String reqGuid = "12345";
	private static final String customerId = "678910";

	@Test
	public void execute_NoInputs_ProperExecutableReturned() throws Exception {

		//arrange
		request.setParameter("reqGuid",reqGuid);
		reqObj.setCustomerID(customerId);
		Objects.requireNonNull(request.getSession()).setAttribute(reqGuid, reqObj);

		//act
		String json = executeAction("/downloadExeAction.action");

		//assert
		assertTrue(StringUtils.isNotBlank(json));
		assertEquals(response.getContentType(), ContentType.APPLICATION_OCTET_STREAM.getMimeType());
		String contentDisposition = response.getHeader("Content-Disposition");
		assertTrue(StringUtils.isNotBlank(contentDisposition));
		assertTrue(StringUtils.contains(contentDisposition,"SWDHSetup"));
		assertTrue(StringUtils.contains(contentDisposition,".exe"));
	}
}
