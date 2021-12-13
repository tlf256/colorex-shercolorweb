package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.ActionSupport;

import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulationResponse;

import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;
import org.springframework.beans.factory.annotation.Autowired;

public class ProcessPctAction extends ActionSupport implements SessionAware, LoginRequired {
	private Map<String, Object> sessionMap;
	@Autowired
	private FormulationService formulationService;
	@Autowired
	private CustomerService customerService;
	
	static Logger logger = LoggerFactory.getLogger(ProcessPctAction.class);
	private FormulaInfo displayFormula;
	private String requestId;
	private String percentOfFormula;
	private String returnStatus = "success";
	private String[] dispSourceDescr = new String[2];
	private List<SwMessage> validationMsgs;
	
	private String reqGuid;
	
	private static final long serialVersionUID = 1L;
	public String execute() {
		//wherein we formulate!
		
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			FormulationResponse oldFormula = (FormulationResponse) reqObj.getFormResponse();
			
			//and set the forced percentage
			if (percentOfFormula.isEmpty()) {
				percentOfFormula = "0";
			}
			reqObj.setPercentageFactor(Integer.valueOf(percentOfFormula)); 
	

			//call the formulation service to scale.  But first, reconstruct the sourceDescr.
			dispSourceDescr = oldFormula.getFormulas().get(0).getSourceDescr().split("%");
			// may be prefixed with the word "CUSTOM" so split on % may need extra help to complete
			String prefix;
			if(dispSourceDescr[0].lastIndexOf(" ") > 0){
				prefix = dispSourceDescr[0].substring(0, dispSourceDescr[0].lastIndexOf(" ") + 1);
			} else { 
				// no space so no words prefixing percentage
				prefix = "";
			}
			
			
			displayFormula = formulationService.scaleFormulaByPercent(oldFormula.getFormulas().get(0), reqObj.getPercentageFactor());
			displayFormula.setSourceDescr(prefix + percentOfFormula + "%" + dispSourceDescr[1]);
			reqObj.setDisplayFormula(displayFormula); 
			validationMsgs = formulationService.validateFormulation(displayFormula);
			for(SwMessage item:validationMsgs) {
				addActionMessage(item.getMessage());
			}
			
			//save the returned formula in the session using the guid for later display.

			sessionMap.put(reqGuid, reqObj);
			
			return returnStatus;
		} catch (RuntimeException e) {
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
	
	public String getRequestId() {
		return requestId;
	}


	public void setRequestId(String requestId) {
		this.requestId = requestId;
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

}
