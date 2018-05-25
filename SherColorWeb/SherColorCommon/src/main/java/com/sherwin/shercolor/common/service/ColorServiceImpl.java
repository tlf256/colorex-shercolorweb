package com.sherwin.shercolor.common.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.colormath.functions.ColorCoordinatesCalculator;
import com.sherwin.shercolor.common.dao.CdsColorBaseDao;
import com.sherwin.shercolor.common.dao.CdsColorMastDao;
import com.sherwin.shercolor.common.dao.CdsColorStandDao;
import com.sherwin.shercolor.common.domain.CdsColorBase;
import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CdsColorStand;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.OeServiceColorBase;
import com.sherwin.shercolor.common.domain.OeServiceColorDataSet;
import com.sherwin.shercolor.common.domain.OeServiceColorMast;
import com.sherwin.shercolor.common.domain.OeServiceColorStand;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.exception.SherColorExceptionBuilder;

@Service
@Transactional
public class ColorServiceImpl implements ColorService {
	
	static Logger logger = LogManager.getLogger(ColorServiceImpl.class);

	@Autowired
	CdsColorStandDao cdsColorStandDao;
	
	@Autowired
	CdsColorMastDao cdsColorMastDao;
	
	@Autowired
	CdsColorBaseDao cdsColorBaseDao;
	
	@Autowired
	ColorCoordinatesCalculator cdsClrCoordsCalculator;
	
	@Autowired
	private SherColorExceptionBuilder sherColorExceptionBuilder;
	

	@Override
	public ColorCoordinates getColorCoordinates(double[] curve) {
		ColorCoordinates returnCoords = null;
		try {
			returnCoords = cdsClrCoordsCalculator.getColorCoordinates(curve);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return returnCoords;
	}

	@Override
	public ColorCoordinates getColorCoordinates(BigDecimal[] curve, String illumCode) {
		// TODO Auto-generated method stub
		ColorCoordinates returnCoords = null;
		try {
			returnCoords = cdsClrCoordsCalculator.getColorCoordinates(curve, illumCode);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return returnCoords;
	}

	@Override
	public ColorCoordinates getColorCoordinates(String colorComp, String colorID, String illumCode) {
		ColorCoordinates returnCoords = null;
		try {
			CdsColorStand theColorStand = cdsColorStandDao.readByEffectiveVersion(colorComp, colorID, new Date(), "2DK");
			if (theColorStand==null) {
				return null;
			}
			returnCoords = cdsClrCoordsCalculator.getColorCoordinates(theColorStand.getCurve(), illumCode);
		}
		catch (RuntimeException e){
			logger.error(e.getMessage());
			SherColorException se = sherColorExceptionBuilder.buildfrom(e);
			throw se;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return returnCoords;
	}

	public OeServiceColorDataSet getDsColorFromOracle(String colorComp, String colorId){
		OeServiceColorDataSet dsColor = new OeServiceColorDataSet();
		
		CdsColorMast cdsColorMast = cdsColorMastDao.read(colorComp, colorId);
		if(cdsColorMast!=null){
			List<OeServiceColorMast> oeColorMastList = new ArrayList<OeServiceColorMast>();
			oeColorMastList.add(new OeServiceColorMast(cdsColorMast));
			dsColor.setColorMastList(oeColorMastList);
			
			List<CdsColorStand> cdsColorStandList = cdsColorStandDao.listForColorCompId(colorComp, colorId, new Date());
			List<OeServiceColorStand> oeColorStandList = new ArrayList<OeServiceColorStand>();
			for(CdsColorStand cdsColorStand : cdsColorStandList){
				oeColorStandList.add(new OeServiceColorStand(cdsColorStand));
			}
			dsColor.setColorStandList(oeColorStandList);
			
			List<CdsColorBase> cdsColorBaseList = cdsColorBaseDao.listForColorCompId(colorComp, colorId);
			List<OeServiceColorBase> oeColorBaseList = new ArrayList<OeServiceColorBase>();
			for(CdsColorBase cdsColorBase : cdsColorBaseList){
				oeColorBaseList.add(new OeServiceColorBase(cdsColorBase));
			}
			dsColor.setColorBaseList(oeColorBaseList);
			
		} else {
			dsColor = null;
		}
		return dsColor;
	}

	
	public boolean isCompanyOwnedColor(String colorComp, String colorId, CustWebParms custWebParms){
		boolean retVal = false;
		
		if(colorComp.equalsIgnoreCase("SHERWIN-WILLIAMS") || colorComp.equalsIgnoreCase("MAUTZ-SW")){
			// everything but martha is company owned (8000 - 8499)
			if(colorId.matches("8[0-4][0-9][0-9]")){
				//its Martha
				retVal = false;
			} else {
				retVal = true;
			}
		} else {
			// not SW, check if colorComp is in parms record as primary or alternate color company
			if(custWebParms!=null){
				if(colorComp.equalsIgnoreCase(custWebParms.getColorComp()) || 
				   colorComp.equalsIgnoreCase(custWebParms.getAltColorComp1()) ||
				   colorComp.equalsIgnoreCase(custWebParms.getAltColorComp2()) || 
				   colorComp.equalsIgnoreCase(custWebParms.getAltColorComp3()) ||
				   colorComp.equalsIgnoreCase(custWebParms.getAltColorComp4()) ||
				   colorComp.equalsIgnoreCase(custWebParms.getAltColorComp5())){
					
					//colorComp listed as theirs, should be company owned but wait, there are exceptions by palette...
					CdsColorMast colorMast = cdsColorMastDao.read(colorComp, colorId);
					if(colorMast!=null){
						// set to true, exceptions will convert to false
						retVal = true;
						// palette exceptions by company...
						if(colorComp.equalsIgnoreCase("MAUTZ")){
							// MAUTZ anything but this is NOT company owned
							if(!colorMast.getPalette().equalsIgnoreCase("PRIMER")) retVal = false;
						} else if(colorComp.equalsIgnoreCase("FLEXBON")){
							// FLEXBON anything but this is NOT company owned
							if(!colorMast.getPalette().equalsIgnoreCase("PRIMER")) retVal = false;
						} else if(colorComp.equalsIgnoreCase("DURON")){
							// DURON must we one of these or it is not company owned
							if(!colorMast.getPalette().equalsIgnoreCase("CAROLINA LOWCOUNTRY") &&
							   !colorMast.getPalette().equalsIgnoreCase("CURB APPEAL") &&
							   !colorMast.getPalette().equalsIgnoreCase("DURA CLAD") &&
							   !colorMast.getPalette().equalsIgnoreCase("DURON MAXWOOD SD/TRM") &&
							   !colorMast.getPalette().equalsIgnoreCase("DURON READY MIXED CO") &&
							   !colorMast.getPalette().equalsIgnoreCase("DURON STN COLOR CARD") &&
							   !colorMast.getPalette().equalsIgnoreCase("DURON VARA-FLEC") &&
							   !colorMast.getPalette().equalsIgnoreCase("FLOOD MAXWOOD DCK/SD") &&
							   !colorMast.getPalette().equalsIgnoreCase("HISTORIC CHARLESTON") &&
							   !colorMast.getPalette().equalsIgnoreCase("MAXWOOD STAINS") &&
							   !colorMast.getPalette().equalsIgnoreCase("MISC COLOR CARDS") &&
							   !colorMast.getPalette().equalsIgnoreCase("MISC WHITES") &&
							   !colorMast.getPalette().equalsIgnoreCase("MOUNT VERNON ESTATE") &&
							   !colorMast.getPalette().equalsIgnoreCase("OLD BUILDERS SELECT") &&
							   !colorMast.getPalette().equalsIgnoreCase("PORCH & FLOOR") &&
							   !colorMast.getPalette().equalsIgnoreCase("SIDING IN A CAN")) retVal = false;
						} else if(colorComp.equalsIgnoreCase("MAB")){
							// MAB must we one of these or it is not company owned
							if(!colorMast.getPalette().equalsIgnoreCase("COLOR QUEST") &&
							   !colorMast.getPalette().equalsIgnoreCase("COLOR SYMPHONY") &&
							   !colorMast.getPalette().equalsIgnoreCase("INDUTRIAL CARD") &&
							   !colorMast.getPalette().equalsIgnoreCase("MARK II") &&
							   !colorMast.getPalette().equalsIgnoreCase("MARK III") &&
							   !colorMast.getPalette().startsWith("CABOT")) retVal = false;
						} else if(colorComp.equalsIgnoreCase("COLUMBIA")){
							// COLUMBIA considers any of these as not company owned
							if(colorMast.getPalette().equalsIgnoreCase("MILLENNIUM") ||
							   colorMast.getPalette().equalsIgnoreCase("PREVIOUS COLOR SYSTEM") ||
							   colorMast.getPalette().equalsIgnoreCase("COLOR IS") ||
							   colorMast.getPalette().equalsIgnoreCase("HULS")) retVal = false;
							
						}
					} else {
						// color mast is null, not company owned
						retVal = false;
						
					}
					
				} else {
					// colorComp not listed as theirs in customer's profile, not company owned
					retVal = false;
				}
			} else { 
				// parms is null, don't know if company is theirs or not, assume no
				retVal = false;
			}
			
		}
		
		return retVal;
	}
}
