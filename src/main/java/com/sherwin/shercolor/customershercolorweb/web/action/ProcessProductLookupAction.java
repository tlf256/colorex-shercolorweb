package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class ProcessProductLookupAction extends ActionSupport implements SessionAware, LoginRequired  {

	private transient Map<String, Object> sessionMap;
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessProductLookupAction.class);
	private String reqGuid;

	@Autowired
	private transient ProductService productService;

	private transient List<CdsProd> productList = new ArrayList<>();

	public String loadProductLookup() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			buildLookupTable();

			sessionMap.put(reqGuid, reqObj);
		    return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	private void buildLookupTable() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

		String [] intBases = new String[1];
		String [] extBases = new String[1];

		String intBasesStr = reqObj.getIntBases();
		String extBasesStr = reqObj.getExtBases();

		boolean buildTable = false;
		
		if (intBasesStr != null && intBasesStr.length() > 0 && !intBasesStr.equals("PACKAGE COLOR")) {
			buildTable = true;
			intBases = intBasesStr.split(",");
		}
		if (extBasesStr != null && extBasesStr.length() > 0 && !extBasesStr.equals("PACKAGE COLOR")) {
			buildTable = true;
			extBases = extBasesStr.split(",");
		}
		if (buildTable) {
			productList = productService.productAutocompleteCompatibleBase("", intBases, extBases, reqObj.getCustomerID());
		}
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

	public List<CdsProd> getProductList() {
		return productList;
	}

	public void setProductList(List<CdsProd> productList) {
		this.productList = productList;
	}

}