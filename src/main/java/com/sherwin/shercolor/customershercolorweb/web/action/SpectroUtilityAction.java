package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.SpectroInfo;
import org.springframework.stereotype.Component;

@Component
public class SpectroUtilityAction extends ActionSupport  implements SessionAware, LoginRequired {

	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(SpectroUtilityAction.class.getName());
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private String spectroModel;
	private String spectroSerial;
	
	
	public String stampSession(){
		String retVal=null;
		
		try{
			logger.debug("inside SpectroUtilityAction stampSession. getting map for reqGuid = " + reqGuid);
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			logger.debug("inside SpectroUtilityAction stampSession. creating spectro info ");
			SpectroInfo theSpectro = new SpectroInfo();
			logger.debug("inside SpectroUtilityAction stampSession. setting Spectro as " + spectroModel + " " + spectroSerial);
			theSpectro.setModel(spectroModel);
			theSpectro.setSerialNbr(spectroSerial);
			reqObj.setSpectro(theSpectro);
			logger.debug("inside SpectroUtilityAction stampSession. putting map for reqGuid = " + reqGuid);
			sessionMap.put(reqGuid, reqObj);
			
			retVal = SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.toString() + " " + e.getMessage(), e);
			retVal = ERROR;
		}
		
		return retVal;
	}

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public String getSpectroModel() {
		return spectroModel;
	}

	public String getSpectroSerial() {
		return spectroSerial;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public void setSpectroModel(String spectroModel) {
		this.spectroModel = Encode.forHtml(spectroModel);
	}

	public void setSpectroSerial(String spectroSerial) {
		this.spectroSerial = Encode.forHtml(spectroSerial);
	}
	
}
