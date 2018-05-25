package com.sherwin.shercolor.common.dao;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CustWebDevices;
import com.sherwin.shercolor.common.domain.CustWebDevicesPK;
import com.sherwin.shercolor.common.domain.CustWebLoginTransform;
import com.sherwin.shercolor.common.domain.CustWebLoginTransformPK;
import com.sherwin.shercolor.common.exception.SherColorException;

@Repository
public class CustWebLoginTransformDaoImpl implements CustWebLoginTransformDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CustWebLoginTransformDaoImpl.class);
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	Locale locale;

	@Override
	public CustWebLoginTransform read(String keyField) {
		CustWebLoginTransform record = null;
		CustWebLoginTransformPK pkey = new CustWebLoginTransformPK();
		Session session = getSessionFactory().getCurrentSession();

		pkey.setKeyField(keyField);
		
		try {
			record = (CustWebLoginTransform) session.get(CustWebLoginTransform.class, pkey);
		} catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw (e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return record;
	}

	@Override
	public boolean create(CustWebLoginTransform custWebLoginXForm) {
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{

			
			if(custWebLoginXForm!=null){
				logger.info("inside create and here are some values " + custWebLoginXForm.getKeyField() + " " + custWebLoginXForm.getCustomerId());

				CustWebLoginTransform currentRecord = read(custWebLoginXForm.getKeyField());
				if (currentRecord==null){
					logger.info("inside dao create before session.save ");
					session.save(custWebLoginXForm);
					logger.info("inside dao create after session.save ");
					result = true;
				} else {
					//Record already exists.  That's fine just fine.
					result = true;
				}
			} else {
				SherColorException se = new SherColorException();
				se.setCode(1501);
				se.setMessage(messageSource.getMessage("1501", new Object[] {}, locale ));
				String msg = "Error creating record : %s  ";
				logger.error(String.format(msg, se.getMessage()));
				throw se;
			}
		} catch (HibernateException he) {
			String msg = "Error creating record : %s  ";
			logger.error(String.format(msg, he.getMessage()));
			he.printStackTrace();
			throw(he);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
 
		logger.info("end of create and about to return result equal to " + result);
		return result;

	}

	@Override
	public boolean delete(CustWebLoginTransform custWebLoginXForm) {
		boolean result = false;
		 
		Session session = getSessionFactory().getCurrentSession();

		try{
			session.delete(custWebLoginXForm);
			result = true;
		} catch (HibernateException e) {
			String msg = "Error deleting record : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
 
		return result;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
