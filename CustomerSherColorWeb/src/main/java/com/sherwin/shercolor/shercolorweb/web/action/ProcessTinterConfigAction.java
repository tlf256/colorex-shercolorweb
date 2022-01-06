package com.sherwin.shercolor.shercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebEcal;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.service.EcalService;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.shercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.shercolorweb.web.model.TinterCanister;
import com.sherwin.shercolor.shercolorweb.web.model.TinterInfo;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Component;

@Component
public class ProcessTinterConfigAction extends ActionSupport implements SessionAware, LoginRequired  {
	private Map<String, Object> sessionMap;
	private List<CustWebEcal> myEcalList; // for future
	private List<String> defaultColorantList;
	private List<String> defaultModelList = new ArrayList<String>();
	private static final long serialVersionUID = 1L;
	static Logger logger = LoggerFactory.getLogger(ProcessTinterConfigAction.class);
	//for ajax calls
	private String reqGuid;
	private String customerId;
	private String clrntSysId;
	private String tinterModel;
	private String tinterSerialNbr;
	//to send back canister list
	private TinterInfo newtinter;

	private boolean reReadLocalHostTinter;



	@Autowired
	TinterService tinterService;
	@Autowired
	EcalService   ecalService;

	public String AjaxGetModels(){
		this.setDefaultModelList(tinterService.listOfModelsForCustomerId(customerId, clrntSysId));
		return SUCCESS;
	}



	public String AjaxGetCanisterList(){

		logger.debug("inside getCanisterList and reqGuid is " + reqGuid);
		
		if(newtinter !=null){

			logger.debug("newtinter:" + newtinter.getModel());
			logger.debug("newtinter:" + Encode.forHtml(newtinter.getSerialNbr()));
			logger.debug("newtinter:" + newtinter.getClrntSysId());
			logger.debug("thisserial:" + this.tinterSerialNbr);
			logger.debug("newtinterserial:" + Encode.forHtml(newtinter.getSerialNbr()));
		}
		else{
			newtinter = new TinterInfo();
		}
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		logger.debug("inside getCanisterList, got reqObj, now getting tinter");
		if(reqObj !=null) {this.setCustomerId(reqObj.getCustomerID());}
		else {System.out.print("reqObj null");}
		System.out.print("customerId:" + this.getCustomerId());


		newtinter.setClrntSysId(this.getClrntSysId());
		newtinter.setModel(getTinterModel());
		newtinter.setSerialNbr(this.getTinterSerialNbr());

		if(newtinter!=null && newtinter.getModel()!=null && !newtinter.getModel().isEmpty() &&
				newtinter.getCanisterList()==null){ // make sure we have all the params we need to do this query
			//first look for a specific serial number and customer id
			List<CustWebColorantsTxt>  colorantsTxtList = tinterService.getCanisterList(this.getCustomerId(), newtinter.getClrntSysId(), newtinter.getModel(), newtinter.getSerialNbr());
			if (colorantsTxtList == null  || colorantsTxtList.isEmpty()){
				//next look for template.
				colorantsTxtList = tinterService.getCanisterList("DEFAULT", newtinter.getClrntSysId(), newtinter.getModel(), null);
				//save it.
			}
			if(colorantsTxtList!=null ){
				sessionMap.put("colorantsTxtList",colorantsTxtList); //**********save to session variable so SaveNewTinter(page submit) can use it  *****/
				List<TinterCanister> buildMe = new ArrayList<TinterCanister>(); //canister list
				for(CustWebColorantsTxt colorant:colorantsTxtList){
					//build canister to send back to ajax call
					TinterCanister canister = new TinterCanister();
					canister.setClrntCode(colorant.getClrntCode());
					canister.setPosition(colorant.getPosition());
					canister.setCurrentClrntAmount(colorant.getCurrentClrntAmount());
					canister.setFillAlarmLevel(colorant.getFillAlarmLevel());
					canister.setFillStopLevel(colorant.getFillStopLevel());
					canister.setMaxCanisterFill(colorant.getMaxCanisterFill());
					buildMe.add(canister);

				}
				
				newtinter.setCanisterList(buildMe);
				logger.debug("New Canister list added for: " + newtinter.getClrntSysId() + "_" + newtinter.getModel()+"_"+Encode.forHtml(newtinter.getSerialNbr()));


			}
			else{
				logger.debug("Not saving colorantsTxt.  Customer ID is:" + getCustomerId());
			}
		}




		//do this later - DJM
		//reqObj.setTinter(getNewtinter());
		//sessionMap.put(reqGuid, reqObj);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String SaveNewTinter(){
		/*
		 * colorants.txt
    	private String customerId; 
    	private String clrntSysId;
    	private String tinterModel;
    	private String tinterSerialNbr;
    	private String clrntCode;
    	private int position;
    	private Double maxCanisterFill;
    	private Double fillAlarmLevel;
    	private Double fillStopLevel;
    	private Double currentClrntAmount;
		 */

		/* canister list 
		 * private int position;
			private String clrntCode;
			private Double maxCanisterFill;
			private Double fillAlarmLevel;
			private Double fillStopLevel;
			private Double currentClrntAmount;
		 */

		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		logger.debug("Begin Saving colorantsTxt.");
		
		try {
			if(reqObj !=null) {this.setCustomerId(reqObj.getCustomerID());}
			List<CustWebColorantsTxt> colorantsTxtList = (List<CustWebColorantsTxt>) sessionMap.get("colorantsTxtList");
			if(colorantsTxtList ==null) {
				logger.error("ColorantsTxt is null");
				addActionError(getText("processTinterConfigAction.nullClrntsTxt"));
				return INPUT;
			}
			else{
			
				for(CustWebColorantsTxt colorant:colorantsTxtList){
					colorant.setCustomerId(getCustomerId()); // change customer id so we can add this back into colorantstxt
					colorant.setTinterSerialNbr(Encode.forHtml(this.getNewtinter().getSerialNbr()));
					

				}
				//2018-05-06 BKP Corrected comparison on string to use .equals instead of != based on Veracode scan
				//if(this.getCustomerId()!=null && this.getCustomerId()!= "DEFAULT" && this.getNewtinter().getSerialNbr() != null
				if(this.getCustomerId()!=null && !this.getCustomerId().equals("DEFAULT") && this.getNewtinter().getSerialNbr() != null
						&& this.getNewtinter().getSerialNbr().length()>1){
					logger.info("Saving colorantsTxt.  Serial num is:" + Encode.forHtml(this.getNewtinter().getSerialNbr()));
					logger.debug("Saving colorantsTxt.  Serial num is:" + Encode.forHtml(this.getNewtinter().getSerialNbr()));
						// create records if they don't already exist, and colorant code may be different than expected because of layout update
						if(tinterService.conditionalSaveColorantsTxtByPosition(colorantsTxtList) < 0){
							//save if new customer id otherwise do nothing.  If error return error
							addActionError(getText("processTinterConfigAction.configNotCompleteColorantsTxt"));
							return INPUT;
						}
				}
				else {
					addActionError(getText("processTinterConfigAction.invalidSerialNbr", new String[]{this.getNewtinter().getSerialNbr()}));
					return INPUT;
				}
			}
			reReadLocalHostTinter = true;
		} catch(RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String display(){
		String retVal = null;

		try {
			logger.debug("inside ProcessTinterConfigAction and reqGuid is " + reqGuid);
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			logger.debug("inside ProcessTinterConfigAction, got reqObj, now getting tinter");
			TinterInfo tinter = reqObj.getTinter();
			logger.debug("inside ProcessTinterConfigAction, got tinter, returning success");

			//TODO get last Config date info to be shown on screen...

			// setup tinter stuff if needed
			if(tinter==null) logger.error("inside ProcessConfigAction_display and tinter object is null");
			//set up list of colorants for combo box
			this.setDefaultColorantList( tinterService.listOfColorantSystemsByCustomerId("DEFAULT"));
			//get List of Ecals
			if(reqObj.getCustomerID()!=null){
				myEcalList = ecalService.getEcalsByCustomer(reqObj.getCustomerID());
			}
			else{
				String customerID="TEST";
				myEcalList = ecalService.getEcalsByCustomer(customerID);
			}
			//get List of Template Cals
			logger.debug("CustomerID=" +reqObj.getCustomerID());


			retVal = SUCCESS;

		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
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

	public List<CustWebEcal> getMyEcalList() {
		return myEcalList;
	}

	public void setMyEcalList(List<CustWebEcal> myEcalList) {
		this.myEcalList = myEcalList;
	}

	public List<String> getDefaultColorantList() {
		return defaultColorantList;
	}

	public void setDefaultColorantList(List<String> defaultColorantList) {
		ArrayList<String> newClrntList = new ArrayList<String>();
		for (String dclElement : defaultColorantList) {
			newClrntList.add(Encode.forHtml(dclElement));
			
		}
		this.defaultColorantList = newClrntList;
	}

	public List<String> getDefaultModelList() {
		return defaultModelList;
	}

	public void setDefaultModelList(List<String> defaultModelList) {
		ArrayList<String> newModelList = new ArrayList<String>();
		for (String dmlElement : defaultModelList) {
			newModelList.add(Encode.forHtml(dmlElement));
			
		}
		this.defaultModelList = newModelList;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = Encode.forHtml(customerId);
	}

	public String getClrntSysId() {
		return clrntSysId;
	}

	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = Encode.forHtml(clrntSysId);
	}


	public String getTinterModel() {
		return tinterModel;
	}

	public void setTinterModel(String tinterModel) {
		this.tinterModel = Encode.forHtml(tinterModel);
	}


	public String getTinterSerialNbr() {
		return tinterSerialNbr;
	}

	public void setTinterSerialNbr(String tinterSerialNbr) {
		this.tinterSerialNbr = Encode.forHtml(tinterSerialNbr);
	}

	public TinterInfo getNewtinter() {
		return newtinter;
	}

	public void setNewtinter(TinterInfo newtinter) {
		this.newtinter = newtinter;
	}

	public boolean isReReadLocalHostTinter() {
		return reReadLocalHostTinter;
	}


}
