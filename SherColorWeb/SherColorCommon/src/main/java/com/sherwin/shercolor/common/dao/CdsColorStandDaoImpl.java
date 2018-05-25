package com.sherwin.shercolor.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CdsColorStand;
import com.sherwin.shercolor.common.domain.CdsColorStandPK;

public class CdsColorStandDaoImpl implements CdsColorStandDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	CdsColorMastDao cdsColorMastDao;

	static Logger logger = LogManager.getLogger(CdsColorStandDaoImpl.class);

	public CdsColorStand read(String spectroModel, String spectroMode, String colorComp, String colorId, int seqNbr){
		CdsColorStand record = null;
		CdsColorStandPK pkey = new CdsColorStandPK();
		
		Session session = getSessionFactory().getCurrentSession();

		pkey.setSpectroModel(spectroModel);
		pkey.setSpectroMode(spectroMode);
		pkey.setColorComp(colorComp);
		pkey.setColorId(colorId);
		pkey.setSeqNbr(seqNbr);
		
		try {
			record = (CdsColorStand) session.get(CdsColorStand.class,pkey);
		} catch (HibernateException e){
			String msg = "Error reading %s record from CdsColorStand : %s ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return record;
	}
	
	
	public List<CdsColorStand> listForColorCompId(String colorComp, String colorId, Date activityDate){
		List<CdsColorStand> recordList = null;

		CdsColorMast cdsColorMast = cdsColorMastDao.read(colorComp, colorId);
		
		if(cdsColorMast==null){
			recordList = new ArrayList<CdsColorStand>();
		} else {
			recordList = listForColorCompId(cdsColorMast, activityDate);
		}

		return recordList;
	}

	@SuppressWarnings("unchecked")
	public List<CdsColorStand> listForColorCompId(CdsColorMast cdsColorMast, Date activityDate){
		List<CdsColorStand> recordList = null;

		Session session = getSessionFactory().getCurrentSession();

		StringBuilder hql = new StringBuilder();
		hql.append("from CdsColorStand ccs ");
		hql.append("where ccs.spectroModel = 'XTS' ");
		hql.append("  and ccs.spectroMode = 'SCI' ");
		hql.append("  and ccs.colorComp = :colorComp ");
		hql.append("  and ccs.colorId = :colorId ");
		hql.append("  and ccs.effDate <= :activityDate ");
		hql.append("  and ccs.expDate >= :activityDate ");

		try {
			Query query;
			// first try by XrefComp & Id
			if(cdsColorMast.getXrefComp()!=null && !cdsColorMast.getXrefComp().isEmpty() && cdsColorMast.getXrefId()!=null && !cdsColorMast.getXrefId().isEmpty()){
				query = session.createQuery(hql.toString())
						.setParameter("colorComp",cdsColorMast.getXrefComp())
						.setParameter("colorId",cdsColorMast.getXrefId())
						.setParameter("activityDate",activityDate)
						;
				
				recordList = (List<CdsColorStand>) query.list();
			}
			if(recordList==null){
				// no record list so now try by colorComp & colorId
				query = session.createQuery(hql.toString())
						.setParameter("colorComp",cdsColorMast.getColorComp())
						.setParameter("colorId",cdsColorMast.getColorId())
						.setParameter("activityDate",activityDate)
						;
				
				recordList = (List<CdsColorStand>) query.list();
			}
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return recordList;
	}

	public CdsColorStand readByEffectiveVersion(String colorComp, String colorId, Date activityDate, String colorEngVer){
		CdsColorStand record = null;
		
		try {
			CdsColorMast cdsColorMast = cdsColorMastDao.read(colorComp, colorId);
			
			if(cdsColorMast!=null){
				record = readByEffectiveVersion(cdsColorMast, activityDate, colorEngVer);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return record;
	}
	
	@SuppressWarnings("unchecked")
	public CdsColorStand readByEffectiveVersion(CdsColorMast cdsColorMast, Date activityDate, String colorEngVer){
		CdsColorStand record = null;
		List<CdsColorStand> recordList = null;

		Session session = getSessionFactory().getCurrentSession();

		StringBuilder hqlVer1 = new StringBuilder();
		hqlVer1.append("from CdsColorStand ccs ");
		hqlVer1.append("where ccs.spectroModel = 'XTS' ");
		hqlVer1.append("  and ccs.spectroMode = 'SCI' ");
		hqlVer1.append("  and ccs.colorComp = :colorComp ");
		hqlVer1.append("  and ccs.colorId = :colorId ");
		hqlVer1.append("  and ccs.effDate <= :activityDate ");
		hqlVer1.append("  and ccs.expDate >= :activityDate ");
		hqlVer1.append("  order by ccs.seqNbr ");

		StringBuilder hqlVer2 = new StringBuilder();
		hqlVer2.append("from CdsColorStand ccs ");
		hqlVer2.append("where ccs.spectroModel = 'XTS' ");
		hqlVer2.append("  and ccs.spectroMode = 'SCI' ");
		hqlVer2.append("  and ccs.colorComp = :colorComp ");
		hqlVer2.append("  and ccs.colorId = :colorId ");
		hqlVer2.append("  and ccs.effDate <= :activityDate ");
		hqlVer2.append("  and ccs.expDate >= :activityDate ");
		hqlVer2.append("  and ccs.seqNbr >= 200 and ccs.seqNbr <= 299");
		hqlVer2.append("  order by ccs.seqNbr desc ");

		try {
			Query query;
			// first try by XrefComp & Id
			if(cdsColorMast.getXrefComp()!=null && !cdsColorMast.getXrefComp().isEmpty() && cdsColorMast.getXrefId()!=null && !cdsColorMast.getXrefId().isEmpty()){
				if(colorEngVer!=null && colorEngVer.startsWith("2")){
					query = session.createQuery(hqlVer2.toString())
							.setParameter("colorComp",cdsColorMast.getXrefComp())
							.setParameter("colorId",cdsColorMast.getXrefId())
							.setParameter("activityDate",activityDate)
							;
					
					recordList = (List<CdsColorStand>) query.list();
				}
				if(recordList==null || recordList.size()==0){
					// nothing found for v2, try v1
					query = session.createQuery(hqlVer1.toString())
							.setParameter("colorComp",cdsColorMast.getXrefComp())
							.setParameter("colorId",cdsColorMast.getXrefId())
							.setParameter("activityDate",activityDate)
							;
					recordList = (List<CdsColorStand>) query.list();
				}
			}
			// if nothing found from xref attempt, try with colorComp and colorId
			if(recordList==null || recordList.size()==0){
				if(colorEngVer!=null && colorEngVer.startsWith("2")){
					query = session.createQuery(hqlVer2.toString())
							.setParameter("colorComp",cdsColorMast.getColorComp())
							.setParameter("colorId",cdsColorMast.getColorId())
							.setParameter("activityDate",activityDate)
							;
					
					recordList = (List<CdsColorStand>) query.list();
				}
				if(recordList==null || recordList.size()==0){
					// nothing found for v2, try v1
					query = session.createQuery(hqlVer1.toString())
							.setParameter("colorComp",cdsColorMast.getColorComp())
							.setParameter("colorId",cdsColorMast.getColorId())
							.setParameter("activityDate",activityDate)
							;
					recordList = (List<CdsColorStand>) query.list();
				}
			}
			
			if (recordList!=null && recordList.size()>0){
				record = recordList.get(0);
			} else {
				record = null;
			}
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hqlVer1.toString(), e.getMessage()));
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return record;
	}
	
	@SuppressWarnings("unchecked")
	public List<CdsColorStand> listForLABRanges(String colorComp, Double minLValue, Double maxLValue, Double minAValue, Double maxAValue, Double minBValue, Double maxBValue){
		List<CdsColorStand> recordList = null;

		Session session = getSessionFactory().getCurrentSession();
		
		Date currentDate = new Date();
		
		if(colorComp==null || colorComp.isEmpty()) colorComp = null;
		
		StringBuilder hql = new StringBuilder();
		hql.append("from CdsColorStand ccs ");
		hql.append("where ccs.spectroModel = 'XTS' ");
		hql.append("  and ccs.spectroMode = 'SCI' ");
		hql.append("  and (:colorComp is null or ccs.colorComp = :colorComp) ");
		hql.append("  and ccs.effDate <= :currentDate ");
		hql.append("  and ccs.expDate >= :currentDate ");
		hql.append("  and ccs.cieLValue >= :minLValue ");
		hql.append("  and ccs.cieLValue <= :maxLValue ");
		hql.append("  and ccs.cieAValue >= :minAValue ");
		hql.append("  and ccs.cieAValue <= :maxAValue ");
		hql.append("  and ccs.cieBValue >= :minBValue ");
		hql.append("  and ccs.cieBValue <= :maxBValue ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("colorComp",colorComp)
					.setParameter("currentDate",currentDate)
					.setParameter("minLValue",minLValue)
					.setParameter("maxLValue",maxLValue)
					.setParameter("minAValue",minAValue)
					.setParameter("maxAValue",maxAValue)
					.setParameter("minBValue",minBValue)
					.setParameter("maxBValue",maxBValue)
					;
			
			recordList = (List<CdsColorStand>) query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
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
