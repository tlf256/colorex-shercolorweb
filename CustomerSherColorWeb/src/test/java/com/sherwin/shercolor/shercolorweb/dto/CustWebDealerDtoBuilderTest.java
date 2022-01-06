package com.sherwin.shercolor.shercolorweb.dto;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sherwin.shercolor.common.domain.CustWebDealer;
import com.sherwin.shercolor.shercolorweb.web.dto.CustWebDealerDto;
import com.sherwin.shercolor.shercolorweb.web.dto.CustWebDealerDtoBuilder;

public class CustWebDealerDtoBuilderTest {

	private CustWebDealerDtoBuilder target;

	private String[] customerId 	= {"777777777", "666666666"};
	private String[] dealerName 	= {"Test Dealer 1", "Test Dealer 2"};
	private String 	 dlrPhoneNbr 	= "999-999-9999";
	private int    	 homeStore 		= 4314;
	private String[] comments		= {"Dto Builder Test Dealer 1", "Dto Builder Test Dealer 2"};

	@Before
	public void testCreate() {
		target = new CustWebDealerDtoBuilder();
	}
	
	@Test
	public void testBuild() throws Exception {
		CustWebDealer custWebDealer = new CustWebDealer();
		custWebDealer = fillCustWebDealer1();
		CustWebDealerDto result = target.build(custWebDealer);
		assertNotNull(result);
		testCustWebDealerDto1(result);
		System.out.println("One customer dealer record Dto built successfully");
	}
	
		@Test
		public void testBuildList() throws Exception {
		List<CustWebDealer> customers = new ArrayList<CustWebDealer>();
		CustWebDealer dealer1 = fillCustWebDealer1();
		customers.add(dealer1);
		CustWebDealer dealer2 = fillCustWebDealer2();
		customers.add(dealer2);
		List<CustWebDealerDto> customersDtoList = target.build(customers);
		assertNotNull(customersDtoList);
		testCustWebDealerDtoList(customersDtoList);
		System.out.println("List of customer dealer records Dto built successfully");
	}
	
	@Test
	public void testUpdateBuild() throws Exception{
		CustWebDealerDto dto1 = null;
		dto1 = fillCustWebDealerDto1();
		CustWebDealer updateResult1 = new CustWebDealer();
		updateResult1 = target.buildUpdate(dto1);
		assertNotNull(updateResult1);
		testCustWebDealerUpdate(updateResult1);
		CustWebDealerDto dto2 = null;
		dto2 = fillCustWebDealerDto2();
		CustWebDealer updateResult2 = new CustWebDealer();
		updateResult2 = target.buildUpdate(dto2);
		assertNotNull(updateResult2);
		testCustWebDealerUpdate2(updateResult2);
		System.out.println("one customer dealer record Dto UPDATE built successfully");
	}

	public CustWebDealer fillCustWebDealer1() {
		CustWebDealer custWebDealer = new CustWebDealer();
		custWebDealer.setCustomerId(customerId[0]);
		custWebDealer.setDealerName(dealerName[0]);
		custWebDealer.setDlrPhoneNbr(dlrPhoneNbr);
		custWebDealer.setHomeStore(homeStore);
		custWebDealer.setComments(comments[0]);
	    return custWebDealer;
	}

	public CustWebDealer fillCustWebDealer2() {
		CustWebDealer custWebDealer = new CustWebDealer();
		custWebDealer.setCustomerId(customerId[1]);
		custWebDealer.setDealerName(dealerName[1]);
		custWebDealer.setDlrPhoneNbr(dlrPhoneNbr);
		custWebDealer.setHomeStore(homeStore);
		custWebDealer.setComments(comments[1]);
	    return custWebDealer;
	}

	public void testCustWebDealerDto1(CustWebDealerDto custWebDealerDto) {
		assertEquals(customerId[0], custWebDealerDto.getCustomerId());
	 	assertEquals(dealerName[0], custWebDealerDto.getDealerName());
	 	assertEquals(dlrPhoneNbr, custWebDealerDto.getDlrPhoneNbr());
	 	assertEquals(String.valueOf(homeStore), custWebDealerDto.getHomeStore());
	 	assertEquals(comments[0], custWebDealerDto.getComments());
	}

	public void testCustWebDealerDto2(CustWebDealerDto custWebDealerDto) {
		assertEquals(customerId[1], custWebDealerDto.getCustomerId());
	 	assertEquals(dealerName[1], custWebDealerDto.getDealerName());
	 	assertEquals(dlrPhoneNbr, custWebDealerDto.getDlrPhoneNbr());
	 	assertEquals(String.valueOf(homeStore), custWebDealerDto.getHomeStore());
	 	assertEquals(comments[1], custWebDealerDto.getComments());
	}
	
	private void testCustWebDealerDtoList(List<CustWebDealerDto> customers) {
		int pass = 0;
		for(CustWebDealerDto customer : customers){
			if (pass == 0) testCustWebDealerDto1(customer);
			if (pass == 1) testCustWebDealerDto2(customer);
			pass++;
		}
	}
	
	public CustWebDealerDto fillCustWebDealerDto1() {
		CustWebDealerDto dto1 = new CustWebDealerDto();
		dto1.setCustomerId(customerId[0]);
		dto1.setDealerName(dealerName[0]);
		dto1.setDlrPhoneNbr(dlrPhoneNbr);
		dto1.setHomeStore(String.valueOf(homeStore));
		dto1.setComments(comments[0]);
		return dto1;
	}
		
	public void testCustWebDealerUpdate(CustWebDealer custWebDealer) {
		assertEquals(customerId[0], custWebDealer.getCustomerId());
		assertEquals(dealerName[0], custWebDealer.getDealerName());
		assertEquals(dlrPhoneNbr, custWebDealer.getDlrPhoneNbr());
		assertEquals(homeStore, custWebDealer.getHomeStore());
		assertEquals(comments[0], custWebDealer.getComments());
	}

	public CustWebDealerDto fillCustWebDealerDto2() {
		CustWebDealerDto dto2 = new CustWebDealerDto();
		dto2.setCustomerId(customerId[1]);
		dto2.setDealerName(dealerName[1]);
		dto2.setDlrPhoneNbr(dlrPhoneNbr);
		dto2.setHomeStore(String.valueOf(homeStore));
		dto2.setComments(comments[1]);
		return dto2;
	}

	public void testCustWebDealerUpdate2(CustWebDealer custWebDealer) {
		assertEquals(customerId[1], custWebDealer.getCustomerId());
		assertEquals(dealerName[1], custWebDealer.getDealerName());
		assertEquals(dlrPhoneNbr, custWebDealer.getDlrPhoneNbr());
		assertEquals(homeStore, custWebDealer.getHomeStore());
		assertEquals(comments[1], custWebDealer.getComments());
	}

}
