package com.sherwin.shercolor.common.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CustWebDevices;
import com.sherwin.shercolor.common.domain.CustWebDevicesPK;
import com.sherwin.shercolor.common.exception.SherColorException;

@Repository
public class CustWebDevicesDaoImpl implements CustWebDevicesDao {
	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CustWebDevicesDaoImpl.class);
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	Locale locale;

	public CustWebDevices read(String customerId, String modelType, String serialNbr){
		CustWebDevices record = null;
		CustWebDevicesPK pkey = new CustWebDevicesPK();
		Session session = getSessionFactory().getCurrentSession();

		pkey.setCustomerId(customerId);
		pkey.setSerialNbr(serialNbr);
		pkey.setModelType(modelType);

		try {
			record = (CustWebDevices) session.get(CustWebDevices.class, pkey);
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

	@SuppressWarnings("unchecked")
	public List<CustWebDevices> listForCustomerId (String customerId){
		List<CustWebDevices> recordList = new ArrayList<CustWebDevices>();
		Session session = getSessionFactory().getCurrentSession();

		StringBuilder hql = new StringBuilder();
		hql.append("from CustWebDevices cwd ");
		hql.append("where cwd.customerId = :customerId ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("customerId",customerId);
			
			recordList = query.list();
		} catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			recordList = new ArrayList<CustWebDevices>();
			throw(e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return recordList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CustWebDevices> listSpectrosForCustomerId (String customerId){
		List<CustWebDevices> recordList = new ArrayList<CustWebDevices>();
		Session session = getSessionFactory().getCurrentSession();

		StringBuilder hql = new StringBuilder();
		hql.append("from CustWebDevices cwd ");
		hql.append("where cwd.customerId = :customerId and cwd.deviceType = 'SPECTRO'");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("customerId",customerId);
			
			recordList = query.list();
		} catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			recordList = new ArrayList<CustWebDevices>();
			throw(e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return recordList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CustWebDevices> listTintersForCustomerId (String customerId){
		List<CustWebDevices> recordList = new ArrayList<CustWebDevices>();
		Session session = getSessionFactory().getCurrentSession();

		StringBuilder hql = new StringBuilder();
		hql.append("from CustWebDevices cwd ");
		hql.append("where cwd.customerId = :customerId and cwd.deviceType = 'TINTER'");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("customerId",customerId);
			
			recordList = query.list();
		} catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			recordList = new ArrayList<CustWebDevices>();
			throw(e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return recordList;
		
	}
	
	public boolean create(CustWebDevices custWebDev){
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{

			
			if(custWebDev!=null){
				logger.info("inside create and here are some values " + custWebDev.getCustomerId() + " " + custWebDev.getDeviceModel() + " " + custWebDev.getSerialNbr() );

				CustWebDevices currentRecord = read(custWebDev.getCustomerId(), custWebDev.getDeviceModel(), custWebDev.getSerialNbr());
				if (currentRecord==null){
					logger.info("inside dao create before session.save ");
					session.save(custWebDev);
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
	
	public boolean delete(CustWebDevices custWebDev){
		boolean result = false;
		 
		Session session = getSessionFactory().getCurrentSession();

		try{
			session.delete(custWebDev);
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
