package com.sherwin.shercolor.common.dao;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsRoomList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional

public class CdsRoomListDaoTest {

	@Autowired
	CdsRoomListDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadPk() {
		String listName = "EXTERIOR";
		int seqNbr = 1;
		CdsRoomList result = target.read(listName,seqNbr);
		if (result!=null) {
			System.out.println("Read by list name " + result.getListName() + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String listName = "OUTERIOR";
		int seqNbr = 0;
		CdsRoomList result = target.read(listName,seqNbr);
		if (result == null) {
			System.out.println("Read by list name " + listName + " NOT FOUND - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}
	
}
