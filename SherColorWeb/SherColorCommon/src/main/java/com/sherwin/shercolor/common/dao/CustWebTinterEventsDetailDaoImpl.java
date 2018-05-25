package com.sherwin.shercolor.common.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetail;
import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetailPK;

@Repository
public class CustWebTinterEventsDetailDaoImpl implements CustWebTinterEventsDetailDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	static Logger logger = Logger.getLogger(CustWebTinterEventsDetailDaoImpl.class.getName());

	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	Locale locale;
	

	public CustWebTinterEventsDetail read(String guid, String type, String name){
		CustWebTinterEventsDetail record = null;
		CustWebTinterEventsDetailPK pkey = new CustWebTinterEventsDetailPK();
		
		Session session = getSessionFactory().getCurrentSession();

		pkey.setGuid(guid);
		pkey.setType(type);
		pkey.setName(name);

		try {
			record = (CustWebTinterEventsDetail) session.get(CustWebTinterEventsDetail.class, pkey);
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

	public boolean create(CustWebTinterEventsDetail teDetail){
		boolean retVal = false;
		
		Session session = getSessionFactory().getCurrentSession();

		try{
			session.save(teDetail);
			retVal = true;
		} catch (HibernateException he) {
			String msg = "Error creating record : %s  ";
			logger.error(String.format(msg, he.getMessage()));
			he.printStackTrace();
			throw(he);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return retVal;
	}
	
	@SuppressWarnings("unchecked")
	public List<CustWebTinterEventsDetail> listForGuid(String guid){
		List<CustWebTinterEventsDetail> recordList = new ArrayList<CustWebTinterEventsDetail>();
		
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CustWebTinterEventsDetail cwt ");
		hql.append("where cwt.guid = :guid ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("guid",guid)
					;
			
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

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
