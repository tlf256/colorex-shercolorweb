package com.sherwin.shercolor.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sherwin.shercolor.common.domain.CdsClrnt;
import com.sherwin.shercolor.common.domain.CdsClrntPK;

public class CdsClrntDaoImpl implements CdsClrntDao {

	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsClrntDaoImpl.class);

	public CdsClrnt read(String clrntSysId, String tintSysId) {

		CdsClrnt record = null;
		CdsClrntPK pkey = new CdsClrntPK();

		Session session = getSessionFactory().getCurrentSession();

		pkey.setClrntSysId(clrntSysId);
		pkey.setTintSysId(tintSysId);

		try {
			record = (CdsClrnt) session.get(CdsClrnt.class, pkey);
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

	@SuppressWarnings("unchecked")
	public CdsClrnt readByFbSysId(String clrntSysId, String fbSysId) {

		CdsClrnt record = null;
		List<CdsClrnt> recordList = null;
		Session session = getSessionFactory().getCurrentSession();

		String hql = "from CdsClrnt cc where cc.clrntSysId = :clrntSysId and cc.fbSysId = :fbSysId";

		try {
			Query query = session.createQuery(hql.toString()).setParameter("clrntSysId", clrntSysId)
					.setParameter("fbSysId", fbSysId);

			recordList = query.list();

			if (recordList.size() > 0) {
				record = recordList.get(0);
			} else {
				record = null;
			}

		} catch (HibernateException e) {
			String msg = "Error reading clrntSysId %s and fbSysId %s : %s  ";
			logger.error(String.format(msg, clrntSysId, fbSysId, e.getMessage()));
			e.printStackTrace();
			throw (e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return record;
	}

	@SuppressWarnings("unchecked")
	public List<CdsClrnt> listForClrntSys(String clrntSysId, Boolean activeOnly) {
		List<CdsClrnt> recordList = null;
		Session session = sessionFactory.getCurrentSession();

		if (activeOnly) {
			String statusInd = "A";
			String hql = "from CdsClrnt cc where cc.clrntSysId like :clrntSysId and statusInd = :statusInd";
			try {
				Query query = session.createQuery(hql.toString()).setParameter("clrntSysId", clrntSysId)
						.setParameter("statusInd", statusInd);
				recordList = query.list();
			} catch (HibernateException e) {
				String msg = "Error reading clrntSysId %s and statusInd %s : %s  ";
				logger.error(String.format(msg, clrntSysId, statusInd, e.getMessage()));
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw e;
			}
		} else {
			String hql = "from CdsClrnt cc where cc.clrntSysId like :clrntSysId";
			try {
				Query query = session.createQuery(hql.toString()).setParameter("clrntSysId", clrntSysId);
				recordList = query.list();
			} catch (HibernateException e) {
				String msg = "Error reading clrntSysId %s : %s  ";
				logger.error(String.format(msg, clrntSysId, e.getMessage()));
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw e;
			}
		}
		return recordList;
	}
	
	public HashMap<String,CdsClrnt> mapTintSysIdForClrntSys(String clrntSysId, Boolean activeOnly){
		HashMap<String,CdsClrnt> map = new HashMap<String,CdsClrnt>();
		
		List<CdsClrnt> recordList = null;
		Session session = sessionFactory.getCurrentSession();

		if (activeOnly) {
			String statusInd = "A";
			String hql = "from CdsClrnt cc where cc.clrntSysId like :clrntSysId and statusInd = :statusInd";
			try {
				Query query = session.createQuery(hql.toString()).setParameter("clrntSysId", clrntSysId)
						.setParameter("statusInd", statusInd);
				recordList = query.list();
			} catch (HibernateException e) {
				String msg = "Error reading clrntSysId %s and statusInd %s : %s  ";
				logger.error(String.format(msg, clrntSysId, statusInd, e.getMessage()));
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw e;
			}
		} else {
			String hql = "from CdsClrnt cc where cc.clrntSysId like :clrntSysId";
			try {
				Query query = session.createQuery(hql.toString()).setParameter("clrntSysId", clrntSysId);
				recordList = query.list();
			} catch (HibernateException e) {
				String msg = "Error reading clrntSysId %s : %s  ";
				logger.error(String.format(msg, clrntSysId, e.getMessage()));
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw e;
			}
		}
		

		for(CdsClrnt clrnt : recordList){
			map.put(clrnt.getTintSysId(), clrnt);
		}
		
		return map;
	}
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
