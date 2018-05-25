package com.sherwin.shercolor.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.dao.CustWebEcalDao;
import com.sherwin.shercolor.common.domain.CustWebEcal;

@Service
@Transactional
public class EcalServiceImpl implements EcalService{
	@Autowired
	CustWebEcalDao dao;
	
	
	
	@Override
	public List<CustWebEcal> getEcalTemplate(String colorantid, String tintermodel) {
		List<CustWebEcal> ecalList = null;
		ecalList = dao.getEcalTemplate( colorantid, tintermodel);
		return ecalList;
	}

	@Override
	public List<CustWebEcal> getEcalsByCustomer(String customerid) {
		List<CustWebEcal> ecalList = null;
		ecalList = dao.getEcalList(customerid);
		return ecalList;
	}

	@Override
	public List<CustWebEcal> getEcalList(String customerid,String colorantid,String tintermodel,	String tinterserial){
		List<CustWebEcal> ecalList = null;
		ecalList = dao.getEcalList( customerid, colorantid, tintermodel,	 tinterserial);
		return ecalList;
	}
	
	
	@Override
	public CustWebEcal selectGData(String colorantId){
		CustWebEcal ecal = null;
		ecal = dao.selectGData(colorantId);
		return ecal;
	}
	@Override
	public CustWebEcal selectEcal(String filename){
		CustWebEcal ecal = null;
		ecal = dao.selectEcal(filename);
		return ecal;
	}
	@Override
	public void uploadEcal(CustWebEcal ecal){
		dao.uploadEcal(ecal);
	}
	@Override
	public void setDao(CustWebEcalDao dao) {
		this.dao = dao;
	}
	
	
}
