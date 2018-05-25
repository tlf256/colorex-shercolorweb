package com.sherwin.shercolor.common.validation;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;


import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.service.ColorMastService;

public class ColorValidatorImpl implements ColorValidator {
	
	static Logger logger = LogManager.getLogger(ColorValidatorImpl.class);
	
	@Autowired
	private ColorMastService cms; 
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	Locale locale;
	
	@Override
	public CdsColorMast checkColor(String colorComp, String colorId) {
		CdsColorMast returnrec = null;

		try {
			returnrec = cms.read(colorComp, colorId);
			if (returnrec==null) {
				SherColorException se = new SherColorException();
				se.setCode(402);
				se.setMessage(messageSource.getMessage("402", new Object[] {colorComp + " " + colorId,""}, locale ));
				throw se;
			}
		} catch (SherColorException se) {
			//no need to log this, as this is expected from Above.  
			throw se;
		}  catch (Exception e) {
			//log this one as it is UNexpected.
			logger.error(e.getMessage());
			throw e;
		}
		
		return returnrec;
	}

}
