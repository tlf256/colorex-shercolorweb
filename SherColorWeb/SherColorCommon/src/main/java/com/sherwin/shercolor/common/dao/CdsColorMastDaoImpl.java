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

import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CdsColorMastPK;


@Repository
@Transactional
public class CdsColorMastDaoImpl implements CdsColorMastDao{
	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsColorMastDaoImpl.class);
	
	public CdsColorMast read(String colorComp, String colorId){
		CdsColorMast record = null;
		CdsColorMastPK pkey = new CdsColorMastPK();
		
		Session session = getSessionFactory().getCurrentSession();

		pkey.setColorComp(colorComp);
		pkey.setColorId(colorId);
		
		try {
			record = (CdsColorMast) session.get(CdsColorMast.class,pkey);
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return record;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listOfColorCompanies(){
		List<String> companyList = new ArrayList<String>();
		Session session = getSessionFactory().getCurrentSession();

		String hql = "select distinct colorComp from CdsColorMast ccm where ccm.expDate >= current_date or ccm.expDate is null order by colorComp";

		try {
			Query query = session.createQuery(hql);
			
			companyList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			companyList = new ArrayList<String>();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return companyList;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> stringListForSwColorAutocomplete(String partialColorInfo){
		List<String> colorList = new ArrayList<String>();
		List<CdsColorMast> recordList = null;
		Session session = getSessionFactory().getCurrentSession();
		StringBuilder colorKey = new StringBuilder();
		String partialColorId = partialColorInfo + "%";
		//String partialColorName = partialColorInfo + "%";
		// 3-7-2017*BKP*Modified to have color name be "contains" instead of "begins"
		String partialColorName = "%" + partialColorInfo + "%";
		String partialLocId = partialColorInfo + "%";
		
		String hql = "from CdsColorMast ccm where ccm.colorComp = 'SHERWIN-WILLIAMS' and (ccm.expDate >= current_date or ccm.expDate is null) and (ccm.colorId like :partialColorId or ccm.colorName like :partialColorName or ccm.locId like :partialLocId)";

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("partialColorId",partialColorId)
					.setParameter("partialColorName",partialColorName)
					.setParameter("partialLocId",partialLocId)
					;
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			recordList = new ArrayList<CdsColorMast>();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		for (CdsColorMast record:recordList){
			colorKey.setLength(0);
			colorKey.append(record.getColorId());
			colorKey.append(" ");
			colorKey.append(record.getColorName());
			colorKey.append(" ");
			colorKey.append(record.getLocId());
			
			colorList.add(colorKey.toString());
		}
		
		return colorList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CdsColorMast> listForSwColorAutocomplete(String partialColorInfo){
		List<CdsColorMast> recordList = null;
		Session session = getSessionFactory().getCurrentSession();
		String partialColorId = partialColorInfo + "%";
		//String partialColorName = partialColorInfo + "%";
		// 3-7-2017*BKP*Modified to have color name be "contains" instead of "begins"
		String partialColorName = "%" + partialColorInfo + "%";
		String partialLocId = partialColorInfo + "%";

		//logger.info("In listForSwColorAutocomplete");
		
		String hql = "from CdsColorMast ccm where ccm.colorComp = 'SHERWIN-WILLIAMS' and (ccm.expDate >= current_date or ccm.expDate is null) and (ccm.colorId like :partialColorId or ccm.colorName like :partialColorName or ccm.locId like :partialLocId)";

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("partialColorId",partialColorId)
					.setParameter("partialColorName",partialColorName)
					.setParameter("partialLocId",partialLocId)
					;
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			recordList = new ArrayList<CdsColorMast>();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

	
		return recordList;
		
	}

	@SuppressWarnings("unchecked")
	public List<String> stringListForCompetColorAutocomplete(String partialColorInfo){
		List<String> colorList = new ArrayList<String>();
		List<CdsColorMast> recordList = null;
		Session session = getSessionFactory().getCurrentSession();
		StringBuilder colorKey = new StringBuilder();
		String partialColorId = partialColorInfo + "%";
		// 3-7-2017*BKP*Modified to have color name be "contains" instead of "begins"
		String partialColorName = "%" + partialColorInfo + "%";
		//String partialColorName = partialColorInfo + "%";
		
		String hql = "from CdsColorMast ccm where ccm.colorComp <> 'SHERWIN-WILLIAMS' and (ccm.expDate >= current_date or ccm.expDate is null) and (ccm.colorId like :partialColorId or ccm.colorName like :partialColorName)";

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("partialColorId",partialColorId)
					.setParameter("partialColorName",partialColorName)
					;
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			recordList = new ArrayList<CdsColorMast>();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		for (CdsColorMast record:recordList){
			colorKey.setLength(0);
			colorKey.append(record.getColorComp());
			colorKey.append(" ");
			colorKey.append(record.getColorId());
			colorKey.append(" ");
			colorKey.append(record.getColorName());
			
			colorList.add(colorKey.toString());
		}
		
		return colorList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CdsColorMast> listForCompetColorAutocomplete(String partialColorInfo){
		
		List<CdsColorMast> recordList = null;
		Session session = getSessionFactory().getCurrentSession();
		
		String partialColorId = partialColorInfo + "%";
		//String partialColorName = partialColorInfo + "%";
		// 3-7-2017*BKP*Modified to have color name be "contains" instead of "begins"
		String partialColorName = "%" + partialColorInfo + "%";
		
		String hql = "from CdsColorMast ccm where ccm.colorComp <> 'SHERWIN-WILLIAMS' and (ccm.expDate >= current_date or ccm.expDate is null) and (ccm.colorId like :partialColorId or ccm.colorName like :partialColorName)";

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("partialColorId",partialColorId)
					.setParameter("partialColorName",partialColorName)
					;
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			recordList = new ArrayList<CdsColorMast>();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return recordList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listForAutocompleteColorId(String partialColorId){
		List<String> colorList = new ArrayList<String>();
		List<CdsColorMast> recordList = null;
		Session session = getSessionFactory().getCurrentSession();
		StringBuilder colorKey = new StringBuilder();
		
		String hql = "from CdsColorMast ccm where ccm.colorId like :partialColorId and (ccm.expDate >= current_date or ccm.expDate is null)" ;

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("partialColorId",partialColorId+"%");
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			
			recordList = new ArrayList<CdsColorMast>();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		for (CdsColorMast record:recordList){
			colorKey.setLength(0);
			colorKey.append(record.getColorComp());
			colorKey.append(" ");
			colorKey.append(record.getColorId());
			colorKey.append(" ");
			colorKey.append(record.getColorName());
			
			colorList.add(colorKey.toString());
		}
		
		return colorList;
	}

	@SuppressWarnings("unchecked")
	public List<String> listForAutocompleteSwColorId(String partialColorId){
		List<String> colorList = new ArrayList<String>();
		List<CdsColorMast> recordList = null;
		Session session = getSessionFactory().getCurrentSession();
		StringBuilder colorKey = new StringBuilder();
		
		String hql = "from CdsColorMast ccm where ccm.colorComp = 'SHERWIN-WILLIAMS' and (ccm.expDate >= current_date or ccm.expDate is null) and ccm.colorId like :partialColorId";

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("partialColorId",partialColorId+"%");
			
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

		for (CdsColorMast record:recordList){
			colorKey.setLength(0);
			colorKey.append(record.getColorComp());
			colorKey.append(" ");
			colorKey.append(record.getColorId());
			colorKey.append(" ");
			colorKey.append(record.getColorName());
			
			colorList.add(colorKey.toString());
		}
		
		return colorList;
	}

	@SuppressWarnings("unchecked")
	public List<String> listForAutocompleteColorName(String partialColorName){
		List<String> colorList = new ArrayList<String>();
		List<CdsColorMast> recordList = null;
		Session session = getSessionFactory().getCurrentSession();
		StringBuilder colorKey = new StringBuilder();
		
		String hql = "from CdsColorMast ccm where ccm.colorName like :partialColorName and (ccm.expDate >= current_date or ccm.expDate is null)";

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("partialColorName","%"+partialColorName+"%");
					//.setParameter("partialColorName",partialColorName+"%");
			
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

		for (CdsColorMast record:recordList){
			colorKey.setLength(0);
			colorKey.append(record.getColorComp());
			colorKey.append(" ");
			colorKey.append(record.getColorId());
			colorKey.append(" ");
			colorKey.append(record.getColorName());
			
			colorList.add(colorKey.toString());
		}
		
		return colorList;
	}

	@SuppressWarnings("unchecked")
	public List<String> listForAutocompleteSwColorName(String partialColorName){
		List<String> colorList = new ArrayList<String>();
		List<CdsColorMast> recordList = null;
		Session session = getSessionFactory().getCurrentSession();
		StringBuilder colorKey = new StringBuilder();
		
		String hql = "from CdsColorMast ccm where ccm.colorComp = 'SHERWIN-WILLIAMS' and (ccm.expDate >= current_date or ccm.expDate is null) and ccm.colorName like :partialColorName";

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("partialColorName","%"+partialColorName+"%");
					//.setParameter("partialColorName",partialColorName+"%");
			
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

		for (CdsColorMast record:recordList){
			colorKey.setLength(0);
			colorKey.append(record.getColorComp());
			colorKey.append(" ");
			colorKey.append(record.getColorId());
			colorKey.append(" ");
			colorKey.append(record.getColorName());
			
			colorList.add(colorKey.toString());
		}
		
		return colorList;
	}

	@SuppressWarnings("unchecked")
	public List<CdsColorMast> listForColorNameMatch(String searchCriteria, Boolean exactMatch){
		List<CdsColorMast> recordList = null;
		StringBuilder hql = new StringBuilder();
		Session session = getSessionFactory().getCurrentSession();
		
		hql.append("from CdsColorMast ccm ");
		if (exactMatch) {
			hql.append("where ccm.colorName = :searchCriteria ");
		} else {
			hql.append("where ccm.colorName like :searchCriteria ");
			searchCriteria = "%" + searchCriteria + "%";
		}
		hql.append(" and (ccm.expDate >= current_date or ccm.expDate is null) order by ccm.colorComp, ccm.colorId");
		
		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("searchCriteria",searchCriteria);
			
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
	
	@SuppressWarnings("unchecked")
	public List<CdsColorMast> listForColorIdMatch(String searchCriteria, Boolean exactMatch){
		List<CdsColorMast> recordList = null;
		
		Session session = getSessionFactory().getCurrentSession();
		
		StringBuilder hql = new StringBuilder();

		hql.append("from CdsColorMast ccm ");
		if (exactMatch) {
			hql.append("where ccm.colorId = :searchCriteria ");
		} else {
			hql.append("where ccm.colorId like :searchCriteria ");
			searchCriteria = "%" + searchCriteria + "%";
		}
		hql.append(" and (ccm.expDate >= current_date or ccm.expDate is null) order by ccm.colorComp, ccm.colorId ");
		
		try {
			Query query = session.createQuery(hql.toString())
				.setParameter("searchCriteria",searchCriteria);
			
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
