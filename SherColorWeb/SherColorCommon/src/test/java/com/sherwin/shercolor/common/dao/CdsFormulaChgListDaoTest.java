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

import com.sherwin.shercolor.common.domain.CdsFormulaChgList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class CdsFormulaChgListDaoTest {

	@Autowired
	private CdsFormulaChgListDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveReadCdsFormulaChgList() {
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "0057";
		String salesNbr = "650924830";
		String clrntSysId = "CCE";

		CdsFormulaChgList result = target.read(colorComp, colorId, salesNbr,
				clrntSysId);

		assertNotNull(result);
	}

	@Test
	public void testNegativeReadCdsFormulaChgList() {
		String colorComp = "JUNK";
		String colorId = "JUNK";
		String salesNbr = "JUNK";
		String clrntSysId = "JUNK";

		CdsFormulaChgList result = target.read(colorComp, colorId, salesNbr,
				clrntSysId);

		assertNull(result);
	}
	
	@Test 
	public void testListDeletesForProductAndColor(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "0008";
		String salesNbr = "650866239";
		
		List<CdsFormulaChgList> result = target.listDeletesForColorAndProduct(colorComp, colorId, salesNbr);
		
		if (result!=null) {
			System.out.println("List AFCD Deletes for [" + colorComp + " " + colorId + " " + salesNbr + "] match Found " + result.size() + " records...");
			for (CdsFormulaChgList afcd:result){
				System.out.println("--->" + afcd.getColorComp() + " " + afcd.getColorId() + " " + afcd.getSalesNbr() + " " + afcd.getTypeCode() + " " + afcd.getCdsMessageId());
			} 
		}

		assertTrue(result.size()>0);
	}
	
	@Test
	public void testFailedListDeletesForProductAndColor(){
		String colorComp = "JUNK";
		String colorId = "JUNK";
		String salesNbr = "JUNK";
		
		List<CdsFormulaChgList> result = target.listDeletesForColorAndProduct(colorComp, colorId, salesNbr);
		
		assertTrue(result.size()==0);
	}
	
	@Test 
	public void testListProductForAlternateBaseRequired(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "7600";
		
		List<String> result = target.listProductForAlternateBaseRequired(colorComp, colorId);
		
		if (result!=null) {
			System.out.println("List AFCD Alternate Product Bases for [" + colorComp + " " + colorId + "] match Found " + result.size() + " products...");
			for (String prodNbr :result){
				System.out.println("--->" + prodNbr);
			} 
		}

		assertTrue(result.size()>0);
	}
	


}
