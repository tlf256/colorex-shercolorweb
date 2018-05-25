package com.sherwin.shercolor.common.validation;

import javax.validation.ValidationException;

import com.sherwin.shercolor.common.dao.CdsProdDao;
import com.sherwin.shercolor.common.dao.PosProdDao;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.PosProd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductValidatorImpl  implements ProductValidator {
	
	static Logger logger = LogManager.getLogger(ProductValidatorImpl.class);

	@Autowired
	PosProdDao posProdDao;
	
	@Autowired
	CdsProdDao cdsProdDao;
	
	public void checkPosProd(String salesNbr) throws ValidationException {
		PosProd posProd = null;
		try {
			posProd = posProdDao.read(salesNbr);
			if (posProd==null){
				throw new ValidationException();
			}
		} catch (ValidationException e){
			//expected as valid, so don't log
			throw(e);
		} catch(Exception e) {
			//unexpected (included possible HibernateException from posProdDao.read) so log it.
			logger.error(e.getMessage());
			throw e;
		}
		return;
	};
	
	public String checkPosProdByRex(String salesNbr) throws ValidationException {
		PosProd posProd = null;
		String productNbr = null;
		String szCd = null;
		try {
			try{
				productNbr = salesNbr.substring(0,9);
				szCd = salesNbr.substring(10,12);
			}
			catch(StringIndexOutOfBoundsException e){
				throw new ValidationException();
			};
			posProd = posProdDao.readByProdNbrSzCd(productNbr, szCd);
			if (posProd==null){
				throw new ValidationException();
			}
		} catch (ValidationException e){
			//expected as valid, so don't log
			throw(e);
		} catch(Exception e) {
			//unexpected (included possible HibernateException from posProdDao.read) so log it.
			logger.error(e.getMessage());
			throw e;
		}
		return posProd.getSalesNbr();
	};
	
	public String checkPosProdByUpc(String salesNbr) throws ValidationException {
		PosProd posProd = null;
		try {
			posProd = posProdDao.readByUpc(salesNbr);
			if (posProd==null){
				throw new ValidationException();
			}
		} catch (ValidationException e){
			//expected as valid, so don't log
			throw(e);
		} catch(Exception e) {
			//unexpected (included possible HibernateException from posProdDao.read) so log it.
			logger.error(e.getMessage());
			throw e;
		}
		return posProd.getSalesNbr();
	};
	
	public void checkCdsProd(String salesNbr) throws ValidationException {
		CdsProd cdsProd = null;
		try {
			cdsProd = cdsProdDao.read(salesNbr);
			if (cdsProd==null){
				throw new ValidationException();
			}
		} catch (ValidationException e){
			//expected as valid, so don't log
			throw(e);
		} catch(Exception e) {
			//unexpected (included possible HibernateException from posProdDao.read) so log it.
			logger.error(e.getMessage());
			throw e;
		}
		return;
	};
	
	public void checkSizeCode(String salesNbr) throws ValidationException {
		PosProd posProd = null;
		try {
			posProd = posProdDao.read(salesNbr);
			if (! posProd.getSzCd().equals("14") && posProd.getSzCd().equals("16") && posProd.getSzCd().equals("20"))
				throw new ValidationException();
		} catch (ValidationException e){
			//expected as valid, so don't log
			throw(e);
		} catch(Exception e) {
			//unexpected (included possible HibernateException from posProdDao.read) so log it.
			logger.error(e.getMessage());
			throw e;
		}
	}

	public PosProdDao getPosProdDao() {
		return posProdDao;
	}

	public void setPosProdDao(PosProdDao posProdDao) {
		this.posProdDao = posProdDao;
	}

	public CdsProdDao getCdsProdDao() {
		return cdsProdDao;
	}

	public void setCdsProdDao(CdsProdDao cdsProdDao) {
		this.cdsProdDao = cdsProdDao;
	};

}
