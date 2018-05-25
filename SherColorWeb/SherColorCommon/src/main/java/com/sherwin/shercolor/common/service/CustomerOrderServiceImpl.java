package com.sherwin.shercolor.common.service;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.dao.CustWebDealerCustDao;
import com.sherwin.shercolor.common.dao.CustWebDealerCustOrdDao;
import com.sherwin.shercolor.common.dao.CustWebDealerDao;
import com.sherwin.shercolor.common.domain.CustWebDealer;
import com.sherwin.shercolor.common.domain.CustWebDealerCust;
import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.exception.SherColorExceptionBuilder;

@Service
@Transactional
public class CustomerOrderServiceImpl implements CustomerOrderService {
	
	static Logger logger = LogManager.getLogger(CustomerServiceImpl.class);

	@Autowired
	CustWebDealerDao custWebDealerDao;

	@Autowired
	CustWebDealerCustDao custWebDealerCustDao;

	@Autowired
	CustWebDealerCustOrdDao custWebDealerCustOrdDao;

	@Autowired
	private SherColorExceptionBuilder sherColorExceptionBuilder;
	
	@Autowired
	Locale locale;

	@Override
	public boolean createDealer(CustWebDealer custWebDealer){
		boolean result = false;
		try {
			result = custWebDealerDao.create(custWebDealer);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}

	public CustWebDealer readDealer(String customerId){
		CustWebDealer custWebDealer = null;
		try {
			custWebDealer = custWebDealerDao.read(customerId);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return custWebDealer;
	}
	
	public boolean updateDealer(CustWebDealer custWebDealer) {
		boolean result = false;
		try {
			result = custWebDealerDao.update(custWebDealer);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
	
	public List<CustWebDealer> listDealers() {
		List<CustWebDealer> custWebDealerList = null;
		try {
			custWebDealerList = custWebDealerDao.listDealers();
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return custWebDealerList;
	}
	
	public boolean deleteDealer(CustWebDealer custWebDealer) {
		boolean result = false;
		try {
			result = custWebDealerDao.delete(custWebDealer);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
	
	public boolean createDealerCust(CustWebDealerCust custWebDealerCust) {
		boolean result = false;
		try {
			result = custWebDealerCustDao.create(custWebDealerCust);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}

	public CustWebDealerCust readDealerCust(String customerId, String dlrCustId) {
		CustWebDealerCust custWebDealerCust = null;
		try {
			custWebDealerCust = custWebDealerCustDao.read(customerId, dlrCustId);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return custWebDealerCust;
	}
	
	public boolean updateDealerCust(CustWebDealerCust custWebDealerCust) {
		boolean result = false;
		try {
			result = custWebDealerCustDao.update(custWebDealerCust);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}

	public List<CustWebDealerCust> listDealerCustomers(String customerId) {
		List<CustWebDealerCust> custWebDealerCustList = null;
		try {
			custWebDealerCustList = custWebDealerCustDao.listCustomers(customerId);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return custWebDealerCustList;
	}
	
	public List<CustWebDealerCust> listDealerCustomersAutocomplete(String customerId, String dlrCustId) {
		List<CustWebDealerCust> custWebDealerCustList = null;
		try {
			custWebDealerCustList = custWebDealerCustDao.listCustomersAutocomplete(customerId, dlrCustId);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return custWebDealerCustList;
	}

	public boolean deleteDealerCust(CustWebDealerCust custWebDealerCust){
		boolean result = false;
		try {
			result = custWebDealerCustDao.delete(custWebDealerCust);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
	
	public boolean createDealerCustOrd(CustWebDealerCustOrd custWebDealerCustOrd){
		boolean result = false;
		try {
			result = custWebDealerCustOrdDao.create(custWebDealerCustOrd);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
	
	public CustWebDealerCustOrd readDealerCustOrd(String customerId, String dlrCustId, int controlNbr) {
		CustWebDealerCustOrd custWebDealerCustOrd = null;
		try {
			custWebDealerCustOrd = custWebDealerCustOrdDao.read(customerId, dlrCustId, controlNbr);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return custWebDealerCustOrd;
	}
	
	public boolean updateDealerCustOrd(CustWebDealerCustOrd custWebDealerCustOrd) {
		boolean result = false;
		try {
			result = custWebDealerCustOrdDao.update(custWebDealerCustOrd);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
	
	public List<CustWebDealerCustOrd> listDealerCustOrders(String customerId, String dlrCustId){
		List<CustWebDealerCustOrd> custWebDealerCustOrdList = null;
		try {
			custWebDealerCustOrdList = custWebDealerCustOrdDao.listCustOrders(customerId, dlrCustId);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return custWebDealerCustOrdList;
	}
	
	public boolean deleteDealerCustOrd(CustWebDealerCustOrd custWebDealerCustOrd) {
		boolean result = false;
		try {
			result = custWebDealerCustOrdDao.delete(custWebDealerCustOrd);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
	
}
