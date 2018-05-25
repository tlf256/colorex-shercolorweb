package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/shercolorcommon.xml" })
@Transactional
public class CdsMessageDaoTest {

	@Autowired
	private CdsMessageDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveReadCdsMessage() {
		String cdsMessageId = "1000";
		String module = "AFCD";

		CdsMessage result = target.read(cdsMessageId, module);

		assertNotNull(result);
	}

	@Test
	public void testNegativeReadCdsFormulaChgList() {
		String cdsMessageId = "JUNK";
		String module = "JUNK";

		CdsMessage result = target.read(cdsMessageId, module);

		assertNull(result);
	}
}
