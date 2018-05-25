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

import com.sherwin.shercolor.common.domain.CustWebTinterEvents;
import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetail;
import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetailPK;
import com.sherwin.shercolor.common.domain.CustWebTinterEventsPK;

public class CustWebTinterEventsDaoImpl implements CustWebTinterEventsDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	static Logger logger = Logger.getLogger(CustWebTinterEventsDaoImpl.class.getName());

	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	Locale locale;

	public CustWebTinterEvents read(String guid){
		CustWebTinterEvents record = null;
		
		CustWebTinterEventsPK pkey = new CustWebTinterEventsPK();
		
		Session session = getSessionFactory().getCurrentSession();

		pkey.setGuid(guid);
		try {
			record = (CustWebTinterEvents) session.get(CustWebTinterEvents.class, pkey);
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

	public String create(CustWebTinterEvents tintEvent){
		String returnGuid;
		
		Session session = getSessionFactory().getCurrentSession();

		try{
			returnGuid = java.util.UUID.randomUUID().toString();
			tintEvent.setGuid(returnGuid);
			session.save(tintEvent);
		} catch (HibernateException he) {
			returnGuid = null;
			String msg = "Error creating record : %s  ";
			logger.error(String.format(msg, he.getMessage()));
			he.printStackTrace();
			throw(he);
		} catch (Exception e) {
			returnGuid = null;
			logger.error(e.getMessage());
			throw e;
		}
		
		return returnGuid;
		
	}
	
	public CustWebTinterEvents findLastPurge(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr){
		CustWebTinterEvents record = null;

		try {
			List<CustWebTinterEvents> recordList = listForTinterAndFunction(customerId, clrntSysId, tinterModel, tinterSerialNbr, "PurgeAll", false);
			
			if(recordList!=null && recordList.size()>0){
				for(CustWebTinterEvents item : recordList){
					if(item.getErrorStatus()!=null && item.getErrorStatus().equals("0")){
						record = item;
						break;
					}
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return record;
		
		
	}

	@SuppressWarnings("unchecked")
	public List<CustWebTinterEvents> listForTinterAndFunction(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr, String function, boolean ascendingOrder){
		List<CustWebTinterEvents> recordList = new ArrayList<CustWebTinterEvents>();
		
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CustWebTinterEvents cwt ");
		hql.append("where cwt.customerId = :customerId ");
		hql.append("  and cwt.clrntSysId = :clrntSysId ");
		hql.append("  and cwt.tinterModel = :tinterModel ");
		hql.append("  and cwt.tinterSerialNbr = :tinterSerialNbr ");
		hql.append("  and cwt.function = :function ");
		if(ascendingOrder) hql.append(" order by cwt.dateTime ");
		else hql.append(" order by cwt.dateTime desc" );


		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("customerId",customerId)
					.setParameter("clrntSysId",clrntSysId)
					.setParameter("tinterModel",tinterModel)
					.setParameter("tinterSerialNbr",tinterSerialNbr)
					.setParameter("function",function)
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
