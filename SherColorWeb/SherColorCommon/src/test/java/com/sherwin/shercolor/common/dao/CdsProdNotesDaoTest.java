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

import com.sherwin.shercolor.common.domain.CdsProdNotes;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
//@Rollback
public class CdsProdNotesDaoTest {

	@Autowired
	CdsProdNotesDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadPk() {
		String salesNbr ="650592058";
		int seqNbr = 1;
		CdsProdNotes result = target.read(salesNbr,seqNbr);
		if (result!=null) {
			System.out.println("Read by salesNbr and seqNbr ->  " + result.getSalesNbr() + " " +  result.getSeqNbr());
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String salesNbr ="999999999";
		int seqNbr = 0;
		CdsProdNotes result = target.read(salesNbr,seqNbr);
		if (result == null) {
			System.out.println("Read by salesNbr and seqNbr NOT FOUND - Test is SUCCESSFUL!  ");
		}
		assertTrue(result == null);
	}
	
	@Test 
	public void testListForSalesNbr(){
		String salesNbr ="650592058";
		List<CdsProdNotes> result = target.listForSalesNbr(salesNbr);
		
		if (result!=null) {
			System.out.println("List by Product Number retrieved  - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}
	
	@Test 
	public void testFalesListForSalesNbr(){
		String salesNbr ="999999999";
		List<CdsProdNotes> result = target.listForSalesNbr(salesNbr);
		
		if (result.isEmpty()) {
			System.out.println("List by Product Number retrieved  - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}
	

}
