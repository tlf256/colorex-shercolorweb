package com.sherwin.shercolor.common.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.CustWebParmsPK;

@Repository
public class CustWebParmsDaoImpl implements CustWebParmsDao {

	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CustWebParmsDaoImpl.class);

	public CustWebParms read(String customerId, int SeqNbr) {
		CustWebParms record = null;
		CustWebParmsPK pkey = new CustWebParmsPK();
		Session session = getSessionFactory().getCurrentSession();

		pkey.setCustomerId(customerId);
		pkey.setSeqNbr(SeqNbr);

		try {
			record = (CustWebParms) session.get(CustWebParms.class, pkey);
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

	public CustWebParms readByCustId(String customerId) {
		CustWebParms record = null;
		CustWebParmsPK pkey = new CustWebParmsPK();
		Session session = getSessionFactory().getCurrentSession();

		pkey.setCustomerId(customerId);
		pkey.setSeqNbr(1);

		try {
			record = (CustWebParms) session.get(CustWebParms.class, pkey);
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
	public List<CustWebParms> readAllByCustId(String customerId) {
		List<CustWebParms> custList = new ArrayList<CustWebParms>();
		Session session = getSessionFactory().getCurrentSession();

		StringBuilder hql = new StringBuilder();
		hql.append("from CustWebParms cwp ");
		hql.append("where cwp.customerId = :customerId ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("customerId",customerId);
			
			custList = query.list();
		} catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			custList = new ArrayList<CustWebParms>();
			throw(e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return custList;


	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
