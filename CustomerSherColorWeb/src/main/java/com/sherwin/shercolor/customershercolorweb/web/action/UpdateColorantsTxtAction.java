package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsClrnt;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.service.ColorantService;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterCanister;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;


public class UpdateColorantsTxtAction extends ActionSupport  implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;

	@Autowired
	TinterService tinterService;
	
	@Autowired
	ColorantService colorantService;

	static Logger logger = LoggerFactory.getLogger(UpdateColorantsTxtAction.class.getName());
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private String colorantSystem; 
	private String tinterModel; 
	private String tinterSerial;
	private ArrayList<CustWebColorantsTxt> colorantsTxtList;
	private ArrayList<TinterCanister> tinterCanisterList;
	
	
	public String execute(){
		String retVal = null;
		String customerId = null;
		tinterCanisterList = new ArrayList<TinterCanister>();
		Map<String, String> rgbHexList = buildRgbHexList();
		
		// get data from session
		if(sessionMap != null) {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			if(reqObj != null) {
				customerId = reqObj.getCustomerID();
			}
		}
		List<CdsClrnt> colorantList = colorantService.getColorantList(colorantSystem);

		try{
			// grab existing records 
			List<CustWebColorantsTxt> currentRecords = tinterService.getCanisterList(customerId, colorantSystem, tinterModel, tinterSerial);
			
			// construct updated colorantsTxt list
			for (CustWebColorantsTxt colorantsTxt : colorantsTxtList) {
				colorantsTxt.setCustomerId(customerId);
				colorantsTxt.setClrntSysId(colorantSystem);
				colorantsTxt.setTinterModel(tinterModel);
				colorantsTxt.setTinterSerialNbr(tinterSerial);
				colorantsTxt.setFillStopLevel(1.2);
				colorantsTxt.setCurrentClrntAmount(colorantsTxt.getMaxCanisterFill());
				if (colorantsTxt.getMaxCanisterFill() != null) {
					if (colorantsTxt.getMaxCanisterFill() <= 192) {
						colorantsTxt.setFillAlarmLevel(32.0);
					} else {
						colorantsTxt.setFillAlarmLevel(64.0);
					}
				}
				
				// fill out tinter canister list to return for the canister layout modal
				TinterCanister canister = new TinterCanister();
				canister.setClrntCode(colorantsTxt.getClrntCode());
				canister.setCurrentClrntAmount(colorantsTxt.getCurrentClrntAmount());
				canister.setMaxCanisterFill(colorantsTxt.getMaxCanisterFill());
				canister.setPosition(colorantsTxt.getPosition());
				canister.setRgbHex(rgbHexList.get(colorantsTxt.getClrntCode()));
				canister.setFillStopLevel(colorantsTxt.getFillStopLevel());
				canister.setFillAlarmLevel(colorantsTxt.getFillAlarmLevel());
				for (CdsClrnt colorant : colorantList) {
					if(colorant.getTintSysId().equals(colorantsTxt.getClrntCode())) {
						canister.setClrntName(colorant.getName());
						break;
					}
				}
				tinterCanisterList.add(canister);	
			}
			
			// replace canister layout
			boolean result = tinterService.deleteColorantsTxt(currentRecords);
			if (result) {
				result = tinterService.saveColorantsTxt(colorantsTxtList);
			}
			if (result) {
				// update tinter info in session 
				if(sessionMap != null) {
					RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
					if(reqObj != null) {
						TinterInfo tinter = reqObj.getTinter();
						if (tinter != null && tinter.getCanisterList() != null) {
							tinter.setCanisterList(tinterCanisterList);
						}
					}
				}
				retVal = SUCCESS;
			} else {
				logger.error("Database Error: ColorantsTxt could not be updated");
				retVal = ERROR;
			}
				
		} catch (RuntimeException e) {
			logger.error(e.toString() + " " + e.getMessage(), e);
			retVal = ERROR;
		}
		return retVal;
	}
	
	
	private Map<String, String> buildRgbHexList() {
		Map<String, String> rgbHexList = new HashMap<String, String>();
		rgbHexList.put("B1", "#000000");
		rgbHexList.put("LB", "#000000");
		rgbHexList.put("BLK", "#000000");
		rgbHexList.put("G2", "#009933");
		rgbHexList.put("PG", "#009933");
		rgbHexList.put("TGR", "#009933");
		rgbHexList.put("L1", "#0000ff");
		rgbHexList.put("PB", "#0000ff");
		rgbHexList.put("TBL", "#0000ff");
		rgbHexList.put("N1", "#996633");
		rgbHexList.put("BU", "#996633");
		rgbHexList.put("R2", "#990000");
		rgbHexList.put("RO", "#990000");
		rgbHexList.put("ROX", "#990000");
		rgbHexList.put("R3", "#ff3399");
		rgbHexList.put("MAG", "#ff3399");
		rgbHexList.put("QV", "#ff3399");
		rgbHexList.put("R4", "#ff0000");
		rgbHexList.put("QR", "#ff0000");
		rgbHexList.put("RED", "#ff0000");
		rgbHexList.put("Y1", "#ffff00");
		rgbHexList.put("OY", "#ffff00");
		rgbHexList.put("Y3", "#ffcc00");
		rgbHexList.put("YO", "#ffcc00");
		rgbHexList.put("YOX", "#ffcc00");
		rgbHexList.put("W1", "#ffffff");
		rgbHexList.put("TW", "#ffffff");
		rgbHexList.put("WHT", "#ffffff");
		rgbHexList.put("MY", "#ffcc00");
		rgbHexList.put("UO", "#ff8000");
		rgbHexList.put("YRS", "#ff8000");
		rgbHexList.put("YGS", "#dfff00");
		rgbHexList.put("ORN", "#c54e1b");
		
		return rgbHexList;
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
	
	public String getColorantSystem() {
		return colorantSystem;
	}

	public void setColorantSystem(String colorantSystem) {
		this.colorantSystem = colorantSystem;
	}
	
	public String getTinterModel() {
		return tinterModel;
	}

	public void setTinterModel(String tinterModel) {
		this.tinterModel = tinterModel;
	}
	
	public String getTinterSerial() {
		return tinterSerial;
	}

	public void setTinterSerial(String tinterSerial) {
		this.tinterSerial = tinterSerial;
	}

	public ArrayList<CustWebColorantsTxt> getColorantsTxtList() {
		return colorantsTxtList;
	}

	public void setColorantsTxtList(ArrayList<CustWebColorantsTxt> colorantsTxtList) {
		this.colorantsTxtList = colorantsTxtList;
	}

	public ArrayList<TinterCanister> getTinterCanisterList() {
		return tinterCanisterList;
	}

	public void setTinterCanisterList(ArrayList<TinterCanister> tinterCanisterList) {
		this.tinterCanisterList = tinterCanisterList;
	}

}