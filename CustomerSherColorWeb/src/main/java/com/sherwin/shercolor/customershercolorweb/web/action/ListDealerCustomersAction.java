package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebDealer;
import com.sherwin.shercolor.common.domain.CustWebDealerCust;
import com.sherwin.shercolor.common.service.CustomerOrderService;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustDtoBuilder;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerDtoBuilder;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@SuppressWarnings("serial")
public class ListDealerCustomersAction extends ActionSupport implements SessionAware, LoginRequired {

	static Logger logger = LogManager.getLogger(LookupJobAction.class);
	
	private Map<String, Object> sessionMap;
	
	@Autowired 
	private CustomerOrderService		customerOrderService;

	private String						reqGuid;
	private String 						customerId; 
	private CustWebDealerDto			custWebDealerDto;	
	private List<CustWebDealerCustDto> 	listCustWebDealerCustDto;

	public String display() {
		List<CustWebDealerCust> listCustWebDealerCust;
		CustWebDealer				custWebDealer;
		logger.debug("Request GuId is -> " + reqGuid);
		RequestObject reqObj 		= (RequestObject) sessionMap.get(reqGuid);
		customerId					= reqObj.getCustomerID();
		try{
			custWebDealer = customerOrderService.readDealer(customerId);
			custWebDealerDto = new CustWebDealerDtoBuilder().build(custWebDealer);
			listCustWebDealerCust = customerOrderService.listDealerCustomers(customerId);
			listCustWebDealerCustDto = new CustWebDealerCustDtoBuilder().build(listCustWebDealerCust);
		}	
		catch (HibernateException he){
			logger.error("Hibernation Exception caught" + he.toString() + " " + he.getMessage(), he);
			return ERROR;
		}
		catch (Exception e){
			logger.error("Exception caught" + e.toString() + " " + e.getMessage(), e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ListDealerCustomersAction.logger = logger;
	}

	public CustomerOrderService getCustomerOrderService() {
		return customerOrderService;
	}

	public void setCustomerOrderService(CustomerOrderService customerOrderService) {
		this.customerOrderService = customerOrderService;
	}

	public List<CustWebDealerCustDto> getListCustWebDealerCustDto() {
		return listCustWebDealerCustDto;
	}

	public void setListCustWebDealerCustDto(List<CustWebDealerCustDto> listCustWebDealerCustDto) {
		this.listCustWebDealerCustDto = listCustWebDealerCustDto;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public CustWebDealerDto getCustWebDealerDto() {
		return custWebDealerDto;
	}

	public void setCustWebDealerDto(CustWebDealerDto custWebDealerDto) {
		this.custWebDealerDto = custWebDealerDto;
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);
		
	}

}
