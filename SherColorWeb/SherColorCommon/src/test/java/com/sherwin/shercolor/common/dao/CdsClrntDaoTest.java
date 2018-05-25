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

import com.sherwin.shercolor.common.domain.CdsClrnt;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/shercolorcommon.xml" })
@Transactional
public class CdsClrntDaoTest {
	@Autowired
	private CdsClrntDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveRead() {
		String clrntSysId = "808";
		String tintSysId = "B";

		CdsClrnt result = target.read(clrntSysId, tintSysId);

		assertNotNull(result);
	}

	@Test
	public void testNegativeRead() {
		String clrntSysId = "JUNK";
		String tintSysId = "JUNK";

		CdsClrnt result = target.read(clrntSysId, tintSysId);

		assertNull(result);
	}

	@Test
	public void testPositiveReadByFbSysId() {
		String clrntSysId = "808";
		String fbSysId = "B";

		CdsClrnt result = target.readByFbSysId(clrntSysId, fbSysId);

		assertNotNull(result);
	}

	@Test
	public void testNegativeReadByFbSysId() {
		String clrntSysId = "JUNK";
		String fbSysId = "JUNK";

		CdsClrnt result = target.readByFbSysId(clrntSysId, fbSysId);

		assertNull(result);
	}

	@Test
	public void testPositiveListForClrntSysActiveOnly() {
		String clrntSysId = "808";
		boolean activeOnly = true;

		List<CdsClrnt> result = target.listForClrntSys(clrntSysId, activeOnly);

		assertTrue(result.size() > 0);
	}

	@Test
	public void testPositiveListForClrntSysAll() {
		String clrntSysId = "808";
		boolean activeOnly = false;

		List<CdsClrnt> result = target.listForClrntSys(clrntSysId, activeOnly);

		assertTrue(result.size() > 0);
	}

	@Test
	public void testNegativeListForClrntSysActiveOnly() {
		String clrntSysId = "JUNK";
		boolean activeOnly = true;

		List<CdsClrnt> result = target.listForClrntSys(clrntSysId, activeOnly);

		assertTrue(result.size() == 0);
	}

	@Test
	public void testNegativeListForClrntSysAll() {
		String clrntSysId = "JUNK";
		boolean activeOnly = false;

		List<CdsClrnt> result = target.listForClrntSys(clrntSysId, activeOnly);

		assertTrue(result.size() == 0);
	}
}
