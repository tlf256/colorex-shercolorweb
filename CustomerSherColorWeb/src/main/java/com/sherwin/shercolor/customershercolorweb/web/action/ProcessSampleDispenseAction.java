package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.sherwin.shercolor.common.domain.CustWebBase;
import com.sherwin.shercolor.common.domain.CustWebCanTypes;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.common.service.UtilityService;
import com.sherwin.shercolor.customershercolorweb.web.model.DispenseItem;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;

@SuppressWarnings("serial")
public class ProcessSampleDispenseAction extends ActionSupport implements SessionAware, LoginRequired  {

	static Logger logger = LogManager.getLogger(ProcessSampleDispenseAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private FormulaInfo displayFormula;
	private List<DispenseItem> dispenseFormula;
	private int qtyDispensed;
	private TinterInfo tinter;
	private boolean siteHasTinter = false;
	private boolean siteHasPrinter;
	private boolean sessionHasTinter = false;
	private boolean accountIsDrawdownCenter = false;
	private boolean accountUsesRoomByRoom = false;
	private boolean tinterDoesBaseDispense = false;
	List<CustWebCanTypes> canTypesList = null;
	private Double factoryFill = null;
	private double sizeConversion;
	private double dispenseFloor;
	private DispenseItem baseDispense = null;
	
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
			
			// check if user has tinter configured
			List<String> tinterList = tinterService.listOfModelsForCustomerId(reqObj.getCustomerID(), null);
			if(tinterList.size() > 0) { 
				setSiteHasTinter(true);
			}
			
			// check if this account is a drawdown center and if it is profiled to use room by room option
			CustWebCustomerProfile profile = customerService.getCustWebCustomerProfile(reqObj.getCustomerID());
			if (profile != null) {
				String customerType = profile.getCustomerType();
				if (customerType != null && customerType.trim().toUpperCase().equals("DRAWDOWN")){
					setAccountIsDrawdownCenter(true);
				}
				setAccountUsesRoomByRoom(profile.isUseRoomByRoom());
			}
			
			String tinterModel = null;
			if (reqObj.getTinter() != null) {
				tinterModel = reqObj.getTinter().getModel();
			}
			
			// grab list of can types associated with current tinter model
			List<CustWebTinterProfileCanTypes> tinterCanList = tinterService.listOfCustWebTinterProfileCanTypesByTinterModel(tinterModel);
			canTypesList = new ArrayList<CustWebCanTypes>();
			for (CustWebTinterProfileCanTypes tinterCan : tinterCanList) {
				CustWebCanTypes canType = new CustWebCanTypes();
				canType = tinterService.getCustWebCanType(tinterCan.getCanType());
				canTypesList.add(canType);
			}
			
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
			
			// check tinter properties
			CustWebTinterProfile tinterProfile = tinterService.getCustWebTinterProfile(tinterModel);
			if (tinterProfile != null) {
				setTinterDoesBaseDispense(tinterProfile.isDoesDispenseBase());
				setDispenseFloor(tinterProfile.getColorantDispenseFloor());
			}
			
			// set up tinter colorant dispense info for small sample
			if(tinter != null && tinter.getModel() != null && !tinter.getModel().isEmpty() && tinter.getClrntSysId().equals(reqObj.getClrntSys())){
				setSessionHasTinter(true);
				logger.debug("About to get colorant map for " + reqObj.getCustomerID() + " " + tinter.getClrntSysId() + " " + tinter.getModel() + " " + tinter.getSerialNbr());
				HashMap<String,CustWebColorantsTxt> colorantMap = tinterService.getCanisterMap(reqObj.getCustomerID(), tinter.getClrntSysId(), tinter.getModel(), tinter.getSerialNbr());

				if(colorantMap != null && !colorantMap.isEmpty()){
					logger.debug("colorant map is not null");
					dispenseFormula = new ArrayList<DispenseItem>();

					for(FormulaIngredient ingr : displayFormula.getIngredients()){  
						DispenseItem addItem = new DispenseItem();
						addItem.setClrntCode(ingr.getTintSysId());
						addItem.setShots(0);	// shots get filled in on the page based on can type
						addItem.setUom(ingr.getShotSize());
						
						// Validating completeness of colorantMap data returned from DB. If not, send error msg back to SampleDispense.jsp
						if(!colorantMap.containsKey(ingr.getTintSysId())){
							addActionMessage(getText("processFormulaAction.selectedJobMissingColorant", new String[] {ingr.getTintSysId()} ));
							logger.error("Colorant map is incomplete for Colorant: " + ingr.getTintSysId() + " in Colorant System: " + ingr.getClrntSysId());
							retVal = INPUT; 
						} else {
							addItem.setPosition(colorantMap.get(ingr.getTintSysId()).getPosition());
							dispenseFormula.add(addItem);
						}
					}
					
					// look up code for the base being used in formula 
					CustWebBase custWebBase = productService.readCustWebBase(prodNbr);
					if (custWebBase != null) {
						String baseCode = custWebBase.getBaseCode();
						
						// check if base is loaded into any tinter canisters
						if (colorantMap.containsKey(baseCode)) {
							baseDispense = new DispenseItem();
							baseDispense.setClrntCode(baseCode);
							baseDispense.setPosition(colorantMap.get(baseCode).getPosition());
							if (dispenseFormula.get(0) != null) {
								baseDispense.setUom(dispenseFormula.get(0).getUom());
							}
						}
					}

					retVal = SUCCESS;

				} else {
					logger.debug("colorant map is null for " + reqObj.getCustomerID() + " " + tinter.getClrntSysId() + " " + tinter.getModel() + " " + tinter.getSerialNbr());
					retVal = ERROR;
				}
			} else {
				// no tinter, go on
				retVal = SUCCESS;
			}		
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
	
	public List<DispenseItem> getDispenseFormula() {
		return dispenseFormula;
	}

	public void setDispenseFormula(List<DispenseItem> dispenseFormula) {
		this.dispenseFormula = dispenseFormula;
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

	public boolean isSessionHasTinter() {
		return sessionHasTinter;
	}

	public void setSessionHasTinter(boolean sessionHasTinter) {
		this.sessionHasTinter = sessionHasTinter;
	}



	public boolean isTinterDoesBaseDispense() {
		return tinterDoesBaseDispense;
	}



	public void setTinterDoesBaseDispense(boolean tinterDoesBaseDispense) {
		this.tinterDoesBaseDispense = tinterDoesBaseDispense;
	}



	public DispenseItem getBaseDispense() {
		return baseDispense;
	}



	public void setBaseDispense(DispenseItem baseDispense) {
		this.baseDispense = baseDispense;
	}
}

