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
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsProdListPK;
import com.sherwin.shercolor.common.domain.CdsProdList;

@Repository
@Transactional
public class CdsProdListDaoImpl implements CdsProdListDao {
	
	static Logger logger = LogManager.getLogger(CdsProdListDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
//	static Logger logger = Logger.getLogger(CdsProdListDaoImpl.class);

	public CdsProdList read(String prodListId, String salesNbr){
		CdsProdList record = null;
		CdsProdListPK pkey = new CdsProdListPK();
		Session session = sessionFactory.getCurrentSession();

		pkey.setProdListId(prodListId);
		pkey.setSalesNbr(salesNbr);

		try {
			record = (CdsProdList) session.get(CdsProdList.class,pkey);
		}
		catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw e;
		}
		catch(RuntimeException e) {
			logger.error(e.getMessage());
			throw e;
		}
		catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return record;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CdsProdList> getAllExclusions(String salesNbr) {
		List<CdsProdList> recordList = new ArrayList<CdsProdList>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CdsProdList cpl ");
		hql.append("where cpl.salesNbr = :salesNbr ");


		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("salesNbr",salesNbr);

			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
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