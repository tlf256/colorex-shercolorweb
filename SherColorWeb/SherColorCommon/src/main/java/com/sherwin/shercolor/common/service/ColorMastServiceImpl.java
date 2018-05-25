package com.sherwin.shercolor.common.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.dao.CdsColorMastDao;
import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.exception.SherColorExceptionBuilder;
import com.sherwin.shercolor.common.validation.ColorValidator;
import com.sherwin.shercolor.util.domain.SwMessage;

@Service
@Transactional
public class ColorMastServiceImpl implements ColorMastService {
	
	static Logger logger = LogManager.getLogger(ColorMastServiceImpl.class);

	@Autowired
	CdsColorMastDao cdsColorMastDao;
	
	@Autowired
	ColorValidator colorValidator;

	@Autowired
	private SherColorExceptionBuilder sherColorExceptionBuilder;
	
	@Override
	public String[] autocompleteColor(String colorNameOrId) {
		
			
		String[] result = null;
		List<String> list;
		

		try {
			list = cdsColorMastDao.listForAutocompleteColorName(colorNameOrId);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		if(! CollectionUtils.isEmpty(list)){
			result = list.toArray(new String[list.size()]);
			return result;
		} else {
			return result;
		}
	}
	
	@Override
	public String[] listColorCompanies() {
		
			
		String[] result = null;
		List<String> list;
		

		try {
			list = cdsColorMastDao.listOfColorCompanies();
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		if(! CollectionUtils.isEmpty(list)){
			result = list.toArray(new String[list.size()]);
			return result;
		} else {
			return result;
		}
	}
	
	@Override
	public List<CdsColorMast> autocompleteCompetitiveColor(String colorNameOrId) {
		
		List<CdsColorMast> list;
		
		try {
			list = cdsColorMastDao.listForCompetColorAutocomplete(colorNameOrId);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		if(!list.isEmpty()){
			return list;
		} else {
			return null;
		}
	}
	
	@Override
	public List<CdsColorMast> autocompleteSWColor(String colorNameOrId) {
		
		List<CdsColorMast> list;

		try {
			list = cdsColorMastDao.listForSwColorAutocomplete(colorNameOrId);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		if(!list.isEmpty()){
			return list;
		} else {
			return null;
		}
	}
	
	@Override
	public CdsColorMast read(String colorComp, String colorID) {

		
		CdsColorMast theRecord;

		try {
			theRecord = cdsColorMastDao.read(colorComp, colorID);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return theRecord;
	}

	
	@Override
	public List<SwMessage> validate(String colorComp, String colorID) {
		List<SwMessage> returnList = new ArrayList<SwMessage>();
		
		try {
			colorValidator.checkColor(colorComp, colorID);
		} catch (SherColorException sce) {
			returnList.add(new SwMessage(Level.ERROR,Integer.toString(sce.getCode()) ,sce.getMessage()));			
		} catch (Exception e) {
			logger.error(e.getMessage());
			returnList.add(new SwMessage(Level.ERROR,"UNKNOWN",e.getMessage()));			
		}
		
		return returnList;
	}

}
