package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
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
			else System.out.println("reqGuid is empty");
		}
		catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage() + e.getCause());
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	public String update() {
		try {
			System.out.println("updateType is " + getUpdateType() + " at beginning of method.");
			//Setting up colorList object
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			tinter = reqObj.getTinter();
			List<CustWebColorantsTxt> custWebColorantsTxtList = new ArrayList<CustWebColorantsTxt>();
			//Testing to see if both lists are acceptable
			
			if (custWebColorantsTxtList.isEmpty() && !tinter.getCanisterList().isEmpty()) {
				System.out.println("passed condition");
				for(TinterCanister canister : tinter.getCanisterList()) {
					
					switch (getUpdateType()) {
					case "setAllFull":
						System.out.println("In setAllFull");
						System.out.println("canister amount = " + canister.getCurrentClrntAmount());
						
						//Testing to see if canister object amount < max amount
						
						if(canister.getCurrentClrntAmount() < canister.getMaxCanisterFill()) {
							System.out.println("in loop");
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
						else System.out.println("Canister object current amount full or empty.");
						break;
						
					case "setfl":
						System.out.println("In setOneFull");
						System.out.println("canister amount = " + canister.getCurrentClrntAmount());
						System.out.println("selectedRowPosition is " + selectedRowPosition);
						//Testing to see when on the correct row according to key and canister values are acceptable for subtraction
						
						if(canister.getCurrentClrntAmount() <= canister.getMaxCanisterFill() && canister.getCurrentClrntAmount() >= 0){
							
							//Using the key to assure update of ONLY the selected canister/colorant.
							
							if(Integer.parseInt(selectedRowPosition) == canister.getPosition()) {
								System.out.println("in loop");
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
							else System.out.println("Canister does not match the key.");
						}
						else System.out.println("Canister object current amount full or empty.");
						break;
						
					case "addqt":
						System.out.println("In addQuart");
						System.out.println("canister amount = " + canister.getCurrentClrntAmount());
						System.out.println("selectedRowPosition is " + selectedRowPosition);
						//Testing to see when on the correct row according to key and canister values are acceptable for addition
						
						if(Integer.parseInt(selectedRowPosition) == canister.getPosition() && canister.getCurrentClrntAmount() <= canister.getMaxCanisterFill() && canister.getCurrentClrntAmount() >= 0){
							if((canister.getCurrentClrntAmount() + 32.0d) <= canister.getMaxCanisterFill()){
								System.out.println("in loop");
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
							else System.out.println("Incrementing by this amount will result in a current colorant amount that exceeds the Max Fill amount.");
						}
						else System.out.println("Canister object current amount full or empty.");
						break;
						
					case "subqt":
						System.out.println("In subQuart");
						System.out.println("canister = " + canister.getClrntCode() + " amount = " + canister.getCurrentClrntAmount());
						System.out.println("selectedRowPosition is " + selectedRowPosition);
						//Testing to see when on the correct row according to key and canister values are acceptable for subtraction
						
						if(Integer.parseInt(selectedRowPosition) == canister.getPosition() && canister.getCurrentClrntAmount() <= canister.getMaxCanisterFill() && canister.getCurrentClrntAmount() >= 0){
							if((canister.getCurrentClrntAmount() - 32.0d) >= 0){
								System.out.println("in loop");
								CustWebColorantsTxt temp = new CustWebColorantsTxt();
								temp.setCustomerId(reqObj.getCustomerID());
								temp.setClrntCode(canister.getClrntCode());
								temp.setClrntSysId(tinter.getClrntSysId());
								temp.setTinterModel(tinter.getModel());
								temp.setTinterSerialNbr(tinter.getSerialNbr());
								temp.setPosition(canister.getPosition());
								temp.setFillAlarmLevel(canister.getFillAlarmLevel());
								temp.setMaxCanisterFill(canister.getMaxCanisterFill());
								System.out.println("before sub canister = " + canister.getClrntCode() + " amount = " + canister.getCurrentClrntAmount());
								canister.setCurrentClrntAmount(canister.getCurrentClrntAmount() - 32.0d);
								System.out.println("after sub canister = " + canister.getClrntCode() + " amount = " + canister.getCurrentClrntAmount());
								temp.setCurrentClrntAmount(canister.getCurrentClrntAmount());
								System.out.println("temp canister amt after update = " + temp.getCurrentClrntAmount());
								temp.setFillStopLevel(canister.getFillStopLevel());
								custWebColorantsTxtList.add(temp);
								tinterService.updateColorantLevels(custWebColorantsTxtList);
							}
							else System.out.println("Decrementing by this amount will result in a negative current colorant amount.");
						}
						else System.out.println("Canister object current amount full or empty.");
						break;

					default:
						logger.error("Invalid updateType given, updateType = " + getUpdateType());
						System.out.println("method failed");
						return ERROR;
					}
				}
			}
			else {
				custWebColorantsTxtList.clear();
				System.out.println("Cleared list, retry action.");
			}
			
			//return setallfull if setAllFull function is called to reload the page, otherwise no reload.
			if(getUpdateType().equals("setAllFull")){
				return "setallfull";
			}
			return SUCCESS;
			
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage() + e.getCause());
			System.out.println("method failed");
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
