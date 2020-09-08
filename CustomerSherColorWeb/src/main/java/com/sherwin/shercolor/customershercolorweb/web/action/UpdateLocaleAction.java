package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Locale;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.util.domain.LocaleContainer;


public class UpdateLocaleAction extends ActionSupport implements SessionAware {
	
    private static final long serialVersionUID = 1L;
    static Logger logger = LogManager.getLogger(UpdateLocaleAction.class);
    private Map<String, Object> sessionMap;
    
    @Autowired
    LocaleContainer localeContainer;
    
   
    
    public String execute() {
    	Locale userLocale = (Locale) sessionMap.get("WW_TRANS_I18N_LOCALE");
    	// userLocale will be null if user has not requested a language change during this session
    	if (userLocale != null) {
    		localeContainer.setLocale(userLocale);
    	}
    	return SUCCESS;
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

}