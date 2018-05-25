package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsMiscCodes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/shercolorcommon.xml" })
@Transactional
public class CdsMiscCodesDaoTest {

	@Autowired
	private CdsMiscCodesDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveReadCdsMiscCodes() {
		String miscType = "ILLUM";
		String miscCode = "A";
		CdsMiscCodes result = target.read(miscType, miscCode);

		assertNotNull(result);
	}

	@Test
	public void testNegativeReadCdsMiscCodes() {
		String miscType = "JUNK";
		String miscCode = "JUNK";
		CdsMiscCodes result = target.read(miscType, miscCode);

		assertNull(result);
	}

	@Test
	public void testPositiveListForType() {
		String miscType = "ILLUM";

		List<CdsMiscCodes> result = target.listForType(miscType);

		assertTrue(result.size() > 0);
	}

	@Test
	public void testNegativeListForType() {
		String miscType = "JUNK";

		List<CdsMiscCodes> result = target.listForType(miscType);

		assertTrue(result.size() == 0);
	}

}
