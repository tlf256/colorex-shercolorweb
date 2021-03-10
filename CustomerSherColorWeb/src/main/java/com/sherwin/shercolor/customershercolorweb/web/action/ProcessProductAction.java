package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.Level;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CustWebPackageColor;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.service.ProductColorService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.autoComplete;
import com.sherwin.shercolor.util.domain.SwMessage;



public class ProcessProductAction extends ActionSupport implements SessionAware, LoginRequired  {

	private ProductService productService;
	private ProductColorService productColorService;
	
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
	
	public String execute() {
		
		 try {
			 RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			 OVERRIDEWARNMSG = getText("processProductAction.clickNext");
			 String colorComp = reqObj.getColorComp();
			 String colorId = reqObj.getColorID();
			 
			 //validate the product - need call to productService once complete
			 
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
					
					// We need to review how validation errors are handled - product validation and color validation are
					// slightly different.  Would be better if they were consistent.
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
											//check if this is a warning on the same salesNbr, and we have already displayed a 
											//warning for this sales number.  If so, then the user wants to go past the warning.
											if (reqObj.isValidationWarning() && reqObj.getValidationWarningSalesNbr().equals(salesNbr)) {
												//All is well, John Spartan.  
											} else {
												//Since this is a warning, add the warning "Click Next to override and continue" at 
												//the end here.  Eventually we may wish to convert this to retrieve this from a
												//properties file for globalization.
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
			 
		 	PosProd posProd = productService.readPosProd(salesNbr);
		 	if(posProd!=null && posProd.getUpc()!=null) reqObj.setUpc(posProd.getUpc());
		 	else reqObj.setUpc("");
		 	
		 	Optional<CdsProd> goodProd = productService.readCdsProd(salesNbr);
			
		 	//Check for CdsProd, if not, build prepCmt for PosProd
		 	Optional<String[]> prepCmtOptional = goodProd.map(CdsProd::getPrepComment).map(x -> x.split("-"));
		 	if(!prepCmtOptional.isPresent()) {
		 		prepCmtOptional = Optional.of(new String[2]);
		 		prepCmtOptional.get()[0] = posProd.getProdNbr();
		 		prepCmtOptional.get()[1] = posProd.getSzCd();
		 	}
		 	String[] prepCmt = prepCmtOptional.get();
			 
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
		     
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
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
			logger.error(e.getMessage(), e);
			setMessage(e.getMessage());
		}
		return SUCCESS;
	}

	private List<autoComplete> mapToOptions(List<CdsProd> prodList) {
		List<autoComplete> outList = new ArrayList<autoComplete>();
		String theLabel;
		String theValue;
		String[] prepComment;
		
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
		} catch (Exception e) {
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
	
	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public ProductColorService getProductColorService() {
		return productColorService;
	}

	public void setProductColorService(ProductColorService productColorService) {
		this.productColorService = productColorService;
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

}