package com.sherwin.shercolor.common.validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.sherwin.shercolor.common.dao.CdsColorBaseDao;
import com.sherwin.shercolor.common.dao.CdsColorMastDao;
import com.sherwin.shercolor.common.dao.CdsColorStandDao;
import com.sherwin.shercolor.common.dao.CdsFormulaChgListDao;
import com.sherwin.shercolor.common.dao.CdsMessageDao;
import com.sherwin.shercolor.common.dao.CdsProdCharzdDao;
import com.sherwin.shercolor.common.dao.CdsProdDao;
import com.sherwin.shercolor.common.dao.CdsProdFamilyDao;
import com.sherwin.shercolor.common.dao.CdsProdListDao;
import com.sherwin.shercolor.common.dao.PosProdDao;
import com.sherwin.shercolor.common.domain.CdsColorBase;
import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CdsColorStand;
import com.sherwin.shercolor.common.domain.CdsFormulaChgList;
import com.sherwin.shercolor.common.domain.CdsMessage;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CdsProdFamily;
import com.sherwin.shercolor.common.domain.CdsProdList;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.service.ColorBaseService;

public class ProductColorValidatorImpl implements ProductColorValidator {
	
	static Logger logger = LogManager.getLogger(ProductColorValidatorImpl.class);
	
	@Autowired
	CdsColorBaseDao colorBaseDao;
	
	@Autowired
	ColorBaseService colorBaseService;
	
	@Autowired
	Locale locale;
	
	@Autowired
	CdsColorMastDao colorMastDao;
	
	@Autowired
	CdsColorStandDao colorStandDao;
	
	@Autowired
	PosProdDao posProdDao;
	
	@Autowired
	CdsProdDao cdsProdDao;

	@Autowired
	CdsProdListDao prodListDao;
	
	@Autowired
	CdsProdCharzdDao prodCharzdDao;
	
	@Autowired
	CdsProdFamilyDao prodFamilyDao;
	
	@Autowired
	CdsFormulaChgListDao formulaChgListDao;
	
	@Autowired
	CdsMessageDao messageDao;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;

	public boolean checkProductColorBaseMatch(String colorComp, String colorId, String salesNbr){
		boolean pass = false;
		
		try {
			// get prod int_ext indicator and prod_comp to determine bases that can be used for the color
			CdsProd cdsProd = cdsProdDao.read(salesNbr);
			
			if(cdsProd==null){
				pass=true; // no cds prod will not compare base types
			} else {
				String prodComp;
				if(cdsProd.getProdComp()==null || cdsProd.getProdComp().isEmpty() || cdsProd.getProdComp().startsWith("SW")){
					prodComp = "SW";
				} else {
					prodComp = cdsProd.getProdComp();
				}
				
				List<String> checkBaseList = null;
				//List<String> intBaseList = colorBaseDao.listBasesForColorCompIdIntExtProdComp(colorComp, colorId, "I", prodComp);
				List<String> intBaseList = colorBaseService.InteriorColorBaseAssignments(colorComp, colorId, prodComp);
				if(intBaseList==null) intBaseList = new ArrayList<String>();
				//List<String> extBaseList = colorBaseDao.listBasesForColorCompIdIntExtProdComp(colorComp, colorId, "E", prodComp);
				List<String> extBaseList = colorBaseService.ExteriorColorBaseAssignments(colorComp, colorId, prodComp);
				if(extBaseList==null) extBaseList = new ArrayList<String>();
				
				List<String> autoBaseList = null;
				CdsColorStand colorStand = colorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2DK");
				if(colorStand!=null && colorStand.getCurve()!=null){
					autoBaseList = colorBaseService.GetAutoBase(colorStand.getCurve(), prodComp);
				} else { 
					autoBaseList = new ArrayList<String>();
				}
	
				if(intBaseList.size()==0 && extBaseList.size()==0 && autoBaseList.size()==0){
					pass = true;
				} else {
					// determine base list to check against...
					if(intBaseList.size()==0 && extBaseList.size()==0){
						checkBaseList = autoBaseList;
					} else {
						if(cdsProd.getIntExt().equalsIgnoreCase("INTERIOR")){
							checkBaseList = intBaseList;
						} else {
							checkBaseList = extBaseList;
						}
					}
					
					for(String checkBase : checkBaseList) {
						if(cdsProd.getBase().equalsIgnoreCase(checkBase)) {
							pass = true;
							break;
						}
					}
				}
				
				if(!pass){
					SherColorException se = new SherColorException();
					se.setCode(1424);
					se.setMessage(messageSource.getMessage("1424", new Object[] {cdsProd.getIntExt() + " " + cdsProd.getBase()}, locale ));
					throw se;
				}
			}
		}  catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return pass;
	}
	
	public boolean checkForceBaseAssignment(String colorComp, String colorId, String salesNbr){
		boolean pass = false;
		
		//hashmap base name to prod family code
		HashMap<String,String> nameToCode = new HashMap<String,String>();
		
		try {
			CdsProd cdsProd = cdsProdDao.read(salesNbr);
			String ieFlag = "E";
			//11/21/2016*BKP* added null check to prevent null pointer exceptions when validating products that have not been characterized.
			if (cdsProd != null) {
				if(cdsProd.getIntExt().equalsIgnoreCase("INTERIOR")){
					ieFlag = "I";
				}
			}
		
			// get all force base types
			List<CdsColorBase> forceBaseRecords = colorBaseDao.listForceBasesForColorCompIdIntExt(colorComp, colorId, ieFlag);
			// Build hash map of bases being forced and associated prod family base type code
			for (CdsColorBase baseRec : forceBaseRecords){
				if(baseRec.getBase().equalsIgnoreCase("VIVID YELLOW")) nameToCode.put("VIVID YELLOW","VY");
				if(baseRec.getBase().equalsIgnoreCase("LIGHT YELLOW")) nameToCode.put("LIGHT YELLOW","LY");
				if(baseRec.getBase().equalsIgnoreCase("BRIGHT YELLOW")) nameToCode.put("BRIGHT YELLOW","BY");
				if(baseRec.getBase().equalsIgnoreCase("YELLOW")) nameToCode.put("YELLOW","DY"); // DURON YELLOW
				if(baseRec.getBase().equalsIgnoreCase("PRIMARY RED")) nameToCode.put("PRIMARY RED","PR");
				if(baseRec.getBase().equalsIgnoreCase("REAL RED")) nameToCode.put("REAL RED","RR");
				if(baseRec.getBase().equalsIgnoreCase("RED")) nameToCode.put("RED","DR"); // DURON RED
			}
		
			if (nameToCode!=null && !nameToCode.isEmpty()){
				// get product number
				PosProd posProd = posProdDao.read(salesNbr);
				
				if (posProd!=null){
					// read cds prod, check if base entered is one of the force bases
					String enteredBase="";
					if (cdsProd==null || cdsProd.getBase()==null) {
						// no cds prod, must allow it...
						pass = true;
					} else {
						enteredBase = cdsProd.getBase();
						if(nameToCode.containsKey(enteredBase)) pass = true;
					}
					
					if (!pass){
						// get full product family for product
						List<CdsProdFamily> fullFamily = prodFamilyDao.listFullFamilyForPrimaryProdNbr(posProd.getProdNbr(), false);
						if(fullFamily.size()>0) {
							// does family contain Force Base, if so then throw exception, otherwise pass
							boolean famContainsForceBase = false;
							for(CdsProdFamily familyRec : fullFamily){
								if (nameToCode.containsValue(familyRec.getBaseType())) famContainsForceBase = true;
							}
							if (famContainsForceBase){
								// throw an exception, pass in list of bases they must choose from
								String forceBaseNames = nameToCode.keySet().toString();
								SherColorException se = new SherColorException();
								se.setCode(1420);
								se.setMessage(messageSource.getMessage("1420", new Object[] {forceBaseNames}, locale ));
								throw se;
							} else {
								pass = true;
							}
						} else {
							pass = true; //no prod family for product so ForceBase does not apply
						}
					}
				} else {
					pass = true; // no PosProd so no family so Force Base does not apply
				}
			} else {
				pass = true; // no force bases, set to true
			}
		}  catch (SherColorException e) {
			throw e;	
		}  catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return pass;
	}
	
	public boolean checkProductExcludedFromColor(String colorComp, String colorId, String salesNbr){
		boolean pass = false;
		
		try {
			// read color mast to get exclude or force product list
			CdsColorMast colorMast = colorMastDao.read(colorComp, colorId);
			
			// if exclude list exists and this product on it, throw error
			if(colorMast!=null){
				if(colorMast.getExcludeProd()!=null && !colorMast.getExcludeProd().isEmpty()){
					CdsProdList excludeProdList = prodListDao.read(colorMast.getExcludeProd(), salesNbr);
					if(excludeProdList!=null){
						SherColorException se = new SherColorException();
						se.setCode(1421);
						se.setMessage(messageSource.getMessage("1421", new Object[] {colorMast.getColorId() + " - " + colorMast.getColorName(), colorMast.getExcludeProd()}, locale ));
						pass=false;
						throw se;
					}
				}
				
				// if force list exists and this product is not on it, throw error
				if(colorMast.getForceProd()!=null && !colorMast.getForceProd().isEmpty()){
					CdsProdList forceProdList = prodListDao.read(colorMast.getForceProd(), salesNbr);
					if(forceProdList==null){
						SherColorException se = new SherColorException();
						se.setCode(1422);
						se.setMessage(messageSource.getMessage("1422", new Object[] {colorMast.getColorId() + " - " + colorMast.getColorName(), colorMast.getForceProd()}, locale ));
						pass=false;
						throw se;
					}
				}
			}

			pass = true;
		}  catch (SherColorException e) {
			throw e;	
		}  catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return pass;
	}
	
	public boolean checkAfcdDelete(String colorComp, String colorId, String salesNbr){
		boolean pass = false;
		String altProdNbr = null;
		String genericMsg = null;
		
		try {
			// check for Deletes for Product and Color
			List<CdsFormulaChgList> prodColorDeletes = formulaChgListDao.listDeletesForColorAndProduct(colorComp, colorId, salesNbr);
			for(CdsFormulaChgList thisDelete : prodColorDeletes){
				CdsMessage cdsMessage = messageDao.read(thisDelete.getCdsMessageId(), "AFCD");
				String msg="";
				if(cdsMessage!=null && cdsMessage.getMessageText()!=null){
					msg = cdsMessage.getMessageText();
					if(msg.contains("Please use this color with")){
						altProdNbr = msg.substring(msg.length()-15, msg.length()-6);
					} else {
						genericMsg = msg;
					}
				}
			}
	
			if(altProdNbr!=null){
				String base = cdsProdDao.read(salesNbr).getBase();
				SherColorException se = new SherColorException();
				se.setCode(1425);
				se.setMessage(messageSource.getMessage("1425", new Object[] {colorComp + "-" + colorId,base,altProdNbr}, locale ));
				pass = false;
				throw se;
			} else {
				if(genericMsg!=null){
					SherColorException se = new SherColorException();
					se.setCode(1426);
					se.setMessage(messageSource.getMessage("1426", new Object[] {genericMsg}, locale ));
					pass = false;
					throw se;
				} else {
					pass = true;
				}
			}
		}  catch (SherColorException e) {
			throw e;	
		}  catch (Exception e) {
			logger.error(e.getMessage());
			throw e;		
		}
		return pass;
	}
	
	public boolean checkVinylSafeRestrictions(String colorComp, String colorId, String salesNbr){
		boolean pass = false;
		
		try {
			// Read color mast and see if this color is vinyl safe color
			CdsColorMast colorMast = colorMastDao.read(colorComp, colorId);
			if(colorMast!=null && colorMast.getIsVinylSiding()){
				// If vinyl safe color, product must be a vinyl safe product or it is an error
				PosProd posProd = posProdDao.read(salesNbr);
				boolean isVinylSafeProduct = false;
				if(posProd!=null){
					List<CdsProdCharzd> prodCharzdList = prodCharzdDao.listForProdNbr(posProd.getProdNbr(),true);
					for(CdsProdCharzd prodCharzd : prodCharzdList){
						if(prodCharzd.getVinyl_excludeClrnt()!=null && !prodCharzd.getVinyl_excludeClrnt().isEmpty()){
							isVinylSafeProduct = true;
						}
					}
				}
				if(isVinylSafeProduct){
					// is vs product, pass it
					pass = true;
				} else {
					SherColorException se = new SherColorException();
					se.setCode(1423);
					se.setMessage(messageSource.getMessage("1423", new Object[] {colorComp + " - " + colorId,salesNbr}, locale ));
					pass = false;
					throw se;
				}
			} else {
				// no color mast so not a vs color, it passes
				pass = true;
			}
		}  catch (SherColorException e) {
			throw e;	
		}  catch (Exception e) {
			logger.error(e.getMessage());
			throw e;		
		}

		return pass;
	}
}
