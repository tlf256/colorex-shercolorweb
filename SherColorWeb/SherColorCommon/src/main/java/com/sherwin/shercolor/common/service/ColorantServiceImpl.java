package com.sherwin.shercolor.common.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.dao.CdsClrntDao;
import com.sherwin.shercolor.common.dao.CdsClrntIncrDao;
import com.sherwin.shercolor.common.dao.CdsClrntListDao;
import com.sherwin.shercolor.common.dao.CdsClrntSysDao;
import com.sherwin.shercolor.common.dao.CdsProdCharzdDao;
import com.sherwin.shercolor.common.dao.CdsProdListDao;
import com.sherwin.shercolor.common.dao.PosProdDao;
import com.sherwin.shercolor.common.domain.CdsClrnt;
import com.sherwin.shercolor.common.domain.CdsClrntIncr;
import com.sherwin.shercolor.common.domain.CdsClrntList;
import com.sherwin.shercolor.common.domain.CdsClrntSys;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CdsProdList;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.exception.SherColorExceptionBuilder;

@Service
@Transactional
public class ColorantServiceImpl implements ColorantService {
	
	static Logger logger = LogManager.getLogger(ColorantServiceImpl.class);

	@Autowired
	PosProdDao posProdDao;
	
	@Autowired
	CdsProdCharzdDao prodCharzdDao;
	
	@Autowired
	CdsProdListDao cdsProdListDao;
	
	@Autowired
	private SherColorExceptionBuilder sherColorExceptionBuilder;
	
	@Autowired
	CdsClrntSysDao cdsClrntSysDao;
	
	@Autowired
	CdsClrntDao clrntDao;
	
	@Autowired
	CdsClrntIncrDao clrntIncrDao;
	
	@Autowired
	CdsClrntListDao clrntListDao;
	
	@Override
	public List<CdsClrntSys> getAvailableColorantSystems(String salesNbr) {
		List<CdsClrntSys> result = new ArrayList<CdsClrntSys>();
		
		List<CdsClrntSys> effectiveClrntSyses;
		List<CdsProdList> exclusions;
		List<String> excludedClrntSyses = new ArrayList<String>();
		try {
			//First, get all colorant systems that are currently in effect.
			effectiveClrntSyses = cdsClrntSysDao.listForClrntSysId(true);
		
			//now exclude colorant systems that don't work for this product.
			exclusions = cdsProdListDao.getAllExclusions(salesNbr);
			for (CdsProdList item:exclusions) {
				if (item.getProdListId().equalsIgnoreCase("NO ZVOC")) {
					excludedClrntSyses.add("CCE");
				}
				if (item.getProdListId().equalsIgnoreCase("NO BAC")) {
					excludedClrntSyses.add("BAC");
				}
			}
			
			boolean addit;
			for (CdsClrntSys item:effectiveClrntSyses) {
				addit=true;
				for (String item2:excludedClrntSyses) {
					if (item.getClrntSysId().equalsIgnoreCase(item2)) {
						addit=false;
					}
				}
				if (addit) {
					result.add(item);
				}
			}

		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		
		return result;
	}
	
	public CdsClrntSys getColorantSystem(String clrntSysId){
		CdsClrntSys clrntSys = null;
		
		clrntSys = cdsClrntSysDao.read(clrntSysId);
		try {
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		
		
		return clrntSys;
	}
	
	public List<CdsClrnt> getColorantList(String clrntSysId){
		List<CdsClrnt> result = new ArrayList<CdsClrnt>();
		
		try {
			result = clrntDao.listForClrntSys(clrntSysId, true);
			
			Collections.sort(result, CdsClrnt.NameComparator);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
	}
	
	public List<CdsClrnt> getColorantList(String clrntSysId, String salesNbr, boolean vinylSafeRequested){
		List<CdsClrnt> result = new ArrayList<CdsClrnt>();
		
		try {
			List<CdsClrnt> initialClrntList = clrntDao.listForClrntSys(clrntSysId, true);
			
			HashMap<String,CdsClrntList> vsExcludeClrntMap = new HashMap<String,CdsClrntList>();;
			HashMap<String,CdsClrntList> generalExcludeClrntMap = new HashMap<String,CdsClrntList>();;
			
			PosProd posProd = posProdDao.read(salesNbr);
			
			if(posProd!=null && posProd.getProdNbr()!=null){
				CdsProdCharzd prodCharzd = prodCharzdDao.read(posProd.getProdNbr(), clrntSysId);
				if(prodCharzd!=null){
					if(prodCharzd.getVinyl_excludeClrnt()!=null)
					vsExcludeClrntMap = clrntListDao.mapTintSysIdForClrntList(prodCharzd.getVinyl_excludeClrnt(), clrntSysId);
					
					if(prodCharzd.getExcludeClrnt()!=null)
					generalExcludeClrntMap = clrntListDao.mapTintSysIdForClrntList(prodCharzd.getExcludeClrnt(), clrntSysId);
				}
			}
	
			// walk throug colorant list, if not excluded for product or vinyl safe restriction, add to result
			boolean restrict;
			for(CdsClrnt cdsClrnt : initialClrntList){
				restrict = false;
				if(vinylSafeRequested && !vsExcludeClrntMap.isEmpty()){
					if(vsExcludeClrntMap.containsKey(cdsClrnt.getTintSysId())) restrict = true;
				}
				if(!generalExcludeClrntMap.isEmpty()){
					if(generalExcludeClrntMap.containsKey(cdsClrnt.getTintSysId())) restrict = true;
				}
				if(!restrict) result.add(cdsClrnt);
			}
			
			
			Collections.sort(result, CdsClrnt.NameComparator);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return result;
		
	}

	public List<String> getColorantIncrementHeader(String clrntSysId){
		List<String> result = new ArrayList<String>();
		
		try {
			List<CdsClrntIncr> clrntIncrs = clrntIncrDao.listForClrntSys(clrntSysId);
			
			Collections.sort(clrntIncrs, CdsClrntIncr.OzIncrRatioComparator);
			
			for (CdsClrntIncr incr : clrntIncrs){
				result.add(incr.getIncr());
			}
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return result;
		
	}

}
