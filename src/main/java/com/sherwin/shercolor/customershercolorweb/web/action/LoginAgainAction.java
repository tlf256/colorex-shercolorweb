package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginAgainAction extends ActionSupport  implements SessionAware  {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> sessionMap;
	static Logger logger = LogManager.getLogger(LoginAgainAction.class);
	private String reqGuid;
	@Value("${sherlink.login.url}")
	private String sherLinkURL;
	private String loMessage;
	private String isAJAX = "false";
	private String sessionStatus;


	
	public String loginagain() {
		try {

			//logger.error("in loginagain, sherLinkURL is " + sherLinkURL);
			 return SUCCESS;
		     
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String invalidLogin() {

		RequestObject reqObj = null;
		try {
			//logger.error("in loginagain, sherLinkURL is " + sherLinkURL);
			//logger.error("in loginagain, getting testsession");
			Map<String, Object> testsession = ActionContext.getContext().getSession();
			//logger.error("in loginagain, got testsession, checking if it's null");
			//06-13-2017 - BKP - test for a valid session.  If it's null (i.e. someone coming in using a deep link)
			//we'll treat them as logged out.
			if (testsession != null) {
				if (reqGuid != null) {
					reqObj = (RequestObject) testsession.get(reqGuid);
					//logger.error("assigned reqObj as reqGuid was not null");
				} else {
					//logger.error("reqGuid is null");
				}
			} else {
				reqObj = null;
			}
			if (reqObj == null) {
				 loMessage = getText("global.yourSessionHasExpired");//"Your session has expired.";
			} else {
				 loMessage = getText("global.youHaveBeenLoggedOut");
			}
			
			//Check for AJAX
			if(isAJAX.equals("true") && !isAJAX.isEmpty()){
				sessionStatus = "expired";
				return "ajax";
			}
			else return SUCCESS;
		     
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	

	
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}


	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}



	public String getSherLinkURL() {
		return sherLinkURL;
	}



	public void setSherLinkURL(String sherLinkURL) {
		this.sherLinkURL = Encode.forHtml(sherLinkURL);
	}
	
	public String getLoMessage() {
		return loMessage;
	}
	
	public void setLoMessage(String loMessage) {
		this.loMessage = Encode.forHtml(loMessage);
	}

	public void setIsAJAX(String isAJAX) {
		this.isAJAX = Encode.forHtml(isAJAX);
	}

	public String getSessionStatus() {
		return sessionStatus;
	}

	
}
