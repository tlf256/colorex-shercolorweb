package com.sherwin.shercolor.customershercolorweb.web.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsClrnt;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.CustWebEcal;
import com.sherwin.shercolor.common.domain.CustWebTinterEvents;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.service.ColorantService;
import com.sherwin.shercolor.common.service.EcalService;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.customershercolorweb.web.model.DispenseItem;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterCanister;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;
import com.sherwin.shercolor.util.domain.SwMessage;

public class TinterUtilityAction extends ActionSupport  implements SessionAware, LoginRequired {

	private static final long serialVersionUID = 1L;

	static Logger logger = LogManager.getLogger(TinterUtilityAction.class.getName());
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private String tinterClrnt;
	private String tinterModel;
	private String tinterSerial;
	
	private FormulaInfo displayFormula;
	private List<DispenseItem> dispenseFormula;

	// for decrement
	private List<Map<String,Object>> shotList;
	private boolean adjustedColorantLevels;
	
	// for tinterInit
	List<String> initErrorList;

	TinterInfo tinter;

	@Autowired
	private TinterService tinterService;
	
	@Autowired
	private ColorantService colorantService;
	
	@Autowired
	private EcalService ecalService;
	
	public String stampSession(){
		String retVal=null;
		
		try{
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			tinter = new TinterInfo();
			
			//Check if config on file (ColorantsTxt), get lastPurge from DB, get colorant levels from DB
			List<CustWebColorantsTxt> colorantList = tinterService.getCanisterList(reqObj.getCustomerID(), tinterClrnt, tinterModel, tinterSerial);
			if(colorantList==null || colorantList.size()==0){
				// Tinter defined on localhost does not exist in our DB
				tinter.setTinterOnFile(false);
				retVal = SUCCESS;
			} else {
				// good config
				tinter.setTinterOnFile(true);
				
				// fill in clrntSys, model, serialNbr for session
				tinter.setClrntSysId(tinterClrnt);
				tinter.setModel(tinterModel);
				tinter.setSerialNbr(tinterSerial);
				
				// populate canister list (includes colorant levels)
				List<CdsClrnt> cdsClrntList = colorantService.getColorantList(tinterClrnt); // need this for the name of colorant

				List<TinterCanister> buildMe = new ArrayList<TinterCanister>();
				for(CustWebColorantsTxt colorant : colorantList){
					TinterCanister canister = new TinterCanister();
					canister.setClrntCode(colorant.getClrntCode());
					canister.setPosition(colorant.getPosition());
					canister.setCurrentClrntAmount(colorant.getCurrentClrntAmount());
					canister.setFillAlarmLevel(colorant.getFillAlarmLevel());
					canister.setFillStopLevel(colorant.getFillStopLevel());
					canister.setMaxCanisterFill(colorant.getMaxCanisterFill());
					// get colorant name from cdsClrntList
					for(CdsClrnt myCdsClrnt : cdsClrntList){
						if(myCdsClrnt.getTintSysId().equals(colorant.getClrntCode())){
							canister.setClrntName(myCdsClrnt.getName());
							break;
						}
					}
					// set rgbHex
					// yuk case statement for now, find a better place to store and retrieve this
					switch(colorant.getClrntCode()){
					case "B1":
						canister.setRgbHex("#000000");
						break;
					case "G2":
						canister.setRgbHex("#009933");
						break;
					case "L1":
						canister.setRgbHex("#0000ff");
						break;
					case "N1":
						canister.setRgbHex("#996633");
						break;
					case "R2":
						canister.setRgbHex("#990000");
						break;
					case "R3":
						canister.setRgbHex("#ff3399");
						break;
					case "R4":
						canister.setRgbHex("#ff0000");
						break;
					case "Y1":
						canister.setRgbHex("#ffff00");
						break;
					case "Y3":
						canister.setRgbHex("#ffcc00");
						break;
					case "W1":
						canister.setRgbHex("#ffffff");
						break;
					default:
						canister.setRgbHex("#000000"); // black
						break;
					}
					buildMe.add(canister);
				}
				tinter.setCanisterList(buildMe);

				refreshTinterInfo(reqObj.getCustomerID(), tinter);

				reqObj.setTinter(tinter);
				retVal = SUCCESS;
			}
			
			sessionMap.put(reqGuid, reqObj);
			
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			System.out.println("Stamp Session Exception: " + e.getMessage());
			retVal = ERROR;
		}
		
		return retVal;
	}

	public String stampSessionTinterInit(){
		String retVal=null;
		
		try{
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			tinter = reqObj.getTinter();
			
			if(tinter==null){
				// tinter should already be in the session, error if not
				retVal = ERROR;
			} else {
				if(initErrorList==null || initErrorList.size()==0){
					tinter.setLastInitErrorList(new ArrayList<String>());
				} else {
					tinter.setLastInitErrorList(initErrorList);
				}

				reqObj.setTinter(tinter);
				retVal = SUCCESS;
			}
			
			sessionMap.put(reqGuid, reqObj);
			
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			System.out.println("Stamp Session TinterInit Exception: " + e.getMessage());
			retVal = ERROR;
		}
		
		return retVal;
	}

	public String getSession(){
		String retVal=null;
		
		try{
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			tinter = reqObj.getTinter();
			System.out.println("in tinterutilityaction getSession, tinter is " + tinter.getModel() + " " + tinter.getClrntSysId() + " " + tinter.getSerialNbr());
			refreshTinterInfo(reqObj.getCustomerID(), tinter);
			
			retVal = SUCCESS;
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			System.out.println("Get Session Exception: " + e.getMessage());
			retVal = ERROR;
		}
		
		return retVal;
	}
	

	private void refreshTinterInfo(String customerId, TinterInfo tinter){
		
		// has nozzle?
		tinter.setAutoNozzleCover(tinterService.hasAutoNozzleCover(customerId, tinter.getClrntSysId(), tinter.getModel(), tinter.getSerialNbr()));
		
		// get last purge date
		CustWebTinterEvents lastPurgeEvent =tinterService.getLastPurgeDateAndUser(customerId, tinter.getClrntSysId(), tinter.getModel(), tinter.getSerialNbr());
		if(lastPurgeEvent!=null){
			tinter.setLastPurgeUser(lastPurgeEvent.getEventDetails());
			SimpleDateFormat jsdf = new SimpleDateFormat("EE MMM d y H:m:s 'GMT'Z (zz)");
			tinter.setLastPurgeDate(jsdf.format(lastPurgeEvent.getDateTime()));
		}
		
		// do we have an Ecal on file for this tinter?
		List<CustWebEcal> ecalList = ecalService.getEcalList(customerId, tinter.getClrntSysId(), tinter.getModel(), tinter.getSerialNbr());
		if(ecalList!=null && ecalList.size()>0) tinter.setEcalOnFile(true);
		else tinter.setEcalOnFile(false);
		
		
	}
	
	
	public String decrementColorant(){
		String retVal=null;
		
		try{
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			tinter = reqObj.getTinter();
			
			List<CustWebColorantsTxt> writeColorantList = new ArrayList<CustWebColorantsTxt>();
			if(shotList!=null){
				if(shotList.size()>0){
					for(Map<String,Object> item : shotList){
						String code=null; Double shots=null; Double uom=null;
						if(item.containsKey("code")) code = item.get("code").toString();
						if(item.containsKey("shots")) shots = Double.parseDouble(item.get("shots").toString());
						if(item.containsKey("uom")) uom = Double.parseDouble(item.get("uom").toString());
						if(code!=null && uom!=null && shots!=null){
							// TODO find item in tinter.canisterList, decrement and put in list to be written to DB
							for(TinterCanister canister : tinter.getCanisterList()){
								if(canister.getClrntCode().equalsIgnoreCase(code)){
									CustWebColorantsTxt addClrnt = new CustWebColorantsTxt();
									addClrnt.setCustomerId(reqObj.getCustomerID());
									addClrnt.setClrntSysId(tinter.getClrntSysId());
									addClrnt.setTinterModel(tinter.getModel());
									addClrnt.setTinterSerialNbr(tinter.getSerialNbr());
									addClrnt.setClrntCode(canister.getClrntCode());
									addClrnt.setPosition(canister.getPosition());
									addClrnt.setMaxCanisterFill(canister.getMaxCanisterFill());
									addClrnt.setFillAlarmLevel(canister.getFillAlarmLevel());
									addClrnt.setFillStopLevel(canister.getFillStopLevel());
									Double newLevel = canister.getCurrentClrntAmount() - (shots/uom);
									canister.setCurrentClrntAmount(newLevel);
									addClrnt.setCurrentClrntAmount(canister.getCurrentClrntAmount());
									writeColorantList.add(addClrnt);
								} // end if codes match
							} // end for each canister in the tinter
						} // end if shotList item is good
					} // end for each shotList
					// Write to db through tinterService...
					List<SwMessage> errorList = tinterService.updateColorantLevels(writeColorantList);
					if(errorList==null || errorList.size()<=0) adjustedColorantLevels = true;
					else adjustedColorantLevels = false;
				} else {
					// empty list passed in
					adjustedColorantLevels = false;
				}
			} else {
				// null list passed in
				adjustedColorantLevels = false;
			}
			
			reqObj.setTinter(tinter);
			// TODO put reqObj back in session
			sessionMap.put(reqGuid, reqObj);
			
			
			retVal = SUCCESS;
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			System.out.println("decrementColor Exception: " + e.getMessage());
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

	public String getTinterClrnt() {
		return tinterClrnt;
	}

	public String getTinterModel() {
		return tinterModel;
	}

	public String getTinterSerial() {
		return tinterSerial;
	}

	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}

	public List<DispenseItem> getDispenseFormula() {
		return dispenseFormula;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public void setTinterClrnt(String tinterClrnt) {
		this.tinterClrnt = Encode.forHtml(tinterClrnt);
	}

	public void setTinterModel(String tinterModel) {
		this.tinterModel = Encode.forHtml(tinterModel);
	}

	public void setTinterSerial(String tinterSerial) {
		this.tinterSerial = Encode.forHtml(tinterSerial);
	}

	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}

	public void setDispenseFormula(List<DispenseItem> dispenseFormula) {
		this.dispenseFormula = dispenseFormula;
	}

	public TinterInfo getTinter() {
		return tinter;
	}

	public boolean isAdjustedColorantLevels() {
		return adjustedColorantLevels;
	}

	public void setShotList(List<Map<String,Object>> shotList) {
		this.shotList = shotList;
	}

	public void setInitErrorList(List<String> initErrorList) {
		this.initErrorList = initErrorList;
	}


	
}
