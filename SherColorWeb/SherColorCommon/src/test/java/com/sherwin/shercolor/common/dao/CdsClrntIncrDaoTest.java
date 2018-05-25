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

import com.sherwin.shercolor.common.domain.CdsClrntIncr;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/shercolorcommon.xml" })
@Transactional
public class CdsClrntIncrDaoTest {
	@Autowired
	private CdsClrntIncrDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveRead() {
		String clrntSysId = "808";
		String incr = "192";

		CdsClrntIncr result = target.read(clrntSysId, incr);

		assertNotNull(result);
	}

	@Test
	public void testNegativeRead() {
		String clrntSysId = "JUNK";
		String incr = "JUNK";

		CdsClrntIncr result = target.read(clrntSysId, incr);

		assertNull(result);
	}

	@Test
	public void testPositiveListForClrntSys() {
		String clrntSysId = "808";

		List<CdsClrntIncr> result = target.listForClrntSys(clrntSysId);

		assertTrue(result.size() > 0);
	}

	@Test
	public void testNegativeListForClrntSys() {
		String clrntSysId = "JUNK";

		List<CdsClrntIncr> result = target.listForClrntSys(clrntSysId);

		assertTrue(result.size() == 0);
	}

}
