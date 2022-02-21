package com.sherwin.shercolor.customershercolorweb.web.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.logging.log4j.Level;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsFbProd;
import com.sherwin.shercolor.common.domain.CdsMiscCodes;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CdsTsfOver;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.common.domain.OeFormInputRequest;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.common.service.ProductColorService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.common.service.UtilityService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;


public class ProcessProductChangeAction extends ActionSupport implements SessionAware, LoginRequired  {
	
	private Map<String, Object> sessionMap;
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessProductAction.class);
	private String partialProductNameOrId; 
	private String message;
	private String salesNbr;
	private String reqGuid;
	private String userProduct;
	private String oldProdNbr;
	private String newProdNbr;
	private String oldSizeCode;
	private String newSizeCode;
	private double oldTintStrength;
	private double newTintStrength;
	private boolean ableToReformulate;
	private boolean ableToRematch;
	private boolean requireVinylPrompt;
	// user response to the prompt
	private boolean makeVinylSafe;
	// product's availability for vinyl-safe
	private boolean newProdVinylExclude;
	private boolean checkVSOverride = true;
	private String userIllum;
	private String selectedProdFam;
	private Map<String, Map<String,String>> colorProdFamilies = new HashMap<String, Map<String,String>>();
	private Map<String, FormulaInfo> formulas = new HashMap<String, FormulaInfo>();
	
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductColorService productColorService;
	@Autowired
	private CustomerService customerService;
	@Autowired 
	private UtilityService utilityService;
	@Autowired
	private FormulationService formulationService;
	
	
	
	public String lookupProductOptions() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			setOldProdNbr(reqObj.getProdNbr());
			setOldSizeCode(reqObj.getSizeCode());
			// validates new sales number the user picked
			String retVal = this.validateProduct();
			
			if (retVal.equalsIgnoreCase("SUCCESS")) {
				// check for size change
				PosProd posProd = productService.readPosProd(salesNbr);
				if (posProd != null) {
					setNewProdNbr(posProd.getProdNbr());
					setNewSizeCode(posProd.getSzCd());
				}
				
				// check for projected curve and color eye readings 
				setAbleToReformulate(checkReformulation());
				setAbleToRematch(checkRematch());
				// check if we need to ask the user whether they want vinyl-safe
				if (ableToReformulate || ableToRematch) {
					setRequireVinylPrompt(checkVinylSafe(getNewProdNbr()));
				}
				// check whether the user is changing from a vinyl-safe formulation to a product that doesn't support it
				if (checkVSOverride) {
					// new product is not available for vinyl safe, but old formulation was vinyl safe 
					if ((!newProdVinylExclude && !requireVinylPrompt) && reqObj.isVinylExclude()){
						// leave flag set to verify that the user wants this
					} else {
						setCheckVSOverride(false);
					}
				}
				// Check tint strength for both products
				oldTintStrength = checkTintStrength(reqObj.getSalesNbr(), oldProdNbr) * 100;
				newTintStrength = checkTintStrength(salesNbr, newProdNbr) * 100;
			}
			return retVal;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			setMessage("Unknown Error");
			return ERROR;
		}
	}
	
	
	public String validateProduct() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		String colorComp = reqObj.getColorComp();
		String colorId = reqObj.getColorID();
		
		// convert entered value to salesNbr (could be entered as SalesNbr or UPC)
		String enteredSalesNbr = productService.getSalesNbr(partialProductNameOrId);
		if (enteredSalesNbr == null){
			this.setSalesNbr(partialProductNameOrId);
		} else {
			this.setSalesNbr(enteredSalesNbr);
		}
		 
		//Call the validation.  If it validates successfully, call a read and get the data.
		List<SwMessage> errlist = productService.validateProduct(salesNbr);
		if (errlist.size()>0) {
			Boolean gotARealError = false;
			for(SwMessage item:errlist) {
				if (item.getSeverity()!=null) {
					if (item.getSeverity().equals(Level.ERROR)) {
						addFieldError("partialProductNameOrId", item.getMessage());
						gotARealError = true;
					}
				}
			}
			if (gotARealError) {
				return INPUT;
			} else {
				// call the product color validation
				List<SwMessage> errmsg = productColorService.validate(colorComp, colorId, salesNbr);
				if (errmsg.size() > 0) {
					Boolean gotARealError2 = false;
					for(SwMessage item:errmsg) {
						if (item.getSeverity()!=null) {
							if (item.getSeverity().equals(Level.ERROR)) {
								addFieldError("partialProductNameOrId", item.getMessage());
								gotARealError2 = true;
							} else {
								//check if this is a warning on the same salesNbr, and we have already displayed a warning for this sales number
								if (reqObj.isValidationWarning() && reqObj.getValidationWarningSalesNbr() != null && reqObj.getValidationWarningSalesNbr().equals(salesNbr)) {
									// let the user go past the warning
								} else {
									addActionMessage(item.getMessage());
									reqObj.setValidationWarning(true);
									sessionMap.put(reqGuid, reqObj);
									gotARealError2 = true;
								}
							}
						}
					}
					if (reqObj.isValidationWarning()) {
						reqObj.setValidationWarningSalesNbr(salesNbr);
					}
					if (gotARealError2) {
						return INPUT;
					}
				}
			}
		} else {
			// call the product color validation
			List<SwMessage> errmsg = productColorService.validate(colorComp, colorId, salesNbr);
			if (errmsg.size() > 0) {
				Boolean gotARealError = false;
				for(SwMessage item:errmsg) {
					if (item.getSeverity()!=null) {
						if (item.getSeverity().equals(Level.ERROR)) {
							addFieldError("partialProductNameOrId", item.getMessage());
							gotARealError = true;
						} else {
							if (reqObj.isValidationWarning() && reqObj.getValidationWarningSalesNbr() != null && reqObj.getValidationWarningSalesNbr().equals(salesNbr)) {
								// let the user go past the warning
							} else {
								addActionMessage(item.getMessage());
								reqObj.setValidationWarning(true);
								sessionMap.put(reqGuid, reqObj);
								gotARealError = true;
							}
						}
					}
				}
				if (reqObj.isValidationWarning()) {
					reqObj.setValidationWarningSalesNbr(salesNbr);
				}
				if (gotARealError) {
					return INPUT;
				}
			}
		} 
		sessionMap.put(reqGuid, reqObj);
		return SUCCESS;
	}

	
	public String updateProductNoAdjustment() {
		try {
			if (salesNbr != null && !salesNbr.equals("")) {
				setProductInfoIntoSession();				
				return SUCCESS;
			} else {
				addActionError(getText("processProductChangeAction.couldNotBeUpdated"));
				return INPUT;
			}
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			setMessage("Unknown Error");
			return ERROR;
		}
	}

	
	public String updateProductSizeChange() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid); 
			String retVal = INPUT;
			
			if (reqObj != null && reqObj.getDisplayFormula() != null) {
				PosProd posProd = productService.readPosProd(salesNbr);
				String oldSizeCode = reqObj.getSizeCode();
				String newSizeCode = posProd.getSzCd();
				String miscCode = oldSizeCode + newSizeCode;
				CdsMiscCodes miscCodeRecord = utilityService.getCdsMiscCodes("SZCNV", miscCode);
				
				if (miscCodeRecord != null && miscCodeRecord.getMiscName() != null) {
					Double sizeRatio = 0.0;
					try {
						sizeRatio = Double.parseDouble(miscCodeRecord.getMiscName());
					} catch (NumberFormatException e) {
						logger.debug("miscCodeRecord couldn't be parsed");
					}
					if (sizeRatio != 0) {
						FormulaInfo newDisplayFormula = formulationService.scaleFormulaBySize(reqObj.getDisplayFormula(), sizeRatio);						
						newDisplayFormula.setSalesNbr(salesNbr);
						
						retVal = processFormulaValidation(newDisplayFormula, null, false);
						
						if (retVal.equalsIgnoreCase("SUCCESS")) {
							reqObj.setDisplayFormula(newDisplayFormula);
							setProductInfoIntoSession();
						}
					
					// end checking sizeRatio parsing
					} else {
						addActionError(getText("processProductChangeAction.couldNotUpdateSize"));
					}
				// end miscCodeRecord nullcheck
				} else {
					logger.info("error fetching miscCodeRecord");
					addActionError(getText("processProductChangeAction.couldNotUpdateSize"));
				}
			// end reqObj and displayFormula nullcheck	
			}  else {
				logger.info("problem looking up formula in session");
				addActionError(getText("processProductChangeAction.couldNotUpdateSize"));
			}
			return retVal;
			
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			setMessage("Unknown Error");
			return ERROR;
		}
	}

	
	private boolean checkReformulation() {
		boolean retVal = false;
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		FormulaInfo displayFormula = reqObj.getDisplayFormula();
		String prodNbr = "";
		
		if (reqObj.getColorType().equalsIgnoreCase("CUSTOM")){
			CustWebParms custWebParms = customerService.getDefaultCustWebParms(reqObj.getCustomerID()); 
			custWebParms.setClrntSysId(reqObj.getClrntSys());
			Double[] projCurve = formulationService.projectCurve(displayFormula, custWebParms);
			
			// check the rules on whether we can reformulate
			boolean lessThanSix = false;
			boolean isWhite = false;
			boolean hasWhite = false;
			if (displayFormula.getIngredients() != null && displayFormula.getIngredients().size() < 6) {
				lessThanSix = true;
			}
			PosProd posProd = productService.readPosProd(salesNbr); 
			if (posProd != null) {
				prodNbr = posProd.getProdNbr();
			}
			CdsProdCharzd cdsProdCharzd = productService.readCdsProdCharzd(prodNbr, reqObj.getClrntSys());
			CdsProdCharzd oldCdsProdCharzd = productService.readCdsProdCharzd(reqObj.getProdNbr(), reqObj.getClrntSys());
			if (cdsProdCharzd != null) {
				isWhite = cdsProdCharzd.getIsWhite();
			}
			for (FormulaIngredient colorant : displayFormula.getIngredients()) {
				String name = colorant.getName();
				if (name.contains("WHITE") || name.contains("White") || name.contains("WHT")) {
					hasWhite = true;
					break;
				}
			}
			if (lessThanSix && oldCdsProdCharzd != null && cdsProdCharzd != null && (!isWhite || (isWhite && !hasWhite)) && projCurve != null){
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	
	private FormulationResponse lookupReformulation() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		FormulationResponse reformulation = null;
		CustWebParms custWebParms = customerService.getDefaultCustWebParms(reqObj.getCustomerID()); 
		custWebParms.setClrntSysId(reqObj.getClrntSys());
		
		FormulaInfo displayFormula = reqObj.getDisplayFormula();
		Double[] projCurve = formulationService.projectCurve(displayFormula, custWebParms);
		if (projCurve != null) {
			double[] curveArray = Stream.of(projCurve).mapToDouble(Double::doubleValue).toArray();
			
			OeFormInputRequest oeRequest = new OeFormInputRequest();
			oeRequest.setClrntSysId(reqObj.getClrntSys());
			oeRequest.setColorComp(reqObj.getColorComp());
			oeRequest.setColorId(reqObj.getColorID());
			oeRequest.setSalesNbr(salesNbr); 
			oeRequest.setColorCurve(curveArray);
			oeRequest.setProjection(false);
			oeRequest.setCorrection(false);
			oeRequest.setGetProdFamily(false);
			if (requireVinylPrompt == true) {
				oeRequest.setVinylSafe(makeVinylSafe);
			} else {
				checkVinylSafe(salesNbr);
				oeRequest.setVinylSafe(newProdVinylExclude);
			}
		
			// set default daylight for custom
			String[] illums = new String[3];
			illums[0] = "D65";
			illums[1] = "A";
			illums[2] = "F2";
			
			oeRequest.setIllum(illums);
			reformulation = formulationService.formulate(oeRequest, custWebParms);
		}
		
		return reformulation;
	}
	
	
	public String updateProductReformulate() {
		try{
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			String retVal = INPUT;
			List<SwMessage> swmsgList = new ArrayList<SwMessage>();
			FormulationResponse reformulation = lookupReformulation();
			
			// we shouldn't be seeing errors if we've gotten this far, but just in case,
			if (reformulation.getStatus().equals("ERROR")) {
				for(SwMessage item : reformulation.getMessages()) {
					if(item.getSeverity().isInRange(Level.FATAL, Level.ERROR)){
						addFieldError("partialProductNameOrId", item.getMessage());
					}
					retVal = INPUT;
				}
			} else {
				if (reformulation.getStatus().equals("COMPLETE")) {
					retVal = SUCCESS;
					// get SwMessages from the FormulationResponse
					swmsgList = reformulation.getMessages();
					FormulaInfo displayFormula = reformulation.getFormulas().get(0);
					retVal = processFormulaValidation(displayFormula, swmsgList, true);
					if (retVal.equalsIgnoreCase("SUCCESS")) {
						reqObj.setDisplayFormula(reformulation.getFormulas().get(0));
						setProductInfoIntoSession();
					}
				}
			}
			return retVal;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			setMessage("Unknown Error");
			return ERROR;
		}
	}

	
	private boolean checkRematch() {
		boolean retVal = false;
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		FormulaInfo displayFormula = reqObj.getDisplayFormula();
		
		// check if it's a custom match and we have a curve
		if ((reqObj.getColorType().equalsIgnoreCase("CUSTOMMATCH") || reqObj.getColorType().equalsIgnoreCase("SAVEDMEASURE"))
				&& (reqObj.getCurveArray() != null || (displayFormula != null && displayFormula.getMeasuredCurve() != null))) {
			
			retVal = true;
		}
		return retVal;
	}
	
	
	private FormulationResponse lookupRematch() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		FormulationResponse rematch = null;
		double[] dblCurve = null;
			
		if (reqObj.getCurveArray() != null) {
			dblCurve = new double[40];
			for (int x=0; x<40; x++) {
				dblCurve[x] = reqObj.getCurveArray()[x].doubleValue();
			}
		}

		if (dblCurve != null) {
			CustWebParms custWebParms = customerService.getDefaultCustWebParms(reqObj.getCustomerID()); 
			custWebParms.setClrntSysId(reqObj.getClrntSys());
			
			OeFormInputRequest oeRequest = new OeFormInputRequest();
			oeRequest.setClrntSysId(reqObj.getClrntSys());
			oeRequest.setColorComp(reqObj.getColorComp());
			oeRequest.setColorId(reqObj.getColorID());
			oeRequest.setSalesNbr(salesNbr); 
			oeRequest.setColorCurve(dblCurve);
			oeRequest.setProjection(false);
			oeRequest.setCorrection(false);
			oeRequest.setGetProdFamily(false);
			if (requireVinylPrompt == true) {
				oeRequest.setVinylSafe(makeVinylSafe);
			} else {
				checkVinylSafe(salesNbr);
				oeRequest.setVinylSafe(newProdVinylExclude);
			}
		
			String[] illums = new String[3];
			switch (userIllum) {
				case "A":
					illums[0] = "A";
					illums[1] = "D65";
					illums[2] = "F2";
					break;
				case "D65":
					illums[0] = "D65";
					illums[1] = "A";
					illums[2] = "F2";
					break;
				case "F2":
					illums[0] = "F2";
					illums[1] = "A";
					illums[2] = "D65";
					break;
			}
			oeRequest.setIllum(illums);
			rematch = formulationService.prodFamilyFormulate(oeRequest, custWebParms);
		}
		
		return rematch;
	}
	
	
	public String updateProductRematch() {
		try{
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			List<SwMessage> swmsgList = new ArrayList<SwMessage>();
			String retVal = INPUT;
			
			// check if we arrived back here from the user selecting a prod family
			FormulationResponse prevFormResponse = (FormulationResponse) reqObj.getFormResponse();
			boolean prodFamWasSelected = false;
			if (prevFormResponse != null && selectedProdFam != null && !selectedProdFam.equals("")) {	
				
				for(FormulaInfo item : prevFormResponse.getFormulas()) {
					if (item.getSalesNbr().equals(selectedProdFam)) {
						prodFamWasSelected = true;
						swmsgList = prevFormResponse.getMessages();
						retVal = processFormulaValidation(item, swmsgList, true);
						
						if (retVal.equalsIgnoreCase("SUCCESS")) {
							setSalesNbr(selectedProdFam);
							setProductInfoIntoSession();
							reqObj.setDisplayFormula(item); 
							reqObj.setProductChosenFromDifferentBase(true);
						}
					}
				}
			}
			
			// we still need to get the formulas
			if (!prodFamWasSelected) {
				FormulationResponse rematch = lookupRematch();
				if (rematch.getFormulas().size() > 0) {
					reqObj.setFormResponse(rematch);
				}
				
				switch (rematch.getStatus()) {
				case "PICKPRODFAM":
					// check if we need to show better performance in different base screen
					Optional<CdsProd> cdsProd;
					String quality = "";
					String base    = "";
					String comment = getText("global.comment");
					DecimalFormat dffmt = new DecimalFormat("###.##");
					
					for(FormulaInfo item : rematch.getFormulas()) {
						Map<String, String> rowData = new HashMap<String, String>();
						cdsProd = productService.readCdsProd(item.getSalesNbr());
						quality = cdsProd.map(CdsProd::getQuality).orElse("");
						base = cdsProd.map(CdsProd::getBase).orElse("");
						
						if (comment.equals(getText("processProdFamilyAction.bestPerformance"))) {
							comment = getText("processProdFamilyAction.productEntered");
							formulas.put("prodEntered", item);
						}
						if (comment.equals(getText("global.comment"))) {
							comment = getText("processProdFamilyAction.bestPerformance");
							formulas.put("bestPerformance", item);
						}
						
						rowData.put("prodNbr", item.getProdNbr());
						rowData.put("quality", quality); 
						rowData.put("base", base);
						rowData.put("deltaE", dffmt.format(item.getAverageDeltaE()));
						rowData.put("contrastRatio", item.getContrastRatioThick().toString());
						rowData.put("comment", comment);
						rowData.put("salesNbr", item.getSalesNbr());
						
						if (comment.equals(getText("processProdFamilyAction.bestPerformance"))) {
							colorProdFamilies.put("bestPerformance", rowData);
						} else {
							colorProdFamilies.put("prodEntered", rowData);
						}
					}
					retVal = SUCCESS;
					break;
				
				case "ERROR":
					for(SwMessage item : rematch.getMessages()) {
						if(item.getSeverity().isInRange(Level.FATAL, Level.ERROR)){
							addFieldError("partialProductNameOrId", item.getMessage());
						}
						retVal = INPUT;
					}
					break;
	
				case "COMPLETE":
					retVal = SUCCESS;
					// get SwMessages from the FormulationResponse
					swmsgList = rematch.getMessages();
					FormulaInfo displayFormula = rematch.getFormulas().get(0);
					retVal = processFormulaValidation(displayFormula, swmsgList, true);
					
					if (retVal.equalsIgnoreCase("SUCCESS")) {
						reqObj.setDisplayFormula(rematch.getFormulas().get(0));
						setProductInfoIntoSession();
					}
					break;
				}
			}
			return retVal;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			setMessage("Unknown Error");
			return ERROR;
		}
	}
	
	
	private double checkTintStrength(String salesNbr, String prodNbr) {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		double tsf = 0.0;
		
		Optional<CdsProd> cdsProd = productService.readCdsProd(salesNbr);
		String prodComp = cdsProd.map(CdsProd::getProdComp).orElse("");
		if (prodComp.startsWith("SW")) {
			prodComp = "SW";
		}
		
		List<CdsFbProd> cfbList = productService.listCdsFbProd(prodComp, salesNbr);
		if (cfbList != null && !cfbList.isEmpty()) {
			tsf = cfbList.get(0).getTsf();
			// make sure TSF values are all the same, otherwise we can't determine tint strength for the salesNbr
			for (CdsFbProd item : cfbList) {
				if (item.getTsf() != tsf) {
					tsf = 0;
					break;
				}
			}
		// if no CdsFbProd record, look at CdsProd
		} else {
			tsf = cdsProd.map(CdsProd::getTsf).orElse(0.0);
		}
		/*
		// check for a TSF override -- commented out for now since the table is only on Dev and still being built
		String clrntSys = reqObj.getClrntSys();
		CdsTsfOver tsfOver = productService.readCdsTsfOver(prodNbr, clrntSys);
		if (tsfOver != null) {
			tsf = tsfOver.getTsf();
		}*/
		return tsf;
	}
	
	
	public String updateProductTintStrength() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid); 
			String retVal = INPUT;
			if (reqObj != null && reqObj.getDisplayFormula() != null) {
				
				Double tsRatio = newTintStrength / oldTintStrength;
				if (tsRatio != 0 && !tsRatio.isNaN() && oldTintStrength != 0) {
					FormulaInfo newDisplayFormula = formulationService.scaleFormulaBySize(reqObj.getDisplayFormula(), tsRatio);						
					newDisplayFormula.setSalesNbr(salesNbr);
					
					retVal = processFormulaValidation(newDisplayFormula, null, false);
					
					if (retVal.equalsIgnoreCase("SUCCESS")) {
						reqObj.setDisplayFormula(newDisplayFormula);
						setProductInfoIntoSession();
					}
				
				// end checking sizeRatio parsing
				} else {
					addActionError(getText("processProductChangeAction.couldNotUpdateTintStrength"));
				}
			// end reqObj and displayFormula nullcheck	
			}  else {
				logger.info("problem looking up formula in session");
				addActionError(getText("processProductChangeAction.couldNotUpdateTintStrength"));
			}
			return retVal;
			
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			setMessage("Unknown Error");
			return ERROR;
		}
	}
	
	
	public String updateProductTintStrengthSize() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid); 
			String retVal = INPUT;
			if (reqObj != null && reqObj.getDisplayFormula() != null) {
				
				Double tsRatio = newTintStrength / oldTintStrength;
				Double sizeRatio = 0.0;
				String miscCode = oldSizeCode + newSizeCode;
				CdsMiscCodes miscCodeRecord = utilityService.getCdsMiscCodes("SZCNV", miscCode);
				
				if (miscCodeRecord != null && miscCodeRecord.getMiscName() != null) {
					try {
						sizeRatio = Double.parseDouble(miscCodeRecord.getMiscName());
					} catch (NumberFormatException e) {
						logger.debug("miscCodeRecord couldn't be parsed");
					}
				}
				// scale by both
				if (tsRatio != 0 && !tsRatio.isNaN() && oldTintStrength != 0 && sizeRatio != 0) {
					double finalRatio = tsRatio * sizeRatio;
					FormulaInfo newDisplayFormula = formulationService.scaleFormulaBySize(reqObj.getDisplayFormula(), finalRatio);						
					newDisplayFormula.setSalesNbr(salesNbr);
					
					retVal = processFormulaValidation(newDisplayFormula, null, false);
					
					if (retVal.equalsIgnoreCase("SUCCESS")) {
						reqObj.setDisplayFormula(newDisplayFormula);
						setProductInfoIntoSession();
					}
				// end checking ratio parsing
				} else {
					addActionError(getText("processProductChangeAction.couldNotUpdateTintStrengthSize"));
				}
			// end reqObj and displayFormula nullcheck	
			}  else {
				logger.info("problem looking up formula in session");
				addActionError(getText("processProductChangeAction.couldNotUpdateTintStrengthSize"));
			}
			return retVal;
			
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			setMessage("Unknown Error");
			return ERROR;
		}
	}
	
	
	
	// set vinylExclude field to indicate if we are making a vinyl safe formula, 
	// and return boolean for whether we need to prompt user for input
	private boolean checkVinylSafe(String newProd) {
		 RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		 CdsProdCharzd charzdRec = productService.readCdsProdCharzd(newProd, reqObj.getClrntSys());
		 
		 if (charzdRec == null) {
			 setNewProdVinylExclude(false);
			 return false;
		 }
		 // if we made it here, we have a charzdRec on file. Check the vinyl field - if not null, we need to prompt
		 if (charzdRec.getVinyl_excludeClrnt() == null) {
			 setNewProdVinylExclude(false);
			return false;
		 }
		 
		 // check to see if the color uses a P series primer other than P1. If so, it should not be allowed to be vinyl sided.
		 if (reqObj.getPrimerId() != null && !reqObj.getPrimerId().equals("P1")) {
			 setNewProdVinylExclude(false);
			return false;
		 }
		 
		 // check to see if the color is a "vinyl only" color.  If so, we can set the VinylExclude flag to true and skip the prompt
		 if (reqObj.isColorVinylOnly()) {
			 setNewProdVinylExclude(true);
			return false;
		 }
		 
		 return true;
	}
	
	
	private String processFormulaValidation(FormulaInfo displayFormula, List<SwMessage> swmsgList, boolean oeFormulation) {
		String retVal = SUCCESS;
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		CustWebParms custWebParms = customerService.getDefaultCustWebParms(reqObj.getCustomerID()); 
		List<SwMessage> validationMsgs = new ArrayList<SwMessage>();
		String defaultClrntSys = custWebParms.getClrntSysId();
		
		if (oeFormulation) {
			// for reformulation and rematch, send in oe response messages and check for validation warnings	
			validationMsgs = formulationService.validateFormulation(displayFormula, swmsgList);
		} else {
			// otherwise do manual formulation checks
			validationMsgs = formulationService.manualFormulationWarnings(displayFormula);
		}
		
		// if the colorant system used does not match the customer's default colorant system, add a warning.
		if (!defaultClrntSys.equals(reqObj.getClrntSys())) {
			SwMessage csMsg = new SwMessage();
			csMsg.setCode("NONDFLTCLRNTSYS");
			csMsg.setMessage(getText("generateFormulaAction.formulaRequiresClrntSys", new String[]{reqObj.getClrntSys(), reqObj.getClrntSys()}));
			csMsg.setSeverity(Level.WARN);
			validationMsgs.add(csMsg);
		}
		
		int errorCount = 0;
		for(SwMessage item:validationMsgs) {
			if(item.getSeverity().isInRange(Level.FATAL, Level.ERROR)){
				addActionError(item.getMessage());
				logger.debug("logging action error: " + item.getMessage());
				errorCount++;
				retVal = INPUT;
			} else if(item.getSeverity().isInRange(Level.WARN, Level.WARN)){
				// check if we have already displayed warnings for this sales number
				if (reqObj.isValidationWarning() && reqObj.getValidationWarningSalesNbr() != null && reqObj.getValidationWarningSalesNbr().equals(salesNbr)) {
					// let the user go past the warning
				} else {
					// show the warning to user
					addActionMessage(item.getMessage());
					reqObj.setValidationWarning(true);
					retVal = INPUT;
				}
			}
		}
		// set up override if there are warnings but no errors
		if (reqObj.isValidationWarning() && errorCount == 0) {
			reqObj.setValidationWarningSalesNbr(salesNbr);
		}
		if (retVal.equalsIgnoreCase("SUCCESS")) {
			List<SwMessage> canLabelMsgs = formulationService.canLabelFormulationWarnings(reqObj.getDisplayFormula(), swmsgList);
			reqObj.setCanLabelMsgs(canLabelMsgs);
			// post the validation message list to the request object for use in printing.
			reqObj.setDisplayMsgs(validationMsgs);
		}
		
		return retVal;
	}
	

	public void setProductInfoIntoSession() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		PosProd posProd = productService.readPosProd(salesNbr);
	 	if(posProd!=null && posProd.getUpc()!=null) reqObj.setUpc(posProd.getUpc());
	 	else reqObj.setUpc("");
	 	
	 	Optional<CdsProd> goodProd = productService.readCdsProd(salesNbr);
		//Check for CdsProd, if not, build prepCmt for PosProd
	 	String[] prepCmt = goodProd.map(CdsProd::getPrepComment)
                .map(x -> x.split("-"))
                .orElse(new String[]{posProd.getProdNbr(),posProd.getSzCd()});
		 
		//set the successful information into the request object.
		reqObj.setSalesNbr(salesNbr);
		reqObj.setProdNbr(prepCmt[0]);
		reqObj.setFinish(goodProd.map(CdsProd::getFinish).orElse(""));
		reqObj.setKlass(goodProd.map(CdsProd::getKlass).orElse(""));
		reqObj.setIntExt(goodProd.map(CdsProd::getIntExt).orElse(""));
		reqObj.setBase(goodProd.map(CdsProd::getBase).orElse(""));
		reqObj.setComposite(goodProd.map(CdsProd::getComposite).orElse(""));
		reqObj.setQuality(goodProd.map(CdsProd::getQuality).orElse(""));
		if(reqObj.getColorType().equalsIgnoreCase("CUSTOM")){
			if(reqObj.getIntExt().equalsIgnoreCase("INTERIOR")){
				reqObj.setIntBases(reqObj.getBase());
			} else {
				reqObj.setExtBases(reqObj.getBase());
			}
		}
		
		reqObj.setSizeCode(prepCmt[1]);
		reqObj.setSizeText(productService.getSizeText(reqObj.getSizeCode()));
		reqObj.setValidationWarning(false);
		reqObj.setValidationWarningSalesNbr("");
		if (requireVinylPrompt == true) {
			reqObj.setVinylExclude(makeVinylSafe);
		} else {
			checkVinylSafe(salesNbr);
			reqObj.setVinylExclude(newProdVinylExclude);
		}
		sessionMap.put(reqGuid, reqObj);
	}
	
	

	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getPartialProductNameOrId() {
		return partialProductNameOrId;
	}

	public void setPartialProductNameOrId(String partialProductNameOrId) {
		this.partialProductNameOrId = partialProductNameOrId;
	}
	
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public String getSalesNbr() {
		return salesNbr;
	}

	public void setSalesNbr(String salesNbr) {
		this.salesNbr = Encode.forHtml(salesNbr);
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public String getUserProduct() {
		return userProduct;
	}

	public void setUserProduct(String userProduct) {
		this.userProduct = userProduct;
	}

	public String getOldProdNbr() {
		return oldProdNbr;
	}

	public void setOldProdNbr(String oldProdNbr) {
		this.oldProdNbr = oldProdNbr;
	}

	public String getNewProdNbr() {
		return newProdNbr;
	}

	public void setNewProdNbr(String newProdNbr) {
		this.newProdNbr = newProdNbr;
	}

	public String getOldSizeCode() {
		return oldSizeCode;
	}

	public void setOldSizeCode(String oldSizeCode) {
		this.oldSizeCode = oldSizeCode;
	}

	public String getNewSizeCode() {
		return newSizeCode;
	}

	public void setNewSizeCode(String newSizeCode) {
		this.newSizeCode = newSizeCode;
	}
	
	public double getOldTintStrength() {
		return oldTintStrength;
	}

	public void setOldTintStrength(double oldTintStrength) {
		this.oldTintStrength = oldTintStrength;
	}

	public double getNewTintStrength() {
		return newTintStrength;
	}

	public void setNewTintStrength(double newTintStrength) {
		this.newTintStrength = newTintStrength;
	}
	
	public boolean isAbleToReformulate() {
		return ableToReformulate;
	}

	public void setAbleToReformulate(boolean ableToReformulate) {
		this.ableToReformulate = ableToReformulate;
	}

	public boolean isRequireVinylPrompt() {
		return requireVinylPrompt;
	}

	public void setRequireVinylPrompt(boolean requireVinylPrompt) {
		this.requireVinylPrompt = requireVinylPrompt;
	}

	public boolean isMakeVinylSafe() {
		return makeVinylSafe;
	}

	public void setMakeVinylSafe(boolean makeVinylSafe) {
		this.makeVinylSafe = makeVinylSafe;
	}

	public boolean isAbleToRematch() {
		return ableToRematch;
	}

	public void setAbleToRematch(boolean ableToRematch) {
		this.ableToRematch = ableToRematch;
	}

	public void setNewProdVinylExclude(boolean newProdVinylExclude) {
		this.newProdVinylExclude = newProdVinylExclude;
	}

	public String getUserIllum() {
		return userIllum;
	}

	public void setUserIllum(String userIllum) {
		this.userIllum = userIllum;
	}
	
	public Map<String, Map<String,String>> getColorProdFamilies() {
		return colorProdFamilies;
	}

	public void setColorProdFamilies(Map<String, Map<String,String>> colorProdFamilies) {
		this.colorProdFamilies = colorProdFamilies;
	}

	public Map<String, FormulaInfo> getFormulas() {
		return formulas;
	}

	public void setFormulas(Map<String, FormulaInfo> formulas) {
		this.formulas = formulas;
	}

	public String getSelectedProdFam() {
		return selectedProdFam;
	}

	public void setSelectedProdFam(String selectedProdFam) {
		this.selectedProdFam = selectedProdFam;
	}

	public boolean isCheckVSOverride() {
		return checkVSOverride;
	}

	public void setCheckVSOverride(boolean checkVSOverride) {
		this.checkVSOverride = checkVSOverride;
	}
	

}