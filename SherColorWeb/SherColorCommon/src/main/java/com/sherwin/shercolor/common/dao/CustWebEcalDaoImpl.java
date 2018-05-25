package com.sherwin.shercolor.common.dao;


import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Cache;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


import com.sherwin.shercolor.common.domain.CustWebEcal;


@EnableTransactionManagement
@Repository
@Transactional
public class CustWebEcalDaoImpl implements CustWebEcalDao{

	private final static Logger logger = LogManager.getLogger(CustWebEcalDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	protected void ClearCache(){
		Cache cache = sessionFactory.getCache();

		if (cache != null) {

			cache.evictCollectionRegions();
			cache.evictDefaultQueryRegion();
			cache.evictEntityRegions();
			cache.evictQueryRegions();
			cache.evictNaturalIdRegions();
		}
	}

	@Override
	public CustWebEcal selectGData(String colorantId)  {

		CustWebEcal myecal=null;
		try {
			if (getCurrentSession() != null) {
				getCurrentSession().clear(); // internal cache clear
			}
			Query query = getCurrentSession().createQuery("from CustWebEcal where customerid = :customerid "
					+ "and colorantid = :colorantid");
			query.setParameter("customerid", "GDATA");
			query.setParameter("colorantid", colorantId);
		
			myecal = (CustWebEcal)query.uniqueResult();
		} catch (HibernateException e) {
			logger.error(e.getStackTrace());
		}
		return myecal;
	}

	@Override
	public CustWebEcal selectEcal(String filename)  {

		CustWebEcal myecal=null;
		try {
			if (getCurrentSession() != null) {
				getCurrentSession().clear(); // internal cache clear
			}
			Query query = getCurrentSession().createQuery("from CustWebEcal where filename = :filename");
			query.setParameter("filename", filename);
			myecal = (CustWebEcal)query.uniqueResult();
		} catch (HibernateException e) {
			logger.error(e.getStackTrace());
		}
		return myecal;
	}
	@Override
	public int deleteEcal(String filename){
		int numrows = 0;
		try {
			if (getCurrentSession() != null) {
				getCurrentSession().clear(); // internal cache clear
			}
			Query query = getCurrentSession().createSQLQuery("delete from CUSTWEBECAL where filename = :filename");
			query.setParameter("filename", filename);
			numrows = query.executeUpdate();
		} catch (HibernateException e) {
			logger.error(e.getStackTrace());
		}
		return numrows;
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CustWebEcal> getEcalList( String customerid){
		List<CustWebEcal> ecal = new ArrayList<CustWebEcal>();

		Query query = getCurrentSession().createQuery("select colorantid,"
				+ " tintermodel,tinterserial,"
				+ "uploaddate,uploadtime,filename from CustWebEcal"
				+ " where customerid = :customerid " 
				+ "order by uploaddate desc, uploadtime desc");
		
		query.setParameter("customerid", customerid);
		
	
		List<Object[]> rows = query.list();
		for (Object[] row: rows) {
			CustWebEcal ecalrow= new CustWebEcal();
			ecalrow.setCustomerid(customerid);
			ecalrow.setColorantid((String) row[0]);
			ecalrow.setTintermodel((String) row[1]);
			ecalrow.setTinterserial((String) row[2]);
			ecalrow.setUploaddate((String) row[3]);
			ecalrow.setUploadtime((String) row[4]);
			ecalrow.setFilename((String) row[5]);
			ecal.add(ecalrow);
		}
		
		return ecal;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CustWebEcal> getEcalTemplate( String colorantid, String tintermodel){
		List<CustWebEcal> ecal = null;

		Query query = getCurrentSession().createQuery("from CustWebEcal where customerid = :customerid and"
				+ " colorantid=:colorantid and tintermodel=:tintermodel  order by uploaddate desc, uploadtime desc");
		query.setParameter("customerid", "DEFAULT");
		query.setParameter("colorantid", colorantid);
		query.setParameter("tintermodel", tintermodel);
	
		ecal =query.list();
		return ecal;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CustWebEcal> getEcalList( String customerid,  String tintermodel,	String tinterserial){
		List<CustWebEcal> ecal = new ArrayList<CustWebEcal>();

		Query query = getCurrentSession().createQuery("select colorantid,"
				+ " tintermodel,tinterserial,"
				+ "uploaddate,uploadtime,filename from CustWebEcal"
				+ " where customerid = :customerid and"
				+ " tintermodel=:tintermodel and tinterserial=:tinterserial order by uploaddate desc, uploadtime desc");
		query.setParameter("customerid", customerid);
		query.setParameter("tintermodel", tintermodel);
		query.setParameter("tinterserial", tinterserial);
	
		List<Object[]> rows = query.list();
		for (Object[] row: rows) {
			CustWebEcal ecalrow= new CustWebEcal();
			ecalrow.setColorantid((String) row[0]);
			ecalrow.setTintermodel((String) row[1]);
			ecalrow.setTinterserial((String) row[2]);
			ecalrow.setUploaddate((String) row[3]);
			ecalrow.setUploadtime((String) row[4]);
			ecalrow.setFilename((String) row[5]);
			ecal.add(ecalrow);
		}
		
		return ecal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustWebEcal> getEcalList( String customerid,  String colorantid, String tintermodel,	String tinterserial){

		List<CustWebEcal> ecalList=new ArrayList<CustWebEcal>();
		
		if(colorantid ==null){
			ecalList = getEcalList(customerid,tintermodel,tinterserial);
		}
		else{
			Query query = getCurrentSession().createQuery("select colorantid,"
					+ " tintermodel,tinterserial,"
					+ "uploaddate,uploadtime,filename from CustWebEcal"
		
				    + " where customerid = :customerid and "
					+ " colorantid=:colorantid and tintermodel=:tintermodel and tinterserial=:tinterserial"
					+ " order by uploaddate desc, uploadtime desc");
			query.setParameter("customerid", customerid);
			query.setParameter("colorantid", colorantid);
			query.setParameter("tintermodel", tintermodel);
			query.setParameter("tinterserial", tinterserial);
			List<Object[]> rows = query.list();
			for (Object[] row: rows) {
				CustWebEcal ecalrow= new CustWebEcal();
				ecalrow.setColorantid((String) row[0]);
				ecalrow.setTintermodel((String) row[1]);
				ecalrow.setTinterserial((String) row[2]);
				ecalrow.setUploaddate((String) row[3]);
				ecalrow.setUploadtime((String) row[4]);
				ecalrow.setFilename((String) row[5]);
				ecalList.add(ecalrow);
			}
			
		}
		return ecalList;
	}

	@Override
	public int uploadEcal(CustWebEcal ecal){
		int rc = 0;
		try {
			if (getCurrentSession() != null) {
				getCurrentSession().clear(); // internal cache clear
			}
			if(ecal!=null && ecal.getCustomerid()!=null && ecal.getData() !=null){
				getCurrentSession().save(ecal); 
				getCurrentSession().flush();
				System.out.println("Dao Saved.");
				logger.info("Ecal saved");

			}
			else if(ecal.getData()==null){
				System.out.println("No data in Ecal Upload");
				logger.error("No data in Ecal Upload");
				rc = -1;
			}
		} catch (HibernateException e) {
			logger.error(e.getStackTrace());
			rc = -1;
		}
		return rc;
	}



}
