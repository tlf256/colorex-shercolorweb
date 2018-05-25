package com.sherwin.shercolor.common.dao;

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

import com.sherwin.shercolor.common.domain.CdsRoomList;
import com.sherwin.shercolor.common.domain.CdsRoomListPK;

@Repository
@Transactional
public class CdsRoomListDaoImpl implements CdsRoomListDao {
	
	static Logger logger = LogManager.getLogger(CdsRoomListDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public CdsRoomList read(String listName, int seqNbr) {
		CdsRoomList record = null;
		CdsRoomListPK pkey = new CdsRoomListPK();
		
		Session session = sessionFactory.getCurrentSession();

		pkey.setListName(listName);
		pkey.setSeqNbr(seqNbr);
		
		try {
			record = (CdsRoomList) session.get(CdsRoomList.class,pkey);
		}
		catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
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
	public List<CdsRoomList> listForName(String listName) {
		List<CdsRoomList> recordList = null;
		Session session = sessionFactory.getCurrentSession();

		String hql = "from CdsRoomList crl where crl.listName like :listName";
		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("listName",listName);
			recordList = query.list();
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
			throw e;
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
