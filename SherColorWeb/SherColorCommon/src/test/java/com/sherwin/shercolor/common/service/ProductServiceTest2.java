package com.sherwin.shercolor.common.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.sherwin.shercolor.common.domain.CdsProd;
/*
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.OeServiceProdDataSet;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.util.domain.SwMessage;

*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class ProductServiceTest2 {

	@Rule
    public final ExpectedException exception = ExpectedException.none();
	/*
	String goodSalesNbr 					= "650096589";
	String invalidSalesNbr 					= "650096580";
	String goodRexSize	 					= "A97W01251 16";
	String invalidRexSize 					= "A97W01259 16";
	String goodUpcNbr 						= "035777440308";
	String invalidUpcNbr 					= "035999440308";
	String validSalesNbrInvalidSizeCode 	= "601155427";  // B30Wv0077-27 
	String goodProdNbr						= "K43WQ8053";
	String invalidProdNbr					= "INVALIDPROD";
	*/
	@Autowired
	private ProductService target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveAutocompleteProductActiveSalesNbr() {
		String searchCriteria ="65036";
		List<CdsProd> result = target.productAutocompleteActive(searchCriteria);
		if(! result.isEmpty()){
			System.out.println("101. ProductAutoComplete found " + result.size() + " records with " + searchCriteria + " in the product or sales number. They are ...");
			for (CdsProd item:result){
				System.out.println("this " + item.getPrepComment() + " " + item.getSalesNbr() + " " + item.getQuality() + " contains " + searchCriteria);
			}
			System.out.println("102. ProductAutoComplete found " + result.size() + " records with " + searchCriteria + " in the product or sales number.");
		}
		assertNotNull(result);
	}
	
	@Test
	public void testPositiveAutocompleteProductActiveRex() {
		String searchCriteria ="A97";
		List<CdsProd> result = target.productAutocompleteActive(searchCriteria);
		if(! result.isEmpty()){
			System.out.println("103. ProductAutoComplete found " + result.size() + " records with " + searchCriteria + " in the product or sales number. They are ...");
			for (CdsProd item:result){
				System.out.println("this " + item.getPrepComment() + " " + item.getSalesNbr() + " " + item.getQuality() + " contains " + searchCriteria);
			}
			System.out.println("104. ProductAutoComplete found " + result.size() + " records with " + searchCriteria + " in the product or sales number.");
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeAutocompleteProductActive() {
		String searchCriteria ="XXXXXX";
	
		List<CdsProd> result = target.productAutocompleteActive(searchCriteria);
		
		if(result.isEmpty()){
			System.out.println("105. ProductAutoComplete for " + searchCriteria + " NOT FOUND - test is SUCCESSFUL!  Records found -> "
					+ result.size());
		}
	
		assertNotNull(result);
	}
	
	@Test
	public void testPositiveAutocompleteProductBothActiveSales() {
		String searchCriteria ="65036";
		List<CdsProd> result = target.productAutocompleteBothActive(searchCriteria);
		if(! result.isEmpty()){
			System.out.println("106. ProductAutoComplete found " + result.size() + " records with " + searchCriteria + " in the product or sales number. They are ...");
			for (CdsProd item:result){
				System.out.println("this " + item.getPrepComment() + " " + item.getSalesNbr() + " " + item.getQuality() + " contains " + searchCriteria);
			}
			System.out.println("107. ProductAutoCompleteBoth found " + result.size() + " records with " + searchCriteria + " in the product or sales number.");
		}
		assertNotNull(result);
	}
	
	@Test
	public void testPositiveAutocompleteProductBothActiveRex() {
		String searchCriteria ="B20W01";
		List<CdsProd> result = target.productAutocompleteBothActive(searchCriteria);
		if(! result.isEmpty()){
			System.out.println("108. ProductAutoComplete found " + result.size() + " records with " + searchCriteria + " in the product or sales number. They are ...");
			for (CdsProd item:result){
				System.out.println("this " + item.getPrepComment() + " " + item.getSalesNbr() + " " + item.getQuality() + " contains " + searchCriteria);
			}
			System.out.println("109. ProductAutoCompleteBoth found " + result.size() + " records with " + searchCriteria + " in the product or sales number.");
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeAutocompleteProductBothActive() {
		String searchCriteria ="XXXXXY";
	
		List<CdsProd> result = target.productAutocompleteBothActive(searchCriteria);
		
		if(result.isEmpty()){
			System.out.println("110. ProductAutoCompleteBoth " + searchCriteria + " FOUND - test is SUCCESSFUL!  Records found -> "
					+ result.size());
		}
	
		assertNotNull(result);
	}

	@Test
	public void testNegativeAutocompleteProductBothActive2() {
		String searchCriteria ="ASDFLKJH";
	
		List<CdsProd> result = target.productAutocompleteBothActive(searchCriteria);
		
		if(result.isEmpty()){
			System.out.println("111. ProductAutoCompleteBoth " + searchCriteria + " NOT FOUND - test is SUCCESSFUL!   Records found -> "
					+ result.size());
		}
	
		assertNotNull(result);
	}

}
