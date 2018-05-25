package com.sherwin.shercolor.common.dao;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxtPK;

@Repository
public class CustWebColorantsTxtDaoImpl implements CustWebColorantsTxtDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	static Logger logger = Logger.getLogger(CustWebTranDaoImpl.class.getName());

	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	Locale locale;
	

	public boolean create(CustWebColorantsTxt custWebColorantsTxt){
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{	
			session.save(custWebColorantsTxt);
			result = true;
		} catch (ConstraintViolationException e) {
		    System.out.print(e.getConstraintName() + e.getErrorCode() + "sqlstate" + e.getSQLState());
		
		} catch (HibernateException he) {
			String msg = "Error creating record : %s  ";
			logger.error(String.format(msg, he.getMessage()));
			he.printStackTrace();
			throw(he);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return result;
	}
	
	public CustWebColorantsTxt read(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr, String clrntCode, int position){
		CustWebColorantsTxt record = null;
		CustWebColorantsTxtPK pkey = new CustWebColorantsTxtPK();

		Session session = getSessionFactory().getCurrentSession();

		pkey.setCustomerId(customerId);
		pkey.setClrntSysId(clrntSysId);
		pkey.setTinterModel(tinterModel);
		pkey.setTinterSerialNbr(tinterSerialNbr);
		pkey.setClrntCode(clrntCode);
		pkey.setPosition(position);

		try {
			record = (CustWebColorantsTxt) session.get(CustWebColorantsTxt.class, pkey);
		} catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw (e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return record;
	}
	
	public boolean update(CustWebColorantsTxt custWebColorantsTxt){
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{
			session.update(custWebColorantsTxt);
			result = true;
		} catch (HibernateException e) {
			String msg = "Error updating record : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return result;
	}
	
	public boolean delete(CustWebColorantsTxt custWebColorantsTxt){
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{
			session.delete(custWebColorantsTxt);
			result = true;
		} catch (HibernateException e) {
			String msg = "Error creating record : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<CustWebColorantsTxt> listForUniqueTinter(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr){
		List<CustWebColorantsTxt> recordList = null;
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CustWebColorantsTxt cwct ");
		hql.append("where cwct.customerId = :customerId ");
		hql.append("  and cwct.clrntSysId = :clrntSysId ");
		hql.append("  and cwct.tinterModel = :tinterModel ");
		if(tinterSerialNbr != null){
			hql.append("  and cwct.tinterSerialNbr = :tinterSerialNbr ");
		}
		hql.append("order by cwct.position ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("customerId",customerId)
					.setParameter("clrntSysId",clrntSysId)
					.setParameter("tinterModel",tinterModel);
					if(tinterSerialNbr != null){
						query.setParameter("tinterSerialNbr",tinterSerialNbr);
					}
					
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return recordList;
	}


	@SuppressWarnings("unchecked")
	public List<String> listOfModelsForCustomerId(String customerId, String clrntSysId){
		List<String> modelList = null;
		
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("select distinct cwct.tinterModel ");
		hql.append("from CustWebColorantsTxt cwct ");
		hql.append("where cwct.customerId = :customerId ");
		if(clrntSysId!=null && !clrntSysId.isEmpty()) hql.append("  and cwct.clrntSysId = :clrntSysId ");
		hql.append("order by cwct.tinterModel ");

		try {
			Query query = null;
			if(clrntSysId!=null && !clrntSysId.isEmpty()) {
				query = session.createQuery(hql.toString())
						.setParameter("customerId",customerId)
						.setParameter("clrntSysId",clrntSysId);
			} else {
				query = session.createQuery(hql.toString())
						.setParameter("customerId",customerId);
			}
			
			modelList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return modelList;
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<String> listOfColorantSystemsByCustomerId(String customerId){
		List<String> modelList = null;
		
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("select distinct cwct.clrntSysId ");
		hql.append("from CustWebColorantsTxt cwct ");
		hql.append("where cwct.customerId = :customerId ");
		

		try {
			Query query = null;
		
				query = session.createQuery(hql.toString())
						.setParameter("customerId",customerId);
			
			
			modelList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return modelList;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
