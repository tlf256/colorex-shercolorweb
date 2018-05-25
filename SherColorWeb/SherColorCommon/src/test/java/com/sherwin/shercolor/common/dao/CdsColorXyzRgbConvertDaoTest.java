package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsColorXyzRgbConvert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class CdsColorXyzRgbConvertDaoTest {
	@Autowired
	private CdsColorXyzRgbConvertDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadCdsColorXyzRgbConvert() {
		String illuminant ="D65";
		int observer = 10;
		
		CdsColorXyzRgbConvert result = target.read(illuminant, observer);
		
		if(result!=null){
			System.out.println(result.getIlluminant()+ " " + result.getObserver() + " " + result.getIlluminant_desc() + " " + result.getArxr());
		}

		assertNotNull(result);
	}

	@Test
	public void testFailedReadCdsColorXyzRgbConvert() {
		String illuminant ="JUNK";
		int observer = 99;
		
		CdsColorXyzRgbConvert result = target.read(illuminant, observer);
		
		assertTrue(result == null);
	}


}
