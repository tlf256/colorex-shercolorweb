package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterCanister;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;

@SuppressWarnings("serial")
public class ProcessColorantLevelsAction extends ActionSupport implements SessionAware, LoginRequired{

	static Logger logger = LogManager.getLogger(ProcessColorantLevelsAction.class);
	private String reqGuid;
	private Map<String, Object> sessionMap;
	private TinterInfo tinter;
	private String selectedRowPosition;
	private String updateType;
	
	@Autowired
	TinterService tinterService;
	
	public String display(){
		try {
			if(!StringUtils.isEmpty(reqGuid)) {
				RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
				tinter = reqObj.getTinter();
			}
			else logger.error("reqGuid is empty");
		}
		catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage() + e.getCause(), e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	public String update() {
		try {
			logger.debug("updateType is " + getUpdateType() + " at beginning of method.");
			//Setting up colorList object
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			tinter = reqObj.getTinter();
			List<CustWebColorantsTxt> custWebColorantsTxtList = new ArrayList<CustWebColorantsTxt>();
			//Testing to see if both lists are acceptable
			
			if (custWebColorantsTxtList.isEmpty() && !tinter.getCanisterList().isEmpty()) {
				logger.debug("passed condition");
				for(TinterCanister canister : tinter.getCanisterList()) {
					
					switch (getUpdateType()) {
					case "setAllFull":
						logger.debug("In setAllFull");
						logger.debug("canister amount = " + canister.getCurrentClrntAmount());
						
						//Testing to see if canister object amount < max amount
						
						if(canister.getCurrentClrntAmount() < canister.getMaxCanisterFill()) {
							logger.debug("in loop");
							CustWebColorantsTxt temp = new CustWebColorantsTxt();
							temp.setCustomerId(reqObj.getCustomerID());
							temp.setClrntCode(canister.getClrntCode());
							temp.setClrntSysId(tinter.getClrntSysId());
							temp.setTinterModel(tinter.getModel());
							temp.setTinterSerialNbr(tinter.getSerialNbr());
							temp.setPosition(canister.getPosition());
							temp.setFillAlarmLevel(canister.getFillAlarmLevel());
							temp.setMaxCanisterFill(canister.getMaxCanisterFill());
							canister.setCurrentClrntAmount(canister.getMaxCanisterFill());
							temp.setCurrentClrntAmount(canister.getMaxCanisterFill());
							temp.setFillStopLevel(canister.getFillStopLevel());
							custWebColorantsTxtList.add(temp);
							tinterService.updateColorantLevels(custWebColorantsTxtList);
						}
						else logger.debug("Canister object current amount full or empty.");
						break;
						
					case "setfl":
						logger.debug("In setOneFull");
						logger.debug("canister amount = " + canister.getCurrentClrntAmount());
						logger.debug("selectedRowPosition is " + selectedRowPosition);
						//Testing to see when on the correct row according to key and canister values are acceptable for subtraction
						
						if(canister.getCurrentClrntAmount() <= canister.getMaxCanisterFill() && canister.getCurrentClrntAmount() >= 0){
							
							//Using the key to assure update of ONLY the selected canister/colorant.
							
							if(Integer.parseInt(selectedRowPosition) == canister.getPosition()) {
								logger.debug("in loop");
								CustWebColorantsTxt temp = new CustWebColorantsTxt();
								temp.setCustomerId(reqObj.getCustomerID());
								temp.setClrntCode(canister.getClrntCode());
								temp.setClrntSysId(tinter.getClrntSysId());
								temp.setTinterModel(tinter.getModel());
								temp.setTinterSerialNbr(tinter.getSerialNbr());
								temp.setPosition(canister.getPosition());
								temp.setFillAlarmLevel(canister.getFillAlarmLevel());
								temp.setMaxCanisterFill(canister.getMaxCanisterFill());
								canister.setCurrentClrntAmount(canister.getMaxCanisterFill());
								temp.setCurrentClrntAmount(canister.getCurrentClrntAmount());
								temp.setFillStopLevel(canister.getFillStopLevel());
								custWebColorantsTxtList.add(temp);
								tinterService.updateColorantLevels(custWebColorantsTxtList);
							}
							else logger.error("Canister does not match the key.");
						}
						else logger.debug("Canister object current amount full or empty.");
						break;
						
					case "addqt":
						logger.debug("In addQuart");
						logger.debug("canister amount = " + canister.getCurrentClrntAmount());
						logger.debug("selectedRowPosition is " + selectedRowPosition);
						//Testing to see when on the correct row according to key and canister values are acceptable for addition
						
						if(Integer.parseInt(selectedRowPosition) == canister.getPosition() && canister.getCurrentClrntAmount() <= canister.getMaxCanisterFill() && canister.getCurrentClrntAmount() >= 0){
							if((canister.getCurrentClrntAmount() + 32.0d) <= canister.getMaxCanisterFill()){
								logger.debug("in loop");
								CustWebColorantsTxt temp = new CustWebColorantsTxt();
								temp.setCustomerId(reqObj.getCustomerID());
								temp.setClrntCode(canister.getClrntCode());
								temp.setClrntSysId(tinter.getClrntSysId());
								temp.setTinterModel(tinter.getModel());
								temp.setTinterSerialNbr(tinter.getSerialNbr());
								temp.setPosition(canister.getPosition());
								temp.setFillAlarmLevel(canister.getFillAlarmLevel());
								temp.setMaxCanisterFill(canister.getMaxCanisterFill());
								canister.setCurrentClrntAmount(canister.getCurrentClrntAmount() + 32.0d);
								temp.setCurrentClrntAmount(canister.getCurrentClrntAmount());
								temp.setFillStopLevel(canister.getFillStopLevel());
								custWebColorantsTxtList.add(temp);
								tinterService.updateColorantLevels(custWebColorantsTxtList);
							}
							else logger.error("Incrementing by this amount will result in a current colorant amount that exceeds the Max Fill amount.");
						}
						else logger.debug("Canister object current amount full or empty.");
						break;
						
					case "subqt":
						logger.debug("In subQuart");
						logger.debug("canister = " + canister.getClrntCode() + " amount = " + canister.getCurrentClrntAmount());
						logger.debug("selectedRowPosition is " + selectedRowPosition);
						//Testing to see when on the correct row according to key and canister values are acceptable for subtraction
						
						if(Integer.parseInt(selectedRowPosition) == canister.getPosition() && canister.getCurrentClrntAmount() <= canister.getMaxCanisterFill() && canister.getCurrentClrntAmount() >= 0){
							if((canister.getCurrentClrntAmount() - 32.0d) >= 0){
								logger.debug("in loop");
								CustWebColorantsTxt temp = new CustWebColorantsTxt();
								temp.setCustomerId(reqObj.getCustomerID());
								temp.setClrntCode(canister.getClrntCode());
								temp.setClrntSysId(tinter.getClrntSysId());
								temp.setTinterModel(tinter.getModel());
								temp.setTinterSerialNbr(tinter.getSerialNbr());
								temp.setPosition(canister.getPosition());
								temp.setFillAlarmLevel(canister.getFillAlarmLevel());
								temp.setMaxCanisterFill(canister.getMaxCanisterFill());
								logger.debug("before sub canister = " + canister.getClrntCode() + " amount = " + canister.getCurrentClrntAmount());
								canister.setCurrentClrntAmount(canister.getCurrentClrntAmount() - 32.0d);
								logger.debug("after sub canister = " + canister.getClrntCode() + " amount = " + canister.getCurrentClrntAmount());
								temp.setCurrentClrntAmount(canister.getCurrentClrntAmount());
								logger.debug("temp canister amt after update = " + temp.getCurrentClrntAmount());
								temp.setFillStopLevel(canister.getFillStopLevel());
								custWebColorantsTxtList.add(temp);
								tinterService.updateColorantLevels(custWebColorantsTxtList);
							}
							else logger.error("Decrementing by this amount will result in a negative current colorant amount.");
						}
						else logger.debug("Canister object current amount full or empty.");
						break;

					default:
						logger.error("Invalid updateType given, updateType = " + getUpdateType());
						return ERROR;
					}
				}
			}
			else {
				custWebColorantsTxtList.clear();
				logger.debug("Cleared list, retry action.");
			}
			
			//return setallfull if setAllFull function is called to reload the page, otherwise no reload.
			if(getUpdateType().equals("setAllFull")){
				return "setallfull";
			}
			return SUCCESS;
			
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage() + e.getCause(), e);
			return ERROR;
		}
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

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public TinterInfo getTinter() {
		return tinter;
	}

	public void setTinter(TinterInfo tinter) {
		this.tinter = tinter;
	}

	public String getSelectedRowPosition() {
		return selectedRowPosition;
	}

	public void setSelectedRowPosition(String selectedRowPosition) {
		this.selectedRowPosition = Encode.forHtml(selectedRowPosition);
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = Encode.forHtml(updateType);
	}
	
}
