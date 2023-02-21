package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CdsFbColor;
import com.sherwin.shercolor.common.domain.CdsFbProd;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CustWebPackageColor;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.service.ColorBaseService;
import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.ProductColorService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.autoComplete;
import com.sherwin.shercolor.util.domain.SwMessage;
import org.springframework.stereotype.Component;


@Component
public class ProcessProductAction extends ActionSupport implements SessionAware, LoginRequired  {

	private Map<String, Object> sessionMap;
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessProductAction.class);
	private String partialProductNameOrId;
	private List<autoComplete> options;
	private String message;
	private String colorComp;
	private String colorID;
	private String colorName;
	private String salesNbr;
	private String reqGuid;
	private String OVERRIDEWARNMSG;
	private String forceProd;
	private String shorthandProdNbr;
	
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductColorService productColorService;
	@Autowired
	private ColorService colorService;
	@Autowired
	private ColorBaseService colorBaseService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ColorMastService colorMastService;

	
	public String checkForceProd() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			colorComp = reqObj.getColorComp();
			colorID = reqObj.getColorID();
			CdsColorMast colorMast = colorMastService.read(colorComp, colorID);
	
			if (colorMast != null) {
				setForceProd(colorMast.getForceProd());
			}
			return SUCCESS;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	
	public String execute() {
		
		 try {
			 RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			 OVERRIDEWARNMSG = getText("processProductAction.clickNext");
			 String colorComp = reqObj.getColorComp();
			 String colorId = reqObj.getColorID();
			 
			 //validate the product - need call to productService once complete
			 
			 //convert entered value to salesNbr (could be entered as SalesNbr or UPC)
			 String enteredSalesNbr = productService.getSalesNbr(partialProductNameOrId);
			 if(enteredSalesNbr == null){
				 this.setSalesNbr(partialProductNameOrId);
			 } else {
				 this.setSalesNbr(enteredSalesNbr);
			 }
			 
			// check if color/product is a package color
			checkPackageColor(colorComp, colorId);
			// if not, do validation checks
			if(!reqObj.isPackageColor()) {
				//Call the validation.  If it validates successfully, call a read and get the data.
				boolean gotARealError = false;
				
				logger.debug("Validating product info in master product table...");
				List<SwMessage> errlist = productService.validateProductInPOS(salesNbr);
				if(!errlist.isEmpty()) {
					gotARealError = checkMessageSeverity(errlist, reqObj);
					if(gotARealError) {
						return INPUT;
					}
				}
				
				logger.debug("Validating product size code in CDS...");
				errlist = productService.validateProductSizeCode(salesNbr, reqObj.getColorType());
				if(!errlist.isEmpty()) {
					gotARealError = checkMessageSeverity(errlist, reqObj);
					//Custom Manual colors can use products whose size code is not defined in SherColor.
					if(gotARealError) {
						if (reqObj.isValidationWarning()) {
							reqObj.setValidationWarningSalesNbr(salesNbr);
						}
						return INPUT;
					}
				}
				
				logger.debug("Validating product color info...");
				errlist = productColorService.validate(colorComp, colorId, salesNbr);
				if(!errlist.isEmpty()) {
					gotARealError = checkMessageSeverity(errlist, reqObj);
					if(gotARealError) {
						if (reqObj.isValidationWarning()) {
							reqObj.setValidationWarningSalesNbr(salesNbr);
						}
						return INPUT;
					} 
				}
				
			 }
			 
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
		    return SUCCESS;
		     
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	
	// check if a color / product combo is a package color, and whether it is tintable
	public void checkPackageColor(String colorComp, String colorId) {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		reqObj.setPackageColor(false);
		reqObj.setPkgClrTintable(false);
		
		if (colorComp.equals("SHERWIN-WILLIAMS")) {
			CdsColorMast cdsColorMast = colorMastService.read(colorComp, colorId);
			Optional<CdsProd> cdsProd = productService.readCdsProd(salesNbr);
			String cdsProdBase = cdsProd.map(CdsProd::getBase).orElse("");
			
			// package colors have matching value in CdsColorMast(colorName) and CdsProd(Base), or HRB exception
			if (cdsColorMast != null && cdsColorMast.getColorName() != null) {
				String colorName = cdsColorMast.getColorName();
				// we found a match
				if (colorName.equals(cdsProdBase) || (cdsProdBase.equals("HRB") && colorName.equals("HIGH REFLECTIVE WHITE"))){
					
					// look up colorant system in session or default colorant sys for this user
					String clrntSys = reqObj.getClrntSys();
					if ((clrntSys == null || clrntSys.isEmpty()) && reqObj.getTinter() != null) {
						clrntSys = reqObj.getTinter().getClrntSysId();
					}
					CustWebParms cwp = customerService.getDefaultCustWebParms(reqObj.getCustomerID());
					String prodComp = "";
					if (cwp != null) {
						if ((clrntSys == null || clrntSys.isEmpty())) {
							clrntSys = cwp.getClrntSysId();
						}
						prodComp = cwp.getProdComp();
					}
					
					// make sure we don't have a formula book entry for this combination
					if (!productColorService.hasFormulaBookEntry(colorComp, colorId, salesNbr, clrntSys, prodComp)) {
						reqObj.setPackageColor(true);
						reqObj.setPkgClrTintable(true);
						
						// if color has a restricted product list, the product must be included in it to be allowed through
						if(cdsColorMast.getForceProd() != null) {
							reqObj.setPackageColor(false);
							reqObj.setPkgClrTintable(false);
							List<CdsProd> list = productService.getForcedProducts(cdsColorMast.getForceProd());
							for(CdsProd cp : list) {
								// user's chosen product is on the restricted product list so it's valid
								if (cp.getSalesNbr().equals(salesNbr)) {
									reqObj.setPackageColor(true);
									reqObj.setPkgClrTintable(true);
								}
							}
						}
					}
				}
			}
			
			// if didn't find a match, check for exception in CustWebPackageColor table
			if (!reqObj.isPackageColor()) {
				CustWebPackageColor custWebPkgClr = productColorService.getPackageColor(colorComp, colorId, salesNbr); 	
				if(custWebPkgClr != null) {
					reqObj.setPackageColor(true);
					if(custWebPkgClr.isTintable()) {
						reqObj.setPkgClrTintable(true);
					} else {
						reqObj.setPkgClrTintable(false);
					}
				}
			}
		}
	 }
	
	private boolean checkMessageSeverity(List<SwMessage> errlist, RequestObject reqObj) {
		boolean result = false;
		for(SwMessage item:errlist) {
			if (item.getSeverity() != null && item.getSeverity().equals(Level.ERROR)) {
				logger.debug("Validation Error received: {}", item.getMessage());
				addFieldError("partialProductNameOrId", item.getMessage());
				result = true;
			} else {
				logger.debug("Validation Warning code: {} and message: {}", item.getCode(), item.getMessage());
				if (reqObj.isValidationWarning() && reqObj.getValidationWarningSalesNbr() != null && reqObj.getValidationWarningSalesNbr().equals(salesNbr)) {
					// let the user go past the warning
					logger.debug("Validation warning is already flagged for this salesNbr {}. letting it through", salesNbr);
					result = false;
				} else {
					logger.debug("Validation warning has not been flagged for this salesNbr {}. Notifying the user", salesNbr);
					addActionMessage(item.getMessage());
					reqObj.setValidationWarning(true);
					result = true;
				}
			}
		}
		return result;
	}
	
	
	public String listProducts() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		String intBases = reqObj.getIntBases();
		String extBases = reqObj.getExtBases();
		String colorType = reqObj.getColorType();
		String intBasesList[] = {""};
		String extBasesList[] = {""};
		if (intBases != null) {	
			intBasesList = intBases.split(",");
		}
		if (extBases != null) { 
			extBasesList = extBases.split(",");
		}
		
		/* 03/16/2022 RMF Add Suggested Products for Colors with Forced Products */
		try {
			colorComp = reqObj.getColorComp();
			colorID = reqObj.getColorID();
			CdsColorMast colorMast = colorMastService.read(colorComp, colorID);
	
			if (colorMast != null) {
				String forceProd = colorMast.getForceProd();
				if (forceProd != null) {
					/* 4/11/2022 KGH -- autocomplete lookup for matching records, otherwise returns entire force prod list */
					List<CdsProd> list = productService.autocompleteForceProd(partialProductNameOrId.toUpperCase(), forceProd);
					if (list != null && list.size() > 0) {
						setOptions(mapToOptions(list));
						return SUCCESS;
					}
				}	
			}
		} 
		catch (SherColorException e){
			logger.error(Encode.forJava(e.getMessage()), e);
			setMessage(e.getMessage());
		}
		
		/* Try to autocomplete the product based on shorthand product format */
		try {
			String expandedProdList = productService.fillProdNbr(partialProductNameOrId.toUpperCase());
			if (expandedProdList != null && expandedProdList.length() > 0) {
				List<CdsProd> cdsProdList;
				if (colorType.equals("CUSTOM")) {
					cdsProdList = productService.productAutocompleteBothActive(expandedProdList,reqObj.getCustomerID());
					setOptions(mapToOptions(cdsProdList));
				} 
				else {
					cdsProdList = productService.productAutocompleteCompatibleBase(expandedProdList, intBasesList, extBasesList, reqObj.getCustomerID());
					setOptions(mapToOptions(cdsProdList));
				}
				if (!cdsProdList.isEmpty()) {
					return SUCCESS;
				}
			}
		} 
		catch (SherColorException e){
			logger.error(Encode.forJava(e.getMessage()), e);
			setMessage(e.getMessage());
		}
		
		try {
			// list all products in autocomplete search because the custom manual option does not have primary base types
			if (colorType.equals("CUSTOM")) {
				/* 09/06/2017 - New Active Products Search. */
				setOptions(mapToOptions(productService.productAutocompleteBothActive(partialProductNameOrId.toUpperCase(),reqObj.getCustomerID())));
			} else {
				/* 04/14/2020 - Filter by Compatible Base Products Search */
				setOptions(mapToOptions(productService.productAutocompleteCompatibleBase(partialProductNameOrId.toUpperCase(), intBasesList, extBasesList, reqObj.getCustomerID())));
			}
		}
		catch (SherColorException e){
			logger.error(Encode.forJava(e.getMessage()), e);
			setMessage(e.getMessage());
		}
		return SUCCESS;
	}

	private List<autoComplete> mapToOptions(List<CdsProd> prodList) {
		List<autoComplete> outList = new ArrayList<autoComplete>();
		String theLabel;
		String theValue;
		String[] prepComment;
		
		// sort by product number and then by size code
		Collections.sort(prodList, Comparator.comparing(CdsProd::getPrepComment));
		for (CdsProd item : prodList) {
			prepComment = item.getPrepComment().split("-");
			prepComment[1] = getSizeText(prepComment[1]);
			theLabel = prepComment[0] + " " + item.getSalesNbr() + " " + item.getQuality() + " " + item.getComposite() + " " + item.getFinish() +  " " + prepComment[1] + " " + item.getIntExt() + " " + item.getBase();
			theValue = item.getSalesNbr();
		    outList.add(new autoComplete(theLabel,theValue));
		}
		return outList;
	}
	
	// User hit the backup button on the Product page
	public String backItUp() {
		try {
			//blank out the sales number as we are backing up.
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setSalesNbr("");
			reqObj.setProdNbr("");
			reqObj.setFinish("");
			reqObj.setBase("");
			reqObj.setComposite("");
			reqObj.setQuality("");
			reqObj.setSizeText("");
			reqObj.setKlass("");
			reqObj.setIntExt("");
			reqObj.setValidationWarning(false);
			reqObj.setValidationWarningSalesNbr("");
			reqObj.setClosestSwColorId("");
			reqObj.setClosestSwColorName("");
			sessionMap.put(reqGuid, reqObj);
		    return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	
	public String displayProdChange() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			colorComp = reqObj.getColorComp();
			colorID = reqObj.getColorID();
			List<String> baseList;
			CdsColorMast colorMast = colorMastService.read(colorComp, colorID);
			String prodComp = "SW";
			
			// re-populate bases lists in case we came from lookup jobs page and both are empty
			if (colorMast != null && (reqObj.getIntBases() == null || reqObj.getIntBases().equals("")) && 
									 (reqObj.getExtBases() == null || reqObj.getExtBases().equals(""))) {
				
				baseList = colorBaseService.InteriorColorBaseAssignments(colorMast.getColorComp(), colorMast.getColorId(), prodComp);
				reqObj.setIntBases(StringUtils.join(baseList, ','));
				
				baseList = colorBaseService.ExteriorColorBaseAssignments(colorMast.getColorComp(), colorMast.getColorId(), prodComp);
				reqObj.setExtBases(StringUtils.join(baseList, ','));
				
				// confirm that at least one of the intBases and ExtBases lists are populated.  If neither are populated, call autobase
				if (reqObj.getIntBases() == null && reqObj.getExtBases() == null) {
					// call autobase
					String custID = (String) reqObj.getCustomerID();
					CustWebParms cwp = customerService.getDefaultCustWebParms(custID);
					String custProdComp = cwp.getProdComp();
					baseList = colorBaseService.GetAutoBase(colorMast.getColorComp(), colorMast.getColorId(), custProdComp);
					reqObj.setIntBases(StringUtils.join(baseList, ','));
					reqObj.setExtBases(StringUtils.join(baseList, ','));
				}
			}
			
			sessionMap.put(reqGuid, reqObj);
		    return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String fillProdNbr() {
		try {
			if (shorthandProdNbr == null) {
				shorthandProdNbr = "";
				return ERROR;
			}
			
			//Shorthand prodNbr-szCode passed
			if (shorthandProdNbr.contains("-")) {
				String prodNbr = shorthandProdNbr.substring(0, shorthandProdNbr.indexOf("-"));
				String szCode = shorthandProdNbr.substring(shorthandProdNbr.indexOf("-"));
				String expandedProduct = productService.fillProdNbr(prodNbr);
				shorthandProdNbr = expandedProduct + szCode;
			}
			//Shorthand prodNbr szCode passed
			else if (shorthandProdNbr.contains(" ")) {
				String prodNbr = shorthandProdNbr.substring(0, shorthandProdNbr.indexOf(" "));
				String szCode = shorthandProdNbr.substring(shorthandProdNbr.indexOf(" ") + 1);
				String expandedProduct = productService.fillProdNbr(prodNbr);
				shorthandProdNbr = expandedProduct + "-" + szCode;
			}
			//Only shorthand product number was passed
			else {
				String expandedProduct = productService.fillProdNbr(shorthandProdNbr);
				shorthandProdNbr = expandedProduct;
			}
			return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public List<autoComplete> getOptions() {
		return options;
	}

	public void setOptions(List<autoComplete> options) {
		this.options = options;
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

	public String getColorID() {
		return colorID;
	}

	public void setColorID(String colorID) {
		this.colorID = colorID;
	}

	public String getColorComp() {
		return colorComp;
	}

	public void setColorComp(String colorComp) {
		this.colorComp = Encode.forHtml(colorComp);
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	private String getSizeText(String sizeCode) {
		return productService.getSizeText(sizeCode);
	}


	public String getForceProd() {
		return forceProd;
	}


	public void setForceProd(String forceProd) {
		this.forceProd = forceProd;
	}


	public String getShorthandProdNbr() {
		return shorthandProdNbr;
	}


	public void setShorthandProdNbr(String shorthandProdNbr) {
		this.shorthandProdNbr = shorthandProdNbr;
	}	

}