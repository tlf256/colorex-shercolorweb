package com.sherwin.shercolor.colormath.functions;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.colormath.domain.AutoBase;
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.common.dao.CdsColorStandDao;
import com.sherwin.shercolor.common.domain.CdsColorStand;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class ColorBaseCalculatorTest {
	
	static Properties props = new Properties();
	
	static{
		props.setProperty("U", "ULTRADEEP");
		props.setProperty("D", "DEEP");
		props.setProperty("X", "EXTRA WHITE");
		props.setProperty("L", "LUMINOUS");
		props.setProperty("N", "NEUTRAL");
		props.setProperty("A", "ACCENT");
		props.setProperty("M", "MIDTONE");
		props.setProperty("W", "WHITE");
	}
	
	private HashMap<String,String[]> autobaseMap = new HashMap<String,String[]>();
	
	private ColorBaseCalculator target;
	
	@Autowired
	private ColorCoordinatesCalculator colorCalc;
	
	@Autowired
	private CdsColorStandDao cdsColorStandDao;
	
	//private AutoBase input = new AutoBase(65.02, 4.04, 10.43);
	private AutoBase input = new AutoBase(84.43,5.05,-1.39);
	private AutoBase input2 = new AutoBase();

	@Before
	public void setUp() throws Exception {
		target = new ColorBaseCalculator();
		System.out.println("");
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
		
	}

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testMatchWithSherwinAlgorithm() {
		ColorBaseCalculator.calcBaseSherwinWilliams(input);
		
		System.out.println("SherwinWilliams Algorithm");
		System.out.println("AutoBase   : " + Arrays.toString(autobaseMap.get("SW_"+input.getBase1() + input.getBase2())));
	}
	
	@Test
	public void testMatchWithSherwinAlgorithmNullInput() {
		ColorBaseCalculator.calcBaseSherwinWilliams(input2);
		
		System.out.println("SherwinWilliams Algorithm NULL INPUT");
		System.out.println("AutoBase   : " + Arrays.toString(autobaseMap.get("SW_"+input.getBase1() + input.getBase2())));
	}
	
	@Test
	public void testMatchWithDuronAlgorithm() {
		ColorBaseCalculator.calcBaseDuron(input);
		
		System.out.println("Duron Algorithm");
		System.out.println("AutoBase   : " + Arrays.toString(autobaseMap.get("DURON_"+input.getBase1() + input.getBase2())));
	}

	@Test
	public void testSwBaseViaDbRead(){
		CdsColorStand colorStand = cdsColorStandDao.readByEffectiveVersion("SHERWIN-WILLIAMS", "6255", new Date(), "2DK");
		double[] curve = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		for(int i=0;i<40;i++){
			curve[i]=colorStand.getCurve()[i].doubleValue();
		}
		ColorCoordinates colorCoords = colorCalc.getColorCoordinates(curve);
		AutoBase autoBase = new AutoBase(colorCoords.getCieL(),colorCoords.getCieA(), colorCoords.getCieB());
		ColorBaseCalculator.calcBaseSherwinWilliams(autoBase);
		System.out.println("SW6255 Autobase is " + Arrays.toString(autobaseMap.get("SW_"+autoBase.getBase1() + autoBase.getBase2())));
		assertTrue(autobaseMap.get("SW_"+autoBase.getBase1() + autoBase.getBase2())[0].equalsIgnoreCase("EXTRA WHITE"));
		
	}

	@Test
	public void testSwBaseViaDbRead2(){
		CdsColorStand colorStand = cdsColorStandDao.readByEffectiveVersion("SHERWIN-WILLIAMS", "6255", new Date(), "2DK");
		ColorCoordinates colorCoords = colorCalc.getColorCoordinates(colorStand.getCurve(),"D65");
		AutoBase autoBase = new AutoBase(colorCoords.getCieL(),colorCoords.getCieA(), colorCoords.getCieB());
		ColorBaseCalculator.calcBaseSherwinWilliams(autoBase);
		System.out.println("SW6255 Autobase is " + Arrays.toString(autobaseMap.get("SW_"+autoBase.getBase1() + autoBase.getBase2())));
		assertTrue(autobaseMap.get("SW_"+autoBase.getBase1() + autoBase.getBase2())[0].equalsIgnoreCase("EXTRA WHITE"));
		
	}
}
