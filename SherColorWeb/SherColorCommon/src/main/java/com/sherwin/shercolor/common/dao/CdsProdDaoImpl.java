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

import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CdsProdPK;
import com.sherwin.shercolor.common.domain.CustWebActiveProds;

@Repository
@Transactional
public class CdsProdDaoImpl implements CdsProdDao {
	static Logger logger = LogManager.getLogger(CdsProdDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	private List<CdsProd> cdsProdList;
	private List<CustWebActiveProds> activeProdList;

	public CdsProd read(String salesNbr){
		CdsProd record = null;
		CdsProdPK pkey = new CdsProdPK();
		
		Session session = sessionFactory.getCurrentSession();

		pkey.setSalesNbr(salesNbr);
		
		try {
			record = (CdsProd) session.get(CdsProd.class,pkey);
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
		catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return record;
	};

	@SuppressWarnings("unchecked")
	public List<CdsProd> listForBaseType(String base){
		List<CdsProd> recordList = null;
		Session session = sessionFactory.getCurrentSession();

		String hql = "from CdsProd cp where cp.base like :base";
		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("base",base);
			recordList = query.list();
		} catch (HibernateException e){
			String msg = "Error reading records with base %s : %s  ";
			logger.error(String.format(msg, base, e.getMessage()));
			throw e;
		} catch (Exception e) {
				logger.error(e.getMessage());
				throw e;
			}
		return recordList;
	};

	@SuppressWarnings("unchecked")
	// List of objects.
	public List<CdsProd> listForAutocompleteProduct(String partialProductId){
		List<CdsProd> recordList = null;
		Session session = sessionFactory.getCurrentSession();

		String hql = "from CdsProd cp where cp.salesNbr like :salesNbr or cp.prepComment like :salesNbr or cp.quality like :salesNbr";

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("salesNbr",partialProductId+"%");
			recordList = query.list();
		} catch (HibernateException e){
			String msg = "Error reading records with partialProductId %s : %s  ";
			logger.error(String.format(msg, partialProductId, e.getMessage()));
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return recordList;
	}
	
	@SuppressWarnings("unchecked")
	// List of objects.
	public List<CdsProd> listForAutocompleteProductActive(String partialProductId){
		List<Object[]> result = null;
		CdsProd cdsProd = null;
		CustWebActiveProds activeProd = null;
		cdsProdList = new ArrayList<CdsProd>();
		activeProdList = new ArrayList<CustWebActiveProds>();
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from CDS_PROD cp join CUSTWEBACTIVEPRODS ap on cp.sales_nbr = salesnbr"  
				 + " where sales_nbr like :sales_nbr or cp.prep_comment like :prep_comment or cp.quality like :quality";
		try {
			Query query = session.createSQLQuery(sql).addEntity(CdsProd.class).addEntity(CustWebActiveProds.class);
			query.setParameter("sales_nbr", partialProductId + "%");
			query.setParameter("prep_comment", partialProductId + "%");
			query.setParameter("quality", partialProductId + "%");
			result = query.list();
			} catch (HibernateException e) {
				String msg = "Error reading partialProductId number " + partialProductId;
				logger.error(String.format(msg, e.getMessage()));
				e.printStackTrace();
				throw(e);
		}
		catch(RuntimeException e) {
			logger.error(e.getMessage());
			throw e;
		}
		for (Object[] aRow : result){
			cdsProd = (CdsProd) aRow[0];
			activeProd = (CustWebActiveProds) aRow[1];
			cdsProdList.add(cdsProd);
			activeProdList.add(activeProd);
		}
		return cdsProdList;
	}

	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
