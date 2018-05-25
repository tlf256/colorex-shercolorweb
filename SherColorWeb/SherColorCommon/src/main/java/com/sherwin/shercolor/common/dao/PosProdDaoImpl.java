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

import com.sherwin.shercolor.common.domain.CustWebActiveProds;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.domain.PosProdPK;

@Repository
public class PosProdDaoImpl implements PosProdDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	private List<PosProd> posProdList;

	private List<CustWebActiveProds> activeProdList;


	static Logger logger = LogManager.getLogger(PosProdDaoImpl.class);

	public PosProd read(String salesNbr){
		PosProd record = null;
		PosProdPK pkey = new PosProdPK();

		Session session = getSessionFactory().getCurrentSession();
		
		pkey.setSalesNbr(salesNbr);
		
		try {
			record = (PosProd) session.get(PosProd.class,pkey);
		} catch (HibernateException e){
			String msg = "Error reading sales number %s : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return record;
	}
	
	@SuppressWarnings("unchecked")
	public PosProd readByProdNbrSzCd(String prodNbr, String szCd){
		PosProd record = null;
		List<PosProd> recordList = null;

		Session session = getSessionFactory().getCurrentSession();
		
		String hql = "from PosProd pp where pp.prodNbr = :prodNbr and pp.szCd = :szCd";
		
		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("prodNbr",prodNbr)
					.setParameter("szCd", szCd);
			
			recordList = query.list();
			
			//only expecting one record since prodNbr and szCd make it unique, grab first one from the list
			if (recordList.size()>0){
				record = recordList.get(0);
			} else {
				record = null;
			}
			
		} catch (HibernateException e){
			String msg = "Error reading prodNbr %s and szCd %s : %s  ";
			logger.error(String.format(msg, prodNbr, szCd, e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return record;
	}
	
	@SuppressWarnings("unchecked")
	public PosProd readByUpc(String upc){
		PosProd record = null;
		List<PosProd> recordList = null;
		
		Session session = getSessionFactory().getCurrentSession();
		
		String hql = "from PosProd pp where pp.upc = :upc";
		
		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("upc",upc);
			
			recordList = query.list();
			
			//only expecting one record since upc make it unique, grab first one from the list
			if (recordList.size()>0){
				record = recordList.get(0);
			} else {
				record = null;
			}

		} catch (HibernateException e){
			String msg = "Error reading by UPC %s : %s  ";
			logger.error(String.format(msg, upc, e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return record;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> stringListForAutocomplete(String partialProductInfo){
		List<String> productList = new ArrayList<String>();
		List<PosProd> recordList = null;
		Session session = getSessionFactory().getCurrentSession();
		StringBuilder productKey = new StringBuilder();
		String partialSalesNbr = partialProductInfo + "%";
		String partialProdNbr = partialProductInfo + "%";
		String partialUpc = partialProductInfo + "%";
		
		String hql = "from PosProd pp where pp.salesNbr like :partialSalesNbr or pp.prodNbr like :partialProdNbr or pp.upc like :partialUpc)";

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("partialSalesNbr",partialSalesNbr)
					.setParameter("partialProdNbr",partialProdNbr)
					.setParameter("partialUpc",partialUpc)
					;
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			throw(e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		for (PosProd record:recordList){
			productKey.setLength(0);
			productKey.append(record.getSalesNbr());
			productKey.append(" ");
			productKey.append(record.getProdNbr());
			productKey.append("-");
			productKey.append(record.getSzCd());
			productKey.append(" ");
			productKey.append(record.getUpc());
			
			productList.add(productKey.toString());
		}
		
		return productList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PosProd> listForAutocomplete(String partialProductInfo){

		List<PosProd> recordList = null;
		Session session = getSessionFactory().getCurrentSession();
		String partialSalesNbr = partialProductInfo + "%";
		String partialProdNbr = partialProductInfo + "%";
		String partialUpc = partialProductInfo + "%";
		
		String hql = "from PosProd pp where pp.salesNbr like :partialSalesNbr or pp.prodNbr like :partialProdNbr or pp.upc like :partialUpc)";

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("partialSalesNbr",partialSalesNbr)
					.setParameter("partialProdNbr",partialProdNbr)
					.setParameter("partialUpc",partialUpc)
					;
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			throw(e);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

	
		return recordList;
	}
		

	@SuppressWarnings("unchecked")
	public List<PosProd> listForAutocompleteActive(String partialProductId){
		// Product must be on the active product list (CustWebActiveProds table) to be eligible for selection.
		List<Object[]> result = null;
		posProdList = new ArrayList<PosProd>();
		activeProdList = new ArrayList<CustWebActiveProds>();
		PosProd posProd = null;
		CustWebActiveProds activeProd = null;
		Session session = sessionFactory.getCurrentSession();
		// So why did Lou list CustWebActiveProds first?  Listing PosProd first caused error that no one on web had answer!
		String sql = "select * from CUSTWEBACTIVEPRODS ap join POS_PROD pp on ap.salesnbr = pp.sales_nbr " + 
				"where pp.sales_nbr like :sales_nbr or pp.prod_nbr like :prod_nbr " +
				"or pp.upc = :upc";

		try {
			Query query = session.createSQLQuery(sql).addEntity(PosProd.class).addEntity(CustWebActiveProds.class);
			query.setParameter("sales_nbr", partialProductId + "%");
			query.setParameter("prod_nbr", partialProductId + "%");
			query.setParameter("upc", partialProductId + "%");
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
			posProd = (PosProd) aRow[0];
			activeProd = (CustWebActiveProds) aRow[1];
			posProdList.add(posProd);
			activeProdList.add(activeProd);
		}
		return posProdList;
	}
		
	@SuppressWarnings("unchecked")
	public List<PosProd> listForProdNbr(String searchCriteria, Boolean exactMatch){
		List<PosProd> recordList = new ArrayList<PosProd>();
		
		Session session = getSessionFactory().getCurrentSession();
		
		StringBuilder hql = new StringBuilder();

		hql.append("from PosProd pp ");
		if (exactMatch) {
			hql.append("where pp.prodNbr = :searchCriteria ");
		} else {
			hql.append("where pp.prodNbr like :searchCriteria ");
			searchCriteria = "%" + searchCriteria + "%";
		}
		hql.append(" order by pp.prodNbr, pp.szCd ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("searchCriteria",searchCriteria);
				
				recordList = query.list();

		} catch (HibernateException e){
			String msg = "Error searching for %s where exact match is %s : %s ";
			logger.error(String.format(msg, searchCriteria, exactMatch, e.getMessage()));
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
