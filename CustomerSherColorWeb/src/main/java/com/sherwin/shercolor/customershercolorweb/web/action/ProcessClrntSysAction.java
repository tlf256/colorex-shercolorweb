package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsClrntSys;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.service.ColorantService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;


@SuppressWarnings("serial")
public class ProcessClrntSysAction extends ActionSupport implements SessionAware, LoginRequired  {

	static Logger logger = LogManager.getLogger(ProcessClrntSysAction.class);
	private Map<String, Object> sessionMap;
	private String partialProductNameOrId;
	private List<String> selectClrntSysIds;
	private String selectedClrntSys;
	private String defaultClrntSys;
	
	private String reqGuid;
	

	private String colorComp;
	private String colorID;
	private String colorName;
	private String salesNbr;
	
	@Autowired
	ColorantService colorantService;
	
	@Autowired
	CustomerService customerService;
	
	public String execute() {
		try {
			//save the colorant system in the session for use when formulating
			//set the successful information into the request object.
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setClrntSys(selectedClrntSys);
			sessionMap.put(reqGuid, reqObj);
		    return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
		 
	}
	
	// User hit the backup button on the ClrntSys page
	public String backItUp() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setClrntSys("");
			sessionMap.put(reqGuid, reqObj);
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}
	
	public String display() {
		List<CustWebParms> custList;
		List<CdsClrntSys> availableClrntSyses;
		String custID;
		
		List<String> custClrntSysIds = new ArrayList<String>();
		List<String> availClrntSysIds = new ArrayList<String>();
		

		 try {
			 selectClrntSysIds = new ArrayList<String>();
			 RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			custID = reqObj.getCustomerID();
			 
			//get the customer's colorant system(s)
			custList = customerService.getAllCustWebParms(custID);
			for (CustWebParms item:custList) {
				custClrntSysIds.add(item.getClrntSysId());
			}
			 
			//get a list of the available colorant systems, less any exclusions
			availableClrntSyses = colorantService.getAvailableColorantSystems(reqObj.getSalesNbr());
			for (CdsClrntSys item:availableClrntSyses) {
				availClrntSysIds.add(item.getClrntSysId());
			}
			
			//now get the overlap of the two lists.  That will be the list of colorant systems.
			boolean addit;
			boolean defaultset = false;
			for (String item:custClrntSysIds) {
				addit = false;
				for (String item2:availClrntSysIds){
					if (item.equals(item2)) {
						addit = true;
					}
				}
				if (addit) {
					selectClrntSysIds.add(item);
					if (!defaultset) {
						this.setDefaultClrntSys(item);
						defaultset = true;
					}
				}
			} 
			
			//Now that the selectClrntSysIds is set, see how many there are.  If there are none,
			// report an error.  If one, we don't want to display the page, just continue forward
			//using the only colorant system available.  If multiple, we want to display the page.
			if (selectClrntSysIds.size()==0) {
				addFieldError("clrntSys", getText("processClrntSysAction.noApplicableClrntSysAvail"));
				return NONE;
			} else if (selectClrntSysIds.size()==1) {
				reqObj.setClrntSys(selectClrntSysIds.get(0));
				 sessionMap.put(reqGuid, reqObj);
				return SUCCESS;
			} else {
				return INPUT;
			}
			
		    
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public String getPartialProductNameOrId() {
		return partialProductNameOrId;
	}

	public void setPartialProductNameOrId(String partialProductNameOrId) {
		this.partialProductNameOrId = partialProductNameOrId;
	}
	
	public List<String> getSelectClrntSysIds() {
		return selectClrntSysIds;
	}

	public void setSelectClrntSysIds(List<String> selectClrntSysIds) {
		this.selectClrntSysIds = selectClrntSysIds;
	}

	public String getSelectedClrntSys() {
		return selectedClrntSys;
	}

	public void setSelectedClrntSys(String selectedClrntSys) {
		this.selectedClrntSys = Encode.forHtml(selectedClrntSys);
	}

	public String getDefaultClrntSys() {
		return defaultClrntSys;
	}

	public void setDefaultClrntSys(String defaultClrntSys) {
		this.defaultClrntSys = Encode.forHtml(defaultClrntSys);
	}

	public String getColorComp() {
		return colorComp;
	}

	public void setColorComp(String colorComp) {
		this.colorComp = Encode.forHtml(colorComp);
	}

	public String getColorID() {
		return colorID;
	}

	public void setColorID(String colorID) {
		this.colorID = Encode.forHtml(colorID);
	}

	public String getSalesNbr() {
		return salesNbr;
	}

	public void setSalesNbr(String salesNbr) {
		this.salesNbr = Encode.forHtml(salesNbr);
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = Encode.forHtml(colorName);
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}
	
}