package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class ProcessVinylSafeAction  extends ActionSupport implements SessionAware, LoginRequired  {

	private Map<String, Object> sessionMap;
	
	@Autowired
	ProductService productService;
	
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessVinylSafeAction.class);
	
	private String reqGuid;
	private boolean makeVinylSafeFormula;
	
	public String display() {
		 try {
			 //check and see if we even need to display this screen.
			 RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			 CdsProdCharzd charzdRec = productService.readCdsProdCharzd(reqObj.getProdNbr(),reqObj.getClrntSys());
			 if (charzdRec==null) {
					reqObj.setVinylExclude(false);
					sessionMap.put(reqGuid, reqObj);
				 return SUCCESS;
			 }
			 //if we made it here, we have a charzdRec on file. Check the vinyl field - if not null, we need to prompt for the screen.
			 if (charzdRec.getVinyl_excludeClrnt()==null) {
				reqObj.setVinylExclude(false);
				sessionMap.put(reqGuid, reqObj);
				 return SUCCESS;
			 }
			 
			 //check to see if the color uses a P series primer other than P1.  If so, it should not be allowed
			 //to be vinyl sided.
			 if (reqObj.getPrimerId()!=null && !reqObj.getPrimerId().equals("P1")) {
					reqObj.setVinylExclude(false);
					sessionMap.put(reqGuid, reqObj);
					 return SUCCESS;
			 }
			 
			 //check to see if the color is a "vinyl only" color.  If so, we can set the VinylExclude flag to true and
			 //skip this screen.
			 if (reqObj.isColorVinylOnly()) {
					reqObj.setVinylExclude(true);
					sessionMap.put(reqGuid, reqObj);
					 return SUCCESS;
			 }
			 
			 return INPUT;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String execute() {
		 try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setVinylExclude(makeVinylSafeFormula);
			sessionMap.put(reqGuid, reqObj);
			return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	// User hit the backup button on the vinyl Safe page
	public String backItUp() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setClrntSys("");
			sessionMap.put(reqGuid, reqObj);
		    return SUCCESS;
		} catch (RuntimeException e) {
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

	public boolean isMakeVinylSafeFormula() {
		return makeVinylSafeFormula;
	}

	public void setMakeVinylSafeFormula(boolean makeVinylSafeFormula) {
		this.makeVinylSafeFormula = makeVinylSafeFormula;
	}
}
