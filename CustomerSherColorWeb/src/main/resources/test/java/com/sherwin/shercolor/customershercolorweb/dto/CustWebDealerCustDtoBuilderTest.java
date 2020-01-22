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

import com.sherwin.shercolor.common.domain.CustWebDealerCust;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustDtoBuilder;

public class CustWebDealerCustDtoBuilderTest {

	private CustWebDealerCustDtoBuilder target;

	private String[] customerId 	= {"777777777", "666666666"};
	private String[] comments		= {"Dto Builder Test Dealer 1", "Dto Builder Test Dealer 2"};
	private String dlrCustId  		= "9999";
	private String dlrCustName 		= "Test Customer";
	private String dlrCustPhoneNbr 	= "999-999-9999";
	private String dlrCustAddress 	= "123 Main St.";
	private String dlrCustCity 		= "Cleveland";
	private String dlrCustState 	= "OH";
	private String dlrCustZip 		= "44115";
	private String dlrCustCountry 	= "US";
	private String dlrCustContact 	= "John Doe";
	private String updateUser 		= "JDD";
	private String updateDate	    = "11/01/2017";
	private Date   compareDate		= null;

	@Before
	public void testCreate() throws ParseException {
		target = new CustWebDealerCustDtoBuilder();
		try{
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
			Date theDate = (Date)formatter.parse(updateDate); 			
			compareDate = theDate;
		}
		catch(ParseException pe){
			throw pe;
		}
	}
	
	@Test
	public void testBuild() throws Exception {
		CustWebDealerCust custWebDealerCust = new CustWebDealerCust();
		custWebDealerCust = fillCustWebDealerCust1();
		CustWebDealerCustDto result = target.build(custWebDealerCust);
		assertNotNull(result);
		testCustWebDealerCustDto1(result);
	}
	
		@Test
		public void testBuildList() throws Exception {
		List<CustWebDealerCust> customers = new ArrayList<CustWebDealerCust>();
		CustWebDealerCust dealer1 = fillCustWebDealerCust1();
		customers.add(dealer1);
		CustWebDealerCust dealer2 = fillCustWebDealerCust2();
		customers.add(dealer2);
		List<CustWebDealerCustDto> customersDtoList = target.build(customers);
		assertNotNull(customersDtoList);
		testCustWebDealerCustDtoList(customersDtoList);
	}
	
	@Test
	public void testUpdateBuild() throws Exception{
		CustWebDealerCustDto dto1 = null;
		dto1 = fillCustWebDealerCustDto1();
		CustWebDealerCust updateResult1 = new CustWebDealerCust();
		updateResult1 = target.buildUpdate(dto1);
		assertNotNull(updateResult1);
		testCustWebDealerCustUpdate(updateResult1);
 
		CustWebDealerCustDto dto2 = null;
		dto2 = fillCustWebDealerCustDto2();
		CustWebDealerCust updateResult2 = new CustWebDealerCust();
		updateResult2 = target.buildUpdate(dto2);
		assertNotNull(updateResult2);
		testCustWebDealerCustUpdate2(updateResult2);
	}

	public CustWebDealerCust fillCustWebDealerCust1() throws ParseException {
		CustWebDealerCust custWebDealerCust = new CustWebDealerCust();
		custWebDealerCust.setCustomerId(customerId[0]);
		custWebDealerCust.setComments(comments[0]);
		custWebDealerCust.setDlrCustId(dlrCustId);
		custWebDealerCust.setDlrCustName(dlrCustName);
		custWebDealerCust.setDlrCustPhoneNbr(dlrCustPhoneNbr);
		custWebDealerCust.setDlrCustAddress(dlrCustAddress);
		custWebDealerCust.setDlrCustCity(dlrCustCity);
		custWebDealerCust.setDlrCustState(dlrCustState);
		custWebDealerCust.setDlrCustZip(dlrCustZip);
		custWebDealerCust.setDlrCustCountry(dlrCustCountry);
		custWebDealerCust.setDlrCustContact(dlrCustContact);
		custWebDealerCust.setUpdateUser(updateUser);
		try{
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
			Date theDate = (Date)formatter.parse(updateDate); 			
			custWebDealerCust.setDateAdded(theDate);
			custWebDealerCust.setDateUpdated(theDate);
		}
		catch(ParseException pe){
			throw pe;
		}
		return custWebDealerCust;
	}

	public CustWebDealerCust fillCustWebDealerCust2() throws ParseException {
		CustWebDealerCust custWebDealerCust = new CustWebDealerCust();
		custWebDealerCust.setCustomerId(customerId[1]);
		custWebDealerCust.setComments(comments[1]);
		custWebDealerCust.setDlrCustId(dlrCustId);
		custWebDealerCust.setDlrCustName(dlrCustName);
		custWebDealerCust.setDlrCustPhoneNbr(dlrCustPhoneNbr);
		custWebDealerCust.setDlrCustAddress(dlrCustAddress);
		custWebDealerCust.setDlrCustCity(dlrCustCity);
		custWebDealerCust.setDlrCustState(dlrCustState);
		custWebDealerCust.setDlrCustZip(dlrCustZip);
		custWebDealerCust.setDlrCustCountry(dlrCustCountry);
		custWebDealerCust.setDlrCustContact(dlrCustContact);
		custWebDealerCust.setUpdateUser(updateUser);
		try{
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
			Date theDate = (Date)formatter.parse(updateDate); 			
			custWebDealerCust.setDateAdded(theDate);
			custWebDealerCust.setDateUpdated(theDate);
		}
		catch(ParseException pe){
			throw pe;
		}

	    return custWebDealerCust;
	}

	public void testCustWebDealerCustDto1(CustWebDealerCustDto custWebDealerCustDto) {
		assertEquals(customerId[0], custWebDealerCustDto.getCustomerId());
	 	assertEquals(comments[0], custWebDealerCustDto.getComments());
		assertEquals(dlrCustId, custWebDealerCustDto.getDlrCustId());
		assertEquals(dlrCustName, custWebDealerCustDto.getDlrCustName());
		assertEquals(dlrCustPhoneNbr,custWebDealerCustDto.getDlrCustPhoneNbr());
		assertEquals(dlrCustAddress,custWebDealerCustDto.getDlrCustAddress());
		assertEquals(dlrCustCity, custWebDealerCustDto.getDlrCustCity());
		assertEquals(dlrCustState, custWebDealerCustDto.getDlrCustState());
		assertEquals(dlrCustZip, custWebDealerCustDto.getDlrCustZip());
		assertEquals(dlrCustCountry, custWebDealerCustDto.getDlrCustCountry());
		assertEquals(dlrCustContact, custWebDealerCustDto.getDlrCustContact());
		assertEquals(updateUser, custWebDealerCustDto.getUpdateUser());
		assertEquals(compareDate.toString(), custWebDealerCustDto.getDateAdded());
		assertEquals(compareDate.toString(), custWebDealerCustDto.getDateUpdated());
	}

	public void testCustWebDealerCustDto2(CustWebDealerCustDto custWebDealerCustDto) throws ParseException {
		assertEquals(customerId[1], custWebDealerCustDto.getCustomerId());
	 	assertEquals(comments[1], custWebDealerCustDto.getComments());
		assertEquals(dlrCustId, custWebDealerCustDto.getDlrCustId());
		assertEquals(dlrCustName, custWebDealerCustDto.getDlrCustName());
		assertEquals(dlrCustPhoneNbr,custWebDealerCustDto.getDlrCustPhoneNbr());
		assertEquals(dlrCustAddress,custWebDealerCustDto.getDlrCustAddress());
		assertEquals(dlrCustCity, custWebDealerCustDto.getDlrCustCity());
		assertEquals(dlrCustState, custWebDealerCustDto.getDlrCustState());
		assertEquals(dlrCustZip, custWebDealerCustDto.getDlrCustZip());
		assertEquals(dlrCustCountry, custWebDealerCustDto.getDlrCustCountry());
		assertEquals(dlrCustContact, custWebDealerCustDto.getDlrCustContact());
		assertEquals(updateUser, custWebDealerCustDto.getUpdateUser());
		assertEquals(compareDate.toString(), custWebDealerCustDto.getDateAdded());
		assertEquals(compareDate.toString(), custWebDealerCustDto.getDateUpdated());
	}
	
	private void testCustWebDealerCustDtoList(List<CustWebDealerCustDto> customers) throws ParseException {
		int pass = 0;
		for(CustWebDealerCustDto customer : customers){
			if (pass == 0) testCustWebDealerCustDto1(customer);
			if (pass == 1) testCustWebDealerCustDto2(customer);
			pass++;
		}
	}
	
	public CustWebDealerCustDto fillCustWebDealerCustDto1() {
		CustWebDealerCustDto dto1 = new CustWebDealerCustDto();
		dto1.setCustomerId(customerId[0]);
		dto1.setComments(comments[0]);
		dto1.setDlrCustId(dlrCustId);
		dto1.setDlrCustName(dlrCustName);
		dto1.setDlrCustPhoneNbr(dlrCustPhoneNbr);
		dto1.setDlrCustAddress(dlrCustAddress);
		dto1.setDlrCustCity(dlrCustCity);
		dto1.setDlrCustState(dlrCustState);
		dto1.setDlrCustZip(dlrCustZip);
		dto1.setDlrCustCountry(dlrCustCountry);
		dto1.setDlrCustContact(dlrCustContact);
		dto1.setUpdateUser(updateUser);
		dto1.setDateUpdated(updateDate);
		dto1.setDateAdded(updateDate);
		return dto1;
	}
		
	public void testCustWebDealerCustUpdate(CustWebDealerCust custWebDealerCust) {
		assertEquals(customerId[0], custWebDealerCust.getCustomerId());
		assertEquals(dlrCustId, custWebDealerCust.getDlrCustId());
		assertEquals(dlrCustName, custWebDealerCust.getDlrCustName());
		assertEquals(dlrCustPhoneNbr,custWebDealerCust.getDlrCustPhoneNbr());
		assertEquals(dlrCustAddress,custWebDealerCust.getDlrCustAddress());
		assertEquals(dlrCustCity, custWebDealerCust.getDlrCustCity());
		assertEquals(dlrCustState, custWebDealerCust.getDlrCustState());
		assertEquals(dlrCustZip, custWebDealerCust.getDlrCustZip());
		assertEquals(dlrCustCountry, custWebDealerCust.getDlrCustCountry());
		assertEquals(dlrCustContact, custWebDealerCust.getDlrCustContact());
		assertEquals(updateUser, custWebDealerCust.getUpdateUser());
		assertEquals(comments[0], custWebDealerCust.getComments());
		assertEquals(compareDate, custWebDealerCust.getDateUpdated());
		assertEquals(compareDate, custWebDealerCust.getDateAdded());
	}

	public CustWebDealerCustDto fillCustWebDealerCustDto2() {
		CustWebDealerCustDto dto2 = new CustWebDealerCustDto();
		dto2.setCustomerId(customerId[1]);
		dto2.setComments(comments[1]);
		dto2.setDlrCustId(dlrCustId);
		dto2.setDlrCustName(dlrCustName);
		dto2.setDlrCustPhoneNbr(dlrCustPhoneNbr);
		dto2.setDlrCustAddress(dlrCustAddress);
		dto2.setDlrCustCity(dlrCustCity);
		dto2.setDlrCustState(dlrCustState);
		dto2.setDlrCustZip(dlrCustZip);
		dto2.setDlrCustCountry(dlrCustCountry);
		dto2.setDlrCustContact(dlrCustContact);
		dto2.setUpdateUser(updateUser);
		dto2.setDateUpdated(updateDate);
		dto2.setDateAdded(updateDate);
		return dto2;
	}

	public void testCustWebDealerCustUpdate2(CustWebDealerCust custWebDealerCust) {
		assertEquals(customerId[1], custWebDealerCust.getCustomerId());
		assertEquals(dlrCustName, custWebDealerCust.getDlrCustName());
		assertEquals(dlrCustId, custWebDealerCust.getDlrCustId());
		assertEquals(dlrCustPhoneNbr,custWebDealerCust.getDlrCustPhoneNbr());
		assertEquals(dlrCustAddress,custWebDealerCust.getDlrCustAddress());
		assertEquals(dlrCustCity, custWebDealerCust.getDlrCustCity());
		assertEquals(dlrCustState, custWebDealerCust.getDlrCustState());
		assertEquals(dlrCustZip, custWebDealerCust.getDlrCustZip());
		assertEquals(dlrCustCountry, custWebDealerCust.getDlrCustCountry());
		assertEquals(dlrCustContact, custWebDealerCust.getDlrCustContact());
		assertEquals(updateUser, custWebDealerCust.getUpdateUser());
		assertEquals(comments[1], custWebDealerCust.getComments());
		assertEquals(compareDate, custWebDealerCust.getDateUpdated());
		assertEquals(compareDate, custWebDealerCust.getDateAdded());
	}

}
