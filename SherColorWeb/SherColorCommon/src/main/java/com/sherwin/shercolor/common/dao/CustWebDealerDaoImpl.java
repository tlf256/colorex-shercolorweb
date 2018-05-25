package com.sherwin.shercolor.common.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CustWebDealer;
import com.sherwin.shercolor.common.domain.CustWebDealerPK;
import com.sherwin.shercolor.common.dao.CustWebDealerDao;

@Repository
public class CustWebDealerDaoImpl implements CustWebDealerDao {

	@Autowired
	private SessionFactory sessionFactory;

	static Logger logger = Logger.getLogger(CustWebDealerDaoImpl.class.getName());
	
	@Override
	public boolean create(CustWebDealer custWebDealer) {
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.save(custWebDealer);
		}
		catch (HibernateException he){
			String msg = "Error CREATING CustWebDealer record : %s";
			logger.error(String.format(msg, he.getMessage()));
			throw (he);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw (e);
		}
		return true;
	}
	
	@Override
	public CustWebDealer read(String customerId) {
		CustWebDealer custWebDealer = null;
		CustWebDealerPK pkey = new CustWebDealerPK();

		Session session = getSessionFactory().getCurrentSession();

		pkey.setCustomerId(customerId);
		
		try{
			custWebDealer = (CustWebDealer) session.get(CustWebDealer.class,pkey);
		}
		catch (HibernateException he){
			String msg = "Error READING CustWebDealer record by unique key %s : %s";
			logger.error(String.format(msg, pkey.toString(), he.getMessage()));
			throw (he);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw (e);
		}
		return custWebDealer;
	}
	
	@Override
	public CustWebDealer getDealer(String customerId) {
		CustWebDealer result = null;
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CustWebDealer d "
				+ "where d.customerId = :customerId";
		
		try {
			Query query = session.createQuery(hql);
			query.setParameter("customerId", customerId);

			@SuppressWarnings("rawtypes")
			java.util.List list = query.list();
			
			if(! CollectionUtils.isEmpty(list)){
				result = (CustWebDealer) list.get(0);
			}
		
			} catch (HibernateException e) {
				String msg = "Error READING CustWebDealer record by key %s : %s";
				logger.error(String.format(msg, customerId, e.getMessage()));
				e.printStackTrace();
				throw(e);
		}
		catch(RuntimeException e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}

	@Override
	public boolean update(CustWebDealer custWebDealer) {
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.update(custWebDealer);
			return true;	
		}
		catch (HibernateException he){
			String msg = "Error UPDATING CustWebDealer record : %s";
			logger.error(String.format(msg, he.getMessage()));
			throw (he);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw (e);
		}
	}
	
	@Override
	public boolean delete(CustWebDealer custWebDealer){
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.delete(custWebDealer);
			return true;	
		}
		catch (HibernateException he){
			String msg = "Error DELETING CustWebDealer record : %s";
			logger.error(String.format(msg, he.getMessage()));
			throw (he);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw (e);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CustWebDealer> listDealers() {
		List<CustWebDealer> listDealers = new ArrayList<CustWebDealer>();
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CustWebDealer d";
		try {
			Query query = session.createQuery(hql.toString());
			listDealers = query.list();
			}
			catch (HibernateException he){
				String msg = "Error READING LIST custwebdealer list : %s";
				logger.error(String.format(msg, he.getMessage()));
				throw (he);
			}
			catch (Exception e){
				logger.error(e.getMessage());
				throw (e);
			}
		return listDealers;
	}

	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CustWebDealerDaoImpl.logger = logger;
	}

	
}
