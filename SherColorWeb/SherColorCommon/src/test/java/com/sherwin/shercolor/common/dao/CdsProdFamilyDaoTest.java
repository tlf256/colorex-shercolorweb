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

import com.sherwin.shercolor.common.domain.CdsProdFamily;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
//@Rollback
public class CdsProdFamilyDaoTest {

	@Autowired
	CdsProdFamilyDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadPk() {
		String name = "A98W0225X";
		String prodNbr = "A98T00154";
		CdsProdFamily result = target.read(name,prodNbr);
		if (result!=null) {
			System.out.println("Product family Read by prodNbr and family name Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String name = "A98W0225X";
		String prodNbr = "A98T00159";
		CdsProdFamily result = target.read(name,prodNbr);
		if (result == null) {
			System.out.println("Product family Read by prodNbr and family name NOT FOUND - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}

	@Test
	public void testPositiveReadbyProdNbrPrimary() {
		String prodNbr = "A98W02251";
		CdsProdFamily result = target.readByProdNbrPrimary(prodNbr);
		if (result!=null) {
			System.out.println("Product Family Read by prodNbr Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadbyProdNbrPrimary() {
		String prodNbr = "A99W02251";
		CdsProdFamily result = target.readByProdNbrPrimary(prodNbr);
		if (result == null) {
			System.out.println("Product Family Read by prodNbr NOT FOUND - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}
	
	@Test 
	public void testListForProdNbr(){
		String prodNbr ="A98W02251";
		List<CdsProdFamily> result = target.listForProdNbr(prodNbr);
		if (result!=null) {
			System.out.println("Product Family List by Product Number retrieved  - test is SUCCESSFUL!");
			System.out.println("Returned " + result.size() + " records.");
		}
		assertNotNull(result);
	}
	
	@Test 
	public void testFalseListForProdNbr(){
		String prodNbr ="B99W00001";
		List<CdsProdFamily> result = target.listForProdNbr(prodNbr);
		if (result.isEmpty()) {
			System.out.println("Product Family List by Product Number NOT retrieved  - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test 
	public void testListForProdNbrExamplleTwo(){
		String prodNbr ="A97T01254";
		List<CdsProdFamily> result = target.listForProdNbr(prodNbr);
		if (result!=null) {
			System.out.println("Product Family List by Product Number (Example 2) retrieved  - test is SUCCESSFUL!");
			System.out.println("Returned " + result.size() + " records.");
			for(CdsProdFamily prodFam : result){
				System.out.println(prodFam.getName() + " " + prodFam.getProdNbr() + " " + prodFam.getProcOrder());
			}
		}
		assertNotNull(result);
	}
	
	@Test 
	public void testFullFamilyListForPrimaryProdNbr(){
		String prodNbr ="A97T01254";
		List<CdsProdFamily> result = target.listFullFamilyForPrimaryProdNbr(prodNbr,false);
		if (result!=null) {
			System.out.println("Full Product Family List for Primary Product Number retrieved  - test is SUCCESSFUL!");
			System.out.println("Returned " + result.size() + " records.");
			for(CdsProdFamily prodFam : result){
				System.out.println(prodFam.getName() + " " + prodFam.getProdNbr() + " " + prodFam.getProcOrder());
			}
		}
		assertNotNull(result);
	}
	
	@Test 
	public void testFalseFullFamilyListForPrimaryProdNbr(){
		String prodNbr ="B99W00001";
		List<CdsProdFamily> result = target.listFullFamilyForPrimaryProdNbr(prodNbr,false);
		if (result.isEmpty()) {
			System.out.println("Full Product Family List for Primary Product Number NOT retrieved  - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

}
