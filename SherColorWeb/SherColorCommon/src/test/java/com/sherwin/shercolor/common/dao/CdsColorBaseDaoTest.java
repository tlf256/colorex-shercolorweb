package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsColorBase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class CdsColorBaseDaoTest {
	@Autowired
	CdsColorBaseDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadCdsColorBase() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		String ieFlag = "I";
		int seqNbr = 1;
		
		CdsColorBase result = target.read(colorComp, colorId, ieFlag, seqNbr);
		
		if(result!=null){
			System.out.println(result.getColorComp()+ " " + result.getColorId() + " " + result.getIeFlag() + " " + result.getSeqNbr() + " " + result.getBase() + " " + result.getProdComp());
		}

		assertNotNull(result);
	}

	@Test
	public void testFailedReadCdsColorBase() {
		String colorComp ="JUNK";
		String colorId = "JUNK";
		String ieFlag = "X";
		int seqNbr = 0;
		
		CdsColorBase result = target.read(colorComp, colorId, ieFlag, seqNbr);
		
		assertTrue(result == null);
	}


	@Test
	public void testSearchByColorCompIdIeFlag() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		String ieFlag = "I";
		
		List<CdsColorBase> result = target.listForColorCompIdIntExt(colorComp, colorId, ieFlag);

		if (result!=null) {
			System.out.println("found " + result.size() + " records for " + colorComp + " " + colorId + " " + ieFlag + " here they are...");
			for (CdsColorBase item:result){
				System.out.println("---> " + item.getColorComp() + " " + item.getColorId() + " " + item.getIeFlag() + " " + item.getSeqNbr() + " " + item.getBase() + " " + item.getProdComp());
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testSearchByColorCompIdIeProdCompFlag() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		String ieFlag = "I";
		String prodComp = "SW";
		
		List<CdsColorBase> result = target.listForColorCompIdIntExtProdComp(colorComp, colorId, ieFlag, prodComp);

		if (result!=null) {
			System.out.println("found " + result.size() + " records for " + colorComp + " " + colorId + " " + ieFlag + " " + prodComp + " here they are...");
			for (CdsColorBase item:result){
				System.out.println("---> " + item.getColorComp() + " " + item.getColorId() + " " + item.getIeFlag() + " " + item.getSeqNbr() + " " + item.getBase() + " " + item.getProdComp());
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testFailedSearchByColorCompIdIeFlag() {
		String colorComp ="JUNK";
		String colorId = "JUNK";
		String ieFlag = "X";
		
		List<CdsColorBase> result = target.listForColorCompIdIntExt(colorComp, colorId, ieFlag);

		assertTrue(result.size() == 0 );
	}

	@Test
	public void testFailedSearchByColorCompIdIeProdCompFlag() {
		String colorComp ="JUNK";
		String colorId = "JUNK";
		String ieFlag = "X";
		String prodComp = "XX";
		
		List<CdsColorBase> result = target.listForColorCompIdIntExtProdComp(colorComp, colorId, ieFlag, prodComp);

		assertTrue(result.size() == 0 );
	}

	@Test
	public void testSearchBaseByColorCompIdIeProdCompFlag() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		String ieFlag = "I";
		String prodComp = "SW";
		
		List<String> result = target.listBasesForColorCompIdIntExtProdComp(colorComp, colorId, ieFlag, prodComp);

		if (result!=null) {
			System.out.println("found " + result.size() + " bases for " + colorComp + " " + colorId + " " + ieFlag + " " + prodComp + " here they are...");
			for (String item:result){
				System.out.println("---> " + item);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testFailedSearchBaseByColorCompIdIeProdCompFlag() {
		String colorComp ="JUNK";
		String colorId = "JUNK";
		String ieFlag = "X";
		String prodComp = "XX";
		
		List<String> result = target.listBasesForColorCompIdIntExtProdComp(colorComp, colorId, ieFlag, prodComp);
		
		assertTrue(result.size() == 0 );
	}

	@Test
	public void testListForceBasesForColorCompId() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6862";
		String ieFlag = "I";
		
		List<CdsColorBase> result = target.listForceBasesForColorCompIdIntExt(colorComp, colorId, ieFlag);

		if (result!=null) {
			System.out.println("found " + result.size() + " force bases for " + colorComp + " " + colorId + " here they are...");
			for (CdsColorBase item:result){
				System.out.println("---> " + item.getColorComp() + " " + item.getColorId() + " " + item.getBase() + " " + item.getProdComp());
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testFailedListForceBasesForColorCompId() {
		String colorComp ="JUNK";
		String colorId = "JUNK";
		String ieFlag = "X";
		
		List<CdsColorBase> result = target.listForceBasesForColorCompIdIntExt(colorComp, colorId, ieFlag);

		assertTrue(result.size() == 0 );
	}


	@Test
	public void testSearchByColorCompId() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		
		List<CdsColorBase> result = target.listForColorCompId(colorComp, colorId);

		if (result!=null) {
			System.out.println("found " + result.size() + " records for " + colorComp + " " + colorId + " here they are...");
			for (CdsColorBase item:result){
				System.out.println("---> " + item.getColorComp() + " " + item.getColorId() + " " + item.getIeFlag() + " " + item.getSeqNbr() + " " + item.getBase() + " " + item.getProdComp());
			}
		}
		
		assertTrue(result.size() > 0 );
	}


}
