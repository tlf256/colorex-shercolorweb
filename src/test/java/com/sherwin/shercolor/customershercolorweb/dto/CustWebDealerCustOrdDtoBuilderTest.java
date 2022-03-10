package com.sherwin.shercolor.customershercolorweb.dto;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustOrdDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustOrdDtoBuilder;

public class CustWebDealerCustOrdDtoBuilderTest {

	private CustWebDealerCustOrdDtoBuilder target;

	private String[] customerId 	= {"777777777", "666666666"};
	private String[] comments		= {"Dto Builder Test Dealer 1", "Dto Builder Test Dealer 2"};
	private String   dlrCustId  	= "9999";
	private int[]    controlNbr     = {99999, 66666}; 
	private String   custOrderNbr 	= "ABC123";
	private String   updateUser 	= "JDD";
	private String   updateDate	    = "11/01/2017";
	private Date     compareDate	= null;

	
	@Before
	public void testCreate() throws ParseException {
		target = new CustWebDealerCustOrdDtoBuilder();
		try{
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
			Date theDate = (Date)formatter.parse(updateDate); 			
			compareDate = theDate;
		}
		catch(ParseException pe){
			throw pe;
		}
		System.out.println("Test Date -> " + compareDate.toString());
	}
	
	@Test
	public void testBuild() throws Exception {
		CustWebDealerCustOrd custWebDealerCustOrd = new CustWebDealerCustOrd();
		custWebDealerCustOrd = fillCustWebDealerCustOrd1();
		CustWebDealerCustOrdDto result = target.build(custWebDealerCustOrd);
		assertNotNull(result);
		testCustWebDealerCustOrdDto1(result);
		System.out.println("Test successful for building ONE customer dealer order with Dto.");
	}
	
		@Test
		public void testBuildList() throws Exception {
		List<CustWebDealerCustOrd> customers = new ArrayList<CustWebDealerCustOrd>();
		CustWebDealerCustOrd dealer1 = fillCustWebDealerCustOrd1();
		customers.add(dealer1);
		CustWebDealerCustOrd dealer2 = fillCustWebDealerCustOrd2();
		customers.add(dealer2);
		List<CustWebDealerCustOrdDto> customersDtoList = target.build(customers);
		assertNotNull(customersDtoList);
		testCustWebDealerCustOrdDtoList(customersDtoList);
		System.out.println("Test successful for building customer dealer order List with Dto.");
	}
	
	@Test
	public void testUpdateBuild() throws Exception {
		CustWebDealerCustOrdDto dto1 = null;
		dto1 = fillCustWebDealerCustOrdDto1();
		CustWebDealerCustOrd updateResult1 = new CustWebDealerCustOrd();
		updateResult1 = target.buildUpdate(dto1);
		assertNotNull(updateResult1);
		testCustWebDealerCustOrdUpdate(updateResult1);
		System.out.println("Test successful for building customer dealer order UPDATE with domain.");
	}
	
		@Test
		public void testUpdateBuildList() throws Exception {
		CustWebDealerCustOrdDto dto2 = null;
		dto2 = fillCustWebDealerCustOrdDto2();
		CustWebDealerCustOrd updateResult2 = new CustWebDealerCustOrd();
		updateResult2 = target.buildUpdate(dto2);
		assertNotNull(updateResult2);
		testCustWebDealerCustOrdUpdate2(updateResult2);
		System.out.println("Test successful for building customer dealer order List UPDATE with domain.");
	}

	public CustWebDealerCustOrd fillCustWebDealerCustOrd1() throws ParseException {
		CustWebDealerCustOrd custWebDealerCustOrd = new CustWebDealerCustOrd();
		custWebDealerCustOrd.setCustomerId(customerId[0]);
		custWebDealerCustOrd.setComments(comments[0]);
		custWebDealerCustOrd.setDlrCustId(dlrCustId);
		custWebDealerCustOrd.setControlNbr(controlNbr[0]);
		custWebDealerCustOrd.setCustOrderNbr(custOrderNbr);
		custWebDealerCustOrd.setUpdateUser(updateUser);
/*
		try{
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
			Date theDate = (Date)formatter.parse(updateDate); 			
			custWebDealerCustOrd.setDateAdded(theDate);
			custWebDealerCustOrd.setDateUpdated(theDate);
		}
		catch(ParseException pe){
			throw pe;
		}

*/		
		return custWebDealerCustOrd;
	}

	public CustWebDealerCustOrd fillCustWebDealerCustOrd2() throws ParseException {
		CustWebDealerCustOrd custWebDealerCustOrd = new CustWebDealerCustOrd();
		custWebDealerCustOrd.setCustomerId(customerId[1]);
		custWebDealerCustOrd.setDlrCustId(dlrCustId);
		custWebDealerCustOrd.setUpdateUser(updateUser);
		custWebDealerCustOrd.setControlNbr(controlNbr[1]);
		custWebDealerCustOrd.setCustOrderNbr(custOrderNbr);
		custWebDealerCustOrd.setComments(comments[1]);
/*
		try{
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
			Date theDate = (Date)formatter.parse(updateDate); 			
			custWebDealerCustOrd.setDateAdded(theDate);
			custWebDealerCustOrd.setDateUpdated(theDate);
		}
		catch(ParseException pe){
			throw pe;
		}
*/	    
		return custWebDealerCustOrd;
	}

	public void testCustWebDealerCustOrdDto1(CustWebDealerCustOrdDto custWebDealerCustOrdDto) {
		assertEquals(customerId[0], custWebDealerCustOrdDto.getCustomerId());
		assertEquals(dlrCustId, custWebDealerCustOrdDto.getDlrCustId());
		assertEquals(Integer.toString(controlNbr[0]), custWebDealerCustOrdDto.getControlNbr());
		assertEquals(custOrderNbr, custWebDealerCustOrdDto.getCustOrderNbr());
		assertEquals(updateUser, custWebDealerCustOrdDto.getUpdateUser());
	 	assertEquals(comments[0], custWebDealerCustOrdDto.getComments());
/*
		assertEquals(compareDate.toString(), custWebDealerCustOrdDto.getDateAdded());
		assertEquals(compareDate.toString(), custWebDealerCustOrdDto.getDateUpdated());

*/	
	}

	public void testCustWebDealerCustOrdDto2(CustWebDealerCustOrdDto custWebDealerCustOrdDto) throws ParseException {
		assertEquals(customerId[1], custWebDealerCustOrdDto.getCustomerId());
		assertEquals(dlrCustId, custWebDealerCustOrdDto.getDlrCustId());
		assertEquals(Integer.toString(controlNbr[1]), custWebDealerCustOrdDto.getControlNbr());
		assertEquals(custOrderNbr, custWebDealerCustOrdDto.getCustOrderNbr());
		assertEquals(updateUser, custWebDealerCustOrdDto.getUpdateUser());
	 	assertEquals(comments[1], custWebDealerCustOrdDto.getComments());
/*
		assertEquals(compareDate.toString(), custWebDealerCustOrdDto.getDateAdded());
		assertEquals(compareDate.toString(), custWebDealerCustOrdDto.getDateUpdated());
	
*/
	}
	
	private void testCustWebDealerCustOrdDtoList(List<CustWebDealerCustOrdDto> customers) throws ParseException {
		int pass = 0;
		for(CustWebDealerCustOrdDto customer : customers){
			if (pass == 0) testCustWebDealerCustOrdDto1(customer);
			if (pass == 1) testCustWebDealerCustOrdDto2(customer);
			pass++;
		}
	}
	
	public CustWebDealerCustOrdDto fillCustWebDealerCustOrdDto1() {
		CustWebDealerCustOrdDto dto1 = new CustWebDealerCustOrdDto();
		dto1.setCustomerId(customerId[0]);
		dto1.setDlrCustId(dlrCustId);
		dto1.setControlNbr(Integer.toString(controlNbr[0]));
		dto1.setCustOrderNbr(custOrderNbr);
		dto1.setUpdateUser(updateUser);
		dto1.setComments(comments[0]);
		return dto1;
	}
		
	public void testCustWebDealerCustOrdUpdate(CustWebDealerCustOrd custWebDealerCustOrd) {
		assertEquals(customerId[0], custWebDealerCustOrd.getCustomerId());
		assertEquals(dlrCustId, custWebDealerCustOrd.getDlrCustId());
		assertEquals(controlNbr[0], custWebDealerCustOrd.getControlNbr());
		assertEquals(custOrderNbr, custWebDealerCustOrd.getCustOrderNbr());
		assertEquals(updateUser, custWebDealerCustOrd.getUpdateUser());
		assertEquals(comments[0], custWebDealerCustOrd.getComments());
	}

	public CustWebDealerCustOrdDto fillCustWebDealerCustOrdDto2() {
		CustWebDealerCustOrdDto dto2 = new CustWebDealerCustOrdDto();
		dto2.setCustomerId(customerId[1]);
		dto2.setComments(comments[1]);
		dto2.setDlrCustId(dlrCustId);
		dto2.setControlNbr(Integer.toString(controlNbr[1]));
		dto2.setCustOrderNbr(custOrderNbr);
		dto2.setUpdateUser(updateUser);
		return dto2;
	}

	public void testCustWebDealerCustOrdUpdate2(CustWebDealerCustOrd custWebDealerCustOrd) {
		assertEquals(customerId[1], custWebDealerCustOrd.getCustomerId());
		assertEquals(dlrCustId, custWebDealerCustOrd.getDlrCustId());
		assertEquals(controlNbr[1], custWebDealerCustOrd.getControlNbr());
		assertEquals(custOrderNbr, custWebDealerCustOrd.getCustOrderNbr());
		assertEquals(updateUser, custWebDealerCustOrd.getUpdateUser());
		assertEquals(comments[1], custWebDealerCustOrd.getComments());
	}

}
