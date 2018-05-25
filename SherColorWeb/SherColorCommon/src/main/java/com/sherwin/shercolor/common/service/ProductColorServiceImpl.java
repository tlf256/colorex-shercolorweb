package com.sherwin.shercolor.common.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.validation.ProductColorValidator;
import com.sherwin.shercolor.util.domain.SwMessage;

@Service
@Transactional
public class ProductColorServiceImpl implements ProductColorService{
	
	static Logger logger = LogManager.getLogger(ProductColorServiceImpl.class);

	@Autowired 
	ProductColorValidator validator;
	
	public List<SwMessage> validate(String colorComp, String colorId, String salesNbr){
		List<SwMessage> retVal = new ArrayList<SwMessage>();
		
		// these are errors
		try {
			if(validator.checkForceBaseAssignment(colorComp, colorId, salesNbr)) {
				if(validator.checkProductExcludedFromColor(colorComp, colorId, salesNbr)){
					if(validator.checkVinylSafeRestrictions(colorComp, colorId, salesNbr)){
						if(validator.checkAfcdDelete(colorComp, colorId, salesNbr)){
							// no errors
						}
					}
				}
			}
		} catch (SherColorException se) {
			SwMessage errMsg = new SwMessage();
			errMsg.setCode(String.valueOf(se.getCode()));
			errMsg.setMessage(se.getLocalizedMessage());
			errMsg.setSeverity(Level.ERROR);
			retVal.add(errMsg);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		// if no errors, check for Warnings
		if(retVal.size()==0){
			try{
				validator.checkProductColorBaseMatch(colorComp, colorId, salesNbr);
			} catch (SherColorException se) {
				SwMessage errMsg = new SwMessage();
				errMsg.setCode(String.valueOf(se.getCode()));
				errMsg.setMessage(se.getLocalizedMessage());
				errMsg.setSeverity(Level.WARN);
				retVal.add(errMsg);
			} catch(Exception e) {
				logger.error(e.getMessage());
				throw e;
			}
		}
	
		return retVal;
	}
}
