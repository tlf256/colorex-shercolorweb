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

import com.sherwin.shercolor.common.domain.CdsProdNotesPK;
import com.sherwin.shercolor.common.domain.CdsProdNotes;

@Repository
@Transactional
public class CdsProdNotesDaoImpl implements CdsProdNotesDao {
	
	static Logger logger = LogManager.getLogger(CdsProdNotesDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	public CdsProdNotes read(String salesNbr, int seqNbr){
		CdsProdNotes record = null;
		CdsProdNotesPK pkey = new CdsProdNotesPK();
		Session session = sessionFactory.getCurrentSession();

		pkey.setSalesNbr(salesNbr);
		pkey.setSeqNbr(seqNbr);

		try {
			record = (CdsProdNotes) session.get(CdsProdNotes.class,pkey);
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
	public List<CdsProdNotes> listForSalesNbr(String salesNbr){
		List<CdsProdNotes> recordList = null;
		Session session = sessionFactory.getCurrentSession();

		String hql = "from CdsProdList cpL where cpL.salesNbr like :salesNbr";
		try {
			Query query = session.createQuery(hql.toString())
				.setParameter("salesNbr",salesNbr);
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
