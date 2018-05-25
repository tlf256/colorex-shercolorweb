package com.sherwin.shercolor.cal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.cal.dao.CalDao;
import com.sherwin.shercolor.cal.domain.CalTemplate;
import com.sherwin.shercolor.cal.domain.Ecal;

@Service
@Transactional
public class CalServiceImpl implements CalService{
	@Autowired
	CalDao dao;
	
	@Override
	public CalTemplate getCalTemplate(String filename){
		CalTemplate blob = dao.selectBlob(filename);
		return blob;
	}
	@Override
	public List<Ecal> getEcalList(String customerid,String colorantid,String tintermodel,	String tinterserial){
		List<Ecal> ecalList = null;
		ecalList = dao.getEcalList( customerid, colorantid, tintermodel,	 tinterserial);
		return ecalList;
	}
	
	@Override
	public Ecal selectEcal(String filename){
		Ecal ecal = null;
		ecal = dao.selectEcal(filename);
		return ecal;
	}
	@Override
	public void uploadEcal(Ecal ecal){
		dao.uploadEcal(ecal);
	}
	@Override
	public void setDao(CalDao dao) {
		this.dao = dao;
	}
	
	
}
