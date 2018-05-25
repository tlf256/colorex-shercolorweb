package com.sherwin.shercolor.common.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CdsFormulaChgList;
import com.sherwin.shercolor.common.domain.CdsFormulaChgListPK;

@Repository
public class CdsFormulaChgListDaoImpl implements CdsFormulaChgListDao {

	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsFormulaChgListDaoImpl.class);

	public CdsFormulaChgList read(String colorComp, String colorId, String salesNbr, String clrntSysId) {
		CdsFormulaChgList record = null;
		CdsFormulaChgListPK pkey = new CdsFormulaChgListPK();

		Session session = getSessionFactory().getCurrentSession();

		pkey.setColorComp(colorComp);
		pkey.setColorId(colorId);
		pkey.setSalesNbr(salesNbr);
		pkey.setClrntSysId(clrntSysId);
		
		try {
			record = (CdsFormulaChgList) session.get(CdsFormulaChgList.class, pkey);
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
	public List<CdsFormulaChgList> listDeletesForColorAndProduct(String colorComp, String colorId, String salesNbr){
		List<CdsFormulaChgList> recordList = new ArrayList<CdsFormulaChgList>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CdsFormulaChgList afcd ");
		hql.append("where afcd.colorComp = :colorComp ");
		hql.append("  and afcd.colorId = :colorId ");
		hql.append("  and afcd.salesNbr = :salesNbr ");
		hql.append("  and afcd.typeCode = 'D' ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("colorComp",colorComp)
					.setParameter("colorId",  colorId)
					.setParameter("salesNbr",  salesNbr);
			
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
	public List<String> listProductForAlternateBaseRequired(String colorComp, String colorId){
		List<String> productNbrList = new ArrayList<String>();
		StringBuilder sql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		sql.append("select NVL(msg.MessageText,'NA') as MessageText ");
		sql.append("from Cds_Formula_Chg_List afcd ");
		sql.append("left outer join Cds_Message msg on msg.CdsMessageId = afcd.CdsMessageId ");
		sql.append("where afcd.Color_Comp = :selectColorComp ");
		sql.append("  and afcd.Color_Id = :selectColorId ");
		sql.append("  and msg.MessageText like '%Please use this color with%base%' ");

		try {
			Query query = session.createSQLQuery(sql.toString())
					.addScalar("MessageText",new StringType())
					.setParameter("selectColorComp", colorComp)
					.setParameter("selectColorId", colorId);

			//List<Object[]> rows = query.list();
			List<String> rows = query.list();
			for(String message : rows){
				//String message = row[1].toString();
				//String message = row.toString();
				// pull prodNbr from message. It is between the last word "with" and the last word "base"
				if(!message.trim().equalsIgnoreCase("NA")){
					int idx1 = message.lastIndexOf("with");
					int idx2 = message.lastIndexOf("base");
					if(idx2>(idx1+4)){
						String prodNbr = message.substring(idx1+4, idx2-1).trim();
						if(!productNbrList.contains(prodNbr)){
							productNbrList.add(prodNbr);
						}
					}
				}
			}

		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, sql.toString(), e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return productNbrList;
		

	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
