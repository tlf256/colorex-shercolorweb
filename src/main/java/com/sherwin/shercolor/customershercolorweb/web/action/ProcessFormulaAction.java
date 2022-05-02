package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.sherwin.shercolor.common.domain.CdsMiscCodes;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CdsRoomList;
import com.sherwin.shercolor.common.domain.CustWebBase;
import com.sherwin.shercolor.common.domain.CustWebCanTypes;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.CustWebCustomerProfile;
import com.sherwin.shercolor.common.domain.CustWebTinterProfile;
import com.sherwin.shercolor.common.domain.CustWebTinterProfileCanTypes;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.CustWebTranCorr;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.DrawdownLabelService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.common.service.UtilityService;
import com.sherwin.shercolor.customershercolorweb.util.CorrectionInfoBuilder;
import com.sherwin.shercolor.customershercolorweb.util.CorrectionInfoBuilderImpl;
import com.sherwin.shercolor.customershercolorweb.util.ShercolorLabelPrintImpl;
import com.sherwin.shercolor.customershercolorweb.web.model.CorrectionInfo;
import com.sherwin.shercolor.customershercolorweb.web.model.DispenseItem;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;
import com.sherwin.shercolor.util.domain.SwMessage;


@SuppressWarnings("serial")
public class ProcessFormulaAction extends ActionSupport implements SessionAware, LoginRequired  {

	private DataInputStream inputStream;
	private byte[] data;
	static Logger logger = LogManager.getLogger(ProcessFormulaAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private FormulaInfo displayFormula;
	private List<DispenseItem> dispenseFormula;
	private List<DispenseItem> drawdownShotList;
	private int qtyDispensed;
	private int qtyOrdered;
	private TinterInfo tinter;
	private int recDirty;
	private boolean siteHasTinter;
	private boolean siteHasPrinter;
	private boolean displayDeltaEColumn = false;
	private boolean accountIsDrawdownCenter = false;
	private boolean accountUsesRoomByRoom = false;
	private boolean tinterDoesBaseDispense = false;
	private int dispenseBase;
	private boolean sessionHasTinter;
	private boolean midCorrection = false;
	private String printOrientation;
	private String printLabelType;
	private boolean printCorrectionLabel;
	private List<Map<String,Object>> shotList;
	private String canType;
	List<CustWebCanTypes> canTypesList = null;
	private String clrntAmtList;

	private TinterService tinterService;
	private TranHistoryService tranHistoryService;
	private List<CustWebTran> tranHistory;
	private DrawdownLabelService drawdownLabelService;
	private ColorMastService colorMastService;

	private List<CdsRoomList> roomByRoomList;
	private String roomByRoom;
	private double sizeConversion;
	private Double factoryFill = null;
	private double dispenseFloor;
	private DispenseItem baseDispense = null;
	private double sampleSize;
	private String colorNotes;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private FormulationService formulationService;
	
	@Autowired 
	private ProductService productService;
	
	@Autowired 
	private UtilityService utilityService;
	
	public String display(){
		String retVal = null;

		try {
			//logger.info("reqGuid is " + reqGuid);
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			displayFormula = reqObj.getDisplayFormula();
			qtyDispensed = reqObj.getQuantityDispensed();
			qtyOrdered = reqObj.getQuantityOrdered();
			tinter = reqObj.getTinter();
			colorNotes = reqObj.getColorNotes();
			setSiteHasPrinter(reqObj.isPrinterConfigured());
			
			// check if this account is a drawdown center or profiled to use room by room option
			lookupCustomerProfile();
			
			if (accountIsDrawdownCenter) {
				lookupFactoryFill();
				setupSizeConversion();
				
				if (tinter != null && tinter.getModel() != null && !tinter.getModel().isEmpty()){
					buildCanList();
					setupDrawdownTinter();
				} else {
					addActionMessage(getText("processFormulaAction.noTinterConfigured"));
				}
			}
			
			if(accountUsesRoomByRoom) {
				buildRoomList();
			}			

			// setup formula display messages since this now intercepts other actions from going directly to displayFormula.jsp
			if(reqObj.getDisplayMsgs()!=null){
				for(SwMessage item:reqObj.getDisplayMsgs()) {
					addActionMessage(Encode.forHtml(item.getMessage()));
				}
				// after all have been presented to user, clear the messages from the request object
				// if deltae warning, do not clear display message because they will not go directly to final formula display
				if(displayFormula.getDeltaEWarning()==null || displayFormula.getDeltaEWarning().isEmpty()) {
					reqObj.getDisplayMsgs().clear();
				}
			}

			// Check if correction in process
			List<CustWebTranCorr> tranCorrList = tranHistoryService.getCorrections(reqObj.getCustomerID(), reqObj.getControlNbr(), reqObj.getLineNbr());

			CorrectionInfoBuilder corrBuilder = new CorrectionInfoBuilderImpl(tranHistoryService, tinterService);

			CorrectionInfo corrInfo = corrBuilder.getCorrectionInfo(reqObj, tranCorrList);
			if(corrInfo.getCorrStatus().equalsIgnoreCase("MIDUNIT") || corrInfo.getCorrStatus().equalsIgnoreCase("MIDCYCLE")){
				midCorrection = true;
				addActionMessage(getText("processFormulaAction.currentlyBeingCorrected"));
			} else {
				midCorrection = false;
			}


			siteHasTinter = false;
			List<String> tinterList = tinterService.listOfModelsForCustomerId(reqObj.getCustomerID(), null);
			if(tinterList.size()>0) siteHasTinter = true;

			sessionHasTinter = false;
			// BMW: Update - 1/21/2018 - Added an additional conditional check here to prevent dispense
			// 							 when creating a job that uses a colorant system that does not
			//							 match the currently used tinter
			if(tinter!=null && tinter.getModel()!=null && !tinter.getModel().isEmpty() && tinter.getClrntSysId().equals(reqObj.getClrntSys())){
				sessionHasTinter = true;
				// Setup Tinter Colorant Dispense Info for Formula being displayed
				logger.debug("About to get colorant map for " + reqObj.getCustomerID() + " " + tinter.getClrntSysId() + " " + tinter.getModel() + " " + tinter.getSerialNbr());
				HashMap<String,CustWebColorantsTxt> colorantMap = tinterService.getCanisterMap(reqObj.getCustomerID(), tinter.getClrntSysId(), tinter.getModel(), tinter.getSerialNbr());

				logger.debug("back from tinterService");
				if(colorantMap!=null && !colorantMap.isEmpty()){
					logger.debug("colorant map is not null");
					if(dispenseFormula==null) dispenseFormula = new ArrayList<DispenseItem>();
					else dispenseFormula.clear();
					for(FormulaIngredient ingr : displayFormula.getIngredients()){
						
						logger.debug("pulling map info for " + ingr.getTintSysId());
						DispenseItem addItem = new DispenseItem();
						addItem.setClrntCode(ingr.getTintSysId());
						logger.debug(addItem.getClrntCode());
						addItem.setShots(ingr.getShots());
						logger.debug(addItem.getShots());
						addItem.setUom(ingr.getShotSize());
						logger.debug(addItem.getUom());
						//Validating completeness of colorantMap data returned from DB. If not, send error msg back to DisplayJobs.jsp
						if(!colorantMap.containsKey(ingr.getTintSysId())){
							tranHistory = tranHistoryService.getCustomerJobs(reqObj.getCustomerID());
							addActionMessage(getText("processFormulaAction.selectedJobMissingColorant", new String[] {ingr.getTintSysId()}));
							logger.error("Colorant map is incomplete for Colorant: " + ingr.getTintSysId() + " in Colorant System: " + ingr.getClrntSysId());
							return "errormsg";
						}
						else {
							addItem.setPosition(colorantMap.get(ingr.getTintSysId()).getPosition());
							dispenseFormula.add(addItem);
						}
					}
					
					if (accountIsDrawdownCenter) {
						// build DispenseItem lists for formula and base
						buildBaseDispense(colorantMap);
						buildDrawdownShotList();
						
						// set chosen can type and base dispense in case this is a saved job 
						setCanType(reqObj.getCanType());
						setDispenseBase(reqObj.getDispenseBase());
					}

					retVal = SUCCESS;

				} else {
					logger.debug("colorant map is null for " + reqObj.getCustomerID() + " " + tinter.getClrntSysId() + " " + tinter.getModel() + " " + tinter.getSerialNbr());
					retVal = ERROR;
				}
				
			} else {
				// no tinter go on...
				retVal = SUCCESS;

			}
			/*
			// Add DeltaE Warning to Display Msgs if there is a warning to display
			if (displayFormula.getAverageDeltaE() != null && displayFormula.getAverageDeltaE() > 1) {
				displayDeltaEColumn = true;
				displayFormula.setAverageDeltaE(Double.parseDouble(String.format("%,.2f", displayFormula.getAverageDeltaE())));
				addActionError(getText("compareColorsResult.deltaEgreaterThanOneWarning"));
			}*/
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			retVal = ERROR;
		}

		return retVal;
	}
	
	
	public void setupSizeConversion() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
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
	}
	
	
	public void buildCanList() {
		List<CustWebTinterProfileCanTypes> tinterCanList = tinterService.listOfCustWebTinterProfileCanTypesByTinterModel(tinter.getModel());
		canTypesList = new ArrayList<CustWebCanTypes>();
		
		for (CustWebTinterProfileCanTypes tinterCan : tinterCanList) {
			CustWebCanTypes canType = new CustWebCanTypes();
			canType = tinterService.getCustWebCanType(tinterCan.getCanType());
			canTypesList.add(canType);
		}
		
		// sort by sample size, largest to smallest 
		Collections.sort(canTypesList, Comparator.comparing(CustWebCanTypes::getSampleSize).reversed());
	}
	
	
	public void buildRoomList() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		String intExt = reqObj.getIntExt();
		setRoomByRoomList(utilityService.listCdsRoomsForListName(intExt));
		
		// add option for user to choose Other and enter in a custom name 
		CdsRoomList otherOption = new CdsRoomList();
		otherOption.setRoomUse("Other");
		roomByRoomList.add(otherOption);
		// if user has already saved a room to session, display it on the page
		setRoomByRoom(reqObj.getRoomByRoom());
	}
	
	
	private void lookupFactoryFill() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		// get factory fill for the product
		String prodNbr = reqObj.getProdNbr();
		String clrntSysId = reqObj.getClrntSys();
		CdsProdCharzd cdsProdCharzd = productService.readCdsProdCharzd(prodNbr, clrntSysId);
		if (cdsProdCharzd != null) {
			setFactoryFill(cdsProdCharzd.getFactoryFill());
		}
	}
	
	
	private void setupDrawdownTinter() {
		// check tinter properties
		CustWebTinterProfile tinterProfile = tinterService.getCustWebTinterProfile(tinter.getModel());
		if (tinterProfile != null) {
			setTinterDoesBaseDispense(tinterProfile.isDoesDispenseBase());
			setDispenseFloor(tinterProfile.getColorantDispenseFloor());
		}
	}
	
	
	private void buildBaseDispense(HashMap<String,CustWebColorantsTxt> colorantMap) {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		// check if product is profiled for base dispense 
		CustWebBase custWebBase = productService.readCustWebBase(reqObj.getProdNbr());
		if (custWebBase != null) {
			String baseCode = custWebBase.getBaseCode();
			
			// check if tinter is profiled for base dispense and base is loaded into a tinter canister
			if (colorantMap.containsKey(baseCode) && tinterDoesBaseDispense == true) {
				baseDispense = new DispenseItem();
				baseDispense.setClrntCode(baseCode);
				baseDispense.setPosition(colorantMap.get(baseCode).getPosition());
				if (dispenseFormula.get(0) != null) {
					baseDispense.setUom(dispenseFormula.get(0).getUom());
				}
				// if base dispense is available based on above conditions, set default true option unless they already declined it for this job
				if (reqObj.getDispenseBase() == -1) {
					reqObj.setDispenseBase(1);
				}
			}
		}
	}
	
	
	private void buildDrawdownShotList() {
		// build shotList for table display and to save transaction
		drawdownShotList = new ArrayList<DispenseItem>();
		for (FormulaIngredient ingr : displayFormula.getIngredients()) {
			DispenseItem drawdownItem = new DispenseItem(); 
			drawdownItem.setClrntCode(ingr.getTintSysId());
			drawdownItem.setClrntName(ingr.getName());
			drawdownItem.setShots(ingr.getShots());
			drawdownItem.setUom(ingr.getShotSize());
			drawdownShotList.add(drawdownItem);
		}
	}
	
	
	private void lookupCustomerProfile() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		CustWebCustomerProfile profile = customerService.getCustWebCustomerProfile(reqObj.getCustomerID());
		if (profile != null) {
			String customerType = profile.getCustomerType();
			
			if (customerType != null && customerType.trim().toUpperCase().equals("DRAWDOWN")){
				setAccountIsDrawdownCenter(true);
			}
			setAccountUsesRoomByRoom(profile.isUseRoomByRoom());
		}
	}
	
	
	
	public String displaySampleDispense() {
		String retVal = null;
		try {
			// wait a half second in case user is saving rooms field
			Thread.sleep(500);
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			setDisplayFormula(reqObj.getDisplayFormula());
			setQtyDispensed(reqObj.getQuantityDispensed());
			setQtyOrdered(reqObj.getQuantityOrdered());
			setTinter(reqObj.getTinter());
			setSiteHasPrinter(reqObj.isPrinterConfigured());
			
			// check if user has tinter configured
			List<String> tinterList = tinterService.listOfModelsForCustomerId(reqObj.getCustomerID(), null);
			if(tinterList.size() > 0) { 
				setSiteHasTinter(true);
			}
			
			// check how account is profiled and set up small sample tinting info
			lookupCustomerProfile();
			lookupFactoryFill();
			setupSizeConversion();
			if (tinter != null && tinter.getModel() != null && !tinter.getModel().isEmpty()) {
				buildCanList();
				setupDrawdownTinter();
			}
			
			CustWebCanTypes canProfile = tinterService.getCustWebCanType(reqObj.getCanType());
			if(canProfile != null) {
				setSampleSize(canProfile.getSampleSize());
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
					
					baseDispense = null;
					if (reqObj.getDispenseBase() == 1) {
						buildBaseDispense(colorantMap);
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
			retVal = ERROR;
		}
		return retVal;
	}
	
	
	

	public String execute() {

		try {
			return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String print() {

		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			ShercolorLabelPrintImpl printLabel = new ShercolorLabelPrintImpl(drawdownLabelService,customerService,colorMastService,formulationService);
			printLabel.CreateLabelPdf("label.pdf", reqObj, printLabelType, printOrientation, canType, clrntAmtList, printCorrectionLabel, shotList);
			inputStream = new DataInputStream( new FileInputStream(new File("label.pdf")));

			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String printAsJson() {
		FileInputStream fin = null;
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			ShercolorLabelPrintImpl printLabel = new ShercolorLabelPrintImpl(drawdownLabelService,customerService,colorMastService,formulationService);
			if (clrntAmtList != null) {
				clrntAmtList = clrntAmtList.replaceAll("\n", "");
				clrntAmtList = clrntAmtList.replaceAll("\t", "");
			}
			printLabel.CreateLabelPdf("label.pdf", reqObj, printLabelType, printOrientation, canType, clrntAmtList, printCorrectionLabel, shotList);
			File file = new File("label.pdf");
			fin = new FileInputStream(file);
			byte fileContent[] = new byte[(int)file.length()];

			// Reads up to certain bytes of data from this input stream into an array of bytes.
			fin.read(fileContent);
			setData(fileContent);
			inputStream = new DataInputStream( new FileInputStream(new File("label.pdf")));

			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String save() {

		try {
			return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	
	public String saveRoomByRoom() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			// save user's room choice into session
			if (roomByRoom != null) {
				reqObj.setRoomByRoom(roomByRoom);
				sessionMap.put(reqGuid, reqObj);
			}
			return SUCCESS;
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String saveCanType() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			// save user's can type into session
			if (canType != null) {
				reqObj.setCanType(canType);
				sessionMap.put(reqGuid, reqObj);
			}
			return SUCCESS;
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String saveDispenseBase() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			// save user's base dispense choice into session
			reqObj.setDispenseBase(dispenseBase);
			sessionMap.put(reqGuid, reqObj);
			
			return SUCCESS;
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
	}
	
	
	public String saveColorNotes() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			// save user's color notes entry into session
			reqObj.setColorNotes(colorNotes);
			return SUCCESS;
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String saveQuantityOrdered() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			// save user's order quantity entry into session
			reqObj.setQuantityOrdered(qtyOrdered);
			return SUCCESS;
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String displayUpdatedFormula() {
		try {
			recDirty = 1;
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			SwMessage saveReminder = new SwMessage();
			saveReminder.setSeverity(Level.INFO);
			saveReminder.setMessage(getText("processManualFormulaAction.changesNotYetSaved"));
			if(reqObj.getDisplayMsgs()==null) reqObj.setDisplayMsgs(new ArrayList<SwMessage>());
			reqObj.getDisplayMsgs().add(saveReminder);
			
			String retVal = this.display();
			return retVal;
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
	}
	

	
	public DataInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(DataInputStream inputStream) {
		this.inputStream = inputStream;
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

	public TinterService getTinterService() {
		return tinterService;
	}

	public void setTinterService(TinterService tinterService) {
		this.tinterService = tinterService;
	}

	public int getQtyDispensed() {
		return qtyDispensed;
	}

	public void setQtyDispensed(int qtyDispensed) {
		this.qtyDispensed = qtyDispensed;
	}
	
	public int getQtyOrdered() {
		return qtyOrdered;
	}

	public void setQtyOrdered(int qtyOrdered) {
		this.qtyOrdered = qtyOrdered;
	}

	public TinterInfo getTinter() {
		return tinter;
	}
	
	public void setTinter(TinterInfo tinter) {
		this.tinter = tinter;
	}

	public int getRecDirty() {
		return recDirty;
	}

	public void setRecDirty(int recDirty) {
		this.recDirty = recDirty;
	}

	public boolean isSessionHasTinter() {
		return sessionHasTinter;
	}
	
	public void setSessionHasTinter(boolean sessionHasTinter) {
		this.sessionHasTinter = sessionHasTinter;
	}

	public boolean isSiteHasTinter() {
		return siteHasTinter;
	}
	
	public void setSiteHasTinter(boolean siteHasTinter) {
		this.siteHasTinter = siteHasTinter;
	}

	public TranHistoryService getTranHistoryService() {
		return tranHistoryService;
	}

	public void setTranHistoryService(TranHistoryService tranHistoryService) {
		this.tranHistoryService = tranHistoryService;
	}

	public boolean isMidCorrection() {
		return midCorrection;
	}

	public List<CustWebTran> getTranHistory() {
		return tranHistory;
	}


	public byte[] getData() {
		return data;
	}

	@TypeConversion(converter="com.sherwin.shercolor.common.web.model.StringToByteArrayConverter")
	public void setData(byte[] data) {
		this.data = data;
	}

	public boolean isSiteHasPrinter() {
		return siteHasPrinter;
	}

	public void setSiteHasPrinter(boolean siteHasPrinter) {
		this.siteHasPrinter = siteHasPrinter;
	}

	public boolean isDisplayDeltaEColumn() {
		return displayDeltaEColumn;
	}

	public void setDisplayDeltaEColumn(boolean displayDeltaEColumn) {
		this.displayDeltaEColumn = displayDeltaEColumn;
	}

	public DrawdownLabelService getDrawdownLabelService() {
		return drawdownLabelService;
	}

	public void setDrawdownLabelService(DrawdownLabelService drawdownLabelService) {
		this.drawdownLabelService = drawdownLabelService;
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
	
	public int isDispenseBase() {
		return dispenseBase;
	}

	public void setDispenseBase(int dispenseBase) {
		this.dispenseBase = dispenseBase;
	}

	public List<CdsRoomList> getRoomByRoomList() {
		return roomByRoomList;
	}

	public void setRoomByRoomList(List<CdsRoomList> roomByRoomList) {
		this.roomByRoomList = roomByRoomList;
	}

	public String getRoomByRoom() {
		return roomByRoom;
	}

	public void setRoomByRoom(String roomByRoom) {
		this.roomByRoom = roomByRoom;
	}

	public String getPrintOrientation() {
		return printOrientation;
	}

	public String getPrintLabelType() {
		return printLabelType;
	}

	public void setPrintOrientation(String printOrientation) {
		this.printOrientation = printOrientation;
	}

	public void setPrintLabelType(String printLabelType) {
		this.printLabelType = printLabelType;
	}

	public ColorMastService getColorMastService() {
		return colorMastService;
	}

	public void setColorMastService(ColorMastService colorMastService) {
		this.colorMastService = colorMastService;
	}

	public String getCanType() {
		return canType;
	}

	public String getClrntAmtList() {
		return clrntAmtList;
	}

	public void setCanType(String canType) {
		this.canType = canType;
	}

	public void setClrntAmtList(String clrntAmtList) {
		this.clrntAmtList = clrntAmtList;
	}

	public boolean isPrintCorrectionLabel() {
		return printCorrectionLabel;
	}

	public void setPrintCorrectionLabel(boolean printCorrectionLabel) {
		this.printCorrectionLabel = printCorrectionLabel;
	}

	public List<Map<String, Object>> getShotList() {
		return shotList;
	}

	public void setShotList(List<Map<String, Object>> shotList) {
		this.shotList = shotList;
	}
	
	
	public List<CustWebCanTypes> getCanTypesList() {
		return canTypesList;
	}

	public void setCanTypesList(List<CustWebCanTypes> canTypesList) {
		this.canTypesList = canTypesList;
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

	public DispenseItem getBaseDispense() {
		return baseDispense;
	}

	public void setBaseDispense(DispenseItem baseDispense) {
		this.baseDispense = baseDispense;
	}

	public boolean isTinterDoesBaseDispense() {
		return tinterDoesBaseDispense;
	}

	public void setTinterDoesBaseDispense(boolean tinterDoesBaseDispense) {
		this.tinterDoesBaseDispense = tinterDoesBaseDispense;
	}

	public Double getFactoryFill() {
		return factoryFill;
	}

	public void setFactoryFill(Double factoryFill) {
		this.factoryFill = factoryFill;
	}

	public List<DispenseItem> getDrawdownShotList() {
		return drawdownShotList;
	}

	public void setDrawdownShotList(List<DispenseItem> drawdownShotList) {
		this.drawdownShotList = drawdownShotList;
	}


	public double getSampleSize() {
		return sampleSize;
	}


	public void setSampleSize(double sampleSize) {
		this.sampleSize = sampleSize;
	}


	public String getColorNotes() {
		return colorNotes;
	}


	public void setColorNotes(String colorNotes) {
		this.colorNotes = colorNotes;
	}
	
}
