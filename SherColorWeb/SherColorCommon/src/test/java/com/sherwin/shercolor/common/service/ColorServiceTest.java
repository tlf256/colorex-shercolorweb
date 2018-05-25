package com.sherwin.shercolor.common.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.OeServiceColorDataSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class ColorServiceTest {

	@Autowired
	private ColorService target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	

	
	@Test
	public void testGetColorCoordinatesDouble() {  //SW6106 - Kilim Beige
		double[] curve = {8.5454D,9.8891D,13.0628D,20.4314D,31.6782D,39.3078D,41.9917D,43.7609D,45.9493D,47.5962D,48.1183D,48.3551D,48.7558D,49.5168D,50.8593D,52.5787D,54.5094D,56.5196D,58.3763D,59.9248D,61.3052D,62.8414D,64.1409D,64.8976D,65.1403D,65.0295D,64.8936D,64.6806D,64.5444D,64.4543D,64.3829D,64.3577D,64.229D,64.0975D,63.898D,63.7873D,63.8011D,63.9099D,63.9851D,63.8023D};
		ColorCoordinates colorCoords = target.getColorCoordinates(curve);
		System.out.println("SW6016 XYZ using double (moussa) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	
	@Test
	public void testGetColorCoordinatesBigDecimal() {  //SW6106 - Kilim Beige
		BigDecimal[] curve = {new BigDecimal(8.5454),new BigDecimal(9.8891),new BigDecimal(13.0628),new BigDecimal(20.4314),new BigDecimal(31.6782),new BigDecimal(39.3078),new BigDecimal(41.9917),new BigDecimal(43.7609),new BigDecimal(45.9493),new BigDecimal(47.5962),new BigDecimal(48.1183),new BigDecimal(48.3551),new BigDecimal(48.7558),new BigDecimal(49.5168),new BigDecimal(50.8593),new BigDecimal(52.5787),new BigDecimal(54.5094),new BigDecimal(56.5196),new BigDecimal(58.3763),new BigDecimal(59.9248),new BigDecimal(61.3052),new BigDecimal(62.8414),new BigDecimal(64.1409),new BigDecimal(64.8976),new BigDecimal(65.1403),new BigDecimal(65.0295),new BigDecimal(64.8936),new BigDecimal(64.6806),new BigDecimal(64.5444),new BigDecimal(64.4543),new BigDecimal(64.3829),new BigDecimal(64.3577),new BigDecimal(64.229),new BigDecimal(64.0975),new BigDecimal(63.898),new BigDecimal(63.7873),new BigDecimal(63.8011),new BigDecimal(63.9099),new BigDecimal(63.9851),new BigDecimal(63.8023)};
		ColorCoordinates colorCoords = target.getColorCoordinates(curve, "D65");
		System.out.println("SW6016 XYZ using BigDec (brianp) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	
	@Test
	public void testGetColorCoordinatesSW() {  //SW6106 - Kilim Beige
		ColorCoordinates colorCoords = target.getColorCoordinates("SHERWIN-WILLIAMS","6106", "D65");
		System.out.println("SW6016 XYZ using Color Company and ID is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	
	@Test
	public void testGetColorCoordinatesCompetitive() {  //SW6106 - Kilim Beige
		ColorCoordinates colorCoords = target.getColorCoordinates("BEHR","UL140-7", "D65");
		System.out.println("BEHR  XYZ using Color Company and ID is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	
	@Test
	public void testGetColorCoordinatesInvalid() {  //SW6106 - Kilim Beige
		ColorCoordinates colorCoords = target.getColorCoordinates("BOBO","6806-8", "D65");
		//System.out.println("BOBO 6806-8 XYZ using Color Company and ID is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNull(colorCoords);
	}
	
	@Test
	public void testGetColorCoordinatesMastNoStand() {  //SW6106 - Kilim Beige
		ColorCoordinates colorCoords = target.getColorCoordinates("MAB","200-1361", "D65");
		//System.out.println("BOBO 6806-8 XYZ using Color Company and ID is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNull(colorCoords);
	}
	
	
	@Test
	public void testFillDsColorFromOracle(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6385";
		OeServiceColorDataSet result = target.getDsColorFromOracle(colorComp, colorId);
		System.out.println("Test Fill Oe Service Color Data Set from Oracle using " + colorComp + " " + colorId);
		// convert to json for display
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String jsonProd = gson.toJson(result);

		System.out.println(jsonProd);

		assertNotNull(result);
	}

	@Test
	public void testNegativeFillDsProdFromOracleUsingSalesNbr(){
		String colorComp = "XXXXXXXXXXXX";
		String colorId = "XXXXXXXXX";
		OeServiceColorDataSet result = target.getDsColorFromOracle(colorComp, colorId);
		System.out.println("Test Failed Fill Oe Service Color Data Set from Oracle using " + colorComp + " " + colorId);

		assertNull(result);
	}
	
	@Test
	public void testIsCompanyOwnedColorForSwTrue(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6385";
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, null);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertTrue(result);
	}

	@Test
	public void testIsCompanyOwnedColorForSwFalse(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "8101"; // martha
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, null);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertFalse(result);
	}

	@Test
	public void testIsCompanyOwnedColorForMtzTrue(){
		String colorComp = "MAUTZ";
		String colorId = "P-2"; //PRIMER
		CustWebParms webParms = new CustWebParms();
		webParms.setColorComp("MAUTZ-SW");
		webParms.setAltColorComp1("MAUTZ");
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, webParms);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertTrue(result);
	}

	@Test
	public void testIsCompanyOwnedColorForMtzFalse(){
		String colorComp = "MAUTZ";
		String colorId = "1011P"; //Color Source
		CustWebParms webParms = new CustWebParms();
		webParms.setColorComp("MAUTZ-SW");
		webParms.setAltColorComp1("MAUTZ");
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, webParms);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertFalse(result);
	}

	@Test
	public void testIsCompanyOwnedColorForFlxTrue(){
		String colorComp = "FLEXBON";
		String colorId = "P-2"; //PRIMER
		CustWebParms webParms = new CustWebParms();
		webParms.setColorComp("FLEXBON-SW");
		webParms.setAltColorComp1("FLEXBON");
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, webParms);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertTrue(result);
	}

	@Test
	public void testIsCompanyOwnedColorForFlxFalse(){
		String colorComp = "FLEXBON";
		String colorId = "1011P"; //Color Source
		CustWebParms webParms = new CustWebParms();
		webParms.setColorComp("FLEXBON-SW");
		webParms.setAltColorComp1("FLEXBON");
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, webParms);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertFalse(result);
	}

	
	@Test
	public void testIsCompanyOwnedColorForDuronTrue(){
		String colorComp = "DURON";
		String colorId = "10-BL"; //DURON VARA-FLEC
		CustWebParms webParms = new CustWebParms();
		webParms.setColorComp("DURON-SW");
		webParms.setAltColorComp1("DURON");
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, webParms);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertTrue(result);
	}

	@Test
	public void testIsCompanyOwnedColorForDuronFalse(){
		String colorComp = "DURON";
		String colorId = "200-1009"; //Timber Stain
		CustWebParms webParms = new CustWebParms();
		webParms.setColorComp("DURON-SW");
		webParms.setAltColorComp1("DURON");
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, webParms);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertFalse(result);
	}

	@Test
	public void testIsCompanyOwnedColorForMabTrue(){
		String colorComp = "MAB";
		String colorId = "202-1568"; //Cabots
		CustWebParms webParms = new CustWebParms();
		webParms.setColorComp("MAB-SW");
		webParms.setAltColorComp1("MAB");
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, webParms);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertTrue(result);
	}

	@Test
	public void testIsCompanyOwnedColorForMabFalse(){
		String colorComp = "MAB";
		String colorId = "73542"; //Modac
		CustWebParms webParms = new CustWebParms();
		webParms.setColorComp("MAB-SW");
		webParms.setAltColorComp1("MAB");
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, webParms);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertFalse(result);
	}

	@Test
	public void testIsCompanyOwnedColorForColumbiaTrue(){
		String colorComp = "COLUMBIA";
		String colorId = "FMP-306"; //Field Paint
		CustWebParms webParms = new CustWebParms();
		webParms.setColorComp("COLUMBIA-SW");
		webParms.setAltColorComp1("COLUMBIA");
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, webParms);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertTrue(result);
	}

	@Test
	public void testIsCompanyOwnedColorForColumbiaFalse(){
		String colorComp = "COLUMBIA";
		String colorId = "7003"; //Millennium
		CustWebParms webParms = new CustWebParms();
		webParms.setColorComp("COLUMBIA-SW");
		webParms.setAltColorComp1("COLUMBIA");
		
		boolean result = target.isCompanyOwnedColor(colorComp, colorId, webParms);
		System.out.println("Test Is Company Owned Color returned " + result + " for " + colorComp + " " + colorId);
		
		assertFalse(result);
	}


}