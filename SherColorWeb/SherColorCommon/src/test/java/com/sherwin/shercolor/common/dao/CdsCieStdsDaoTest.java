package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsCieStds;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class CdsCieStdsDaoTest {
	
	@Autowired
	private CdsCieStdsDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveReadCdsColorBase() {
		String illumCode = "A";
		CdsCieStds result = target.read(illumCode);
		assertNotNull(result);
	}

	@Test
	public void testFailedReadCdsColorBase() {
		String illumCode = "JUNK";

		CdsCieStds result = target.read(illumCode);

		assertTrue(result == null);
	}
}
