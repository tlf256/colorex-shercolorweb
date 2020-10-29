package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Locale;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.util.domain.LocaleContainer;


public class UpdateLocaleAction extends ActionSupport implements SessionAware, ServletResponseAware, ServletRequestAware {
	
    private static final long serialVersionUID = 1L;
    static Logger logger = LogManager.getLogger(UpdateLocaleAction.class);
    private Map<String, Object> sessionMap;
    private HttpServletResponse response;
    private HttpServletRequest request;
	
    @Autowired
    LocaleContainer localeContainer;
	
   
    
    public String execute() {
    	try {
	    	Locale userLocale = (Locale) sessionMap.get("WW_TRANS_I18N_LOCALE");
	    	// userLocale will be null if user has not requested a language change during this session
	    	if (userLocale != null) {
	    		localeContainer.setLocale(userLocale);
	    		
	    		// convert locale to language tag so it can be easily converted back later
	    		String localeId = userLocale.toLanguageTag();
	    		Cookie localeCookie = new Cookie("locale", localeId);
	    		localeCookie.setMaxAge(60 * 60 * 24 * 365 * 10);
	    		// httponly helps to prevent XSS attacks
	    		localeCookie.setHttpOnly(true);
	    		response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	    		response.addCookie(localeCookie);
	    	}
	    	
	    	return SUCCESS;
    	
    	} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			return ERROR;
		}
    }
    
    
    public String checkLocaleCookie() {
    	try {
	    	String cookieLocale = null;
			request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
			if(request != null && request.getCookies() != null) {
				for (Cookie c : request.getCookies()) {
		            if (c.getName().equals("locale")) {
		                cookieLocale = c.getValue();
		            }
		        }
			}
			if (cookieLocale != null) {
				Locale userLocale = Locale.forLanguageTag(cookieLocale);
				if (userLocale != null) {
					localeContainer.setLocale(userLocale);
					sessionMap.put("WW_TRANS_I18N_LOCALE", userLocale);
				}
			} 	
	    	
	    	return SUCCESS;
    	
    	} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			return ERROR;
		}
    }
    
    
    
    @Override
    public void setSession(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }

    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
    

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}