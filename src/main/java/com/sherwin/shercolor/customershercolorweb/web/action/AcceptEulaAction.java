package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Calendar;
import java.util.Map;
import com.sherwin.shercolor.common.domain.EulaHist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.common.service.EulaService;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class AcceptEulaAction extends ActionSupport implements SessionAware {

	static Logger logger = LogManager.getLogger(AcceptEulaAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private int eulaSeqNbr;
	
	@Autowired 
	private EulaService target;

	@Override
	public String execute() {
		logger.info("in AcceptEulaActuionExecute");
		logger.info(Encode.forJava("reqGuid is " + reqGuid));
		logger.info(Encode.forJava("eulaSeqNbr is " + eulaSeqNbr));
		//The user has accepted the eula.  We need to post a record that the user has accepted the
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
			newEulaRec.setActionType("ACCEPT");
			newEulaRec.setActionUser(origReqObj.getUserId());
			newEulaRec.setCustomerId(origReqObj.getCustomerID());
			newEulaRec.setWebSite("CUSTOMERSHERCOLORWEB");
			newEulaRec.setSeqNbr(eulaSeqNbr);
			newEulaRec.setActionTimeStamp(calendar.getTime());
			logger.info("Completed construction of newEulaRec");
			if (target.createEulaHist(newEulaRec)) {
				//We also need to deactivate the activation record.
				if (target.disableActivationRecord("CUSTOMERSHERCOLORWEB", origReqObj.getCustomerID())) {
					return SUCCESS;
				} else {
					logger.error("Database Error: Could not disable activation record");
					return ERROR;
				}
			} else  {
				logger.error("create eula hist record unsuccessful");
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
	
	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
		
	}

	public int getEulaSeqNbr() {
		return eulaSeqNbr;
	}

	public void setEulaSeqNbr(int eulaSeqNbr) {
		this.eulaSeqNbr = eulaSeqNbr;
	}
	

}
