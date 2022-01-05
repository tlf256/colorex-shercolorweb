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
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsMiscCodes;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CustWebPackageColor;
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
import org.springframework.stereotype.Component;


@Component
public class ProcessProductChangeAction extends ActionSupport implements SessionAware, LoginRequired  {
	
	private Map<String, Object> sessionMap;
	private static final long serialVersionUID = 1L;
	static Logger logger = LoggerFactory.getLogger(ProcessProductAction.class);
	private String partialProductNameOrId; 
	private String message;
	private String salesNbr;
	private String reqGuid;
	private String OVERRIDEWARNMSG;
	private String userProduct;
	private String oldProdNbr;
	private String oldSizeCode;
	private String newProdNbr;
	private String newSizeCode;
	private boolean ableToReformulate;
	//private boolean ableToRematch;
	private boolean requireVinylPrompt;
	private boolean makeVinylSafe;
	//private String userIllum;
	//private Map<String, Map<String,String>> colorProdFamilies = new HashMap<String, Map<String,String>>();
	//private List<FormulaInfo> bothFormulas = new ArrayList<FormulaInfo>();
	
	
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
	
	
	
	
	public String validateProduct() {
		 RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		 OVERRIDEWARNMSG = getText("processProductAction.clickNext");
		 String colorComp = reqObj.getColorComp();
		 String colorId = reqObj.getColorID();
		 
		 //convert entered value to salesNbr (could be entered as SalesNbr or UPC)
		 String enteredSalesNbr = productService.getSalesNbr(partialProductNameOrId);
		 if(enteredSalesNbr==null){
			 this.setSalesNbr(partialProductNameOrId);
		 } else {
			 this.setSalesNbr(enteredSalesNbr);
		 }
		 
		 // check if color/product is a package color
		 // if true, skip validation
		 CustWebPackageColor custWebPkgClr = productColorService.getPackageColor(colorComp, colorId, salesNbr);
		 
		 if(custWebPkgClr != null) {
			 reqObj.setPackageColor(true);
			 if(custWebPkgClr.isTintable()) {
				 reqObj.setPkgClrTintable(true);
			 } else {
				 reqObj.setPkgClrTintable(false);
			 }
		 } else {
			 reqObj.setPackageColor(false);
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
					//probably got a single result back that indicated successful validation.
					//call the product color validation.
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
									if (reqObj.isValidationWarning() && reqObj.getValidationWarningSalesNbr().equals(salesNbr)) {
										// let the user go past the warning
									} else {
										addActionMessage(item.getMessage() + " " + OVERRIDEWARNMSG);
										reqObj.setValidationWarning(true);
										reqObj.setValidationWarningSalesNbr(salesNbr);
										sessionMap.put(reqGuid, reqObj);
										gotARealError2 = true;
									}
								}
							}
						}
						if (gotARealError2) {
							return INPUT;
						}
					}
				}
			} else {
				//call the product color validation.
				List<SwMessage> errmsg = productColorService.validate(colorComp, colorId, salesNbr);
				if (errmsg.size() > 0) {
					Boolean gotARealError = false;
					for(SwMessage item:errmsg) {
						if (item.getSeverity()!=null) {
							if (item.getSeverity().equals(Level.ERROR)) {
								addFieldError("partialProductNameOrId", item.getMessage());
								gotARealError = true;
							} else {
								addActionMessage(item.getMessage());
								reqObj.setValidationWarning(true);
								reqObj.setValidationWarningSalesNbr(salesNbr);
								sessionMap.put(reqGuid, reqObj);
								gotARealError = true;
							}
						}
					}
					if (gotARealError) {
						return INPUT;
					}
				}
			}
		 } 
		 sessionMap.put(reqGuid, reqObj);
		 return SUCCESS;
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
		sessionMap.put(reqGuid, reqObj);
	}
	


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
				if (ableToReformulate) {
					setRequireVinylPrompt(checkVinylSafe(getNewProdNbr()));
				}
				/*
				setAbleToRematch(checkRematch());
				if (ableToRematch) {
					setRequireVinylPrompt(checkVinylSafe(getNewProdNbr()));
				}*/
			}
						
			return retVal;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
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
			boolean lessThanSeven = false;
			boolean isWhite = false;
			boolean hasWhite = false;
			if (displayFormula.getIngredients() != null && displayFormula.getIngredients().size() < 7) {
				lessThanSeven = true;
			}
			PosProd posProd = productService.readPosProd(salesNbr);
			if (posProd != null) {
				prodNbr = posProd.getProdNbr();
			}
			CdsProdCharzd cdsProdCharzd = productService.readCdsProdCharzd(prodNbr, reqObj.getClrntSys());
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
			if (lessThanSeven && cdsProdCharzd != null && (!isWhite || (isWhite && !hasWhite)) && projCurve != null){
				retVal = true;
			}
		}
		
		return retVal;
	}
	
	/*
	private boolean checkRematch() {
		boolean retVal = false;
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		FormulaInfo displayFormula = reqObj.getDisplayFormula();
		
		// check if it's a custom match and we have a curve
		//** might need to check for all zeros?
		if ((reqObj.getColorType().equalsIgnoreCase("CUSTOMMATCH") || reqObj.getColorType().equalsIgnoreCase("SAVEDMEASURE"))
				&& (reqObj.getCurveArray() != null || (displayFormula != null && displayFormula.getMeasuredCurve() != null))) {
			
			retVal = true;
		}
		return retVal;
	}*/
	
	
	// set vinylExclude field to indicate if we are making a vinyl safe formula, 
	// and return boolean for whether we need to prompt user for input
	private boolean checkVinylSafe(String newProd) {
		 RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		 CdsProdCharzd charzdRec = productService.readCdsProdCharzd(newProd, reqObj.getClrntSys());
		 boolean retVal = false;
		 
		 if (charzdRec == null) {
			reqObj.setVinylExclude(false);
			 return false;
		 }
		 // if we made it here, we have a charzdRec on file. Check the vinyl field - if not null, we need to prompt
		 if (charzdRec.getVinyl_excludeClrnt() == null) {
			reqObj.setVinylExclude(false);
			return false;
		 }
		 
		 // check to see if the color uses a P series primer other than P1. If so, it should not be allowed to be vinyl sided.
		 if (reqObj.getPrimerId() != null && !reqObj.getPrimerId().equals("P1")) {
			reqObj.setVinylExclude(false);
			return false;
		 }
		 
		 // check to see if the color is a "vinyl only" color.  If so, we can set the VinylExclude flag to true and skip the prompt
		 if (reqObj.isColorVinylOnly()) {
			reqObj.setVinylExclude(true);
			return false;
		 }
		 
		 return true;
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
			oeRequest.setVinylSafe(reqObj.isVinylExclude());
			if (requireVinylPrompt == true) {
				oeRequest.setVinylSafe(makeVinylSafe);
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
			CustWebParms custWebParms = customerService.getDefaultCustWebParms(reqObj.getCustomerID()); 
			String defaultClrntSys = custWebParms.getClrntSysId();
			List<SwMessage> validationMsgs = new ArrayList<SwMessage>();
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
					// check for warnings and set those as well.			
					validationMsgs = formulationService.validateFormulation(displayFormula, swmsgList);
					
					// if the colorant system used does not match the customer's default colorant system, add a warning.
					if (!defaultClrntSys.equals(reqObj.getClrntSys())) {
						SwMessage csMsg = new SwMessage();
						csMsg.setCode("NONDFLTCLRNTSYS");
						csMsg.setMessage(getText("generateFormulaAction.formulaRequiresClrntSys", 
								new String[] { reqObj.getClrntSys(), reqObj.getClrntSys() }));
						csMsg.setSeverity(Level.WARN);
						validationMsgs.add(csMsg);
					}
					
					for(SwMessage item:validationMsgs) {
						// check if we have already displayed warnings for this sales number
						if (reqObj.isValidationWarning() && reqObj.getValidationWarningSalesNbr() != null && reqObj.getValidationWarningSalesNbr().equals(salesNbr)) {
							// let the user go past the warning
						} else {
							// show the warning to user
							addActionMessage(item.getMessage());
							logger.debug("logging action message: " + item.getMessage());
							reqObj.setValidationWarning(true);
							retVal = INPUT;
						}
					}
					if (reqObj.isValidationWarning()) {
						reqObj.setValidationWarningSalesNbr(salesNbr);
					}
				}
			}
			
			if (retVal.equalsIgnoreCase("SUCCESS")) {
				reqObj.setDisplayFormula(reformulation.getFormulas().get(0));
				setProductInfoIntoSession();
				
				List<SwMessage> canLabelMsgs = formulationService.canLabelFormulationWarnings(reqObj.getDisplayFormula(), swmsgList);
				reqObj.setCanLabelMsgs(canLabelMsgs);
				// post the validation message list to the request object for use in printing.
				reqObj.setDisplayMsgs(validationMsgs);
			}
			
			return retVal;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			setMessage("Unknown Error");
			return ERROR;
		}
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
												
						// check for formulation warnings
						List<SwMessage> allMsgs = formulationService.manualFormulationWarnings(newDisplayFormula);
						int errorCount = 0;
						int newWarningCount = 0;
						for(SwMessage item:allMsgs) {
							if(item.getSeverity().isInRange(Level.FATAL, Level.ERROR)){
								addActionError(item.getMessage());
								errorCount++;
								logger.debug("logging action error: " + item.getMessage());
							} else {
								if(item.getSeverity().isInRange(Level.WARN, Level.WARN)){
									
									// check if we have already displayed warnings for this sales number
									if (reqObj.isValidationWarning() && reqObj.getValidationWarningSalesNbr()!=null && reqObj.getValidationWarningSalesNbr().equals(salesNbr)) {
										// let the user go past the warning
									} else {
										// show the warning to user
										addActionMessage(item.getMessage());
										logger.debug("logging action message: " + item.getMessage());
										reqObj.setValidationWarning(true);
										newWarningCount++;
									}
								}
							}
						}
						
						// post the validation message list to the request object for use in printing.
						List<SwMessage> canLabelMsgs = formulationService.canLabelFormulationWarnings(newDisplayFormula);
						reqObj.setCanLabelMsgs(canLabelMsgs);
						
						
						if (errorCount > 0) {
							// make user resolve errors first and override warnings on next pass
							reqObj.setValidationWarning(false);
							reqObj.setValidationWarningSalesNbr("");
						} else {
							// otherwise set up to override the warnings
							if (reqObj.isValidationWarning()) {
								reqObj.setValidationWarningSalesNbr(salesNbr);
							}
						}
						
						// set new product info in session because there aren't any errors or non-overridden warnings
						if (errorCount + newWarningCount == 0) {
							reqObj.setDisplayFormula(newDisplayFormula);
							setProductInfoIntoSession();
							sessionMap.put(reqGuid, reqObj);
							retVal = SUCCESS;
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

	
	
/*	
	private FormulationResponse lookupRematch() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		FormulationResponse rematch = null;
		double[] dblCurve = null;
			
		//** test both to make sure either can convert both types to double
		if (reqObj.getCurveArray() != null) {
			dblCurve = new double[40];
			for (int x=0; x<40; x++) {
				dblCurve[x] = reqObj.getCurveArray()[x].doubleValue();
			}
		}
		if (reqObj.getDisplayFormula() != null && reqObj.getDisplayFormula().getMeasuredCurve() != null) {
			dblCurve = new double[40];
			for (int x=0; x<40; x++) {
				dblCurve[x] = reqObj.getDisplayFormula().getMeasuredCurve()[x].doubleValue();
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
			oeRequest.setVinylSafe(reqObj.isVinylExclude());
			if (requireVinylPrompt == true) {
				oeRequest.setVinylSafe(makeVinylSafe);
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
			String retVal = INPUT;
			CustWebParms custWebParms = customerService.getDefaultCustWebParms(reqObj.getCustomerID()); 
			String defaultClrntSys = custWebParms.getClrntSysId();
			FormulationResponse rematch = lookupRematch();
			List<SwMessage> validationMsgs = new ArrayList<SwMessage>();
			List<SwMessage> swmsgList = new ArrayList<SwMessage>();
			
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
						bothFormulas.add(item);
					}
					if (comment.equals(getText("global.comment"))) {
						comment = getText("processProdFamilyAction.bestPerformance");
						bothFormulas.add(item);
					}
					
					rowData.put("prodNbr", item.getProdNbr());
					rowData.put("quality", quality); 
					rowData.put("base", base);
					rowData.put("deltaE", dffmt.format(item.getAverageDeltaE()));
					rowData.put("contrastRatio", item.getContrastRatioThick().toString());
					rowData.put("comment", comment);
					
					if (comment.equals(getText("processProdFamilyAction.bestPerformance"))) {
						colorProdFamilies.put("bestPerformance", rowData);
					} else {
						colorProdFamilies.put("prodEntered", rowData);
					}
				}
				retVal = SUCCESS;
				break;
			
			case "ERROR":
			// we shouldn't be seeing errors if we've gotten this far, but just in case,
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
				// check for warnings and set those as well.			
				validationMsgs = formulationService.validateFormulation(displayFormula, swmsgList);
				
				// if the colorant system used does not match the customer's default colorant system, add a warning.
				if (!defaultClrntSys.equals(reqObj.getClrntSys())) {
					SwMessage csMsg = new SwMessage();
					csMsg.setCode("NONDFLTCLRNTSYS");
					csMsg.setMessage(getText("generateFormulaAction.formulaRequiresClrntSys", 
							new String[] { reqObj.getClrntSys(), reqObj.getClrntSys() }));
					csMsg.setSeverity(Level.WARN);
					validationMsgs.add(csMsg);
				}
				
				for(SwMessage item:validationMsgs) {
					// check if we have already displayed warnings for this sales number
					if (reqObj.isValidationWarning() && reqObj.getValidationWarningSalesNbr() != null && reqObj.getValidationWarningSalesNbr().equals(salesNbr)) {
						// let the user go past the warning
					} else {
						// show the warning to user
						addActionMessage(item.getMessage());
						logger.debug("logging action message: " + item.getMessage());
						reqObj.setValidationWarning(true);
						retVal = INPUT;
					}
				}
				if (reqObj.isValidationWarning()) {
					reqObj.setValidationWarningSalesNbr(salesNbr);
				}
				
				//** maybe move this out into own function if we need to also run this later when return from user picking prod
				if (retVal.equalsIgnoreCase("SUCCESS")) {
					reqObj.setDisplayFormula(rematch.getFormulas().get(0));
					setProductInfoIntoSession();
					
					List<SwMessage> canLabelMsgs = formulationService.canLabelFormulationWarnings(reqObj.getDisplayFormula(), swmsgList);
					reqObj.setCanLabelMsgs(canLabelMsgs);
					// post the validation message list to the request object for use in printing.
					reqObj.setDisplayMsgs(validationMsgs);
				}
				break;
			}
			
			return retVal;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			setMessage("Unknown Error");
			return ERROR;
		}
	}*/
	
	

	
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

	public String getOldSizeCode() {
		return oldSizeCode;
	}

	public void setOldSizeCode(String oldSizeCode) {
		this.oldSizeCode = oldSizeCode;
	}

	public String getNewProdNbr() {
		return newProdNbr;
	}

	public void setNewProdNbr(String newProdNbr) {
		this.newProdNbr = newProdNbr;
	}

	public String getNewSizeCode() {
		return newSizeCode;
	}

	public void setNewSizeCode(String newSizeCode) {
		this.newSizeCode = newSizeCode;
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
/*
	public boolean isAbleToRematch() {
		return ableToRematch;
	}

	public void setAbleToRematch(boolean ableToRematch) {
		this.ableToRematch = ableToRematch;
	}

	public String getUserIllum() {
		return userIllum;
	}

	public void setUserIllum(String userIllum) {
		this.userIllum = userIllum;
	}
	
	public List<FormulaInfo> getBothFormulas() {
		return bothFormulas;
	}

	public void setBothFormulas(List<FormulaInfo> theFormulas) {
		this.bothFormulas = theFormulas;
	}
	
	public Map<String, Map<String,String>> getColorProdFamilies() {
		return colorProdFamilies;
	}

	public void setColorProdFamilies(Map<String, Map<String,String>> colorProdFamilies) {
		this.colorProdFamilies = colorProdFamilies;
	}
	*/

}