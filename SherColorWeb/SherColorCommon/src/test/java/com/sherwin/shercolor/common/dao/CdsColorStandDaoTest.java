package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CdsColorStand;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class CdsColorStandDaoTest {
	@Autowired
	private CdsColorStandDao target;
	
	@Autowired
	private CdsColorMastDao colorMastDao;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadCdsColorStand() {
		String spectroModel = "XTS";
		String spectroMode = "SCI";
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		int seqNbr = 1;
		
		CdsColorStand result = target.read(spectroModel, spectroMode, colorComp, colorId, seqNbr);
		
		if(result!=null){
			System.out.println(result.getSpectroModel() + " " + result.getSpectroMode() + " " + result.getColorComp()+ " " + result.getColorId());
		}

		assertNotNull(result);
	}

	@Test
	public void testFailedReadCdsColorStand() {
		String spectroModel = "JUNK";
		String spectroMode = "JUNK";
		String colorComp ="JUNK";
		String colorId = "JINK";
		int seqNbr = 0;
		
		CdsColorStand result = target.read(spectroModel, spectroMode, colorComp, colorId, seqNbr);
		
		assertTrue(result == null);
	}

	@Test
	public void testReadByEffectiveVersionSwColor() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		Date activityDate = new Date();
		String colorEngVer = "";
		
		CdsColorStand result = target.readByEffectiveVersion(colorComp, colorId, activityDate, colorEngVer);
		
		if(result!=null){
			System.out.println("Read By EffectiveVersion[" + colorEngVer + "] -->" + result.getColorComp()+ " " + result.getColorId() + " " + result.getSeqNbr() + " " + result.getCieLValue() + " " + result.getCieAValue() + " " + result.getCieBValue());
		}

		assertNotNull(result);
	}

	@Test
	public void testReadByEffectiveVersionSwColor2dk() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		Date activityDate = new Date();
		String colorEngVer = "2DK";
		
		CdsColorStand result = target.readByEffectiveVersion(colorComp, colorId, activityDate, colorEngVer);
		
		if(result!=null){
			System.out.println("Read By EffectiveVersion[" + colorEngVer + "] -->" + result.getColorComp()+ " " + result.getColorId() + " " + result.getSeqNbr() + " " + result.getCieLValue() + " " + result.getCieAValue() + " " + result.getCieBValue());
		}

		assertNotNull(result);
	}

	@Test
	public void testReadByEffectiveVersionCompetColor() {
		String colorComp ="SICO";
		String colorId = "6001-11";
		Date activityDate = new Date();
		String colorEngVer = "";
		
		CdsColorStand result = target.readByEffectiveVersion(colorComp, colorId, activityDate, colorEngVer);
		
		if(result!=null){
			System.out.println("Read By EffectiveVersion[" + colorEngVer + "] -->" + result.getColorComp()+ " " + result.getColorId() + " " + result.getSeqNbr() + " " + result.getCieLValue() + " " + result.getCieAValue() + " " + result.getCieBValue());
		}

		assertNotNull(result);
	}

	@Test
	public void testReadByEffectiveVersionCompetColor2dk() {
		String colorComp ="SICO";
		String colorId = "6001-11";
		Date activityDate = new Date();
		String colorEngVer = "2DK";
		
		CdsColorStand result = target.readByEffectiveVersion(colorComp, colorId, activityDate, colorEngVer);
		
		if(result!=null){
			System.out.println("Read By EffectiveVersion[" + colorEngVer + "] -->" + result.getColorComp()+ " " + result.getColorId() + " " + result.getSeqNbr() + " " + result.getCieLValue() + " " + result.getCieAValue() + " " + result.getCieBValue());
		}

		assertNotNull(result);
	}

	@Test
	public void testReadByEffectiveVersionUsingOverload() {
		String colorComp ="SICO";
		String colorId = "6001-11";
		Date activityDate = new Date();
		String colorEngVer = "2DK";
		
		CdsColorMast colorMast = colorMastDao.read(colorComp, colorId);
		
		CdsColorStand result = target.readByEffectiveVersion(colorMast, activityDate, colorEngVer);
		
		if(result!=null){
			System.out.println("Read By EffectiveVersion[" + colorEngVer + "] using Overload -->" + result.getColorComp()+ " " + result.getColorId() + " " + result.getSeqNbr() + " " + result.getCieLValue() + " " + result.getCieAValue() + " " + result.getCieBValue());
		}

		assertNotNull(result);
	}

	@Test
	public void testFailedByEffectiveVersion() {
		String colorComp ="JUNK";
		String colorId = "JUNK";
		Date activityDate = new Date();
		String colorEngVer = "2DK";
		
		CdsColorStand result = target.readByEffectiveVersion(colorComp, colorId, activityDate, colorEngVer);
		
		assertTrue(result == null);
	}

	@Test
	public void testListByColorCompId() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		Date activityDate = new Date();
		
		List<CdsColorStand> result = target.listForColorCompId(colorComp, colorId, activityDate);

		if (result!=null) {
			System.out.println("found " + result.size() + " records for " + colorComp + " " + colorId + " " + activityDate + ". They are ...");
			for (CdsColorStand item:result){
				System.out.println("--->" + item.getSpectroModel() + " " + item.getSpectroMode() + " " + item.getColorComp()+ " " + item.getColorId() + " " + item.getSeqNbr() + " " + item.getEffDate() + " " + item.getExpDate());
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testListByColorCompIdUsingOverload() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		Date activityDate = new Date();
		
		CdsColorMast cdsColorMast = colorMastDao.read(colorComp, colorId);
		
		List<CdsColorStand> result = target.listForColorCompId(cdsColorMast, activityDate);

		if (result!=null) {
			System.out.println("found " + result.size() + " records for " + colorComp + " " + colorId + " " + activityDate + ". They are ...");
			for (CdsColorStand item:result){
				System.out.println("--->" + item.getSpectroModel() + " " + item.getSpectroMode() + " " + item.getColorComp()+ " " + item.getColorId() + " " + item.getSeqNbr() + " " + item.getEffDate() + " " + item.getExpDate());
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testFailedListByColorCompIdTooFarInFuture() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateAsString = "2050-01-01";

		Date activityDate = new Date();
		try {
			activityDate = sdf.parse(dateAsString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<CdsColorStand> result = target.listForColorCompId(colorComp, colorId, activityDate);

		if (result!=null) {
			System.out.println("found " + result.size() + " records for " + colorComp + " " + colorId + " " + activityDate + ". OK too far in future.");
			for (CdsColorStand item:result){
				System.out.println("--->" + item.getSpectroModel() + " " + item.getSpectroMode() + " " + item.getColorComp()+ " " + item.getColorId() + " " + item.getSeqNbr() + " " + item.getEffDate() + " " + item.getExpDate());
			}
		}
		
		assertTrue(result.size() == 0 );
	}

	@Test
	public void testFailedListByColorCompIdTooFarBackInTime() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateAsString = "2000-01-01";

		Date activityDate = new Date();
		try {
			activityDate = sdf.parse(dateAsString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<CdsColorStand> result = target.listForColorCompId(colorComp, colorId, activityDate);

		if (result!=null) {
			System.out.println("found " + result.size() + " records for " + colorComp + " " + colorId + " " + activityDate + ". OK too far back in time.");
			for (CdsColorStand item:result){
				System.out.println("--->" + item.getSpectroModel() + " " + item.getSpectroMode() + " " + item.getColorComp()+ " " + item.getColorId() + " " + item.getSeqNbr() + " " + item.getEffDate() + " " + item.getExpDate());
			}
		}
		
		assertTrue(result.size() == 0 );
	}

	@Test
	public void testListForLABRangesSwColors() { //SW6522
		String colorComp ="SHERWIN-WILLIAMS";
		double minLValue = 55.38; // 57.38 - 2
		double maxLValue = 59.38; // 57.38 + 2
		double minAValue = -6.21; // -4.21 - 2
		double maxAValue = -2.21; // -4.21 + 2
		double minBValue = -19.01; // -17.01 - 2
		double maxBValue = -15.01; // -17.01 + 2
		
//		ColorCoordinates stdLAB = new ColorCoordinates();
//		stdLAB.setL_Val(57.38);
//		stdLAB.setA_Val(-4.21);
//		stdLAB.setB_Val(-17.01);
//		
//		ColorCoordinates trialLAB = new ColorCoordinates();
//		
//		ColorDifferenceCalculator calcDiff = new ColorDifferenceCalculator();
		double deltaED65 = 0D;
		
		List<CdsColorStand> result = target.listForLABRanges(colorComp, minLValue, maxLValue, minAValue, maxAValue, minBValue, maxBValue);

		if (result!=null) {
			System.out.println("found " + result.size() + " records for " + colorComp + " L[" + minLValue + "-" + maxLValue + "]" + " A[" + minAValue + "-" + maxAValue + "]" + " B[" + minBValue + "-" + maxBValue + "]" + ". They are ...");
			for (CdsColorStand item:result){
//				trialLAB.setL_Val(item.getCieLValue());
//				trialLAB.setA_Val(item.getCieAValue());
//				trialLAB.setB_Val(item.getCieBValue());
//				deltaED65 = calcDiff.calculateDeltaE(stdLAB, trialLAB);
				
				System.out.println("--->" + item.getColorComp()+ " " + item.getColorId() + " " + item.getEffDate() + " " + item.getExpDate() + " " + item.getCieLValue() + " " + item.getCieAValue() + " " + item.getCieBValue() + " DE = " + deltaED65);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testListForLABRangesAllColors() { //SW6522
		String colorComp = null;
		double minLValue = 55.38; // 57.38 - 2
		double maxLValue = 59.38; // 57.38 + 2
		double minAValue = -6.21; // -4.21 - 2
		double maxAValue = -2.21; // -4.21 + 2
		double minBValue = -19.01; // -17.01 - 2
		double maxBValue = -15.01; // -17.01 + 2
		
		List<CdsColorStand> result = target.listForLABRanges(colorComp, minLValue, maxLValue, minAValue, maxAValue, minBValue, maxBValue);

		if (result!=null) {
			System.out.println("found " + result.size() + " records for All Colors L[" + minLValue + "-" + maxLValue + "]" + " A[" + minAValue + "-" + maxAValue + "]" + " B[" + minBValue + "-" + maxBValue + "]" + ". They are ...");
			for (CdsColorStand item:result){
				System.out.println("--->" + item.getColorComp()+ " " + item.getColorId() + " " + item.getEffDate() + " " + item.getExpDate() + " " + item.getCieLValue() + " " + item.getCieAValue() + " " + item.getCieBValue());
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testFailedListForLABRanges() {
		String colorComp ="SHERWIN-WILLIAMS";
		double minLValue = 0.10;
		double maxLValue = 0.10;
		double minAValue = 0.10;
		double maxAValue = 0.10;
		double minBValue = 0.10;
		double maxBValue = 0.10;
		
		List<CdsColorStand> result = target.listForLABRanges(colorComp, minLValue, maxLValue, minAValue, maxAValue, minBValue, maxBValue);

		assertTrue(result.size() == 0 );
	}

	@Test
	public void testFailed2ByEffectiveVersion() {
		String colorComp ="MAB";
		String colorId = "200-1361";
		Date activityDate = new Date();
		String colorEngVer = "2DK";
		
		CdsColorStand result = target.readByEffectiveVersion(colorComp, colorId, activityDate, colorEngVer);
		
		assertTrue(result == null);
	}


}
