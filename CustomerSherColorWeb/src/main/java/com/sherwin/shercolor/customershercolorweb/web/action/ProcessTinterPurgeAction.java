package com.sherwin.shercolor.customershercolorweb.web.action;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebTinterEvents;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterCanister;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;

public class ProcessTinterPurgeAction extends ActionSupport implements SessionAware, LoginRequired  {
	private Map<String, Object> sessionMap;
	
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessTinterPurgeAction.class);
	private String reqGuid;
	private boolean autoNozzleCover;
	private String currentUser;
	private String lastPurgeDate;
	private String lastPurgeUser;
	
	private TinterInfo tinter;
	private List<TinterCanister> canList;
	
	@Autowired
	TinterService tinterService;

	public String display(){
		String retVal = null;
		
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			TinterInfo tinter = reqObj.getTinter();
			setCanList(tinter.getCanisterList()); // fill it up!
			//already there from stamp tinter session... tinter.setAutoNozzleCover(tinterService.hasAutoNozzleCover(reqObj.getCustomerID(), tinter.getClrntSysId(), tinter.getModel(), tinter.getSerialNbr()));
			
			CustWebTinterEvents lastPurge = tinterService.getLastPurgeDateAndUser(reqObj.getCustomerID(), tinter.getClrntSysId(), tinter.getModel(), tinter.getSerialNbr());
			if(lastPurge!=null) {
				//SimpleDateFormat df = new SimpleDateFormat("E MMM dd yyyy 'at' hh.mm.ss a zzz");
				SimpleDateFormat jsdf = new SimpleDateFormat("EE MMM d y H:m:s 'GMT'Z (zz)");
				tinter.setLastPurgeDate(jsdf.format(lastPurge.getDateTime()));
				tinter.setLastPurgeUser(lastPurge.getEventDetails());
//				SimpleDateFormat df = new SimpleDateFormat("E mon dd yyyy 'at' hh.mm.ss a");
				System.out.println("About to format date" + lastPurge.getDateTime().toString());
				SimpleDateFormat dfDisplay = new SimpleDateFormat("E MMM dd yyyy 'at' hh:mm a");
				lastPurgeDate = dfDisplay.format(lastPurge.getDateTime());
				lastPurgeUser = tinter.getLastPurgeUser();
			} else {
				lastPurgeDate = "Not Available";
				lastPurgeUser = "";
			}
			
			
			autoNozzleCover = tinter.isAutoNozzleCover();
			currentUser = reqObj.getFirstName() + " " + reqObj.getLastName();
			//TODO get last purge date info to be shown on screen...
			
			retVal = SUCCESS;

		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			e.printStackTrace();
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

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public boolean isAutoNozzleCover() {
		return autoNozzleCover;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public String getLastPurgeDate() {
		return lastPurgeDate;
	}

	public String getLastPurgeUser() {
		return lastPurgeUser;
	}

	public TinterInfo getTinter() {
		return tinter;
	}

	public List<TinterCanister> getCanList() {
		return canList;
	}

	public void setCanList(List<TinterCanister> canList) {
		this.canList = canList;
	}



}
