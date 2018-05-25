package com.sherwin.shercolor.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.dao.CustWebDevicesDao;
import com.sherwin.shercolor.common.dao.CustWebJobFieldsDao;
import com.sherwin.shercolor.common.dao.CustWebLoginTransformDao;
import com.sherwin.shercolor.common.dao.CustWebParmsDao;
import com.sherwin.shercolor.common.domain.CustWebDevices;
import com.sherwin.shercolor.common.domain.CustWebJobFields;
import com.sherwin.shercolor.common.domain.CustWebLoginTransform;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.exception.SherColorExceptionBuilder;
import com.sherwin.shercolor.util.domain.SwMessage;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	
	static Logger logger = LogManager.getLogger(CustomerServiceImpl.class);

	@Autowired
	CustWebParmsDao custWebParmsDao;

	@Autowired
	CustWebJobFieldsDao jobFieldsDao;
	
	@Autowired
	CustWebDevicesDao devicesDao;
	
	@Autowired
	CustWebLoginTransformDao custWebLoginTransformDao;
	
	@Autowired
	private SherColorExceptionBuilder sherColorExceptionBuilder;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	Locale locale;

	@Override
	public String getDefaultCustomerTitle(String customerID) {
		CustWebParms thisCust = null;
		try {
			thisCust = custWebParmsDao.readByCustId(customerID);
			if (thisCust!=null) {
				return thisCust.getSwuiTitle();
			} else {
				return "";
			}
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	@Override
	public CustWebParms getDefaultCustWebParms(String customerID) {
		CustWebParms thisCust = null;
		try {
			thisCust = custWebParmsDao.readByCustId(customerID);
			return thisCust;
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	@Override
	public List<CustWebParms> getAllCustWebParms(String customerID) {
		List<CustWebParms> result;
		try {
			result = custWebParmsDao.readAllByCustId(customerID);
			return result;
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}


	public List<CustWebJobFields> getCustJobFields(String customerId){
		List<CustWebJobFields> result;
		
		try {
			result = jobFieldsDao.listForCustomerId(customerId);
			return result;
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
	}


	public List<SwMessage> validateCustJobFields(List<String> jobFieldList){
		List<SwMessage> result = new ArrayList<SwMessage>();
		//TODO Code this when Validation Routine done
		try {

		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
	
	public List<CustWebDevices> getCustDevices(String customerId){
		List<CustWebDevices> result;
		
		try {
			result = devicesDao.listForCustomerId(customerId);
			return result;
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
	}
	
	public List<CustWebDevices> getCustSpectros(String customerId){
		List<CustWebDevices> result;
		
		try {
			result = devicesDao.listSpectrosForCustomerId(customerId);
			return result;
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
	}
	
	public List<CustWebDevices> getCustTinters(String customerId){
		List<CustWebDevices> result;
		
		try {
			result = devicesDao.listTintersForCustomerId(customerId);
			return result;
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
	}
	
	@Transactional
	public SwMessage saveCustDevice (CustWebDevices custWebDev){

		// default result to unknown write error, will be overwritten with null if successful or with exception if thrown
		SwMessage result = new SwMessage();
		result.setCode("1500");
		result.setMessage(messageSource.getMessage("1500", new Object[] {}, locale ));
		result.setSeverity(Level.ERROR);
		
		try {
			if(custWebDev==null){
				//logger.info("in service and custWebTran is null");
			} else {
				//logger.info("in service and custWebTran is NOT null");
			}
			if(devicesDao.create(custWebDev)){
				// ok set result to null
				result = null;
			}
			//logger.info("back from dao.create");
		} catch (SherColorException se) {
			logger.error(se.getMessage());
			result = new SwMessage();
			result.setCode(String.valueOf(se.getCode()));
			result.setMessage(se.getLocalizedMessage());
			result.setSeverity(Level.ERROR);
		} catch (HibernateException he) {
			logger.error(he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		//logger.info("End of saveNewTranHistory and returning result equal to " + result);
		return result;
	}

	@Override
	public String getLoginTransformCustomerId(String keyField) {
		CustWebLoginTransform thisCust = null;
		try {
			thisCust = custWebLoginTransformDao.read(keyField);
			if (thisCust!=null) {
				return thisCust.getCustomerId();
			} else {
				return "";
			}
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
	
}
