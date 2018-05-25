package com.sherwin.shercolor.common.dao;

import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CustWebActiveProds;

@Repository
public class CustWebActiveProdsDaoImpl implements CustWebActiveProdsDao {

	@Autowired
	private SessionFactory sessionFactory;

	static Logger logger = LogManager.getLogger(PosProdDaoImpl.class);

	@Autowired
	Locale locale;

	@Override
	public CustWebActiveProds getActiveProdBySalesNbr(String salesnbr) {
		CustWebActiveProds result = null;
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CustWebActiveProds s "
				+ "where s.salesNbr = :salesnbr";
		
		try {
			Query query = session.createQuery(hql);
			query.setParameter("salesnbr", salesnbr);

			@SuppressWarnings("rawtypes")
			java.util.List list = query.list();
			
			if(! CollectionUtils.isEmpty(list)){
				result = (CustWebActiveProds) list.get(0);
			}
		
			} catch (HibernateException e) {
				String msg = "Error reading sales number %s : salesnbr  ";
				logger.error(String.format(msg, salesnbr, e.getMessage()));
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
	public CustWebActiveProds sgetActiveProdBySalesNbr(String salesnbr) {
		CustWebActiveProds result = null;
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from CUSTWEBACTIVEPRODS s "
				+ "where s.salesnbr = :salesnbr";
		
		try {
			Query query = session.createSQLQuery(sql);
			query.setParameter("salesnbr", salesnbr);

			@SuppressWarnings("rawtypes")
			java.util.List list = query.list();
			
			if(! CollectionUtils.isEmpty(list)){
				result = (CustWebActiveProds) list.get(0);
			}
		
			} catch (HibernateException e) {
				String msg = "Error reading sales number %s : salesnbr  ";
				logger.error(String.format(msg, salesnbr, e.getMessage()));
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
	public CustWebActiveProds getActiveProdByRexSize(String prodNbr, String sizeCd) {
		CustWebActiveProds result = null;
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CustWebActiveProds s "
				+ "where s.prodNbr = :prodNbr "
				+ "and s.sizeCd = :sizeCd";
		
		try {
			Query query = session.createQuery(hql);
			query.setParameter("prodNbr", prodNbr);
			query.setParameter("sizeCd", sizeCd);

			@SuppressWarnings("rawtypes")
			java.util.List list = query.list();
			
			if(! CollectionUtils.isEmpty(list)){
				result = (CustWebActiveProds) list.get(0);
			}
		
			} catch (HibernateException e) {
				String msg = "Error reading prodNbr number %s : salesnbr  ";
				logger.error(String.format(msg, prodNbr, e.getMessage()));
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
	@SuppressWarnings("unchecked")
	public List<CustWebActiveProds> getActiveProdByRex(String prodnbr) {
		List<CustWebActiveProds> result = null;
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from CustWebActiveProds s "
				+ "where s.prodnbr = :prodnbr ";
		
		try {
			Query query = session.createSQLQuery(sql).addEntity(CustWebActiveProds.class);
			query.setParameter("prodnbr", prodnbr);
			result = query.list();
			} catch (HibernateException e) {
				String msg = "Error reading sales number %s : salesnbr  ";
				logger.error(String.format(msg, prodnbr, e.getMessage()));
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
	@SuppressWarnings("unchecked")
	public List<CustWebActiveProds> getAllActiveProds() {
		List<CustWebActiveProds> result = null;
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from CustWebActiveProds ";
		try {
			Query query = session.createSQLQuery(sql).addEntity(CustWebActiveProds.class);
			result = query.list();
			} catch (HibernateException e) {
				String msg = "Error reading sales number %s : salesnbr  ";
				logger.error(String.format(msg, e.getMessage()));
				e.printStackTrace();
				throw(e);
		}
		catch(RuntimeException e) {
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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CustWebActiveProdsDaoImpl.logger = logger;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
