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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.OeServiceProdDataSet;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.util.domain.SwMessage;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class ProductServiceTest {

	@Rule
    public final ExpectedException exception = ExpectedException.none();
	
	String goodSalesNbr 					= "650096589";
	String invalidSalesNbr 					= "650096580";
	String goodRexSize	 					= "A97W01251 16";
	String goodRexSizeDash	 				= "A06W00053-16";
	String invalidRexSize 					= "A97W01259 16";
	String goodUpcNbr 						= "035777440308";
	String invalidUpcNbr 					= "035999440308";
	String validSalesNbrInvalidSizeCode 	= "601155427";  // B30Wv0077-27 
	String goodProdNbr						= "K43WQ8053";
	String invalidProdNbr					= "INVALIDPROD";
	
	@Autowired
	private ProductService target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveAutocompleteProduct() {
		String searchCriteria ="65036";
		List<CdsProd> result = target.productAutocomplete(searchCriteria);
		if(! result.isEmpty()){
			System.out.println("1. ProductAutoComplete found " + result.size() + " records with " + searchCriteria + " in the product or sales number. They are ...");
			for (CdsProd item:result){
				System.out.println("this " + item.getPrepComment() + " " + item.getSalesNbr() + " " + item.getQuality() + " contains " + searchCriteria);
			}
			System.out.println("1. ProductAutoComplete found " + result.size() + " records with " + searchCriteria + " in the product or sales number.");
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeAutocompleteProduct() {
		String searchCriteria ="XXXXXX";
	
		List<CdsProd> result = target.productAutocomplete(searchCriteria);
		
		if(result.isEmpty()){
			System.out.println("2. ProductAutoComplete NOT FOUND - test is SUCCESSFUL!");
		}
	
		assertNotNull(result);
	}
	
	@Test
	public void testPositiveAutocompleteProductBoth() {
		String searchCriteria ="65036";
		List<CdsProd> result = target.productAutocompleteBoth(searchCriteria);
		if(! result.isEmpty()){
			System.out.println("1. ProductAutoComplete found " + result.size() + " records with " + searchCriteria + " in the product or sales number. They are ...");
			for (CdsProd item:result){
				System.out.println("this " + item.getPrepComment() + " " + item.getSalesNbr() + " " + item.getQuality() + " contains " + searchCriteria);
			}
			System.out.println("21. ProductAutoCompleteBoth found " + result.size() + " records with " + searchCriteria + " in the product or sales number.");
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeAutocompleteProductBoth() {
		String searchCriteria ="XXXXXX";
	
		List<CdsProd> result = target.productAutocomplete(searchCriteria);
		
		if(result.isEmpty()){
			System.out.println("20. ProductAutoCompleteBoth NOT FOUND - test is SUCCESSFUL!");
		}
	
		assertNotNull(result);
	}
	
	@Test
	public void testPositiveAutocompleteProductBothOnlyPosProdOnFile() {
		String searchCriteria ="9110";
		List<CdsProd> result = target.productAutocompleteBoth(searchCriteria);
		if(! result.isEmpty()){
			System.out.println("19. ProductAutoCompleteBoth-PosProdOnly found " + result.size() + " records with " + searchCriteria + " in the product or sales number. They are ...");
			for (CdsProd item:result){
				System.out.println("this " + item.getPrepComment() + " " + item.getSalesNbr() + " " + item.getQuality() + " contains " + searchCriteria);
			}
			System.out.println("19. ProductAutoComplete found " + result.size() + " records with " + searchCriteria + " in the product or sales number.");
		}
		assertNotNull(result);
	}
	
	@Test
	public void testPositiveProductAutoComplete() {
		String searchCriteria ="65036";
		List<PosProd> result = target.autocompleteProduct(searchCriteria);
		if(! result.isEmpty()){
			System.out.println("3. Autocomplete Competitive Color found " + result.size() + " records with " + searchCriteria + " in the product or sales number.  They are:");
			for (PosProd item:result){
				System.out.println("this " + item.getProdNbr() + " " + item.getSalesNbr() + " " + item.getDescr() + " contains " + searchCriteria);
			}		
			System.out.println("3. Autocomplete Competitive Color found " + result.size() + " records with " + searchCriteria + " in the product or sales number.");
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeProductAutocomplete() {
		String searchCriteria ="%$#@&";
		List<PosProd> result = target.autocompleteProduct(searchCriteria);
		if(result.isEmpty()){
			System.out.println("4. ProductAutoComplete NOT FOUND - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}
		
	@Test
	public void testPositiveUPCValidateProduct() {
		List<SwMessage> result = target.validateProduct(goodUpcNbr);
		if(result.isEmpty()){
			System.out.println("5. Testing product UPC number " + goodUpcNbr + " for successful Pos-Prod Service look-up");
		}
		for(SwMessage oneResult : result){
			System.out.println("\nSW Message Severity: " + oneResult.getSeverity() + "\n");
		}
	}	
	
	@Test
	public void testNegativeUPCValidateProduct() {
		List<SwMessage> result = target.validateProduct(invalidUpcNbr);
		if(! result.isEmpty()){
			System.out.println("6. Testing product UPC number " + invalidUpcNbr + " for unsuccessful Pos-Prod Service look-up");
		}
		for(SwMessage oneResult : result){
			System.out.println("\nSW Message Severity: " + oneResult.getSeverity() + "  Message: " + oneResult.getMessage() + "\n");
		}
	}	
	
	
	@Test
	public void testPositiveSalesNbrValidateProduct() {
		List<SwMessage> result = target.validateProduct(goodSalesNbr);
		if(result.isEmpty()){
			System.out.println("7. Testing product sales number " + goodSalesNbr + " for successful Pos-Prod Service look-up");
		}
		for(SwMessage oneResult : result){
			System.out.println("\nSW Message Severity: " + oneResult.getSeverity() + "\n");
		}
	}	
	
	@Test
	public void testNegativeSalesNbrValidateProduct() {
		System.out.println("8. Testing product sales number " + invalidSalesNbr + " for unsuccessful Pos-Prod Service look-up");
		List<SwMessage> result = target.validateProduct(invalidSalesNbr);
		for(SwMessage oneResult : result){
			System.out.println("\nSW Message Severity: " + oneResult.getSeverity() + "  Message: " + oneResult.getMessage() + "\n");
		}
	}	
	
	@Test
	public void testPositiveRexNbrSizeValidateProduct() {
		System.out.println("9. Testing product rex number and size number " + goodRexSize + " for successful Pos-Prod Service look-up");
		List<SwMessage> result = target.validateProduct(goodRexSize);
		for(SwMessage oneResult : result){
			System.out.println("\nSW Message Severity: " + oneResult.getSeverity() + "\n");
		}
	}	
	
	@Test
	public void testNegativeRexNbrSizeValidateProduct() {
		System.out.println("10. Testing product rex number and size " + invalidRexSize + " for unsuccessful Pos-Prod Service look-up");
		List<SwMessage> result = target.validateProduct(invalidRexSize);
		for(SwMessage oneResult : result){
			System.out.println("\nSW Message Severity: " + oneResult.getSeverity() + "  Message: " + oneResult.getMessage() + "\n");
		}
	}	
	
	@Test
	public void testPositiveCdsSalesNbrValidateProduct() {
		System.out.println("11. Testing product sales number " + goodSalesNbr + " for successful Pos-Prod Service look-up");
		List<SwMessage> result = target.validateProduct(goodSalesNbr);
		for(SwMessage oneResult : result){
			System.out.println("\nSW Message Severity: " + oneResult.getSeverity() + "\n");
		}
	}	
	
	@Test
	public void testNegativeCdsSalesNbrValidateProduct() {
		System.out.println("12. Testing product sales number " + invalidSalesNbr + " for unsuccessful Pos-Prod Service look-up");
		List<SwMessage> result = target.validateProduct(invalidSalesNbr);
		for(SwMessage oneResult : result){
			System.out.println("\nSW Message Severity: " + oneResult.getSeverity() + "  Message: " + oneResult.getMessage() + "\n");
		}
	}	
	
	@Test
	public void testPositiveSizeCodeValidateProduct() {
		System.out.println("13. Testing product sales number " + goodSalesNbr + " for successful Pos-Prod Service look-up result size code");
		List<SwMessage> result = target.validateProduct(goodSalesNbr);
		for(SwMessage oneResult : result){
			System.out.println("\nSW Message Severity: " + oneResult.getSeverity() + "\n");
		}
	}	
	
	@Test
	public void testNegativeSizeCodeNbrValidateProduct() {
		System.out.println("14. Testing product sales number " + validSalesNbrInvalidSizeCode + " for unsuccessful Pos-Prod Service look-up result size code");
		List<SwMessage> result = target.validateProduct(validSalesNbrInvalidSizeCode);
		for(SwMessage oneResult : result){
			System.out.println("\nSW Message Severity: " + oneResult.getSeverity() + "  Message: " + oneResult.getMessage() + "\n");
		}
	}	
	
	@Test
	public void testPositiveReadCdsProdCharzd() {
		System.out.println("15. Testing product number " + goodProdNbr + " for successful cdsProdCharzd Service look-up");
		CdsProdCharzd result = target.readCdsProdCharzd(goodProdNbr, "CCE");
		assertNotNull(result);
	}	
	@Test
	public void testNegativeReadCdsProdCharzd() {
		System.out.println("16. Testing product number " + invalidProdNbr + " for unsuccessful cdsProdCharzd Service look-up");
		CdsProdCharzd result = target.readCdsProdCharzd(invalidProdNbr, "CCE");
		assertNull(result);
	}	
	
	@Test
	public void testPositiveReadCdsProd() {
		System.out.println("17. Testing sales number " + goodSalesNbr + " for successful cdsProd Service look-up");
		CdsProd result = target.readCdsProd(goodSalesNbr);
		assertNotNull(result);
	}	
	@Test
	public void testNegativeReadCdsProd() {
		System.out.println("18. Testing sales number " + invalidSalesNbr + " for unsuccessful cdsProd Service look-up");
		CdsProd result = target.readCdsProd(invalidSalesNbr);
		assertNull(result);
	}	
	
	@Test
	public void testFillDsProdFromOracleUsingSalesNbr(){
		String salesNbr = "640413571";
		OeServiceProdDataSet result = target.getDsProdFromOracleBySalesNbr(salesNbr);
		System.out.println("22. Test Fill Oe Service Prod Data Set from Oracle using sales number " + salesNbr);
		// convert to json for display
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String jsonProd = gson.toJson(result);

		System.out.println(jsonProd);

		assertNotNull(result);
	}

	@Test
	public void testNegativeFillDsProdFromOracleUsingSalesNbr(){
		String salesNbr = "999999999999";
		OeServiceProdDataSet result = target.getDsProdFromOracleBySalesNbr(salesNbr);
		System.out.println("23. Test Failed Fill Oe Service Prod Data Set from Oracle using sales number " + salesNbr);

		assertNull(result);
	}

	@Test
	public void testPositiveGetSalesNbrByUPC() {
		String result = target.getSalesNbr(goodUpcNbr);
		
		assertNotNull(result);
	}	
	
	@Test
	public void testFailedGetSalesNbrByUPC() {
		String result = target.getSalesNbr(invalidUpcNbr);
		
		assertNull(result);
	}	
	
	@Test
	public void testPositiveGetSalesNbrBySalesNbr() {
		String result = target.getSalesNbr(goodSalesNbr);
		
		assertNotNull(result);
	}	
	
	@Test
	public void testFailedGetSalesNbrBySalesNbr() {
		String result = target.getSalesNbr(invalidSalesNbr);
		
		assertNull(result);
	}
	
	@Test
	public void testPositiveGetSalesNbrByProdAndSizeDash() {
		String result = target.getSalesNbr(goodRexSizeDash);
		
		assertNotNull(result);
	}
	
	@Test
	public void testPositiveGetSalesNbrByProdAndSize() {
		String result = target.getSalesNbr(goodRexSize);
		
		assertNotNull(result);
	}	
	
	@Test
	public void testFailedGetSalesNbrByProdAndSize() {
		String result = target.getSalesNbr(invalidRexSize);
		
		assertNull(result);
	}	

}
