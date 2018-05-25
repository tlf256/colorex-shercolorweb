package com.sherwin.shercolor.common.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.colormath.domain.AutoBase;
import com.sherwin.shercolor.colormath.functions.ColorBaseCalculator;
import com.sherwin.shercolor.colormath.functions.ColorCoordinatesCalculator;
import com.sherwin.shercolor.common.dao.CdsColorBaseDao;
import com.sherwin.shercolor.common.dao.CdsColorStandDao;
import com.sherwin.shercolor.common.dao.CdsFormulaChgListDao;
import com.sherwin.shercolor.common.dao.CdsProdDao;
import com.sherwin.shercolor.common.dao.PosProdDao;
import com.sherwin.shercolor.common.domain.CdsColorStand;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.exception.SherColorExceptionBuilder;

@Service
@Transactional
public class ColorBaseServiceImpl implements ColorBaseService{
	
	static Logger logger = LogManager.getLogger(ColorBaseServiceImpl.class);
	
	@Autowired
	CdsColorBaseDao cdsColorBaseDao;

	@Autowired
	CdsColorStandDao cdsColorStandDao;
	
	@Autowired
	CdsFormulaChgListDao cdsFormulaChgListDao;
	
	@Autowired
	PosProdDao posProdDao;
	
	@Autowired
	CdsProdDao cdsProdDao;
	
	@Autowired
	private SherColorExceptionBuilder sherColorExceptionBuilder;
	
	@Autowired
	private ColorCoordinatesCalculator colorCoordsCalc;
	
	

	public List<String> InteriorColorBaseAssignments(String colorComp, String colorId, String prodComp){
		List<String> result = null;
		
		try {
			result = cdsColorBaseDao.listBasesForColorCompIdIntExtProdComp(colorComp, colorId, "I", prodComp);
			List<String> addBases = getAfcdBaseAssignments(colorComp, colorId, "I");
			if(addBases.size()>0){
				for(String base : addBases){
					if(!result.contains(base)) result.add(base);
				}
			}
			if(result.size()==0) result=null;
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return result;
	}

	public List<String> ExteriorColorBaseAssignments(String colorComp, String colorId, String prodComp){
		List<String> result = null;
		
		try {
			result = cdsColorBaseDao.listBasesForColorCompIdIntExtProdComp(colorComp, colorId, "E", prodComp);
			List<String> addBases = getAfcdBaseAssignments(colorComp, colorId, "I");
			if(addBases.size()>0){
				for(String base : addBases){
					if(!result.contains(base)) result.add(base);
				}
			}
			if(result.size()==0) result=null;
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return result;
	}
	
	public List<String> GetAutoBase(String colorComp, String colorId, String prodComp){
		List<String> result = null;
		
		try {
			CdsColorStand colorStand = cdsColorStandDao.readByEffectiveVersion(colorComp, colorId, new Date(), "2Dk");
			
			if(colorStand!=null){
				result = GetAutoBase(colorStand.getCurve(),prodComp);
			} else {
				// no color stand, return empty string
				result = new ArrayList<String>();
				
			}
			
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return result;
		
	}

	public List<String> GetAutoBase(BigDecimal[] curve, String prodComp){
		List<String> result = null;
		
		try {
			ColorCoordinates colorCoords = colorCoordsCalc.getColorCoordinates(curve, "D65");
			
			AutoBase autobase = new AutoBase(colorCoords.getCieL(),colorCoords.getCieA(),colorCoords.getCieB());
						
			if(prodComp.equalsIgnoreCase("SW") || prodComp.equalsIgnoreCase("FLEXBON")){
				ColorBaseCalculator.calcBaseSherwinWilliams(autobase);
			} else {
				ColorBaseCalculator.calcBaseDuron(autobase);
			}
			
			Map<String, String[]> autobaseMap = buildAutobaseMap();
			
			String[] mapResult = autobaseMap.get(prodComp.toUpperCase() + "_" + autobase.getBase1() + autobase.getBase2());

			if(mapResult.length==0){
				result=null;
			} else {
				result = Arrays.asList(mapResult);
			}
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return result;
		
	}
	
	private List<String> getAfcdBaseAssignments(String colorComp, String colorId, String getInteriorOrExterior){
		List<String> baseList = new ArrayList<String>();
		
		// get afcd alternate base suggestions from formulaChgList table
		List<String> productNbrList = cdsFormulaChgListDao.listProductForAlternateBaseRequired(colorComp, colorId);
		
		// lookup rex and determine if interior/exterior product
		for(String prodNbr : productNbrList){
			List<PosProd> posProdList = posProdDao.listForProdNbr(prodNbr, true);
			for (PosProd posProd : posProdList){
				CdsProd cdsProd = cdsProdDao.read(posProd.getSalesNbr());
				if(cdsProd!=null && cdsProd.getBase()!=null && cdsProd.getIntExt()!=null){
					if(getInteriorOrExterior.equalsIgnoreCase("I")){
						if(cdsProd.getIntExt().equalsIgnoreCase("INTERIOR") || cdsProd.getIntExt().equalsIgnoreCase("INT/EXT")){
							if(!baseList.contains(cdsProd.getBase())) baseList.add(cdsProd.getBase());
						}
					} else {
						// looking for exterior
						if(cdsProd.getIntExt().equalsIgnoreCase("EXTERIOR") || cdsProd.getIntExt().equalsIgnoreCase("INT/EXT")){
							if(!baseList.contains(cdsProd.getBase())) baseList.add(cdsProd.getBase());
						}
					}
				}
			}
		}
		
		// return list of bases to add to recommended base list
		
		return baseList;
	}

	private Map<String,String[]> buildAutobaseMap(){
		HashMap<String,String[]> autobaseMap = new HashMap<String,String[]>();
		
		// sw and flexbon can get L (luminous), X (extra white), D (deep), U (ultradeep)
		// SW can get L,X,D,U map to HRB & LUMIUNOUS, EXTRA WHITE, DEEP, ULTRADEEP
		autobaseMap.put("SW_LX", new String[]{"HRB","LUMINOUS WHITE","EXTRA WHITE"});
		autobaseMap.put("SW_XD", new String[]{"EXTRA WHITE","DEEP"});
		autobaseMap.put("SW_DU", new String[]{"DEEP","ULTRADEEP"});
		autobaseMap.put("SW_UD", new String[]{"ULTRADEEP","DEEP"});
		autobaseMap.put("SW_DX", new String[]{"DEEP","EXTRA WHITE"});
		autobaseMap.put("SW_XL", new String[]{"EXTRA WHITE","HRB","LUMINOUS WHITE"});
		// FLEXBON can get L,X,D,U map to WHITE, DEEP, ULTRADEEP (no Luminous so treat L like X
		autobaseMap.put("FLEXBON_LX", new String[]{"WHITE","DEEP"});
		autobaseMap.put("FLEXBON_XD", new String[]{"WHITE","DEEP"});
		autobaseMap.put("FLEXBON_DU", new String[]{"DEEP","ULTRADEEP"});
		autobaseMap.put("FLEXBON_UD", new String[]{"ULTRADEEP","DEEP"});
		autobaseMap.put("FLEXBON_DX", new String[]{"DEEP","WHITE"});
		autobaseMap.put("FLEXBON_XL", new String[]{"WHITE","DEEP"});
		// all others can get W (white), M (midtone), D (deep), A (accent), N (neutral) 
		// DURON can get W,M,D,A,N map to WHITE, MIDTONE, DEEP, ACCENT, NEUTRAL
		autobaseMap.put("DURON_WM", new String[]{"WHITE","MIDTONE"});
		autobaseMap.put("DURON_MD", new String[]{"MIDTONE","DEEP"});
		autobaseMap.put("DURON_DA", new String[]{"DEEP","ACCENT"});
		autobaseMap.put("DURON_AN", new String[]{"ACCENT","NEUTRAL"});
		autobaseMap.put("DURON_NA", new String[]{"NEUTRAL","ACCENT"});
		autobaseMap.put("DURON_AD", new String[]{"ACCENT","DEEP"});
		autobaseMap.put("DURON_DM", new String[]{"DEEP","MIDTONE"});
		autobaseMap.put("DURON_MW", new String[]{"MIDTONE","WHITE"});
		// MAB can get W,M,D,A,N map to WHITE, MEDIUM, DEEP, ACCENT, NEUTRAL
		autobaseMap.put("MAB_WM", new String[]{"WHITE","MEDIUM"});
		autobaseMap.put("MAB_MD", new String[]{"MEDIUM","DEEP"});
		autobaseMap.put("MAB_DA", new String[]{"DEEP","ACCENT"});
		autobaseMap.put("MAB_AN", new String[]{"ACCENT","NEUTRAL"});
		autobaseMap.put("MAB_NA", new String[]{"NEUTRAL","ACCENT"});
		autobaseMap.put("MAB_AD", new String[]{"ACCENT","DEEP"});
		autobaseMap.put("MAB_DM", new String[]{"DEEP","MEDIUM"});
		autobaseMap.put("MAB_MW", new String[]{"MEDIUM","WHITE"});
		// MAUTZ can get W,M,D,A,N map to WHITE, MEDIUM, DEEP, NEUTRAL (no accent so treat A like N)
		autobaseMap.put("MAUTZ_WM", new String[]{"WHITE","MEDIUM"});
		autobaseMap.put("MAUTZ_MD", new String[]{"MEDIUM","DEEP"});
		autobaseMap.put("MAUTZ_DA", new String[]{"DEEP","NEUTRAL"});
		autobaseMap.put("MAUTZ_AN", new String[]{"NEUTRAL","DEEP"});
		autobaseMap.put("MAUTZ_NA", new String[]{"NEUTRAL","DEEP"});
		autobaseMap.put("MAUTZ_AD", new String[]{"NEUTRAL","DEEP"});
		autobaseMap.put("MAUTZ_DM", new String[]{"DEEP","MEDIUM"});
		autobaseMap.put("MAUTZ_MW", new String[]{"MEDIUM","WHITE"});
		// CLRWHEEL can get W,M,D,A,N map to WHITE, TINT WHITE, DEEP, ACCENT (no neutral so treat N like A)
		autobaseMap.put("CLRWHEEL_WM", new String[]{"WHITE","TINT WHITE"});
		autobaseMap.put("CLRWHEEL_MD", new String[]{"TINT WHITE","DEEP"});
		autobaseMap.put("CLRWHEEL_DA", new String[]{"DEEP","ACCENT"});
		autobaseMap.put("CLRWHEEL_AN", new String[]{"ACCENT","DEEP"});
		autobaseMap.put("CLRWHEEL_NA", new String[]{"ACCENT","DEEP"});
		autobaseMap.put("CLRWHEEL_AD", new String[]{"ACCENT","DEEP"});
		autobaseMap.put("CLRWHEEL_DM", new String[]{"DEEP","TINT WHITE"});
		autobaseMap.put("CLRWHEEL_MW", new String[]{"TINT WHITE","WHITE"});
		// PARKER can get W,M,D,A,N map to WHITE, MEDIUM, DEEP, NEUTRAL (no accent so treat A like N)
		autobaseMap.put("PARKER_WM", new String[]{"WHITE","MEDIUM"});
		autobaseMap.put("PARKER_MD", new String[]{"MEDIUM","DEEP"});
		autobaseMap.put("PARKER_DA", new String[]{"DEEP","NEUTRAL"});
		autobaseMap.put("PARKER_AN", new String[]{"NEUTRAL","DEEP"});
		autobaseMap.put("PARKER_NA", new String[]{"NEUTRAL","DEEP"});
		autobaseMap.put("PARKER_AD", new String[]{"NEUTRAL","DEEP"});
		autobaseMap.put("PARKER_DM", new String[]{"DEEP","MEDIUM"});
		autobaseMap.put("PARKER_MW", new String[]{"MEDIUM","WHITE"});
		// KWAL can get W,M,D,A,N map to WHITE, MEDIUM, DEEP, NEUTRAL (no accent so treat A like N)
		autobaseMap.put("KWAL_WM", new String[]{"WHITE","MEDIUM"});
		autobaseMap.put("KWAL_MD", new String[]{"MEDIUM","DEEP"});
		autobaseMap.put("KWAL_DA", new String[]{"DEEP","NEUTRAL"});
		autobaseMap.put("KWAL_AN", new String[]{"NEUTRAL","DEEP"});
		autobaseMap.put("KWAL_NA", new String[]{"NEUTRAL","DEEP"});
		autobaseMap.put("KWAL_AD", new String[]{"NEUTRAL","DEEP"});
		autobaseMap.put("KWAL_DM", new String[]{"DEEP","MEDIUM"});
		autobaseMap.put("KWAL_MW", new String[]{"MEDIUM","WHITE"});
		// FRAZEE can get W,M,D,A,N map to WHITE, MEDIUM, DEEP, ACCENT, NEUTRAL
		autobaseMap.put("FRAZEE_WM", new String[]{"WHITE","MEDIUM"});
		autobaseMap.put("FRAZEE_MD", new String[]{"MEDIUM","DEEP"});
		autobaseMap.put("FRAZEE_DA", new String[]{"DEEP","ACCENT"});
		autobaseMap.put("FRAZEE_AN", new String[]{"ACCENT","NEUTRAL"});
		autobaseMap.put("FRAZEE_NA", new String[]{"NEUTRAL","ACCENT"});
		autobaseMap.put("FRAZEE_AD", new String[]{"ACCENT","DEEP"});
		autobaseMap.put("FRAZEE_DM", new String[]{"DEEP","MEDIUM"});
		autobaseMap.put("FRAZEE_MW", new String[]{"MEDIUM","WHITE"});
		// GENERAL can get W,M,D,A,N map to WHITE, DEEP, ACCENT, NEUTRAL (no medium so treat M like W)
		autobaseMap.put("GENERAL_WM", new String[]{"WHITE","DEEP"});
		autobaseMap.put("GENERAL_MD", new String[]{"WHITE","DEEP"});
		autobaseMap.put("GENERAL_DA", new String[]{"DEEP","ACCENT"});
		autobaseMap.put("GENERAL_AN", new String[]{"ACCENT","NEUTRAL"});
		autobaseMap.put("GENERAL_NA", new String[]{"NEUTRAL","ACCENT"});
		autobaseMap.put("GENERAL_AD", new String[]{"ACCENT","DEEP"});
		autobaseMap.put("GENERAL_DM", new String[]{"DEEP","WHITE"});
		autobaseMap.put("GENERAL_MW", new String[]{"WHITE","DEEP"});

		return autobaseMap;
	}

}
