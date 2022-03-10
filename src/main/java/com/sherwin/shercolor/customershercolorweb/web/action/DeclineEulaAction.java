package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Calendar;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.EulaHist;
import com.sherwin.shercolor.common.service.EulaService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.springframework.stereotype.Component;

@Component
public class DeclineEulaAction extends ActionSupport implements SessionAware, LoginRequired {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1066954050143612577L;
	static Logger logger = LogManager.getLogger(DeclineEulaAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private int eulaSeqNbr;
	
	@Autowired 
	private EulaService target;
	
	public String execute() {
		logger.info("in DeclineEulaActuionExecute");
		logger.info("reqGuid is " + reqGuid);
		logger.info("eulaSeqNbr is " + eulaSeqNbr);
		//The user has DECLINED the eula.  We need to post a record that the user has declineD the
		//displayed eula.

		try {

			 RequestObject origReqObj = (RequestObject) sessionMap.get(reqGuid);
			 if (origReqObj==null) {
				 logger.info("DEBUG origReqObj is null - probably a session timeout");
				 //loMessage = "Your session has expired.";
				 return LOGIN;
			 }
			 
			Calendar calendar = Calendar.getInstance();
			EulaHist newEulaRec = new EulaHist();
			newEulaRec.setActionType("DECLINE");
			newEulaRec.setActionUser(origReqObj.getUserId());
			newEulaRec.setCustomerId(origReqObj.getCustomerID());
			newEulaRec.setWebSite("CUSTOMERSHERCOLORWEB");
			newEulaRec.setSeqNbr(eulaSeqNbr);
			newEulaRec.setActionTimeStamp(calendar.getTime());
			
			if (target.createEulaHist(newEulaRec)) {
				return SUCCESS;
			} else  {
				logger.error("Database Error: Could not create EulaHist record");
				return ERROR;
			}
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
  
	}
	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}
	
	public int getEulaSeqNbr() {
		return eulaSeqNbr;
	}

	public void setEulaSeqNbr(int eulaSeqNbr) {
		this.eulaSeqNbr = eulaSeqNbr;
	}
	
	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
		
	}
}
