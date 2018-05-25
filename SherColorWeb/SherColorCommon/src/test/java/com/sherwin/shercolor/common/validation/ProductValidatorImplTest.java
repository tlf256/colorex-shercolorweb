package com.sherwin.shercolor.common.validation;

import static org.junit.Assert.*;

import javax.validation.ValidationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class ProductValidatorImplTest {

	@Rule
    public final ExpectedException exception = ExpectedException.none();
	
	String goodSalesNbr 			= "650096589";
	String invalidSalesNbr 			= "650096580";
	String goodRexSize	 			= "A06W00053-16";
	String invalidRexSize 			= "A97W01259 16";
	String goodUpcNbr 				= "035777440308";
	String invalidUpcNbr 			= "035999440308";
	String invalidSalesNbrSizeCode 	= "601155427";  // B30Wv0077-27 
	
	@Autowired
	private ProductValidator target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveCheckPosProd() {
		System.out.println("1. Testing sales number " + goodSalesNbr + " for successful Pos-Prod look-up");
		target.checkPosProd(goodSalesNbr);
	}
	
	@Test
	public void testNegativeCheckPosProd() {
		System.out.println("2. Testing sales number " + goodSalesNbr + " for unsuccessful Pos-Prod look-up");
		exception.expect(ValidationException.class);
		target.checkPosProd(invalidSalesNbr);
	}
	
	@Test
	public void testPositiveCheckPosProdByRex() {
		System.out.println("3. Testing rex number and size code " + goodRexSize + " for successful Pos-Prod look-up");
		target.checkPosProdByRex(goodRexSize);
	}
	
	@Test
	public void testNegativeCheckPosProdByRex() {
		System.out.println("4. Testing rex number and size code " + invalidRexSize + " for unsuccessful  Pos-Prod look-up");
		exception.expect(ValidationException.class);
		target.checkPosProdByRex(invalidRexSize);
	}
	
	@Test
	public void testPositiveCheckPosProdByUpc() {
		System.out.println("5. Testing UPC code " + goodUpcNbr + " for successful Pos-Prod look-up");
		//exception.expect(ValidationException.class);
		target.checkPosProdByUpc(goodUpcNbr);
	}
	
	@Test
	public void testNegativeCheckPosProdByUpc() {
		System.out.println("6. Testing UPC code " + invalidUpcNbr + " for unsuccessful Pos-Prod look-up");
		exception.expect(ValidationException.class);
		target.checkPosProdByUpc(invalidUpcNbr);
	}
	
	@Test
	public void testPositiveCheckCdsProd() {
		System.out.println("7. Testing sales number " + goodSalesNbr + " for successful Cds-Prod look-up");
		target.checkCdsProd(goodSalesNbr);
	}
	
	@Test
	public void testNegativeCheckCdsProd() {
		System.out.println("8. Testing sales number " + invalidSalesNbr + " for unsuccessful Cds-Prod look-up");
		exception.expect(ValidationException.class);
		target.checkCdsProd(invalidSalesNbr);
	}
	
	@Test
	public void testPositiveCheckSizeCode() {
		System.out.println("9. Testing sales number " + goodSalesNbr + " for successful Size Code look-up");
		target.checkCdsProd(goodSalesNbr);
	}
	
	@Test
	public void testNegativeCheckSizeCode() {
		System.out.println("10. Testing sales number " + invalidSalesNbrSizeCode + " for unsuccessful Size Code look-up");
		exception.expect(ValidationException.class);
		target.checkCdsProd(invalidSalesNbrSizeCode);
	}

}
