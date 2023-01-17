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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class CookieConsentActionTest extends StrutsSpringJUnit4TestCase<CookieConsentAction> {

	@Test
	public void display_NoInputs_SuccessReturnedAndCookieBannerTrue() throws Exception {

		//arrange
		ActionProxy proxy = getActionProxy("/displayCookieBanner.action");
		assertNotNull(proxy);

		CookieConsentAction target = (CookieConsentAction) proxy.getAction();
		assertNotNull(target);

		//act
		String result = proxy.execute();
		// Needed to use findValueAfterExecute()
		executeAction("/displayCookieBanner.action");

		//assert
		boolean cookieBanner = (boolean) findValueAfterExecute("cookieBanner");
		assertTrue(cookieBanner);
		assertTrue(StringUtils.isNotBlank(result));
		assertEquals(Action.SUCCESS, result);
	}
}
