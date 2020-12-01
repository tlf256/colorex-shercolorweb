package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebCustomerProfile;
import com.sherwin.shercolor.common.domain.CustWebTinterProfile;
import com.sherwin.shercolor.common.domain.CustWebTinterProfileCanTypes;
import com.sherwin.shercolor.common.domain.CdsMiscCodes;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CustWebCanTypes;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.common.service.UtilityService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;

@SuppressWarnings("serial")
public class ProcessSampleDispenseAction extends ActionSupport implements SessionAware, LoginRequired  {

	static Logger logger = LogManager.getLogger(ProcessSampleDispenseAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private FormulaInfo displayFormula;
	private int qtyDispensed;
	private TinterInfo tinter;
	private boolean siteHasTinter = false;
	private boolean siteHasPrinter;
	private boolean accountIsDrawdownCenter = false;
	private boolean accountUsesRoomByRoom = false;
	List<CustWebCanTypes> canTypesList = null;
	private Double factoryFill = null;
	private double sizeConversion;
	private double dispenseFloor;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired 
	private TinterService tinterService;
	
	@Autowired 
	private ProductService productService;
	
	@Autowired 
	private UtilityService utilityService;

	
	public String display() {
		String retVal = null;
		
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			setDisplayFormula(reqObj.getDisplayFormula());
			setQtyDispensed(reqObj.getQuantityDispensed());
			setTinter(reqObj.getTinter());
			setSiteHasPrinter(reqObj.isPrinterConfigured());
			
			// check if this account is a drawdown center and if it is profiled to use room by room option
			CustWebCustomerProfile profile = customerService.getCustWebCustomerProfile(reqObj.getCustomerID());
			if (profile != null) {
				String customerType = profile.getCustomerType();
				if (customerType != null && customerType.trim().toUpperCase().equals("DRAWDOWN")){
					setAccountIsDrawdownCenter(true);
				}
				setAccountUsesRoomByRoom(profile.isUseRoomByRoom());
			}
			
			// get list of can types based on tinter model
			String tinterModel = null;
			if (reqObj.getTinter() != null) {
				tinterModel = reqObj.getTinter().getModel();
			}
			CustWebTinterProfile tinterProfile = tinterService.getCustWebTinterProfile(tinterModel);
			if (tinterProfile != null) {
				setDispenseFloor(tinterProfile.getColorantDispenseFloor());
			}
			
			// grab list of can types associated with current tinter model
			List<CustWebTinterProfileCanTypes> tinterCanList = tinterService.listOfCustWebTinterProfileCanTypesByTinterModel(tinterModel);
			canTypesList = new ArrayList<CustWebCanTypes>();
			for (CustWebTinterProfileCanTypes tinterCan : tinterCanList) {
				CustWebCanTypes canType = new CustWebCanTypes();
				canType = tinterService.getCustWebCanType(tinterCan.getCanType());
				canTypesList.add(canType);
			}
			
			// check if user has tinter configured
			List<String> tinterList = tinterService.listOfModelsForCustomerId(reqObj.getCustomerID(), null);
			if(tinterList.size() > 0) { siteHasTinter = true; }
			
			// get factory fill for the product
			String prodNbr = reqObj.getProdNbr();
			String clrntSysId = reqObj.getClrntSys();
			CdsProdCharzd cdsProdCharzd = productService.readCdsProdCharzd(prodNbr, clrntSysId);
			if (cdsProdCharzd != null) {
				setFactoryFill(cdsProdCharzd.getFactoryFill());
			}
			
			// look up size conversion for adapting the sample fill and colorant amount calculations
			String sizeCode = reqObj.getSizeCode();
			if (sizeCode != null) {
				// default size for calculation is one gallon, so only look up other size codes than 16
				if (sizeCode.equals("16")) {
					setSizeConversion(1.0);
				} else {
					String miscCode = sizeCode + "16";
					CdsMiscCodes miscCodeRecord = utilityService.getCdsMiscCodes("SZCNV", miscCode);
					if (miscCodeRecord != null) {
						setSizeConversion(Double.parseDouble(miscCodeRecord.getMiscName()));
					}
				}
			}
			retVal = SUCCESS;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			e.printStackTrace();
			retVal = ERROR;
		}
		return retVal;
	}
	
	
	

	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;			
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}
	
	public boolean isSiteHasTinter() {
		return siteHasTinter;
	}

	public void setSiteHasTinter(boolean siteHasTinter) {
		this.siteHasTinter = siteHasTinter;
	}

	public boolean isSiteHasPrinter() {
		return siteHasPrinter;
	}

	public void setSiteHasPrinter(boolean siteHasPrinter) {
		this.siteHasPrinter = siteHasPrinter;
	}

	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}

	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}

	public int getQtyDispensed() {
		return qtyDispensed;
	}

	public void setQtyDispensed(int qtyDispensed) {
		this.qtyDispensed = qtyDispensed;
	}

	public TinterInfo getTinter() {
		return tinter;
	}

	public void setTinter(TinterInfo tinter) {
		this.tinter = tinter;
	}

	public boolean isAccountIsDrawdownCenter() {
		return accountIsDrawdownCenter;
	}

	public void setAccountIsDrawdownCenter(boolean accountIsDrawdownCenter) {
		this.accountIsDrawdownCenter = accountIsDrawdownCenter;
	}
	
	public boolean isAccountUsesRoomByRoom() {
		return accountUsesRoomByRoom;
	}

	public void setAccountUsesRoomByRoom(boolean accountUsesRoomByRoom) {
		this.accountUsesRoomByRoom = accountUsesRoomByRoom;
	}
	
	public List<CustWebCanTypes> getCanTypesList() {
		return canTypesList;
	}

	public void setCanTypesList(List<CustWebCanTypes> canTypesList) {
		this.canTypesList = canTypesList;
	}

	public Double getFactoryFill() {
		return factoryFill;
	}

	public void setFactoryFill(Double factoryFill) {
		this.factoryFill = factoryFill;
	}

	public double getSizeConversion() {
		return sizeConversion;
	}

	public void setSizeConversion(double sizeConversion) {
		this.sizeConversion = sizeConversion;
	}

	public double getDispenseFloor() {
		return dispenseFloor;
	}

	public void setDispenseFloor(double dispenseFloor) {
		this.dispenseFloor = dispenseFloor;
	}
}

