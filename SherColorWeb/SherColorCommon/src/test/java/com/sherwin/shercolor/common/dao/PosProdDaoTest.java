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

import com.sherwin.shercolor.common.domain.PosProd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class PosProdDaoTest {
	@Autowired
	PosProdDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadPk() {
		String salesNbr ="650696370";
		
		PosProd result = target.read(salesNbr);
		
		if (result!=null) {
			System.out.println("Read by SalesNbr -> " + result.getSalesNbr() + " " + result.getProdNbr()+ " " + result.getSzCd() + " " + result.getDescr());
		}

		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String salesNbr ="JUNK";
		
		PosProd result = target.read(salesNbr);
		
		assertTrue(result == null);
	}
	
	@Test 
	public void testReadByProdSz(){
		String prodNbr ="A87W00051";
		String szCd = "16";
		
		PosProd result = target.readByProdNbrSzCd(prodNbr, szCd);
		
		if (result!=null) {
			System.out.println("Read by Prod/Size -> " + result.getSalesNbr() + " " + result.getProdNbr()+ " " + result.getSzCd() + " " + result.getDescr());
		}

		assertNotNull(result);
	}
	
	@Test 
	public void testFailedReadByProdSz(){
		String prodNbr ="JUNKJUNK";
		String szCd = "XX";
		
		PosProd result = target.readByProdNbrSzCd(prodNbr, szCd);
		
		assertTrue(result==null);
	}
	
	@Test
	public void testReadByUpc(){
		String upc ="035777501894";
		
		PosProd result = target.readByUpc(upc);
		
		if (result!=null) {
			System.out.println("Read by UPC -> " + result.getSalesNbr() + " " + result.getProdNbr()+ " " + result.getSzCd() + " " + result.getDescr());
		}

		assertNotNull(result);
	}
	
	@Test
	public void testFailedReadByUpc(){
		String upc ="JUNKJUNKJUNK";
		
		PosProd result = target.readByUpc(upc);
		
		assertTrue(result==null);
	}
	

	@Test 
	public void testSearchByProdNbrPartialMatch(){
		String searchCriteria = "B20W0225";
		
		List<PosProd> result = target.listForProdNbr(searchCriteria, false);
		
		if (result!=null) {
			System.out.println("Search by parital prodNbr [" + searchCriteria + "] match found " + result.size() + " records...");
			for (PosProd product:result){
				System.out.println(product.getSalesNbr() + " " + product.getProdNbr() + " " + product.getSzCd() + " " + product.getDescr());
			}
		}

		assertTrue(result.size()>0);
	}
	
	@Test 
	public void testSearchByProdNbrFullMatch(){
		String searchCriteria = "B20W02251";
		
		List<PosProd> result = target.listForProdNbr(searchCriteria, true);
		
		if (result!=null) {
			System.out.println("Search by full prodNbr [" + searchCriteria + "] match Found " + result.size() + " records...");
			for (PosProd product:result){
				System.out.println(product.getSalesNbr() + " " + product.getProdNbr() + " " + product.getSzCd() + " " + product.getDescr());
			}
		}

		assertTrue(result.size()>0);
	}
	
	@Test
	public void testFailedSearchByProdNbrFullMatch(){
		String searchCriteria = "JUNKJUNK";
		
		List<PosProd> result = target.listForProdNbr(searchCriteria, true);
		
		assertTrue(result.size()==0);
	}
	
	@Test
	public void testStringProductAutocomplete() {
		String searchValue = "B20W012";
		
		List<String> result = target.stringListForAutocomplete(searchValue);

		if (result!=null) {
			System.out.println("Product Autocomplete found " + result.size() + " records with " + searchValue + " in the SalesNbr, ProdNbr or Upc. They are ...");
			for (String item:result){
				System.out.println("--->" + item + " starts with " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testStringProductAutocomplete2() {
		String searchValue = "640310";
		
		List<String> result = target.stringListForAutocomplete(searchValue);

		if (result!=null) {
			System.out.println("Product Autocomplete found " + result.size() + " records with " + searchValue + " in the SalesNbr, ProdNbr or Upc. They are ...");
			for (String item:result){
				System.out.println("--->" + item + " starts with " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}
	
	
	@Test
	public void testProductAutocomplete() {
		String searchValue = "B20W012";
		
		List<PosProd> result = target.listForAutocomplete(searchValue);

		if (result!=null) {
			System.out.println("Product Autocomplete found " + result.size() + " records with " + searchValue + " in the SalesNbr, ProdNbr or Upc. They are ...");
			for (PosProd item:result){
				System.out.println("--->" + item.getProdNbr() + " starts with " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testProductAutocomplete2() {
		String searchValue = "640310";
		
		List<PosProd> result = target.listForAutocomplete(searchValue);

		if (result!=null) {
			System.out.println("Product Autocomplete found " + result.size() + " records with " + searchValue + " in the SalesNbr, ProdNbr or Upc. They are ...");
			for (PosProd item:result){
				System.out.println("--->" + item.getProdNbr() + " starts with " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testListForAutocompletePosProdActive(){
		String partialKey[] = { "65009651", "A96W0125", "03577744023", "65009650", "A96W0125", "035777440223", 
				"65018695", "B20T0265", "03577700498", "65018693", "B20W0265", "03577700496" };
		for (int x = 0; x < 12; x++){
			List<PosProd> result = target.listForAutocompleteActive(partialKey[x]);
			for (int i = 0; i < result.size(); i++){
				System.out.println("\t" + result.get(i).getProdNbr() + "-" + result.get(i).getSzCd() + " NEW"); 
			}
			System.out.println(x + ". NEW Autocomplete for Active Products list retrieved for " + partialKey[x] + 
					"  - NEW count -> " + result.size() + " - test is SUCCESSFUL!");
			assertNotNull(result);
			List<PosProd> resultOld = target.listForAutocomplete(partialKey[x]);
			for (int i = 0; i < resultOld.size(); i++){
				System.out.println("\t" + resultOld.get(i).getProdNbr() + "-" + resultOld.get(i).getSzCd() + " OLD"); 
			}
			System.out.println(x + ". OLD Autocomplete for Active Products list retrieved for " + partialKey[x] + 
					"  - OLD count -> " + resultOld.size() + " - test is SUCCESSFUL!");
			assertNotNull(resultOld);
		}	
	}
	
	@Test 
	public void testListForAutocompletePosProdActiveNotFnd(){
		String partialKey[] = { "69009651", "A99W0125", "03577844023", "69009650", "A99W0125", "035778440223", 
				"69018695", "B90T0265", "03577800498", "69018693", "B90W0265", "03577800496" };
		for (int x = 0; x < 12; x++){
			List<PosProd> result = target.listForAutocompleteActive(partialKey[x]);
			int cnt = result.size();
			if (result.isEmpty())
			System.out.println(x + ". Autocomplete for NOT FOUND Active Products list retrieved for " + partialKey[x] + 
					"  - count -> " + cnt + " - test is SUCCESSFUL!");
			else
				assertNull(result);
			
		}	
	}

}

