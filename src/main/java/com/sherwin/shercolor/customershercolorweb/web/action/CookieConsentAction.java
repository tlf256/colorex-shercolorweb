package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.stereotype.Component;

@Component
public class CookieConsentAction extends ActionSupport implements SessionAware, LoginRequired, ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(CookieConsentAction.class);
	private Map<String, Object> sessionMap;
	private boolean cookieBanner;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public String display() {
		try {
			logger.info("begin display...");
			
			setCookieBanner(getCookieConsent());
			
			logger.debug("display success");
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String execute() {
		try {
			logger.info("begin execute...");
			
			setCookieBanner(false);
			
			logger.debug("cookieBanner is " + cookieBanner);
			
			createConsentCookie();
			
			logger.debug("execute success");
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	private boolean getCookieConsent() {
		logger.info("getCookieConsent: getting request and checking for consent");
		request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
		Cookie[] cookies = null;
		boolean result = true;
		String cookieName = "";
		
		if(request != null) {
			logger.debug("getCookieConsent: request is not null");
			cookies = request.getCookies();
		}
		
		// verify if consent has already been given
		if(cookies != null) {
			logger.debug("getCookieConsent: cookies array is not null");
			for(Cookie cookie : cookies) {
				cookieName = cookie.getName();
				if(cookieName.contains("consent")) {
					result = false;
					break;
				}
			}
		}
		
		// drop any existing cookies if consent is needed 
		// except JSESSIONID since it is a technical cookie
		if(result && cookies != null) { 
			logger.info("getCookieConsent: consent is needed, dropping functional cookies");
			for(Cookie cookie : cookies) {
				cookieName = cookie.getName();
				if(!cookieName.contains("JSESSIONID")) {
					// maxAge of 0 deletes cookie
					cookie.setMaxAge(0);
				}
			}
		}
		
		logger.debug("getCookieConsent: returning " + result + " result");
		
		return result;
	}
	
	private void createConsentCookie() {
		logger.info("createConsentCookie: getting response and creating cookie");
		response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
		String cookieValue = ""; // does consent cookie need a value?
		
		Cookie cookie = new Cookie("consent", cookieValue);
		cookie.setHttpOnly(true);
		// expire cookie in one week
		cookie.setMaxAge(7*24*60*60);
		
		logger.debug("createConsentCookie: adding cookie to response");
		
		response.addCookie(cookie);
		
		logger.debug("end createConsentCookie");
	}
	
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);		
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public boolean isCookieBanner() {
		return cookieBanner;
	}

	public void setCookieBanner(boolean cookieBanner) {
		this.cookieBanner = cookieBanner;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
}
