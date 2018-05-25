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

import com.sherwin.shercolor.common.domain.CustWebJobFields;
import com.sherwin.shercolor.common.domain.CustWebJobFieldsPK;

public class CustWebJobFieldsDaoImpl implements CustWebJobFieldsDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CustWebJobFieldsDaoImpl.class);

	public CustWebJobFields read(String customerId, int seqNbr){
		CustWebJobFields record = null;
		CustWebJobFieldsPK pkey = new CustWebJobFieldsPK();
		Session session = getSessionFactory().getCurrentSession();

		pkey.setCustomerId(customerId);
		pkey.setSeqNbr(seqNbr);

		try {
			record = (CustWebJobFields) session.get(CustWebJobFields.class, pkey);
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
	public List<CustWebJobFields> listForCustomerId (String customerId){
		List<CustWebJobFields> recordList = new ArrayList<CustWebJobFields>();
		Session session = getSessionFactory().getCurrentSession();

		StringBuilder hql = new StringBuilder();
		hql.append("from CustWebJobFields cwjf ");
		hql.append("where cwjf.customerId = :customerId ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("customerId",customerId);
			
			recordList = query.list();
		} catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			recordList = new ArrayList<CustWebJobFields>();
			throw(e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return recordList;
		
	}
	
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
