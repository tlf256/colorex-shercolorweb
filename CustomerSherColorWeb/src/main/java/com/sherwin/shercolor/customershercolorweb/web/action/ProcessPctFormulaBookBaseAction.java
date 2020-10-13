package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.ActionSupport;

import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulationResponse;

import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;

public class ProcessPctFormulaBookBaseAction extends ActionSupport implements SessionAware, LoginRequired {
	private Map<String, Object> sessionMap;
	private FormulationService formulationService;
	private CustomerService customerService;
	
	private Map<String,String> colorBases;

	private String selectedColorBase;
	

	private FormulaInfo displayFormula;

	private String percentOfFormula;
	
	private String reqGuid;
	
	static Logger logger = LogManager.getLogger(ProcessPctFormulaBookBaseAction.class);
	private static final long serialVersionUID = 1L;
	
	private String[] dispSourceDescr = new String[2];
	private List<SwMessage> validationMsgs;
	
	private String returnStatus = "success";
	
	public ProcessPctFormulaBookBaseAction(){

	
	}
	
	public String execute() {
		//wherein we formulate!
		String selectedColorID = "";
		String selectedBase    = "";
		String selectedSubBase = "";
		
		try {
			//break up the requested color base
			String[] selectedSplit = new String[4];
			selectedSplit = selectedColorBase.split(String.valueOf((char) 31));
			selectedColorID = selectedSplit[1].trim();
			selectedBase 	= selectedSplit[2].trim();
			selectedSubBase = selectedSplit[3].trim();
			
			
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			FormulationResponse oldFormula = (FormulationResponse) reqObj.getFormResponse();
			
			//and set the forced percentage
			if (percentOfFormula.isEmpty()) {
				percentOfFormula = "0";
			}
			reqObj.setPercentageFactor(Integer.valueOf(percentOfFormula)); 
			
			for(FormulaInfo item:oldFormula.getFormulas()) {
				if (item.getColorId().equals(selectedColorID) && 
					item.getFbBase().equals(selectedBase)	&&
					item.getFbSubBase().equals(selectedSubBase)) {
					dispSourceDescr = item.getSourceDescr().split("%");
					// may be prefixed with the word "CUSTOM" so split on % may need extra help to complete
					String prefix;
					if(dispSourceDescr[0].lastIndexOf(" ") > 0){
						prefix = dispSourceDescr[0].substring(0, dispSourceDescr[0].lastIndexOf(" ") + 1);
					} else { 
						// no space so no words prefixing percentage
						prefix = "";
					}
					displayFormula = formulationService.scaleFormulaByPercent(item, reqObj.getPercentageFactor());
					displayFormula.setSourceDescr(prefix + percentOfFormula + "%" + dispSourceDescr[1]);
					reqObj.setDisplayFormula(displayFormula); 
					validationMsgs = formulationService.validateFormulation(displayFormula);
					for(SwMessage item2:validationMsgs) {
						addActionMessage(item2.getMessage());
					}
				}
			}
	
		
			sessionMap.put(reqGuid, reqObj);
			
			return returnStatus;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String display() {

		 try {
				colorBases = new HashMap<String, String>();
				RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
				String theValue;
				String theKey;
				int hashCntr = 0;
				FormulationResponse oldFormula = (FormulationResponse) reqObj.getFormResponse();
				for(FormulaInfo item:oldFormula.getFormulas()) {
					theValue = item.getColorId()  + " " 
							+ item.getFbBase() + " " + item.getFbSubBase();
					theKey = hashCntr + Character.toString((char) 31)  + item.getColorId() + Character.toString((char) 31) + " " 
							+ item.getFbBase() + Character.toString((char) 31) + " " + item.getFbSubBase();
					colorBases.put(theKey, theValue);
					hashCntr = hashCntr + 1;
				}
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}

	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}
	

	
	public FormulationService getFormulationService() {
		return formulationService;
	}


	public void setFormulationService(FormulationService formulationService) {
		this.formulationService = formulationService;
	}
	

	public CustomerService getCustomerService() {
		return customerService;
	}


	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public String getPercentOfFormula() {
		return percentOfFormula;
	}

	public void setPercentOfFormula(String percentOfFormula) {
		this.percentOfFormula = Encode.forHtml(percentOfFormula);
	}
	
	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}


	public Map<String,String> getColorBases() {
		return colorBases;
	}

	public void setColorBases(Map<String,String> colorBases) {
		this.colorBases = colorBases;
	}

	public String getSelectedColorBase() {
		return selectedColorBase;
	}

	public void setSelectedColorBase(String selectedColorBase) {
		this.selectedColorBase = selectedColorBase;
	}

}
