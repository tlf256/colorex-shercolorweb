package com.sherwin.shercolor.common.validation;

import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.sherwin.shercolor.common.domain.CdsColorMast;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class ColorValidatorImplTest {
	
	@Autowired
	private ColorValidator target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveCheckcolor() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId ="1648";
		CdsColorMast result = null;
	
		try {
			result = target.checkColor(colorComp, colorId);
		} catch (Exception e) {
			fail();
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeCheckcolor() {
		String colorComp ="INVALID";
		String colorId ="INVALID";
		CdsColorMast result = null;
	
		try {
			result = target.checkColor(colorComp, colorId);
		} catch (Exception e) {
			System.out.println("Error thrown - msg is " + e.getMessage());
		}
		assertNull(result);
	}

}
