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

import com.sherwin.shercolor.common.domain.CdsMiscCodes;
import com.sherwin.shercolor.common.domain.CdsMiscCodesPK;

@Repository
public class CdsMiscCodesDaoImpl implements CdsMiscCodesDao {

	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsMiscCodesDaoImpl.class);

	public CdsMiscCodes read(String miscType, String miscCode) {
		CdsMiscCodes record = null;
		CdsMiscCodesPK pkey = new CdsMiscCodesPK();

		Session session = getSessionFactory().getCurrentSession();

		pkey.setMiscType(miscType);
		pkey.setMiscCode(miscCode);

		try {
			record = (CdsMiscCodes) session.get(CdsMiscCodes.class, pkey);
		} catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw (e);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return record;
	}

	@SuppressWarnings("unchecked")
	public List<CdsMiscCodes> listForType(String miscType) {
		List<CdsMiscCodes> recordList = new ArrayList<CdsMiscCodes>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CdsMiscCodes cmc ");
		hql.append("where cmc.miscType = :miscType ");

		try {
			Query query = session.createQuery(hql.toString()).setParameter("miscType", miscType);

			recordList = query.list();

		} catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
			throw (e);
		} 		
		catch (Exception e) {
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
