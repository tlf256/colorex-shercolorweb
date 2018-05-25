package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsFbColor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional

public class CdsFbColorDaoTest {

	@Autowired
	CdsFbColorDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadPk() {
		String colorId = "6686";
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String fbBase = "B21W10251";
		CdsFbColor result = target.read(colorComp,colorId,clrntSysId, fbBase);
		if (result!=null) {
			System.out.println("Read by color i.d., prodNbr, colorant system and color company Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String colorId = "6686";
		String clrntSysId = "CCE";
		String colorComp = "SHERWIN-WILLIAMS";
		String fbBase = "B61W10251";
		CdsFbColor result = target.read(colorComp,colorId,clrntSysId,fbBase);
		if (result == null) {
			System.out.println("Read by color i.d., prodNbr, colorant system and color company NOT FOUND - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}
	

}
