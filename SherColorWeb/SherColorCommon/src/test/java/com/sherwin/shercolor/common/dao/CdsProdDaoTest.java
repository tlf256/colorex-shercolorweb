package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsProd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional

public class CdsProdDaoTest {

	@Autowired
	CdsProdDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadPk() {
		String salesNbr ="640512901";
		CdsProd result = target.read(salesNbr);
		if (result!=null) {
			System.out.println("Read by prodNbr -> " + result.getSalesNbr() + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String salesNbr ="640512909";
		CdsProd result = target.read(salesNbr);
		if (result == null) {
			System.out.println("Read by prodNbr NOT FOUND - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}

	@Test 
	public void testListForBaseType(){
		String baseType ="EXTRA WHITE";
		List<CdsProd> result = target.listForBaseType(baseType);
		
		if (result!=null) {
			System.out.println("List by Base Type retrieved  - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}
	
	@Test 
	public void testFalseListForBaseType(){
		String baseType ="green";
		List<CdsProd> result = target.listForBaseType(baseType);
		
		if (result.isEmpty()) {
			System.out.println("List by Base Type NOT retrieved  - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test 
	public void testListForAutocompleteProduct(){
		String partialKey ="B20";
		List<CdsProd> result = target.listForAutocompleteProduct(partialKey);
		
		if (result!=null) {
			System.out.println("Autocomplete list retrieved  - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}
	
	@Test 
	public void testFalseListForAutocomplete(){
		String partialKey ="X20";
		List<CdsProd> result = target.listForAutocompleteProduct(partialKey);
		
		if (result.isEmpty()) {
			System.out.println("Autocomplete list NOT retrieved  - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test 
	public void testListForAutocompleteProductActive(){
		String partialKey[] = { "B20", "A96", "A97", "A98", "A97W", "A97T", "A97Y", "A97R", "PROMAR 200 ZERO", "SUPER", "6509", "6401" };
		for (int x = 0; x < 12; x++){
			List<CdsProd> result = target.listForAutocompleteProductActive(partialKey[x]);
			System.out.println("New Autocomplete for Active Products list retrieved for " + partialKey[x] + 
					"  - NEW count -> " + result.size() + " - test is SUCCESSFUL!");
			assertNotNull(result);
			List<CdsProd> resultOld = target.listForAutocompleteProduct(partialKey[x]);
			System.out.println("Old Autocomplete for Active Products list retrieved for " + partialKey[x] + 
					"  - OLD count -> " + resultOld.size() + " - test is SUCCESSFUL!");
			assertNotNull(result);
		}	
	}
	
	@Test 
	public void testListForAutocompleteProductActiveNotFnd(){
		String partialKey[] = { "Z20", "Z99", "Z97", "Z98", "PROMAR 500 ZERO", "SUPERMAN", "9909", "9904" };
		for (int x = 0; x < 8; x++){
			List<CdsProd> result = target.listForAutocompleteProductActive(partialKey[x]);
			int cnt = result.size();
			if (result.isEmpty())
			System.out.println("Autocomplete for Active Products list retrieved for " + partialKey[x] + 
					"  - count -> " + cnt + " - test is SUCCESSFUL!");
			else
				assertNull(result);
		}	
	}
	
}
