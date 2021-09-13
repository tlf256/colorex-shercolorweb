package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsClrntSys;
import com.sherwin.shercolor.common.domain.Eula;
import com.sherwin.shercolor.common.domain.EulaTemplate;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.EulaService;
import com.sherwin.shercolor.customerprofilesetup.web.model.CustomerSummary;
import com.sherwin.shercolor.customerprofilesetup.web.model.RequestObject;

public class CustomerAction extends ActionSupport implements SessionAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(CustomerAction.class);
	private Map<String, Object> sessionMap;
	private List<CustomerSummary> custSummList;
	private boolean updateMode;
	private boolean edited;
	private boolean newCustomer;
	List<String> eulaList;
	List<String> custTypeList;
	List<String> eulaTempList;

	@Autowired
	CustomerService customerService;
	
	@Autowired 
	private EulaService eulaService;
	
	@Autowired
	ColorantService colorantService;
	
	public String execute() {
		try {
			//RequestObject reqObj = new RequestObject();
			
			custSummList = new ArrayList<CustomerSummary>();
			
			List<Object[]> custList = customerService.getCustSummaryList();
			
			for(Object[] item : custList) {
				CustomerSummary summary = new CustomerSummary();
				summary.setCustomerId(item[0].toString());
				if(item[1]==null) {
					summary.setCustomerName("");
				} else {
					summary.setCustomerName(item[1].toString());
				}
				summary.setClrntSysSummary(item[2].toString());
				custSummList.add(summary);
			}
			
			//sessionMap.put("CustomerDetail", reqObj);
			
			setUpdateMode(false);
						
			return SUCCESS;
		
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			return ERROR;
		}
	}
	
	public String display() {
		return SUCCESS;
	}
	
	public String displayEdit() {
		RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
		
		buildSelectLists(reqObj.getCustomerId(), reqObj.isNewCustomer());
		
		return SUCCESS;
	}
	
	private void buildSelectLists(String customerId, boolean isNewCustomer) {
		setEulaList(buildEulaList(customerId, isNewCustomer));
		setCustTypeList(buildCustTypeList());
		setEulaTempList(buildEulaTempList());
	}
	
	public String createNew() {
		try {
			RequestObject reqObj = new RequestObject();
			
			reqObj.setNewCustomer(true);
			
			buildSelectLists(null, true);
			
			sessionMap.put("CustomerDetail", reqObj);
						
			return SUCCESS;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	private List<String> buildEulaList(String customerId, boolean isNewCustomer){
		List<String> eulaList = new ArrayList<String>();
		Eula sherColorWebEula = new Eula();
		
		eulaList.add(0, "None");
		
		// read active eulas 
		sherColorWebEula = eulaService.readActive("CUSTOMERSHERCOLORWEB", customerId);
		
		// only add EULA types to list if customer ID is null
		if(sherColorWebEula != null) {
			if(sherColorWebEula.getCustomerId() == null) {
				eulaList.add("SherColor Web EULA");
				eulaList.add("Custom EULA Template");
				if(!isNewCustomer) {
					eulaList.add("Custom EULA");
				}
			}
		}
		
		return eulaList;
	}
	
	private List<String> buildEulaTempList(){
		List<String> eulaTempList = new ArrayList<String>();
		
		List<EulaTemplate> tempList = eulaService.getTemplatesForWebsite("CUSTOMERSHERCOLORWEB");
		
		for(EulaTemplate temp : tempList) {
			StringBuilder template = new StringBuilder();
			template.append(temp.getTemplateId());
			template.append(" ");
			template.append(temp.getSeqNbr());
			eulaTempList.add(template.toString());
		}
		
		return eulaTempList;
	}
	
	private List<String> buildCustTypeList(){
		List<String> typeList = new ArrayList<String>();
		
		typeList.add(0, "CUSTOMER");
		typeList.add(1, "DRAWDOWN");
		typeList.add(2, "STORE");
		
		return typeList;
	}
	
	private List<String> buildClrntSysIdList(){
		List<String> clrntSysIdList = new ArrayList<String>();
		
		List<CdsClrntSys> recordList = colorantService.getAllColorantSystems();
		
		for(CdsClrntSys ccs : recordList) {
			clrntSysIdList.add(ccs.getClrntSysId());
		}
		
		return clrntSysIdList;
	}
	
	public String update() { 
		try { 
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			buildSelectLists(reqObj.getCustomerId(), reqObj.isNewCustomer());
			
			setEdited(true);
			
			return SUCCESS;
	  
		} catch (RuntimeException e){ 
			logger.error(e.getMessage(), e); 
			return ERROR; 
		} 
	}
	
	public String reset() {
		 try {
			 sessionMap.clear();
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String cancelUpdate() {
		try {
			setEdited(false);
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String cancel() {
		try {
			setUpdateMode(false);
			setEdited(false);
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public List<CustomerSummary> getCustSummList() {
		return custSummList;
	}
	
	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}
	
	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	public boolean isNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(boolean newCustomer) {
		this.newCustomer = newCustomer;
	}
	
	public List<String> getEulaList() {
		return eulaList;
	}

	public void setEulaList(List<String> eulaList) {
		this.eulaList = eulaList;
	}

	public List<String> getCustTypeList() {
		return custTypeList;
	}

	public void setCustTypeList(List<String> custTypeList) {
		this.custTypeList = custTypeList;
	}

	public List<String> getEulaTempList() {
		return eulaTempList;
	}

	public void setEulaTempList(List<String> eulaTempList) {
		this.eulaTempList = eulaTempList;
	}

	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

}
