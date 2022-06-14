package com.sherwin.shercolor.customershercolorweb.web.action;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.colormath.functions.ColorCoordinatesCalculator;
import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.service.ColorBaseService;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.common.service.CustomerService;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;


public class MeasureColorAction extends ActionSupport implements SessionAware, LoginRequired {

	private ColorService colorService;
	private CustomerService customerService;
	private ColorBaseService colorBaseService;

	private Map<String, Object> sessionMap;
	
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(MeasureColorAction.class);
	
	private String reqGuid;
	private boolean measureColor;
	private String measuredCurve;

	private String message;
	
	private String intBases;
	
	private String extBases;
	private boolean measure;
	private boolean compare;
	private boolean closestColors;
	
	@Autowired
	private ColorCoordinatesCalculator colorCoordCalc;

	public MeasureColorAction(){
		
		
	}
	
	// User hit the backup button on the Color page
	public String backItUp() {
		try {
			//blank out the color info as we are backing up.
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setColorComp("");
			reqObj.setColorID("");
			reqObj.setColorName("");
			reqObj.setIntBases("");
			reqObj.setExtBases("");
			reqObj.setRgbHex("");
			reqObj.setPrimerId(null);
			reqObj.setColorType("");
			reqObj.setColorVinylOnly(false);
			sessionMap.put(reqGuid, reqObj);
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String display() {

		 try {
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String execute() {

		List<String> baseList;
		String rgbHex = null;
		BigDecimal[] curveArray = new BigDecimal[40];
		String[] strCurveArray = new String[40];
		
		try {
			
			//convert the comma-delimited measuredCurve string to a BigDecimal array
			strCurveArray = measuredCurve.split(",");
			for (int i=0; i < 40; i++) {
				curveArray[i] = new BigDecimal(strCurveArray[i].trim());
			}
			
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			// Try getting an RGB value for the object.
			ColorCoordinates colorCoord = colorService.getColorCoordinates(curveArray, "D65");
			
			//compare colors can potentially be redirected here twice
			//so distinction needs to be made between the first and second measurements
			if(measure || compare) {
				if(colorCoord == null) {
					colorCoord = new ColorCoordinates();
					double[] curveArrDouble = new double[40];
					
					for(int i = 0; i < curveArray.length; i++) {
						curveArrDouble[i] = curveArray[i].doubleValue();
					}
					
					colorCoord = colorCoordCalc.getColorCoordinates(curveArrDouble);
				}
				
				Map<String, ColorCoordinates> coordMap = reqObj.getColorCoordMap();
				
				if(coordMap == null) {
					coordMap = new HashMap<String, ColorCoordinates>();
				}
				
				if(compare) {
					coordMap.put("sample", colorCoord);
				} else {
					coordMap.put("standard", colorCoord);
				}
				
				reqObj.setColorCoordMap(coordMap);
				
				sessionMap.put(reqGuid, reqObj);
				
				if(compare) {
					return "result";
				} else {
					return "sample";
				}
			}
			
			if (colorCoord != null) {
				rgbHex = colorCoord.getRgbHex();
				
				CdsColorMast closestSwColor = colorService.findClosestSwColorFromCoord(colorCoord);	
				if (closestSwColor != null) {
					reqObj.setClosestSwColorName(closestSwColor.getColorName());
					reqObj.setClosestSwColorId(closestSwColor.getColorId());
				}
			}
			
			reqObj.setRgbHex(rgbHex);
			reqObj.setCurveArray(curveArray);
			
			if(closestColors) {
				Map<String, ColorCoordinates> coordMap = new HashMap<String, ColorCoordinates>();
				coordMap.put("colorCoord", colorCoord);
				
				reqObj.setColorCoordMap(coordMap);
				
				sessionMap.put(reqGuid, reqObj);
				
				return "closestColors";
			}
			
			//2018-01-15 BKP - copied from below to here to calculated bases based on curve.
			//confirm that at least one of the intBases and ExtBases lists are populated.  If neither are populated,
			//call the autobase routine.
			if (intBases==null && extBases==null) {
				//call autobase
				String custID = (String) reqObj.getCustomerID();
				CustWebParms bobo = customerService.getDefaultCustWebParms(custID);
				String custProdComp = bobo.getProdComp();
				baseList = colorBaseService.GetAutoBase(curveArray, custProdComp );
				setIntBases(StringUtils.join(baseList, ','));
				setExtBases(StringUtils.join(baseList, ','));
			}
			
			reqObj.setIntBases(intBases);
			reqObj.setExtBases(extBases);

			sessionMap.put(reqGuid, reqObj);
			
			return SUCCESS;

		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String calibrate() {

		 try {
			 measureColor = true;
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String measure() {

		 try {
			 if(measure) {
				 return "measure";
			 } else if(compare) {
				 return "sample";
			 } else {
				 return SUCCESS;
			 }
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = Encode.forHtml(message);
	}

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public String getMeasuredCurve() {
		return measuredCurve;
	}

	public void setMeasuredCurve(String measuredCurve) {
		this.measuredCurve = Encode.forHtml(measuredCurve);
	}

	
	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public boolean isMeasureColor() {
		return measureColor;
	}

	public ColorService getColorService() {
		return colorService;
	}

	public void setColorService(ColorService colorService) {
		this.colorService = colorService;
	}
	
	public ColorBaseService getColorBaseService() {
		return colorBaseService;
	}

	public void setColorBaseService(ColorBaseService colorBaseService) {
		this.colorBaseService = colorBaseService;
	}

	public String getExtBases() {
		return extBases;
	}

	public void setExtBases(String extBases) {
		this.extBases = Encode.forHtml(extBases);
	}

	public String getIntBases() {
		return intBases;
	}

	public void setIntBases(String intBases) {
		this.intBases = Encode.forHtml(intBases);
	}

	public boolean isMeasure() {
		return measure;
	}

	public void setMeasure(boolean measure) {
		this.measure = measure;
	}

	public boolean isCompare() {
		return compare;
	}

	public void setCompare(boolean compare) {
		this.compare = compare;
	}

	public boolean isClosestColors() {
		return closestColors;
	}

	public void setClosestColors(boolean closestColors) {
		this.closestColors = closestColors;
	}

}
