package com.sherwin.shercolor.customershercolorweb.web.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.interceptor.I18nInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class UpdateLocaleActionTest extends StrutsSpringJUnit4TestCase<UpdateLocaleAction> {


	@Test
	public void execute_LocaleJunk_ErrorReturned() throws Exception {

		//arrange
		ActionProxy proxy = getActionProxy("/updateLocale.action");
		assertNotNull(proxy);

		Map<String, Object> sessionMap = new HashMap<>();
		sessionMap.put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, "lkjjsdlgjsd");
		proxy.getInvocation().getInvocationContext().setSession(sessionMap);

		//act
		String result = proxy.execute();

		//assert
		assertTrue(StringUtils.isNotBlank(result));
		assertEquals(Action.ERROR, result);
	}

	@Test
	public void execute_LocaleNotSupported_ErrorReturned() throws Exception {

		//arrange
		ActionProxy proxy = getActionProxy("/updateLocale.action");
		assertNotNull(proxy);

		Map<String, Object> sessionMap = new HashMap<>();
		sessionMap.put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, new Locale("es","ES"));
		proxy.getInvocation().getInvocationContext().setSession(sessionMap);

		//act
		String result = proxy.execute();

		//assert
		assertTrue(StringUtils.isNotBlank(result));
		assertEquals(Action.ERROR, result);
	}

	@Test
	public void execute_LocaleSupportedAndConsentExists_CookieCreatedProperly() throws Exception {

		//arrange
		request.setCookies(new Cookie("consent","true"));

		ActionProxy proxy = getActionProxy("/updateLocale.action");
		assertNotNull(proxy);

		Map<String, Object> sessionMap = new HashMap<>();
		sessionMap.put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, Locale.US);
		proxy.getInvocation().getInvocationContext().setSession(sessionMap);

		//act
		String result = proxy.execute();

		//assert
		assertTrue(StringUtils.isNotBlank(result));
		assertEquals(Action.SUCCESS, result);
		assertNotNull(response.getCookie("locale"));
		assertThat(response.getCookie("locale").getValue()).isNotBlank();
		assertSame(Locale.US.toLanguageTag(), response.getCookie("locale").getValue());
	}

	@Test
	public void checkLocaleCookie_LocaleSupported_LocaleCookiesAndSessionMapUpdated() throws Exception {

		//arrange
		request.setCookies(new Cookie("locale",Locale.US.toLanguageTag()));

		ActionProxy proxy = getActionProxy("/checkLocaleCookieAction.action");
		assertNotNull(proxy);

		//act
		String result = proxy.execute();
		executeAction("/checkLocaleCookieAction.action");

		//assert
		assertTrue(StringUtils.isNotBlank(result));
		assertEquals(Action.SUCCESS, result);
		assertSame(Locale.US, proxy.getInvocation().getInvocationContext().getSession().get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE));
	}
}
